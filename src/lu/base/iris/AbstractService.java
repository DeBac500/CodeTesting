package lu.base.iris;


/**
 * AbstractService description : base class for 
 * service implementation
 * 
 * @author xcapt
 * @author klinst
 */
public abstract class AbstractService implements IService 
{
  /**
   * The service name
   */
  private String _servicename;
  
  /**
   * The parent service container
   */
  private IServiceContainer _parent;
  
  /**
   * Constructs the Service. The name of the
   * service is set as its class name.
   */
  public AbstractService() 
  {
    _servicename = getClass().getName();
  }
  
  public AbstractService(String serviceName){
	  _servicename = serviceName;
  }


    /**
     * Gets the parent service container.
     * 
     * @return The container
     */
    public IServiceContainer getContainer() 
    {
        return _parent;
    }


    /**
     * Gets the service by name
     * 
     * @param name The service name
     * @return The service instance
     */
    public IService getService(String name) 
    {
        return getContainer().getService(name);
    }


    /**
     * Gets a particular service by class name
     * 
     * @param clas The class of the service
     * @return The service instance
     */
    public IService getService(Class clas) 
    {
        return getContainer().getService(clas.getName());
    }

    /**
     * Sets the parent service container
     * @param parent The parent service container
     */
    public void setContainer(IServiceContainer parent) 
    {
        _parent = parent;
    }

    /**
     * Gets the name of this service.
     * @return Its name
     */
    public String getName() 
    {
        return _servicename;
    }

    /**
     * Gets the event dispatcher from the
     * parent service container.
     * 
     * @return The dispatcher
     */
    public IServiceEventDispatcher getEventDispatcher() 
    {
        return getContainer().getEventDispatcher();
    }

    /**
     * Adds a listener to the event dispatcher
     * of the parent service container.
     * 
     * @param listener The service listener
     */
    public void addListener(IServiceListener listener) 
    {
        getEventDispatcher().addListener(this, listener);
    }


    /**
     * Posts an event to the parent container's
     * event dispatcher.
     * 
     * @param evt The event to be dispatched
     */
    public void post(ServiceEvent evt) 
    {
        getEventDispatcher().post(evt);
    }


    /**
     * Posts a Service Event of the given type.
     * 
     * @param type The type of service event
     */
    public void post(int type) 
    {
        post(new ServiceEvent(this, type));
    }
   	
}