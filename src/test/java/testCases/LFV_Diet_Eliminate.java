package testCases;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import io.github.bonigarcia.wdm.WebDriverManager;
import utilities.AddHandler;

public class LFV_Diet_Eliminate {
	
	public static List<String> excludeIngredients = Arrays.asList(

            "pork", "meat", "poultry", "fish", "sausage", "ham", "salami", "bacon", "milk", "cheese",

           "yogurt", "butter", "ice cream", "egg", "prawn", "oil", "olive oil", "coconut oil", "soybean oil",

            "corn oil", "safflower oil", "sunflower oil", "rapeseed oil", "peanut oil", "cottonseed oil",

            "canola oil", "mustard oil", "cereals", "tinned vegetable", "bread", "maida", "atta", "sooji", "poha",

            "cornflake", "cornflour", "pasta", "white rice", "pastry", "cakes", "biscuit", "soy", "soy milk",

            "white miso paste", "soy sauce", "soy curls", "edamame", "soy yogurt", "soy nut", "tofu", "pies",

            "chip", "cracker", "potatoe", "sugar", "jaggery", "glucose", "fructose", "corn syrup", "cane sugar",

            "aspartame", "cane solid", "maltose", "dextrose", "sorbitol", "mannitol", "xylitol", "maltodextrin",

            "molasses", "brown rice syrup", "splenda", "nutra sweet", "stevia", "barley malt"); 
	
	static WebDriver driver;
	
	public static void main(String[] args) throws InterruptedException {
	

	     		
		AddHandler ads = new AddHandler();
		WebDriverManager.chromedriver().setup();

//	    WebDriver driver = new ChromeDriver();

	        ChromeOptions options = new ChromeOptions();

//		options.addArguments("--blink-settings=imagesEnabled=false");
////		options.addArguments("--disable-images");
//		// options.addArguments("--disable-javascript");
//		options.addArguments("--remote-allow-origins=*");
//		options.addArguments("--headless");
//		options.addArguments("--disable-popup-blocking");
//		options.addArguments("--disable-notifications");
//		options.addArguments("--disable-extensions");
//		options.addArguments("enable-automation");
//		options.addArguments("--no-sandbox");
//		options.addArguments("--dns-prefetch-disable");
//		options.addArguments("--disable-gpu");
//		options.addArguments("--disable-dev-shm-usage");
//		options.addArguments("--disable-software-rasterizer");
//		options.addArguments("--disable-features=SharedStorageAPI");
		
		WebDriver driver = new ChromeDriver(options); 
//		WebDriver driver = new ChromeDriver(options); 

	     Thread.sleep(2000);

	       driver.get("https://m.tarladalal.com/");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		long initialHeight = ((Number) js.executeScript("return document.body.scrollHeight")).longValue();
		while (true) {
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000); // adjust sleep based on load time
			long newHeight = ((Number) js.executeScript("return document.body.scrollHeight")).longValue();
			if (newHeight == initialHeight) {
				break; // stop if height hasn't changed
			}
			initialHeight = newHeight;
		}
		WebElement recipeslink = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Recipes List')]")));
		Assert.assertTrue(recipeslink.isDisplayed(), "Element is not visible");
		if (driver.getCurrentUrl().equals("https://www.tarladalal.com/#google_vignette")) {
			WebElement adx = driver.findElement(By.xpath("//*[contains(text(),'Close')]"));
			adx.click();
			System.out.println("closed the ad before clicking the Recipes link");
		}
		recipeslink.click();

		if (!driver.getCurrentUrl().equals("https://www.tarladalal.com/recipes/")) {
			System.out.println("URL mismatch! Current URL is: " + driver.getCurrentUrl());
			ads.closeAdIfPresent(driver);
			System.out.println("current page is: " + driver.getCurrentUrl());
		}
		
		int currentPage = 1;
		while (true) {
		    try {
		        // Wait for the recipes to load
		        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
		            ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='overlay-content']//a[@href]"))
		        );

		        // Extract and print all recipe URLs from current page
		        List<WebElement> recipeLinks = driver.findElements(By.xpath("//div[@class='overlay-content']//a[@href]"));
		        for (WebElement recipeLink : recipeLinks) {
		            String recipeUrl = recipeLink.getAttribute("href");
		            if (recipeUrl != null && recipeUrl.contains("recipe")) {
		                System.out.println("Recipe URL: " + recipeUrl);
		            }
		        }

		        // Try to locate the next page button
		     
		        WebElement nextPageLink = driver.findElement(By.xpath(
		                "//ul[@class='pagination justify-content-center align-items-center']//a[contains(text(), 'Next')]"
		            ));
		        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextPageLink);
		        Thread.sleep(500);
		        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -150);");
		        Thread.sleep(300);

		        ads.closeAdIfPresent(driver);

		        nextPageLink = driver.findElement(By.xpath("//a[contains(text(),'Next')]")); // re-find to avoid stale
                js.executeScript("arguments[0].click();", nextPageLink);

		    } catch (Exception e) {
		        System.out.println("No further page found or error: " + e.getMessage());
		        break; // break the loop when next page is not found
		    }
		}
	}
		
		private void recipeDetails(WebDriver driver,String recipeURL) throws Exception {
			// To get the RecipeURL

			//System.out.println("RecipeUrl: " + recipeURL);
			if (recipeURL != null && recipeURL.contains("recipe")) return ;
			driver.navigate().to(recipeURL); // Navigate to the recipe page

			// To get the RecipeName
			System.out.println("RecipeName: " + driver.getTitle());
			Thread.sleep(1000);

			// To get the RecipeID
			String recipeId = recipeURL.replaceAll(".*-(\\d+)r$", "$1");
			System.out.println("RecipeID: " + recipeId);

			// To get the Preparation time
			WebElement prepTime = driver
					.findElement(By.xpath("//div[@class='content']//h6[text()='Preparation Time']/..//strong"));
			System.out.println("Preparation Time: " + prepTime.getText());

			// To get the Cooking time
			WebElement cookTime = driver
					.findElement(By.xpath("//div[@class='content']//h6[text()='Cooking Time']/..//strong"));
			System.out.println("Cooking Time: " + cookTime.getText());

			// To get the Makes
			WebElement servings = driver.findElement(By.xpath("//div[@class='content']//h6[text()='Makes ']/..//strong"));
			System.out.println("Makes: " + servings.getText());

			// To Extract ingredients

			List<WebElement> ingredientElements = driver.findElements(By.xpath("//div[@id='ingredients']"));
			System.out.println("Ingredients:");
			for (WebElement ingredient : ingredientElements) {
				System.out.println("- " + ingredient.getText());
			}

			// To get the Preparation Method
			WebElement method = driver.findElement(By.xpath("//div[@id='methods']"));
			System.out.println("Preparation_method: " + method.getText());

			// To get the Recipe Tags
			WebElement Tags = driver.findElement(By.xpath("//ul[@class='tags-list']"));
			System.out.println("Tags: " + Tags.getText());

			// To get the Nutrient values
			WebElement Nutrients = driver.findElement(By.id("nutrients"));
			System.out.println("Nutrients Values: " + Nutrients.getText());

			// To get the Cuisine Category
//			WebElement Cuisine = driver.findElement(By.xpath("//p/span[3]/a"));
//			System.out.println("Cuisine Category: " + Cuisine.getText());

			System.out.println("--------------------");
		}
		
		private void createEliminateList() {
			
		}
		
	
	

}
