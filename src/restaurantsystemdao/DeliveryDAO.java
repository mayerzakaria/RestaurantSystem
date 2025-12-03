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
            case DELIVERED: return " deliverd";  // Note: space before 'deliverd' to match DB schema
            case OFFLINE: return "cancelled";
            default: return "pending";
        }
    }

    private Delivery.DeliveryStatus mapStatusFromDB(String dbStatus) {
        if (dbStatus == null) return Delivery.DeliveryStatus.IDLE;

        switch (dbStatus.trim().toLowerCase()) {
            case "pending": return Delivery.DeliveryStatus.IDLE;
            case "pickedup": return Delivery.DeliveryStatus.PICKED_UP;
            case "on the way": return Delivery.DeliveryStatus.ON_THE_WAY;
            case "deliverd": return Delivery.DeliveryStatus.DELIVERED;
            case "cancelled": return Delivery.DeliveryStatus.OFFLINE;
            default: return Delivery.DeliveryStatus.IDLE;
        }
    }

    public void update(Delivery delivery) throws SQLException {
        Connection conn = null;
        try {
            conn = DB.getConnection();
            conn.setAutoCommit(false);

            // Update Person table
            updatePerson(conn, delivery);

            // Update Delivery details table
            updateDeliveryDetails(conn, delivery);

            conn.commit();
            System.out.println("Delivery person updated successfully: " + delivery.getName());

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

    private void updatePerson(Connection conn, Delivery delivery) throws SQLException {
        String sql = "UPDATE Person SET name=?, email=?, phonenumber=?, password=? WHERE id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, delivery.getName());
            ps.setString(2, delivery.getEmail());
            ps.setString(3, delivery.getPhoneNumber());
            ps.setString(4, delivery.getPassword());
            ps.setString(5, delivery.getId());
            ps.executeUpdate();
        }
    }

    private void updateDeliveryDetails(Connection conn, Delivery delivery) throws SQLException {
        String sql = "UPDATE Delievery SET pickuptime=?, estimateddeleiverytime=?, status=?, isavaliable=? " +
                     "WHERE Delievryid=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, delivery.getPickupTime() != null ? Timestamp.valueOf(delivery.getPickupTime()) : null);
            ps.setTimestamp(2, delivery.getEstimatedDeliveryTime() != null ? Timestamp.valueOf(delivery.getEstimatedDeliveryTime()) : null);
            ps.setString(3, mapStatusToDB(delivery.getStatus()));
            ps.setBoolean(4, delivery.isAvailable());
            ps.setString(5, delivery.getId());
            ps.executeUpdate();
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
                // Set the actual ID from the database
                delivery.setId(rs.getString("id"));

                // Set other delivery-specific fields
                if (rs.getTimestamp("pickuptime") != null) {
                    delivery.setPickupTime(rs.getTimestamp("pickuptime").toLocalDateTime());
                }
                if (rs.getTimestamp("estimateddeleiverytime") != null) {
                    delivery.setEstimatedDeliveryTime(rs.getTimestamp("estimateddeleiverytime").toLocalDateTime());
                }

                // Map status from database
                String dbStatus = rs.getString("status");
                Delivery.DeliveryStatus status = new DeliveryDAO().mapStatusFromDB(dbStatus);
                delivery.setStatus(status);

                // Set availability - if they have a current order, they should not be available
                boolean isAvailable = rs.getBoolean("isavaliable");

                // Load active order assigned to this delivery person
                // Check for any order that's not complete/cancelled/delivered
                String orderSql = "SELECT o.idOrder, o.orderdate, o.subtotal, o.discountamount, o.total, " +
                                "o.status, o.ordertype, o.Payment_id, o.Customerrid, o.Delieveryid " +
                                "FROM `Order` o " +
                                "WHERE o.Delieveryid = ? AND o.status NOT IN ('COMPLETE', 'CANCELLED', 'DELIVERED') " +
                                "ORDER BY o.orderdate DESC LIMIT 1";

                try (PreparedStatement orderPs = conn.prepareStatement(orderSql)) {
                    orderPs.setString(1, deliveryId);
                    ResultSet orderRs = orderPs.executeQuery();

                    if (orderRs.next()) {
                        // Create a basic Order object with the data we have
                        restaurantsystem.Order order = new restaurantsystem.Order();
                        order.setOrderId(orderRs.getInt("idOrder"));
                        order.setOrderDate(orderRs.getTimestamp("orderdate") != null ?
                                         orderRs.getTimestamp("orderdate").toLocalDateTime() : null);
                        order.setSubtotal(orderRs.getDouble("subtotal"));
                        order.setDiscountAmount(orderRs.getDouble("discountamount"));
                        order.setTotal(orderRs.getDouble("total"));
                        order.setCustomerId(orderRs.getString("Customerrid"));

                        // Set the payment ID - IMPORTANT: This is required to update the order later
                        int paymentId = orderRs.getInt("Payment_id");
                        if (!orderRs.wasNull()) {
                            order.setPaymentId(paymentId);
                        }

                        // Set the order status
                        String orderStatus = orderRs.getString("status");
                        if (orderStatus != null) {
                            order.setStatus(restaurantsystem.Status.valueOf(orderStatus));
                        }

                        // Set the order type
                        String orderType = orderRs.getString("ordertype");
                        if (orderType != null) {
                            order.setOrderType(restaurantsystem.Systemmode.valueOf(orderType));
                        }

                        // Load delivery address for the order
                        String addressSql = "SELECT a.id, a.FullAddress, a.isDefault " +
                                          "FROM Address a " +
                                          "WHERE a.customerId = ? AND a.isDefault = 1 LIMIT 1";
                        try (PreparedStatement addrPs = conn.prepareStatement(addressSql)) {
                            addrPs.setString(1, order.getCustomerId());
                            ResultSet addrRs = addrPs.executeQuery();
                            if (addrRs.next()) {
                                restaurantsystem.Address address = new restaurantsystem.Address(
                                    addrRs.getInt("id"),
                                    addrRs.getString("FullAddress"),
                                    addrRs.getBoolean("isDefault")
                                );
                                order.setDeliveryAddress(address);
                            }
                        }

                        // Set the current order on the delivery person
                        delivery.setCurrentOrder(order);

                        // If they have an active order, they should not be available
                        // Override the isAvailable flag from database
                        delivery.setAvailable(false);

                        // Set the correct status based on delivery status from DB
                        // This ensures the status is consistent with having an order
                        if (status == Delivery.DeliveryStatus.IDLE) {
                            // If DB says IDLE but they have an order, set to ASSIGNED
                            delivery.setStatus(Delivery.DeliveryStatus.ASSIGNED);
                        }
                    } else {
                        // No active order found, set availability from database
                        delivery.setAvailable(isAvailable);
                    }
                }

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
                Delivery delivery = new Delivery(
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phonenumber"),
                    rs.getString("password")
                );
               
                delivery.setId(rs.getString("id"));

              
                if (rs.getTimestamp("pickuptime") != null) {
                    delivery.setPickupTime(rs.getTimestamp("pickuptime").toLocalDateTime());
                }
                if (rs.getTimestamp("estimateddeleiverytime") != null) {
                    delivery.setEstimatedDeliveryTime(rs.getTimestamp("estimateddeleiverytime").toLocalDateTime());
                }

                deliveryPersons.add(delivery);
            }
        }
        return deliveryPersons;
    }

}
