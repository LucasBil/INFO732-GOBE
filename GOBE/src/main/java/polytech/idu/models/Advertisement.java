package polytech.idu.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

import polytech.idu.util.StringSanitizer;

public abstract class Advertisement extends Model {
    protected String title;
    protected String description;
    protected Profile holder;
    protected float price;
    protected Date date;
    protected Date expire;
    protected University university;
    protected float guarantee;
    
    
    public String getTitle() {
        return title;
    }
    
    public Advertisement(int id, String title, String description, Profile holder, float price, Date date, Date expire, University university, float guarantee) {
        super(id);
        this.title = title;
        this.description = description;
        this.holder = holder;
        this.price = price;
        this.date = date;
        this.expire = expire;
        this.university = university;
        this.guarantee = guarantee;
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

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public float getGuarantee() {
        return guarantee;
    }

    public void setGuarantee(float guarantee) {
        this.guarantee = guarantee;
    }

    private static final Set<String> STOP_WORDS = Set.of(
        "the","a","an","and","or","is","are","on","in","at","for",
        "to","from","with","of","near","close","very","your","this",
        "that","by","as","be","it","its"
    );


    public ArrayList<String> getKeywords() {
        ArrayList<String> keywords = new ArrayList<>();

        String text = (title + " " + description).toLowerCase();

        String[] words = text.replaceAll("[^a-zA-Z0-9 ]", " ").split("\\s+");

        for (String w : words) {
            if (w.length() < 3) continue; 
            if (STOP_WORDS.contains(w)) continue;
            if (!keywords.contains(w)) keywords.add(StringSanitizer.cleanString(w)); 
        }
        return keywords;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, holder, price, date, expire, university, guarantee);
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
                ", university='" + university + '\'' +
                ", guarantee=" + guarantee +
                '}';
    }
}
