package hello.wsd.security.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import hello.wsd.common.exception.CustomException;
import hello.wsd.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FirebaseAuthService {

    private final FirebaseAuth firebaseAuth;

    public FirebaseToken verifyToken(String token) {
        try {
            return FirebaseAuth.getInstance().verifyIdToken(token);
        } catch (FirebaseAuthException e) {
            log.error("Firebase Token Error: {}", e.getMessage());
            throw new CustomException(ErrorCode.INVALID_TOKEN, "Firebase 토큰 검증에 실패했습니다.");
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN, "토큰 값이 비어있습니다.");
        } catch (Exception e) {
            // 그 외 알 수 없는 오류
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "Firebase 인증 처리 중 오류가 발생했습니다.");
        }
    }
}
