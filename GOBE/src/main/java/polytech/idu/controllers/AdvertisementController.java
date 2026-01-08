package polytech.idu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import polytech.idu.models.Advertisement;
import polytech.idu.repositories.AdvertisementRepository;

import java.util.List;

@RestController
@RequestMapping("/api/advertisements")
@CrossOrigin(origins = "http://localhost:5173") // Allow Vite frontend
public class AdvertisementController {

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @GetMapping
    public List<Advertisement> getAllAdvertisements() {
        return advertisementRepository.findAll().stream()
                .filter(ad -> {
                    if (ad instanceof polytech.idu.models.Good) {
                        polytech.idu.models.enums.GoodStatus status = ((polytech.idu.models.Good) ad).getStatus();
                        return status == polytech.idu.models.enums.GoodStatus.AVAILABLE;
                    }
                    return true;
                })
                .toList();
    }

    @Autowired
    private polytech.idu.repositories.ProfileRepository profileRepository;

    @Autowired
    private polytech.idu.repositories.UniversityRepository universityRepository;

    @Autowired
    private polytech.idu.repositories.NotificationRepository notificationRepository;

    @PostMapping
    public Advertisement createAdvertisement(@RequestBody java.util.Map<String, Object> payload) {
        String title = (String) payload.get("title");
        String description = (String) payload.get("description");
        Double price = Double.valueOf(payload.get("price").toString());
        Integer holderId = Integer.parseInt(payload.get("holderId").toString());
        Integer universityId = Integer.parseInt(payload.get("universityId").toString());
        String type = (String) payload.get("type");

        polytech.idu.models.Profile holder = profileRepository.findById(holderId)
                .orElseThrow(() -> new RuntimeException("Holder not found"));
        polytech.idu.models.University place = universityRepository.findById(universityId)
                .orElseThrow(() -> new RuntimeException("University not found"));

        Advertisement savedAd;

        if ("GOOD".equals(type)) {
            polytech.idu.models.Good good = new polytech.idu.models.Good();
            good.setTitle(title);
            good.setDescription(description);
            good.setPrice(price.floatValue());
            good.setHolder(holder);
            good.setPlace(place);
            good.setDate(new java.util.Date());
            good.setExpire(new java.util.Date(System.currentTimeMillis() + 86400000L * 30));

            String stateStr = (String) payload.get("goodState");
            if (stateStr != null) {
                good.setState(polytech.idu.models.enums.GoodState.valueOf(stateStr));
            }
            good.setStatus(polytech.idu.models.enums.GoodStatus.AVAILABLE);
            good.setType("GOOD");

            savedAd = advertisementRepository.save(good);
        } else {
            polytech.idu.models.Service service = new polytech.idu.models.Service();
            service.setTitle(title);
            service.setDescription(description);
            service.setPrice(price.floatValue());
            service.setHolder(holder);
            service.setPlace(place);
            service.setDate(new java.util.Date());
            service.setExpire(new java.util.Date(System.currentTimeMillis() + 86400000L * 30));
            service.setType("SERVICE");

            savedAd = advertisementRepository.save(service);
        }

        checkForNotificationMatches(savedAd);
        return savedAd;
    }

    private void checkForNotificationMatches(Advertisement ad) {
        List<String> adKeywords = ad.getKeywords(); // Assumes getKeywords() exists and returns lowercase
        List<polytech.idu.models.Profile> allProfiles = profileRepository.findAll();

        for (polytech.idu.models.Profile profile : allProfiles) {
            if (profile.getId() == ad.getHolder().getId())
                continue; // Don't notify self

            List<String> validKeywords = profile.getFollowedKeywords();
            if (validKeywords == null || validKeywords.isEmpty())
                continue;

            for (String kw : validKeywords) {
                if (kw == null)
                    continue;
                // Check if the followed keyword is in the ad's keywords (simple containment)
                // Note: adKeywords should be clean. kw should be clean.
                if (adKeywords.contains(kw.toLowerCase())) {
                    polytech.idu.models.Notification notification = new polytech.idu.models.Notification();
                    notification.setUser(profile);
                    notification.setMessage("New ad matching '" + kw + "': " + ad.getTitle());
                    notification.setRead(false);
                    notification.setCreatedAt(new java.util.Date());
                    notificationRepository.save(notification);
                    break; // Notify once per ad provided at least one keyword matches
                }
            }
        }
    }

    @GetMapping("/{id}")
    public Advertisement getAdvertisementById(@PathVariable int id) {
        return advertisementRepository.findById(id).orElseThrow(() -> new RuntimeException("Advertisement not found"));
    }
}
