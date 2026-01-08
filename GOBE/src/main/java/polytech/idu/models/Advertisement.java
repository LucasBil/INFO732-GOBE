package polytech.idu.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;

import polytech.idu.util.StringSanitizer;

@Entity
@Table(name = "Advertisement")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Advertisement extends Model {
    @Column(name = "title")
    protected String title;

    @Column(name = "description")
    protected String description;

    @ManyToOne
    @JoinColumn(name = "holder")
    protected Profile holder;

    @Column(name = "price")
    protected float price;

    @Column(name = "date")
    protected Date date;

    @Column(name = "expire")
    protected Date expire;

    @ManyToOne
    @JoinColumn(name = "place")
    protected University place;

    @Column(name = "guarantee")
    protected float guarantee;

    @Column(name = "type")
    protected String type;

    public String getTitle() {
        return title;
    }

    public Advertisement(int id, String title, String description, Profile holder, float price, Date date, Date expire,
            University place, float guarantee) {
        super(id);
        this.title = title;
        this.description = description;
        this.holder = holder;
        this.price = price;
        this.date = date;
        this.expire = expire;
        this.place = place;
        this.guarantee = guarantee;
    }

    public Advertisement() {
        super();
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

    public University getPlace() {
        return place;
    }

    public void setPlace(University place) {
        this.place = place;
    }

    public float getGuarantee() {
        return guarantee;
    }

    public void setGuarantee(float guarantee) {
        this.guarantee = guarantee;
    }

    public String getType() {
        if (this.type == null) {
            if (this instanceof polytech.idu.models.Service)
                return "SERVICE";
            if (this instanceof polytech.idu.models.Good)
                return "GOOD";
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private static final Set<String> STOP_WORDS = Set.of(
            "the", "a", "an", "and", "or", "is", "are", "on", "in", "at", "for",
            "to", "from", "with", "of", "near", "close", "very", "your", "this",
            "that", "by", "as", "be", "it", "its");

    public ArrayList<String> getKeywords() {
        ArrayList<String> keywords = new ArrayList<>();

        String text = (title + " " + description).toLowerCase();

        String[] words = text.replaceAll("[^a-zA-Z0-9 ]", " ").split("\\s+");

        for (String w : words) {
            if (w.length() < 3)
                continue;
            if (STOP_WORDS.contains(w))
                continue;
            if (!keywords.contains(w))
                keywords.add(StringSanitizer.cleanString(w));
        }
        return keywords;
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
