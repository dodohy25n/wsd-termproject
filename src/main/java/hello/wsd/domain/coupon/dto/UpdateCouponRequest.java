package hello.wsd.domain.coupon.dto;

import hello.wsd.domain.coupon.entity.CouponStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UpdateCouponRequest {
    private String title;
    private String description;
    private LocalDateTime issueStartsAt;
    private LocalDateTime issueEndsAt;
    private Integer totalQuantity;
    private Integer limitPerUser;
    private CouponStatus status;
}
