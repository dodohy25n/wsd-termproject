package hello.wsd.domain.favorite.dto;

import hello.wsd.domain.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteStoreResponse {

    private Long storeId;
    private String name;
    private String address;
    private String storeCategory;
    private String imageUrl; // 대표 이미지가 있다면 추가, 현재는 없음

    public static FavoriteStoreResponse from(Store store) {
        return FavoriteStoreResponse.builder()
                .storeId(store.getId())
                .name(store.getName())
                .address(store.getAddress())
                .storeCategory(store.getStoreCategory().toString())
                .build();
    }
}
