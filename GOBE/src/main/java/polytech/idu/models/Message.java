package polytech.idu.models;

import java.util.Date;

public class Message {
    private Profile sender;
    private Profile receiver;
    private String content;
    private Date timestamp;

    public Message(Profile sender, Profile receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = new Date();
    }

    public Profile getSender() { return sender; }
    public Profile getReceiver() { return receiver; }
    public String getContent() { return content; }
    public Date getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + sender.getFirstname() + ": " + content;
    }
}
