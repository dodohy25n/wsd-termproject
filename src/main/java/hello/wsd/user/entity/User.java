package hello.wsd.user;

import hello.wsd.common.BaseEntity;
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
    private String email;

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
    private UserRole role;

    // 소셜 로그인 정보 (일반 로그인은 null or NONE)
    @Enumerated(EnumType.STRING)
    private SocialProvider socialType;

    // 소셜 식별 값 (예: 카카오의 회원번호)
    private String socialId;
}
