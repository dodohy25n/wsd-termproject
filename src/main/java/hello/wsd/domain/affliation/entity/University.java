package hello.wsd.domain.affliation.entity;

import jakarta.persistence.*;
import lombok.*;

/*
마스터 테이블 (Admin 관리)
*/
@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "university_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true)
    private String emailDomain;

    @Builder
    public University(String name, String emailDomain) {
        this.name = name;
        this.emailDomain = emailDomain;
    }
}
