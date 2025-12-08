package polytech.idu.models;

import polytech.idu.models.enums.TimeSlotStatus;

import java.util.Date;

public class TimeSlot extends Model {
    protected Profile profile;
    protected Advertisement advertisement;
    protected float ammount;
    protected Date date;
    protected TimeSlotStatus status;
    
    public TimeSlot(int id, Profile profile, Advertisement advertisement, float ammount, Date date, TimeSlotStatus status) {
        super(id);
        this.profile = profile;
        this.advertisement = advertisement;
        this.ammount = ammount;
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
    public float getAmmount() {
        return ammount;
    }
    public void setAmmount(float ammount) {
        this.ammount = ammount;
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
    }
}
