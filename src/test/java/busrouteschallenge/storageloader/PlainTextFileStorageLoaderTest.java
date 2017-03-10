package busrouteschallenge.storageloader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import busrouteschallenge.storage.Storage;
import busrouteschallenge.storage.exception.AlreadyExistingException;
import busrouteschallenge.storageloader.data.BusRoute;
import busrouteschallenge.storageloader.exception.InvalidRouteException;
import busrouteschallenge.storageloader.exception.StorageLoadException;
import busrouteschallenge.storageloader.exception.WrongNumberOfExpectedRoutesException;
import busrouteschallenge.storageloader.parser.Parser;
import busrouteschallenge.storageloader.validator.Validator;

@RunWith(MockitoJUnitRunner.class)
public class PlainTextFileStorageLoaderTest {

    private static final int ANY_INT = 100;

    private static final String ANY_STRING = "ANY_STRING";
    private static final String ANOTHER_STRING = "ANOTHER_STRING";
    private static final String A_THIRD_STRING = "A_THIRD_STRING";

    @Mock
    private BusRoute anyBusRoute = Mockito.mock(BusRoute.class);
    @Mock
    private BusRoute anotherBusRoute = Mockito.mock(BusRoute.class);

    @Mock
    private Parser parser;

    @Mock
    private Validator validator;

    @Mock
    private Storage storage;

    private PlainTextFileStorageLoader sut;

    @Before
    public void setUp() {
	sut = new PlainTextFileStorageLoader(parser, validator, storage);
    }

