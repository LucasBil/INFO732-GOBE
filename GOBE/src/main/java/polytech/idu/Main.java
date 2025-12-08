package polytech.idu;

<<<<<<< HEAD
import java.util.Arrays;
import java.util.Date;

import polytech.idu.models.*;

public class Main {
    public static void main(String[] args) {

        // Create a profile with interest in "vélo" and "adulte"
        Profile buyer = new Profile(
                "Maxence",
                "Dupont",
                new Date(),
                "email@example.com",
                "USMB"
        );

        Profile seller = new Profile(
                "Alice",
                "Martin",
                new Date(),
                "alice@example.com",
                "USMB"
        );

        buyer.getPreference().addAll(Arrays.asList("vélo"));

        // Register the profile as an observer
        AdvertisementService.subscribe(buyer);

        // Create a new advertisement
        Advertisement ad = new Good(
                "Prout à vendre",
                "velo Btwin peu utilisé pour adulte",
                seller,
                100.0f,
                new Date(),
                new Date(),
                "USMB",
                50.0f
        );

        // Notify observers
        AdvertisementService.notifyObservers(ad);

        TimeSlot ts = new TimeSlot(
                buyer,
                ad,
                100.0f,
                new Date(),
                polytech.idu.models.enums.TimeSlotStatus.AVAILABLE
        );

        // Buyer reserves the timeslot
        buyer.reserverTimeSlot(ts);

        // Seller validates the reservation
        seller.validerTimeSlot(ts);

        // Buyer makes the payment
        buyer.payer(ts);


        // ------------------------------------------------------
        // ---------------------- CHAT SYSTEM --------------------
        // ------------------------------------------------------

        System.out.println("\n=== CHAT SYSTEM DEMO ===");

        ChatService chat = new ChatService();

        // Buyer writes to the seller
        chat.send(buyer, seller, "Bonjour, je suis intéressé par votre vélo.");
        chat.send(seller, buyer, "Bonjour Maxence ! Il est toujours disponible.");
        chat.send(buyer, seller, "Super, on peut se voir demain ?");
        chat.send(seller, buyer, "Oui, parfait pour moi.");

        // Print the conversation
        System.out.println("\nConversation entre " + buyer.getFirstname() + " et " + seller.getFirstname() + " :");

        for (Message m : chat.getConversation(buyer, seller)) {
            System.out.println(m.getTimestamp() + " | " + m.getSender().getFirstname() + ": " + m.getContent());
        }
=======
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
>>>>>>> dev
    }
}
