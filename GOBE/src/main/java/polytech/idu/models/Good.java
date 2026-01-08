package polytech.idu.models;

import polytech.idu.models.enums.GoodState;
import polytech.idu.models.enums.GoodStatus;

import java.util.Date;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "Good")
@PrimaryKeyJoinColumn(name = "advertisement")
public class Good extends Advertisement {
    public Good(int id, String title, String description, Profile holder, float price, Date date, Date expire,
            University place,
            float guarantee) {
        super(id, title, description, holder, price, date, expire, place, guarantee);
    }

    public Good() {
        super();
    }

    protected GoodStatus status;
    protected GoodState state;

    public GoodStatus getStatus() {
        return status;
    }

    public void setStatus(GoodStatus status) {
        this.status = status;
    }

    public GoodState getState() {
        return state;
    }

    public void setState(GoodState state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        Good good = (Good) o;
        return status == good.status && state == good.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), status, state);
    }
}
