package hello.wsd.domain.store.dto;

import hello.wsd.domain.store.entity.Store;
import hello.wsd.domain.store.entity.StoreCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreResponse {
    private Long id;
    private Long userId; // Owner ID
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String phoneNumber;
    private String introduction;
    private String operatingHours;
    private StoreCategory storeCategory;

    public static StoreResponse from(Store store) {
        return StoreResponse.builder()
                .id(store.getId())
                .userId(store.getUser().getId())
                .name(store.getName())
                .address(store.getAddress())
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                .phoneNumber(store.getPhoneNumber())
                .introduction(store.getIntroduction())
                .operatingHours(store.getOperatingHours())
                .storeCategory(store.getStoreCategory())
                .build();
    }
}
