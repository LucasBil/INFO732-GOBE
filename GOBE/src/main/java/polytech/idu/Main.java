package polytech.idu;

import javax.swing.*;

import polytech.idu.views.profile.LoginView;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new LoginView().setVisible(true);
        });
    }
}
