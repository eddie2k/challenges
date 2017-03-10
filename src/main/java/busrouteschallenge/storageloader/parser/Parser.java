package busrouteschallenge.storageloader.parser;

import org.jvnet.hk2.annotations.Contract;

import busrouteschallenge.storageloader.data.BusRoute;
import busrouteschallenge.storageloader.exception.InvalidRouteException;
import busrouteschallenge.storageloader.exception.WrongNumberOfExpectedRoutesException;

/**
 * Bus routes parser.
 */
@Contract
public interface Parser {

    /**
     * Parses the given line and returns the number of expected routes, assuming
     * the line has the right format.
     * 
     * @param line
     *            The line to be parsed.
     * @return The number of expected routes.
     * @throws WrongNumberOfExpectedRoutesException
     *             If the number of expected routes cannot be read from the
     *             line.
     */
    public int parseExpectedNumberOfRoutes(String line);

    /**
     * Parses the given line and returns a valid {@link BusRoute}, assuming the
     * line has the right format.
     * 
     * @param line
     *            The line to be parsed.
     * @return A valid {@link BusRoute} extracted from the line.
     * @throws InvalidRouteException
     *             If the {@link BusRoute} cannot be read from the line.
     */
    public BusRoute parseBusRoute(String line);
}
