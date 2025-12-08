package polytech.idu;

import javax.swing.*;

import polytech.idu.controllers.AdvertisementController;
import polytech.idu.views.MainWindow;
// import polytech.idu.views.profile.LoginView;

public class Main {
    public static void main(String[] args) {
        
        // Mock data initialization
        polytech.idu.services.UniversityService uniService = new polytech.idu.services.UniversityService();
        uniService.insert(new polytech.idu.models.University(1, "USMB", "Annecy"));
        System.out.println(uniService.getAll().size() + " universities loaded.");

        AdvertisementController adController = new AdvertisementController();
        adController.addAdvertisement(new polytech.idu.models.Advertisement(1, "Old bike", "A used bike in good condition", null, 100.0f, new java.util.Date(), new java.util.Date(System.currentTimeMillis() + 86400000), null, 20.0f) {});
        adController.addAdvertisement(new polytech.idu.models.Advertisement(2, "Lawn mower", "Electric lawn mower, barely used", null, 150.0f, new java.util.Date(), new java.util.Date(System.currentTimeMillis() + 86400000), null, 30.0f) {});
        System.out.println(adController.getAllAdvertisements().size() + " advertisements loaded.");

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            // new LoginView().setVisible(true);
            new MainWindow().setVisible(true);
        });
    }
}
