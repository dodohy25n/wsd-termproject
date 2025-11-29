package hello.wsd.domain.user.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        long expiresIn
) {}