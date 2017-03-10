package busrouteschallenge.utils;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

public class Awaiter {
    private static final Logger LOGGER = Logger.getRootLogger();

    public void waitForSigTerm() {
	AtomicBoolean stop = new AtomicBoolean(false);

	Runtime.getRuntime().addShutdownHook(new Thread(() -> {
	    LOGGER.info("SIGTERM received. Stopping the service...");
	    stop.set(true);
	}));

	try {
	    while (!stop.get()) {
		Thread.sleep(1000);
	    }
	} catch (InterruptedException e) {
	    LOGGER.fatal(e.getMessage());
	    new RuntimeException(e);
	}
    }
}
