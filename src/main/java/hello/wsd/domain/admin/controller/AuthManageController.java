package hello.wsd.domain.admin.controller;

import hello.wsd.common.response.CommonResponse;
import hello.wsd.domain.admin.dto.RefreshTokenResponse;
import hello.wsd.domain.admin.service.AuthManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Admin", description = "관리자 API")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AuthManageController {
    private final AuthManageService authManageService;

    @Operation(summary = "전체 리프레시 토큰 목록 조회", description = "Redis에 저장된 모든 리프레시 토큰을 조회합니다.")
    @GetMapping("/tokens")
    public ResponseEntity<CommonResponse<List<RefreshTokenResponse>>> getAllRefreshTokens() {
        List<RefreshTokenResponse> tokens = authManageService.getAllTokens();
        return ResponseEntity.ok(CommonResponse.success(tokens));
    }

    @Operation(summary = "특정 사용자 리프레시 토큰 조회", description = "특정 사용자의 리프레시 토큰을 조회합니다.")
    @GetMapping("/tokens/{userId}")
    public ResponseEntity<CommonResponse<String>> getUserRefreshToken(
            @Parameter(description = "사용자 ID") @PathVariable Long userId) {
        String token = authManageService.getToken(userId);
        return ResponseEntity.ok(CommonResponse.success(token));
    }

    @Operation(summary = "특정 사용자 리프레시 토큰 삭제", description = "특정 사용자의 리프레시 토큰을 삭제하여 강제 로그아웃 시킵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "토큰 삭제 성공")
    })
    @DeleteMapping("/tokens/{userId}")
    public ResponseEntity<CommonResponse<Void>> deleteUserRefreshToken(
            @Parameter(description = "사용자 ID") @PathVariable Long userId) {
        authManageService.deleteToken(userId);
        return ResponseEntity.noContent().build();
    }
}
