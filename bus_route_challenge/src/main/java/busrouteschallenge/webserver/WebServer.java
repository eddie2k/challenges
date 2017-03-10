package busrouteschallenge.webserver;

import java.net.URI;
import java.nio.file.Path;

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class WebServer {

    public void start(Path path, URI baseUri) {
	ResourceConfig rc = new BusRoutesWebApp(path);
	GrizzlyHttpServerFactory.createHttpServer(baseUri, rc);
    }
}
