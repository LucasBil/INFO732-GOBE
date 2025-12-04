package polytech.idu;

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
                "Paris"
        );

        Profile seller = new Profile(
                "Alice",
                "Martin",
                new Date(),
                "alice@example.com",
                "Lyon"
        );

        buyer.getPreference().addAll(Arrays.asList("vélo", "adulte"));

        // Register the profile as an observer
        AdvertisementService.subscribe(buyer);

        // Create a new advertisement
        Advertisement ad = new Good(
                "Vélo à vendre",
                "vélo Btwin peu utilisé pour adulte",
                seller,
                100.0f,
                new Date(),
                new Date(),
                1,
                50.0f
        );

        // Notify observers
        AdvertisementService.notifyObservers(ad);
    }
}
