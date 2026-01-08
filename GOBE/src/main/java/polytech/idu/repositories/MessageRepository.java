package polytech.idu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import polytech.idu.models.Message;
import polytech.idu.models.Profile;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findBySenderAndReceiver(Profile sender, Profile receiver);

    List<Message> findByReceiverAndSender(Profile receiver, Profile sender);

    List<Message> findBySender(Profile sender);

    List<Message> findByReceiver(Profile receiver);
}
