package polytech.idu.views.advertisements;

import javax.swing.*;
import java.awt.*;

public class PostAdvertisementView extends JPanel {

    private JTextField titleField;
    private JTextArea descArea;
    private JTextField priceField;

    public PostAdvertisementView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(8,8));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; titleField = new JTextField(); form.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; form.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; priceField = new JTextField(); form.add(priceField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.anchor = GridBagConstraints.NORTH; form.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        descArea = new JTextArea(8,40);
        form.add(new JScrollPane(descArea), gbc);

        JButton submit = new JButton("Post Advertisement");
        submit.addActionListener(e -> doPost());

        add(form, BorderLayout.CENTER);
        add(submit, BorderLayout.SOUTH);
    }

    private void doPost() {
        String title = titleField.getText().trim();
        String price = priceField.getText().trim();
        String desc = descArea.getText().trim();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title is required", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // TODO: integrate with model / persistence
        JOptionPane.showMessageDialog(this, "Advertisement posted: " + title);
        titleField.setText("");
        priceField.setText("");
        descArea.setText("");
    }
}
