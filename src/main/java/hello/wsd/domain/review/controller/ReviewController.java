package hello.wsd.domain.review.controller;

import hello.wsd.common.response.CommonResponse;
import hello.wsd.common.response.PageResponse;
import hello.wsd.common.response.SwaggerErrorResponse;
import hello.wsd.domain.review.dto.CreateReviewRequest;
import hello.wsd.domain.review.dto.ReviewResponse;
import hello.wsd.domain.review.dto.ReviewStatsResponse;
import hello.wsd.domain.review.dto.UpdateReviewRequest;
import hello.wsd.domain.review.service.ReviewService;
import hello.wsd.security.details.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Review", description = "리뷰 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

        private final ReviewService reviewService;

        @Operation(summary = "리뷰 작성", description = "상점에 대한 리뷰를 작성합니다.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "리뷰 작성 성공"),
                        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content(schema = @Schema(implementation = SwaggerErrorResponse.class))),
                        @ApiResponse(responseCode = "404", description = "상점 없음", content = @Content(schema = @Schema(implementation = SwaggerErrorResponse.class))),
                        @ApiResponse(responseCode = "409", description = "이미 작성된 리뷰 존재", content = @Content(schema = @Schema(implementation = SwaggerErrorResponse.class)))
        })
        @PostMapping("/stores/{storeId}/reviews")
        public ResponseEntity<CommonResponse<Long>> createReview(
                        @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails,
                        @Parameter(description = "상점 ID") @PathVariable Long storeId,
                        @RequestBody @Valid CreateReviewRequest request) {
                Long reviewId = reviewService.createReview(principalDetails.getUser(), storeId, request);
                return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(reviewId));
        }

        @Operation(summary = "리뷰 수정", description = "작성한 리뷰를 수정합니다. (본인만 가능)")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "리뷰 수정 성공"),
                        @ApiResponse(responseCode = "403", description = "권한 없음 (본인 리뷰 아님)", content = @Content(schema = @Schema(implementation = SwaggerErrorResponse.class))),
                        @ApiResponse(responseCode = "404", description = "리뷰 없음", content = @Content(schema = @Schema(implementation = SwaggerErrorResponse.class)))
        })
        @PatchMapping("/reviews/{reviewId}")
        public ResponseEntity<CommonResponse<Void>> updateReview(
                        @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails,
                        @Parameter(description = "리뷰 ID") @PathVariable Long reviewId,
                        @RequestBody @Valid UpdateReviewRequest request) {
                reviewService.updateReview(reviewId, principalDetails.getUser(), request);
                return ResponseEntity.ok(CommonResponse.success(null));
        }

        @Operation(summary = "리뷰 삭제", description = "작성한 리뷰를 삭제합니다. (본인만 가능)")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "리뷰 삭제 성공"),
                        @ApiResponse(responseCode = "403", description = "권한 없음 (본인 리뷰 아님)", content = @Content(schema = @Schema(implementation = SwaggerErrorResponse.class))),
                        @ApiResponse(responseCode = "404", description = "리뷰 없음", content = @Content(schema = @Schema(implementation = SwaggerErrorResponse.class)))
        })
        @DeleteMapping("/reviews/{reviewId}")
        public ResponseEntity<CommonResponse<Void>> deleteReview(
                        @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails,
                        @Parameter(description = "리뷰 ID") @PathVariable Long reviewId) {
                reviewService.deleteReview(reviewId, principalDetails.getUser());
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(CommonResponse.success(null));
        }

        @Operation(summary = "상점 리뷰 목록 조회", description = "특정 상점의 리뷰 목록을 페이징하여 조회합니다.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "성공"),
                        @ApiResponse(responseCode = "404", description = "상점 없음", content = @Content(schema = @Schema(implementation = SwaggerErrorResponse.class)))
        })
        @GetMapping("/stores/{storeId}/reviews")
        public ResponseEntity<CommonResponse<PageResponse<ReviewResponse>>> getReviews(
                        @Parameter(description = "상점 ID") @PathVariable Long storeId,
                        @Parameter(description = "페이징 정보") @PageableDefault(size = 10) Pageable pageable) {
                Page<ReviewResponse> reviews = reviewService.getReviews(storeId, pageable);
                return ResponseEntity.ok(CommonResponse.success(PageResponse.from(reviews)));
        }

        @Operation(summary = "상점 리뷰 통계", description = "상점의 평점 평균, 총 리뷰 수, 별점별 개수 분포를 조회합니다.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "통계 조회 성공"),
                        @ApiResponse(responseCode = "404", description = "상점 없음", content = @Content(schema = @Schema(implementation = SwaggerErrorResponse.class)))
        })
        @GetMapping("/stores/{storeId}/reviews/stats")
        public ResponseEntity<CommonResponse<ReviewStatsResponse>> getReviewStats(
                        @Parameter(description = "상점 ID") @PathVariable Long storeId) {
                ReviewStatsResponse response = reviewService.getReviewStats(storeId);
                return ResponseEntity.ok(CommonResponse.success(response));
        }
}
