package busrouteschallenge.storageloader.exception;

/**
 * Thrown when there are two or more routes with the same identifier.
 */
public class DuplicatedRouteException extends InvalidRouteException {

    private static final long serialVersionUID = 3037062648368623609L;

    public DuplicatedRouteException() {
	super();
    }
}
