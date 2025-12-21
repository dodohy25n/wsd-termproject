package hello.wsd.domain.item.controller;

import hello.wsd.common.response.ApiResponse;
import hello.wsd.domain.item.dto.CreateItemRequest;
import hello.wsd.domain.item.dto.ItemResponse;
import hello.wsd.domain.item.dto.UpdateItemRequest;
import hello.wsd.domain.item.service.ItemService;
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
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/stores/{storeId}/items")
    public ResponseEntity<ApiResponse<Long>> createItem(
            @PathVariable Long storeId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody @Valid CreateItemRequest request) {
        Long itemId = itemService.createItem(storeId, principalDetails.getUser(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(itemId));
    }

    @GetMapping("/stores/{storeId}/items")
    public ResponseEntity<ApiResponse<List<ItemResponse>>> getItems(@PathVariable Long storeId) {
        List<ItemResponse> response = itemService.getItems(storeId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse<ItemResponse>> getItem(@PathVariable Long itemId) {
        ItemResponse response = itemService.getItem(itemId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse<Void>> updateItem(
            @PathVariable Long itemId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody @Valid UpdateItemRequest request) {
        itemService.updateItem(itemId, principalDetails.getUser(), request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(
            @PathVariable Long itemId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        itemService.deleteItem(itemId, principalDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
