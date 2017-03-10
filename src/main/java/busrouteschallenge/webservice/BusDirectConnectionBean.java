package busrouteschallenge.webservice;

import com.fasterxml.jackson.annotation.JsonProperty;

class BusDirectConnectionBean {

    @JsonProperty("dep_sid")
    public int departureStationId;

    @JsonProperty("arr_sid")
    public int arrivalStationId;

    @JsonProperty("direct_bus_route")
    public boolean directConnection;

    public BusDirectConnectionBean(int departureStationId, int arrivalStationId, boolean directConnection) {
	this.departureStationId = departureStationId;
	this.arrivalStationId = arrivalStationId;
	this.directConnection = directConnection;
    }

}