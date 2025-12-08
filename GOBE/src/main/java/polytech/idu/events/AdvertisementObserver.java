package polytech.idu.events;

import polytech.idu.models.Advertisement;

public interface AdvertisementObserver {
    void onAdvertisementCreated(Advertisement ad);
}
