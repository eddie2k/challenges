package busrouteschallenge.webservice;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.glassfish.hk2.api.Immediate;

import busrouteschallenge.localservice.BusRoutesLocalService;
import busrouteschallenge.localservice.exception.LocalServiceNotReadyException;

@Path("api")
@Immediate
public class BusRouteWebService {
    private static final Logger LOGGER = Logger.getRootLogger();

    private final BusRoutesLocalService localService;

    @Inject
    public BusRouteWebService(BusRoutesLocalService localService) {
	this.localService = localService;
    }

    @GET
    @Path("direct")
    @Produces(MediaType.APPLICATION_JSON)
    public Response isDirectConnection(@NotNull @QueryParam("dep_sid") Integer departureStationId,
	    @NotNull @QueryParam("arr_sid") Integer arrivalStationId) {
	LOGGER.debug("Looking for connection between " + departureStationId + " and " + arrivalStationId);
	try {
	    boolean isDirectConnection = localService.isDirectConnection(departureStationId, arrivalStationId);
	    BusDirectConnectionBean responseBean = new BusDirectConnectionBean(departureStationId, arrivalStationId,
		    isDirectConnection);
	    LOGGER.debug("Connection between " + departureStationId + " and " + arrivalStationId + ": "
		    + isDirectConnection);
	    return Response.status(Response.Status.OK).entity(responseBean).build();
	} catch (LocalServiceNotReadyException e) {
	    LOGGER.debug("Error finding for connection between " + departureStationId + " and " + arrivalStationId
		    + "; the local service is not ready");
	    return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
	}
    }

}