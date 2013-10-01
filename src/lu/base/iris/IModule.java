package lu.base.iris;

import java.util.Collection;

/**
 * IModule description : module interface, encapsulates many services and user
 * interaction capabilities
 */
public interface IModule {
	/**
	 * Method init.
	 * @return boolean
	 */
	public boolean init();
	/**
	* Method cleanup.
    * @return boolean
	*/
	public void cleanup();

	/**
	 * Method getName: gets the module unique name.
	 * @return String module name
	 */
	public String getName();
	/**
	 * Method getServices: retrieves the list of services included in the
	 * objects.
	 * @return Collection collection of IService objects
	 */
	public Collection getServices();
}