package restaurantsystemdao;

import java.sql.*;
import restaurantsystem.Person;
import util.DB;

public class PersonDAO {
    
    public void insert(Person p) throws SQLException {
        String sql = "INSERT INTO Person(id, name, email, phonenumber, password) " +
                     "VALUES (?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "name=VALUES(name), email=VALUES(email), " +
                     "phonenumber=VALUES(phonenumber), password=VALUES(password)";
        
       try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getEmail());
            ps.setString(4, p.getPhoneNumber());
            ps.setString(5, p.getPassword());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Person added/updated: " + p.getName());
            }

        } catch (SQLException e) {
            System.err.println("Error inserting/updating person: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    
}