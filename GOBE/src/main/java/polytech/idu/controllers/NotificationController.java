package polytech.idu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import polytech.idu.models.Notification;
import polytech.idu.models.Profile;
import polytech.idu.repositories.NotificationRepository;
import polytech.idu.repositories.ProfileRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:5173")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @GetMapping("/{userId}")
    public List<Notification> getUserNotifications(@PathVariable int userId) {
        Profile user = profileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @PutMapping("/{id}/read")
    public Notification markAsRead(@PathVariable int id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    // Helper to create notifications internally (or if needed by frontend for some
    // reason)
    @PostMapping
    public Notification createNotification(@RequestBody Map<String, Object> payload) {
        int userId = (Integer) payload.get("userId");
        String message = (String) payload.get("message");

        Profile user = profileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return notificationRepository.save(new Notification(user, message));
    }
}
