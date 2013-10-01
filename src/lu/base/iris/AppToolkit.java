package lu.base.iris;

/**
 * AppToolkit description : toolkit containing 
 * application data as global variables.
 * 
 * @author xcapt
 * @author klinst
 */
public class AppToolkit 
{
  /**
   * The container for the services
   */
  private static DefaultServiceContainer _srvcontainer;
  
  /**
   * The container for the modules
   */
  private static DefaultModuleContainer _modcontainer;
  
  /**
   * The Application
   */
  private static IApplication _app;

  /**
	 * Method getServiceContainer: gets the global 
   * service container.
   * 
	 * @return IServiceContainer
	 */
	public static IServiceContainer getServiceContainer() 
  {
    if (_srvcontainer == null) 
    {
      _srvcontainer = new DefaultServiceContainer();
    }
    return _srvcontainer;
  }

  /**
   * Gets the service by name
   * 
   * @param name The name of the service
   * @return IService The service with the name
   */
  public static IService getService(String name) 
  {
    return getServiceContainer().getService(name);
  }

  /**
   * Gets the service by class
   * 
   * @param cl The class of the service
   * @return IService The service instance of the class
   */
  public static IService getService(Class cl) 
  {
    return getServiceContainer().getService(cl.getName());
  }

  /**
   * Method setServiceContainer: replaces or eventually sets 
   * the global service container.
   * 
   * @param container The container to use
   */
  public static void setServiceContainer(DefaultServiceContainer container) 
  {
    _srvcontainer = container;
  }

  /**
   * Method getModuleContainer: gets or creates the global 
   * module container.
   * 
   * @return DefaultModuleContainer
   */
  public static DefaultModuleContainer getModuleContainer() 
  {
    if (_modcontainer == null) 
    {
      _modcontainer = new DefaultModuleContainer();
    }
    
    return _modcontainer;
  }

  /**
   * Gets the module by name.
   * 
   * @param name The name of the module
   * @return IModule The instance of the module
   */
  public static IModule getModule(String name) 
  {
    return getModuleContainer().getModule(name);
  }

  /**
   * Gets the module by class.
   * 
   * @param cl The class of the module
   * @return IModule The module instance of the class
   */
  public static IModule getModule(Class cl) 
  {
    return getModuleContainer().getModule(cl.getName());
  }

  /**
   * Sets the module container to use.
   * 
   * @param container The container
   */
  public static void setModuleContainer(DefaultModuleContainer container) 
  {
    _modcontainer = container;
  }

  /**
   * Method setApp: registers the current application.
   * 
   * @param app The application instance
   */
  public static void setApp(IApplication app)
  {
    _app = app;
  }
    
  /**
   * Method getApp: retrieves the current application 
   * object.
   * 
   * @return IApplication The application object
   */
  public static IApplication getApp()
  {
    return _app;
  }
}