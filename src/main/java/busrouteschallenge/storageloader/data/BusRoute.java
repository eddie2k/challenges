package busrouteschallenge.storageloader.data;

import java.util.Collections;
import java.util.List;

public class BusRoute {

    private final int routeId;
    private final List<Integer> stationIds;

    public BusRoute(Integer routeId, List<Integer> stationIds) {
	this.routeId = routeId;
	this.stationIds = stationIds;
    }

    public int getRouteId() {
	return routeId;
    }

    public List<Integer> getStationIds() {
	return Collections.unmodifiableList(stationIds);
    }

}
