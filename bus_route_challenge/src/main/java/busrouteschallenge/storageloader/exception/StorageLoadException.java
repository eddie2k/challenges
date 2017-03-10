package busrouteschallenge.storageloader.exception;

/**
 * Thrown when an error occurs loading the storage with data.
 */
public class StorageLoadException extends RuntimeException {

    private static final long serialVersionUID = 247987994139710443L;

    public StorageLoadException(Throwable e) {
	super(e);
    }

    public StorageLoadException(String msg) {
	super(msg);
    }

}
