package polytech.idu.models;

import polytech.idu.models.enums.GoodState;
import polytech.idu.models.enums.GoodStatus;

import java.util.Objects;

public class Good extends Advertisement {
    protected GoodStatus status;
    protected GoodState state;

    public Good(int id, GoodStatus status, GoodState state) {
        super(id);
        this.status = status;
        this.state = state;
    }

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
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Good good = (Good) o;
        return status == good.status && state == good.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), status, state);
    }
}
