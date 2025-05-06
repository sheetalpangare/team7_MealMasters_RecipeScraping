package LFV_Diet_Recipes;

//import io.github.bonigarcia.wdm.WebDriverManager;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.sql.Connection;

public class LFV_Add {
	
	public String recipe_name = null,recipe_category = null,food_category = null,ingredients = null,preparation_time = null,cooking_time = null,preparation_method = null,nutrient_values = null;
	public String tag = null, no_of_servings = null, cuisine_category = null, recipe_description = null;

	WebDriver driver;
	
	List<String> includeIngredients = Arrays.asList(
            "lettuce", "kale", "chard", "arugula", "spinach", "cabbage", "pumpkin", "sweet potatoes",
           "purple potatoes", "yams", "turnip", "parsnip", "karela", "bittergourd", "beet", "carrot",
            "cucumber", "red onion", "white onion", "broccoli", "cauliflower", "celery", "artichoke",
            "bell pepper", "mushroom", "tomato", "banana", "mango", "papaya", "plantain", "apple",
            "orange", "pineapple", "pear", "tangerine", "berry", "melon", "peach", "plum", "nectarine",
            "avocado", "amaranth", "rajgira", "ramdana", "barnyard", "sanwa", "samvat ke chawal",
            "buckwheat", "kuttu", "finger millet", "ragi", "nachni", "foxtail millet", "kangni",
            "kakum", "kodu", "kodon", "little millet", "moraiyo", "kutki", "shavan", "sama",
            "pearl millet", "bajra", "broom corn millet", "chena", "sorghum", "jowar", "lentil",
            "pulse", "moong dhal", "masoor dhal", "toor dhal", "urd dhal", "lobia", "rajma",
            "matar", "chana", "almond", "cashew", "pistachio", "brazil nut", "walnut", "pine nut",
            "hazelnut", "macadamia nut", "pecan", "peanut", "hemp seed", "sun flower seed",
            "sesame seed", "chia seed", "flax seed"); 

    List<String> excludeIngredients = Arrays.asList(
            "pork", "meat", "poultry", "fish", "sausage", "ham", "salami", "bacon", "milk", "cheese",
           "yogurt", "butter", "ice cream", "egg", "prawn", "oil", "olive oil", "coconut oil", "soybean oil",
            "corn oil", "safflower oil", "sunflower oil", "rapeseed oil", "peanut oil", "cottonseed oil",
            "canola oil", "mustard oil", "cereals", "tinned vegetable", "bread", "maida", "atta", "sooji", "poha",
            "cornflake", "cornflour", "pasta", "white rice", "pastry", "cakes", "biscuit", "soy", "soy milk",
            "white miso paste", "soy sauce", "soy curls", "edamame", "soy yogurt", "soy nut", "tofu", "pies",
            "chip", "cracker", "potatoe", "sugar", "jaggery", "glucose", "fructose", "corn syrup", "cane sugar",
            "aspartame", "cane solid", "maltose", "dextrose", "sorbitol", "mannitol", "xylitol", "maltodextrin",
            "molasses", "brown rice syrup", "splenda", "nutra sweet", "stevia", "barley malt"); 

	public static void main(String[] args) throws Exception {

		LFV_Add rec=new LFV_Add();
		rec.init();
		rec.categoryVegan();
		rec.getAllRecipeUrls();
		

	}

	private void init() throws Exception {
		WebDriverManager.chromedriver().setup();
		// WebDriver driver = new ChromeDriver();
		ChromeOptions options = new ChromeOptions();
		Map<String, Object> prefs = new HashMap<>();
		prefs.put("profile.managed_default_content_settings.images", 2); // Disable images
		options.setExperimentalOption("prefs", prefs);
		driver = new ChromeDriver(options);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();
		Thread.sleep(2000);
		driver.get("https://www.tarladalal.com/");
		Thread.sleep(2000);
		}
	
	private void categoryVegan() {

		// To get "vegan" category recipes
		WebElement searchBox = driver.findElement(By.xpath("//i[@class='fa fa-search primary search-show']"));
		searchBox.click();

		WebElement searchInputs = driver.findElement(By.xpath("//input[@placeholder='Search Here']"));
		searchInputs.sendKeys("vegan");

		WebElement searchBtn = driver.findElement(By.xpath(
				"//button[@class='input-group-text btn btn-main primary-bg fw-semibold font-size-14 text-white search-btn']"));
		searchBtn.click();

		System.out.println("Food category: Vegan");
	}
		
