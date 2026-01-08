package polytech.idu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytech.idu.models.TimeSlot;
import polytech.idu.models.Profile;
import polytech.idu.models.Advertisement;
import polytech.idu.models.enums.TimeSlotStatus;
import polytech.idu.repositories.TimeSlotRepository;

import java.util.Date;
import java.util.List;

@Service
public class TimeSlotService {

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    public List<TimeSlot> getAllTimeSlots() {
        return timeSlotRepository.findAll();
    }

    public List<TimeSlot> getTimeSlotsByProfile(Profile profile) {
        return timeSlotRepository.findByProfile(profile);
    }

    public List<TimeSlot> getTimeSlotsByAdvertisement(Advertisement advertisement) {
        return timeSlotRepository.findByAdvertisement(advertisement);
    }

    public TimeSlot requestTimeSlot(Profile requester, Advertisement ad, Date date, float amount) {
        TimeSlot ts = new TimeSlot();
        ts.setProfile(requester);
        ts.setAdvertisement(ad);
        ts.setDate(date);
        ts.setAmount(amount);
        ts.setStatus(TimeSlotStatus.PENDING);
        return timeSlotRepository.save(ts);
    }

    public TimeSlot updateStatus(int id, TimeSlotStatus status) {
        TimeSlot ts = timeSlotRepository.findById(id).orElseThrow(() -> new RuntimeException("TimeSlot not found"));
        ts.setStatus(status); // This triggers statusChanged() logic if kept in model, but persistent changes
                              // happen here
        return timeSlotRepository.save(ts);
    }

    public List<TimeSlot> addAvailableTimeSlots(Advertisement ad, List<Date> dates) {
        java.util.List<TimeSlot> slots = new java.util.ArrayList<>();
        for (Date date : dates) {
            TimeSlot ts = new TimeSlot();
            ts.setAdvertisement(ad);
            ts.setDate(date);
            ts.setStatus(TimeSlotStatus.AVAILABLE);
            ts.setAmount(ad.getPrice());
            slots.add(ts);
        }
        return timeSlotRepository.saveAll(slots);
    }

    public List<TimeSlot> bookTimeSlots(Profile requester, List<Integer> slotIds, Advertisement ad) {
        List<TimeSlot> slots = timeSlotRepository.findAllById(slotIds);
        if (slots.size() != slotIds.size()) {
            throw new RuntimeException("Some slots were not found.");
        }

        for (TimeSlot ts : slots) {
            if (ts.getStatus() == TimeSlotStatus.AVAILABLE) {
                ts.setStatus(TimeSlotStatus.PENDING);
                ts.setProfile(requester);
            } else {
                throw new RuntimeException("Slot " + ts.getId() + " is already booked or not available.");
            }
        }
        return timeSlotRepository.saveAll(slots);
    }
}
