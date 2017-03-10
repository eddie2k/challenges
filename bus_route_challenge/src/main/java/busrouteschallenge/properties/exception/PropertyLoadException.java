package busrouteschallenge.properties.exception;

import java.io.IOException;

/**
 * Thrown when there are errors loading the system properties.
 */
public class PropertyLoadException extends RuntimeException {

    private static final long serialVersionUID = 4026293975942498262L;

    public PropertyLoadException(IOException e) {
	super(e);
    }
}
