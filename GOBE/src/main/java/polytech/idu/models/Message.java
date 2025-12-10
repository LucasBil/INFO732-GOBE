package polytech.idu.models;

import java.util.Objects;

import polytech.idu.annotations.Column;
import polytech.idu.annotations.ManyToOne;
import polytech.idu.annotations.Table;

@Table(
    name = "Message",
    dependencies = {Profile.class}
)
public class Message {
    @Column(name = "id", type = "INTEGER", primary = true, autoIncrement = true)
    protected int id;

    @ManyToOne(
        target = Profile.class,
        columnName = "sender",
        refColumn = "id"
    )
    protected Profile sender;

    @ManyToOne(
        target = Profile.class,
        columnName = "receiver",
        refColumn = "id"
    )
    protected Profile receiver;

    @Column(name = "text", type = "TEXT")
    protected String text;

    public Message() {}
    
    public Message(int id, Profile sender, Profile receiver, String text) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
