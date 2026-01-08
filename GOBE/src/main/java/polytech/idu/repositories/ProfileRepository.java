package polytech.idu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import polytech.idu.models.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    Profile findByEmail(String email);
}
