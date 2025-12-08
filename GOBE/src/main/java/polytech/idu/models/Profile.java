package polytech.idu.models;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import polytech.idu.models.enums.TimeSlotStatus;
import polytech.idu.models.enums.Currency;

public class Profile implements AdvertisementObserver {
    protected String firstname;
    protected String lastname;
    protected Date birthdate;
    protected String email;
    protected String university;
    protected ArrayList<String> preference;

    @Override
    public void onAdvertisementCreated(Advertisement ad) {
        System.out.println("New advertisement for " + firstname + " " + lastname + ": " + ad.getTitle());
    }

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

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public ArrayList<String> getPreference() {
        return preference;
    }

    public void addPreference(String preference) {
        this.preference.add(preference);
    }

    public Profile(String firstname, String lastname, Date birthdate, String email, String university) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.email = email;
        this.university = university;
        this.preference = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Objects.equals(firstname, profile.firstname) && Objects.equals(lastname, profile.lastname) && Objects.equals(birthdate, profile.birthdate) && Objects.equals(email, profile.email) && Objects.equals(university, profile.university);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname, birthdate, email, university);
    }

    @Override
    public String toString() {
        return "Profile{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", birthdate=" + birthdate +
                ", email='" + email + '\'' +
                ", university='" + university + '\'' +
                '}';
    }

    public void reserverTimeSlot(TimeSlot s){
        s.setStatus(TimeSlotStatus.PENDING);
        s.setProfile(this);
    }

    public void validerTimeSlot(TimeSlot s){
        s.setStatus(TimeSlotStatus.WAITING_PAYMENT);
        s.getProfile().bookingValidated(s);
    }

    public void refuserTimeSlot(TimeSlot s){
        s.setStatus(TimeSlotStatus.AVAILABLE);
        s.getProfile().bookingRefused(s);
    }

    public void bookingValidated(TimeSlot s){
        System.out.println("Le TimeSlot " + s + " a été validé par l'annonceur. Vous pouvez procéder au paiement.");
    }

    public void bookingRefused(TimeSlot s){
        System.out.println("Le TimeSlot " + s + " a été refusé par l'annonceur. Veuillez choisir un autre créneau.");
    }

    public Transaction payer(TimeSlot s){

        if (s.getStatus() == TimeSlotStatus.WAITING_PAYMENT){
            java.util.Date date = new java.util.Date();
            Transaction t = new Transaction(this , s, date, Currency.EUR);
            System.out.println("Paiement effectué pour le TimeSlot " + s + ".");
            return t;
        }

        else{
            System.out.println("Le TimeSlot n'a pas été valider par l'annonceur ! Vous ne pouvez pas payer.");
            return null;
        }
    }
        
    
}
