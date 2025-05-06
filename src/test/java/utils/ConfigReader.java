package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

	static ConfigReader configFileReader = new ConfigReader();
	private static Properties prop = new Properties();
	private final static String propertyFilePath = "src/test/resources/Config/config.properties";

	public ConfigReader() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(propertyFilePath));
			prop = new Properties();
			try {
				prop.load(reader);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Config.properties not found at " + propertyFilePath);
		}
	}

	public static String getApplicationUrl() {
		String url = prop.getProperty("url");
		if (url != null)
			return url;
		else
			throw new RuntimeException("url not specified in the Config.properties file.");
	}

	public static String getBrowser() {
		String browser = prop.getProperty("browser");
		if (browser != null) {
			return browser;
		} else {
			throw new RuntimeException("Browser not specified in the Config.properties file.");
		}
	}

	public String getExcelPath() {
		String testDataPath = prop.getProperty("excelPath");
		if (testDataPath != null) {
			return testDataPath;
		} else {
			throw new RuntimeException("Test Data not specified in the Config.properties file.");
		}
	}

}