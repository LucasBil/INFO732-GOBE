package polytech.idu.services;

import java.sql.ResultSet;

import polytech.idu.models.Advertisement;

public class AdvertisementService extends Service<Advertisement> {
    public AdvertisementService() {
        super(Advertisement.class);
    }

    @Override
    protected Advertisement fromResultSet(ResultSet rs) throws Exception {
        return new Advertisement(rs.getInt("id")) {
            {
                setId(rs.getInt("id"));
                setTitle(rs.getString("title"));
                setDescription(rs.getString("description"));
                setPrice(rs.getFloat("price"));
                setDate(rs.getDate("date"));
                setExpire(rs.getDate("expire"));
                setGuarantee(rs.getFloat("guarantee"));
            }
        };
    }
}
