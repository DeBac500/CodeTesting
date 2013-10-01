package lu.base.iris;


/**
 * IServiceContainer description : service container interface
 */
public interface IServiceContainer {
    /**
	 * Method addService: adds a service to the service container.
	 * @param service
	 */
	void addService(IService service);
    /**
	 * Method getService: retrieves a service by its name.
	 * @param name service name
	 * @return IService
	 */
	IService getService(String name);
    /**
	 * Method getEventDispatcher: retrieves the event dispatcher of the
	 * container
	 * @return IServiceEventDispatcher event dispatcher
	 */
	IServiceEventDispatcher getEventDispatcher();
    /**
	 * Method init : calls init on all registered services..
	 * @return boolean false if a fatal error occured during initialization
	 */
	boolean init();
    /**
	 * Method cleanup: calls cleanup on all registered services.
	 */
	void cleanup();
}