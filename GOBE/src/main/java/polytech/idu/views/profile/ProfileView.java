package polytech.idu.views.profile;

import javax.swing.*;
import java.awt.*;

public class ProfileView extends JPanel {

    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel ratingLabel;

    public ProfileView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10,10));

        JPanel info = new JPanel(new GridLayout(0,1,4,4));
        nameLabel = new JLabel("Name: Alice Example");
        emailLabel = new JLabel("Email: alice@example.com");
        ratingLabel = new JLabel("Rating: 4.8 / 5");

        info.add(nameLabel);
        info.add(emailLabel);
        info.add(ratingLabel);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton edit = new JButton("Edit Profile");
        top.add(edit);

        add(top, BorderLayout.NORTH);
        add(info, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(new JButton("My Ads"));
        bottom.add(new JButton("My Transactions"));
        add(bottom, BorderLayout.SOUTH);
    }
}
