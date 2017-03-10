package busrouteschallenge.webservice;

import static com.github.fge.jackson.JsonLoader.fromResource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.io.IOException;
import java.io.StringReader;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JsonNodeReader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import busrouteschallenge.localservice.BusRoutesLocalService;
import busrouteschallenge.localservice.exception.LocalServiceNotReadyException;

@RunWith(MockitoJUnitRunner.class)
public class BusRouteWebServiceTest {

    private static final int ANY_INT = 123;

    private static final int ANOTHER_INT = 456;

    @Mock
    private BusRoutesLocalService localService;

    private BusRouteWebService sut;

    @Before
    public void setUp() {
	sut = new BusRouteWebService(localService);
    }

    @Test
    public void shouldReturnStatus200_whenRequestIsSuccessful() {
	// when
	Response response = sut.isDirectConnection(ANY_INT, ANY_INT);

	// then
	assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    @Test
    public void shouldReturnStatus503_whenLocalServiceIsNotReady() {
	// given
	doThrow(LocalServiceNotReadyException.class).when(localService).isDirectConnection(anyInt(), anyInt());

	// when
	Response response = sut.isDirectConnection(ANY_INT, ANY_INT);

	// then
	assertThat(response.getStatus()).isEqualTo(Response.Status.SERVICE_UNAVAILABLE.getStatusCode());
    }

    @Test
    public void shouldReturnFalse_whenThereIsNoConnection() {
	// given
	doReturn(false).when(localService).isDirectConnection(anyInt(), anyInt());

	// when
	Response response = sut.isDirectConnection(ANY_INT, ANY_INT);

	// then
	BusDirectConnectionBean responseBean = (BusDirectConnectionBean) response.getEntity();
	assertThat(responseBean.directConnection).isFalse();
    }

    @Test
    public void shouldReturnTrue_whenThereIsNoConnection() {
	// given
	doReturn(true).when(localService).isDirectConnection(anyInt(), anyInt());

	// when
	Response response = sut.isDirectConnection(ANY_INT, ANY_INT);

	// then
	BusDirectConnectionBean responseBean = (BusDirectConnectionBean) response.getEntity();
	assertThat(responseBean.directConnection).isTrue();
    }

    @Test
    public void shouldResponseIncludeDepartureStationId() {
	// given
	boolean anyBoolean = true;
	doReturn(anyBoolean).when(localService).isDirectConnection(anyInt(), anyInt());

	// when
	Response response = sut.isDirectConnection(ANY_INT, ANOTHER_INT);

	// then
	BusDirectConnectionBean responseBean = (BusDirectConnectionBean) response.getEntity();
	assertThat(responseBean.departureStationId).isEqualTo(ANY_INT);
    }

    @Test
    public void shouldResponseIncludeArrivalStationId() {
	// given
	boolean anyBoolean = true;
	doReturn(anyBoolean).when(localService).isDirectConnection(anyInt(), anyInt());

	// when
	Response response = sut.isDirectConnection(ANY_INT, ANOTHER_INT);

	// then
	BusDirectConnectionBean responseBean = (BusDirectConnectionBean) response.getEntity();
	assertThat(responseBean.arrivalStationId).isEqualTo(ANOTHER_INT);
    }

    @Test
    public void shouldResponseObeyJsonSchema() throws IOException, ProcessingException {
	// given
	boolean anyBoolean = true;
	doReturn(anyBoolean).when(localService).isDirectConnection(anyInt(), anyInt());

	// when
	Response response = sut.isDirectConnection(ANY_INT, ANY_INT);

	// then
	BusDirectConnectionBean responseBean = (BusDirectConnectionBean) response.getEntity();
	String responseAsJsonString = new ObjectMapper().writeValueAsString(responseBean);
	JsonNode responseAsJsonNode = new JsonNodeReader().fromReader(new StringReader(responseAsJsonString));

	String jsonSchemaFile = "/BusDirectConnectionSchema.json";
	JsonSchema schema = JsonSchemaFactory.byDefault().getJsonSchema(fromResource(jsonSchemaFile));

	assertThat(schema.validateUnchecked(responseAsJsonNode).isSuccess()).isTrue();
    }
}
