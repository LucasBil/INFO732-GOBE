package polytech.idu.models;

import polytech.idu.models.enums.TimeSlotStatus;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "TimeSlot")
public class TimeSlot extends Model {
    @ManyToOne
    @JoinColumn(name = "profile_id")
    protected Profile profile;

    @ManyToOne
    @JoinColumn(name = "advertisement_id")
    protected Advertisement advertisement;

    @Column(name = "amount")
    protected float amount;

    @Column(name = "date")
    protected Date date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    protected TimeSlotStatus status;

    public TimeSlot() {
        super();
    }

    public TimeSlot(int id, Profile profile, Advertisement advertisement, float amount, Date date,
            TimeSlotStatus status) {
        super(id);
        this.profile = profile;
        this.advertisement = advertisement;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public TimeSlotStatus getStatus() {
        return status;
    }

    public void setStatus(TimeSlotStatus status) {
        this.status = status;
        this.statusChanged();
    }

    public void statusChanged() {
        // Notify the profile about the status change
        if (this.profile != null) {
            switch (this.status) {
                case PENDING:
                    System.out.println("Le TimeSlot est en attente de validation par l'annonceur.");
                    break;
                case WAITING_PAYMENT:
                    System.out.println("Le TimeSlot est en attente de paiement.");
                    break;
                case AVAILABLE:
                    System.out.println("Le TimeSlot est disponible.");
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public String toString() {
        return "TimeSlot [profile=" + profile + ", advertisement=" + advertisement + ", amount=" + amount + ", date="
                + date + ", status=" + status + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((profile == null) ? 0 : profile.hashCode());
        result = prime * result + ((advertisement == null) ? 0 : advertisement.hashCode());
        result = prime * result + Float.floatToIntBits(amount);
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TimeSlot other = (TimeSlot) obj;
        if (profile == null) {
            if (other.profile != null)
                return false;
        } else if (!profile.equals(other.profile))
            return false;
        if (advertisement == null) {
            if (other.advertisement != null)
                return false;
        } else if (!advertisement.equals(other.advertisement))
            return false;
        if (Float.floatToIntBits(amount) != Float.floatToIntBits(other.amount))
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (status != other.status)
            return false;
        return true;
    }
}
