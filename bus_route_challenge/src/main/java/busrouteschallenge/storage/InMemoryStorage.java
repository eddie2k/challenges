package busrouteschallenge.storage;

import static java.util.Collections.emptyMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.log4j.Logger;

import busrouteschallenge.storage.exception.AlreadyExistingException;
import busrouteschallenge.storageloader.data.BusRoute;

public class InMemoryStorage implements Storage {

    private static final Logger LOGGER = Logger.getRootLogger();

    /**
     * For each station, stores which bus routes reach the station and in which
     * sequence number in that journey. Invariant: null keys or values will
     * never be inserted.
     */
    private final Map<Integer, Map<Integer, Short>> stationToRoutes;

    public InMemoryStorage() {
	this.stationToRoutes = new HashMap<>();
    }

    @Override
    public void storeRoute(BusRoute busRoute) throws AlreadyExistingException {
	LOGGER.debug(
		"Storing route " + busRoute.getRouteId() + " (" + busRoute.getStationIds().size() + " stations)...");
	int routeId = busRoute.getRouteId();
	List<Integer> stations = busRoute.getStationIds();
	int numberOfStations = stations.size();

	IntStream.range(0, numberOfStations)
		.forEach(index -> storeStationInRoute(routeId, stations.get(index), (short) index));
    }

    private void storeStationInRoute(int routeId, Integer stationId, short seqNumber) {
	Short previous = stationToRoutes.computeIfAbsent(stationId, k -> new HashMap<>()).putIfAbsent(routeId,
		seqNumber);
	if (previous != null) {
	    throw new AlreadyExistingException("Route " + routeId + " already contained the station " + stationId);
	}
    }

    @Override
    public IntStream findConnections(int fromStationId, int toStationId) {
	Map<Integer, Short> routesToDepartureStation = stationToRoutes.getOrDefault(fromStationId, emptyMap());
	Map<Integer, Short> routesToArrivalStation = stationToRoutes.getOrDefault(toStationId, emptyMap());

	return routesToArrivalStation.entrySet().stream()
		.filter(arrBusNumberAndSeq -> routesToDepartureStation.containsKey(arrBusNumberAndSeq.getKey()))
		.map(arrBusNumberAndSeq -> new BusJourney(arrBusNumberAndSeq.getKey(),
			(short) routesToDepartureStation.get(arrBusNumberAndSeq.getKey()),
			(short) arrBusNumberAndSeq.getValue()))
		.filter(journey -> journey.getDepartureSeq() < journey.getArrivalSeq())
		.mapToInt(BusJourney::getRouteId);
    }
}

class BusJourney {
    private int routeId;
    private short departureSeq;
    private short arrivalSeq;

    public BusJourney(int routeId, short departureSeq, short arrivalSeq) {
	this.routeId = routeId;
	this.departureSeq = departureSeq;
	this.arrivalSeq = arrivalSeq;
    }

    public int getRouteId() {
	return routeId;
    }

    public short getDepartureSeq() {
	return departureSeq;
    }

    public short getArrivalSeq() {
	return arrivalSeq;
    }

}
