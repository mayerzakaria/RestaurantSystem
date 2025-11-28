/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurantsystem.dao;

import restaurantsystem.Table;
import restaurantsystem.Table.TableStatus;

import java.sql.*;
import java.util.ArrayList;

public class TableDAO 
{
    private Connection conn;

    public TableDAO(Connection conn) {
        this.conn = conn;
    }

    // Insert new table
    public boolean insertTable(Table table, String cashierId) throws SQLException {
        String sql = "INSERT INTO `Table` (tablenumber, capacity, Tablestatus, Cashierid) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, table.getTableNumber());
            stmt.setInt(2, table.getCapacity());
            stmt.setString(3, table.getStatus().name());
            stmt.setString(4, cashierId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Update table status
    public boolean updateTableStatus(int tableNumber, String cashierId, TableStatus status) throws SQLException {
        String sql = "UPDATE `Table` SET Tablestatus = ? WHERE tablenumber = ? AND Cashierid = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setInt(2, tableNumber);
            stmt.setString(3, cashierId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Get table by number
    public Table getTable(int tableNumber, String cashierId) throws SQLException {
        String sql = "SELECT * FROM `Table` WHERE tablenumber = ? AND Cashierid = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tableNumber);
            stmt.setString(2, cashierId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Table(
                        rs.getInt("tablenumber"),
                        rs.getInt("capacity"),
                        TableStatus.valueOf(rs.getString("Tablestatus"))
                    );
                }
            }
        }
        return null;
    }

    // Get all tables for a cashier
    public ArrayList<Table> getTablesByCashier(String cashierId) throws SQLException {
        ArrayList<Table> tables = new ArrayList<>();
        String sql = "SELECT * FROM `Table` WHERE Cashierid = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cashierId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tables.add(new Table(
                        rs.getInt("tablenumber"),
                        rs.getInt("capacity"),
                        TableStatus.valueOf(rs.getString("Tablestatus"))
                    ));
                }
            }
        }
        return tables;
    }
}

