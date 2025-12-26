package hello.wsd.domain.affiliation.repository;

import hello.wsd.domain.affiliation.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniversityRepository extends JpaRepository<University, Long> {
}
