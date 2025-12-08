package polytech.idu.services;

import java.sql.ResultSet;

import polytech.idu.models.University;

public class UniversityService extends Service<University> {
    public UniversityService() {
        super(University.class);
    }

    @Override
    protected University fromResultSet(ResultSet rs) throws Exception {
        return new University(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("city")
        );
    }
}
