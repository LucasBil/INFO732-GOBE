package polytech.idu.services;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import polytech.idu.models.Profile;
import java.util.Date;
import java.util.Locale;

import polytech.idu.models.University;

public class ProfileService  extends Service<Profile> {
    
    public ProfileService() {
        super(Profile.class);
    }
}
