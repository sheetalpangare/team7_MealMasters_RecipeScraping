package base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import testcases.Recipe;

public class DBConnection {
    private static final String BASE_URL = "jdbc:postgresql://localhost:5432/";
    private static final String DB_NAME = "recipes_scraping";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(BASE_URL + DB_NAME, USERNAME, PASSWORD);
    }

    public void createDatabase() throws SQLException {
        try (Connection tempConn = DriverManager.getConnection(BASE_URL, USERNAME, PASSWORD);
             Statement stmt = tempConn.createStatement()) {
            stmt.executeUpdate("DROP DATABASE IF EXISTS " + DB_NAME);
            stmt.executeUpdate("CREATE DATABASE " + DB_NAME);
        }
    }

    public void createTable(String tableName) {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " ("
                + "recipe_id TEXT PRIMARY KEY,"
                + "recipe_name TEXT NOT NULL,"
                + "recipe_category TEXT,"
                + "food_category TEXT,"
                + "ingredients TEXT,"
                + "preparation_time TEXT,"
                + "cooking_time TEXT,"
                + "tags TEXT,"
                + "no_of_servings INTEGER,"  // Updated to match Recipe class
                + "cuisine_category TEXT,"
                + "recipe_description TEXT,"
                + "preparation_method TEXT,"
                + "nutrient_values TEXT,"  // Fixed naming
                + "recipe_url TEXT,"
                + ");";

        try (Connection conn = this.connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    public void insertData(String tableName, Recipe recipe) {
        String sql = "INSERT INTO " + tableName + " (recipe_id, recipe_name, recipe_category, food_category, "
                + "ingredients, preparation_time, cooking_time, tags, no_of_servings, cuisine_category, "
                + "recipe_description, preparation_method, nutrient_values, recipe_url, lfv_recipes_to_avoid, lchf_recipes_to_avoid) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, recipe.getRecipeId());
            pstmt.setString(2, recipe.getRecipeName());
            pstmt.setString(3, recipe.getRecipeCategory());
            pstmt.setString(4, recipe.getFoodCategory());
            pstmt.setString(5, recipe.getIngredients());
            pstmt.setString(6, recipe.getPreparationTime());
            pstmt.setString(7, recipe.getCookingTime());
            pstmt.setString(8, recipe.getTags());
            pstmt.setInt(9, recipe.getNoOfServings());  // Updated to match Recipe class
            pstmt.setString(10, recipe.getCuisineCategory());
            pstmt.setString(11, recipe.getRecipeDescription());
            pstmt.setString(12, recipe.getPreparationMethod());
            pstmt.setString(13, recipe.getNutrientValues()); // Fixed naming
            pstmt.setString(14, recipe.getRecipeUrl());
          
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting data: " + e.getMessage());
        }
    }
}