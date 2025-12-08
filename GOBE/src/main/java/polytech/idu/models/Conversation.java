package polytech.idu.models;

import java.util.ArrayList;
import java.util.Date; // Import Date
import java.util.List;

public class Conversation {

    private Profile p1;
    private Profile p2;
    private List<Message> messages;

    public Conversation(Profile p1, Profile p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.messages = new ArrayList<>();
    }

    public boolean involves(Profile p) {
        return p.equals(p1) || p.equals(p2);
    }

    public void sendMessage(Profile sender, String content) {
        Profile receiver = sender.equals(p1) ? p2 : p1;
        
        Message msg = new Message(-1, sender, receiver, content, new Date());
        
        messages.add(msg);
    }

    public List<Message> getMessages() {
        return messages;
    }
}