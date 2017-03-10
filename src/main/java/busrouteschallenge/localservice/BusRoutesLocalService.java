package busrouteschallenge.localservice;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import busrouteschallenge.localservice.exception.LocalServiceNotReadyException;
import busrouteschallenge.storage.Storage;
import busrouteschallenge.storageloader.AsyncStorageLoader;

public class BusRoutesLocalService {

    private static final Logger LOGGER = Logger.getRootLogger();

    private final AtomicBoolean storageLoaded = new AtomicBoolean(false);

    private final Storage storage;

    @Inject
    public BusRoutesLocalService(Storage storage, AsyncStorageLoader loader, Path path) {
	if (storage == null) {
	    throw new NullPointerException("Storage must not be null");
	}
	if (loader == null) {
	    throw new NullPointerException("Loader must not be null");
	}
	if (path == null) {
	    throw new NullPointerException("Path must not be null");
	}

	this.storage = storage;

	Function<Void, Void> onSuccess = v -> {
	    LOGGER.info("Asynchronous load ended");
	    storageLoaded.set(true);
	    return null;
	};
	Function<Throwable, Void> onFailure = t -> {
	    LOGGER.info("Error loading data");
	    LOGGER.info(t);
	    return null;
	};
	loader.load(path, onSuccess, onFailure);
    }

    public boolean isDirectConnection(int depStationId, int arrStationId) throws LocalServiceNotReadyException {
	LOGGER.debug("Checking direct connection between " + depStationId + " and " + arrStationId);
	if (storageLoaded.get()) {
	    boolean result = storage.findConnections(depStationId, arrStationId).findAny().isPresent();
	    LOGGER.debug("Direct connection between " + depStationId + " and " + arrStationId + " is " + result);
	    return result;
	} else {
	    LOGGER.debug("Cannot check direct connection between " + depStationId + " and " + arrStationId
		    + "; Data not ready");
	    throw new LocalServiceNotReadyException("Data not ready");
	}
    }
}