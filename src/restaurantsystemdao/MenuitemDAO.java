package restaurantsystemdao;

import java.sql.*;
import java.util.ArrayList;
import restaurantsystem.MenuItem;
import util.DB;

public class MenuitemDAO {

    // Insert or update a MenuItem
    public static void insertMenuItem(MenuItem mi) {
        String sql = "INSERT INTO MenuItem(itemId, name, description, price, category, isAvailable, menuId) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?) "
                   + "ON DUPLICATE KEY UPDATE "
                   + "name = VALUES(name), description = VALUES(description), "
                   + "price = VALUES(price), category = VALUES(category), "
                   + "isAvailable = VALUES(isAvailable), menuId = VALUES(menuId)";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, mi.getItemId());
            ps.setString(2, mi.getName());           
            ps.setString(3, mi.getDescription());   
            ps.setDouble(4, mi.getPrice());          
            ps.setString(5, mi.getCategory());       
            ps.setBoolean(6, mi.isAvailable());     
            ps.setInt(7, mi.getMenuId());           

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println(" MenuItem added/updated: " + mi.getName());
            }

        } catch (SQLException e) {
            System.err.println(" Error inserting/updating MenuItem: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Find MenuItem by ID
    public static MenuItem findMenuItemById(int itemId) {
        String sql = "SELECT * FROM MenuItem WHERE itemId = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, itemId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new MenuItem(
                    rs.getInt("itemId"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getString("category"),
                    rs.getBoolean("isAvailable"), 
                    rs.getInt("menuId")
                );
            }

        } catch (SQLException e) {
            System.err.println(" Error finding MenuItem: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Delete MenuItem by ID
    public static void deleteMenuItem(int itemId) {
        String sql = "DELETE FROM MenuItem WHERE itemId = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, itemId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println(" MenuItem deleted: " + itemId);
            } else {
                System.out.println(" No MenuItem found with ID: " + itemId);
            }

        } catch (SQLException e) {
            System.err.println(" Error deleting MenuItem: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // List all MenuItems
    public static ArrayList<MenuItem> listAllMenuItems() {
        ArrayList<MenuItem> items = new ArrayList<>();
        String sql = "SELECT * FROM MenuItem";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                items.add(new MenuItem(
                    rs.getInt("itemId"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getString("category"),
                    rs.getBoolean("isAvailable"),
                    rs.getInt("menuId")
                ));
            }

        } catch (SQLException e) {
            System.err.println(" Error listing MenuItems: " + e.getMessage());
            e.printStackTrace();
        }
        return items;
    }
}