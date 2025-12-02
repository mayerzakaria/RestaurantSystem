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
            case DELIVERED: return "deliverd"; // match spelling in DB
            case OFFLINE: return "cancelled";  
            default: return "pending";
        }
    }
}
