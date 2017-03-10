package busrouteschallenge.storageloader.exception;

/**
 * Generic unchecked exception to cover validation/parser exceptions,
 */
public class InvalidRouteException extends RuntimeException {

    private static final long serialVersionUID = 4801929983766130067L;

    public InvalidRouteException() {
	super();
    }

    public InvalidRouteException(String msg) {
	super(msg);
    }
}
