package hello.wsd.domain.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        long expiresIn
) {}