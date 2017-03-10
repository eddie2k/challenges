package busrouteschallenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

import java.net.URI;
import java.nio.file.Path;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import busrouteschallenge.utils.Awaiter;
import busrouteschallenge.webserver.WebServer;

@RunWith(MockitoJUnitRunner.class)
public class LauncherTest {

    private static final String ANY_STRING = "ANY_STRING";
    private static final String ANOTHER_STRING = "ANOTHER_STRING";

    @Mock
    private WebServer server;

    @Mock
    private Awaiter awaiter;

    @InjectMocks
    private Launcher sut = new Launcher();

    @Before
    public void setUp() {

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIAE_whenRunWithEmptyArgs() {
	// when
	sut.run(new String[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIAE_whenRunMoreThanOneArg() {
	// when
	sut.run(new String[] { ANY_STRING, ANOTHER_STRING });
    }

    @Test
    public void shouldStartServer_whenRun_withOneArgument() {
	// when
	sut.run(new String[] { ANY_STRING });

	// then
	verify(server).start(any(), any());
    }

    @Test
    public void shouldUseGivenFile_whenRun_withOneArgument() {
	// when
	sut.run(new String[] { ANY_STRING });

	// then
	ArgumentCaptor<Path> pathCaptor = ArgumentCaptor.forClass(Path.class);
	verify(server).start(pathCaptor.capture(), any());
	assertThat(pathCaptor.getValue().toString()).isEqualTo(ANY_STRING);
    }

    @Test
    public void shouldServerUseHttp_whenRun_withOneArgument() {
	// when
	sut.run(new String[] { ANY_STRING });

	// then
	ArgumentCaptor<URI> uriCaptor = ArgumentCaptor.forClass(URI.class);
	verify(server).start(any(), uriCaptor.capture());
	assertThat(uriCaptor.getValue().getScheme()).isEqualTo("http");
    }

    @Test
    public void shouldServerUserIp0_0_0_0_whenRun_withOneArgument() {
	// when
	sut.run(new String[] { ANY_STRING });

	// then
	ArgumentCaptor<URI> uriCaptor = ArgumentCaptor.forClass(URI.class);
	verify(server).start(any(), uriCaptor.capture());
	assertThat(uriCaptor.getValue().getHost()).isEqualTo("0.0.0.0");
    }

    @Test
    public void shouldServerUsePort8088_whenRun_withOneArgument() {
	// when
	sut.run(new String[] { ANY_STRING });

	// then
	ArgumentCaptor<URI> uriCaptor = ArgumentCaptor.forClass(URI.class);
	verify(server).start(any(), uriCaptor.capture());
	assertThat(uriCaptor.getValue().getPort()).isEqualTo(8088);
    }

    @Test
    public void shouldWaitForSigTerm_afterServerStarted() {
	// when
	sut.run(new String[] { ANY_STRING });

	// then
	InOrder inOrder = inOrder(server, awaiter);
	inOrder.verify(server).start(any(), any());
	inOrder.verify(awaiter).waitForSigTerm();
    }
}
