package busrouteschallenge.storageloader.exception;

/**
 * Thrown when the number of stations in a route exceed the limit.
 */
public class TooManyStationsInRouteException extends InvalidRouteException {

    private static final long serialVersionUID = -8164120954256796091L;

    public TooManyStationsInRouteException(String msg) {
	super(msg);
    }

}
