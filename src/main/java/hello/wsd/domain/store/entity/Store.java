package hello.wsd.domain.store.entity;

import hello.wsd.common.entity.BaseEntity;
import hello.wsd.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "store", indexes = @Index(name = "idx_store_name", columnList = "name"))
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Owner

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    private Double latitude;
    private Double longitude;

    private String phoneNumber;

    @Lob
    private String introduction;

    @Lob
    private String operatingHours; // JSON String

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoreCategory storeCategory;

    @Builder
    public Store(User user, String name, String address, Double latitude, Double longitude, String phoneNumber,
            String introduction, String operatingHours, StoreCategory storeCategory) {
        this.user = user;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phoneNumber = phoneNumber;
        this.introduction = introduction;
        this.operatingHours = operatingHours;
        this.storeCategory = storeCategory;
    }

    public void updateStore(String name, String address, Double latitude, Double longitude, String phoneNumber,
            String introduction, String operatingHours, StoreCategory storeCategory) {
        if (name != null) {
            this.name = name;
        }
        if (address != null) {
            this.address = address;
        }
        if (latitude != null) {
            this.latitude = latitude;
        }
        if (longitude != null) {
            this.longitude = longitude;
        }
        if (phoneNumber != null) {
            this.phoneNumber = phoneNumber;
        }
        if (introduction != null) {
            this.introduction = introduction;
        }
        if (operatingHours != null) {
            this.operatingHours = operatingHours;
        }
        if (storeCategory != null) {
            this.storeCategory = storeCategory;
        }
    }
}
