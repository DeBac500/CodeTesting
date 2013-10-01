package lu.base.iris;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.log4j.Logger;
/**
 * default implementation of the IServiceContainer, using a simple Map to store
 * IService through their names and a List to store the registration order used
 * to init the contained IService objects in a defined order.
 * It also provides a default ServiceEventDispatcher for IService event handling
 */
public class DefaultServiceContainer implements IServiceContainer {
	/** trace object */
	private static Logger trace =
		Logger.getLogger(AbstractApplication.class.getName());

	private Hashtable _servicesmap = new Hashtable();
	private ArrayList _services = new ArrayList();
	private IServiceEventDispatcher _dispatcher = new ServiceEventDispatcher();

	public DefaultServiceContainer() {
		AppToolkit.setServiceContainer(this);
	}

	public void addService(IService service) {
		_services.add(service);
		_servicesmap.put(service.getName(), service);
		service.setContainer(this);
	}

	public IService getService(String name) {
		return (IService) _servicesmap.get(name);
	}

	public void cleanup() {
		getEventDispatcher().stopDispatch();
		Collections.reverse(_services);

		for (Iterator it = _services.iterator(); it.hasNext();) {
			IService service = (IService) it.next();
			trace.debug("Unloading service " + service.getName() + "...");

			try {
				service.cleanup();
			} catch (Throwable e) {
				trace.error("cleanup error", e);
			}
		}
		_servicesmap.clear();
	}

	public boolean init() {
		boolean result = true;

		getEventDispatcher().startDispatch();

		for (Iterator it = _services.iterator(); it.hasNext();) {
			IService service = (IService) it.next();
			trace.info("Initializing service " + service.getName() + "...");
			try {
				if (!service.init()) {
					trace.error(
						"Failed to initialize service :" + service.getName());
					result = false;
				} else {
					trace.info(
						"Initialized service " + service.getName() + ".");
					ServiceEvent evt =
						new ServiceEvent(service, ServiceEvent.EVT_SERVICE_INITIATED);
					getEventDispatcher().post(evt);
				}
			} catch (Exception e) {
				trace.error(
					"Exception initializing service :" + service.getName(),
					e);
			}
		}
		return result;
	}

	public IServiceEventDispatcher getEventDispatcher() {
		return _dispatcher;
	}
}