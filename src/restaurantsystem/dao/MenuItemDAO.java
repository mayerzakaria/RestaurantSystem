package restaurantsystem.dao;

import restaurantsystem.MenuItem;
import java.sql.*;
import java.util.ArrayList;

public class MenuItemDAO {
    private Connection conn;

    public MenuItemDAO(Connection conn) {
        this.conn = conn;
    }

    // Insert new menu item
    public boolean insertMenuItem(MenuItem item, int menuId) throws SQLException {
        String sql = "INSERT INTO MenuItem (id, name, description, price, category, isAvailable, Menu_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, generateMenuItemId(menuId)); // implement ID generation
            stmt.setString(2, item.getName());
            stmt.setString(3, item.getDescription());
            stmt.setDouble(4, item.getPrice());
            stmt.setString(5, item.getCategory());
            stmt.setBoolean(6, item.isAvailable());
            stmt.setInt(7, menuId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Fetch all menu items for a specific menu
    public ArrayList<MenuItem> getMenuItemsByMenu(int menuId) throws SQLException {
        ArrayList<MenuItem> items = new ArrayList<>();
        String sql = "SELECT * FROM MenuItem WHERE Menu_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, menuId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MenuItem item = new MenuItem(
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getString("category"),
                        rs.getBoolean("isAvailable")
                    );
                    items.add(item);
                }
            }
        }
        return items;
    }

    // Update menu item availability
    public boolean updateAvailability(int id, int menuId, boolean isAvailable) throws SQLException {
        String sql = "UPDATE MenuItem SET isAvailable = ? WHERE id = ? AND Menu_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, isAvailable);
            stmt.setInt(2, id);
            stmt.setInt(3, menuId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Optional: delete a menu item
    public boolean deleteMenuItem(int id, int menuId) throws SQLException {
        String sql = "DELETE FROM MenuItem WHERE id = ? AND Menu_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setInt(2, menuId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Example helper to generate MenuItem ID (you can adjust logic)
    private int generateMenuItemId(int menuId) throws SQLException {
        String sql = "SELECT MAX(id) AS maxId FROM MenuItem WHERE Menu_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, menuId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("maxId") + 1;
                }
            }
        }
        return 1; // start from 1 if table is empty
    }
}
