package hello.wsd.domain.user.repository;

import hello.wsd.domain.user.entity.OwnerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerProfileRepository extends JpaRepository<OwnerProfile, Long> {
}
