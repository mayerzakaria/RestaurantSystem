package restaurantsystem.dao;

import restaurantsystem.Menu;
import restaurantsystem.MenuItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class MenuDAO {
    private Connection conn;

    public MenuDAO(Connection conn) {
        this.conn = conn;
    }

    // Insert a new menu
    public boolean insertMenu(Menu menu, int menuId) throws SQLException {
        String sql = "INSERT INTO Menu (id, LastUpdate) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, menuId);
            stmt.setDate(2, new java.sql.Date(menu.getLastUpdate().getTime()));
            return stmt.executeUpdate() > 0;
        }
    }

    // Get menu by ID
    public Menu getMenuById(int menuId, MenuItemDAO itemDAO) throws SQLException {
        String sql = "SELECT * FROM Menu WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, menuId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Menu menu = new Menu();
                    menu.getMenuItems().addAll(itemDAO.getMenuItemsByMenu(menuId));
                    return menu;
                }
            }
        }
        return null;
    }

    // Update menu last update
    public boolean updateLastUpdate(int menuId, Date lastUpdate) throws SQLException {
        String sql = "UPDATE Menu SET LastUpdate = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(lastUpdate.getTime()));
            stmt.setInt(2, menuId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Delete menu
    public boolean deleteMenu(int menuId) throws SQLException {
        String sql = "DELETE FROM Menu WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, menuId);
            return stmt.executeUpdate() > 0;
        }
    }
}
