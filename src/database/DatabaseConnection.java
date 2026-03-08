package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Modify these variables to match your MySQL setup
    private static final String URL = "jdbc:mysql://localhost:3306/bank_db";
    private static final String USERNAME = "root"; // Default username is often root
    private static final String PASSWORD = "Lucky5369420$$$";     // Try your MySQL password here, leave empty if no password

    /**
     * Connects to the database and returns the connection.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load the MySQL JDBC driver (optional in newer versions but good practice)
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found. Make sure to add the JDBC JAR to your project.");
            e.printStackTrace();
            throw new SQLException("Driver not found", e);
        }
    }
}
