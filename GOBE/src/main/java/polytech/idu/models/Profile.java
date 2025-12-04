package polytech.idu.models;

import java.util.Date;
import java.util.Objects;

import polytech.idu.models.enums.TimeSlotStatus;

public class Profile {
    protected String firstname;
    protected String lastname;
    protected Date birthdate;
    protected String email;
    protected String city;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Profile(String firstname, String lastname, Date birthdate, String email, String city) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.email = email;
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Objects.equals(firstname, profile.firstname) && Objects.equals(lastname, profile.lastname) && Objects.equals(birthdate, profile.birthdate) && Objects.equals(email, profile.email) && Objects.equals(city, profile.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname, birthdate, email, city);
    }

    @Override
    public String toString() {
        return "Profile{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", birthdate=" + birthdate +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

    public void reserverTimeSlot(TimeSlot s){
        s.setStatus(TimeSlotStatus.PENDING);
    }

    public void validerTimeSlot(TimeSlot s){
        s.setStatus(TimeSlotStatus.WAITING_PAYMENT);
    }

    public void refuserTimeSlot(TimeSlot s){
        s.setStatus(TimeSlotStatus.AVAILABLE);
    }

    // public Transaction payer(TimeSlot s){
    // }
}
