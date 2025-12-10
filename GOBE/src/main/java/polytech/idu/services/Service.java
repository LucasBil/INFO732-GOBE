package polytech.idu.services;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import polytech.idu.annotations.Column;
import polytech.idu.annotations.ManyToOne;
import polytech.idu.annotations.OneToMany;
import polytech.idu.annotations.Table;
import polytech.idu.singletons.DB;

public class Service<T> {
    private final DB db;
    private final Class<T> clazz;

    public Service(Class<T> clazz) {
        this.db = DB.getInstance();
        this.clazz = clazz;
    }

    private Object convertValue(Field field, Object sqlValue) throws Exception {
        if (sqlValue == null) return null;
        Class<?> type = field.getType();

        if (type == Date.class) {
            if (sqlValue instanceof String s) {
                // Format ISO SQLite : "2003-08-31"
                try {
                    SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd");
                    iso.setLenient(false);
                    return iso.parse(s);
                } catch (Exception ignore) {}
                // Format Java Date.toString() : "Sun Aug 31 00:00:00 CEST 2003"
                try {
                    SimpleDateFormat full = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                    full.setLenient(true);
                    return full.parse(s);
                } catch (Exception ignore) {}
                // Format timestamp en string
                try {
                    long timestamp = Long.parseLong(s);
                    return new Date(timestamp);
                } catch (Exception ignore) {}

                throw new ParseException("Unrecognized date format: " + s, 0);
            }
            // Si SQLite renvoie un long
            if (sqlValue instanceof Long l) {
                return new Date(l);
            }
        }

        if (type == ArrayList.class) {
            if (sqlValue instanceof String s) {
                // cas JSON
                if (s.startsWith("[") && s.endsWith("]")) {
                    ArrayList<String> list = new ArrayList<>();
                    s = s.substring(1, s.length() - 1); // remove brackets
                    if (!s.isBlank()) {
                        for (String item : s.split(",")) {
                            list.add(item.trim().replace("\"", ""));
                        }
                    }
                    return list;
                }

                // cas CSV "a,b,c"
                if (s.contains(",")) {
                    ArrayList<String> list = new ArrayList<>();
                    for (String item : s.split(",")) {
                        list.add(item.trim());
                    }
                    return list;
                }

                // cas single item
                ArrayList<String> one = new ArrayList<>();
                one.add(s);
                return one;
            }
        }

        // 🎯 Types simples
        if (type.isAssignableFrom(sqlValue.getClass()))
            return sqlValue;

        if (type == int.class || type == Integer.class)
            return ((Number) sqlValue).intValue();

        if (type == float.class || type == Float.class)
            return ((Number) sqlValue).floatValue();

        if (type == double.class || type == Double.class)
            return ((Number) sqlValue).doubleValue();
        // 🎯 Enum
        if (type.isEnum())
            return Enum.valueOf((Class<Enum>) type, sqlValue.toString());

        return sqlValue;
    }

    private T fromResultSet(ResultSet rs) throws Exception {
        return fromResultSetRecursive(rs, new HashSet<>());
    }

    private T fromResultSetRecursive(ResultSet rs, Set<String> visited) throws Exception {

        T instance = clazz.getDeclaredConstructor().newInstance();
        String key = clazz.getName() + "#" + rs.getInt("id");

        // Évite les cycles infinis (bidirectionnels)
        if (visited.contains(key)) {
            return instance; 
        }
        visited.add(key);

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            // ---------- Champs simples ----------
            if (!field.isAnnotationPresent(ManyToOne.class)
                    && !field.isAnnotationPresent(OneToMany.class)) {
                try {
                    Object raw = rs.getObject(field.getName());
                    Object value = convertValue(field, raw);
                    field.set(instance, value);
                } catch (SQLException ignored) {}
            }

            // ---------- MANY TO ONE ----------
            else if (field.isAnnotationPresent(ManyToOne.class)) {
                ManyToOne mto = field.getAnnotation(ManyToOne.class);
                String fk = mto.columnName();

                Object fkValue = rs.getObject(fk);
                if (fkValue == null) continue;

                Class<?> targetClass = field.getType();
                Service<?> foreignService = new Service<>(targetClass);
                Object foreignObj = foreignService.getOne((int) fkValue, visited);

                field.set(instance, foreignObj);
            }

            // ---------- ONE TO MANY ----------
            else if (field.isAnnotationPresent(OneToMany.class)) {
                OneToMany otm = field.getAnnotation(OneToMany.class);

                Class<?> targetClass = otm.target();
                String mappedBy = otm.mappedBy();

                Service<?> childService = new Service<>(targetClass);

                String sql = "SELECT * FROM " + targetClass.getSimpleName()
                        + " WHERE " + mappedBy + "=" + rs.getInt("id");

                ArrayList<Object> children = new ArrayList<>();
                try (ResultSet rsChild = db.query(sql)) {
                    while (rsChild.next()) {
                        Object childObj = childService.fromResultSetRecursive(rsChild, visited);
                        children.add(childObj);
                    }
                }

                field.set(instance, children);
            }
        }

