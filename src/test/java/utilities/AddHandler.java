package utilities;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class AddHandler {

//	public void closeAdIfPresent(WebDriver driver) {
//	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
//	    
//	    try {
//	        // Switch to the known iframe by ID
//	        driver.switchTo().frame("aswift_8");
//	        System.out.println("Switched to iframe: aswift_8");
//
//	        // Optionally switch to inner iframe if the dismiss button is inside another frame
//	        List<WebElement> nestedFrames = driver.findElements(By.tagName("iframe"));
//	        if (!nestedFrames.isEmpty()) {
//	            driver.switchTo().frame(nestedFrames.get(0));
//	            System.out.println("Switched to nested iframe inside aswift_8");
//	        }
//
//	        // Now try to locate and click the dismiss button
//	        WebElement dismissBtn = wait.until(ExpectedConditions.elementToBeClickable(
//	            By.xpath("//div[@id='dismiss-button' and @role='button']")));
//	        dismissBtn.click();
//	        System.out.println("Dismiss button clicked.");
//	    } catch (Exception e) {
//	        System.out.println("Dismiss button not found or not clickable: " + e.getMessage());
//	    } finally {
//	        // Always return to the main page
//	        driver.switchTo().defaultContent();
//	    }
//	    
//	    
//	}
	

    public void closeAdIfPresent(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Step 1: Try closing from all visible iframes
            List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
            for (WebElement frame : iframes) {
                try {
                    driver.switchTo().frame(frame);
                    List<WebElement> closeBtns = driver.findElements(By.xpath(
                        "//div[@aria-label='Close' or @role='button' or @title='Close' or contains(@class, 'close')]"
                    ));

                    for (WebElement btn : closeBtns) {
                        if (btn.isDisplayed() && btn.isEnabled()) {
                            btn.click();
                            System.out.println("Closed ad inside iframe.");
                            driver.switchTo().defaultContent();
                            return;
                        }
                    }

                    driver.switchTo().defaultContent(); // Important: switch back to main DOM
                } catch (Exception e) {
                    driver.switchTo().defaultContent(); // Fail-safe
                }
            }

            // Step 2: Try closing ad in main DOM
            List<WebElement> mainCloseBtns = driver.findElements(By.xpath(
                "//div[@aria-label='Close' or @role='button' or @title='Close' or contains(@class, 'close')]"
            ));

            for (WebElement btn : mainCloseBtns) {
                if (btn.isDisplayed() && btn.isEnabled()) {
                    btn.click();
                    System.out.println("Closed ad in main DOM.");
                    return;
                }
            }

        } catch (Exception e) {
            System.out.println("No ad or error while closing: " + e.getMessage());
        } finally {
            try {
                driver.switchTo().defaultContent();
            } catch (Exception ignore) {}
        }
    }

}
