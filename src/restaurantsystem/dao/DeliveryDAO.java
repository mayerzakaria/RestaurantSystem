package restaurantsystem.dao;

import restaurantsystem.Delivery;
import restaurantsystem.Order;
import java.sql.*;
import java.time.LocalDateTime;

public class DeliveryDAO {
    private Connection conn;

    public DeliveryDAO(Connection conn) {
        this.conn = conn;
    }

    // Add a new delivery person
    public boolean addDelivery(Delivery d) {
        String sql = "INSERT INTO Delivery (DeliveryId, totalDeliveries, status, isAvailable) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, d.getDeliveryPersonId());
            stmt.setInt(2, d.getTotalDeliveries());
            stmt.setString(3, d.getStatus().name());
            stmt.setBoolean(4, d.isAvailable());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update delivery person info (status, availability, pickup/est. time, current order)
   public boolean updateDelivery(Delivery d) {
String sql = "UPDATE Delivery SET totalDeliveries=?, currentOrderId=?, pickupTime=?, estimatedDeliveryTime=?, status=?, isAvailable=? WHERE DeliveryId=?";
try (PreparedStatement stmt = conn.prepareStatement(sql)) {
stmt.setInt(1, d.getTotalDeliveries());


    if (d.getCurrentOrder() != null) {
        stmt.setInt(2, d.getCurrentOrder().getOrderId()); // use setInt if orderId is INT in DB
    } else {
        stmt.setNull(2, java.sql.Types.INTEGER); // match DB type
    }

    stmt.setTimestamp(3, d.getPickupTime() != null ? Timestamp.valueOf(d.getPickupTime()) : null);
    stmt.setTimestamp(4, d.getEstimatedDeliveryTime() != null ? Timestamp.valueOf(d.getEstimatedDeliveryTime()) : null);
    stmt.setString(5, d.getStatus().name());
    stmt.setBoolean(6, d.isAvailable());
    stmt.setString(7, d.getDeliveryPersonId());

    return stmt.executeUpdate() > 0;
} catch (SQLException e) {
    e.printStackTrace();
    return false;
}


}


    // Get delivery person by ID
  public Delivery getDeliveryById(String id) {
    String sql = "SELECT * FROM Delivery WHERE DeliveryId=?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            // Create Delivery object with dummy values (since constructor requires them)
            Delivery d = new Delivery("DummyName", "dummy@email.com", "000000", "pass");

            // Use setters instead of accessing private fields directly
            d.setDeliveryCounter(Delivery.getDeliveryCounter()); // optional if needed
            d.setTotalDeliveries(rs.getInt("totalDeliveries"));  // <-- you need to add this setter
            d.setStatus(Delivery.DeliveryStatus.valueOf(rs.getString("status"))); // <-- add setter
            d.setAvailable(rs.getBoolean("isAvailable"));

            Timestamp pickup = rs.getTimestamp("pickupTime");
            if (pickup != null) d.setPickupTime(pickup.toLocalDateTime()); // <-- add setter

            Timestamp est = rs.getTimestamp("estimatedDeliveryTime");
            if (est != null) d.setEstimatedDeliveryTime(est.toLocalDateTime()); // <-- add setter

            // Retrieve current order by ID if needed
            String orderId = rs.getString("currentOrderId");
            if (orderId != null) {
                // d.setCurrentOrder(orderDAO.getOrderById(orderId)); <-- requires setter in Delivery
            }

            return d;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
}
