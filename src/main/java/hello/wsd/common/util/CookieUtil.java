package hello.wsd.common.util;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    // 쿠키 만료 시간 (14일)
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 14 * 24 * 60 * 60;

    public ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .path("/api/auth")      // 특정 경로에서만 전송
                .httpOnly(true)         // 자바스크립트 접근 방지 (XSS 방어)
                .secure(true)           // HTTPS 전송 (로컬 개발시 false 고려 필요)
                .maxAge(REFRESH_TOKEN_EXPIRE_TIME)
                .sameSite("None")       // 서로 다른 도메인 간 요청 시 필수 (CORS)
                .build();
    }

    public ResponseCookie createExpiredCookie(String cookieName) {
        return ResponseCookie.from(cookieName, "")
                .path("/api/auth")
                .maxAge(0)              // 즉시 만료
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .build();
    }
}
