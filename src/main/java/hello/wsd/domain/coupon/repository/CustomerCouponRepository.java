package hello.wsd.domain.coupon.repository;

import hello.wsd.domain.coupon.entity.CustomerCoupon;
import hello.wsd.domain.user.entity.User;
import hello.wsd.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerCouponRepository extends JpaRepository<CustomerCoupon, Long> {
    Integer countByCouponAndUser(Coupon coupon, User user);

    Long countByCoupon(Coupon coupon);

    List<CustomerCoupon> findByUser(User user);

    Optional<CustomerCoupon> findByIdAndUser(Long id, User user);
}
