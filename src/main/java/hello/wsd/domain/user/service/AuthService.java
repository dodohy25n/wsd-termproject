// src/main/java/hello/wsd/auth/service/AuthService.java
package hello.wsd.domain.user.service;

import hello.wsd.domain.user.dto.LoginRequest;
import hello.wsd.domain.user.dto.SignupRequest;
import hello.wsd.domain.user.dto.AuthTokens;
import hello.wsd.security.jwt.JwtTokenProvider;
import hello.wsd.security.details.CustomUserDetails;
import hello.wsd.domain.user.entity.Role;
import hello.wsd.domain.user.entity.SocialType;
import hello.wsd.domain.user.entity.User;
import hello.wsd.domain.user.repository.UserRepository;
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

    @Transactional
    public void signUp(SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .role(Role.ROLE_CUSTOMER)
                .socialType(SocialType.LOCAL)
                .build();

        userRepository.save(user);
    }

    @Transactional
    public AuthTokens login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        User user = principal.getUser();

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

    private AuthTokens generateTokenResponse(User user) {

        String accessToken = jwtTokenProvider.createAccessToken(
                user.getId(), user.getUsername(), user.getRole().name()
        );

        String refreshToken = jwtTokenProvider.createRefreshToken(
                user.getId(), user.getUsername(), user.getRole().name()
        );

        refreshTokenService.save(user.getId(), refreshToken);

        return new AuthTokens(accessToken, refreshToken, jwtTokenProvider.getAccessTokenExpiresIn());
    }
}