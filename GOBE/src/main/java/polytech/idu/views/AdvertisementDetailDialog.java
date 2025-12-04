package polytech.idu.views;

import javax.swing.*;
import java.awt.*;

public class AdvertisementDetailDialog extends JDialog {

    public AdvertisementDetailDialog(Window owner, String id, String title) {
        super(owner, "Advertisement " + id, ModalityType.APPLICATION_MODAL);
        initUI(id, title);
        setSize(500, 350);
        setLocationRelativeTo(owner);
    }

    private void initUI(String id, String title) {
        JPanel main = new JPanel(new BorderLayout(8,8));

        JPanel header = new JPanel(new GridLayout(0,1));
        header.add(new JLabel("Title: " + title));
        header.add(new JLabel("Owner: Alice"));
        header.add(new JLabel("Status: Available"));

        JTextArea desc = new JTextArea("This is a sample description for ad #" + id + ".\nReplace with real description from the model.");
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setEditable(false);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton contact = new JButton("Contact");
        JButton reserve = new JButton("Reserve");

        contact.addActionListener(e -> JOptionPane.showMessageDialog(this, "Contact action (open message to owner)"));
        reserve.addActionListener(e -> JOptionPane.showMessageDialog(this, "Reserve action (start transaction)"));

        actions.add(contact);
        actions.add(reserve);

        main.add(header, BorderLayout.NORTH);
        main.add(new JScrollPane(desc), BorderLayout.CENTER);
        main.add(actions, BorderLayout.SOUTH);

        setContentPane(main);
    }
}
