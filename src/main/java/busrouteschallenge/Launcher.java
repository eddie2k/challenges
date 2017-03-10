package busrouteschallenge;

import static busrouteschallenge.properties.PropertyFactory.getProperties;

import java.net.URI;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.log4j.Logger;

import busrouteschallenge.properties.ApplicationConstants;
import busrouteschallenge.utils.Awaiter;
import busrouteschallenge.webserver.WebServer;

public class Launcher {

    private static final Logger LOGGER = Logger.getRootLogger();

    private static final String BASE_URI = getProperties().getProperty(ApplicationConstants.SERVER_ADDRESS);

    private WebServer server = new WebServer();
    private Awaiter awaiter = new Awaiter();

    public void run(String[] args) {
	LOGGER.debug("Application started. Agrs=" + Arrays.asList(args));
	if (args.length != 1) {
	    throw new IllegalArgumentException(
		    "Arguments must be exactly one string (currently=" + Arrays.asList(args) + ")");
	}

	String filename = args[0];
	LOGGER.debug("Using file with filename=" + filename);
	server.start(Paths.get(filename), URI.create(BASE_URI));
	awaiter.waitForSigTerm();
    }
}