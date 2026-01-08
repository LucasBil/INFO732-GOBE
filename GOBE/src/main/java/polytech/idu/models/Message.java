package polytech.idu.models;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "Message")
public class Message extends Model {
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Profile sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Profile receiver;

    @Column(name = "content")
    private String content;

    @Column(name = "timestamp")
    private Date timestamp;

    public Message() {
        super();
    }

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

    public Profile getSender() {
        return sender;
    }

    public Profile getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + sender.getFirstName() + ": " + content;
    }
}