package restaurantsystemdao;

import java.sql.*;
import restaurantsystem.Payment;
import util.DB;

public class PaymentDAO {
    
    public int insert(Payment payment) throws SQLException {
        // ✅ FIXED: Added placeholder for 'id'
        String sql = "INSERT INTO Payment(id, amount, paymentMethod, status) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, 
                     Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, payment.getOrderId());    
            ps.setDouble(2, payment.getAmount());
            ps.setString(3, payment.getPaymentMethod().name());
            ps.setString(4, payment.getStatus().name());
            
            ps.executeUpdate();
            
           
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                payment.setOrderId(generatedId);
                System.out.println(" Payment added successfully: " + generatedId);
                return generatedId;
            }
        }
        return -1;
    }
    
    public Payment findById(int paymentId) throws SQLException {
        String sql = "SELECT * FROM Payment WHERE id = ?";
        
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, paymentId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return new Payment(
                    rs.getDouble("amount"),
                    Payment.PaymentMethod.valueOf(rs.getString("paymentMethod")),
                    rs.getInt("id")
                );
            }
        }
        return null;
    }
}