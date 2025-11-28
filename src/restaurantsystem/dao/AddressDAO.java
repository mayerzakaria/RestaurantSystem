package restaurantsystem.dao;

import restaurantsystem.Address;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressDAO
{
    private Connection conn;

    public AddressDAO(Connection conn) {
        this.conn = conn;
    }

    // Insert address
   public boolean insertAddress(Address address, String customerId) throws SQLException 
   {
    String sql = "INSERT INTO Address (id, FullAddress, isDefault, Customerrid) VALUES (?, ?, ?, ?)";
    PreparedStatement stmt = conn.prepareStatement(sql);
    stmt.setInt(1, address.getAddressId());
    stmt.setString(2, address.getFullAddress());
    stmt.setBoolean(3, address.isDefault());
    stmt.setString(4, customerId);

    return stmt.executeUpdate() > 0;
}

public List<Address> getAddressesByCustomer(String customerId) throws SQLException {
    String sql = "SELECT * FROM Address WHERE Customerrid = ?";
    PreparedStatement stmt = conn.prepareStatement(sql);
    stmt.setString(1, customerId);
    ResultSet rs = stmt.executeQuery();

    List<Address> addresses = new ArrayList<>();
    while (rs.next()) {
        Address address = new Address(
            rs.getInt("id"),
            rs.getString("FullAddress"),
            rs.getBoolean("isDefault")
        );
        addresses.add(address);
    }
    return addresses;
}

}
