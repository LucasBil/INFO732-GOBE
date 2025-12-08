package polytech.idu.views;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private HomeView homeView = new HomeView();
    private MessageView messageView = new MessageView();
    private ProfileView profileView = new ProfileView();

    public MainWindow() {
        super("GOBE");
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Menu
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Annonces", homeView);
        // tabs.addTab("Messages", messageView);
        // tabs.addTab("Profile", profileView);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(tabs, BorderLayout.CENTER);
    }
}
