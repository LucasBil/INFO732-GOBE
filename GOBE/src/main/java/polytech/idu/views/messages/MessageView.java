package polytech.idu.views.messages;

import polytech.idu.controllers.MessageController;
import polytech.idu.controllers.UserController;
import polytech.idu.models.Message;
import polytech.idu.models.Profile;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MessageView extends JPanel {

    private JList<Message> inboxList;
    private MessageController controller;

    public MessageView() {
        this.controller = new MessageController();
        setLayout(new BorderLayout());
        initInboxList();
        add(new JScrollPane(inboxList), BorderLayout.CENTER);
        loadConversations();
    }

    private void initInboxList() {
        inboxList = new JList<>(new DefaultListModel<>());
        inboxList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        inboxList.setCellRenderer(new ConversationListRenderer());
        inboxList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    openConversationDialog(inboxList.getSelectedValue());
                }
            }
        });
    }

    private void loadConversations() {
        Profile currentUser = UserController.getCurrentUser();
        if (currentUser != null) {
            List<Message> conversations = controller.getBy("sender", currentUser);
            conversations.addAll(controller.getBy("receiver", currentUser));
            DefaultListModel<Message> model = (DefaultListModel<Message>) inboxList.getModel();
            model.clear();
            for (Message msg : conversations) {
                model.addElement(msg);
            }
        }
    }

    private void openConversationDialog(Message lastMessage) {
        if (lastMessage != null) {
            Profile currentUser = UserController.getCurrentUser();
            Profile otherUser = lastMessage.getSender().equals(currentUser) ? lastMessage.getReceiver() : lastMessage.getSender();
            new ConversationDialog(SwingUtilities.getWindowAncestor(this), otherUser.getFirstname()).setVisible(true);
        }
    }

    class ConversationListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Message) {
                Message msg = (Message) value;
                Profile currentUser = UserController.getCurrentUser();
                Profile otherUser = msg.getSender().equals(currentUser) ? msg.getReceiver() : msg.getSender();
                setText("<html><b>" + otherUser.getFirstname() + "</b><br><i>" + msg.getText() + "</i></html>");
            }
            return this;
        }
    }
}
