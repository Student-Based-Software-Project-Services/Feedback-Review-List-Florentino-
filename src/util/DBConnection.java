package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    
    private static Connection conn;
    private static final String URL = "jdbc:mysql://localhost:3306/db_feedback_review";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    
    public static Connection getConnection() {
        conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    public static void close() {
        try {
            conn.close();
            System.out.println("Database connection closed!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}