package hello.wsd.domain.activity.entity;

import hello.wsd.common.entity.BaseEntity;
import hello.wsd.domain.coupon.entity.CustomerCoupon;
import hello.wsd.domain.store.entity.Store;
import hello.wsd.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_coupon_id", nullable = false, unique = true)
    private CustomerCoupon customerCoupon; // 사용 완료한 쿠폰 ID (실제 구매자 증명 용도)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_review_id")
    private Review parentReview; // 답글인 경우 원본 리뷰 ID

    @Column(nullable = false)
    private int rating;

    @Lob
    private String content;

    private boolean isPrivate;

    public Review(User user, Store store, CustomerCoupon customerCoupon, Review parentReview, int rating,
            String content, boolean isPrivate) {
        this.user = user;
        this.store = store;
        this.customerCoupon = customerCoupon;
        this.parentReview = parentReview;
        this.rating = rating;
        this.content = content;
        this.isPrivate = isPrivate;
    }
}
