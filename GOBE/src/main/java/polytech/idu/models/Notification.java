package polytech.idu.models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Notification")
public class Notification extends Model {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Profile user;

    @Column(name = "message")
    private String message;

    @Column(name = "is_read")
    private boolean isRead;

    @Column(name = "created_at")
    private Date createdAt;

    public Notification() {
        super();
        this.createdAt = new Date();
        this.isRead = false;
    }

    public Notification(Profile user, String message) {
        super(0);
        this.user = user;
        this.message = message;
        this.isRead = false;
        this.createdAt = new Date();
    }

    public Profile getUser() {
        return user;
    }

    public void setUser(Profile user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
