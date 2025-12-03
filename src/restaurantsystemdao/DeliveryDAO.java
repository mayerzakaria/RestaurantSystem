package restaurantsystemdao;

import java.sql.*;
import restaurantsystem.Delivery;
import util.DB;
import java.util.ArrayList;
public class DeliveryDAO {

    public void insert(Delivery delivery) throws SQLException {
        Connection conn = null;
        try {
            conn = DB.getConnection();
            conn.setAutoCommit(false);

         
            insertPerson(conn, delivery);

           
            insertDeliveryDetails(conn, delivery);

            conn.commit();
            System.out.println("Delivery person added successfully: " + delivery.getName());

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Transaction rolled back due to error");
                } catch (SQLException rollbackEx) {
                    System.err.println("Error during rollback: " + rollbackEx.getMessage());
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    System.err.println("Error closing connection: " + closeEx.getMessage());
                }
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
        String sql = "INSERT INTO Delievery(Delievryid, pickuptime, estimateddeleiverytime, status, isavaliable) " +
                     "VALUES (?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "pickuptime=VALUES(pickuptime), " +
                     "estimateddeleiverytime=VALUES(estimateddeleiverytime), " +
                     "status=VALUES(status), isavaliable=VALUES(isavaliable)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, delivery.getId());
            ps.setTimestamp(2, delivery.getPickupTime() != null ? Timestamp.valueOf(delivery.getPickupTime()) : null);
            ps.setTimestamp(3, delivery.getEstimatedDeliveryTime() != null ? Timestamp.valueOf(delivery.getEstimatedDeliveryTime()) : null);
            ps.setString(4, mapStatusToDB(delivery.getStatus()));
            ps.setBoolean(5, delivery.isAvailable());
            ps.executeUpdate();
        }
    }

    private String mapStatusToDB(Delivery.DeliveryStatus status) {
        switch (status) {
            case IDLE: return "pending";
            case ASSIGNED: return "pending";
            case PICKED_UP: return "pickedup";
            case ON_THE_WAY: return "on the way";
        case DELIVERED: return "deliverd"; 
            case OFFLINE: return "cancelled";  
            default: return "pending";
        }
    }
    
    public static Delivery findById(String deliveryId) throws SQLException {
        String sql = "SELECT p.id, p.name, p.email, p.phonenumber, p.password, " +
                     "d.pickuptime, d.estimateddeleiverytime, d.status, d.isavaliable " +
                     "FROM Person p " +
                     "INNER JOIN Delievery d ON p.id = d.Delievryid " +
                     "WHERE p.id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, deliveryId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Delivery delivery = new Delivery(
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phonenumber"),
                    rs.getString("password")
                );
                return delivery;
            }
        }
        return null;
    }

   
    public static ArrayList<Delivery> getAllDeliveryPersons() throws SQLException {
        ArrayList<Delivery> deliveryPersons = new ArrayList<>();
        String sql = "SELECT p.id, p.name, p.email, p.phonenumber, p.password, " +
                     "d.pickuptime, d.estimateddeleiverytime, d.status, d.isavaliable " +
                     "FROM Person p " +
                     "INNER JOIN Delievery d ON p.id = d.Delievryid";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                deliveryPersons.add(new Delivery(
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phonenumber"),
                    rs.getString("password")
                ));
            }
        }
        return deliveryPersons;
    }
    
}
