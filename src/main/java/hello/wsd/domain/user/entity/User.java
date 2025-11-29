package hello.wsd.user.entity;

import hello.wsd.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 일반 로그인 & 소셜 로그인 공통 식별자 (이메일)
    @Column(nullable = false, unique = true)
    private String username;

    // 일반 로그인용 (소셜 로그인 사용자는 null)
    private String password;

    // 사용자 이름
    @Column(nullable = false)
    private String name;

    // 사용자 전화번호
    @Column(nullable = false)
    private String phoneNumber;

    // 권한 (USER, ADMIN)
    @Enumerated(EnumType.STRING)
    private Role role;

    // 소셜 로그인 정보 (일반 로그인은 null or NONE)
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    // 소셜 식별 값 (예: 카카오의 회원번호)
    private String socialId;

    public static User create(String username, String encode, String name, String phoneNumber, Role role) {
        User user = new User();
        user.username = username;
        user.password = encode;
        user.name = name;
        user.phoneNumber = phoneNumber;
        user.role = role;
        return user;
    }
}
