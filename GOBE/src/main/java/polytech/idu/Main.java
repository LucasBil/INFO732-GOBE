package polytech.idu;

import java.util.ArrayList;

import polytech.idu.models.University;
import polytech.idu.services.UniversityService;

public class Main {
    public static void main(String[] args) {
        UniversityService service = new UniversityService();
        ArrayList<University> universities = service.getBy("id", 1);
        for (University university : universities) {
            university.setName("USMB");
            service.update(university);
            System.out.println(university);
        }
        System.out.println("Hello world!");
    }
}
