package polytech.idu.models;

import jakarta.persistence.*;

@Entity
@Table(name = "Service")
@PrimaryKeyJoinColumn(name = "advertisement")
public class Service extends Advertisement {
    public Service() {
        super();
    }

    public Service(int id, String title, String description, Profile holder, float price, java.util.Date date,
            java.util.Date expire, University place,
            float guarantee) {
        super(id, title, description, holder, price, date, expire, place, guarantee);
    }
}
