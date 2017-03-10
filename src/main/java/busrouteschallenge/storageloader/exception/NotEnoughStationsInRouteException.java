package busrouteschallenge.storageloader.exception;

/**
 * Thrown when there are lesser stations in a route than the required minimum.
 */
public class NotEnoughStationsInRouteException extends InvalidRouteException {

    private static final long serialVersionUID = -2358089353050996544L;

    public NotEnoughStationsInRouteException(String msg) {
	super(msg);
    }

}
