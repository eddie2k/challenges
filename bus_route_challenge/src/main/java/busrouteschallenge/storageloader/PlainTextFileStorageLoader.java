package busrouteschallenge.storageloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;

import busrouteschallenge.storage.Storage;
import busrouteschallenge.storage.exception.AlreadyExistingException;
import busrouteschallenge.storageloader.data.BusRoute;
import busrouteschallenge.storageloader.exception.InvalidRouteException;
import busrouteschallenge.storageloader.exception.StorageLoadException;
import busrouteschallenge.storageloader.exception.WrongNumberOfExpectedRoutesException;
import busrouteschallenge.storageloader.parser.Parser;
import busrouteschallenge.storageloader.validator.Validator;

public class PlainTextFileStorageLoader implements StorageLoader {

    private final Parser parser;
    private final Validator validator;
    private final Storage storage;

    @Inject
    public PlainTextFileStorageLoader(Parser parser, Validator validator, Storage storage) {
	Objects.requireNonNull(parser, "parser must be non null");
	Objects.requireNonNull(validator, "validator must be non null");
	Objects.requireNonNull(storage, "storage must be non null");

	this.parser = parser;
	this.validator = validator;
	this.storage = storage;
    }

    @Override
    public void load(BufferedReader reader) {
	Objects.requireNonNull(reader, "reader must be non null");

	try {
	    int numberOfExpectedRoutes = loadExpectedNumberOfRoutes(reader);
	    List<BusRoute> busRoutes = loadBusRoutes(reader);
	    validator.verifyValidBusRoutes(numberOfExpectedRoutes, busRoutes);
	    storeRoutes(busRoutes);
	} catch (IOException | UncheckedIOException | InvalidRouteException | AlreadyExistingException e) {
	    throw new StorageLoadException(e);
	}
    }

    /**
     * Load the expected number of routes, assuming the next line of the reader
     * contains it.
     * 
     * @param reader
     * @return
     * @throws IOException
     *             If there is a problem reading from the reader.
     * @throws WrongNumberOfExpectedRoutesException
     *             If the expected number of routes cannot be parsed.
     */
    private int loadExpectedNumberOfRoutes(BufferedReader reader) throws IOException {
	return parser.parseExpectedNumberOfRoutes(reader.readLine());
    }

    /**
     * Loads the {@link BusRoute}s from the reader, from the current line to the
     * end of the reader.
     * 
     * @param reader
     * @return
     * @throws UncheckedIOException
     *             If there is a problem reading from the reader.
     * @throws InvalidRouteException
     *             If a {@link BusRoute} cannot be parsed.
     */
    private List<BusRoute> loadBusRoutes(BufferedReader reader) {
	return reader.lines().map(parser::parseBusRoute).collect(Collectors.toList());
    }

    /**
     * Stores the given {@link List} of {@link BusRoute}s.
     * 
     * @param busRoutes
     * @throws AlreadyExistingException
     *             if the route was already stored
     */
    private void storeRoutes(List<BusRoute> busRoutes) {
	busRoutes.stream().forEach(storage::storeRoute);
    }
}
