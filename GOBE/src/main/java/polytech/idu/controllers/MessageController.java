package polytech.idu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import polytech.idu.models.Message;
import polytech.idu.models.Profile;
import polytech.idu.services.MessageService;
import polytech.idu.repositories.ProfileRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:5173")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ProfileRepository profileRepository;

    @PostMapping
    public Message sendMessage(@RequestBody Map<String, Object> payload) {
        try {
            int senderId = Integer.parseInt(payload.get("senderId").toString());
            int receiverId = Integer.parseInt(payload.get("receiverId").toString());
            String content = (String) payload.get("content");

            Profile sender = profileRepository.findById(senderId)
                    .orElseThrow(() -> new RuntimeException("Sender not found"));
            Profile receiver = profileRepository.findById(receiverId)
                    .orElseThrow(() -> new RuntimeException("Receiver not found"));

            return messageService.sendMessage(sender, receiver, content);
        } catch (Exception e) {
            e.printStackTrace(); // Log error for debugging
            throw new RuntimeException("Failed to send message: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}/{otherId}")
    public List<Message> getConversation(@PathVariable int userId, @PathVariable int otherId) {
        Profile p1 = profileRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Profile p2 = profileRepository.findById(otherId)
                .orElseThrow(() -> new RuntimeException("Other user not found"));

        return messageService.getConversation(p1, p2);
    }

    @GetMapping("/{userId}/conversations")
    public java.util.Set<Profile> getConversations(@PathVariable int userId) {
        Profile me = profileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return messageService.getConversationPartners(me);
    }
}
