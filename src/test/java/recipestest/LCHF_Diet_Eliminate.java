package recipestest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import baseclass.BaseClass;
import io.github.bonigarcia.wdm.WebDriverManager;
import utilities.AdHandler;
import utilities.DBConnection;
import utilities.ReceipePojo;

public class LCHF_Diet_Eliminate extends BaseClass {

	// LCHF exclusion list (from your Excel for LCHF elimination)
	public static List<String> excludeIngredients = Arrays.asList("Ham", "sausage", "tinned fish", "tuna", "sardines",
			"yams", "beets", "parsnip", "turnip", "rutabagas", "carrot", "yuca", "kohlrabi", "celery root",
			"horseradish", "daikon", "jicama", "radish", "pumpkin", "squash", "Whole fat milk", "low fat milk",
			"fat free milk", "Evaporated milk", "condensed milk", "curd", "buttermilk", "ice cream", "flavored milk",
			"sweetened yogurt", "soft cheese", "grain", "Wheat", "oat", "barely", "rice", "millet", "jowar", "bajra",
			"corn", "dal", "lentil", "banana", "mango", "papaya", "plantain", "apple", "orange", "pineapple", "pear",
			"tangerine", "all melon varieties", "peach", "plum", "nectarine", "Avocado", "olive oil", "coconut oil",
			"soybean oil", "corn oil", "safflower oil", "sunflower oil", "rapeseed oil", "peanut oil", "cottonseed oil",
			"canola oil", "mustard oil", "sugar", "jaggery", "glucose", "fructose", "corn syrup", "cane sugar",
			"aspartame", "cane solids", "maltose", "dextrose", "sorbitol", "mannitol", "xylitol", "maltodextrin",
			"molasses", "brown rice syrup", "splenda", "nutra sweet", "stevia", "barley malt", "potato", "corn", "pea");

	static WebDriver driver;

	public static void main(String[] args) throws Exception {
		List<String> urls = new ArrayList<>();
		AdHandler ads = new AdHandler();

		// Setup ChromeDriver
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		Map<String, Object> prefs = new HashMap<>();
		prefs.put("profile.managed_default_content_settings.images", 2);
		options.setExperimentalOption("prefs", prefs);
		driver = new ChromeDriver(options);
		driver.manage().window().maximize();

		// Create the DB table if it doesn't exist.
		DBConnection.createTable("LCHF_Diet_Eliminate");

		// Navigate to the homepage and wait for full load.
		Thread.sleep(2000);
		driver.get("https://m.tarladalal.com/");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		// Scroll down until the page content is fully loaded.
		long initialHeight = ((Number) js.executeScript("return document.body.scrollHeight")).longValue();
		while (true) {
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
			long newHeight = ((Number) js.executeScript("return document.body.scrollHeight")).longValue();
			if (newHeight == initialHeight) {
				break;
			}
			initialHeight = newHeight;
		}

		// Click the "Recipes List" link.
		WebElement recipesLink = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Recipes List')]")));
		Assert.assertTrue(recipesLink.isDisplayed(), "Recipes List link is not visible!");
		if (driver.getCurrentUrl().equals("https://www.tarladalal.com/#google_vignette")) {
			WebElement adClose = driver.findElement(By.xpath("//*[contains(text(),'Close')]"));
			adClose.click();
			System.out.println("Closed the ad on landing page.");
		}
		recipesLink.click();

		// Validate that we've reached the recipes page; if not, close any ad.
		if (!driver.getCurrentUrl().equals("https://www.tarladalal.com/recipes/")) {
			System.out.println("URL mismatch! Current URL: " + driver.getCurrentUrl());
			ads.closeAdIfPresent(driver);
		}

		// Pagination loop: process up to 5 pages (simplified; no A–Z pagination)
		int currentPage = 1;
		while (true) {
			try {
				if (currentPage == 5)
					break;
				new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions
						.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='overlay-content']//a[@href]")));
				List<WebElement> recipeLinks = driver
						.findElements(By.xpath("//div[@class='overlay-content']//a[@href]"));
				for (WebElement link : recipeLinks) {
					urls.add(link.getAttribute("href"));
				}
				currentPage++;
				System.out.println("Pagination URL: " + driver.getCurrentUrl());
				WebElement nextPageLink = driver.findElement(By.xpath(
						"//ul[@class='pagination justify-content-center align-items-center']//a[contains(text(),'Next')]"));
				js.executeScript("arguments[0].scrollIntoView(true);", nextPageLink);
				Thread.sleep(500);
				js.executeScript("window.scrollBy(0,-150);");
				Thread.sleep(300);
				ads.closeAdIfPresent(driver);
				Thread.sleep(300);
				// Re-find the Next button to avoid stale element exceptions.
				nextPageLink = driver.findElement(By.xpath("//a[contains(text(),'Next')]"));
				js.executeScript("arguments[0].click();", nextPageLink);
			} catch (Exception e) {
				System.out.println("No further page found or error: " + e.getMessage());
				break;
			}
		}

