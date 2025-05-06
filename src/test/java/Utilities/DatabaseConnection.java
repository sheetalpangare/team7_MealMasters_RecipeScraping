package Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {
	
	static ConfigReader configReader;

    private static final String URL = "jdbc:postgresql://localhost:5432/recipescraping_db";

    public static Connection getConnection() throws SQLException {

    	configReader = new ConfigReader();	
		Connection connection = DriverManager.getConnection(URL, configReader.getUsername(), configReader.getPassword());
		return connection;
    }
}
