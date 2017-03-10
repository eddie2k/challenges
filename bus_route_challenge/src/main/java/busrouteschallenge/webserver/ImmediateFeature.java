package busrouteschallenge.webserver;

import javax.inject.Inject;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;

public class ImmediateFeature implements Feature {

    @Inject
    public ImmediateFeature(ServiceLocator locator) {
	// Required for immediate scope (i.e.: singleton + instantiation on
	// start)
	ServiceLocatorUtilities.enableImmediateScope(locator);
    }

    @Override
    public boolean configure(FeatureContext context) {
	return true;
    }
}
