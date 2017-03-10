package busrouteschallenge.storage;

import java.util.stream.IntStream;

import org.jvnet.hk2.annotations.Contract;

import busrouteschallenge.storage.exception.AlreadyExistingException;
import busrouteschallenge.storageloader.data.BusRoute;

/**
 * Storage for bus routes.
 */
@Contract
public interface Storage {

    /**
     * Stores a bus route in the storage.
     * 
     * @param busRoute
     *            The bus route to be stored.
     * 
     * @throws AlreadyExistingException
     *             if there is already a bus route with the same identifier.
     */
    public void storeRoute(BusRoute busRoute);

    /**
     * Finds the bus routes between the given station (potentially in a lazy
     * strategy).
     * 
     * @param fromStationId
     * @param toStationId
     * @return
     */
    public IntStream findConnections(int fromStationId, int toStationId);

}
