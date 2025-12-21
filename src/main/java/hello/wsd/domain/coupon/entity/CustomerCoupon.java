package hello.wsd.domain.coupon.entity;

import hello.wsd.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_coupon_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Column(nullable = false, unique = true)
    private String couponCode; // 점주 확인용 고유 코드

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponUsageStatus status = CouponUsageStatus.UNUSED;

    private LocalDateTime issuedAt = LocalDateTime.now();

    private LocalDateTime usedAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt; // 쿠폰 만료 시점

    @Builder
    public CustomerCoupon(User user, Coupon coupon, String couponCode, CouponUsageStatus status, LocalDateTime issuedAt,
            LocalDateTime usedAt, LocalDateTime expiresAt) {
        this.user = user;
        this.coupon = coupon;
        this.couponCode = couponCode;
        this.status = status != null ? status : CouponUsageStatus.UNUSED;
        this.issuedAt = issuedAt != null ? issuedAt : LocalDateTime.now();
        this.usedAt = usedAt;
        this.expiresAt = expiresAt;
    }

    public void use() {
        this.status = CouponUsageStatus.USED;
        this.usedAt = LocalDateTime.now();
    }
}
