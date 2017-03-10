package busrouteschallenge.storage;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

import busrouteschallenge.storage.exception.AlreadyExistingException;
import busrouteschallenge.storageloader.data.BusRoute;

public class InMemoryStorageTest {

    private static final int ANY_STATION_ID = 123;
    private static final int ANOTHER_STATION_ID = 456;

    private static final BusRoute ANY_BUS_ROUTE = new BusRoute(100, asList(4, 5, 6));

    private Storage sut;

    @Before
    public void setUp() {
	sut = new InMemoryStorage();
    }

    @Test(expected = AlreadyExistingException.class)
    public void shouldThrowAlreadyExistingException_whenStoringSameTwice() {
	// when
	sut.storeRoute(ANY_BUS_ROUTE);
	sut.storeRoute(ANY_BUS_ROUTE);
    }

    @Test
    public void shouldNotFindConnections_whenStorageIsEmpty() {
	// when
	IntStream s = sut.findConnections(ANY_STATION_ID, ANOTHER_STATION_ID);

	// then
	assertThat(s.count()).isZero();
    }

    @Test
    public void shouldNotFindConnections_whenRouteIsNotStored() {
	// given
	sut.storeRoute(new BusRoute(66, asList(1, 2, 3)));
	sut.storeRoute(new BusRoute(100, asList(4, 5, 6)));

	// when
	IntStream s = sut.findConnections(1, 6);

	// then
	assertThat(s.count()).isZero();
    }

    @Test
    public void shouldNotFindConnections_whenReverseRouteIsStored() {
	// given
	sut.storeRoute(new BusRoute(66, asList(0, 1)));

	// when
	IntStream s = sut.findConnections(1, 0);

	// then
	assertThat(s.count()).isZero();
    }

    @Test
    public void shouldFindConnections_whenRouteIsStored() {
	// given
	sut.storeRoute(new BusRoute(66, asList(1, 2, 3)));
	sut.storeRoute(new BusRoute(100, asList(1, 3, 6)));

	// when
	IntStream s = sut.findConnections(1, 3);

	// then
	assertThat(s.count()).isEqualTo(2);
    }
}
