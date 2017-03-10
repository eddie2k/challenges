package busrouteschallenge.storageloader.exception;

/**
 * Thrown when the global number of stations in all the routes exceed the limit.
 */
public class TooManyDifferentStationsException extends InvalidRouteException {

    private static final long serialVersionUID = 1832082829597684561L;

    public TooManyDifferentStationsException(String msg) {
	super(msg);
    }

}
