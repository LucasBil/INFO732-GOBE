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
    }
}
