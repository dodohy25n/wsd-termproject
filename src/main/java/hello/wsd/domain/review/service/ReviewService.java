package hello.wsd.domain.review.service;

import hello.wsd.common.exception.CustomException;
import hello.wsd.common.exception.ErrorCode;
import hello.wsd.domain.review.dto.CreateReviewRequest;
import hello.wsd.domain.review.dto.ReviewResponse;
import hello.wsd.domain.review.dto.ReviewStatsResponse;
import hello.wsd.domain.review.dto.UpdateReviewRequest;
import hello.wsd.domain.review.entity.Review;
import hello.wsd.domain.review.repository.ReviewRepository;
import hello.wsd.domain.store.entity.Store;
import hello.wsd.domain.store.repository.StoreRepository;
import hello.wsd.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public Long createReview(User user, Long storeId, CreateReviewRequest request) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        if (reviewRepository.existsByUserAndStore(user, store)) {
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE, "이미 해당 상점에 대한 리뷰를 작성했습니다.");
        }

        Review review = Review.builder()
                .user(user)
                .store(store)
                .content(request.getContent())
                .rating(request.getRating())
                .customerCoupon(null)
                .parentReview(null)
                .isPrivate(false)
                .build();

        reviewRepository.save(review);
        return review.getId();
    }

    @Transactional
    public void updateReview(Long reviewId, User user, UpdateReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        review.updateReview(request.getContent(), request.getRating());
    }

    @Transactional
    public void deleteReview(Long reviewId, User user) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        reviewRepository.delete(review);
    }

    public Page<ReviewResponse> getReviews(Long storeId, Pageable pageable) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        return reviewRepository.findByStore(store, pageable)
                .map(ReviewResponse::from);
    }

    public ReviewStatsResponse getReviewStats(Long storeId) {
        Double avgRating = reviewRepository.findAverageRatingByStoreId(storeId);
        Long totalReviews = reviewRepository.countByStoreId(storeId);
        Long rating1 = reviewRepository.countByStoreIdAndRating(storeId, 1);
        Long rating2 = reviewRepository.countByStoreIdAndRating(storeId, 2);
        Long rating3 = reviewRepository.countByStoreIdAndRating(storeId, 3);
        Long rating4 = reviewRepository.countByStoreIdAndRating(storeId, 4);
        Long rating5 = reviewRepository.countByStoreIdAndRating(storeId, 5);

        return ReviewStatsResponse.builder()
                .averageRating(avgRating != null ? avgRating : 0.0)
                .totalReviews(totalReviews != null ? totalReviews : 0L)
                .rating1Count(rating1 != null ? rating1 : 0L)
                .rating2Count(rating2 != null ? rating2 : 0L)
                .rating3Count(rating3 != null ? rating3 : 0L)
                .rating4Count(rating4 != null ? rating4 : 0L)
                .rating5Count(rating5 != null ? rating5 : 0L)
                .build();
    }
}
