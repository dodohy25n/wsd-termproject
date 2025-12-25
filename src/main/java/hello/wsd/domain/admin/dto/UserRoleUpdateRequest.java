package hello.wsd.domain.admin.dto;

import hello.wsd.domain.user.entity.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRoleUpdateRequest {

    @NotNull(message = "Role is required")
    private Role role;
}
