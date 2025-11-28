package restaurantsystem.dao;

import restaurantsystem.Payment;
import restaurantsystem.Payment.PaymentMethod;
import restaurantsystem.Status;

import java.sql.*;

public class PaymentDAO {
    private Connection conn;

    public PaymentDAO(Connection conn) {
        this.conn = conn;
    }

    // Insert payment
    public boolean insertPayment(Payment payment) throws SQLException {
        String sql = "INSERT INTO Payment (id, amount, paymentMethod, status) VALUES (?, ?, ?, ?)";
       try (PreparedStatement stmt = conn.prepareStatement(sql)) {
    stmt.setInt(1, payment.getOrderId());
    stmt.setDouble(2, payment.getAmount());
    stmt.setString(3, payment.getPaymentMethod().name());
    stmt.setString(4, payment.getStatus().name());
    return stmt.executeUpdate() > 0;
}
       catch (SQLIntegrityConstraintViolationException e) {
    System.out.println("Payment for this order already exists.");
    return false;
    }
    }
    // Get payment by order ID
    public Payment getPaymentByOrderId(int orderId) throws SQLException 
    {
    String sql = "SELECT * FROM Payment WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
    stmt.setInt(1, orderId);
    try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
            Payment payment = new Payment(
                rs.getDouble("amount"),
                Payment.PaymentMethod.valueOf(rs.getString("paymentMethod")),
                rs.getInt("id")
            );
            payment.setStatus(Status.valueOf(rs.getString("status")));
            return payment;
        }
    }
}
return null;
    }
}
