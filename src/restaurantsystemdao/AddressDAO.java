package restaurantsystemdao;

import restaurantsystem.Address;
import util.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressDAO {


// Insert a new address
public static void insertAddress(Address address) 
{
    String sql = "INSERT INTO Address(id, fullAddress, isDefault, customerId) " +
                 "VALUES (?, ?, ?, ?) " +
                 "ON DUPLICATE KEY UPDATE fullAddress = VALUES(fullAddress), " +
                 "isDefault = VALUES(isDefault), customerId = VALUES(customerId)";

    try (Connection conn = DB.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, address.getId());
        ps.setString(2, address.getFullAddress());
        ps.setBoolean(3, address.isDefault());
        ps.setString(4, address.getCustomerId());

        int rows = ps.executeUpdate();
        if (rows > 0) {
            System.out.println("Address added/updated: " + address.getFullAddress());
        }

    } catch (SQLException e) {
        System.err.println("Error inserting/updating address: " + e.getMessage());
        e.printStackTrace();
    }
}

// Get an address by ID
public static Address getAddressById(int addressId) 
{
    String sql = "SELECT * FROM Address WHERE id = ?";
    try (Connection conn = DB.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, addressId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new Address(
                rs.getInt("id"),
                rs.getString("fullAddress"),
                rs.getBoolean("isDefault"),
                rs.getString("customerId")
            );
        }

    } catch (SQLException e) {
        System.err.println("Error retrieving address: " + e.getMessage());
        e.printStackTrace();
    }
    return null;
}

// Get all addresses
public static List<Address> getAllAddresses() {
    List<Address> addresses = new ArrayList<>();
    String sql = "SELECT * FROM Address";

    try (Connection conn = DB.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            addresses.add(new Address(
                rs.getInt("id"),
                rs.getString("fullAddress"),
                rs.getBoolean("isDefault"),
                rs.getString("customerId")
            ));
        }

    } catch (SQLException e) {
        System.err.println("Error retrieving addresses: " + e.getMessage());
        e.printStackTrace();
    }

    return addresses;
}

// Update an address
public static void updateAddress(Address address) {
    String sql = "UPDATE Address SET fullAddress = ?, isDefault = ?, customerId = ? WHERE id = ?";

    try (Connection conn = DB.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, address.getFullAddress());
        ps.setBoolean(2, address.isDefault());
        ps.setString(3, address.getCustomerId());
        ps.setInt(4, address.getId());

        int rows = ps.executeUpdate();
        if (rows > 0) {
            System.out.println("Address updated: " + address.getFullAddress());
        }

    } catch (SQLException e) {
        System.err.println("Error updating address: " + e.getMessage());
        e.printStackTrace();
    }
}

// Delete an address
public static void deleteAddress(int id) {
    String sql = "DELETE FROM Address WHERE id = ?";

    try (Connection conn = DB.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, id);
        int rows = ps.executeUpdate();
        if (rows > 0) {
            System.out.println("Address deleted with ID: " + id);
        }

    } catch (SQLException e) {
        System.err.println("Error deleting address: " + e.getMessage());
        e.printStackTrace();
    }
}

// Get all addresses by customerId
public static List<Address> getAddressesByCustomerId(String customerId) {
    List<Address> addresses = new ArrayList<>();
    String sql = "SELECT * FROM Address WHERE customerId = ?";

    try (Connection conn = DB.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, customerId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            addresses.add(new Address(
                rs.getInt("id"),
                rs.getString("fullAddress"),
                rs.getBoolean("isDefault"),
                rs.getString("customerId")
            ));
        }

    } catch (SQLException e) {
        System.err.println("Error retrieving addresses for customer: " + e.getMessage());
        e.printStackTrace();
    }

    return addresses;
}


}
