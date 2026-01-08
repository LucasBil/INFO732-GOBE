package polytech.idu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import polytech.idu.models.University;

@Repository
public interface UniversityRepository extends JpaRepository<University, Integer> {
}
