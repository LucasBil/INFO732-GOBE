package polytech.idu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import polytech.idu.models.TimeSlot;
import polytech.idu.models.Profile;
import polytech.idu.models.Advertisement;
import polytech.idu.models.enums.TimeSlotStatus;
import polytech.idu.services.TimeSlotService;
import polytech.idu.repositories.ProfileRepository;
import polytech.idu.repositories.AdvertisementRepository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/timeslots")
@CrossOrigin(origins = "http://localhost:5173")
public class TimeSlotController {

    @Autowired
    private TimeSlotService timeSlotService;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private polytech.idu.repositories.MessageRepository messageRepository;

    @Autowired
    private polytech.idu.repositories.NotificationRepository notificationRepository;

    @Autowired
    private polytech.idu.repositories.TimeSlotRepository timeSlotRepository;

    @GetMapping
    public List<TimeSlot> getAllTimeSlots() {
        return timeSlotService.getAllTimeSlots();
    }

    @GetMapping("/advertisement/{adId}")
    public List<TimeSlot> getSlotsByAd(@PathVariable int adId) {
        Advertisement ad = advertisementRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found"));
        return timeSlotService.getTimeSlotsByAdvertisement(ad);
    }

    @PostMapping
    public TimeSlot requestTimeSlot(@RequestBody Map<String, Object> payload) {
        try {
            int profileId = Integer.parseInt(payload.get("profileId").toString());
            int adId = Integer.parseInt(payload.get("advertisementId").toString());
            // String dateStr = (String) payload.get("date");
            double amount = Double.parseDouble(payload.get("amount").toString());

            Profile requester = profileRepository.findById(profileId)
                    .orElseThrow(() -> new RuntimeException("Profile not found"));
            Advertisement ad = advertisementRepository.findById(adId)
                    .orElseThrow(() -> new RuntimeException("Ad not found"));

            Date date = new Date(); // Simplification: In real app, parse dateStr

            TimeSlot ts = timeSlotService.requestTimeSlot(requester, ad, date, (float) amount);

            // Notify Ads Holder and Start Conversation
            if (ad.getHolder() != null) {
                // Notification
                String message = requester.getFirstName() + " requested to book your ad: " + ad.getTitle();
                notificationRepository.save(new polytech.idu.models.Notification(ad.getHolder(), message));

                // Auto-Message (Start Conversation)
                if (!ad.getHolder().equals(requester)) {
                    polytech.idu.models.Message chatMsg = new polytech.idu.models.Message(requester, ad.getHolder(),
                            "Hello! I have requested to book your ad: " + ad.getTitle());
                    messageRepository.save(chatMsg);
                }
            }

            return ts;
        } catch (Exception e) {
            throw new RuntimeException("Error requesting time slot: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public TimeSlot updateStatus(@PathVariable int id, @RequestBody Map<String, String> payload) {
        try {
            System.out.println("Updating status for TimeSlot ID: " + id);
            System.out.println("Payload: " + payload);

            String statusStr = payload.get("status");
            if (statusStr == null) {
                throw new RuntimeException("Status is missing in payload");
            }

            TimeSlotStatus status = TimeSlotStatus.valueOf(statusStr);
            TimeSlot ts = timeSlotService.updateStatus(id, status);

            // Handle Notifications and Ad Status
            if (ts != null) {
                Profile requester = ts.getProfile();
                Advertisement ad = ts.getAdvertisement();

                if (requester == null)
                    System.out.println("WARNING: Requester is null for TimeSlot " + id);
                if (ad == null)
                    System.out.println("WARNING: Ad is null for TimeSlot " + id);

                if (requester != null && ad != null) {
                    try {
                        if (status == TimeSlotStatus.CONFIRMED) {
                            // Notify Requester
                            notificationRepository.save(new polytech.idu.models.Notification(requester,
                                    "Your booking for " + ad.getTitle() + " has been ACCEPTED!"));

                            // If Good, mark as RESERVED
                            if (ad instanceof polytech.idu.models.Good) {
                                polytech.idu.models.Good good = (polytech.idu.models.Good) ad;
                                good.setStatus(polytech.idu.models.enums.GoodStatus.RESERVED);
                                advertisementRepository.save(good);
                            }
                        } else if (status == TimeSlotStatus.REJECTED) {
                            // Notify Requester
                            notificationRepository.save(new polytech.idu.models.Notification(requester,
                                    "Your booking for " + ad.getTitle() + " was REJECTED."));
                        }
                    } catch (Exception e) {
                        System.err.println("Failed to send notification or update ad status: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
            return ts;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating status: " + e.getMessage());
        }
    }

    @PostMapping("/batch-create")
    public List<TimeSlot> createTimeSlots(@RequestBody Map<String, Object> payload) {
        try {
            int adId = Integer.parseInt(payload.get("advertisementId").toString());
            List<String> dateStrings = (List<String>) payload.get("dates");

            Advertisement ad = advertisementRepository.findById(adId)
                    .orElseThrow(() -> new RuntimeException("Ad not found"));

            List<Date> dates = new java.util.ArrayList<>();
            for (String ds : dateStrings) {
                dates.add(new Date(Long.parseLong(ds))); // Expecting timestamps for simplicity or ISO strings parsed
            }

            return timeSlotService.addAvailableTimeSlots(ad, dates);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating slots: " + e.getMessage());
        }
    }

    @PostMapping("/batch-book")
    public List<TimeSlot> bookTimeSlots(@RequestBody Map<String, Object> payload) {
        try {
            int profileId = Integer.parseInt(payload.get("profileId").toString());
            int adId = Integer.parseInt(payload.get("advertisementId").toString());
            List<Integer> slotIds = (List<Integer>) payload.get("slotIds");

            Profile requester = profileRepository.findById(profileId)
                    .orElseThrow(() -> new RuntimeException("Profile not found"));
            Advertisement ad = advertisementRepository.findById(adId)
                    .orElseThrow(() -> new RuntimeException("Ad not found"));

            List<TimeSlot> bookedSlots = timeSlotService.bookTimeSlots(requester, slotIds, ad);

            // Notify Ad Holder
            if (ad.getHolder() != null) {
                String message = requester.getFirstName() + " booked " + bookedSlots.size() + " slots for: "
                        + ad.getTitle();
                notificationRepository.save(new polytech.idu.models.Notification(ad.getHolder(), message));

                // Auto-Message
                if (!ad.getHolder().equals(requester)) {
                    polytech.idu.models.Message chatMsg = new polytech.idu.models.Message(requester, ad.getHolder(),
                            "Hello! I have booked " + bookedSlots.size() + " slots for: " + ad.getTitle());
                    messageRepository.save(chatMsg);
                }
            }

            return bookedSlots;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error booking slots: " + e.getMessage());
        }
    }
}
