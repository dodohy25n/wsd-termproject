package hello.wsd.domain.user.dto;

import hello.wsd.domain.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequest {
    private String email;
    private String password;
    private String name;
    private String phoneNumber;

    private Role role;
    private Long universityId; // Customer인 경우
    private String businessNumber; // Owner인 경우
}
