package busrouteschallenge.storageloader.exception;

/**
 * Thrown when the number of routes exceed the limit.
 */
public class TooManyRoutesException extends InvalidRouteException {

    private static final long serialVersionUID = 5166685562357509735L;

    public TooManyRoutesException(String msg) {
	super(msg);
    }

}
