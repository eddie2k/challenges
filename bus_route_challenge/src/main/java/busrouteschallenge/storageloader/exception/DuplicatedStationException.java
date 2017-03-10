package busrouteschallenge.storageloader.exception;

/**
 * Thrown when there are two or more stations with the same identifier in a
 * route.
 */
public class DuplicatedStationException extends InvalidRouteException {

    private static final long serialVersionUID = -2075695274274836107L;

    public DuplicatedStationException() {
	super();
    }

}
