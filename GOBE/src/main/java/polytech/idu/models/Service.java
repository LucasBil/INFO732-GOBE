package polytech.idu.models;

public class Service extends Advertisement {
    public Service(int id, String title, String description, Profile holder, float price, java.util.Date date, java.util.Date expire, University university,
            float guarantee) {
        super(id, title, description, holder, price, date, expire, university, guarantee);
    }
}
