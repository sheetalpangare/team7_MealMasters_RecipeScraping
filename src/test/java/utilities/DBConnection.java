package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	
	public static Connection getConn() throws Exception {

	      String jdbcUrl = "jdbc:postgresql://localhost:5432/Receipe_db";

	      String username = "postgres";

	      String password = "password";

	      //Enter the username and password of your pgAdmin

	      Class.forName("org.postgresql.Driver");

	      return DriverManager.getConnection(jdbcUrl, username, password);

	}

	public static void closeConn(Connection conn) {

	   try {

	         conn.close();

	      } catch (SQLException e) {

	         e.printStackTrace();

	     }

	  }

}
