package polytech.idu.views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdvertisementView extends JPanel {

    private JTable table;

    public AdvertisementView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        String[] cols = {"ID", "Title", "Owner", "Status"};
        Object[][] data = {
                {1, "Old bike", "Alice", "Available"},
                {2, "Lawn mower", "Bob", "Requested"}
        };

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JButton("New Ad"));
        top.add(new JButton("Refresh"));
        add(top, BorderLayout.NORTH);
    }
}
