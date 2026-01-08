package polytech.idu.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.List;

import jakarta.persistence.*;

import polytech.idu.models.enums.TimeSlotStatus;
import polytech.idu.models.enums.Currency;

@Entity
@Table(name = "Profile")
public class Profile extends Model implements AdvertisementObserver {
    @Column(name = "firstname")
    protected String firstname;

    @Column(name = "lastname")
    protected String lastname;

    @Column(name = "birthdate")
    protected Date birthdate;

    @Column(name = "email")
    protected String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "university")
    protected University university;

    @Transient
    protected List<String> preference;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "Profile_Keywords", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "keyword")
    protected List<String> followedKeywords = new ArrayList<>();

    public Profile() {
        super();
    }

    @Override
    public Notification onAdvertisementCreated(Advertisement ad) {
        if (this.followedKeywords == null || this.followedKeywords.isEmpty())
            return null;

        List<String> adKeywords = ad.getKeywords();
        for (String kw : this.followedKeywords) {
            if (kw != null && adKeywords.contains(kw.toLowerCase())) {
                Notification notification = new Notification();
                notification.setUser(this);
                notification.setMessage("New ad matching '" + kw + "': " + ad.getTitle());
                notification.setRead(false);
                notification.setCreatedAt(new java.util.Date());
                return notification;
            }
        }
        return null;
    }

    public String getFirstName() {
        return firstname;
    }

    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }

    public String getLastName() {
        return lastname;
    }

    public void setLastName(String lastname) {
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

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public List<String> getPreference() {
        return preference;
    }

    public void addPreference(String preference) {
        if (this.preference == null)
            this.preference = new ArrayList<>();
        this.preference.add(preference);
    }

    public List<String> getFollowedKeywords() {
        return followedKeywords;
    }

    public void setFollowedKeywords(List<String> followedKeywords) {
        this.followedKeywords = followedKeywords;
    }

    public void addFollowedKeyword(String keyword) {
        if (this.followedKeywords == null)
            this.followedKeywords = new ArrayList<>();
        if (!this.followedKeywords.contains(keyword)) {
            this.followedKeywords.add(keyword);
        }
    }

    public void removeFollowedKeyword(String keyword) {
        if (this.followedKeywords != null) {
            this.followedKeywords.remove(keyword);
        }
    }

    public Profile(int id, String firstname, String lastname, Date birthdate, String email, University university) {
        super(id);
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.email = email;
        this.university = university;
        this.preference = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        Profile profile = (Profile) o;
        return Objects.equals(firstname, profile.firstname) && Objects.equals(lastname, profile.lastname)
                && Objects.equals(birthdate, profile.birthdate) && Objects.equals(email, profile.email)
                && Objects.equals(university, profile.university);
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

    public void reserverTimeSlot(TimeSlot s) {
        s.setStatus(TimeSlotStatus.PENDING);
        s.setProfile(this);
    }

    public void validerTimeSlot(TimeSlot s) {
        s.setStatus(TimeSlotStatus.WAITING_PAYMENT);
        s.getProfile().bookingValidated(s);
    }

    public void refuserTimeSlot(TimeSlot s) {
        s.setStatus(TimeSlotStatus.AVAILABLE);
        s.getProfile().bookingRefused(s);
    }

    public void bookingValidated(TimeSlot s) {
        System.out.println("Le TimeSlot " + s + " a été validé par l'annonceur. Vous pouvez procéder au paiement.");
    }

    public void bookingRefused(TimeSlot s) {
        System.out.println("Le TimeSlot " + s + " a été refusé par l'annonceur. Veuillez choisir un autre créneau.");
    }

    public Transaction payer(TimeSlot s) {

        if (s.getStatus() == TimeSlotStatus.WAITING_PAYMENT) {
            java.util.Date date = new java.util.Date();
            Transaction t = new Transaction(-1, this, s, date, Currency.EUR);
            System.out.println("Paiement effectué pour le TimeSlot " + s + ".");
            return t;
        }

        else {
            System.out.println("Le TimeSlot n'a pas été valider par l'annonceur ! Vous ne pouvez pas payer.");
            return null;
        }
    }

}
