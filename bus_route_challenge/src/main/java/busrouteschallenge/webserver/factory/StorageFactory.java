package busrouteschallenge.webserver.factory;

import org.glassfish.hk2.api.Factory;

import busrouteschallenge.storage.InMemoryStorage;
import busrouteschallenge.storage.Storage;

public class StorageFactory implements Factory<Storage> {

    @Override
    public Storage provide() {
	return new InMemoryStorage();
    }

    @Override
    public void dispose(Storage instance) {

    }

}
