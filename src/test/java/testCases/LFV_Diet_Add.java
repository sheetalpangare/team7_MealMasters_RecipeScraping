package testCases;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;
import utilities.AddHandler;

public class LFV_Diet_Add {
	
	public static void main(String[] args) throws InterruptedException {

        WebDriverManager.chromedriver().setup();

    //    WebDriver driver = new ChromeDriver();

        ChromeOptions options = new ChromeOptions();
//        options.addExtensions(Paths.get("./src/test/resources/adblock.crx").toFile());

        Map<String, Object> prefs = new HashMap<>();

//        prefs.put("profile.managed_default_content_settings.images", 2); // Disable images

//        options.setExperimentalOption("prefs", prefs);

        WebDriver driver = new ChromeDriver(options); 

     Thread.sleep(2000);
     driver.manage().window().maximize();

     driver.get("https://www.tarladalal.com/");

    


    // Step 2: select receipe list from home page

//        Thread.sleep(2000);
       WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    
//	    
//	     AddHandler adHandler = new AddHandler(driver);
//	     adHandler.handleVignetteAdIfPresent();  // Call before interacting with the page
	     
        WebElement receipesList = driver.findElement(By.xpath("//a[normalize-space()='Recipes List']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", receipesList);
        Thread.sleep(5000);
        receipesList.click();
       
//        closeGoogleVignetteIfPresent(driver);

//        WebElement searchBox = driver.findElement(By.xpath("//i[@class='fa fa-search primary search-show']"));
//
//        searchBox.click();
//
//        WebElement searchInputs = driver.findElement(By.xpath("//input[@placeholder='Search Here']"));
//
//        searchInputs.sendKeys("vegan");    
//
//        WebElement searchBtn = driver.findElement(By.xpath("//button[@class='input-group-text btn btn-main primary-bg fw-semibold font-size-14 text-white search-btn']"));
//
//        searchBtn.click();

        Thread.sleep(5000); 

     // Step 3: Define ingredient filters

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

     		

        Thread.sleep(2000);
        

        	        

        	     // Step 4: Extract all recipe URLs on current search page
        

//        	 List<WebElement> recipePages = driver.findElements(By.xpath("//nav[@aria-label='Page navigation example']"));
//        	 int pagecount=recipePages.size();
//        	 System.out.println("number of pages: " +pagecount);
//        	 try {
//                 WebElement popup = driver.findElement(By.cssSelector(".popup-close")); // update selector if needed
//                 popup.click();
//             } catch (Exception ignored) {
//             	System.out.println("Popup didnt appear");
//             }
//travesrse through pages
//        	 for(int p=1;p<recipePages.size();p++)
//        	 {
//        	 WebElement active_ReceipePages = driver.findElement(By.xpath("//nav[@aria-label='Page navigation example']//li//a[text()='" +p+ "']"));
//        	 active_ReceipePages.click(); 
//        	 store the receipe card elements
//        closeGoogleVignetteIfPresent(driver);
        List<WebElement> pageLinks = driver.findElements(By.xpath("//ul[@class='pagination justify-content-center align-items-center']//*[@class='page-item']"));
        for (int i = 1; i < pageLinks.size(); i++) {
        	System.out.println("pages " +pageLinks);
        	if (i>pageLinks.size()){
            WebElement link = driver.findElement(By.xpath("//ul[@class='pagination justify-content-center align-items-center']//a[normalize-space(text())="+i+"]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", link);
            link.click();
            System.out.println("Visiting page: " + i);
            Thread.sleep(2000);
        }
      }
        	 
        	 List<WebElement> recipeLinkElements = driver.findElements(By.xpath("//div[@class='overlay-content']//a[@href]"));
//        	 


       
        	        for (WebElement linkElement : recipeLinkElements) {

        	            String recipeUrl = linkElement.getAttribute("href");

        	            if (recipeUrl != null && recipeUrl.contains("recipe")) {

        	                System.out.println("Recipe URL: " + recipeUrl);

        	            }

        	        }

       

            // Step 5: Extract ingredients

        List<WebElement> ingredientElements = driver.findElements(By.xpath("//div[@id='rcpinglist']//li"));

        List<String> ingredientsText = ingredientElements.stream()

                .map(e -> e.getText().toLowerCase())

                .collect(Collectors.toList());



        boolean hasValidIngredient = includeIngredients.stream()

                .anyMatch(inc -> ingredientsText.stream().anyMatch(i -> i.contains(inc)));



        boolean hasInvalidIngredient = excludeIngredients.stream()

                .anyMatch(exc -> ingredientsText.stream().anyMatch(i -> i.contains(exc)));



        if (hasValidIngredient && !hasInvalidIngredient) {

        	 String recipeTitle = driver.findElement(By.tagName("h1")).getText();

            System.out.println("✅ MATCH: " +recipeTitle);

            System.out.println("URL: " + recipeTitle);

            System.out.println("------------------------------------------");

        }

    }
	
	public static void closeGoogleVignetteIfPresent(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            // Wait for iframe with ad
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.cssSelector("iframe[src*='ads']")));

            WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div[role='button'], button[aria-label='Close ad']")));
            closeBtn.click();

            driver.switchTo().defaultContent();
            System.out.println("Google vignette ad closed.");
        } catch (Exception e) {
            // No vignette present
        }
	}

}
