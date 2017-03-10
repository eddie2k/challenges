package busrouteschallenge.localservice;

import static java.util.stream.IntStream.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import busrouteschallenge.localservice.exception.LocalServiceNotReadyException;
import busrouteschallenge.storage.Storage;
import busrouteschallenge.storageloader.AsyncStorageLoader;

@RunWith(MockitoJUnitRunner.class)
public class BusRoutesLocalServiceTest {

    private static final int ANY_ROUTE_ID = 1;
    private static final int ANY_STATION_ID = 123;
    private static final int ANOTHER_STATION_ID = 456;

    private static final String ANY_STRING = "ANY_STRING";
    private static final Path ANY_PATH = Paths.get(ANY_STRING);

    @Mock
    private Storage storage;

    @Mock
    private AsyncStorageLoader loader;

    private BusRoutesLocalService sut;

    @Before
    public void setUp() {
	sut = new BusRoutesLocalService(storage, loader, ANY_PATH);
    }

    // Construction arguments validation
    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenStorageIsNull() {
	// when
	new BusRoutesLocalService(null, loader, ANY_PATH);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenLoaderNull() {
	// when
	new BusRoutesLocalService(storage, null, ANY_PATH);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenPathIsNull() {
	// when
	new BusRoutesLocalService(storage, loader, null);
    }

    // Test
    @Test(expected = LocalServiceNotReadyException.class)
    public void shouldThrowLocalServiceNotReadyException_whenCheckingConnection_andLoadIsNotFinished() {
	// given
	// (default status)

	// when
	sut.isDirectConnection(ANY_STATION_ID, ANOTHER_STATION_ID);
    }

    @Test(expected = LocalServiceNotReadyException.class)
    public void shouldThrowLocalServiceNotReadyException_whenCheckingConnection_andLoadFailed() {
	// given
	ArgumentCaptor<Function> onFailureCaptor = forClass(Function.class);
	verify(loader).load(any(), any(), onFailureCaptor.capture());
	onFailureCaptor.getValue().apply(null);

	// when
	sut.isDirectConnection(ANY_STATION_ID, ANOTHER_STATION_ID);
    }

    @Test
    public void shouldNotFindDirectConnection_whenCheckingConnection_andStorageIsLoaded_andThereIsNoConnection() {
	// given
	doReturn(empty()).when(storage).findConnections(ANY_STATION_ID, ANOTHER_STATION_ID);

	ArgumentCaptor<Function> onSuccessCaptor = forClass(Function.class);
	verify(loader).load(any(), onSuccessCaptor.capture(), any());

	Function<Void, Void> onSuccess = onSuccessCaptor.getValue();
	onSuccess.apply(null);

	// when
	boolean isDirect = sut.isDirectConnection(ANY_STATION_ID, ANOTHER_STATION_ID);

	// then
	assertThat(isDirect).isFalse();
    }

    @Test
    public void shouldFindDirectConnection_whenCheckingConnection_andStorageIsLoaded_andThereAreConnections() {
	// given
	doReturn(IntStream.of(ANY_ROUTE_ID)).when(storage).findConnections(ANY_STATION_ID, ANOTHER_STATION_ID);

	ArgumentCaptor<Function> onSuccessCaptor = forClass(Function.class);
	verify(loader).load(any(), onSuccessCaptor.capture(), any());

	Function<Void, Void> onSuccess = onSuccessCaptor.getValue();
	onSuccess.apply(null);

	// when
	boolean isDirect = sut.isDirectConnection(ANY_STATION_ID, ANOTHER_STATION_ID);

	// then
	assertThat(isDirect).isTrue();
    }
}
