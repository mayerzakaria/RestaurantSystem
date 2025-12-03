package restaurantsystemdao;

import java.sql.*;
import restaurantsystem.Order;
import util.DB;

public class OrderDAO {

    public void insert(Order order) throws SQLException {
        String sql = "INSERT INTO `Order` " +
                     "(idOrder, orderdate, subtotal, discountamount, total, status, ordertype, " +
                     "Payment_id, tablenumber, Customerrid, Delieveryid, Cashierid) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "orderdate=VALUES(orderdate), subtotal=VALUES(subtotal), " +
                     "discountamount=VALUES(discountamount), total=VALUES(total), " +
                     "status=VALUES(status), ordertype=VALUES(ordertype), " +
                     "Payment_id=VALUES(Payment_id), tablenumber=VALUES(tablenumber), " +
                     "Delieveryid=VALUES(Delieveryid), Cashierid=VALUES(Cashierid)";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, order.getOrderId());
            ps.setTimestamp(2, order.getOrderDate() != null ? Timestamp.valueOf(order.getOrderDate()) : null);
            ps.setDouble(3, order.getSubtotal());
            ps.setDouble(4, order.getDiscountAmount());
            ps.setDouble(5, order.getTotal());
            ps.setString(6, order.getStatus().name());
            ps.setString(7, order.getOrderType().name());

                    Integer paymentId = order.getPaymentId();
          if (paymentId != null) ps.setInt(8, paymentId);
          else ps.setNull(8, Types.INTEGER);

          Integer tableNum = order.getTableNumber();
          if (tableNum != null) ps.setInt(9, tableNum);
          else ps.setNull(9, Types.INTEGER);

          ps.setString(10, order.getCustomerId());

          String deliveryId = order.getDeliveryId();
          if (deliveryId != null) ps.setString(11, deliveryId);
          else ps.setNull(11, Types.VARCHAR);

          String cashierId = order.getCashierId();
          if (cashierId != null) ps.setString(12, cashierId);
          else ps.setNull(12, Types.VARCHAR);

            ps.executeUpdate();
            System.out.println("Order added successfully: " + order.getOrderId());
        }
    }

    public Order findById(int orderId) throws SQLException {
        String sql = "SELECT * FROM `Order` WHERE idOrder = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("idOrder"));
                order.setOrderDate(rs.getTimestamp("orderdate") != null ? rs.getTimestamp("orderdate").toLocalDateTime() : null);
                order.setSubtotal(rs.getDouble("subtotal"));
                order.setDiscountAmount(rs.getDouble("discountamount"));
                order.setTotal(rs.getDouble("total"));

               

                return order;
            }
        }
        return null;
    }

public static int getLastOrderId() throws SQLException {
    String sql = "SELECT idOrder FROM `Order` ORDER BY idOrder DESC LIMIT 1";

    try (Connection conn = DB.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("idOrder");
        }
    }
    return -1; 
}

}
