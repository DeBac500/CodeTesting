package lu.base.iris;

/**
 * IServiceEventDispatcher description : service event dispatcher
 */
public interface IServiceEventDispatcher {
    /**
	 * Method post: posts service event and dispatches it to the registered
	 * listeners.
	 * @param evt
	 */
	void post(ServiceEvent evt);
    /**
	 * Method addListener: adds a listener for the specified service.
	 * @param service subject
	 * @param listener observer
	 */
	void addListener(IService service, IServiceListener listener);
    /**
	 * Method startDispatch: starts event dispatching, will dispatch events
	 * that are posted subsequently.
	 */
	void startDispatch();
    /**
	 * Method stopDispatch: stops event dispatching, does not guarantee that
	 * posted events will be buffered.
	 */
	void stopDispatch();
}