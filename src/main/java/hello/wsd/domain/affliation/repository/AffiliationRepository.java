package hello.wsd.domain.affliation.repository;

import hello.wsd.domain.affliation.entity.Affiliation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AffiliationRepository extends JpaRepository<Affiliation, Long> {
}
