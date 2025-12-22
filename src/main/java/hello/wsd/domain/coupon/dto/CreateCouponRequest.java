package hello.wsd.domain.coupon.dto;

import hello.wsd.domain.coupon.entity.CouponStatus;
import hello.wsd.domain.coupon.entity.CouponType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCouponRequest {

    @NotBlank(message = "쿠폰명은 필수입니다.")
    private String title;

    private String description;

    private Long targetAffiliationId;

    private LocalDateTime issueStartsAt;
    private LocalDateTime issueEndsAt;

    @NotNull(message = "총 발행 수량은 필수입니다.")
    private Integer totalQuantity;

    @NotNull(message = "인당 발행 한도는 필수입니다.")
    private Integer limitPerUser;

    @NotNull(message = "쿠폰 타입은 필수입니다.")
    private CouponType type;

    private CouponStatus status; // Optional, default to DRAFT or ACTIVE based on logic

    private List<Long> targetItemIds; // Optional, mapping to items
}
