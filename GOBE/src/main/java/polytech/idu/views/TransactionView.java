package polytech.idu.views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TransactionView extends JPanel {

    private JTable table;

    public TransactionView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        String[] cols = {"ID", "Good", "From", "To", "Status"};
        Object[][] data = {
                {1, "Old bike", "Alice", "Bob", "Completed"}
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
        top.add(new JButton("New Transaction"));
        top.add(new JButton("Filter"));
        add(top, BorderLayout.NORTH);
    }
}
