package backend.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class DBConnection {
    public static Connection getConnection() {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("res/config.properties"));
            
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String pass = props.getProperty("db.password");
            
            return DriverManager.getConnection(url, user, pass);
        } catch (IOException | SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            return null;
        }
    }
}
