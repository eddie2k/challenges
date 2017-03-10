package busrouteschallenge.storageloader.validator;

import static java.util.Arrays.asList;

import org.junit.Before;
import org.junit.Test;

import busrouteschallenge.storageloader.data.BusRoute;
import busrouteschallenge.storageloader.exception.DuplicatedRouteException;
import busrouteschallenge.storageloader.exception.DuplicatedStationException;
import busrouteschallenge.storageloader.exception.NotEnoughStationsInRouteException;
import busrouteschallenge.storageloader.exception.TooManyDifferentStationsException;
import busrouteschallenge.storageloader.exception.TooManyRoutesException;
import busrouteschallenge.storageloader.exception.TooManyStationsInRouteException;
import busrouteschallenge.storageloader.exception.WrongNumberOfExpectedRoutesException;

public class RoutesAndStationValidatorTest {

    private static final int ANOTHER_NUMBER = 456;
    private static final int MAX_ABSOLUTE_STATIONS = 1_000_000;
    private static final int MIN_STATIONS_PER_ROUTE = 2;
    private static final int MAX_STATIONS_PER_ROUTE = 1_000;
    private static final int MAX_ROUTES = 100_000;

    private static final int ANY_NUMBER = 123;

    private static final BusRoute ANY_BUS_ROUTE = new BusRoute(100, asList(4, 5, 6));
    private static final BusRoute ANOTHER_BUS_ROUTE = new BusRoute(66, asList(1, 2, 3));

    private RoutesAndStationValidator sut;

    @Before
    public void setUp() {
	sut = new RoutesAndStationValidator(MAX_ROUTES, MIN_STATIONS_PER_ROUTE, MAX_STATIONS_PER_ROUTE,
		MAX_ABSOLUTE_STATIONS);
    }

