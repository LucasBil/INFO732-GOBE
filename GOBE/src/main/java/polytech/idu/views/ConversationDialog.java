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
        messagesList.setCellRenderer(new MessageBubbleRenderer());

        JTextField input = new JTextField();
        JButton send = new JButton("->");

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

    class MessageBubbleRenderer extends JLabel implements ListCellRenderer<String> {
        @Override
        public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(value);
            setOpaque(true);

            if (value.startsWith("You:")) {
                setHorizontalAlignment(SwingConstants.RIGHT);
                setBackground(new Color(220, 248, 198)); // Light green for user
            } else {
                setHorizontalAlignment(SwingConstants.LEFT);
                setBackground(Color.WHITE);
            }

            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            return this;
        }
    }
}
