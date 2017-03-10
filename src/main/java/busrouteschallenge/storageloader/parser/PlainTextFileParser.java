package busrouteschallenge.storageloader.parser;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import busrouteschallenge.storageloader.data.BusRoute;
import busrouteschallenge.storageloader.exception.InvalidRouteException;
import busrouteschallenge.storageloader.exception.WrongNumberOfExpectedRoutesException;

public class PlainTextFileParser implements Parser {

    private static final Logger LOGGER = Logger.getRootLogger();

    private final String delimiter;

    public PlainTextFileParser(String delimiter) {
	Objects.requireNonNull(delimiter, "delimiter must not be null");

	this.delimiter = delimiter;
    }

    @Override
    public int parseExpectedNumberOfRoutes(String line) {
	Objects.requireNonNull(line, "line must not be null");

	LOGGER.debug("Parsing line: " + line.substring(0, Math.min(15, line.length())) + "...");

	String[] tokens = line.split(delimiter);
	if (tokens.length != 1) {
	    throw new WrongNumberOfExpectedRoutesException(
		    "The number of expected routes must be exactly one single integer (current=" + line + ")");
	}

	try {
	    return Integer.parseInt(line);
	} catch (NumberFormatException e) {
	    throw new WrongNumberOfExpectedRoutesException(
		    "The number of expected routes must be exactly one single integer (current=" + line + ")");
	}
    }

    @Override
    public BusRoute parseBusRoute(String line) {
	Objects.requireNonNull(line, "line must not be null");

	LOGGER.debug("Parsing line: " + line.substring(0, Math.min(15, line.length())) + "...");

	try {
	    List<Integer> ints = Arrays.asList(line.split(delimiter)).stream().map(Integer::parseInt)
		    .collect(Collectors.toList());
	    if (ints.size() >= 3) {
		return new BusRoute(ints.get(0), ints.subList(1, ints.size()));
	    } else {
		throw new InvalidRouteException(
			"A bus route must contain at least three valid integers (currently=" + line + ")");
	    }

	} catch (NumberFormatException e) {
	    throw new InvalidRouteException(
		    "A bus route must contain at least three valid integers (currently=" + line + ")");
	}
    }
}