package restaurantsystem.dao;

import restaurantsystem.Order;
import restaurantsystem.Payment;
import restaurantsystem.Table;
import restaurantsystem.Delivery;
import restaurantsystem.Systemmode;
import restaurantsystem.Status;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class OrderDAO {
private Connection conn;


public OrderDAO(Connection conn) {
    this.conn = conn;
}

// INSERT ORDER
public boolean addOrder(Order order) {
    String sql = "INSERT INTO `Order` (idOrder, orderdate, subtotal, discountamount, total, status, ordertype, Payment_id, tablenumber, Customerrid, Delieveryid, Cashierid) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, order.getOrderId());
        ps.setTimestamp(2, Timestamp.valueOf(order.getOrderDate()));
        ps.setDouble(3, order.getSubtotal());
        ps.setDouble(4, order.getDiscountAmount());
        ps.setDouble(5, order.getTotal());
        ps.setString(6, order.getStatus().name());
        ps.setString(7, order.getOrderType().name());
        ps.setInt(8, order.getPayment().getId());

        if (order.getTable() != null) {
            ps.setInt(9, order.getTable().getTableNumber());
        } else {
            ps.setNull(9, Types.INTEGER);
        }

        ps.setString(10, order.getCustomerId());

        if (order.getAssignedDelivery() != null) {
            ps.setString(11, order.getAssignedDelivery().getDeliveryPersonId()); // matches database column
        } else {
            ps.setNull(11, Types.VARCHAR);
        }

        ps.setString(12, order.getCashierId()); // Make sure Order has getCashierId()
        ps.executeUpdate();
        return true;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

// GET ORDER BY ID
public Order getOrderById(int idOrder, int paymentId, String customerId, String cashierId) {
    String sql = "SELECT * FROM `Order` WHERE idOrder=? AND Payment_id=? AND Customerrid=? AND Cashierid=?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idOrder);
        ps.setInt(2, paymentId);
        ps.setString(3, customerId);
        ps.setString(4, cashierId);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            LocalDateTime orderDate = rs.getTimestamp("orderdate").toLocalDateTime();
            double subtotal = rs.getDouble("subtotal");
            double discount = rs.getDouble("discountamount");
            double total = rs.getDouble("total");
            Status status = Status.valueOf(rs.getString("status"));
            Systemmode type = Systemmode.valueOf(rs.getString("ordertype"));
            // For full details, fetch Table, Payment, Delivery using respective DAOs
            return new Order(customerId, new HashMap<>(), type, (Table)null); 
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

// DELETE ORDER
public boolean deleteOrder(int idOrder, int paymentId, String customerId, String cashierId) {
    String sql = "DELETE FROM `Order` WHERE idOrder=? AND Payment_id=? AND Customerrid=? AND Cashierid=?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idOrder);
        ps.setInt(2, paymentId);
        ps.setString(3, customerId);
        ps.setString(4, cashierId);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

// UPDATE ORDER STATUS
public boolean updateOrderStatus(int idOrder, int paymentId, String customerId, String cashierId, Status newStatus) {
    String sql = "UPDATE `Order` SET status=? WHERE idOrder=? AND Payment_id=? AND Customerrid=? AND Cashierid=?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, newStatus.name());
        ps.setInt(2, idOrder);
        ps.setInt(3, paymentId);
        ps.setString(4, customerId);
        ps.setString(5, cashierId);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

// GET ALL ORDERS
public List<Order> getAllOrders() {
    List<Order> orders = new ArrayList<>();
    String sql = "SELECT * FROM `Order`";
    try (Statement stmt = conn.createStatement()) {
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            int idOrder = rs.getInt("idOrder");
            int paymentId = rs.getInt("Payment_id");
            String customerId = rs.getString("Customerrid");
            String cashierId = rs.getString("Cashierid");
            orders.add(getOrderById(idOrder, paymentId, customerId, cashierId));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return orders;
}


}
