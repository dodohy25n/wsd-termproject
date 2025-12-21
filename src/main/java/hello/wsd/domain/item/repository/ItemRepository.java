package hello.wsd.domain.item.repository;

import hello.wsd.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByStoreId(Long storeId);
}
