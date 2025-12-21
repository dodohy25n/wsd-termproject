package hello.wsd.domain.item.dto;

import hello.wsd.domain.item.entity.Item;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemResponse {
    private Long id;
    private Long storeId;
    private String name;
    private int price;
    private String description;
    private String imageUrl;
    private boolean isSoldOut;

    public static ItemResponse from(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .storeId(item.getStore().getId())
                .name(item.getName())
                .price(item.getPrice())
                .description(item.getDescription())
                .imageUrl(item.getImageUrl())
                .isSoldOut(item.isSoldOut())
                .build();
    }
}
