package hello.wsd.domain.store.dto;

import hello.wsd.domain.store.entity.StoreCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
