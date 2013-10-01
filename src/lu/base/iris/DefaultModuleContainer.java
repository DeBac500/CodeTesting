package lu.base.iris;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * default implementation of the IModuleContainer, using a simple Map to store
 * IModule through their names and a List to store the registration order used
 * to init the contained IModule objects in a defined order.
 */
public class DefaultModuleContainer implements IModuleContainer {
	/** trace object */
	private static Logger trace =
		Logger.getLogger(AbstractApplication.class.getName());

	private Hashtable _modulesmap = new Hashtable();
	private ArrayList _modules = new ArrayList();

	public DefaultModuleContainer() {
		AppToolkit.setModuleContainer(this);
	}

	public void addModule(IModule module) {
		_modules.add(module);
		_modulesmap.put(module.getName(), module);
	}

	public IModule getModule(String name) {
		return (IModule) _modulesmap.get(name);
	}

	public boolean init() {
		boolean result = true;

		for (Iterator it = _modules.iterator(); it.hasNext();) {
			result &= ((IModule) it.next()).init();
		}

		return result;
	}
	public void cleanup() {
		for (Iterator it = _modules.iterator(); it.hasNext();) {
			IModule module = (IModule) it.next();
			try {
				module.cleanup();
			} catch (Throwable e) {
				trace.error("cleanup error", e);
			}
		}
		_modulesmap.clear();
	}

	public Iterator iterator() {
		return _modules.iterator();
	}
}