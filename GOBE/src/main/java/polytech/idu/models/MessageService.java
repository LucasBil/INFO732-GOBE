package polytech.idu.models;
import polytech.idu.models.Message;
import polytech.idu.models.Profile;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MessageService extends Service<Message> {

    public MessageService() {
        super(Message.class);
    }

    /**
     * Send a message from sender to receiver and store in DB
     */
    public void send(Profile sender, Profile receiver, String content) {
        Message m = new Message(sender, receiver, content);
        insert(m);  // save to DB
    }

    /**
     * Get the conversation between two profiles
     */
    public List<Message> getConversation(Profile p1, Profile p2) {
        List<Message> conv = new ArrayList<>();

        String sql = String.format(
                "SELECT * FROM %s WHERE " +
                        "(sender_id = ? AND receiver_id = ?) OR " +
                        "(sender_id = ? AND receiver_id = ?) ORDER BY timestamp ASC;",
                getTable()
        );

        try {
            var stmt = db.prepare(sql);
            stmt.setInt(1, p1.getId());
            stmt.setInt(2, p2.getId());
            stmt.setInt(3, p2.getId());
            stmt.setInt(4, p1.getId());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                conv.add(fromResultSet(rs));
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return conv;
    }

    @Override
    protected Message fromResultSet(ResultSet rs) throws Exception {
        Profile sender = new Profile(rs.getString("sender_name"), "", null, "", "");
        sender.setId(rs.getInt("sender_id"));
        Profile receiver = new Profile(rs.getString("receiver_name"), "", null, "", "");
        receiver.setId(rs.getInt("receiver_id"));
        String content = rs.getString("content");
        Message m = new Message(sender, receiver, content);
        m.setId(rs.getInt("id"));
        m.setTimestamp(rs.getTimestamp("timestamp"));
        return m;
    }
}
