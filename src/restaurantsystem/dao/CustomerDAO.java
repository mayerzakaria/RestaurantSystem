package restaurantsystem.dao;

import restaurantsystem.Customer;
import restaurantsystem.Address;
import util.DB; // or wherever your DB class is

import java.sql.*;

public class CustomerDAO {
    private Connection conn;

    public CustomerDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts a new Customer: first into Person, then into Customer table.
     * Returns true if successful.
     */
    public boolean addCustomer(Customer customer) throws SQLException {
        // 1. Insert into Person
        String sqlPerson = "INSERT INTO Person (id, name, email, phonenumber, password) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmtPerson = conn.prepareStatement(sqlPerson)) {
            stmtPerson.setString(1, customer.getCustomerId());
            stmtPerson.setString(2, customer.getName());
            stmtPerson.setString(3, customer.getEmail());
            stmtPerson.setString(4, customer.getPhoneNumber());
            stmtPerson.setString(5, customer.getPassword());
            stmtPerson.executeUpdate();
        }

        // 2. Insert into Customer
        String sqlCust = "INSERT INTO Customer (customerid, Iselitecustomer, dineincount, subscriptionactive, monthremaining) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmtCust = conn.prepareStatement(sqlCust)) {
            stmtCust.setString(1, customer.getCustomerId());
            stmtCust.setBoolean(2, customer.isEliteCustomer());
            stmtCust.setInt(3, customer.getDineInCount());
            stmtCust.setBoolean(4, customer.isSubscriptionActive());
            stmtCust.setInt(5, customer.getMonthsRemaining());
            stmtCust.executeUpdate();
        }

        // Optionally insert address via AddressDAO (if not null)
        Address addr = customer.getAddress();
        if (addr != null) {
            // you need AddressDAO to manage address insertion
            // or write address insertion logic here
        }

        return true;
    }

    /**
     * Load a Customer by ID (both Person + Customer data).
     */
    public Customer getCustomerById(String id) throws SQLException {
        String sql = "SELECT p.name, p.email, p.phonenumber, p.password, "
                   + "c.Iselitecustomer, c.dineincount, c.subscriptionactive, c.monthremaining "
                   + "FROM Person p JOIN Customer c ON p.id = c.customerid "
                   + "WHERE c.customerid = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Address address = null; // optionally load address via AddressDAO
                Customer customer = new Customer(
                    rs.getString("password"),
                    rs.getBoolean("Iselitecustomer"),
                    address,
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phonenumber")
                );
                customer.setId(id);
                // restore dineInCount, subscriptionActive, monthsRemaining
                customer.setEliteCustomer(rs.getBoolean("Iselitecustomer"));
                customer.setSubscriptionActive(rs.getBoolean("subscriptionactive"));
                customer.setMonthsRemaining(rs.getInt("monthremaining"));
                // you may need to restore dineInCount too
                customer.setDineInCount(rs.getInt("dineincount"));
                return customer;
                
            }
        }
        return null;
    }

    /**
     * Update Customer (both Person and Customer tables).
     */
    public boolean updateCustomer(Customer customer) throws SQLException {
        // Update Person
        String sqlPerson = "UPDATE Person SET name=?, email=?, phonenumber=?, password=? WHERE id=?";
        try (PreparedStatement stmtPerson = conn.prepareStatement(sqlPerson)) {
            stmtPerson.setString(1, customer.getName());
            stmtPerson.setString(2, customer.getEmail());
            stmtPerson.setString(3, customer.getPhoneNumber());
            stmtPerson.setString(4, customer.getPassword());
            stmtPerson.setString(5, customer.getCustomerId());
            stmtPerson.executeUpdate();
        }

        // Update Customer
        String sqlCust = "UPDATE Customer SET Iselitecustomer=?, dineincount=?, subscriptionactive=?, monthremaining=? WHERE customerid=?";
        try (PreparedStatement stmtCust = conn.prepareStatement(sqlCust)) {
            stmtCust.setBoolean(1, customer.isEliteCustomer());
            stmtCust.setInt(2, customer.getDineInCount());
            stmtCust.setBoolean(3, customer.isSubscriptionActive());
            stmtCust.setInt(4, customer.getMonthsRemaining());
            stmtCust.setString(5, customer.getCustomerId());
            stmtCust.executeUpdate();
        }

        return true;
    }

    /**
     * Delete a Customer (Person & Customer rows).
     */
    public boolean deleteCustomer(String id) throws SQLException {
        // Delete Customer first (because foreign key dependency)
        String sqlCust = "DELETE FROM Customer WHERE customerid = ?";
        try (PreparedStatement stmtCust = conn.prepareStatement(sqlCust)) {
            stmtCust.setString(1, id);
            stmtCust.executeUpdate();
        }

        // Delete from Person
        String sqlPerson = "DELETE FROM Person WHERE id = ?";
        try (PreparedStatement stmtPerson = conn.prepareStatement(sqlPerson)) {
            stmtPerson.setString(1, id);
            stmtPerson.executeUpdate();
        }

        return true;
    }
}
