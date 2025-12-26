package hello.wsd.domain.affiliation.repository;

import hello.wsd.domain.affiliation.entity.Affiliation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AffiliationRepository extends JpaRepository<Affiliation, Long> {
}
