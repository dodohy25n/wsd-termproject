package hello.wsd.domain.review.repository;

import hello.wsd.domain.review.entity.Review;
import hello.wsd.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByStore(Store store, Pageable pageable);
}
