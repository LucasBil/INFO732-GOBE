package polytech.idu.models;

import polytech.idu.annotations.Column;
import polytech.idu.annotations.Table;

@Table(name = "University")
public class University {
    @Column(name = "id", type = "INTEGER", primary = true, autoIncrement = true)
    protected int id;

    @Column(name="name", type="TEXT")
    protected String name;

    @Column(name="city", type="TEXT")
    protected String city;
    
    public University() {}

    public University(int id, String name, String city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "University [name=" + name + ", city=" + city + "]";
    }
}
