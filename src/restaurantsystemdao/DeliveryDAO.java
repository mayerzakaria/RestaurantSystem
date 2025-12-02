package restaurantsystemdao;

import java.sql.*;
import restaurantsystem.Delivery;
import util.DB;

public class DeliveryDAO {
    
    public void insert(Delivery delivery) throws SQLException {
        Connection conn = null;
        try {
            conn = DB.getConnection();
            conn.setAutoCommit(false);
            
            // Insert into Person table
            insertPerson(conn, delivery);
            
            // Insert into Delivery table
            insertDeliveryDetails(conn, delivery);
            
            conn.commit();
            System.out.println("Delivery person added successfully: " + delivery.getName());
            
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
    
    private void insertPerson(Connection conn, Delivery delivery) throws SQLException {
        String sql = "INSERT INTO Person(id, name, email, phonenumber, password) " +
                     "VALUES (?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "name=VALUES(name), email=VALUES(email), " +
                     "phonenumber=VALUES(phonenumber), password=VALUES(password)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, delivery.getId());
            ps.setString(2, delivery.getName());
            ps.setString(3, delivery.getEmail());
            ps.setString(4, delivery.getPhoneNumber());
            ps.setString(5, delivery.getPassword());
            ps.executeUpdate();
        }
    }
    
    private void insertDeliveryDetails(Connection conn, Delivery delivery) throws SQLException {
        String sql = "INSERT INTO Delievery(Delievryid, pickuptime, " +
                     "estimateddeleiverytime, status, isavaliable) " +
                     "VALUES (?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "pickuptime=VALUES(pickuptime), " +
                     "estimateddeleiverytime=VALUES(estimateddeleiverytime), " +
                     "status=VALUES(status), isavaliable=VALUES(isavaliable)";
        
         try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, delivery.getId());
        ps.setTimestamp(2, delivery.getPickupTime() != null ? 
                       Timestamp.valueOf(delivery.getPickupTime()) : null);
        ps.setTimestamp(3, delivery.getEstimatedDeliveryTime() != null ? 
                       Timestamp.valueOf(delivery.getEstimatedDeliveryTime()) : null);
        ps.setString(4, delivery.getStatus().name());
        ps.setBoolean(5, delivery.isAvailable());
        ps.executeUpdate();
    }
    }
}
