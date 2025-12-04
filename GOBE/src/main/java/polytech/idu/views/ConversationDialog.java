package polytech.idu.views;

import javax.swing.*;
import java.awt.*;

public class ConversationDialog extends JDialog {

    private DefaultListModel<String> messagesModel;

    public ConversationDialog(Window owner, String conversationTitle) {
        super(owner, conversationTitle, ModalityType.APPLICATION_MODAL);
        initUI(conversationTitle);
        setSize(600, 400);
        setLocationRelativeTo(owner);
    }

    private void initUI(String conversationTitle) {
        messagesModel = new DefaultListModel<>();
        messagesModel.addElement(conversationTitle + " - Hello!");
        messagesModel.addElement("Owner: Thanks for your interest.");

        JList<String> messagesList = new JList<>(messagesModel);

        JTextField input = new JTextField();
        JButton send = new JButton("Send");

        send.addActionListener(e -> {
            String text = input.getText().trim();
            if (!text.isEmpty()) {
                messagesModel.addElement("You: " + text);
                input.setText("");
            }
        });

        JPanel bottom = new JPanel(new BorderLayout(6,6));
        bottom.add(input, BorderLayout.CENTER);
        bottom.add(send, BorderLayout.EAST);

        getContentPane().setLayout(new BorderLayout(8,8));
        getContentPane().add(new JScrollPane(messagesList), BorderLayout.CENTER);
        getContentPane().add(bottom, BorderLayout.SOUTH);
    }
}
