package restaurantsystemdao;

import java.sql.*;
import java.util.ArrayList;
import restaurantsystem.Customer;
import util.DB;
import java.util.regex.*;





public class CustomerDAO 
{
    
 
private static String getNextCustomerId() throws SQLException {
        // Fetch all candidate IDs that start with 'C' and determine the maximum numeric suffix in Java
        String sql = "SELECT id FROM Person WHERE id LIKE 'C%'";
        int maxNumber = 0;
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("id");
                // Split on any non-digit characters and take the last numeric token
                String[] parts = id.split("\\D+");
                if (parts.length > 0) {
                    String numStr = parts[parts.length - 1];
                    if (!numStr.isEmpty()) {
                        try {
                            int num = Integer.parseInt(numStr);
                            if (num > maxNumber) maxNumber = num;
                        } catch (NumberFormatException ex) {
                            // ignore malformed numeric parts
                        }
                    }
                }
            }
        }

        int nextNumber = maxNumber + 1;
        return "C" + String.format("%03d", nextNumber);
    }
    
  
    public void insert(Customer customer) throws SQLException 
    {
        Connection conn = null;
        try {
            conn = DB.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // Generate next customer ID from database
            String newCustomerId = getNextCustomerId();
            customer.setid(newCustomerId);
            
            // 1. Insert into Person table first
            insertPerson(conn, customer);
            
            // 2. Insert into Customer table
            insertCustomerDetails(conn, customer);
            
            conn.commit(); // Commit transaction
            System.out.println(" Customer added successfully: " + customer.getName());
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                    System.err.println(" Transaction rolled back due to error");
                } catch (SQLException rollbackEx) {
                    System.err.println(" Error during rollback: " + rollbackEx.getMessage());
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    System.err.println(" Error closing connection: " + closeEx.getMessage());
                }
            }
        }
    }
    
    private void insertPerson(Connection conn, Customer customer) throws SQLException {
        String sql = "INSERT INTO Person(id, name, email, phonenumber, password) " +
                     "VALUES (?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "name=VALUES(name), email=VALUES(email), " +
                     "phonenumber=VALUES(phonenumber), password=VALUES(password)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer.getId());
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getPhoneNumber());
            ps.setString(5, customer.getPassword());
            ps.executeUpdate();
        }
    }
    
    private void insertCustomerDetails(Connection conn, Customer customer) throws SQLException {
        String sql = "INSERT INTO Customer(customerid, Iselitecustomer, dineincount, " +
                     "subscriptionactive, monthremaining) " +
                     "VALUES (?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "Iselitecustomer=VALUES(Iselitecustomer), " +
                     "dineincount=VALUES(dineincount), " +
                     "subscriptionactive=VALUES(subscriptionactive), " +
                     "monthremaining=VALUES(monthremaining)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer.getId());
            ps.setBoolean(2, customer.isEliteCustomer());
            ps.setInt(3, customer.getDineInCount());
            ps.setBoolean(4, customer.isSubscriptionActive());
            ps.setInt(5, customer.getMonthsRemaining());
            ps.executeUpdate();
        }
    }
    
    /**
     * Find customer by ID - retrieves from both Person and Customer tables
     */
    public Customer findById(String customerId) throws SQLException {
        String sql = "SELECT p.id, p.name, p.email, p.phonenumber, p.password, " +
                     "c.Iselitecustomer, c.dineincount, " +
                     "c.subscriptionactive, c.monthremaining " +
                     "FROM Person p " +
                     "INNER JOIN Customer c ON p.id = c.customerid " +
                     "WHERE c.customerid = ?";
        
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, customerId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                // Use the database constructor
                return new Customer(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phonenumber"),
                    rs.getString("password"),
                    rs.getBoolean("Iselitecustomer"),
                    rs.getInt("dineincount"),
                    rs.getBoolean("subscriptionactive"),
                    rs.getInt("monthremaining")
                );
            }
        }
        return null;
    }
    
    /**
     * Update customer details in database
     */
    public void update(Customer customer) throws SQLException 
    {
        Connection conn = null;
        try {
            conn = DB.getConnection();
            conn.setAutoCommit(false);
            
            updatePerson(conn, customer);
            updateCustomerDetails(conn, customer);
            
            conn.commit();
            System.out.println(" Customer updated successfully: " + customer.getName());
            
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
    
    private void updatePerson(Connection conn, Customer customer) throws SQLException 
    {
        String sql = "UPDATE Person SET name=?, email=?, phonenumber=?, password=? WHERE id=?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getPhoneNumber());
            ps.setString(4, customer.getPassword());
            ps.setString(5, customer.getId());
            ps.executeUpdate();
        }
    }
    
    private void updateCustomerDetails(Connection conn, Customer customer) throws SQLException 
    {
        String sql = "UPDATE Customer SET Iselitecustomer=?, dineincount=?, " +
                     "subscriptionactive=?, monthremaining=? WHERE customerid=?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, customer.isEliteCustomer());
            ps.setInt(2, customer.getDineInCount());
            ps.setBoolean(3, customer.isSubscriptionActive());
            ps.setInt(4, customer.getMonthsRemaining());
            ps.setString(5, customer.getId());
            ps.executeUpdate();
        }
    }

    
    
    public static ArrayList<Customer> getAllCustomers() throws SQLException {
        ArrayList<Customer> customers = new ArrayList<>();
        String sql = "SELECT p.id, p.name, p.email, p.phonenumber, p.password, " +
                     "c.Iselitecustomer, c.dineincount, " +
                     "c.subscriptionactive, c.monthremaining " +
                     "FROM Person p " +
                     "INNER JOIN Customer c ON p.id = c.customerid";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                customers.add(new Customer(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phonenumber"),
                    rs.getString("password"),
                    rs.getBoolean("Iselitecustomer"),
                    rs.getInt("dineincount"),
                    rs.getBoolean("subscriptionactive"),
                    rs.getInt("monthremaining")
                ));
            }
        }
        return customers;
    }
}

