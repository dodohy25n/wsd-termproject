package hello.wsd.domain.coupon.repository;

import hello.wsd.domain.coupon.entity.CouponItem;
import hello.wsd.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CouponItemRepository extends JpaRepository<CouponItem, Long> {
    List<CouponItem> findByItem(Item item);
}
