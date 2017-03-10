package busrouteschallenge.storageloader;

import java.io.BufferedReader;

import busrouteschallenge.storageloader.exception.StorageLoadException;

/**
 * Synchronous loader for data storage.
 */
public interface StorageLoader {

    /**
     * Loads the storage from the given reader,
     * 
     * @param reader
     * 
     * @throws StorageLoadException
     *             If an error occur during the data load.
     */
    public void load(BufferedReader reader);

}
