package polytech.idu.services;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import polytech.idu.models.Model;
import polytech.idu.singletons.DB;

public abstract class Service<T extends Model> {
    private final DB db;
    private final Class<T> clazz;

    public Service(Class<T> clazz) {
        this.db = DB.getInstance();
        this.clazz = clazz;
    }

    public String getTable() {
        return this.clazz.getSimpleName();
    }

    protected String[] getColumns() {
    ArrayList<String> columns = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) continue; // ignore static
            if (field.getName().equals("id")) continue;            // ignore id
            columns.add(field.getName());
        }

        return columns.toArray(new String[0]);
    }

    protected void fillInsert(PreparedStatement stmt, T instance) throws Exception {
        Field[] fields = clazz.getDeclaredFields();
        int index = 1;

        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) continue;
            if (field.getName().equals("id")) continue;

            field.setAccessible(true);
            Object value = field.get(instance);
            stmt.setObject(index++, value);
        }
    }

    protected void fillUpdate(PreparedStatement stmt, T instance) throws Exception {
        fillInsert(stmt, instance);
    }

    protected abstract T fromResultSet(ResultSet rs) throws Exception;

    public ArrayList<T> getAll() {
        String sql = String.format("SELECT * FROM %s;", getTable());
        ArrayList<T> instances = new ArrayList<>();
        try {
            ResultSet rs = db.query(sql);

            while (rs.next()) {
                instances.add(fromResultSet(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return instances;
    }

    public ArrayList<T> getBy(String property, Object value) {
        String sql = String.format("SELECT * FROM %s WHERE %s =?;", getTable(), property);
        ArrayList<T> instances = new ArrayList<>();
        try {
            PreparedStatement stmt = this.db.prepare(sql);
            stmt.setObject(1, value);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                instances.add(fromResultSet(rs));
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instances;
    }

    public T get(Integer identifier) {
        String sql = String.format("SELECT * FROM %s WHERE id == %s;", getTable(), identifier);
        try {
            ResultSet rs = this.db.query(sql);
            return fromResultSet(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insert(T instance) {
        try {
            String[] cols = getColumns();
            StringBuilder sql = new StringBuilder();
            
            sql.append("INSERT INTO ");
            sql.append(getTable());
            sql.append(" (");

            for (int i = 0; i < cols.length; i++) {
                sql.append(cols[i]);
                if (i < cols.length - 1) sql.append(", ");
            }

            sql.append(") VALUES (");
            for (int i = 0; i < cols.length; i++) {
                sql.append("?");
                if (i < cols.length - 1) sql.append(", ");
            }
            sql.append(");");

            PreparedStatement stmt = db.prepare(sql.toString());
            fillInsert(stmt, instance);

            stmt.executeUpdate();
            stmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(T instance) {
        try {
            String[] cols = getColumns();
            StringBuilder sql = new StringBuilder();

            sql.append("UPDATE ");
            sql.append(getTable());
            sql.append(" SET ");

            for (int i = 0; i < cols.length; i++) {
                sql.append(cols[i]).append(" = ?");
                if (i < cols.length - 1) sql.append(", ");
            }

            sql.append(" WHERE id = ?;");

            PreparedStatement stmt = db.prepare(sql.toString());
            fillUpdate(stmt, instance);

            stmt.setInt(cols.length + 1, instance.getId());

            stmt.executeUpdate();
            stmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(T instance) {
        try {
            String sql = String.format("SELECT * FROM %s WHERE id == %s;", getTable(), instance.getId());
            this.db.update(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}