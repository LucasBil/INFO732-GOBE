package polytech.idu.singletons;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DB {
    private static final String URL = "jdbc:sqlite:../db/database.db";
    private static final DB instance = new DB();

    private DB() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DB getInstance() {
        return DB.instance;
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public PreparedStatement prepare(String sql) throws Exception {
        Connection conn = getConnection();
        return conn.prepareStatement(sql);
    }

    public ResultSet query(String sql) {
        try {
            PreparedStatement stmt = prepare(sql);
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int update(String sql) {
        try (PreparedStatement stmt = prepare(sql)) {
            return stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
