package hello.wsd.domain.store.dto;

import hello.wsd.domain.store.entity.Store;
import hello.wsd.domain.store.entity.StoreCategory;
import hello.wsd.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateStoreRequest {

    @NotBlank(message = "가게 이름은 필수입니다.")
    private String name;

    @NotBlank(message = "주소는 필수입니다.")
    private String address;

    private Double latitude;
    private Double longitude;

    private String phoneNumber;

    private String introduction;

    private String operatingHours;

    @NotNull(message = "카테고리는 필수입니다.")
    private StoreCategory storeCategory;

    public Store toEntity(User user) {
        return Store.builder()
                .user(user)
                .name(name)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .phoneNumber(phoneNumber)
                .introduction(introduction)
                .operatingHours(operatingHours)
                .storeCategory(storeCategory)
                .build();
    }
}
