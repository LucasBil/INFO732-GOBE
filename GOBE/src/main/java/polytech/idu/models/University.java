package polytech.idu.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "University")
public class University extends Model {

    @Column(name = "name")
    private String name;

    @Column(name = "city")
    private String city;

    public University() {
        super(0);
    }

    public University(int id, String name, String city) {
        super(id);
        this.name = name;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
