package net.openrally.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SystemConfiguration {
	
	private static final String PROPERTIES_FILE_NAME = "restaurant-webui.properties";

	public static final String RESTAURANT_CORE_BASE_ADDRESS = "restaurant.core.base.address";
	
	private static Properties properties;
	
	private static synchronized void loadProperties(){
		
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		InputStream stream = classLoader
				.getResourceAsStream(PROPERTIES_FILE_NAME);

		if (stream == null) {
			throw new RuntimeException("Could not find restaurant-webui.properties file");
		} 

		properties = new Properties();
		
		try {
			properties.load(stream);
		} catch (IOException e) {
			throw new RuntimeException("Could not open restaurant-webui.properties file: " + e.getMessage());
		}
	}
	
	public static String getPropertyAsString(String propertyName){
		if(null == properties){
			loadProperties();
		}
		return properties.getProperty(propertyName);		
	}
	
	public static Long getPropertyAsLong(String propertyName){
		return Long.parseLong(getPropertyAsString(propertyName));		
	}
	
	public static Double getPropertyAsDouble(String propertyName){
		return Double.parseDouble(getPropertyAsString(propertyName));		
	}
	
	public static Boolean getPropertyAsBoolean(String propertyName){
		return Boolean.parseBoolean(getPropertyAsString(propertyName));		
	}
}
