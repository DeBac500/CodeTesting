package lu.base.iris;


/**
 * IService description : service interface, encapsulates a 
 * functionality and model objects. Also includes event 
 * posting methods.
 * 
 * @author xcapt
 * @author klinst
 */
public interface IService 
{
  /**
	 * Method init: initializes the service.
	 * @return boolean false if a fatal error occured during the service
	 * initialization
	 */
	boolean init() throws Exception;
	
  /**
	 * Method cleanup: free all resources used by the service.
	 */
	void cleanup() throws Exception;
  
  /**
	 * Method getName: returns the service name.
	 * @return String service name
	 */
	String getName();
  
  /**
	 * Method setContainer: sets the service holding the service container.
	 * @param container
	 */
	void setContainer(IServiceContainer container);
  
  /**
	 * Method addListener: registers a new listener to the service.
	 * @param listener
	 */
	void addListener(IServiceListener listener);
  
  /**
	 * Method getEventDispatcher: retrieves the event dispatcher used by the
	 * service.
	 * @return IServiceEventDispatcher dispatcher
	 */
	IServiceEventDispatcher getEventDispatcher();
  
  /**
	 * Method post: post an event to all registered service listeners.
	 * @param evt event
	 */
	void post(ServiceEvent evt);
}