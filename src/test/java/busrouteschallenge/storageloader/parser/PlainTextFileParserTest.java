package busrouteschallenge.storageloader.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import busrouteschallenge.storageloader.data.BusRoute;
import busrouteschallenge.storageloader.exception.InvalidRouteException;
import busrouteschallenge.storageloader.exception.WrongNumberOfExpectedRoutesException;

public class PlainTextFileParserTest {

    private static final int ANY_NUMBER = 1;
    private static final int ANOTHER_NUMBER = 2;
    private static final int A_THIRD_NUMBER = 3;

    private static final String DELIM = " ";
    private static final String EMPTY_STRING = "";

    private PlainTextFileParser sut;

    @Before
    public void setUp() {
	sut = new PlainTextFileParser(DELIM);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenConstruction_withNullDelimiter() {
	// when
	sut = new PlainTextFileParser(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenParseExpectedNumberOfRoutes_withNullString() {
	// when
	sut.parseExpectedNumberOfRoutes(null);
    }

    @Test(expected = WrongNumberOfExpectedRoutesException.class)
    public void shouldThrowWrongNumberOfExpectedRoutesException_whenParseExpectedNumberOfRoutes_withEmptyString() {
	// when
	sut.parseExpectedNumberOfRoutes(EMPTY_STRING);
    }

    @Test(expected = WrongNumberOfExpectedRoutesException.class)
    public void shouldThrowWrongNumberOfExpectedRoutesException_whenParseExpectedNumberOfRoutes_withNonIntegerString() {
	// when
	sut.parseExpectedNumberOfRoutes("12.34");
    }

    @Test(expected = WrongNumberOfExpectedRoutesException.class)
    public void shouldThrowWrongNumberOfExpectedRoutesException_whenParseExpectedNumberOfRoutes_withAlphanumericString() {
	// when
	sut.parseExpectedNumberOfRoutes("onehundred50");
    }

    @Test(expected = WrongNumberOfExpectedRoutesException.class)
    public void shouldThrowWrongNumberOfExpectedRoutesException_whenParseExpectedNumberOfRoutes_withSeveralIntsString() {
	// when
	sut.parseExpectedNumberOfRoutes("12" + DELIM + "34");
    }

    @Test
    public void shouldReturnNumberOfExpectedRoutesException_whenParseExpectedNumberOfRoutes_withSingleIntsString() {
	// when
	sut.parseExpectedNumberOfRoutes("100");
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenParseBusRoutes_withNullString() {
	// when
	sut.parseBusRoute(null);
    }

    @Test(expected = InvalidRouteException.class)
    public void shouldThrowInvalidRouteException_whenParseBusRoutes_withEmptyString() {
	// when
	sut.parseBusRoute(EMPTY_STRING);
    }

    @Test(expected = InvalidRouteException.class)
    public void shouldThrowInvalidRouteException_whenParseBusRoutes_withNonIntegerString() {
	// when
	sut.parseBusRoute("12" + DELIM + "34" + DELIM + "56" + DELIM + "7.8");
    }

    @Test(expected = InvalidRouteException.class)
    public void shouldThrowInvalidRouteException_whenParseBusRoutes_withAlphanumericString() {
	// when
	sut.parseBusRoute("12" + DELIM + "34" + DELIM + "56" + DELIM + "seveneight");
    }

    @Test(expected = InvalidRouteException.class)
    public void shouldThrowInvalidRouteException_whenParseBusRoutes_withLessThanThreeIntegerString() {
	// when
	sut.parseBusRoute("12" + DELIM + "34");
    }

    @Test
    public void shouldBusRouteWithValidRouteId_whenParseBusRoutes_withThreeIntegersString() {
	// when
	BusRoute busRoute = sut.parseBusRoute(ANY_NUMBER + DELIM + ANOTHER_NUMBER + DELIM + A_THIRD_NUMBER);

	// then
	assertThat(busRoute.getRouteId()).isEqualTo(ANY_NUMBER);
    }

    @Test
    public void shouldBusRouteWithValidRouteIdAndStationIds_whenParseBusRoutes_withThreeIntegersString() {
	// when
	BusRoute busRoute = sut.parseBusRoute(ANY_NUMBER + DELIM + ANOTHER_NUMBER + DELIM + A_THIRD_NUMBER);

	// then
	assertThat(busRoute.getStationIds()).isEqualTo(Arrays.asList(ANOTHER_NUMBER, A_THIRD_NUMBER));
    }
}
