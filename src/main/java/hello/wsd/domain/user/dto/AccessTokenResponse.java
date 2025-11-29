package hello.wsd.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccessTokenResponse {
    private String accessToken;
    private Long expiresIn;

    public static AccessTokenResponse of(String accessToken, long expiresIn) {
        return new AccessTokenResponse(accessToken, expiresIn);
    }
}
