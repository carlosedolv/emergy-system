package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
	// Add final (constant)
	private static String URL;
	
	// Add getConnection method
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
