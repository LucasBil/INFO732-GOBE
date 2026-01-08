package polytech.idu.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import polytech.idu.models.*;
import polytech.idu.models.enums.GoodState;
import polytech.idu.models.enums.GoodStatus;
import polytech.idu.models.enums.TimeSlotStatus; // Import added
import polytech.idu.repositories.AdvertisementRepository;
import polytech.idu.repositories.UniversityRepository;
import polytech.idu.repositories.ProfileRepository; // Import added
import polytech.idu.repositories.MessageRepository; // Import added
import polytech.idu.repositories.TimeSlotRepository; // Import added

import java.util.Date;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UniversityRepository universityRepository;
    private final AdvertisementRepository advertisementRepository;
    private final ProfileRepository profileRepository;
    private final MessageRepository messageRepository;
    private final TimeSlotRepository timeSlotRepository;

    public DataInitializer(UniversityRepository universityRepository,
            AdvertisementRepository advertisementRepository,
            ProfileRepository profileRepository,
            MessageRepository messageRepository,
            TimeSlotRepository timeSlotRepository) {
        this.universityRepository = universityRepository;
        this.advertisementRepository = advertisementRepository;
        this.profileRepository = profileRepository;
        this.messageRepository = messageRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        University usmb;
        University polytechSchool; // Renamed to avoid package conflict

        if (universityRepository.count() == 0) {
            usmb = new University(0, "Université Savoie Mont Blanc", "Annecy");
            polytechSchool = new University(0, "Polytech Annecy-Chambéry", "Annecy-le-Vieux");

            usmb = universityRepository.save(usmb);
            polytechSchool = universityRepository.save(polytechSchool);
        } else {
            java.util.List<University> schools = universityRepository.findAll();
            if (schools.size() >= 2) {
                usmb = schools.get(0);
                polytechSchool = schools.get(1);
            } else {
                usmb = schools.isEmpty() ? universityRepository.save(new University(0, "USMB", "Annecy"))
                        : schools.get(0);
                polytechSchool = usmb;
            }
        }

        if (advertisementRepository.count() == 0) {
            // Mock Profiles
            Profile alice = new Profile(0, "Alice", "Dupont", new Date(), "alice@usmb.fr", usmb);
            Profile bob = new Profile(0, "Bob", "Martin", new Date(), "bob@polytech.fr", polytechSchool);
            alice = profileRepository.save(alice); // Save and capture ID/Entity
            bob = profileRepository.save(bob);

            // Mock Goods
            Good fridge = new Good(0, "Vends Mini Frigo",
                    "Idéal étudiant, fonctionne parfaitement. A venir chercher sur place.",
                    null, 80.0f, new Date(), new Date(System.currentTimeMillis() + 86400000L * 30),
                    usmb, 0.0f);
            fridge.setHolder(alice);
            fridge.setStatus(GoodStatus.AVAILABLE);
            fridge.setState(GoodState.B);

            Good bike = new Good(0, "Vélo VTT", "VTT Rockrider, quelques rayures mais roule bien.",
                    null, 120.0f, new Date(), new Date(System.currentTimeMillis() + 86400000L * 30),
                    polytechSchool, 0.0f);
            bike.setHolder(bob);
            bike.setStatus(GoodStatus.AVAILABLE);
            bike.setState(GoodState.C);

            // Mock Service
            Service mathTutoring = new Service(0, "Cours de Soutien Maths",
                    "Etudiant ingénieur donne cours de maths niveau Lycée.",
                    null, 15.0f, new Date(), new Date(System.currentTimeMillis() + 86400000L * 60),
                    polytechSchool, 0.0f);
            mathTutoring.setHolder(bob);

            Service movingHelp = new Service(0, "Aide Déménagement",
                    "Bras musclés pour porter vos cartons ce week-end !",
                    null, 10.0f, new Date(), new Date(System.currentTimeMillis() + 86400000L * 7),
                    usmb, 0.0f);
            movingHelp.setHolder(alice);

            advertisementRepository.save(fridge);
            advertisementRepository.save(bike);
            mathTutoring = advertisementRepository.save(mathTutoring); // Save to capture ID
            advertisementRepository.save(movingHelp);

            // Mock Interaction: Alice messages Bob about math
            Message msg1 = new Message(0, alice, bob, "Bonjour, je suis intéressée par les cours de maths.",
                    new Date());
            messageRepository.save(msg1);

            // Mock Interaction: Alice books a slot for math
            TimeSlot ts1 = new TimeSlot(0, alice, mathTutoring, 15.0f, new Date(System.currentTimeMillis() + 86400000L),
                    TimeSlotStatus.PENDING);
            timeSlotRepository.save(ts1);

            System.out.println("MOCK DATA GENERATED: Profiles, Ads, Messages, TimeSlots.");
        }
    }
}