    // Arguments validation
    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenConstruct_withNullParser() {
	// when
	new PlainTextFileStorageLoader(null, validator, storage);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenConstruct_withNullValidator() {
	// when
	new PlainTextFileStorageLoader(parser, null, storage);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenConstruct_withNullStorage() {
	// when
	new PlainTextFileStorageLoader(parser, validator, null);
    }

    // Functionality testing
    @Test(expected = StorageLoadException.class)
    public void shouldThrowStorageLoadException_whenLoad_withClosedReader() throws IOException {
	// given
	BufferedReader reader = new BufferedReader(new StringReader(ANY_STRING));
	reader.close();

	// when
	sut.load(reader);
    }

    @Test
    public void shouldParseFirstLineAsNumerOfExpectedBusRoutes_whenLoad() {
	// given
	BufferedReader reader = new BufferedReader(new StringReader(ANY_STRING));

	// when
	sut.load(reader);

	// then
	verify(parser).parseExpectedNumberOfRoutes(ANY_STRING);
    }

    @Test
    public void shouldParseSecondAndSucessiveLinesAsBusRoutes_whenLoad() {
	// given
	String anyValidRouteString = "1, 2, 3";
	String anotherValidRouteString = "4, 5, 6";
	BufferedReader reader = new BufferedReader(
		new StringReader(ANY_STRING + "\r\n" + anyValidRouteString + "\r\n" + anotherValidRouteString));

	// when
	sut.load(reader);

	// then
	verify(parser).parseBusRoute(anyValidRouteString);
	verify(parser).parseBusRoute(anotherValidRouteString);
    }

    @Test
    public void shouldValidateNumberOfExpectedRoutes_whenLoad() {
	// given
	BufferedReader reader = new BufferedReader(
		new StringReader(ANY_STRING + "\r\n" + ANOTHER_STRING + "\r\n" + A_THIRD_STRING));
	doReturn(ANY_INT).when(parser).parseExpectedNumberOfRoutes(ANY_STRING);

	doReturn(anyBusRoute).when(parser).parseBusRoute(ANOTHER_STRING);
	doReturn(anotherBusRoute).when(parser).parseBusRoute(A_THIRD_STRING);

	// when
	sut.load(reader);

	// then
	ArgumentCaptor<List<BusRoute>> listCaptor = forClass((Class) List.class);
	ArgumentCaptor<Integer> integerCaptor = forClass(Integer.class);
	verify(validator).verifyValidBusRoutes(integerCaptor.capture(), listCaptor.capture());
	assertThat(integerCaptor.getValue()).isEqualTo(ANY_INT);
    }

    @Test
    public void shouldValidateBusRoutes_whenLoad() {
	// given
	BufferedReader reader = new BufferedReader(
		new StringReader(ANY_STRING + "\r\n" + ANOTHER_STRING + "\r\n" + A_THIRD_STRING));
	doReturn(ANY_INT).when(parser).parseExpectedNumberOfRoutes(ANY_STRING);

	doReturn(anyBusRoute).when(parser).parseBusRoute(ANOTHER_STRING);
	doReturn(anotherBusRoute).when(parser).parseBusRoute(A_THIRD_STRING);

	// when
	sut.load(reader);

	// then
	ArgumentCaptor<List<BusRoute>> listCaptor = forClass((Class) List.class);
	ArgumentCaptor<Integer> integerCaptor = forClass(Integer.class);
	verify(validator).verifyValidBusRoutes(integerCaptor.capture(), listCaptor.capture());
	assertThat(listCaptor.getValue()).containsOnly(anyBusRoute, anotherBusRoute);
    }

    @Test
    public void shouldStoreRoutes_whenLoad_withValidData() {
	// given
	BufferedReader reader = new BufferedReader(
		new StringReader(ANY_STRING + "\r\n" + ANOTHER_STRING + "\r\n" + A_THIRD_STRING));

	// First line of file (irrelevant in this test case)
	doReturn(ANY_INT).when(parser).parseExpectedNumberOfRoutes(ANY_STRING);
	// Route 66 and its stops
	doReturn(anyBusRoute).when(parser).parseBusRoute(ANOTHER_STRING);
	// Route 33 and its stops
	doReturn(anotherBusRoute).when(parser).parseBusRoute(A_THIRD_STRING);

	// when
	sut.load(reader);

	// then
	verify(storage).storeRoute(anyBusRoute);
	verify(storage).storeRoute(anotherBusRoute);
    }

    @Test(expected = StorageLoadException.class)
    public void shouldThrowStorageLoadExceptionot_whenLoad_andParserThrowsWrongNumberOfExpectedRoutesException() {
	// given
	BufferedReader reader = new BufferedReader(
		new StringReader(ANY_STRING + "\r\n" + ANOTHER_STRING + "\r\n" + A_THIRD_STRING));
	doThrow(WrongNumberOfExpectedRoutesException.class).when(parser).parseExpectedNumberOfRoutes(ANY_STRING);

	// when
	sut.load(reader);
    }

    @Test
    public void shouldNotStoreRoutes_whenLoad_andParserThrowsWrongNumberOfExpectedRoutesException() {
	// given
	BufferedReader reader = new BufferedReader(
		new StringReader(ANY_STRING + "\r\n" + ANOTHER_STRING + "\r\n" + A_THIRD_STRING));
	doThrow(WrongNumberOfExpectedRoutesException.class).when(parser).parseExpectedNumberOfRoutes(ANY_STRING);

	// when
	try {
	    sut.load(reader);
	} catch (StorageLoadException e) {
	}

	// then
	verifyZeroInteractions(storage);
    }

    @Test
    public void shouldNotStoreRoutes_whenLoad_andReaderThrowsIOException() {
	// given
	BufferedReader reader = new BufferedReader(new Reader() {

	    @Override
	    public int read(char[] cbuf, int off, int len) throws IOException {
		throw new IOException();
	    }

	    @Override
	    public void close() throws IOException {
	    }
	});

	// when
	try {
	    sut.load(reader);
	} catch (StorageLoadException e) {
	}

	// then
	verifyZeroInteractions(storage);
    }

    @Test(expected = StorageLoadException.class)
    public void shouldThrowStorageLoadException_whenLoad_andParserThrowsInvalidRouteDataException() {
	// given
	BufferedReader reader = new BufferedReader(
		new StringReader(ANY_STRING + "\r\n" + ANOTHER_STRING + "\r\n" + A_THIRD_STRING));
	doThrow(InvalidRouteException.class).when(parser).parseBusRoute(anyString());

	// when
	sut.load(reader);
    }

    public void shouldNotStoreRoutes_whenLoad_andParserThrowsInvalidRouteDataException() {
	// given
	BufferedReader reader = new BufferedReader(
		new StringReader(ANY_STRING + "\r\n" + ANOTHER_STRING + "\r\n" + A_THIRD_STRING));
	doThrow(InvalidRouteException.class).when(parser).parseBusRoute(anyString());

	// when
	try {
	    sut.load(reader);
	} catch (StorageLoadException e) {
	}

	// then
	verifyZeroInteractions(storage);
    }

    @Test
    public void shouldNotStoreRoutes_whenLoad_andValidatorThrowsInvalidRouteException() {
	// given
	BufferedReader reader = new BufferedReader(
		new StringReader(ANY_STRING + "\r\n" + ANOTHER_STRING + "\r\n" + A_THIRD_STRING));
	doReturn(ANY_INT).when(parser).parseExpectedNumberOfRoutes(ANY_STRING);
	doReturn(anyBusRoute).when(parser).parseBusRoute(ANOTHER_STRING);
	doReturn(anotherBusRoute).when(parser).parseBusRoute(A_THIRD_STRING);
	doThrow(InvalidRouteException.class).when(validator).verifyValidBusRoutes(anyInt(), Mockito.anyList());

	// when
	try {
	    sut.load(reader);
	} catch (StorageLoadException e) {
	}

	// then
	verifyZeroInteractions(storage);
    }

    @Test(expected = StorageLoadException.class)
    public void shouldThrowStorageLoadException_whenLoad_andStorageThrowsAlreadyExistingException() {
	// given
	BufferedReader reader = new BufferedReader(
		new StringReader(ANY_STRING + "\r\n" + ANOTHER_STRING + "\r\n" + A_THIRD_STRING));
	doThrow(AlreadyExistingException.class).when(storage).storeRoute(any(BusRoute.class));

	// when
	sut.load(reader);
    }
}
