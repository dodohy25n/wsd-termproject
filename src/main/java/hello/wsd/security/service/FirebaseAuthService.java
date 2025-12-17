package hello.wsd.security.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
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
            return firebaseAuth.verifyIdToken(token);
        } catch (Exception e) {
            log.warn("Firebase Token Verification Failed: {}", e.getMessage());
            throw new IllegalArgumentException("유효하지 않은 Firebase 토큰입니다.");
        }
    }
}
