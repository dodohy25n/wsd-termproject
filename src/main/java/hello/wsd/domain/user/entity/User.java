package hello.wsd.domain.user.entity;

import hello.wsd.common.entity.BaseEntity;
import hello.wsd.security.oauth.OAuth2UserInfo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    // 일반 로그인 & 소셜 로그인 공통 식별자 (이메일)
    // 일반 로그인 -> 이메일 형식 / 소셜 로그인 -> provider_id
    @Column(nullable = false, unique = true)
    private String username;

    // 일반 로그인용 (소셜 로그인 사용자는 null)
    private String password;

    // 소셜 로그인 - 일반 로그인 이메일 중복 불가
    @Column(unique = true)
    private String email;

    // 사용자 이름
    @Column(nullable = false)
    private String name;

    // 사용자 전화번호
    private String phoneNumber;

    // 권한 (USER, ADMIN)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // 소셜 로그인 정보
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SocialType socialType = SocialType.LOCAL;

    // 소셜 식별 값 (예: 카카오의 회원번호)
    private String socialId;

    public static User create(String username, String password, String email, String name, String phoneNumber, Role role, SocialType socialType, String socialId) {
        return User.builder()
                .username(username)
                .password(password)
                .email(email)
                .name(name)
                .phoneNumber(phoneNumber)
                .role(role)
                .socialType(socialType)
                .socialId(socialId)
                .build();
    }

    // 소셜 가입 후 부족한 정보 완성 -> ROLE_GUEST에서 승격
    public void completeInsufficientInfo(Role role, String phoneNumber) {
        this.role = role;
        this.phoneNumber = phoneNumber;
    }

    public User updateSocialInfo(OAuth2UserInfo userInfo) {
        if (userInfo.getName() != null && !userInfo.getName().isEmpty()) {
            this.name = userInfo.getName();
        }
        if (userInfo.getEmail() != null && !userInfo.getEmail().isEmpty()) {
            this.email = userInfo.getEmail();
        }
        return this;
    }
}
