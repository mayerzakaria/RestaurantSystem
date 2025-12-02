package restaurantsystemdao;

import java.sql.*;
import java.util.ArrayList;
import restaurantsystem.Table;
import util.DB;

public class TableDAO {
    
    // Insert or update a table
    public static void insertRestaurantTable(Table t) {
        String sql = "INSERT INTO `Table` (tablenumber, capacity, Tablestatus, Cashierid) "
                   + "VALUES (?, ?, ?, ?) "
                   + "ON DUPLICATE KEY UPDATE "
                   + "capacity = VALUES(capacity), Tablestatus = VALUES(Tablestatus), Cashierid = VALUES(Cashierid)";
        
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, t.getTableNumber());
            ps.setInt(2, t.getCapacity());
            ps.setString(3, t.getStatus().name());
            
            
            if (t.getCashierid() != null) {
                ps.setString(4, t.getCashierid());
            } else {
                ps.setNull(4, java.sql.Types.VARCHAR);
            }
            
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println(" Table added/updated: " + t.getTableNumber());
            }
            
        } catch (SQLException e) {
            System.err.println(" Error inserting/updating table: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Find a table by table number
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
            System.err.println(" Error finding table: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    // Delete a table by table number
    public static void deleteTable(int tableNumber) {
        String sql = "DELETE FROM `Table` WHERE tablenumber = ?";
        
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, tableNumber);
            int rows = ps.executeUpdate();
            
            if (rows > 0) {
                System.out.println(" Table deleted: " + tableNumber);
            } else {
                System.out.println(" No table found with number: " + tableNumber);
            }
            
        } catch (SQLException e) {
            System.err.println(" Error deleting table: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // List all tables
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
            System.err.println(" Error listing tables: " + e.getMessage());
            e.printStackTrace();
        }
        return tables;
    }
}