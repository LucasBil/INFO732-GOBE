package polytech.idu.events;

import java.util.ArrayList;
import java.util.List;

import polytech.idu.models.Advertisement;
import polytech.idu.models.Profile;

public class AdvertisementService {

    private static final List<AdvertisementObserver> observers = new ArrayList<>();

    public static void subscribe(AdvertisementObserver obs) {
        observers.add(obs);
    }

    public static void unsubscribe(AdvertisementObserver obs) {
        observers.remove(obs);
    }

    // Search engine will call this method when a new advertisement is created
    public static void notifyObservers(Advertisement ad) {
        for (AdvertisementObserver obs : observers) {
            if (obs instanceof Profile profile) {
                for (String keyword : ad.getKeywords()) {
                    if (profile.getUniversity() == ad.getUniversity())
                        if (profile.getPreference().contains(keyword)) {
                            profile.onAdvertisementCreated(ad);
                            break;
                    }
                }
            }
        }
    }
}
