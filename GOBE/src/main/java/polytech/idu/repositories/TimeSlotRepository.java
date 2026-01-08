package polytech.idu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import polytech.idu.models.TimeSlot;
import polytech.idu.models.Profile;
import polytech.idu.models.Advertisement;
import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Integer> {
    List<TimeSlot> findByProfile(Profile profile);

    List<TimeSlot> findByAdvertisement(Advertisement advertisement);
}
