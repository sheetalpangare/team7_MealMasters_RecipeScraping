package testCases;

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
import org.testng.Assert;

import io.github.bonigarcia.wdm.WebDriverManager;
import utilities.AddHandler;

public class LFV_Diet {
	

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
			WebElement adx = driver.findElement(By.xpath("contains(text(),'Close')]"));
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

}
