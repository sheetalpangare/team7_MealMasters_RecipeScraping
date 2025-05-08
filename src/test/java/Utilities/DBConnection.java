package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnection {
	
	private static String jdbcUrl = "jdbc:postgresql://localhost:5432/Receipe_db";

    private static String username = "postgres";

    private static String password = "password";
	
	public static Connection getConn() throws Exception {

	      Class.forName("org.postgresql.Driver");
	      System.out.println("jdbcUrl, username, password: " + jdbcUrl +  "," + username + "," + password);
	      return DriverManager.getConnection(jdbcUrl, username, password);

	}
	
	public static void insertReceipe(ReceipePojo receipe) throws Exception {
		String sql = "INSERT INTO LFV_Eliminate_new (recipe_id, recipe_name, recipe_category, food_category, ingredients, "
				+ "preparation_time, cooking_time, tag, no_of_servings, cuisine_category, "
				+ "recipe_description, preparation_method, nutrient_values, recipe_url) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		Connection conn = getConn();
		PreparedStatement preparedstatement = conn.prepareStatement(sql);
		preparedstatement.setString(1, receipe.recipe_id);
		preparedstatement.setString(2, receipe.recipe_name);
		preparedstatement.setString(3, receipe.recipe_category);
		preparedstatement.setString(4, receipe.food_category);
		preparedstatement.setString(5, receipe.ingredients);
		preparedstatement.setString(6, receipe.preparation_time);
		preparedstatement.setString(7, receipe.cooking_time);
		preparedstatement.setString(8, receipe.tag);
		preparedstatement.setString(9, receipe.no_of_servings);
		preparedstatement.setString(10, receipe.cuisine_category);
		preparedstatement.setString(11, receipe.recipe_description);
		preparedstatement.setString(12, receipe.preparation_method);
		preparedstatement.setString(13, receipe.nutrient_values);
		preparedstatement.setString(14, receipe.recipe_URL);
		
		preparedstatement.executeUpdate();
	
}


	public static void closeConn(Connection conn) {

	   try {

	         conn.close();

	      } catch (SQLException e) {

	         e.printStackTrace();

	     }

	  }

}