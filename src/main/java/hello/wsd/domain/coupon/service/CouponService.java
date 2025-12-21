package hello.wsd.domain.coupon.service;

import hello.wsd.common.exception.CustomException;
import hello.wsd.common.exception.ErrorCode;
import hello.wsd.domain.affliation.entity.Affiliation;
import hello.wsd.domain.affliation.repository.AffiliationRepository;
import hello.wsd.domain.coupon.dto.*;
import hello.wsd.domain.coupon.entity.*;
import hello.wsd.domain.coupon.repository.*;
import hello.wsd.domain.item.entity.Item;
import hello.wsd.domain.item.repository.ItemRepository;
import hello.wsd.domain.store.entity.Store;
import hello.wsd.domain.store.repository.StoreRepository;
import hello.wsd.domain.user.entity.User;
import hello.wsd.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {

    private final CouponRepository couponRepository;
    private final CustomerCouponRepository customerCouponRepository;
    private final CouponItemRepository couponItemRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final AffiliationRepository affiliationRepository;

    // --- Owner Operations ---

    @Transactional
    public Long createCoupon(Long storeId, User user, CreateCouponRequest request) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "가게를 찾을 수 없습니다."));

        validateStoreOwner(store, user);

        Affiliation affiliation = null;
        if (request.getTargetAffiliationId() != null) {
            affiliation = affiliationRepository.findById(request.getTargetAffiliationId())
                    .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "제휴 정보를 찾을 수 없습니다."));
        }

        Coupon coupon = Coupon.builder()
                .store(store)
                .title(request.getTitle())
                .description(request.getDescription())
                .targetAffiliation(affiliation)
                .issueStartsAt(request.getIssueStartsAt())
                .issueEndsAt(request.getIssueEndsAt())
                .totalQuantity(request.getTotalQuantity())
                .limitPerUser(request.getLimitPerUser())
                .type(request.getType())
                .status(request.getStatus() != null ? request.getStatus() : CouponStatus.ACTIVE)
                .build();

        Coupon savedCoupon = couponRepository.save(coupon);

        if (request.getTargetItemIds() != null && !request.getTargetItemIds().isEmpty()) {
            List<Item> items = itemRepository.findAllById(request.getTargetItemIds());
            for (Item item : items) {
                // Verify item belongs to store
                if (!item.getStore().getId().equals(store.getId())) {
                    throw new CustomException(ErrorCode.BAD_REQUEST, "다른 가게의 상품이 포함되어 있습니다.");
                }
                CouponItem couponItem = CouponItem.builder()
                        .coupon(savedCoupon)
                        .item(item)
                        .build();
                couponItemRepository.save(couponItem);
            }
        }

        return savedCoupon.getId();
    }

    @Transactional
    public void updateCoupon(Long couponId, User user, UpdateCouponRequest request) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "쿠폰을 찾을 수 없습니다."));

        validateStoreOwner(coupon.getStore(), user);

        coupon.updateCoupon(
                request.getTitle(),
                request.getDescription(),
                request.getIssueStartsAt(),
                request.getIssueEndsAt(),
                request.getTotalQuantity(),
                request.getLimitPerUser(),
                request.getStatus());
    }

    @Transactional
    public void deleteCoupon(Long couponId, User user) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "쿠폰을 찾을 수 없습니다."));

        validateStoreOwner(coupon.getStore(), user);

        couponRepository.delete(coupon);
    }

    // --- Public Operations ---

    public List<CouponResponse> getCouponsByStore(Long storeId) {
        return couponRepository.findByStoreId(storeId).stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());
    }

    public List<CouponResponse> getCouponsByItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "상품을 찾을 수 없습니다."));

        return couponItemRepository.findByItem(item).stream()
                .map(CouponItem::getCoupon)
                .map(CouponResponse::from)
                .collect(Collectors.toList());
    }

    // --- Customer Operations ---

    @Transactional
    public IssueCouponResponse issueCoupon(Long couponId, User user) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "쿠폰을 찾을 수 없습니다."));

        // Validation
        if (coupon.getStatus() != CouponStatus.ACTIVE) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "발급 가능한 상태가 아닙니다.");
        }
        LocalDateTime now = LocalDateTime.now();
        if (coupon.getIssueStartsAt() != null && now.isBefore(coupon.getIssueStartsAt())) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "발급 기간이 아닙니다.");
        }
        if (coupon.getIssueEndsAt() != null && now.isAfter(coupon.getIssueEndsAt())) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "발급 기간이 지났습니다.");
        }

        Integer userCount = customerCouponRepository.countByCouponAndUser(coupon, user);
        if (userCount >= coupon.getLimitPerUser()) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "인당 발급 한도를 초과했습니다.");
        }

        String code = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        CustomerCoupon customerCoupon = CustomerCoupon.builder()
                .user(user)
                .coupon(coupon)
                .couponCode(code)
                .status(CouponUsageStatus.UNUSED)
                .expiresAt(now.plusDays(30))
                .build();

        customerCouponRepository.save(customerCoupon);

        return IssueCouponResponse.from(customerCoupon);
    }

    @Transactional
    public void useCoupon(Long customerCouponId, User user) {
        CustomerCoupon customerCoupon = customerCouponRepository.findByIdAndUser(customerCouponId, user)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "쿠폰을 찾을 수 없습니다."));

        if (customerCoupon.getStatus() == CouponUsageStatus.USED) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "이미 사용된 쿠폰입니다.");
        }

        if (customerCoupon.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "만료된 쿠폰입니다.");
        }

        customerCoupon.use();
    }

    public List<IssueCouponResponse> getMyCoupons(User user) {
        return customerCouponRepository.findByUser(user).stream()
                .map(IssueCouponResponse::from)
                .collect(Collectors.toList());
    }

    private void validateStoreOwner(Store store, User user) {
        User owner = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!Objects.equals(store.getUser().getId(), owner.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN, "가게 주인이 아닙니다.");
        }
    }
}
