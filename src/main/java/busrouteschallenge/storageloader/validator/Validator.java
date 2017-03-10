package busrouteschallenge.storageloader.validator;

import java.util.List;

import org.jvnet.hk2.annotations.Contract;

import busrouteschallenge.storageloader.data.BusRoute;
import busrouteschallenge.storageloader.exception.InvalidRouteException;

/**
 * Bus routes validator.
 */
@Contract
public interface Validator {

    /**
     * Checks if the given List of {@link BusRoute}s are valid.
     * 
     * @param numberOfExpectedRoutes
     *            The number of expected routes.
     * @param busRoutes
     *            List of {@link BusRoute}s to be checked.
     * @throws InvalidRouteException
     *             If validation do not succeed.
     */
    public void verifyValidBusRoutes(int numberOfExpectedRoutes, List<BusRoute> busRoutes);

}
