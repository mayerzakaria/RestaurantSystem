/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

 

package util;  // or any package name you like

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
private static final String URL = "jdbc:mysql://localhost:3306/RestaurantOrderSystem";
private static final String USER = "root";
  private static final String PASSWORD = ""; 


    private static Connection connection = null;

    // private constructor to prevent instantiation
    private DB() {} 
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
    

