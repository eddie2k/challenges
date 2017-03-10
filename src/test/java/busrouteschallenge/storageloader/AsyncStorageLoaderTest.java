package busrouteschallenge.storageloader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AsyncStorageLoaderTest {

    private static final String ANY_STRING = "ANY_STRING";
    private static final Function<Void, Void> ANY_VOID_TO_VOID_FUNCTION = v -> null;
    private static final Function<Throwable, Void> ANY_THROWABLE_TO_VOID_FUNCTION = v -> null;
    private static final Path ANY_PATH = Paths.get(ANY_STRING);

    @Mock
    private StorageLoader syncLoader;

    private AsyncStorageLoader sut;

    @Before
    public void setUp() {
	sut = new AsyncStorageLoader(syncLoader);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenStorageLoaderIsNull() {
	// when
	new AsyncStorageLoader(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenPathIsNull() {
	// when
	sut.load(null, ANY_VOID_TO_VOID_FUNCTION, ANY_THROWABLE_TO_VOID_FUNCTION);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenOnSuccessIsNull() {
	// when
	sut.load(ANY_PATH, null, ANY_THROWABLE_TO_VOID_FUNCTION);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenOnFaliureNull() {
	// when
	sut.load(ANY_PATH, ANY_VOID_TO_VOID_FUNCTION, null);
    }
}
