package hello.wsd.domain.store.controller;

import hello.wsd.common.response.SwaggerErrorResponse;
import hello.wsd.common.response.CommonResponse;
import hello.wsd.common.response.PageResponse;
import hello.wsd.domain.store.dto.CreateStoreRequest;
import hello.wsd.domain.store.dto.StoreResponse;
import hello.wsd.domain.store.dto.UpdateStoreRequest;
import hello.wsd.domain.store.service.StoreService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Store", description = "상점 관련 API")
@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

        private final StoreService storeService;

        @Operation(summary = "상점 등록", description = "새로운 상점을 등록합니다. (점주 권한 필요)")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "상점 등록 성공"),
                        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content(schema = @Schema(implementation = SwaggerErrorResponse.class))),
                        @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content(schema = @Schema(implementation = SwaggerErrorResponse.class)))
        })
        @PostMapping
        public ResponseEntity<CommonResponse<Long>> createStore(
                        @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails,
                        @RequestBody @Valid CreateStoreRequest request) {
                Long storeId = storeService.createStore(principalDetails.getUser(), request);
                return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(storeId));
        }

        @Operation(summary = "상점 단건 조회", description = "상점 ID로 상점의 상세 정보를 조회합니다.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "상점 조회 성공"),
                        @ApiResponse(responseCode = "404", description = "상점 없음", content = @Content(schema = @Schema(implementation = SwaggerErrorResponse.class)))
        })
        @GetMapping("/{storeId}")
        public ResponseEntity<CommonResponse<StoreResponse>> getStore(
                        @Parameter(description = "상점 ID") @PathVariable Long storeId) {
                StoreResponse response = storeService.getStore(storeId);
                return ResponseEntity.ok(CommonResponse.success(response));
        }

        @Operation(summary = "상점 목록 조회", description = "전체 상점 목록을 페이징하여 조회합니다.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "상점 목록 조회 성공")
        })
        @GetMapping
        public ResponseEntity<CommonResponse<PageResponse<StoreResponse>>> getStores(
                        @Parameter(description = "페이징 정보 (page, size, sort)") @PageableDefault(size = 10) Pageable pageable) {
                PageResponse<StoreResponse> response = storeService.getStores(pageable);
                return ResponseEntity.ok(CommonResponse.success(response));
        }

        @Operation(summary = "상점 정보 수정", description = "상점 정보를 수정합니다. (본인 상점만 가능)")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "상점 수정 성공"),
                        @ApiResponse(responseCode = "403", description = "권한 없음 (본인 소유 상점 아님)", content = @Content(schema = @Schema(implementation = SwaggerErrorResponse.class))),
                        @ApiResponse(responseCode = "404", description = "상점 없음", content = @Content(schema = @Schema(implementation = SwaggerErrorResponse.class)))
        })
        @PatchMapping("/{storeId}")
        public ResponseEntity<CommonResponse<Void>> updateStore(
                        @Parameter(description = "상점 ID") @PathVariable Long storeId,
                        @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails,
                        @RequestBody @Valid UpdateStoreRequest request) {
                storeService.updateStore(storeId, principalDetails.getUser(), request);
                return ResponseEntity.ok(CommonResponse.success(null));
        }

        @Operation(summary = "상점 삭제", description = "상점을 삭제합니다. (본인 상점만 가능)")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "상점 삭제 성공"),
                        @ApiResponse(responseCode = "403", description = "권한 없음 (본인 소유 상점 아님)", content = @Content(schema = @Schema(implementation = SwaggerErrorResponse.class))),
                        @ApiResponse(responseCode = "404", description = "상점 없음", content = @Content(schema = @Schema(implementation = SwaggerErrorResponse.class)))
        })
        @DeleteMapping("/{storeId}")
        public ResponseEntity<CommonResponse<Void>> deleteStore(
                        @Parameter(description = "상점 ID") @PathVariable Long storeId,
                        @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principalDetails) {
                storeService.deleteStore(storeId, principalDetails.getUser());
                return ResponseEntity.ok(CommonResponse.success(null));
        }
}