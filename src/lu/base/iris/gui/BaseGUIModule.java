package lu.base.iris.gui;

import javax.swing.ImageIcon;

import lu.base.iris.AppToolkit;
import lu.base.iris.services.ResourceService;
import lu.base.iris.services.TranslationService;

/**
 * AbstractClientModule description : base for application 
 * modules with graphical user interface
 * 
 * @author xcapt
 * @author klinst
 */
public abstract class BaseGUIModule extends AbstractSwingModule
{
  /**
   * The manager of the windows
   */
	private IWindowManager _winmgr;
	
	/**
	 * Constructs a Base GUI Module.
   * @param ref the unique model reference
	 */
	public BaseGUIModule(String ref)
	{
		super(ref);
	}
	
	/**
	 * retrieves the module icone form the 
   * application resources.
	 * 
	 */
	public ImageIcon getIcon()
	{
		ResourceService res = (ResourceService)AppToolkit.getService(ResourceService.class);
		try
		{
			return res.getIcon("MENU_ICON_"+getRef());
		}
		catch(java.util.MissingResourceException e)
		{
			return res.getIcon("MENU_ICON_DEFAULT");
		}
	}
	
	/**
	 * sets the translated module name
	 */
	public boolean init()
	{
		setTitle(TranslationService.getInstance().getTranslation(getRef()));
		return true;
	}
	
	/**
	 * retrieves the WindowManager used
   * from the Application Toolkit
	 */
	 protected IWindowManager getWindowManager()
	 {
	 	if(_winmgr==null)
	 	{
	 		//_winmgr = (WindowManagerService)AppToolkit.getService(WindowManagerService.class);
	 		_winmgr = (IWindowManager)AppToolkit.getService("WindowManagerService");
	 	}
	 	return _winmgr;
	 }
}

