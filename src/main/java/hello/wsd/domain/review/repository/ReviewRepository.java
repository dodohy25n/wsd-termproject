package hello.wsd.domain.review.repository;

import hello.wsd.domain.review.entity.Review;
import hello.wsd.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import hello.wsd.domain.user.entity.User;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = { "user" })
    Page<Review> findByStore(Store store, Pageable pageable);

    boolean existsByUserAndStore(User user, Store store);

    Long countByStoreId(Long storeId);

    @org.springframework.data.jpa.repository.Query("SELECT AVG(r.rating) FROM Review r WHERE r.store.id = :storeId")
    Double findAverageRatingByStoreId(@org.springframework.data.repository.query.Param("storeId") Long storeId);

    Long countByStoreIdAndRating(Long storeId, Integer rating);
}
