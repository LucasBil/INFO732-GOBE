package polytech.idu.models;

import polytech.idu.annotations.Column;
import polytech.idu.annotations.ManyToOne;
import polytech.idu.annotations.Table;
import java.util.Date;
import java.util.Objects;

@Table(
    name = "Transfer",
    dependencies = {Profile.class, TimeSlot.class, Currency.class}
)
public class Transaction {
    @Column(name = "id", type = "INTEGER", primary = true, autoIncrement = true)
    protected int id;

    @ManyToOne(
        target = Profile.class,
        columnName = "sender",
        refColumn = "id"
    )
    protected Profile sender;

    @ManyToOne(
        target = TimeSlot.class,
        columnName = "timeslot",
        refColumn = "id"
    )
    protected TimeSlot timeslot;

    @Column(name = "date", type = "DATE")
    protected Date date;

    @ManyToOne(
        target = Currency.class,
        columnName = "currency",
        refColumn = "id"
    )
    protected Currency currency;

    public Transaction() {}
    
    public Transaction(int id, Profile sender, TimeSlot timeslot, Date date, Currency currency) {
        this.id = id;
        this.sender = sender;
        this.timeslot = timeslot;
        this.date = date;
        this.currency = currency;
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
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(sender, that.sender) && Objects.equals(timeslot, that.timeslot) && Objects.equals(date, that.date) && Objects.equals(currency, that.currency);
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
