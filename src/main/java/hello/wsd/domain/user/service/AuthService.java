// src/main/java/hello/wsd/auth/service/AuthService.java
package hello.wsd.domain.user.service;

import com.google.firebase.auth.FirebaseToken;
import hello.wsd.domain.affliation.entity.University;
import hello.wsd.domain.affliation.repository.UniversityRepository;
import hello.wsd.domain.user.dto.CompleteSocialSignupRequest;
import hello.wsd.domain.user.dto.LoginRequest;
import hello.wsd.domain.user.dto.SignupRequest;
import hello.wsd.domain.user.dto.AuthTokens;
import hello.wsd.domain.user.entity.*;
import hello.wsd.domain.user.repository.CustomerProfileRepository;
import hello.wsd.domain.user.repository.OwnerProfileRepository;
import hello.wsd.security.details.PrincipalDetails;
import hello.wsd.security.jwt.JwtTokenProvider;
import hello.wsd.domain.user.repository.UserRepository;
import hello.wsd.security.service.FirebaseAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
    public void signUp(SignupRequest request) {

        if (userRepository.existsByUsername(request.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        User user = User.create(request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getEmail(),
                request.getName(), request.getPhoneNumber(), request.getRole(), SocialType.LOCAL, null);
        userRepository.save(user);

        // 역할에 따른 프로필 저장
        if (request.getRole() == Role.ROLE_CUSTOMER) {
            createCustomerProfile(user, request.getUniversityId());
        } else if (request.getRole() == Role.ROLE_OWNER) {
            createOwnerProfile(user, request.getBusinessNumber());
        }
    }

    @Transactional
    public AuthTokens login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        User user = principal.getUser();

        return generateTokenResponse(user);
    }

    @Transactional
    public AuthTokens loginByFirebase(String firebaseToken) {
        FirebaseToken decodedToken = firebaseAuthService.verifyToken(firebaseToken);
        String uid = decodedToken.getUid();
        String email = decodedToken.getEmail();
        String name = decodedToken.getName();

        // Firebase UID를 username으로 사용 (또는 email)
        // 여기서는 소셜 로그인 규칙에 맞춰 "firebase_{uid}" 형태 권장
        String username = "firebase_" + uid;

        User user = userRepository.findByUsername(username)
                .orElseGet(() -> {
                    // 신규 회원 가입
                    User newUser = User.create(
                            username,
                            null,
                            email,
                            name != null ? name : "Firebase User",
                            null,
                            Role.ROLE_GUEST,
                            SocialType.FIREBASE,
                            uid);
                    return userRepository.save(newUser);
                });

        return generateTokenResponse(user);
    }

    @Transactional
    public AuthTokens refresh(String refreshToken) {
        // 토큰 유효성 검사
        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("유효하지 않은 리프레시 토큰입니다.");
        }

        // 토큰에서 UserId 추출
        Long userId = jwtTokenProvider.getUserId(refreshToken);

        // Redis 비교
        String storedToken = refreshTokenService.getByUserId(userId);
        if (storedToken == null || !storedToken.equals(refreshToken)) {
            refreshTokenService.delete(userId);
            throw new RuntimeException("리프레시 토큰이 만료되었거나 일치하지 않습니다.");
        }

        // 유저 조회 (유일한 DB 조회)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 토큰 재발급 (Rotation)
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
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (user.getRole() != Role.ROLE_GUEST) {
            throw new IllegalStateException("이미 가입 절차가 완료된 회원입니다.");
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
            University university = universityRepository.findById(universityId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 대학입니다."));
            CustomerProfile profile = CustomerProfile.create(user, university);
            customerProfileRepository.save(profile);
        } else {
            // 대학생 아닌 고객 가입
            CustomerProfile profile = CustomerProfile.create(user);
            customerProfileRepository.save(profile);
        }

    }

    private void createOwnerProfile(User user, String businessNumber) {
        OwnerProfile profile = OwnerProfile.create(user, businessNumber);
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