        return instance;
    }

    public String getTable() {
        Table table = clazz.getAnnotation(Table.class);
        if (table != null) return table.name();
        return clazz.getSimpleName();
    }

    protected Map<Field, Column> getColumns() {
        Map<Field, Column> columns = new LinkedHashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                columns.put(field, column);
            }
        }
        return columns;
    }

    protected Map<Field, ManyToOne> getManyToOneFields() {
        Map<Field, ManyToOne> relations = new LinkedHashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            ManyToOne mto = field.getAnnotation(ManyToOne.class);
            if (mto != null) {
                relations.put(field, mto);
            }
        }
        return relations;
    }

    public ArrayList<T> getAll() {
        ArrayList<T> list = new ArrayList<>();
        String sql = "SELECT * FROM " + getTable() + ";";
        try (ResultSet rs = db.query(sql)) {
            while (rs.next()) {
                list.add(fromResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<T> getBy(String property, String value) {
        ArrayList<T> list = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s WHERE %s = '%s';", getTable(), property, value);
        try (ResultSet rs = db.query(sql)) {
            while (rs.next()) {
                list.add(fromResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(T instance) {
        try {
            StringBuilder cols = new StringBuilder();
            StringBuilder vals = new StringBuilder();
            
            // Colonnes simples
            for (var entry : getColumns().entrySet()) {
                Field f = entry.getKey();
                Column c = entry.getValue();
                if (c.autoIncrement()) continue; // ignorer PK auto-incr
                f.setAccessible(true);
                Object val = f.get(instance);
                cols.append(c.name()).append(", ");
                vals.append("'").append(val).append("', ");
            }

            // ManyToOne
            for (var entry : getManyToOneFields().entrySet()) {
                Field f = entry.getKey();
                ManyToOne mto = entry.getValue();
                f.setAccessible(true);
                Object related = f.get(instance);
                if (related != null) {
                    // On suppose que la classe liée a un champ 'id'
                    Field idField = related.getClass().getDeclaredField("id");
                    idField.setAccessible(true);
                    Object idVal = idField.get(related);
                    cols.append(mto.columnName()).append(", ");
                    vals.append(idVal).append(", ");
                }
            }

            // Supprimer la dernière virgule
            if (cols.length() > 0) cols.setLength(cols.length() - 2);
            if (vals.length() > 0) vals.setLength(vals.length() - 2);

            String sql = String.format("INSERT INTO %s (%s) VALUES (%s);", getTable(), cols, vals);
            db.update(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(T instance) {
        try {
            StringBuilder set = new StringBuilder();
            Object idValue = null;

            // Colonnes simples
            for (var entry : getColumns().entrySet()) {
                Field f = entry.getKey();
                Column c = entry.getValue();
                f.setAccessible(true);
                Object val = f.get(instance);
                if (c.primary()) {
                    idValue = val;
                    continue;
                }
                set.append(c.name()).append("='").append(val).append("', ");
            }

            // ManyToOne
            for (var entry : getManyToOneFields().entrySet()) {
                Field f = entry.getKey();
                ManyToOne mto = entry.getValue();
                f.setAccessible(true);
                Object related = f.get(instance);
                if (related != null) {
                    Field idField = related.getClass().getDeclaredField("id");
                    idField.setAccessible(true);
                    Object idVal = idField.get(related);
                    set.append(mto.columnName()).append("=").append(idVal).append(", ");
                }
            }

            if (set.length() > 0) set.setLength(set.length() - 2);
            String sql = String.format("UPDATE %s SET %s WHERE id=%s;", getTable(), set, idValue);
            db.update(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(T instance) {
        try {
            Field idField = clazz.getDeclaredField("id");
            idField.setAccessible(true);
            Object idValue = idField.get(instance);
            String sql = String.format("DELETE FROM %s WHERE id=%s;", getTable(), idValue);
            db.update(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public T getOne(int id) {
        return getOne(id, new HashSet<>());
    }

    private T getOne(int id, Set<String> visited) {
        String sql = "SELECT * FROM " + getTable() + " WHERE id=" + id + ";";
        try (ResultSet rs = db.query(sql)) {
            if (rs.next()) {
                return fromResultSetRecursive(rs, visited);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}