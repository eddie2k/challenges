package busrouteschallenge.storage.exception;

/**
 * Thrown when, trying to store a route, this route is already stored.
 */
public class AlreadyExistingException extends RuntimeException {

    private static final long serialVersionUID = 5170516024226332840L;

    public AlreadyExistingException(String msg) {
	super(msg);
    }

}
