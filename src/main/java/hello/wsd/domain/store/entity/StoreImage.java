package hello.wsd.domain.store.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private String imageUrl;

    private boolean isThumbnail = false;

    private int orderIndex = 0;

    @Builder
    public StoreImage(Store store, String imageUrl, boolean isThumbnail, int orderIndex) {
        this.store = store;
        this.imageUrl = imageUrl;
        this.isThumbnail = isThumbnail;
        this.orderIndex = orderIndex;
    }
}
