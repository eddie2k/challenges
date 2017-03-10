package busrouteschallenge.webserver;

import java.nio.file.Path;

import javax.inject.Singleton;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import busrouteschallenge.localservice.BusRoutesLocalService;
import busrouteschallenge.storage.Storage;
import busrouteschallenge.storageloader.AsyncStorageLoader;
import busrouteschallenge.storageloader.PlainTextFileStorageLoader;
import busrouteschallenge.storageloader.StorageLoader;
import busrouteschallenge.storageloader.parser.Parser;
import busrouteschallenge.storageloader.validator.Validator;
import busrouteschallenge.webserver.factory.ParserFactory;
import busrouteschallenge.webserver.factory.StorageFactory;
import busrouteschallenge.webserver.factory.ValidatorFactory;
import busrouteschallenge.webservice.BusRouteWebService;

class BusRoutesWebApp extends ResourceConfig {

    public BusRoutesWebApp(Path path) {

	register(com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider.class);
	register(new AbstractBinder() {
	    @Override
	    protected void configure() {
		bind(path).to(Path.class);
		bind(BusRoutesLocalService.class).to(BusRoutesLocalService.class).in(Singleton.class);
		bind(PlainTextFileStorageLoader.class).to(StorageLoader.class).in(Singleton.class);
		bind(AsyncStorageLoader.class).to(AsyncStorageLoader.class).in(Singleton.class);
		bindFactory(StorageFactory.class).to(Storage.class).in(Singleton.class);
		bindFactory(ParserFactory.class).to(Parser.class).in(Singleton.class);
		bindFactory(ValidatorFactory.class).to(Validator.class).in(Singleton.class);
	    }
	});
	register(ImmediateFeature.class);
	register(BusRouteWebService.class);
    }
}
