package testcases;

import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import base.DBConnection;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class RecipeScrapers {

	// Lists to hold recipes in various categories
	List<Recipe> allRecipesList = new ArrayList<>();

	// Food Category classification (Vegan, Vegetarian, Jain, Eggitarian, Non-Veg)
	List<Recipe> veganRecipes = new ArrayList<>();
	List<Recipe> vegetarianRecipes = new ArrayList<>();
	List<Recipe> jainRecipes = new ArrayList<>();
	List<Recipe> eggitarianRecipes = new ArrayList<>();
	List<Recipe> nonVegRecipes = new ArrayList<>();

	// Existing LFV and LCHF recipe filtering lists
	List<Recipe> lchfAddRecipes = new ArrayList<>();
	List<Recipe> lchfEliminationRecipes = new ArrayList<>();
	List<Recipe> lfvEliminationRecipes = new ArrayList<>();
	List<Recipe> lfvAddRecipes = new ArrayList<>();
	List<Recipe> lfvToAddRecipes = new ArrayList<>();
	List<Recipe> lfvNutAllergyEliminationRecipes = new ArrayList<>();
	List<Recipe> lfvOtherAllergyEliminationRecipes = new ArrayList<>();
	List<Recipe> lchfNutAllergyEliminationRecipes = new ArrayList<>();
	List<Recipe> lchfOtherAllergyEliminationRecipes = new ArrayList<>();
	List<Recipe> lfvOptionalRecipes = new ArrayList<>();

	private static final Logger logger = LoggerFactory.getLogger(RecipeScrapers.class);

	// Table names used for DB insertion
	String[] tableNames = { "recipes", "lchfEliminatedRecipe", "lchfAddRecipes", "lfvEliminationRecipes",
			"lfvAddRecipes", "lfvToAddRecipes", "lfvnutallergy", "lfvotherallergy", "lchfnutallergy",
			"lchfotherallergy", "lfvOptionalRecipes", "Vegan", "Vegetarian", "Jain", "Eggitarian", "NonVeg" };

	@Test
	public void RecipeScrape() throws SQLException {

		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--blink-settings=imagesEnabled=false");
		options.addArguments("--disable-images");
		// options.addArguments("--disable-javascript");
		options.addArguments("--remote-allow-origins=*");
		options.addArguments("--headless");
		options.addArguments("--disable-popup-blocking");
		options.addArguments("--disable-notifications");
		options.addArguments("--disable-extensions");
		options.addArguments("enable-automation");
		options.addArguments("--no-sandbox");
		options.addArguments("--dns-prefetch-disable");
		options.addArguments("--disable-gpu");
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--disable-software-rasterizer");
		options.addArguments("--disable-features=SharedStorageAPI");

		WebDriver driver = new ChromeDriver(options);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		DBConnection db = new DBConnection();
		db.createDatabase();
		db.connect();
		for (String tableName : tableNames) {
			db.createTable(tableName);
		}

		try {
			// Navigate to main recipes page (update URL as necessary)
			driver.get("https://m.tarladalal.com/recipes");
			driver.manage().window().maximize();

			// Scroll to trigger dynamic content load
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollBy(0, 500)");

			// Collect recipe links on the page based on known locator
			List<WebElement> recipeLinks = new WebDriverWait(driver, Duration.ofSeconds(10))
					.until(d -> d.findElements(By.xpath("//span[@class='rcc_recipename']/a")));
			for (WebElement link : recipeLinks) {
				String recipeUrl = ((JavascriptExecutor) driver)
						.executeScript("return arguments[0].getAttribute('href');", link).toString();
			}

			// Iterate through subsequent pages if a "Next" button exists
			boolean hasNext = true;
			while (hasNext) {
				ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
				String parentTab = driver.getWindowHandle();
				for (String tab : tabs) {
					if (!tab.equals(parentTab)) {
						driver.switchTo().window(tab);
						try {
							recipeDataScraper(driver);
						} catch (Exception e) {
							logger.error("Error scraping recipe on tab " + tab + ": " + e.getMessage());
						}
						driver.close();
					}
				}
				driver.switchTo().window(parentTab);
				try {
					WebElement nextButton = driver.findElement(By.xpath("//a[@class='next']"));
					if (nextButton.isDisplayed()) {
						nextButton.click();
						wait.until(ExpectedConditions
								.presenceOfAllElementsLocatedBy(By.xpath("//span[@class='rcc_recipename']/a")));
						recipeLinks = driver.findElements(By.xpath("//span[@class='rcc_recipename']/a"));
						for (WebElement link : recipeLinks) {
							String recipeUrl = ((JavascriptExecutor) driver)
									.executeScript("return arguments[0].getAttribute('href');", link).toString();
						}
					} else {
						hasNext = false;
					}
				} catch (Exception e) {
					hasNext = false;
				}
			}
		} catch (Exception e) {
			logger.error("Error during main scraping loop: " + e.getMessage());
		} finally {

			// --- LFV / LCHF Filtering ---
			lchfEliminationRecipes = filterRecipes(allRecipesList, RecipeDetails.LCHF_ELIMINATE, true);
			lchfAddRecipes = filterRecipes(lchfEliminationRecipes, RecipeDetails.LCHF_ADD, false);
			lchfAddRecipes = lchfAddRecipes.stream().filter(rec -> !rec.isLchfRecipesToAvoid())
					.collect(Collectors.toList());

			lfvEliminationRecipes = filterRecipes(allRecipesList, RecipeDetails.LFV_ELIMINATE, true);
			lfvAddRecipes = filterRecipes(lfvEliminationRecipes, RecipeDetails.LFV_ADD, false);
			lfvAddRecipes = lfvAddRecipes.stream().filter(rec -> !rec.isLfvRecipesToAvoid())
					.collect(Collectors.toList());

			lfvEliminationRecipes = filterRecipes(allRecipesList, RecipeDetails.LFV_ELIMINATE, true);
			lfvToAddRecipes = filterRecipes(lfvToAddRecipes, RecipeDetails.LFV_TO_ADD, false);
			lfvToAddRecipes.addAll(lfvAddRecipes);

			lfvNutAllergyEliminationRecipes = filterRecipes(lfvAddRecipes, RecipeDetails.ALLERGY, true);
			lfvOtherAllergyEliminationRecipes = filterRecipes(lfvAddRecipes, RecipeDetails.ALLERGY, true);
			lchfNutAllergyEliminationRecipes = filterRecipes(lchfAddRecipes, RecipeDetails.ALLERGY, true);
			lchfOtherAllergyEliminationRecipes = filterRecipes(lchfAddRecipes, RecipeDetails.ALLERGY, true);

			logger.info("LCHF Eliminated Recipes: " + lchfEliminationRecipes.size());
			logger.info("LCHF Add Recipes: " + lchfAddRecipes.size());
			logger.info("LFV Eliminated Recipes: " + lfvEliminationRecipes.size());
			logger.info("LFV Add Recipes: " + lfvAddRecipes.size());
			logger.info("Partial Vegan LFV Recipes: " + lfvToAddRecipes.size());

			// --- Classify recipes into Food Category lists
			for (Recipe rec : allRecipesList) {
				String fc = rec.getFoodCategory().toLowerCase();
				if (fc.equals("vegan")) {
					veganRecipes.add(rec);
				} else if (fc.equals("vegetarian")) {
					vegetarianRecipes.add(rec);
				} else if (fc.equals("jain")) {
					jainRecipes.add(rec);
				} else if (fc.equals("eggitarian")) {
					eggitarianRecipes.add(rec);
				} else if (fc.equals("non-veg") || fc.equals("non veg")) {
					nonVegRecipes.add(rec);
				}
			}

			logger.info("Vegan Recipes: " + veganRecipes.size());
			logger.info("Vegetarian Recipes: " + vegetarianRecipes.size());
			logger.info("Jain Recipes: " + jainRecipes.size());
			logger.info("Eggitarian Recipes: " + eggitarianRecipes.size());
			logger.info("Non-Veg Recipes: " + nonVegRecipes.size());

			// Insert recipes into the database
			insertRecipesIntoTable("recipes", allRecipesList);
			insertRecipesIntoTable("LCHFEliminatedRecipe", lchfEliminationRecipes);
			insertRecipesIntoTable("lchfAddRecipes", lchfAddRecipes);
			insertRecipesIntoTable("lfvEliminationRecipes", lfvEliminationRecipes);
			insertRecipesIntoTable("lfvAddRecipes", lfvAddRecipes);
			insertRecipesIntoTable("lfvToAddRecipes", lfvToAddRecipes);
			insertRecipesIntoTable("lfvnutallergy", lfvNutAllergyEliminationRecipes);
			insertRecipesIntoTable("lfvotherallergy", lfvOtherAllergyEliminationRecipes);
			insertRecipesIntoTable("lchfnutallergy", lchfNutAllergyEliminationRecipes);
			insertRecipesIntoTable("lchfotherallergy", lchfOtherAllergyEliminationRecipes);
			insertRecipesIntoTable("lfvOptionalRecipes", lfvOptionalRecipes);
			insertRecipesIntoTable("Vegan", veganRecipes);
			insertRecipesIntoTable("Vegetarian", vegetarianRecipes);
			insertRecipesIntoTable("Jain", jainRecipes);
			insertRecipesIntoTable("Eggitarian", eggitarianRecipes);
			insertRecipesIntoTable("NonVeg", nonVegRecipes);

			if (driver != null) {
				driver.quit();
			}
		}
	}

	private void insertRecipesIntoTable(String tableName, List<Recipe> allRecipesList) {
		if (allRecipesList == null || allRecipesList.isEmpty()) {
			System.out.println("No recipes to insert.");
			return;
		}

		DBConnection db = new DBConnection(); // Initialize DB connection

		try {
			for (Recipe recipe : allRecipesList) {
				db.insertData(tableName, recipe); // Call DBConnection's insertData method
			}
			System.out.println("Recipes inserted successfully into table: " + tableName);
		} catch (Exception e) {
			System.out.println("Error inserting recipes: " + e.getMessage());
		}
	}

	private List<Recipe> filterRecipes(List<Recipe> allRecipesList, String filterType, boolean eliminate) {
		if (allRecipesList == null || allRecipesList.isEmpty()) {
			System.out.println("Recipe list is empty or null!");
			return Collections.emptyList(); // Prevent null reference issues
		}

		return allRecipesList.stream().filter(recipe -> recipe != null && applyFilter(recipe, filterType, eliminate))
				.collect(Collectors.toList());
	}

	private boolean applyFilter(Recipe recipe, String filterType, boolean eliminate) {
		if (recipe == null)
			return false; // Prevent errors due to null recipe objects

		if (filterType.equalsIgnoreCase(RecipeDetails.LCHF_ELIMINATE))
			return recipe.isLchfRecipesToAvoid();
		else if (filterType.equalsIgnoreCase(RecipeDetails.LFV_ELIMINATE))
			return recipe.isLfvRecipesToAvoid();
		else if (filterType.equalsIgnoreCase(RecipeDetails.ALLERGY))
			return recipe.hasAllergy();
		else {
			System.out.println("Unknown filter type encountered: " + filterType);
			return true; // Default case keeps the recipe
		}
	}

	public void recipeDataScraper(WebDriver driver) {
		boolean lfvRecipesToAvoid = false;
		boolean lchfRecipesToAvoid = false;

		// Extract recipe ID (assumes ID is the last hyphen-separated token with a
		// trailing "r")
		String recipeUrl = driver.getCurrentUrl();
		String[] parts = recipeUrl.split("-");
		String recipeId = parts[parts.length - 1].replace("r", "");
		logger.info("-------------------------------------------------");
		logger.info("Recipe Id : " + recipeId);

		String recipeName = null;

		// Scrape recipe name safely
		try {
		    WebElement recipeNameElement = driver.findElement(By.xpath("//div[@id='recipehead']//span//span"));
		    recipeName = (recipeNameElement != null) ? recipeNameElement.getText() : "";

		    // Ensure filterList is not null before using toLowerCase()
		    String[] filterList = RecipeDetails.LFV_RECIPE_TO_AVOID;
		    boolean lfvRecipeToAvoid = false;

		    if (filterList != null && filterList.length > 0 && recipeName != null && !recipeName.isEmpty()) {
		        // Ensure recipeName is safely handled before calling `.toLowerCase()`
		        String safeRecipeName = (recipeName != null) ? recipeName.toLowerCase() : "";

		        lfvRecipeToAvoid = Arrays.stream(filterList)
		                .map(String::toLowerCase)
		                .anyMatch(word -> safeRecipeName.contains(word));
		    } else {
		        logger.warn("LFV_RECIPE_TO_AVOID or recipe name is null or empty!");
		    }

		    logger.info("Recipe Name : " + recipeName);
		    logger.info("LFV Avoidance Status: " + lfvRecipeToAvoid);
		} catch (Exception e) {
		    logger.error("Error retrieving recipe title: " + e.getMessage());
		}

		logger.info("Recipe Name : " + recipeName);

		// Scrape preparation and cooking times
		String preparationTime = driver.findElement(By.xpath("//time[@itemprop='prepTime']")).getText();
		String cookingTime = driver.findElement(By.xpath("//time[@itemprop='cookTime']")).getText();
		logger.info("Preparation Time : " + preparationTime);
		logger.info("Cooking Time : " + cookingTime);

		// Scrape ingredients
		List<WebElement> ingredientsLoc = driver.findElements(By.xpath("//span[@itemprop='recipeIngredient']"));
		StringBuilder ingredients = new StringBuilder();
		for (WebElement e : ingredientsLoc) {
			if (!lchfRecipesToAvoid) {
				lchfRecipesToAvoid = e.getText().toLowerCase().contains("processed");
			}
			ingredients.append(" ").append(e.getText());
		}
		logger.info("Ingredients : " + ingredients.toString());

		// Scrape ingredient names (if available)
		List<WebElement> ingredientsNameLoc = driver
				.findElements(By.xpath("//span[@itemprop='recipeIngredient']/a/span"));
		StringBuilder ingredientsName = new StringBuilder();
		for (WebElement e : ingredientsNameLoc) {
			ingredientsName.append("\n").append(e.getText());
		}
		logger.info("Ingredients Name : " + ingredientsName.toString());

		// Scrape number of servings
		String numOfServings = driver.findElement(By.xpath("//span[@id='ctl00_cntrightpanel_lblServes']")).getText();
		logger.info("No of Servings : " + numOfServings);

		// Scrape recipe tags safely
		List<WebElement> tagsLoc = driver.findElements(By.xpath("//div[@id='recipe_tags']/a"));
		StringBuilder tags = new StringBuilder();

		// Ensure filter list is properly initialized
		String[] filterList = RecipeDetails.LFV_RECIPE_TO_AVOID;
		boolean lfvRecipesToAvoid1 = false;

		for (WebElement tag : tagsLoc) {
			String tagText = tag.getText();
			if (tagText != null && !tagText.isEmpty()) {
				if (filterList != null && filterList.length > 0 && tagText != null && !tagText.isEmpty()) {

					lfvRecipesToAvoid1 = Arrays.stream(filterList).map(String::toLowerCase)
							.anyMatch(word -> tagText.toLowerCase().contains(word));
				}
			}
			tags.append(" ").append(tagText);
		}

		logger.info("Recipe Tags: " + tags.toString());
		logger.info("LFV Avoidance Status: " + lfvRecipesToAvoid1);

		// Scrape recipe description
		String recipeDescription = driver.findElement(By.id("recipe_description")).getText();
		logger.info("Recipe Description : " + recipeDescription);

		// Scrape preparation method steps
		List<WebElement> prepMethodLoc = driver
				.findElements(By.xpath("//*[@id='recipe_small_steps']/span[1]//span[@itemprop='text']"));
		StringBuilder preparationMethod = new StringBuilder();
		for (WebElement method : prepMethodLoc) {
			preparationMethod.append(" ").append(method.getText());
		}
		logger.info("Preparation Method : " + preparationMethod.toString());

		// Scrape nutrition values
		List<WebElement> nutritionLoc = driver.findElements(By.xpath("//*[@id='rcpnutrients']//tr"));
		StringBuilder nutritionValues = new StringBuilder();
		for (WebElement nutrition : nutritionLoc) {
			nutritionValues.append(" ").append(nutrition.getText());
		}
		logger.info("Nutrition Values : " + nutritionValues.toString());

		// --- New Filtering and Classification Based on SubCategory, FoodProcessing,
		// and FoodProcessingToAvoid ---
		String subCategory = "";
		String processingMethod = "";
		boolean avoidDueToProcessing = false;

		// Ensure `recipeTitle` and `tags` are properly initialized
		String safeRecipeName = recipeName != null ? recipeName : "";
		String safeTags = tags != null ? tags.toString() : "";

		// Combine text safely
		String combinedForSubCat = (safeRecipeName + " " + safeTags).toLowerCase();

		// Identify SubCategory
		for (String subCat : RecipeDetails.SUBCATEGORY) {
			if (combinedForSubCat.contains(subCat.toLowerCase())) {
				subCategory = subCat;
				break;
			}
		}
		logger.info("SubCategory: " + subCategory);

		// Ensure preparationMethod is not null
		String prepMethodText = "";
		try {
			WebElement prepMethodElement = driver.findElement(By.xpath("//div[@id='prepMethod']"));
			if (prepMethodElement != null) {
				prepMethodText = prepMethodElement.getText().toLowerCase();
			}
		} catch (Exception e) {
			logger.warn("Preparation method element not found.");
		}

		processingMethod = ""; // Initialize processingMethod properly

		// Verify FOOD_PROCESSING array is not null before looping
		if (RecipeDetails.FOOD_PROCESSING != null && RecipeDetails.FOOD_PROCESSING.length > 0) {
			for (String method : RecipeDetails.FOOD_PROCESSING) {
				if (method != null && prepMethodText.contains(method.toLowerCase())) {
					processingMethod = method;
					break; // Stop looping once a match is found
				}
			}
		} else {
			logger.warn("RecipeDetails.FOOD_PROCESSING is null or empty!");
		}

		logger.info("Food Processing Method: " + processingMethod);

		// Check if undesired food processing occurs using FoodProcessingToAvoid list
		for (String avoidTerm : RecipeDetails.FOOD_PROCESSING) {
			if (prepMethodText.contains(avoidTerm.toLowerCase())) {
				avoidDueToProcessing = true;
				break;
			}
		}
		logger.info("Recipe flagged to avoid due to processing: " + avoidDueToProcessing);

		// Determine Food Processing method from the preparation method text
		String processingMethod1 = "";
		String prepMethodText1 = preparationMethod.toString();
		for (String method : RecipeDetails.FOOD_PROCESSING) {
			if (prepMethodText1.toLowerCase().contains(method.toLowerCase())) {
				processingMethod1 = method;
				break;
			}
		}
		logger.info("Food Processing Method: " + processingMethod1);

		// Check if undesired processing occurs using FoodProcessingToAvoid list 

		// Ensure LFV_RECIPE_TO_AVOID and prepMethodText are not null
		if (RecipeDetails.LFV_RECIPE_TO_AVOID != null && prepMethodText != null && !prepMethodText.isEmpty()) {
		    boolean avoidDueToProcessing1 = Arrays.stream(RecipeDetails.LFV_RECIPE_TO_AVOID)
		            .filter(Objects::nonNull) // Filter out any potential null values in the array
		            .map(String::toLowerCase)
		            .anyMatch(term -> prepMethodText1.toLowerCase().contains(term));

		    if (avoidDueToProcessing1) {
		        logger.info("Recipe flagged to avoid due to undesired processing: " + recipeName);
		        // Optionally, you can skip adding this recipe or mark it;
		        // For example: recipe.setProcessingToAvoid(true);
		    }
		} else {
		    logger.warn("LFV_RECIPE_TO_AVOID or prepMethodText is null or empty!");
		}

		// Determine Food Category based on tags and ingredient names (default
		// "Vegetarian")
		String foodCategory = "Vegetarian";
		String combinedText = (tags.toString() + ingredientsName.toString()).toLowerCase();
		boolean isEggetarian = !Arrays.stream(RecipeDetails.EGGETARIAN_ELIMINATE_OPTIONS)
				.anyMatch(combinedText::contains);
		boolean isVegan = !Arrays.stream(RecipeDetails.VEGAN_ELIMINATE_OPTIONS).anyMatch(combinedText::contains);
		if (combinedText.contains("egg") && isEggetarian) {
			foodCategory = "Eggitarian";
		} else if (combinedText.contains("jain")) {
			foodCategory = "Jain";
		} else if (isVegan || combinedText.contains("vegan") || recipeUrl.contains("vegan")) {
			foodCategory = "Vegan";
		}
		logger.info("Food Category : " + foodCategory);

		// Determine Cuisine Category from tags and title using RecipeDetails constants
		// Assuming RecipeDetails.CUISINE_CATEGORY is defined as a comma-separated
		// String
		String combinedText2 = (tags.toString() + recipeName).toLowerCase();
		String cuisineCategory = "";
		// Split on comma and trim each resulting value
		for (String c : RecipeDetails.CUISINE_CATEGORY.split(",")) {
			String trimmedCuisine = c.trim();
			if (combinedText2.contains(trimmedCuisine.toLowerCase())) {
				cuisineCategory = trimmedCuisine + ", " + cuisineCategory;
			}
		}
		logger.info("Cuisine Category : " + cuisineCategory);

		// Determine Recipe Category (Breakfast, Lunch, Snack, Dinner, Drink) based on
		// tags
		String recipeCategory = "";
		for (String option : RecipeDetails.RECIPE_CATEGORY_OPTIONS) {
			if (tags.toString().toLowerCase().contains(option.toLowerCase())) {
				recipeCategory = option;
				break;
			}
		}
		logger.info("Recipe Category : " + recipeCategory);

		
		Recipe recipe = new Recipe(recipeId, recipeName, // this serves as recipeName
				recipeDescription, ingredients.toString(), preparationTime, cookingTime, preparationMethod.toString(),
				Integer.valueOf(numOfServings), // maps to noOfServings field
				cuisineCategory, foodCategory, recipeCategory, tags.toString(), nutritionValues.toString(), recipeUrl);

		// Set additional metadata for subcategory and food processing method.
		recipe.setSubCategory(subCategory); // For example, "Roti", "Soup", etc.
		recipe.setFoodProcessing(processingMethod); // For example, "Raw", "Porched", etc.

		// Optionally mark processing avoidance if needed:
		// recipe.setFoodProcessingToAvoid(avoidDueToProcessing);

		recipe.setRecipeId(recipeId);
		recipe.setRecipeName(recipeName);
		recipe.setIngredients(ingredients.toString());
		recipe.setPreparationTime(preparationTime); // fixed spelling
		recipe.setCookingTime(cookingTime);
		recipe.setNoOfServings(Integer.valueOf(numOfServings)); // uses the "noOfServings" field in Recipe
		recipe.setRecipeDescription(recipeDescription);
		recipe.setPreparationMethod(preparationMethod.toString());
		recipe.setNutrientValues(nutritionValues.toString());
		recipe.setTags(tags.toString());
		recipe.setCuisineCategory(cuisineCategory);
		recipe.setRecipeUrl(recipeUrl);
		recipe.setFoodCategory(foodCategory);
		recipe.setRecipeCategory(recipeCategory);

		// Set additional metadata
		recipe.setSubCategory(subCategory);
		recipe.setFoodProcessing(processingMethod);

		// Mark LFV recipes to avoid if applicable
		recipe.setLfvRecipesToAvoid(lfvRecipesToAvoid);

		// Finally, add the scraped recipe to the main list
		allRecipesList.add(recipe);
	}

	public class RecipeService {
	    private DBConnection db;

	    // Initialize DBConnection in constructor
	    public RecipeService() {
	        this.db = new DBConnection();
	    }

	    // Method to insert multiple recipes into the table
	    public void insertRecipesIntoTable(String tableName, List<Recipe> recipes) throws SQLException {
	        for (Recipe recipe : recipes) {
	            db.insertData(tableName, recipe); // Using DBConnection's method
	        }
	    }


	/**
	 * Filters the recipe list based on a given comma-separated filter string.
	 * 
	 * @param recipeList      List of recipes to filter.
	 * @param filterString    Comma-separated keywords.
	 * @param toBeNotIncluded If true, recipes containing the keyword are excluded.
	 * @return Filtered list of recipes.
	 */
	public List<Recipe> filterRecipes(List<Recipe> recipeList, String filterString, boolean toBeNotIncluded) {
		List<Recipe> filteredRecipes;
		if (toBeNotIncluded) {
			filteredRecipes = recipeList.stream().filter(rec -> !Arrays.stream(filterString.toLowerCase().split(","))
					.anyMatch(rec.getIngredients().toLowerCase()::contains)).collect(Collectors.toList());
		} else {
			filteredRecipes = recipeList.stream().filter(rec -> Arrays.stream(filterString.toLowerCase().split(","))
					.anyMatch(rec.getIngredients().toLowerCase()::contains)).collect(Collectors.toList());
		}
		logger.info("Filtered Recipes: " + filteredRecipes.size() + " for filter: " + filterString);
		return filteredRecipes;
	}
	
	}}