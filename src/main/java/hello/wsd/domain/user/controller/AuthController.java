// src/main/java/hello/wsd/auth/controller/AuthController.java
package hello.wsd.domain.user.controller;

import hello.wsd.common.util.CookieUtil;
import hello.wsd.domain.user.dto.LoginRequest;
import hello.wsd.domain.user.dto.AccessTokenResponse;
import hello.wsd.domain.user.dto.SignupRequest;
import hello.wsd.domain.user.dto.TokenResponse;
import hello.wsd.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<AccessTokenResponse> login(@RequestBody LoginRequest request) {
        TokenResponse tokenResponse = authService.login(request);

        ResponseCookie refreshCookie = cookieUtil.createRefreshTokenCookie(tokenResponse.getRefreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(AccessTokenResponse.of(tokenResponse.getAccessToken(), tokenResponse.getExpiresIn()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponse> refresh(
            @CookieValue(value = "refreshToken", required = false) String refreshToken
    ) {
        TokenResponse tokenResponse = authService.refresh(refreshToken);

        // RRT 적용
        ResponseCookie refreshCookie = cookieUtil.createRefreshTokenCookie(tokenResponse.getRefreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(AccessTokenResponse.of(tokenResponse.getAccessToken(), tokenResponse.getExpiresIn()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(value = "refreshToken", required = false) String refreshToken
    ) {
        authService.logout(refreshToken);

        ResponseCookie deleteCookie = cookieUtil.createExpiredCookie("refreshToken");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .build();
    }
}