/*
 * Created on Aug 13, 2003
 */
package lu.base.iris.gui.simple;

import lu.base.iris.AppToolkit;
import lu.base.iris.gui.AbstractGUIApplication;
import lu.base.iris.gui.UserLayoutService;
import lu.base.iris.services.ConfigurationService;
import lu.base.iris.services.LoggerService;
import lu.base.iris.services.ResourceService;
import lu.base.iris.services.TranslationService;
import lu.base.iris.services.UserPrefService;
//import lu.base.iris.gui.outlook.app.*;

/**
 * DefaultApplication
 */
public abstract class AbstractSimpleApp extends AbstractGUIApplication {
	
	public AbstractSimpleApp(String title, String configfile)
	{
		super();
		setMainView(SimpleMainView.getInstance(title));
		addService(new LoggerService());
		addService(new ConfigurationService(configfile));
		addService(new ResourceService());
		addService(new UserPrefService("user.pref","default"));
		addService(new WindowManagerService());
		addService(new TranslationService("translation"));
		addService(new UserLayoutService());
	}
	/* (non-Javadoc)
	 * @see lu.base.iris.IApplication#start()
	 */
	public void start() {
		// registers the main view to the user layout service for saving 
		// restoring main view size and position on screen
		UserLayoutService layoutserv =
			(UserLayoutService) AppToolkit.getService(UserLayoutService.class);
		layoutserv.manage(getMainView());

	}
	/**
		 * @see ch.eri.iris.IApplication#stop()
		 */
		public void stop() {
			if(closeAppDlg())
			{
				super.stop();
				System.exit(0);
			}
		}
			
	/**
	 * closeAppDlg should be implemented to add handling when the user 
	 * tries to close the application , like showing the confimation dialog 
	 */
	protected abstract boolean closeAppDlg();
}
