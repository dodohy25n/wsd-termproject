package hello.wsd.domain.store.controller;

import hello.wsd.common.response.ApiResponse;
import hello.wsd.common.response.PageResponse;
import hello.wsd.domain.store.dto.CreateStoreRequest;
import hello.wsd.domain.store.dto.StoreResponse;
import hello.wsd.domain.store.dto.UpdateStoreRequest;
import hello.wsd.domain.store.service.StoreService;
import hello.wsd.security.details.PrincipalDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createStore(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody @Valid CreateStoreRequest request) {
        Long storeId = storeService.createStore(principalDetails.getUser(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(storeId));
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<ApiResponse<StoreResponse>> getStore(@PathVariable Long storeId) {
        StoreResponse response = storeService.getStore(storeId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<StoreResponse>>> getStores(
            @PageableDefault(size = 10) Pageable pageable) {
        PageResponse<StoreResponse> response = storeService.getStores(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/{storeId}")
    public ResponseEntity<ApiResponse<Void>> updateStore(
            @PathVariable Long storeId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody @Valid UpdateStoreRequest request) {
        storeService.updateStore(storeId, principalDetails.getUser(), request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<ApiResponse<Void>> deleteStore(
            @PathVariable Long storeId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        storeService.deleteStore(storeId, principalDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