		System.out.println("Total recipe URLs collected: " + urls.size());
		for (String url : urls) {
			System.out.println("Processing recipe URL: " + url);
			recipeDetails(driver, url);
		}

		driver.quit();
	}

	/**
	 * Extracts details for a given recipe URL. Applies LCHF elimination: if any
	 * ingredient contains an exclusion term, the recipe is skipped. Otherwise, it
	 * builds a ReceipePojo and inserts the recipe in the DB.
	 */
	private static void recipeDetails(WebDriver driver, String recipeURL) throws Exception {
		if (recipeURL == null || !recipeURL.contains("recipe"))
			return;
		driver.navigate().to(recipeURL);

		ReceipePojo recipe = new ReceipePojo();
		recipe.recipe_URL = recipeURL;
		String recipeId = recipeURL.replaceAll(".*-(\\d+)r$", "$1");
		recipe.recipe_id = recipeId;
		recipe.recipe_name = driver.getTitle();
		Thread.sleep(1000);

		// Preparation Time
		try {
			WebElement prepTime = driver
					.findElement(By.xpath("//div[@class='content']//h6[text()='Preparation Time']/..//strong"));
			System.out.println("Preparation Time: " + prepTime.getText());
			recipe.preparation_time = prepTime.getText();
		} catch (Exception e) {
			recipe.preparation_time = " ";
		}

		// Cooking Time
		try {
			WebElement cookTime = driver
					.findElement(By.xpath("//div[@class='content']//h6[text()='Cooking Time']/..//strong"));
			System.out.println("Cooking Time: " + cookTime.getText());
			recipe.cooking_time = cookTime.getText();
		} catch (Exception e) {
			recipe.cooking_time = " ";
		}

		// Servings extraction
		try {
			WebElement servings = driver
					.findElement(By.xpath("//div[@class='content']//h6[text()='Makes ']/..//strong"));
			System.out.println("Makes: " + servings.getText());
			recipe.no_of_servings = servings.getText();
		} catch (Exception e) {
			recipe.no_of_servings = " ";
		}

		// Ingredients extraction with LCHF filter check.
		List<WebElement> ingredientElements = driver.findElements(By.xpath("//div[@class='ingredients']//p"));
		System.out.println("Ingredients:");
		List<String> ingredients = new ArrayList<>();
		for (WebElement ingredient : ingredientElements) {
			String text = ingredient.getText().toLowerCase();
			System.out.println("- " + text);
			ingredients.add(text);
			for (String exclude : excludeIngredients) {
				if (text.contains(exclude.toLowerCase())) {
					System.out.println("Skipping recipe (contains excluded ingredient: " + exclude + ")");
					return;
				}
			}
		}
		recipe.ingredients = String.join(", ", ingredients);

		// Preparation Method extraction.
		try {
			WebElement method = driver.findElement(By.xpath("//div[@id='methods']"));
			System.out.println("Preparation Method: " + method.getText());
			recipe.preparation_method = method.getText();
		} catch (Exception e) {
			recipe.preparation_method = " ";
		}

		// Recipe Tags extraction.
		try {
			WebElement tagsElement = driver.findElement(By.xpath("//ul[contains(@class,'tags-list')]"));
			System.out.println("Tags: " + tagsElement.getText());
			recipe.tag = tagsElement.getText();
		} catch (Exception e) {
			recipe.tag = " ";
		}

		// Nutrient Values extraction.
		try {
			WebElement nutrients = driver.findElement(By.id("nutrients"));
			System.out.println("Nutrients: " + nutrients.getText());
			recipe.nutrient_values = nutrients.getText();
		} catch (Exception e) {
			recipe.nutrient_values = " ";
		}

		// Determine Recipe Category from tags.
		final String[] RECIPE_CATEGORY_OPTIONS = { "breakfast", "lunch", "snack", "dinner" };
		List<WebElement> tagElements = driver.findElements(By.xpath("//ul[contains(@class,'tags-list')]/li"));
		String combinedTags = "";
		for (WebElement tag : tagElements) {
			combinedTags += " " + tag.getText();
		}
		String recipeCategory = "";
		for (String option : RECIPE_CATEGORY_OPTIONS) {
			if (combinedTags.toLowerCase().contains(option.toLowerCase())) {
				recipeCategory = option;
				break;
			}
		}
		recipe.recipe_category = recipeCategory;
		System.out.println("Recipe Category: " + recipeCategory);

		// Determine Food Category based on ingredients.
		String ingText = recipe.ingredients.toLowerCase();
		if (ingText.contains("meat") || ingText.contains("chicken") || ingText.contains("fish"))
			recipe.food_category = "Non-Veg";
		else if (ingText.contains("egg") || ingText.contains("eggs"))
			recipe.food_category = "Eggitarian";
		else if (ingText.contains("butter") || ingText.contains("ghee") || ingText.contains("yogurt")
				|| ingText.contains("curd") || ingText.contains("cream") || ingText.contains("paneer"))
			recipe.food_category = "Vegetarian";
		else
			recipe.food_category = "Vegan";
		System.out.println("Food Category: " + recipe.food_category);

		// Cuisine Category extraction.
		List<WebElement> cuisineTags = driver.findElements(By.xpath("//ul[contains(@class,'tags-list')]//li"));
		String cuisineCategory = "";
		List<String> knownCuisines = Arrays.asList("Indian", "South Indian", "Rajathani", "Punjabi", "Bengali",
				"orissa", "Gujarati", "Maharashtrian", "Andhra", "Kerala", "Goan", "Kashmiri", "Himachali",
				"Tamil nadu", "Karnataka", "Sindhi", "Chhattisgarhi", "Madhya pradesh", "Assamese", "Manipuri",
				"Tripuri", "Sikkimese", "Mizo", "Arunachali", "uttarakhand", "Haryanvi", "Awadhi", "Bihari",
				"Uttar pradesh", "Delhi", "North Indian");
		for (WebElement tag : cuisineTags) {
			String tagText = tag.getText().trim().toLowerCase();
			for (String cuisine : knownCuisines) {
				if (tagText.contains(cuisine.toLowerCase())) {
					cuisineCategory = cuisine;
					break;
				}
			}
			if (!cuisineCategory.isEmpty())
				break;
		}
		recipe.cuisine_category = cuisineCategory;
		System.out.println("Cuisine Category: " + cuisineCategory);
		System.out.println("--------------------");

		// Insert the accepted recipe into the database.
		createEliminateList(recipe);
	}

	/**
	 * Inserts the accepted recipe into the database table "LCHF_Diet_Eliminate".
	 *
	 * @param recipe the recipe object to insert.
	 * @throws Exception if insertion fails.
	 */
	private static void createEliminateList(ReceipePojo recipe) throws Exception {
		System.out.println("Accepted Recipe: " + recipe);
		DBConnection.insertRecipe(recipe, "LCHF_Diet_Eliminate");
	}
}