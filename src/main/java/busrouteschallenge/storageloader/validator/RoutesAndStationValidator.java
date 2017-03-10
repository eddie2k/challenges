package busrouteschallenge.storageloader.validator;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;

import busrouteschallenge.storageloader.data.BusRoute;
import busrouteschallenge.storageloader.exception.DuplicatedRouteException;
import busrouteschallenge.storageloader.exception.DuplicatedStationException;
import busrouteschallenge.storageloader.exception.NotEnoughStationsInRouteException;
import busrouteschallenge.storageloader.exception.TooManyDifferentStationsException;
import busrouteschallenge.storageloader.exception.TooManyRoutesException;
import busrouteschallenge.storageloader.exception.TooManyStationsInRouteException;
import busrouteschallenge.storageloader.exception.WrongNumberOfExpectedRoutesException;

/**
 * Bus routes validator that deeply checks the routes for verification.<br>
 * <br>
 * It is checked: <br>
 * <li>that the number of actual routes matches the number of expected routes,
 * greater than 0, and lesser than a system threshold.</li>
 * <li>there are no duplicated routes in the system (denoted by the route
 * identifier).</li>
 * <li>that the number of stations in a route is greater than a system threshold
 * (which must be greater than 2) and lesser than another system threshold.</li>
 * <li>there are no duplicated stations in a route.</li>
 * <li>there are no more different stations (in all the routes) than a system
 * limit</li>
 */
public class RoutesAndStationValidator implements Validator {

    private static final Logger LOGGER = Logger.getRootLogger();

    private final int maxRoutes;
    private final int minStationsPerRoute;
    private final int maxStationsPerRoute;
    // Max number of different stations in the system (all the stations in all
    // the routes)
    private final int maxAbsoluteStations;

    public RoutesAndStationValidator(int maxRoutes, int minStationsPerRoute, int maxStationsPerRoute,
	    int maxAbsoluteStations) {
	if (maxRoutes < 0) {
	    throw new IllegalArgumentException("maxRoutes must be non-negative");
	}
	if (minStationsPerRoute < 2) {
	    throw new IllegalArgumentException("minStationsPerRoute must be greater or equal than 2");
	}
	if (maxStationsPerRoute < minStationsPerRoute) {
	    throw new IllegalArgumentException("maxStationsPerRoute must not be lesser than minStationsPerRoute");
	}

	if (maxAbsoluteStations < 0) {
	    throw new IllegalArgumentException("maxAbsoluteStations must be non-negative");
	}
	if (maxAbsoluteStations < minStationsPerRoute) {
	    throw new IllegalArgumentException("maxAbsoluteStations must not be lesser than minStationsPerRoute");
	}

	this.maxRoutes = maxRoutes;
	this.minStationsPerRoute = minStationsPerRoute;
	this.maxStationsPerRoute = maxStationsPerRoute;
	this.maxAbsoluteStations = maxAbsoluteStations;
    }

    @Override
    public void verifyValidBusRoutes(int numberOfExpectedRoutes, List<BusRoute> busRoutes) {
	Objects.requireNonNull(busRoutes, "busRoutes must be not null");
	LOGGER.debug("Validating data...");

	LOGGER.debug("Validating data (1/7)");
	verifyNumberOfExpectedRoutes(numberOfExpectedRoutes);
	LOGGER.debug("Validating data (2/7)");
	verifyNoDuplicatedRoutes(busRoutes);
	LOGGER.debug("Validating data (3/7)");
	verifyNoDuplicatedStations(busRoutes);
	LOGGER.debug("Validating data (4/7)");
	verifyNoTooManyRoutes(numberOfExpectedRoutes, busRoutes);
	LOGGER.debug("Validating data (5/7)");
	verifyNotEnoughStationsInRoute(busRoutes);
	LOGGER.debug("Validating data (6/7)");
	verifyTooManyStationsInRoute(busRoutes);
	LOGGER.debug("Validating data (7/7)");
	verifyTooManyDifferentStations(busRoutes);

	LOGGER.debug("Validation ended");
    }

    private void verifyNumberOfExpectedRoutes(int numberOfExpectedRoutes) {
	if (numberOfExpectedRoutes < 0 || numberOfExpectedRoutes > maxRoutes) {
	    throw new WrongNumberOfExpectedRoutesException(
		    "Number of expected routes must be between 0 and " + maxRoutes);
	}
    }

    private static void verifyNoDuplicatedRoutes(List<BusRoute> busRoutes) {
	if (containsDuplicates(busRoutes.stream().map(BusRoute::getRouteId).collect(toList()))) {
	    throw new DuplicatedRouteException();
	}
    }

    private static void verifyNoDuplicatedStations(List<BusRoute> busRoutes) {
	if (busRoutes.stream().filter(route -> containsDuplicates(route.getStationIds())).findAny().isPresent()) {
	    throw new DuplicatedStationException();
	}
    }

    private void verifyNoTooManyRoutes(int numberOfExpectedRoutes, List<BusRoute> busRoutes) {
	int numberOfActualRoutes = busRoutes.size();
	if (numberOfExpectedRoutes != numberOfActualRoutes) {
	    throw new TooManyRoutesException(
		    "There are more routes (" + numberOfActualRoutes + ") than limit (" + maxRoutes + " )");
	}
    }

    private void verifyNotEnoughStationsInRoute(List<BusRoute> busRoutes) {
	if (busRoutes.stream().filter(busRoute -> busRoute.getStationIds().size() < minStationsPerRoute).findAny()
		.isPresent()) {
	    throw new NotEnoughStationsInRouteException(
		    "There must be at least " + minStationsPerRoute + " in each route");
	}
    }

    private void verifyTooManyStationsInRoute(List<BusRoute> busRoutes) {
	if (busRoutes.stream().filter(busRoute -> busRoute.getStationIds().size() > maxStationsPerRoute).findAny()
		.isPresent()) {
	    throw new TooManyStationsInRouteException(
		    "There must be at most " + maxStationsPerRoute + " in each route");
	}
    }

    private void verifyTooManyDifferentStations(List<BusRoute> busRoutes) {
	long actualAbsoluteNumberOfStations = busRoutes.stream().flatMap(br -> br.getStationIds().stream()).distinct()
		.count();
	if (actualAbsoluteNumberOfStations > maxAbsoluteStations) {
	    throw new TooManyDifferentStationsException("There must be at most " + maxAbsoluteStations
		    + " different stations (currently=" + actualAbsoluteNumberOfStations + ")");
	}
    }

    private static boolean containsDuplicates(List<Integer> list) {
	return list.stream().distinct().count() != list.size();
    }

}