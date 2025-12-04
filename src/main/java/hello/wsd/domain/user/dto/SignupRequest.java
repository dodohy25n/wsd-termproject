package hello.wsd.domain.user.dto;

import hello.wsd.domain.user.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    private String email;
    private String password;
    private String name;
    private String phoneNumber;

    private Role role;
    private Long universityId; // Customer인 경우
    private String businessNumber; // Owner인 경우
}
