package polytech.idu.models;

import java.util.Date;
import java.util.Objects;

public abstract class Advertisement extends Model {
    public Advertisement(int id) {
        super(id);
    }

    protected String title;
    protected String description;
    protected Profile holder;
    protected float price;
    protected Date date;
    protected Date expire;
    protected int place;
    protected float guarantee;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Profile getHolder() {
        return holder;
    }

    public void setHolder(Profile holder) {
        this.holder = holder;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public float getGuarantee() {
        return guarantee;
    }

    public void setGuarantee(float guarantee) {
        this.guarantee = guarantee;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass()) return false;
        Advertisement that = (Advertisement) o;
        return Float.compare(price, that.price) == 0 && place == that.place && Float.compare(guarantee, that.guarantee) == 0 && Objects.equals(title, that.title) && Objects.equals(description, that.description) && Objects.equals(holder, that.holder) && Objects.equals(date, that.date) && Objects.equals(expire, that.expire);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, holder, price, date, expire, place, guarantee);
    }

    @Override
    public String toString() {
        return "Advertisement{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", holder=" + holder +
                ", price=" + price +
                ", date=" + date +
                ", expire=" + expire +
                ", place=" + place +
                ", guarantee=" + guarantee +
                '}';
    }
}
