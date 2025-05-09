package baseclass;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import drivers.DriverManager;
import utilities.LoggerReader;

public class BaseClass extends DriverManager {

	@Parameters({ "browser", "headless" })
	@BeforeMethod
	public void setUp(@Optional("chrome") String browser, @Optional("true") String headlessParam) {
		LoggerReader.info("Loading the driver in " + browser + ", Headless mode: " + headlessParam);
		boolean headless = Boolean.parseBoolean(headlessParam);
		// Set the URL for navigation
		String url = configReader.getApplicationUrl();
		LoggerReader.info("inside testbase class");
		DriverManager.createDriver(browser, headless);
		WebDriver driver = DriverManager.getDriver();
		if (driver == null) {
			LoggerReader.info("ERROR: WebDriver is null for \" + browser");
		} else {
			LoggerReader.info("Getting inside driver");
			driver.get(url);
			LoggerReader.info("Inside testBase url::" + url);
		}
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(configReader.getImplicitlyWait()));
		// driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

	}

	@AfterMethod
	public void tearDown() {

		LoggerReader.info("-------------------------------------------------------");
		DriverManager.quitDriver();

	}

}