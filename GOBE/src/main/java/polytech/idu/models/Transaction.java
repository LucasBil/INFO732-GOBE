package polytech.idu.models;

import polytech.idu.models.enums.Currency;
import java.util.Date;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "Transaction")
public class Transaction extends Model {
    @ManyToOne
    @JoinColumn(name = "sender_id")
    protected Profile sender;

    @ManyToOne
    @JoinColumn(name = "timeslot_id")
    protected TimeSlot timeslot;

    @Column(name = "date")
    protected Date date;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    protected Currency currency;

    public Transaction() {
        super();
    }

    public Transaction(int id, Profile sender, TimeSlot timeslot, Date date, Currency currency) {
        super(id);
        this.sender = sender;
        this.timeslot = timeslot;
        this.date = date;
        this.currency = currency;
    }

    public Profile getSender() {
        return sender;
    }

    public void setSender(Profile sender) {
        this.sender = sender;
    }

    public TimeSlot getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(TimeSlot timeslot) {
        this.timeslot = timeslot;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        Transaction that = (Transaction) o;
        return Objects.equals(sender, that.sender) && Objects.equals(timeslot, that.timeslot)
                && Objects.equals(date, that.date) && Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, timeslot, date, currency);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "sender=" + sender +
                ", timeslot=" + timeslot +
                ", date=" + date +
                ", currency=" + currency +
                '}';
    }
}
