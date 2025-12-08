package polytech.idu.models;

import java.util.Objects;

public class Message extends Model {
    protected Profile sender;
    protected Profile receiver;
    protected String text;

    public Message(int id, Profile sender, Profile receiver, String text) {
        super(id);
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
    }

    public Profile getSender() {
        return sender;
    }

    public void setSender(Profile sender) {
        this.sender = sender;
    }

    public Profile getReceiver() {
        return receiver;
    }

    public void setReceiver(Profile receiver) {
        this.receiver = receiver;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(sender, message.sender) && Objects.equals(receiver, message.receiver) && Objects.equals(text, message.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, receiver, text);
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender=" + sender +
                ", receiver=" + receiver +
                ", text='" + text + '\'' +
                '}';
    }
}
