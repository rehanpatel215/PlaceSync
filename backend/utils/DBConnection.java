package backend.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class DBConnection {
    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Properties props = new Properties();
                props.load(new FileInputStream("res/config.properties"));
                
                String url = props.getProperty("db.url");
                String user = props.getProperty("db.user");
                String pass = props.getProperty("db.password");
                
                connection = DriverManager.getConnection(url, user, pass);
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
