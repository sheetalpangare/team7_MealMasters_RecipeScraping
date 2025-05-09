package baseclass;

import java.time.Duration;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import drivers.DriverFactory;
import utilities.ConfigReader;

public class BaseClass {
	
	public WebDriver driver;

	
	
	@BeforeClass
//	@Parameters({"browser"})
	public void setUp() throws InterruptedException {

		DriverFactory.initializeBrowser(ConfigReader.getProperty("browser"));
		
		//String browser = ConfigReader.getBrowserType(); //for crossbrowser testing
//		DriverFactory.initializeBrowser(browser);
		
		driver = DriverFactory.getDriver();

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
		driver.manage().window().maximize();

	}

	@AfterClass
	public void tearDown() {
		//driver.manage().deleteAllCookies();
		driver.quit();
	}


}