	private void getAllRecipeUrls() throws Exception{
	  Thread.sleep(5000);

		// all recipe links in list

		List<WebElement> recipeLinks = driver.findElements(By.xpath("//div[@class='overlay-content']//a[@href]"));
		List<String> urls = new ArrayList<String>();
		for (WebElement link : recipeLinks) {
			urls.add(link.getAttribute("href"));			
		}
		
		for (String url : urls) {
			recipeDetails(url);	
		}

	}

	private void recipeDetails(String recipe_URL) throws Exception {
		// To get the RecipeURL
		
		String jdbcUrlPath = "jdbc:postgresql://localhost:5432/RecipeScraping";
		String username = "postgres";
		String password = "K@ntap96";
		Class.forName("org.postgresql.Driver");	

		Connection conn = DriverManager.getConnection(jdbcUrlPath, username, password);	

	//	recipeId, recipeURL , 

		//System.out.println("RecipeUrl: " + recipeURL);
		if (recipe_URL != null && recipe_URL.contains("recipe")) {

			System.out.println("Recipe URL: " + recipe_URL);
		}
		driver.navigate().to(recipe_URL); // Navigate to the recipe page

		// To get the RecipeName
		
		recipe_name = driver.getTitle();
		System.out.println("RecipeName: " + recipe_name);
		Thread.sleep(1000);

		// To get the RecipeID
		
		String recipe_id = recipe_URL.replaceAll(".*-(\\d+)r$", "$1");
		System.out.println("RecipeID: " + recipe_id);

		// To get the Preparation time
		
		WebElement prepTime = driver.findElement(By.xpath("//div[@class='content']//h6[text()='Preparation Time']/..//strong"));
		preparation_time = prepTime.getText();
		System.out.println("Preparation Time: " + preparation_time);

		// To get the Cooking time
		
		WebElement cookTime = driver.findElement(By.xpath("//div[@class='content']//h6[text()='Cooking Time']/..//strong"));
		cooking_time = cookTime.getText();
		System.out.println("Cooking Time: " + cooking_time);

		// To get the Makes
		WebElement servings = driver.findElement(By.xpath("//div[@class='content']//h6[text()='Makes ']/..//strong"));
		no_of_servings = servings.getText();
		System.out.println("Makes: " + no_of_servings);

		// To Extract ingredients

		List<WebElement> ingredientElements = driver.findElements(By.xpath("//div[@id='ingredients']"));
		System.out.println("Ingredients:");
		for (WebElement ingredient : ingredientElements) {
			System.out.println("- " + ingredient.getText());
		}

		// To get the Preparation Method
		WebElement method = driver.findElement(By.xpath("//div[@id='methods']"));
		preparation_method = method.getText();
		System.out.println("Preparation_method: " + preparation_method);

		// To get the Recipe Tags
		WebElement Tags = driver.findElement(By.xpath("//ul[@class='tags-list']"));
		tag = Tags.getText();
		System.out.println("Tags: " + Tags.getText());

		// To get the Nutrient values
		WebElement Nutrients = driver.findElement(By.id("nutrients"));
		nutrient_values = Nutrients.getText();
		System.out.println("Nutrients Values: " + nutrient_values);

		// To get the Cuisine Category
//		WebElement Cuisine = driver.findElement(By.xpath("//p/span[3]/a"));
//		System.out.println("Cuisine Category: " + Cuisine.getText());

		System.out.println("--------------------");
		
		try {
			
			String sql = "INSERT INTO lfv_add (recipe_id,recipe_name,recipe_category,food_category,ingredients,preparation_time,cooking_time,tag,no_of_servings,cuisine_category,recipe_description,preparation_method,nutrient_values,recipe_URL) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedstatement = conn.prepareStatement(sql);
			preparedstatement.setString(1, recipe_id);          // Found 
			preparedstatement.setString(2, recipe_name);        // Found
			preparedstatement.setString(3, recipe_category);   
			preparedstatement.setString(4, food_category);
			preparedstatement.setString(5, ingredients);        // Found  - how to store array in non vary variable
			preparedstatement.setString(6, preparation_time);   // Found
			preparedstatement.setString(7, cooking_time);       // Found
			preparedstatement.setString(8, tag);                // Found
			preparedstatement.setString(9, no_of_servings);     // Found
			preparedstatement.setString(10, cuisine_category);
			preparedstatement.setString(11, recipe_description);
			preparedstatement.setString(12, preparation_method); // Found
			preparedstatement.setString(13, nutrient_values);    // Found
			preparedstatement.setString(14, recipe_URL);         // Found
			preparedstatement.executeUpdate();
			
		}
		catch(SQLException e) {
			System.out.println(e);
		}
	}
	private void createEliminateAndAddList() {
		
	}
}

