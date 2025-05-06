package testcases;

import java.util.List;

public class Recipe {

    // Fields corresponding to expected SQL columns
    // Basic Recipe Data:
    //   Recipe_ID, Recipe_Name, Recipe_Category, Food_Category, Ingredients,
    //   Preparation_Time, Cooking_Time, Tag, No_of_servings, Cuisine_category,
    //   Recipe_Description, Preparation_method, Nutrient_values, Recipe_URL
    private String recipeId;            // Recipe_ID
    private String recipeName;          // Recipe_Name (you can use this in place of recipeTitle)
    private String recipeCategory;      // Recipe_Category
    private String foodCategory;        // Food_Category
    private String ingredients;         // Ingredients
    private String preparationTime;     // Preparation_Time
    private String cookingTime;         // Cooking_Time
    private String tags;                // Tag (or Tags)
    private int noOfServings;        // No_of_servings
    private String cuisineCategory;     // Cuisine_category
    private String recipeDescription;   // Recipe_Description
    private String preparationMethod;   // Preparation_method
    private String nutrientValues;      // Nutrient_values
    private String recipeUrl;           // Recipe_URL

    // Additional fields for filtering and categorization from the Excel file:
    private String subCategory;         // For the "subcategory" column (e.g., "Roti", "Soup", etc.)
    private String foodProcessing;      // For the "foodprocessing" column (e.g., "Raw", "Steamed", "Porched", etc.)
    private String foodProcessingToAvoid; // For the "foodprocessingtoavoid" column

    // (Optional) Fields if you wish to store LFV filtering values:
    private String lfvElimination;      // LFV -Elimination
    private String lfvToAdd;            // LFV-To add
    private String lfvAllergyMilk;      // LFV -Allergy -Milk
    private String lfvAllergyNut;       // LFV -Allergy - Nut

    // Boolean flags for recipe avoidance in diets
    private boolean lfvRecipesToAvoid;
    private boolean lchfRecipesToAvoid;
    private List<String> allergyTags; // Stores allergens like "peanuts", "gluten", etc.
    
