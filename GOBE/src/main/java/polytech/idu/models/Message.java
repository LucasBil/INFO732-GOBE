package polytech.idu.models;

import java.util.Date;

<<<<<<< HEAD
public class Message {
    private Profile sender;
    private Profile receiver;
    private String content;
    private Date timestamp;

    public Message(Profile sender, Profile receiver, String content) {
=======
public class Message extends Model {
    protected Profile sender;
    protected Profile receiver;
    protected String text;

    public Message(int id, Profile sender, Profile receiver, String text) {
        super(id);
>>>>>>> dev
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
