package polytech.idu.controllers;

import polytech.idu.models.Advertisement;
import polytech.idu.models.Profile;
import polytech.idu.services.AdvertisementService;

import java.util.ArrayList;

public class AdvertisementController {
    
    private AdvertisementService service = new AdvertisementService();

    public ArrayList<Advertisement> getAllAdvertisements() {
        return service.getAll();
    }

    public Advertisement getAdvertisementById(int id) {
       return service.getBy("id", id).get(0);
    }

    public ArrayList<Advertisement> getAdvertisementsByUser(Profile user) {
        return service.getBy("user", user);
    }

    public void addAdvertisement(Advertisement ad) {
        service.insert(ad);
    }
}
