package restaurantsystemdao;

import java.sql.*;
import restaurantsystem.Cashier;
import util.DB;

public class CashierDAO {
    
    public void insert(Cashier cashier) throws SQLException {
        Connection conn = null;
        try {
            conn = DB.getConnection();
            conn.setAutoCommit(false);
            
            // Insert into Person table
            insertPerson(conn, cashier);
            
            // Insert into Cashier table
            insertCashierDetails(conn, cashier);
            
            conn.commit();
            System.out.println("Cashier added successfully: " + cashier.getName());
            
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
    
    private void insertPerson(Connection conn, Cashier cashier) throws SQLException {
        String sql = "INSERT INTO Person(id, name, email, phonenumber, password) " +
                     "VALUES (?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "name=VALUES(name), email=VALUES(email), " +
                     "phonenumber=VALUES(phonenumber), password=VALUES(password)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cashier.getId());
            ps.setString(2, cashier.getName());
            ps.setString(3, cashier.getEmail());
            ps.setString(4, cashier.getPhoneNumber());
            ps.setString(5, cashier.getPassword());
            ps.executeUpdate();
        }
    }
    
    private void insertCashierDetails(Connection conn, Cashier cashier) throws SQLException {
        String sql = "INSERT INTO Cashier(Cashierid, Salary, shift) " +
                     "VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "Salary=VALUES(Salary), shift=VALUES(shift)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cashier.getId());
            ps.setDouble(2, cashier.getSalary());
            ps.setString(3, cashier.getShift());
            ps.executeUpdate();
        }
    }
}