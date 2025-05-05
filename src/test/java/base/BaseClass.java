package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;

public class BaseClass {
	
	public WebDriver driver=null;

	@BeforeTest
	public void setUpDriver() {

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		options.addArguments("--headless");
		driver = new ChromeDriver(options);
		driver.get("https://www.tarladalal.com/");
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();

	}

	@AfterClass
	public void teardown() throws InterruptedException {
		Thread.sleep(3000);
		driver.quit();

	}
}
