package polytech.idu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytech.idu.models.Message;
import polytech.idu.models.Profile;
import polytech.idu.repositories.MessageRepository;

import java.util.Date;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message sendMessage(Profile sender, Profile receiver, String content) {
        Message msg = new Message(-1, sender, receiver, content, new Date());
        return messageRepository.save(msg);
    }

    public List<Message> getConversation(Profile p1, Profile p2) {
        // Fetch both directions
        List<Message> dir1 = messageRepository.findBySenderAndReceiver(p1, p2);
        List<Message> dir2 = messageRepository.findByReceiverAndSender(p1, p2);
        dir1.addAll(dir2);
        // Sort by date (timestamp)
        dir1.sort((m1, m2) -> m1.getTimestamp().compareTo(m2.getTimestamp()));
        return dir1;
    }

    public java.util.Set<Profile> getConversationPartners(Profile me) {
        List<Message> sent = messageRepository.findBySender(me);
        List<Message> received = messageRepository.findByReceiver(me);

        java.util.Set<Profile> partners = new java.util.HashSet<>();
        for (Message m : sent)
            partners.add(m.getReceiver());
        for (Message m : received)
            partners.add(m.getSender());

        return partners;
    }
}