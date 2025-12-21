package hello.wsd.domain.coupon.dto;

import hello.wsd.domain.coupon.entity.Coupon;
import hello.wsd.domain.coupon.entity.CouponStatus;
import hello.wsd.domain.coupon.entity.CouponType;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class CouponResponse {
    private Long id;
    private Long storeId;
    private String title;
    private String description;
    private Long targetAffiliationId;
    private LocalDateTime issueStartsAt;
    private LocalDateTime issueEndsAt;
    private Integer totalQuantity;
    private Integer limitPerUser;
    private CouponType type;
    private CouponStatus status;

    public static CouponResponse from(Coupon coupon) {
        return CouponResponse.builder()
                .id(coupon.getId())
                .storeId(coupon.getStore().getId())
                .title(coupon.getTitle())
                .description(coupon.getDescription())
                .targetAffiliationId(
                        coupon.getTargetAffiliation() != null ? coupon.getTargetAffiliation().getId() : null)
                .issueStartsAt(coupon.getIssueStartsAt())
                .issueEndsAt(coupon.getIssueEndsAt())
                .totalQuantity(coupon.getTotalQuantity())
                .limitPerUser(coupon.getLimitPerUser())
                .type(coupon.getType())
                .status(coupon.getStatus())
                .build();
    }
}
