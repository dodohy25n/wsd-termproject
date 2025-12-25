package hello.wsd.domain.admin.controller;

import hello.wsd.common.response.CommonResponse;
import hello.wsd.common.response.PageResponse;
import hello.wsd.domain.admin.dto.UserResponse;
import hello.wsd.domain.admin.dto.UserRoleUpdateRequest;
import hello.wsd.domain.admin.service.UserManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin - User", description = "관리자 사용자 관리 API")
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserManageController {

    private final UserManageService userManageService;

    @Operation(summary = "전체 사용자 목록 조회", description = "가입된 모든 사용자를 페이징하여 조회합니다.")
    @GetMapping
    public ResponseEntity<CommonResponse<PageResponse<UserResponse>>> getAllUsers(
            @Parameter(description = "페이징 정보") @PageableDefault(size = 10) Pageable pageable) {
        PageResponse<UserResponse> response = userManageService.getAllUsers(pageable);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @Operation(summary = "사용자 권한 수정", description = "사용자의 권한을 수정합니다.")
    @PatchMapping("/{userId}/role")
    public ResponseEntity<CommonResponse<Void>> updateUserRole(
            @PathVariable Long userId,
            @RequestBody @Valid UserRoleUpdateRequest request) {
        userManageService.updateUserRole(userId, request);
        return ResponseEntity.ok(CommonResponse.success(null));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "사용자 강제 탈퇴 성공")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<CommonResponse<Void>> deleteUser(@PathVariable Long userId) {
        userManageService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
