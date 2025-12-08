package polytech.idu.models;

public class University extends Model {
    protected String name;
    protected String city;
    
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

    @Override
    public String toString() {
        return "University [name=" + name + ", city=" + city + "]";
    }
}
