package busrouteschallenge.localservice.exception;

public class LocalServiceNotReadyException extends RuntimeException {

    private static final long serialVersionUID = 8524218567817076765L;

    public LocalServiceNotReadyException(String msg) {
	super(msg);
    }

}
