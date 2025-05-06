package Base_Class;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import Utilities.ConfigReader;

public class BaseClass {
	
	static ConfigReader configReader;
	
	public WebDriver driver=null;

	@BeforeTest
	public void setUpDriver() {

		configReader = new ConfigReader();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		options.addArguments("--headless");
		driver = new ChromeDriver(options);
		configReader.getUrl();
		//driver.get("https://www.tarladalal.com/");
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();

	}

	@AfterClass
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}