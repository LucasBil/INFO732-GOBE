package polytech.idu.views.advertisements;

import polytech.idu.controllers.AdvertisementController;
import polytech.idu.models.Advertisement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class HomeView extends JPanel {

    private JTextField searchField = new JTextField();
    private JComboBox<String> filterCombo = new JComboBox<>(new String[]{"All", "Available", "Requested", "Completed"});
    private JPanel adGrid = new JPanel(new GridLayout(0, 4, 10, 10));
    private AdvertisementController controller = new AdvertisementController();

    public HomeView() {
        setLayout(new BorderLayout(8, 8));
        add(createTopPanel(), BorderLayout.NORTH);
        add(new JScrollPane(adGrid), BorderLayout.CENTER);
        loadAdvertisements();
    }

    private JPanel createTopPanel() {
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controls.add(new JLabel("Filtres:"));
        controls.add(filterCombo);

        JButton searchButton = new JButton("Rechercher");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterAdvertisements();
            }
        });
        controls.add(searchButton);

        JPanel top = new JPanel(new BorderLayout(6, 6));
        top.add(new JLabel("Rechercher:"), BorderLayout.WEST);
        top.add(searchField, BorderLayout.CENTER);
        top.add(controls, BorderLayout.EAST);
        
        return top;
    }

    private void loadAdvertisements() {
        adGrid.removeAll();
        for (Advertisement ad : controller.getAll()) {
            adGrid.add(createAdCard(ad));
        }
        adGrid.revalidate();
        adGrid.repaint();
    }

    private void filterAdvertisements() {
        String searchText = searchField.getText().toLowerCase();
        adGrid.removeAll();

        List<Advertisement> filteredAds = controller.getAll().stream()
                .filter(ad -> ad.getTitle().toLowerCase().contains(searchText) || ad.getDescription().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        for (Advertisement ad : filteredAds) {
            adGrid.add(createAdCard(ad));
        }

        adGrid.revalidate();
        adGrid.repaint();
    }

    private JPanel createAdCard(Advertisement ad) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        card.add(new JLabel(ad.getTitle(), SwingConstants.CENTER), BorderLayout.NORTH);

        JPanel imagePlaceholder = new JPanel();
        imagePlaceholder.setBackground(Color.LIGHT_GRAY);
        imagePlaceholder.setPreferredSize(new Dimension(100, 80));
        JLabel imageLabel = ad.getImagePath() != null ? new JLabel(new ImageIcon(ad.getImagePath())) : new JLabel("No Img");
        imagePlaceholder.add(imageLabel);
        card.add(imagePlaceholder, BorderLayout.CENTER);

        JTextArea descArea = new JTextArea(ad.getDescription());
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setEditable(false);
        card.add(new JScrollPane(descArea), BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    new AdvertisementDetailDialog(SwingUtilities.getWindowAncestor(HomeView.this), ad).setVisible(true);
                }
            }
        });

        return card;
    }
}
