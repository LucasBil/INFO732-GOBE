package polytech.idu.util;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import polytech.idu.annotations.Column;
import polytech.idu.annotations.ManyToOne;
import polytech.idu.annotations.Table;
import polytech.idu.models.Advertisement;
import polytech.idu.models.Message;
import polytech.idu.models.Profile;
import polytech.idu.models.TimeSlot;
import polytech.idu.models.TimeSlotStatus;
import polytech.idu.models.Transaction;
import polytech.idu.models.University;
import polytech.idu.singletons.DB;

public class DBInitializer {
    
    private final DB db;

    public DBInitializer() {
        db = DB.getInstance();
    }

    private final Class<?>[] models = new Class<?>[] {
        Advertisement.class,
        Currency.class,
        Message.class,
        Profile.class,
        TimeSlot.class,
        TimeSlotStatus.class,
        Transaction.class,
        University.class,
    };

    public void initDB() {
        List<Class<?>> sortedModels = sortByDependencies(models);

        for (Class<?> clazz : sortedModels) {
            createTable(clazz);
        }
    }

    public List<Class<?>> sortByDependencies(Class<?>[] models) {
        List<Class<?>> sorted = new ArrayList<>();
        Set<Class<?>> visited = new HashSet<>();

        for (Class<?> model : models) {
            visit(model, visited, sorted);
        }

        return sorted;
    }

    private void visit(Class<?> model, Set<Class<?>> visited, List<Class<?>> sorted) {
        if (visited.contains(model)) return;
        visited.add(model);

        Table table = model.getAnnotation(Table.class);
        if (table != null) {
            for (Class<?> dep : table.dependencies()) {
                visit(dep, visited, sorted);
            }
        }

        if (!sorted.contains(model)) {
            sorted.add(model);
        }
    }

    private void createTable(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        if (table == null) return;

        String tableName = table.name();
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        sb.append(tableName).append(" (");

        List<String> foreignKeys = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            // Colonnes normales
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                sb.append(column.name())
                  .append(" ")
                  .append(column.type());

                if (column.primary()) sb.append(" PRIMARY KEY");
                if (column.autoIncrement()) sb.append(" AUTOINCREMENT");

                sb.append(", ");
            }

            // Relations ManyToOne
            ManyToOne mto = field.getAnnotation(ManyToOne.class);
            if (mto != null) {
                sb.append(mto.columnName())
                  .append(" INTEGER, ");

                foreignKeys.add(
                    String.format(
                        "FOREIGN KEY (%s) REFERENCES %s(%s) ON DELETE %s ON UPDATE %s",
                        mto.columnName(),
                        mto.target().getAnnotation(Table.class).name(),
                        mto.refColumn(),
                        mto.onDelete(),
                        mto.onUpdate()
                    )
                );
            }
        }

        // Ajouter les FK à la fin
        for (String fk : foreignKeys) {
            sb.append(fk).append(", ");
        }

        // Retirer la dernière virgule
        sb.setLength(sb.length() - 2);
        sb.append(");");

        try (Statement stmt = db.getConnection().createStatement()) {
            stmt.execute(sb.toString());
            System.out.println("Table créée : " + tableName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
