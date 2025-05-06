package base;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import utils.ConfigReader;
import utils.ListenersReporter;

import utils.LoggerReader;
import com.aventstack.chaintest.plugins.ChainTestListener;

public class BaseTest {

	public WebDriver driver;
	ConfigReader config = new ConfigReader();

	@BeforeMethod
	@Parameters("browser")
	public void setup(@Optional("chrome") String browser) {
		DriverFactory.setUp(browser);
		driver = DriverFactory.getDriver();
		ChainTestListener.log("Browser factory setup");
	}

	@AfterMethod
	public void tearDown() {
		DriverFactory driverFactory = new DriverFactory();
		driverFactory.tearDown();
	}

	@AfterMethod
	public void takeScreenshotOnFailure(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE && driver != null) {
			LoggerReader.info("Test failed: " + result.getName() + ". Attaching screenshot to Allure.");
			ListenersReporter.attachScreenshotToAllure(driver);
		}
	}

}
