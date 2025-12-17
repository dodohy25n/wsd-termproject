package hello.wsd.domain.user.controller;

import hello.wsd.common.util.CookieUtil;
import hello.wsd.domain.user.dto.*;
import hello.wsd.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieUtil cookieUtil;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody SignupRequest request) {
        authService.signUp(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        AuthTokens authTokens = authService.login(request);

        ResponseCookie refreshCookie = cookieUtil.createRefreshTokenCookie(authTokens.getRefreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(LoginResponse.of(authTokens.getAccessToken(), authTokens.getExpiresIn()));
    }

    @PostMapping("/firebase")
    public ResponseEntity<LoginResponse> loginByFirebase(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        AuthTokens authTokens = authService.loginByFirebase(token);

        ResponseCookie refreshCookie = cookieUtil.createRefreshTokenCookie(authTokens.getRefreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(LoginResponse.of(authTokens.getAccessToken(), authTokens.getExpiresIn()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @CookieValue(value = "refreshToken", required = false) String refreshToken) {
        AuthTokens authTokens = authService.refresh(refreshToken);

        // RRT 적용
        ResponseCookie refreshCookie = cookieUtil.createRefreshTokenCookie(authTokens.getRefreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(LoginResponse.of(authTokens.getAccessToken(), authTokens.getExpiresIn()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(value = "refreshToken", required = false) String refreshToken) {
        authService.logout(refreshToken);

        ResponseCookie deleteCookie = cookieUtil.createExpiredCookie("refreshToken");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .build();
    }

    @PostMapping("/complete-social-signup")
    public ResponseEntity<AuthTokens> completeSocialSignup(Long userId, CompleteSocialSignupRequest request) {
        AuthTokens authTokens = authService.completeSocialSignup(userId, request);
        return ResponseEntity.ok()
                .body(authTokens);
    }
}