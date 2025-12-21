package hello.wsd.domain.store.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Lob
    private String description;

    private String imageUrl;

    private boolean isSoldOut = false;

    @Builder
    public Item(Store store, String name, int price, String description, String imageUrl, boolean isSoldOut) {
        this.store = store;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.isSoldOut = isSoldOut;
    }

    public void updateItem(String name, int price, String description, String imageUrl, boolean isSoldOut) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.isSoldOut = isSoldOut;
    }
}
