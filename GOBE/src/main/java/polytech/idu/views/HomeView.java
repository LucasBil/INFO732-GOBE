package polytech.idu.views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HomeView extends JPanel {

    private JTextField searchField;
    private JComboBox<String> filterCombo;
    private JTable table;

    public HomeView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(8,8));

        JPanel top = new JPanel(new BorderLayout(6,6));
        searchField = new JTextField();
        filterCombo = new JComboBox<>(new String[]{"All", "Available", "Requested", "Completed"});
        JButton searchBtn = new JButton("Search");

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controls.add(new JLabel("Filter:"));
        controls.add(filterCombo);
        controls.add(searchBtn);

        top.add(new JLabel("Search:"), BorderLayout.WEST);
        top.add(searchField, BorderLayout.CENTER);
        top.add(controls, BorderLayout.EAST);

        String[] cols = {"ID", "Title", "Owner", "Status"};
        Object[][] data = {
                {1, "Old bike", "Alice", "Available"},
                {2, "Lawn mower", "Bob", "Requested"},
                {3, "Drill", "Charlie", "Available"}
        };

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        Object id = table.getValueAt(row, 0);
                        Object title = table.getValueAt(row, 1);
                        AdvertisementDetailDialog dlg = new AdvertisementDetailDialog(SwingUtilities.getWindowAncestor(HomeView.this), String.valueOf(id), String.valueOf(title));
                        dlg.setVisible(true);
                    }
                }
            }
        });

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}
