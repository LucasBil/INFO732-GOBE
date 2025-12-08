package polytech.idu.views;

import polytech.idu.models.Profile;
import polytech.idu.models.ChatService;
import polytech.idu.models.Message;
import javax.swing.*;
import java.awt.*;
import java.util.List;

// Swing chat UI
public class SwingChatUI extends JFrame {

    private JPanel messagesPanel;
    private JScrollPane scrollPane;
    private JTextField inputField;
    private JButton sendButton;

    private Profile me;
    private Profile other;
    private ChatService chatService;

    public SwingChatUI(Profile me, Profile other, ChatService chatService) {
        this.me = me;
        this.other = other;
        this.chatService = chatService;

        setTitle("Chat: " + me.getFirstname() + " ↔ " + other.getFirstname());
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Messages panel
        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(messagesPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        sendButton = new JButton("Send");
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        // Send action
        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());

        // Load existing messages
        loadConversation();
    }

    private void loadConversation() {
        List<Message> conv = chatService.getConversation(me, other);
        for (Message m : conv) {
            addMessageBubble(m);
        }
    }

    private void sendMessage() {
        String text = inputField.getText().trim();
        if (text.isEmpty()) return;

        // Send message from "me"
        chatService.send(me, other, text);
        addMessageBubble(new Message(me, other, text));
        inputField.setText("");
    }

    private void addMessageBubble(Message msg) {
        JLabel bubble = new JLabel("<html><p style='width:200px'>" + msg.getContent() + "</p></html>");
        bubble.setOpaque(true);
        bubble.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        bubble.setBackground(msg.getSender() == me ? new Color(220, 248, 198) : Color.WHITE);

        JPanel bubblePanel = new JPanel(new FlowLayout(msg.getSender() == me ? FlowLayout.RIGHT : FlowLayout.LEFT));
        bubblePanel.add(bubble);
        messagesPanel.add(bubblePanel);

        messagesPanel.revalidate();
        messagesPanel.repaint();

        // Scroll to bottom
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar()
                .setValue(scrollPane.getVerticalScrollBar().getMaximum()));
    }

    // Test main
    public static void main(String[] args) {
        Profile buyer = new Profile("Maxence", "Dupont", null, "max@example.com", "USMB");
        Profile seller = new Profile("Alice", "Martin", null, "alice@example.com", "USMB");
        ChatService chat = new ChatService();

        // Preload some messages
        chat.send(buyer, seller, "Bonjour, je suis intéressé par votre vélo.");
        chat.send(seller, buyer, "Bonjour Maxence ! Il est toujours disponible.");
        chat.send(buyer, seller, "Super, on peut se voir demain ?");
        chat.send(seller, buyer, "Oui, parfait pour moi.");

        SwingUtilities.invokeLater(() -> {
            SwingChatUI chatUI = new SwingChatUI(buyer, seller, chat);
            chatUI.setVisible(true);
        });
    }
}
