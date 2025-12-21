package hello.wsd.domain.coupon.controller;

import hello.wsd.common.response.ApiResponse;
import hello.wsd.domain.coupon.dto.CouponResponse;
import hello.wsd.domain.coupon.dto.CreateCouponRequest;
import hello.wsd.domain.coupon.dto.IssueCouponResponse;
import hello.wsd.domain.coupon.dto.UpdateCouponRequest;
import hello.wsd.domain.coupon.service.CouponService;
import hello.wsd.security.details.PrincipalDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    // --- Owner ---
    @PostMapping("/stores/{storeId}/coupons")
    public ResponseEntity<ApiResponse<Long>> createCoupon(
            @PathVariable Long storeId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody @Valid CreateCouponRequest request) {
        Long couponId = couponService.createCoupon(storeId, principalDetails.getUser(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(couponId));
    }

    @PatchMapping("/coupons/{couponId}")
    public ResponseEntity<ApiResponse<Void>> updateCoupon(
            @PathVariable Long couponId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody @Valid UpdateCouponRequest request) {
        couponService.updateCoupon(couponId, principalDetails.getUser(), request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/coupons/{couponId}")
    public ResponseEntity<ApiResponse<Void>> deleteCoupon(
            @PathVariable Long couponId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        couponService.deleteCoupon(couponId, principalDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // --- Public ---
    @GetMapping("/stores/{storeId}/coupons")
    public ResponseEntity<ApiResponse<List<CouponResponse>>> getCouponsByStore(@PathVariable Long storeId) {
        List<CouponResponse> response = couponService.getCouponsByStore(storeId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/items/{itemId}/coupons")
    public ResponseEntity<ApiResponse<List<CouponResponse>>> getCouponsByItem(@PathVariable Long itemId) {
        List<CouponResponse> response = couponService.getCouponsByItem(itemId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // --- Customer ---
    @PostMapping("/coupons/{couponId}/issue")
    public ResponseEntity<ApiResponse<IssueCouponResponse>> issueCoupon(
            @PathVariable Long couponId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        IssueCouponResponse response = couponService.issueCoupon(couponId, principalDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/my-coupons/{customerCouponId}/use")
    public ResponseEntity<ApiResponse<Void>> useCoupon(
            @PathVariable Long customerCouponId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        couponService.useCoupon(customerCouponId, principalDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/my-coupons")
    public ResponseEntity<ApiResponse<List<IssueCouponResponse>>> getMyCoupons(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<IssueCouponResponse> response = couponService.getMyCoupons(principalDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
