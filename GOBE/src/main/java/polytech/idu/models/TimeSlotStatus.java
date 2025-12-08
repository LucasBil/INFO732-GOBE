package polytech.idu.models;

import polytech.idu.annotations.Column;
import polytech.idu.annotations.Table;

@Table(name = "TimeSlotStatus")
public class TimeSlotStatus {
        @Column(name = "id", type = "INTEGER", primary = true, autoIncrement = true)
    protected int id;

    @Column(name = "name", type = "TEXT")
    protected String name;

    public TimeSlotStatus() {}
    
    public TimeSlotStatus(int id, String name) {
        this.id = id;
        this.name = name;
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
}
