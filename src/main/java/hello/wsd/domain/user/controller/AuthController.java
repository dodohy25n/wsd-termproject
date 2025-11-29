// src/main/java/hello/wsd/auth/controller/AuthController.java
package hello.wsd.domain;

import hello.wsd.domain.dto.LoginRequest;
import hello.wsd.domain.dto.SignupRequest;
import hello.wsd.domain.dto.TokenResponse;
import hello.wsd.domain.service.AuthService;
import hello.wsd.common.util.CookieUtil;
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
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        TokenResponse tokenResponse = authService.login(request);

        ResponseCookie refreshCookie = cookieUtil.createRefreshTokenCookie(tokenResponse.refreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(tokenResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(
            @CookieValue(value = "refreshToken", required = false) String refreshToken
    ) {
        TokenResponse tokenResponse = authService.refresh(refreshToken);

        ResponseCookie refreshCookie = cookieUtil.createRefreshTokenCookie(tokenResponse.refreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(tokenResponse);
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