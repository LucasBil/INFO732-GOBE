package polytech.idu.views;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private ProfileView profileView;
    private AdvertisementView advertisementView;
    private MessageView messageView;
    private TransactionView transactionView;

    public MainWindow() {
        super("Gobe - Main Window");
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // New views
        HomeView homeView = new HomeView();
        PostAdvertisementView postAdView = new PostAdvertisementView();
        messageView = new MessageView();
        profileView = new ProfileView();
        transactionView = new TransactionView();

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Home", homeView);
        tabs.addTab("Post Ad", postAdView);
        tabs.addTab("Messages", messageView);
        tabs.addTab("Profile", profileView);
        tabs.addTab("Transactions", transactionView);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(tabs, BorderLayout.CENTER);
    }
}
