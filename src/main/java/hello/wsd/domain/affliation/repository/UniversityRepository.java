package hello.wsd.domain.affliation.repository;

import hello.wsd.domain.affliation.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniversityRepository extends JpaRepository<University, Long> {
}
