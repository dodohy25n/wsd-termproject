package hello.wsd.domain.activity.entity;

import hello.wsd.common.entity.BaseEntity;
import hello.wsd.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerStamp extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_stamp_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stamp_card_id", nullable = false)
    private StampCard stampCard;

    private int currentCount = 0;

    @Builder
    public CustomerStamp(User user, StampCard stampCard, int currentCount) {
        this.user = user;
        this.stampCard = stampCard;
        this.currentCount = currentCount;
    }

    public void addStamp(int count) {
        this.currentCount += count;
    }

    public void reset() {
        this.currentCount = 0;
    }
}
