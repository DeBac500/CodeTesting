package lu.base.iris;

import java.util.Iterator;

/**
 * A class that implements an application with a ModuleContainer
 * and a DefaultServiceContainer. This provides a convenient base class 
 * from which other IApplication classes can be easily derived. 
 */ 
public abstract class AbstractApplication implements IApplication 
{
  /**
   * The container for all the modules
   */
  protected DefaultModuleContainer _modcontainer;
  
  /**
   * The container for all the services
   */
  protected DefaultServiceContainer _servcontainer;

  /**
	 * AbstractApplication constructor: registers the application as current
	 * application to the AppToolkit object.
	 */
	public AbstractApplication() 
  {
    	// create service and module containers
        _servcontainer = new DefaultServiceContainer();
        _modcontainer = new DefaultModuleContainer();
        // register application to AppToolkit
        AppToolkit.setApp(this);
    }

  /**
	 * @see lu.base.iris.IApplication#stop()
	 */
	public void stop() 
  {
		_modcontainer.cleanup();
    _servcontainer.cleanup();
  }

  /**
	 * Method addModule: adds a module to the module container and adds its
	 * services to the service container.
	 * @param module
	 */
	protected void addModule(IModule module) {
        _modcontainer.addModule(module);

        for (Iterator it = module.getServices().iterator(); it.hasNext();) {
            _servcontainer.addService((IService) it.next());
        }
    }

    /**
	 * Method addService: adds a service to the service container.
	 * @param service
	 */
	protected void addService(IService service) {
        _servcontainer.addService(service);
    }

    /**
     * sequentially starts the services, application and modules of the
     * application
	 * @see lu.base.iris.IApplication#init()
	 */
	public boolean init() {
        return initServices() && initMain() && initModules();
    }

    /**
	 * Method initServices: calls init for all services.
	 * @return boolean
	 */
	protected boolean initServices() 
  {
        return _servcontainer.init();
    }

    /**
	 * Method initMain: initializes the application.
	 * @return boolean
	 */
	protected boolean initMain() 
  {
        return true;
    }

  /**
	 * Method initModules: calls init for all modules.
	 * @return boolean
	 */
	protected boolean initModules() 
  {
        return _modcontainer.init();
  }
}