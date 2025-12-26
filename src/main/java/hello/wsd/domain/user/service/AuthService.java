// src/main/java/hello/wsd/auth/service/AuthService.java
package hello.wsd.domain.user.service;

import com.google.firebase.auth.FirebaseToken;
import hello.wsd.common.exception.CustomException;
import hello.wsd.common.exception.ErrorCode;
import hello.wsd.domain.affiliation.entity.University;
import hello.wsd.domain.affiliation.repository.UniversityRepository;
import hello.wsd.domain.user.dto.AuthTokens;
import hello.wsd.domain.user.dto.CompleteSocialSignupRequest;
import hello.wsd.domain.user.dto.LoginRequest;
import hello.wsd.domain.user.dto.SignupRequest;
import hello.wsd.domain.user.entity.*;
import hello.wsd.domain.user.repository.CustomerProfileRepository;
import hello.wsd.domain.user.repository.OwnerProfileRepository;
import hello.wsd.domain.user.repository.UserRepository;
import hello.wsd.security.details.PrincipalDetails;
import hello.wsd.security.jwt.JwtTokenProvider;
import hello.wsd.security.service.FirebaseAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final OwnerProfileRepository ownerProfileRepository;
    private final CustomerProfileRepository customerProfileRepository;
    private final UniversityRepository universityRepository;
    private final FirebaseAuthService firebaseAuthService;

    @Transactional
    public Long signUp(SignupRequest request) {

        if (userRepository.existsByUsername(request.getEmail())) {
            log.warn("[SignUp] Duplicate email attempt: {}", request.getEmail());
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE, "이미 가입된 이메일 입니다.");
        }

        User user = User.builder()
                .username(request.getEmail())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .socialType(SocialType.LOCAL)
                .build();

        Long id = userRepository.save(user).getId();

        // 역할에 따른 프로필 저장
        if (request.getRole() == Role.ROLE_CUSTOMER) {
            createCustomerProfile(user, request.getUniversityId());
        } else if (request.getRole() == Role.ROLE_OWNER) {
            createOwnerProfile(user, request.getBusinessNumber());
        }

        log.info("[SignUp] User created successfully. userId={}, email={}, role={}", id, user.getEmail(),
                user.getRole());
        return id;
    }

    @Transactional
    public AuthTokens login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
            User user = principal.getUser();

            log.info("[Login] Success. userId={}, email={}", user.getId(), user.getEmail());
            return generateTokenResponse(user);
        } catch (BadCredentialsException e) {
            log.warn("[Login] Failed. Invalid credentials for username: {}", request.getUsername());
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }
    }

    @Transactional
    public AuthTokens loginByFirebase(String firebaseToken) {
        FirebaseToken decodedToken = firebaseAuthService.verifyToken(firebaseToken);

        String uid = decodedToken.getUid();
        String email = decodedToken.getEmail();
        String name = decodedToken.getName();

        if (email == null || email.isBlank()) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "소셜 계정에 이메일 정보가 없습니다.");
        }

        // Firebase UID를 username으로 사용 (또는 email)
        // 여기서는 소셜 로그인 규칙에 맞춰 "firebase_{uid}" 형태 권장
        String username = "firebase_" + uid;

        User user = userRepository.findByUsername(username)
                .orElseGet(() -> {
                    // 없으면 신규 회원 가입
                    User newUser = User.builder()
                            .username(username)
                            .email(email)
                            .name(name)
                            .role(Role.ROLE_GUEST)
                            .socialType(SocialType.FIREBASE)
                            .socialId(uid)
                            .build();
                    log.info("[FirebaseLogin] New social user created. username={}", username);
                    return userRepository.save(newUser);
                });

        log.info("[FirebaseLogin] Success. userId={}", user.getId());
        return generateTokenResponse(user);
    }

    @Transactional
    public AuthTokens refresh(String refreshToken) {
        // 토큰 유효성 검사
        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            log.warn("[RefreshToken] Invalid token provided.");
            throw new CustomException(ErrorCode.INVALID_TOKEN, "유효하지 않은 리프레시 토큰입니다.");
        }

        // 토큰에서 UserId 추출
        Long userId = jwtTokenProvider.getUserId(refreshToken);

        // Redis 비교
        String storedToken = refreshTokenService.getByUserId(userId);
        if (storedToken == null || !storedToken.equals(refreshToken)) {
            log.warn("[RefreshToken] Token mismatch or not found in Redis. userId={}", userId);
            refreshTokenService.delete(userId);
            throw new CustomException(ErrorCode.INVALID_TOKEN, "리프레시 토큰이 만료되었거나 일치하지 않습니다.");
        }

        // 유저 조회 (유일한 DB 조회)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 토큰 재발급 (Rotation)
        log.info("[RefreshToken] Success. Tokens rotated for userId={}", userId);
        return generateTokenResponse(user);
    }

    @Transactional
    public void logout(String refreshToken) {
        if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
            Long userId = jwtTokenProvider.getUserId(refreshToken);
            refreshTokenService.delete(userId);
        }
    }

    @Transactional
    public AuthTokens completeSocialSignup(Long userId, CompleteSocialSignupRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (user.getRole() != Role.ROLE_GUEST) {
            throw new CustomException(ErrorCode.STATE_CONFLICT, "이미 가입이 완료된 회원입니다.");
        }

        user.completeInsufficientInfo(request.getRole(), request.getPhoneNumber());

        if (request.getRole() == Role.ROLE_CUSTOMER) {
            createCustomerProfile(user, request.getUniversityId());
        } else if (request.getRole() == Role.ROLE_OWNER) {
            createOwnerProfile(user, request.getBusinessNumber());
        }

        // 변경된 Role로 토큰 재발급
        return generateTokenResponse(user);
    }

    private void createCustomerProfile(User user, Long universityId) {
        if (universityId != null) {
            University university = universityRepository.findById(universityId)
                    .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "해당 대학을 찾을 수 없습니다."));
            CustomerProfile profile = CustomerProfile.builder().user(user).university(university).build();
            customerProfileRepository.save(profile);
        } else {
            // 대학생 아닌 고객 가입
            CustomerProfile profile = CustomerProfile.builder().user(user).build();
            customerProfileRepository.save(profile);
        }

    }

    private void createOwnerProfile(User user, String businessNumber) {
        OwnerProfile profile = OwnerProfile.builder().user(user).businessNumber(businessNumber).build();
        ownerProfileRepository.save(profile);
    }

    private AuthTokens generateTokenResponse(User user) {

        String accessToken = jwtTokenProvider.createAccessToken(
                user.getId(), user.getUsername(), user.getRole().name());

        String refreshToken = jwtTokenProvider.createRefreshToken(
                user.getId(), user.getUsername(), user.getRole().name());

        refreshTokenService.save(user.getId(), refreshToken);

        return new AuthTokens(accessToken, refreshToken, jwtTokenProvider.getAccessTokenExpiresIn());
    }
}