package restaurantsystemdao;

import java.sql.*;
import java.util.ArrayList;
import restaurantsystem.Table;
import util.DB;

public class TableDAO 
{

    public static void insertRestaurantTable(Table t) {
        if (t.getCashierid() == null || t.getCashierid().isEmpty()) {
            throw new IllegalArgumentException("Cannot insert table without a cashier!");
        }

        String sql = "INSERT INTO `Table` (tablenumber, capacity, Tablestatus, Cashierid) " +
                     "VALUES (?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE capacity = VALUES(capacity), Tablestatus = VALUES(Tablestatus), Cashierid = VALUES(Cashierid)";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, t.getTableNumber());
            ps.setInt(2, t.getCapacity());
            ps.setString(3, t.getStatus().name());
            ps.setString(4, t.getCashierid());  // guaranteed non-null

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Table added/updated: " + t.getTableNumber());
            }

        } catch (SQLException e) {
            System.err.println("Error inserting/updating table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Table findTableByNumber(int tableNumber) {
        String sql = "SELECT * FROM `Table` WHERE tablenumber = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, tableNumber);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int capacity = rs.getInt("capacity");
                Table.TableStatus status = Table.TableStatus.valueOf(rs.getString("Tablestatus"));
                String cashierid = rs.getString("Cashierid");
                return new Table(tableNumber, capacity, status, cashierid);
            }

        } catch (SQLException e) {
            System.err.println("Error finding table: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteTable(int tableNumber) {
        String sql = "DELETE FROM `Table` WHERE tablenumber = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, tableNumber);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Table deleted: " + tableNumber : "No table found with number: " + tableNumber);

        } catch (SQLException e) {
            System.err.println("Error deleting table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static ArrayList<Table> listAllTables() {
        ArrayList<Table> tables = new ArrayList<>();
        String sql = "SELECT * FROM `Table`";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int tableNumber = rs.getInt("tablenumber");
                int capacity = rs.getInt("capacity");
                Table.TableStatus status = Table.TableStatus.valueOf(rs.getString("Tablestatus"));
                String cashierid = rs.getString("Cashierid");
                tables.add(new Table(tableNumber, capacity, status, cashierid));
            }

        } catch (SQLException e) {
            System.err.println("Error listing tables: " + e.getMessage());
            e.printStackTrace();
        }
        return tables;
    }
    
    
   public static void update(Table table) throws SQLException {
        String sql = "UPDATE `Table` SET `capacity`=?, `Tablestatus`=?, `Cashierid`=? WHERE `tablenumber`=?";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, table.getCapacity());
            ps.setString(2, table.getStatus().name());
            ps.setString(3, table.getCashierid());
            ps.setInt(4, table.getTableNumber());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Table updated: " + table.getTableNumber());
            }
        }
    }
}
