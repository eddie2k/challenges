package busrouteschallenge.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import busrouteschallenge.properties.exception.PropertyLoadException;

public final class PropertyFactory {
    private static final Logger LOGGER = Logger.getRootLogger();

    private static final String PROPERTY_FILE = "config.properties";
    private static final Properties PROPERTIES = loadProperties();

    private PropertyFactory() {

    }

    private static Properties loadProperties() {
	LOGGER.info("Loading application properties from " + PROPERTY_FILE);
	Properties properties = new Properties();

	try (InputStream in = PropertyFactory.class.getClassLoader().getResourceAsStream(PROPERTY_FILE)) {
	    properties.load(in);
	    LOGGER.debug("Application properties loaded");
	} catch (IOException e) {
	    LOGGER.debug("Error loading application properties: " + e.getMessage());
	    throw new PropertyLoadException(e);
	}
	return properties;
    }

    public static Properties getProperties() {
	return PROPERTIES;
    }

}
