package restaurantsystemdao;

import java.sql.*;
import java.util.ArrayList;
import restaurantsystem.Menu;
import util.DB;

public class MenuDAO {

    // Insert or update a Menu
    public static void insertMenu(Menu menu) {
        String sql = "INSERT INTO Menu(id, LastUpdate) VALUES (?, ?) "
                   + "ON DUPLICATE KEY UPDATE LastUpdate = VALUES(LastUpdate)";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, menu.getMenuId()); // ID must be set in Menu object
            ps.setTimestamp(2, new Timestamp(menu.getLastUpdate().getTime()));

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Menu added/updated with ID: " + menu.getMenuId());
            }

        } catch (SQLException e) {
            System.err.println("Error inserting/updating menu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Find Menu by ID
    public static Menu findMenuById(int id) {
        String sql = "SELECT * FROM Menu WHERE id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Menu menu = new Menu();
                menu.setMenuId(rs.getInt("id"));
                menu.setMenuId(rs.getInt("id")); // Only ID and LastUpdate exist
                menu.setDescription(null); // optional: no column for description
                return menu;
            }

        } catch (SQLException e) {
            System.err.println("Error finding menu: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Delete Menu by ID
    public static void deleteMenu(int id) {
        String sql = "DELETE FROM Menu WHERE id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Menu deleted with ID: " + id);
            } else {
                System.out.println("No menu found with ID: " + id);
            }

        } catch (SQLException e) {
            System.err.println("Error deleting menu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // List all Menus
    public static ArrayList<Menu> listAllMenus() {
        ArrayList<Menu> menus = new ArrayList<>();
        String sql = "SELECT * FROM Menu";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Menu menu = new Menu();
                menu.setMenuId(rs.getInt("id"));
                menu.setDescription(null); // No description column
                menus.add(menu);
            }

        } catch (SQLException e) {
            System.err.println("Error listing menus: " + e.getMessage());
            e.printStackTrace();
        }
        return menus;
    }
}
