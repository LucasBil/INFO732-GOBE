package polytech.idu.views.advertisements;

import javax.swing.*;

import polytech.idu.models.Advertisement;

import java.awt.*;

public class AdvertisementDetailDialog extends JDialog {

    public AdvertisementDetailDialog(Window owner, Advertisement ad) {
        super(owner, "Annonce " + ad.getTitle(), ModalityType.APPLICATION_MODAL);
        initUI(ad);
        setSize(500, 350);
        setLocationRelativeTo(owner);
    }

    private void initUI(Advertisement ad) {
        JPanel main = new JPanel(new BorderLayout(8, 8));
        main.add(createImagePanel(), BorderLayout.WEST);
        main.add(createInfoPanel(ad), BorderLayout.CENTER);
        main.add(createActionsPanel(), BorderLayout.SOUTH);

        setContentPane(main);
    }

    private JPanel createImagePanel() {
        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(Color.LIGHT_GRAY);
        imagePanel.setPreferredSize(new Dimension(200, 150));
        imagePanel.add(new JLabel("Image"));
        return imagePanel;
    }

    private JPanel createInfoPanel(Advertisement ad) {
        JPanel infoPanel = new JPanel(new BorderLayout(5, 5));

        JPanel header = new JPanel(new GridLayout(0, 1));
        header.add(new JLabel("Title: " + ad.getTitle()));
        header.add(new JLabel("Holder: " + ad.getHolder()));
        header.add(new JLabel("Price: " + ad.getPrice()));

        JTextArea desc = new JTextArea(ad.getDescription());
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setEditable(false);

        infoPanel.add(header, BorderLayout.NORTH);
        infoPanel.add(new JScrollPane(desc), BorderLayout.CENTER);
        return infoPanel;
    }

    private JPanel createActionsPanel() {
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton contact = new JButton("Contacter");
        JButton reserve = new JButton("Réserver");

        // Temporaire
        contact.addActionListener(e -> JOptionPane.showMessageDialog(this, "Contacter action"));
        reserve.addActionListener(e -> JOptionPane.showMessageDialog(this, "Réserver action"));

        actions.add(contact);
        actions.add(reserve);
        return actions;
    }
}
