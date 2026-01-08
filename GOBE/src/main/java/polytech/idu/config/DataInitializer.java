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
        University polytechSchool;

        // 1. Create Universities
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

        // 2. Create Profiles
        if (profileRepository.findByEmail("maxence@gmail.com") == null) {
            // Maxence Ambert (USMB)
            Profile maxence = new Profile(0, "Maxence", "Ambert", new Date(), "maxence@gmail.com", usmb);
            maxence.addFollowedKeyword("vélo");
            maxence.addFollowedKeyword("informatique");
            maxence = profileRepository.save(maxence);

            // Corentin Campillo (USMB)
            Profile corentin = new Profile(0, "Corentin", "Campillo", new Date(), "corentin@gmail.com", usmb);
            corentin.addFollowedKeyword("cuisine");
            corentin = profileRepository.save(corentin);

            // Extra User for diversity (Polytech)
            Profile alice = new Profile(0, "Alice", "Wonder", new Date(), "alice@polytech.fr", polytechSchool);
            alice = profileRepository.save(alice);

            // 3. Create Goods & Services

            // Maxence sells a Camera (Good)
            Good camera = new Good(0, "Appareil Photo Canon",
                    "Canon EOS 2000D avec objectif 18-55mm. Très peu servi, excellent état.",
                    null, 350.0f, new Date(), new Date(System.currentTimeMillis() + 86400000L * 60),
                    usmb, 0.0f);
            camera.setHolder(maxence);
            camera.setStatus(GoodStatus.AVAILABLE);
            camera.setState(GoodState.A);
            advertisementRepository.save(camera);

            // Corentin sells a Textbook (Good)
            Good book = new Good(0, "Livre Algorithmique",
                    "Introduction aux algorithmes, 3ème édition. Quelques surlignages.",
                    null, 25.0f, new Date(), new Date(System.currentTimeMillis() + 86400000L * 30),
                    usmb, 0.0f);
            book.setHolder(corentin);
            book.setStatus(GoodStatus.AVAILABLE);
            book.setState(GoodState.B);
            advertisementRepository.save(book);

            // Corentin offers Tutoring (Service)
            Service tutoring = new Service(0, "Soutien Scolaire Physique",
                    "Etudiant sérieux propose cours de Physique/Chimie niveau Collège/Lycée.",
                    null, 18.0f, new Date(), new Date(System.currentTimeMillis() + 86400000L * 90),
                    usmb, 0.0f);
            tutoring.setHolder(corentin);
            advertisementRepository.save(tutoring);

            // Alice offers Cleaning (Service)
            Service cleaning = new Service(0, "Ménage Appartement",
                    "Je propose de faire le ménage avant vos états des lieux.",
                    null, 12.0f, new Date(), new Date(System.currentTimeMillis() + 86400000L * 30),
                    polytechSchool, 0.0f);
            cleaning.setHolder(alice);
            advertisementRepository.save(cleaning);

            // 4. Create Interactions (Messages & Bookings)

            // Corentin asks Maxence about the Camera
            Message msg1 = new Message(0, corentin, maxence, "Bonjour Maxence, le prix est-il négociable ?",
                    new Date(System.currentTimeMillis() - 86400000L));
            messageRepository.save(msg1);
            Message msg2 = new Message(0, maxence, corentin,
                    "Salut Corentin, je peux descendre à 330€ si tu viens le chercher.", new Date());
            messageRepository.save(msg2);

            // Alice books a slot for Tutoring with Corentin
            TimeSlot ts1 = new TimeSlot(0, alice, tutoring, 18.0f, new Date(System.currentTimeMillis() + 86400000L * 2),
                    TimeSlotStatus.PENDING);
            timeSlotRepository.save(ts1);

            System.out.println("MOCK DATA GENERATED: Maxence, Corentin, Alice and their ads/interactions.");
        }
    }
}
