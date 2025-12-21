package hello.wsd.domain.coupon.entity;

import hello.wsd.domain.store.entity.Item;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupon_item", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "coupon_id", "item_id" })
})
public class CouponItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Builder
    public CouponItem(Coupon coupon, Item item) {
        this.coupon = coupon;
        this.item = item;
    }
}
