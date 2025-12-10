package polytech.idu;

import polytech.idu.util.DBInitializer;

public class Main {
    public static void main(String[] args) {
        DBInitializer initializer = new DBInitializer();
        initializer.initDB();
    }
}
