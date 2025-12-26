package hello.wsd.domain.affiliation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import hello.wsd.common.entity.BaseEntity;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Affiliation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "affiliation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id", nullable = false)
    private University university;

    @Column(nullable = false)
    private String category; // 단과대학, 학과, 동아리 등

    @Column(nullable = false)
    private String name; // 공과대학, 소프트웨어공학과 등

    private LocalDateTime expiresAt; // 제휴 만료 시점
}
