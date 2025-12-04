package hello.wsd.affliation.repository;

import hello.wsd.affliation.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniversityRepository extends JpaRepository<University, Long> {
}
