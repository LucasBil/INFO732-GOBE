package polytech.idu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import polytech.idu.models.Advertisement;
import polytech.idu.models.Profile;
import polytech.idu.models.TimeSlot;
import polytech.idu.models.University;
import polytech.idu.repositories.AdvertisementRepository;
import polytech.idu.repositories.ProfileRepository;
import polytech.idu.repositories.TimeSlotRepository;
import polytech.idu.repositories.UniversityRepository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/profiles")
@CrossOrigin(origins = "http://localhost:5173")
public class ProfileController {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @GetMapping("/{id}")
    public Profile getProfile(@PathVariable int id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    @PostMapping
    public Profile createProfile(@RequestBody Map<String, Object> payload) {
        try {
            String firstName = (String) payload.get("firstName");
            String lastName = (String) payload.get("lastName");
            String email = (String) payload.get("email");
            Object uniIdObj = payload.get("universityId");

            if (email == null || email.trim().isEmpty())
                throw new RuntimeException("Email is required");
            if (uniIdObj == null || uniIdObj.toString().trim().isEmpty())
                throw new RuntimeException("University is required");

            Integer universityId = Integer.parseInt(uniIdObj.toString());

            if (profileRepository.findByEmail(email) != null) {
                throw new RuntimeException("Email already exists");
            }

            University university = universityRepository.findById(universityId)
                    .orElseThrow(() -> new RuntimeException("University not found"));

            Profile profile = new Profile(0, firstName, lastName, new Date(), email, university);
            return profileRepository.save(profile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/login")
    public Profile login(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        Profile profile = profileRepository.findByEmail(email);
        if (profile == null) {
            throw new RuntimeException("User not found");
        }
        return profile;
    }

    @GetMapping("/{id}/advertisements")
    public List<Advertisement> getProfileAdvertisements(@PathVariable int id) {
        return advertisementRepository.findAll().stream()
                .filter(ad -> ad.getHolder() != null && ad.getHolder().getId() == id)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/timeslots")
    public List<TimeSlot> getProfileTimeSlots(@PathVariable int id) {
        Profile profile = new Profile();
        profile.setId(id);
        return timeSlotRepository.findByProfile(profile);
    }

    @GetMapping("/{id}/reservations")
    public List<TimeSlot> getIncomingReservations(@PathVariable int id) {
        return timeSlotRepository.findAll().stream()
                .filter(ts -> ts.getAdvertisement() != null
                        && ts.getAdvertisement().getHolder() != null
                        && ts.getAdvertisement().getHolder().getId() == id)
                .collect(Collectors.toList());
    }

    @PostMapping("/{id}/keywords")
    public Profile addKeyword(@PathVariable int id, @RequestBody Map<String, String> payload) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        String keyword = payload.get("keyword");
        if (keyword != null && !keyword.trim().isEmpty()) {
            profile.addFollowedKeyword(keyword.trim().toLowerCase());
            return profileRepository.save(profile);
        }
        return profile;
    }

    @DeleteMapping("/{id}/keywords")
    public Profile removeKeyword(@PathVariable int id, @RequestParam String keyword) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        if (keyword != null) {
            profile.removeFollowedKeyword(keyword.trim().toLowerCase());
            return profileRepository.save(profile);
        }
        return profile;
    }
}
