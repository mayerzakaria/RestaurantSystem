/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

 

package util;  // or any package name you like

import java.sql.*;


public class DB {

    private static final String URL = "jdbc:mysql://localhost:3306/RestaurantOrderSystem";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    private static Connection connection = null;

    // private constructor to prevent instantiation
    private DB() { }

    public static Connection getConnection() throws SQLException 
    {
        if (connection == null || connection.isClosed()) {
            try {
                // Load driver (optional for newer drivers; might help compatibility)
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                // Handle error — driver not found
                e.printStackTrace();
                throw new SQLException("MySQL JDBC Driver not found.", e);
            }

            // Get a new connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    public static void closeConnection() 
    {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
