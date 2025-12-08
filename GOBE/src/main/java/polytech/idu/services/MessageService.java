package polytech.idu.services;

import polytech.idu.models.Message;
import polytech.idu.models.Profile;
import polytech.idu.singletons.DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MessageService extends Service<Message> {

    private final DB db;

    public MessageService() {
        super(Message.class);
        this.db = DB.getInstance();
    }

    /**
     * Send a message from sender to receiver and store it in the DB
     */
    public void send(Profile sender, Profile receiver, String content) {
        // [Fix] Uses the new constructor we added to Message.java
        Message m = new Message(sender, receiver, content);
        insert(m); 
    }

    /**
     * [Fix] We must override insert because the generic Service uses reflection 
     * which cannot handle converting 'Profile' objects to 'int' IDs automatically.
     */
    @Override
    public void insert(Message instance) {
        // Assuming your DB columns are named sender_id, receiver_id, content, timestamp
        String sql = "INSERT INTO Message (sender_id, receiver_id, content, timestamp) VALUES (?, ?, ?, ?)";
        
        try {
            PreparedStatement stmt = db.prepare(sql);
            stmt.setInt(1, instance.getSender().getId());
            stmt.setInt(2, instance.getReceiver().getId());
            stmt.setString(3, instance.getContent());
            // Convert Java Date to SQL Timestamp/Date
            stmt.setObject(4, instance.getTimestamp()); 
            
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Message> getConversation(Profile p1, Profile p2) {
        List<Message> conv = new ArrayList<>();

        // [Fix] Changed logic to explicitly check IDs
        String sql = String.format(
                "SELECT * FROM %s WHERE " +
                        "(sender_id = ? AND receiver_id = ?) OR " +
                        "(sender_id = ? AND receiver_id = ?) " +
                        "ORDER BY timestamp ASC;",
                getTable()
        );

        try {
            PreparedStatement stmt = db.prepare(sql);
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
        
        Profile sender = new Profile(rs.getInt("sender_id"), "Unknown", "", null, "", null);
        Profile receiver = new Profile(rs.getInt("receiver_id"), "Unknown", "", null, "", null);

        // [Fix] Use the full constructor
        Message m = new Message(
            rs.getInt("id"), 
            sender, 
            receiver, 
            rs.getString("content"), 
            rs.getTimestamp("timestamp") // Use getTimestamp for DB dates
        );
        
        return m;
    }
}