package lu.base.iris;


import java.util.Iterator;
/**
 * This interface provides a set of methods to manage
 * a set of Module
 */
public interface IModuleContainer
{
	/**
	 * retrieve iterator to iterate through all contained
	 * IModule objects
	 */
	Iterator iterator();
	
	/**
	 * add IModule to the container 
	 */
	void addModule(IModule module);
	/**
	 * get IModule from container given its name, retrieved
	 * with IModule.getName()
	 */
    public IModule getModule(String name);
    /**
     * initialize IModule container
     */
    public boolean init();
}


