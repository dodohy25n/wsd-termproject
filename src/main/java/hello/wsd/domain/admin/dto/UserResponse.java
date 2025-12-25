package hello.wsd.domain.admin.dto;

import hello.wsd.domain.user.entity.Role;
import hello.wsd.domain.user.entity.SocialType;
import hello.wsd.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String name;
    private String phoneNumber;
    private Role role;
    private SocialType socialType;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .socialType(user.getSocialType())
                .build();
    }
}
