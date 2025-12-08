package polytech.idu.models;

import java.util.Date;
import java.util.Objects;

public class Profile extends Model {
    protected String firstname;
    protected String lastname;
    protected Date birthdate;
    protected String email;
    protected University university;

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

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public Profile(int id, String firstname, String lastname, Date birthdate, String email, University university) {
        super(id);
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.email = email;
        this.university = university;
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
                ", city='" + university + '\'' +
                '}';
    }
}
