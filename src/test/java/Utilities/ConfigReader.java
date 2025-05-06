package Utilities;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
	
	private static final Properties properties = new Properties();
	
	public ConfigReader() {
		
		try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
			
			if (input == null) {
				
				throw new FileNotFoundException("config.properties file not found");
			}
			properties.load(input);
		} 
		catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	public String getUrl() {
		
		return properties.getProperty("url");
	}
	public String getUsername() {
		
		return properties.getProperty("username");
	}
	public String getPassword() {
		
		return properties.getProperty("password");
	}
	public String getBrowser() {
		
		return properties.getProperty("browser");
	}
}
