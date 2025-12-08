package polytech.idu.models;

import java.util.Date;

public class Message extends Model {
    private Profile sender;
    private Profile receiver;
    private String content;
    private Date timestamp;

    // Full constructor
    public Message(int id, Profile sender, Profile receiver, String content, Date timestamp) {
        super(id);
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Message(Profile sender, Profile receiver, String content) {
        super(-1); 
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = new Date();
    }

    public Profile getSender() { return sender; }
    public Profile getReceiver() { return receiver; }
    public String getContent() { return content; }
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + sender.getFirstname() + ": " + content;
    }
}