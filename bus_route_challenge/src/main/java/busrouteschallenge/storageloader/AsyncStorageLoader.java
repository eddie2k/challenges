package busrouteschallenge.storageloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import busrouteschallenge.storageloader.exception.StorageLoadException;

/**
 * Asynchronous storage loader. It relies in a synchronous {@link StorageLoader}
 * for the loading itself, but adds background capabilities to it.
 */
public class AsyncStorageLoader {

    private static final Logger LOGGER = Logger.getRootLogger();

    private StorageLoader syncLoader;

    @Inject
    public AsyncStorageLoader(StorageLoader syncLoader) {
	Objects.requireNonNull(syncLoader, "loader must not be null");
	this.syncLoader = syncLoader;
    }

    public void load(Path path, Function<Void, Void> onSuccess, Function<Throwable, Void> onFailure) {
	Objects.requireNonNull(path, "Path must not be null");
	Objects.requireNonNull(onSuccess, "OnSuccess must not be null");
	Objects.requireNonNull(onFailure, "OnFailure must not be null");

	CompletableFuture.runAsync(() -> {
	    LOGGER.info("Loading storage asynchronously...");
	    try (BufferedReader br = Files.newBufferedReader(path)) {
		syncLoader.load(br);
	    } catch (IOException e) {
		throw new StorageLoadException(e);
	    }
	}).thenAccept(v -> {
	    LOGGER.info("Asynchronous data load ended");
	    onSuccess.apply(null);
	}).exceptionally(t -> {
	    LOGGER.info("Asynchronous data load failed");
	    onFailure.apply(t);
	    return null;
	});
    }
}