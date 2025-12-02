package restaurantsystemdao;

import java.sql.*;
import restaurantsystem.Payment;
import restaurantsystem.Status;
import util.DB;

public class PaymentDAO {

    // Insert a new payment into the DB and return the generated ID
    public int insert(Payment payment) throws SQLException {
        String sql = "INSERT INTO Payment(amount, paymentMethod, status) VALUES (?, ?, ?)";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDouble(1, payment.getAmount());
            ps.setString(2, payment.getPaymentMethod().name());
            ps.setString(3, payment.getStatus().name());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                payment.setId(generatedId); // store DB id in the Payment object
                System.out.println("Payment added successfully, DB ID: " + generatedId);
                return generatedId;
            }
        }
        return -1;
    }

    // Find a payment by its database ID
    public Payment findById(int paymentId) throws SQLException {
        String sql = "SELECT * FROM Payment WHERE id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, paymentId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Payment payment = new Payment(
                        rs.getDouble("amount"),
                        Payment.PaymentMethod.valueOf(rs.getString("paymentMethod")),
                        0 // orderId not in DB, set manually if needed
                );
                payment.setId(rs.getInt("id"));
                payment.setStatus(Status.valueOf(rs.getString("status")));
                return payment;
            }
        }
        return null;
    }

    // Update payment status in the DB
    public void updateStatus(Payment payment) throws SQLException {
        String sql = "UPDATE Payment SET status = ? WHERE id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, payment.getStatus().name());
            ps.setInt(2, payment.getId());

            ps.executeUpdate();
            System.out.println("Payment status updated: ID " + payment.getId());
        }
    }

    // Delete a payment by its DB ID
    public void delete(int paymentId) throws SQLException {
        String sql = "DELETE FROM Payment WHERE id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, paymentId);
            ps.executeUpdate();
            System.out.println("Payment deleted: ID " + paymentId);
        }
    }
}
