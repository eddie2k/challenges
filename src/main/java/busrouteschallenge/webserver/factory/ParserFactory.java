package busrouteschallenge.webserver.factory;

import static busrouteschallenge.properties.PropertyFactory.getProperties;

import org.glassfish.hk2.api.Factory;

import busrouteschallenge.properties.ApplicationConstants;
import busrouteschallenge.storageloader.parser.Parser;
import busrouteschallenge.storageloader.parser.PlainTextFileParser;

public class ParserFactory implements Factory<Parser> {

    private static final String DELIMITER = getProperties().getProperty(ApplicationConstants.DELIMITER);

    @Override
    public Parser provide() {
	return new PlainTextFileParser(DELIMITER);
    }

    @Override
    public void dispose(Parser instance) {

    }

}
