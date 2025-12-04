package polytech.idu.views;

import javax.swing.*;
import java.awt.*;

public class MessageView extends JPanel {

    private JTextArea messageArea;
    private JList<String> inboxList;

    public MessageView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addElement("Conversation with Alice");
        listModel.addElement("Conversation with Bob");

        inboxList = new JList<>(listModel);

        inboxList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        inboxList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int idx = inboxList.locationToIndex(evt.getPoint());
                    if (idx >= 0) {
                        String conv = inboxList.getModel().getElementAt(idx);
                        ConversationDialog dialog = new ConversationDialog(SwingUtilities.getWindowAncestor(MessageView.this), conv);
                        dialog.setVisible(true);
                    }
                }
            }
        });

        messageArea = new JTextArea();
        messageArea.setEditable(false);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(inboxList), new JScrollPane(messageArea));
        split.setDividerLocation(250);

        add(split, BorderLayout.CENTER);
    }
}
