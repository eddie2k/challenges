package busrouteschallenge.storageloader.exception;

/**
 * Thrown when the number of expected routes is lesser than 0 or greater than
 * the maximum.
 */
public class WrongNumberOfExpectedRoutesException extends InvalidRouteException {

    private static final long serialVersionUID = 3157709617454567975L;

    public WrongNumberOfExpectedRoutesException(String msg) {
	super(msg);
    }

}
