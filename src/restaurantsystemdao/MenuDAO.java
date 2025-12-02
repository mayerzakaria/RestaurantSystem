package restaurantsystemdao;

import java.sql.*;
import java.util.ArrayList;
import restaurantsystem.Menu;
import util.DB;

public class MenuDAO {


// Insert or update a Menu
public static void insertMenu(Menu m) {
    String sql = "INSERT INTO Menu(menuId, description) "
               + "VALUES (?, ?) "
               + "ON DUPLICATE KEY UPDATE "
               + "description = VALUES(description)";

    try (Connection conn = DB.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setObject(1, m.getMenuId() != 0 ? m.getMenuId() : null); 
        ps.setString(2, m.getDescription());

        int rows = ps.executeUpdate();
        if (rows > 0) {
            System.out.println("Menu added/updated with ID: " + m.getMenuId());
        }

    } catch (SQLException e) {
        System.err.println("Error inserting/updating menu: " + e.getMessage());
        e.printStackTrace();
    }
}

// Find Menu by ID
public static Menu findMenuById(int menuId) {
    String sql = "SELECT * FROM Menu WHERE menuId = ?";
    try (Connection conn = DB.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, menuId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Menu menu = new Menu();
            menu.setMenuId(rs.getInt("menuId"));
            menu.setDescription(rs.getString("description"));
            return menu;
        }

    } catch (SQLException e) {
        System.err.println("Error finding menu: " + e.getMessage());
        e.printStackTrace();
    }
    return null;
}

// Delete Menu by ID
public static void deleteMenu(int menuId) {
    String sql = "DELETE FROM Menu WHERE menuId = ?";
    try (Connection conn = DB.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, menuId);
        int rows = ps.executeUpdate();
        if (rows > 0) {
            System.out.println("Menu deleted with ID: " + menuId);
        } else {
            System.out.println("No menu found with ID: " + menuId);
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
            menu.setMenuId(rs.getInt("menuId"));
            menu.setDescription(rs.getString("description"));
            menus.add(menu);
        }

    } catch (SQLException e) {
        System.err.println("Error listing menus: " + e.getMessage());
        e.printStackTrace();
    }
    return menus;
}


}
