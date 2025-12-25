package hello.wsd.domain.store.repository;

import hello.wsd.domain.store.entity.Store;
import hello.wsd.domain.store.entity.StoreCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Page<Store> findAll(Pageable pageable);

    boolean existsByName(String name);

    Page<Store> findByNameContaining(String keyword, Pageable pageable);

    Page<Store> findByStoreCategory(StoreCategory category, Pageable pageable);

    Page<Store> findByNameContainingAndStoreCategory(String keyword, StoreCategory category, Pageable pageable);
}