    // Constructor with the essential fields (mapping to expected SQL columns)
    public Recipe(String recipeId, String recipeName, String recipeDescription, String ingredients,
                  String preparationTime, String cookingTime, String preparationMethod, int noOfServings,
                  String cuisineCategory, String foodCategory, String recipeCategory, String tags,
                  String nutrientValues, String recipeUrl) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeDescription = recipeDescription;
        this.ingredients = ingredients;
        this.preparationTime = preparationTime;
        this.cookingTime = cookingTime;
        this.preparationMethod = preparationMethod;
        this.noOfServings = noOfServings;
        this.cuisineCategory = cuisineCategory;
        this.foodCategory = foodCategory;
        this.recipeCategory = recipeCategory;
        this.tags = tags;
        this.nutrientValues = nutrientValues;
        this.recipeUrl = recipeUrl;
    }
    
    // Getters and Setters for Basic Recipe Data
    public String getRecipeId() {
        return recipeId;
    }
    
    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }
    
    public String getRecipeName() {
        return recipeName;
    }
    
    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }
    
    public String getRecipeCategory() {
        return recipeCategory;
    }
    
    public void setRecipeCategory(String recipeCategory) {
        this.recipeCategory = recipeCategory;
    }
    
    public String getFoodCategory() {
        return foodCategory;
    }
    
    public void setFoodCategory(String foodCategory) {
        this.foodCategory = foodCategory;
    }
    
    public String getIngredients() {
        return ingredients;
    }
    
    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
    
    public String getPreparationTime() {
        return preparationTime;
    }
    
    public void setPreparationTime(String preparationTime) {
        this.preparationTime = preparationTime;
    }
    
    public String getCookingTime() {
        return cookingTime;
    }
    
    public void setCookingTime(String cookingTime) {
        this.cookingTime = cookingTime;
    }
    
    public String getTags() {
        return tags;
    }
    
    public void setTags(String tags) {
        this.tags = tags;
    }
    
    public int getNoOfServings() {
        return noOfServings;
    }
    
    public void setNoOfServings(int noOfServings) {
        this.noOfServings = noOfServings;
    }
    
    public String getCuisineCategory() {
        return cuisineCategory;
    }
    
    public void setCuisineCategory(String cuisineCategory) {
        this.cuisineCategory = cuisineCategory;
    }
    
    public String getRecipeDescription() {
        return recipeDescription;
    }
    
    public void setRecipeDescription(String recipeDescription) {
        this.recipeDescription = recipeDescription;
    }
    
    public String getPreparationMethod() {
        return preparationMethod;
    }
    
    public void setPreparationMethod(String preparationMethod) {
        this.preparationMethod = preparationMethod;
    }
    
    public String getNutrientValues() {
        return nutrientValues;
    }
    
    public void setNutrientValues(String nutrientValues) {
        this.nutrientValues = nutrientValues;
    }
    
    public String getRecipeUrl() {
        return recipeUrl;
    }
    
    public void setRecipeUrl(String recipeUrl) {
        this.recipeUrl = recipeUrl;
    }
    
    // Getters and Setters for Additional Fields
    public String getSubCategory() {
        return subCategory;
    }
    
    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }
    
    public String getFoodProcessing() {
        return foodProcessing;
    }
    
    public void setFoodProcessing(String foodProcessing) {
        this.foodProcessing = foodProcessing;
    }
    
    public String getFoodProcessingToAvoid() {
        return foodProcessingToAvoid;
    }
    
    public void setFoodProcessingToAvoid(String foodProcessingToAvoid) {
        this.foodProcessingToAvoid = foodProcessingToAvoid;
    }
    
    public String getLfvElimination() {
        return lfvElimination;
    }
    
    public void setLfvElimination(String lfvElimination) {
        this.lfvElimination = lfvElimination;
    }
    
    public String getLfvToAdd() {
        return lfvToAdd;
    }
    
    public void setLfvToAdd(String lfvToAdd) {
        this.lfvToAdd = lfvToAdd;
    }
    
    public String getLfvAllergyMilk() {
        return lfvAllergyMilk;
    }
    
    public void setLfvAllergyMilk(String lfvAllergyMilk) {
        this.lfvAllergyMilk = lfvAllergyMilk;
    }
    
    public String getLfvAllergyNut() {
        return lfvAllergyNut;
    }
    
    public void setLfvAllergyNut(String lfvAllergyNut) {
        this.lfvAllergyNut = lfvAllergyNut;
    }
    
    public boolean isLfvRecipesToAvoid() {
        return lfvRecipesToAvoid;
    }
    
    public void setLfvRecipesToAvoid(boolean lfvRecipesToAvoid) {
        this.lfvRecipesToAvoid = lfvRecipesToAvoid;
    }
    
    public boolean isLchfRecipesToAvoid() {
        return lchfRecipesToAvoid;
    }
    
    public void setLchfRecipesToAvoid(boolean lchfRecipesToAvoid) {
        this.lchfRecipesToAvoid = lchfRecipesToAvoid;
    }
    
    public void setAllergyTags(List<String> allergyTags) {
        this.allergyTags = allergyTags;
    }

    public List<String> getAllergyTags() {
        return allergyTags;
    }
    
    public boolean hasAllergy() {
        return allergyTags != null && !allergyTags.isEmpty();
    }
    
    @Override
    public String toString() {
        return "Recipe [Recipe ID=" + recipeId + ", Recipe Name=" + recipeName 
                + ", Recipe Category=" + recipeCategory + ", Food Category=" + foodCategory
                + ", Ingredients=" + ingredients + ", Preparation Time=" + preparationTime
                + ", Cooking Time=" + cookingTime + ", Tag=" + tags + ", No_of_servings=" + noOfServings
                + ", Cuisine category=" + cuisineCategory + ", Recipe Description=" + recipeDescription
                + ", Preparation_method=" + preparationMethod + ", Nutrient_values=" + nutrientValues
                + ", Recipe_URL=" + recipeUrl + "]";
    }

	


}