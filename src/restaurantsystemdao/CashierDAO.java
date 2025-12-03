package restaurantsystemdao;

import java.sql.*;
import restaurantsystem.Cashier;
import util.DB;
import java.util.ArrayList;
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
     public static Cashier findById(String cashierId) throws SQLException {
        String sql = "SELECT p.id, p.name, p.email, p.phonenumber, p.password, " +
                     "c.Salary, c.shift " +
                     "FROM Person p " +
                     "INNER JOIN Cashier c ON p.id = c.Cashierid " +
                     "WHERE p.id = ?";
        
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, cashierId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                // Create Cashier object with data from both tables
                Cashier cashier = new Cashier(
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phonenumber"),
                    rs.getString("password"),
                    
                    rs.getDouble("Salary"),
                    rs.getString("shift")
                );
                
                return cashier;
            }
        }
        
        return null; // Cashier not found
    }
     public static ArrayList<Cashier> getAllCashiers() throws SQLException {
        ArrayList<Cashier> cashiers = new ArrayList<>();
        String sql = "SELECT p.id, p.name, p.email, p.phonenumber, p.password, " +
                     "c.Salary, c.shift " +
                     "FROM Person p " +
                     "INNER JOIN Cashier c ON p.id = c.Cashierid";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                cashiers.add(new Cashier(
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phonenumber"),
                    rs.getString("password"),
                    rs.getDouble("Salary"),
                    rs.getString("shift")
                ));
            }
        }
        return cashiers;
    }
}