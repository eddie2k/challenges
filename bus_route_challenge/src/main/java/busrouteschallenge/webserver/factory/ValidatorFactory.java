package busrouteschallenge.webserver.factory;

import static busrouteschallenge.properties.PropertyFactory.getProperties;
import static java.lang.Integer.parseInt;

import java.security.InvalidParameterException;

import org.apache.log4j.Logger;
import org.glassfish.hk2.api.Factory;

import busrouteschallenge.properties.ApplicationConstants;
import busrouteschallenge.storageloader.validator.RoutesAndStationValidator;
import busrouteschallenge.storageloader.validator.Validator;

public class ValidatorFactory implements Factory<Validator> {
    private static final Logger LOGGER = Logger.getRootLogger();

    private static final int MAX_ROUTES = loadIntFromProperties(ApplicationConstants.MAX_ROUTES);
    private static final int MIN_STATIONS_PER_ROUTE = loadIntFromProperties(
	    ApplicationConstants.MIN_STATIONS_PER_ROUTE);
    private static final int MAX_STATIONS_PER_ROUTE = loadIntFromProperties(
	    ApplicationConstants.MAX_STATIONS_PER_ROUTE);
    private static final int MAX_ABSOLUTE_STATIONS = loadIntFromProperties(ApplicationConstants.MAX_ABSOLUTE_STATIONS);

    @Override
    public Validator provide() {
	return new RoutesAndStationValidator(MAX_ROUTES, MIN_STATIONS_PER_ROUTE, MAX_STATIONS_PER_ROUTE,
		MAX_ABSOLUTE_STATIONS);
    }

    private static int loadIntFromProperties(String propertyKey) {
	try {
	    return parseInt(getProperties().getProperty(propertyKey));
	} catch (NumberFormatException e) {
	    LOGGER.fatal("Error loading property \"" + propertyKey + "\": " + e.getMessage());
	    throw new InvalidParameterException(e.getMessage());
	}
    }

    @Override
    public void dispose(Validator instance) {

    }

}
