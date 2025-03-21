package vttp.batch5.csf.assessment.server.repositories;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

// Use the following class for MySQL database
@Repository
public class RestaurantRepository {

    @Autowired
    private JdbcTemplate template;

    public static final String sql_getallCustomers = "SELECT * FROM customers";


    public boolean validate(String username, String password)throws NoSuchAlgorithmException{
        SqlRowSet sqlRowSet = template.queryForRowSet(sql_getallCustomers);

        //turn password string into sha 224 hash
        MessageDigest digest = MessageDigest.getInstance("SHA-224");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString  = new StringBuilder();
        for (byte b : hash) {
            hexString .append(String.format("%02x", b));
        }        


        while(sqlRowSet.next()){
            if ((username.equals(sqlRowSet.getString("username")) &&
                (hexString .toString().equals(sqlRowSet.getString("password"))))) {
                return true;
            }
        }

        return false;

    }


    
}
