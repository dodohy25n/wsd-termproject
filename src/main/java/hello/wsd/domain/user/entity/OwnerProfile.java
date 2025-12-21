package hello.wsd.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OwnerProfile {

    @Id
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // User의 PK를 이 테이블의 PK이자 FK로 사용
    @JoinColumn(name = "user_id")
    private User user;

    // 사업자 등록 번호
    @Column(unique = true, nullable = false)
    private String businessNumber;

    // 승인 상태
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    @Builder
    public OwnerProfile(User user, String businessNumber, VerificationStatus verificationStatus) {
        this.user = user;
        this.businessNumber = businessNumber;
        this.verificationStatus = verificationStatus != null ? verificationStatus : VerificationStatus.PENDING;
    }
}
