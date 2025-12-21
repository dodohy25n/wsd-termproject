package hello.wsd.domain.store.dto;

import hello.wsd.domain.store.entity.StoreCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateStoreRequest {

    private String name;

    private String address;

    private Double latitude;
    private Double longitude;

    private String phoneNumber;

    private String introduction;

    private String operatingHours;

    private StoreCategory storeCategory;
}
