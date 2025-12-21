package hello.wsd.domain.activity.entity;

import hello.wsd.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StampCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stamp_card_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private String rewardContent; // 예: 아메리카노 1잔 무료

    @Column(nullable = false)
    private int totalSlots; // 총 모아야 할 스탬프 개수

    private boolean isActive;

    public void deactivate() {
        this.isActive = false;
    }

    @Builder
    public StampCard(Store store, String rewardContent, int totalSlots, boolean isActive) {
        this.store = store;
        this.rewardContent = rewardContent;
        this.totalSlots = totalSlots;
        this.isActive = isActive;
    }
}