    // Constructor arguments validation
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIAE_whenMaxRoutes_isNegative() {
	// given
	new RoutesAndStationValidator(-1, ANY_NUMBER, ANY_NUMBER, ANY_NUMBER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIAE_whenMinStationsPerRoute_isLesserThan2() {
	// given
	new RoutesAndStationValidator(ANY_NUMBER, 1, ANY_NUMBER, ANY_NUMBER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIAE_whenMaxStationsPerRoute_isLesserThanMinStationsPerRoute() {
	// given
	new RoutesAndStationValidator(ANY_NUMBER, 10, 9, ANY_NUMBER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIAE_whenMaxAbsouteStations_isNegative() {
	// given
	new RoutesAndStationValidator(ANY_NUMBER, ANY_NUMBER, ANY_NUMBER, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIAE_whenMaxAbsouteStations_isLesserThanMinStationsPerRoute() {
	// given
	new RoutesAndStationValidator(ANY_NUMBER, 10, ANY_NUMBER, 9);
    }

    // Validation of NumberOfExpectedRoutes
    @Test(expected = WrongNumberOfExpectedRoutesException.class)
    public void shouldThrowWrongNumberOfExpectedRoutesException_whenVerifyingNumberOfExpectedRoutes_withNegativeValue() {
	// when
	sut.verifyValidBusRoutes(-1, asList(ANY_BUS_ROUTE));
    }

    @Test(expected = WrongNumberOfExpectedRoutesException.class)
    public void shouldThrowWrongNumberOfExpectedRoutesException_whenNumberOfExpectedRoutesIsGreaterThanMaxRoutes() {
	// when
	sut = new RoutesAndStationValidator(1, MIN_STATIONS_PER_ROUTE, MAX_STATIONS_PER_ROUTE, MAX_ABSOLUTE_STATIONS);
	sut.verifyValidBusRoutes(2, asList(ANY_BUS_ROUTE));
    }

    @Test
    public void shouldAccepttNumberOfExpected_whenNumberOfExpectedRoutesIsValid() {
	// when
	sut = new RoutesAndStationValidator(1, MIN_STATIONS_PER_ROUTE, MAX_STATIONS_PER_ROUTE, MAX_ABSOLUTE_STATIONS);
	sut.verifyValidBusRoutes(1, asList(ANY_BUS_ROUTE));
    }

    // Validation of routes
    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenVerifyingNull() {
	// when
	sut.verifyValidBusRoutes(ANY_NUMBER, null);
    }

    @Test(expected = WrongNumberOfExpectedRoutesException.class)
    public void shouldThrowWrongNumberOfExpectedRoutesException_whenMoreRoutesThanLimit() {
	// given
	sut = new RoutesAndStationValidator(1, MIN_STATIONS_PER_ROUTE, MAX_STATIONS_PER_ROUTE, MAX_ABSOLUTE_STATIONS);

	// when
	sut.verifyValidBusRoutes(2, asList(ANY_BUS_ROUTE, ANOTHER_BUS_ROUTE));
    }

    @Test(expected = DuplicatedRouteException.class)
    public void shouldThrowDuplicatedRouteException_whenRouteIsPresentTwice() {
	// when
	sut.verifyValidBusRoutes(2, asList(ANY_BUS_ROUTE, ANY_BUS_ROUTE));
    }

    @Test(expected = DuplicatedStationException.class)
    public void shouldThrowDuplicatedStationException_whenStationIsPresentTwice() {
	// when
	sut.verifyValidBusRoutes(1, asList(new BusRoute(66, asList(1, 2, 2))));
    }

    @Test(expected = TooManyRoutesException.class)
    public void shouldThrowTooManyRoutesException_whenMoreRoutesThanLimit() {
	// given
	sut = new RoutesAndStationValidator(2, MIN_STATIONS_PER_ROUTE, MAX_STATIONS_PER_ROUTE, MAX_ABSOLUTE_STATIONS);

	// when
	sut.verifyValidBusRoutes(1, asList(ANY_BUS_ROUTE, ANOTHER_BUS_ROUTE));
    }

    @Test(expected = NotEnoughStationsInRouteException.class)
    public void shouldThrowNotEnoughStationsInRouteException_whenMoreRoutesThanLimit() {
	// given
	sut = new RoutesAndStationValidator(MAX_ROUTES, 3, 5, MAX_ABSOLUTE_STATIONS);

	// when
	sut.verifyValidBusRoutes(1, asList(new BusRoute(ANY_NUMBER, asList(ANY_NUMBER, ANOTHER_NUMBER))));
    }

    @Test(expected = TooManyStationsInRouteException.class)
    public void shouldThrowTooManyStationsInRouteException_whenMoreRoutesThanLimit() {
	// given
	sut = new RoutesAndStationValidator(MAX_ROUTES, 2, 2, MAX_ABSOLUTE_STATIONS);

	// when
	sut.verifyValidBusRoutes(1, asList(new BusRoute(66, asList(1, 2, 3))));
    }

    @Test(expected = TooManyDifferentStationsException.class)
    public void shouldThrowTooManyDifferentStationsException_whenMoreRoutesThanLimit() {
	// given
	sut = new RoutesAndStationValidator(MAX_ROUTES, MIN_STATIONS_PER_ROUTE, MAX_STATIONS_PER_ROUTE, 5);

	// when
	sut.verifyValidBusRoutes(2, asList(new BusRoute(66, asList(1, 2, 3)), new BusRoute(100, asList(4, 5, 6))));
    }

    @Test
    public void shouldNotThrowTooManyDifferentStationsException_whenFewerRoutesThanLimit() {
	// given
	sut = new RoutesAndStationValidator(MAX_ROUTES, MIN_STATIONS_PER_ROUTE, MAX_STATIONS_PER_ROUTE, 5);

	// when
	sut.verifyValidBusRoutes(2, asList(new BusRoute(66, asList(1, 2, 3)), new BusRoute(100, asList(3, 4, 5))));
    }

    @Test
    public void shouldNotThrowException_whenDataIsValid() {
	// when
	sut.verifyValidBusRoutes(2, asList(ANY_BUS_ROUTE, ANOTHER_BUS_ROUTE));
    }
}
