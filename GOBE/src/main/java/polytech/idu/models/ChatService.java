package polytech.idu.models;

import java.util.ArrayList;
import java.util.List;

public class ChatService {

    private List<Conversation> conversations = new ArrayList<>();

    private Conversation getOrCreate(Profile a, Profile b) {
        for (Conversation c : conversations) {
            if (c.involves(a) && c.involves(b)) {
                return c;
            }
        }
        Conversation newConv = new Conversation(a, b);
        conversations.add(newConv);
        return newConv;
    }

    public void send(Profile sender, Profile receiver, String content) {
        Conversation c = getOrCreate(sender, receiver);
        c.sendMessage(sender, content);
    }

    public List<Message> getConversation(Profile a, Profile b) {
        return getOrCreate(a, b).getMessages();
    }
}
