package robotrader.gui.traders;

import javax.swing.AbstractAction;

import lu.base.iris.AppToolkit;
import lu.base.iris.gui.BaseGUIModule;
import lu.base.iris.gui.DefaultViewComponent;
import lu.base.iris.services.TranslatedAction;

/**
 * This class represents a gui module for traders
 * that configures the traders panel as well as the
 * traders file panel
 * 
 * @see robotrader.gui.TradersPane
 * @see robotrader.gui.TradersFilePanel
 * 
 * @author xcapt
 * @author klinst
 */
public class TradersModule extends BaseGUIModule 
{
  /**
   * The array of actions containing an instance
   * of the configuration action and the
   * traders file action
   */
	private AbstractAction[] _actions;
  
	/**
   * creates a new traders module.
	 */
	public TradersModule() 
  {
		super("TradersModule");
	}

  /**
   * Gets the actions
   * @return The array of two actions 
   * provided by this module
   */
	public AbstractAction[] getActions() 
  {
		if (_actions == null) 
    {
			_actions = new AbstractAction[] { 
				new ConfigurationAction(),
				new TradersFileAction()};
		}

		return _actions;
	}

  /**
   * Configures the trader panel.
   * 
   * @author xcapt
   * @author klinst
   */
	class ConfigurationAction extends TranslatedAction 
  {
		/**
     * Serial ID
     */
    private static final long serialVersionUID = 2760392988228421689L;

    /**
     * Construct a new Configuration Action.
     */
    ConfigurationAction() 
    {
			super(getRef() + ".configuration");
		}

    /**
     * Executes the action. Gets the traders service
     * from the application toolkit and fills the
     * traders panel with the traders from the service.
     * The Window Manager creates a new window with
     * the trader panel.
     */
		public void execute() 
    {
			TradersService traderssrv =
				(TradersService) AppToolkit.getService(TradersService.class);
			TradersPane panel = new TradersPane(traderssrv.getTradersFile());
			panel.setTraderList(traderssrv.getAll());

			DefaultViewComponent c =
				new DefaultViewComponent(panel, "TRADERS_CONFIGURATION");
			getWindowManager().makeWindow(c).setVisible(true);
		};
		
    public boolean isEnabled() 
    {
			return true;
		}
	}

	/**
   * The Action for configuring the traders file
   * panel.
   * 
   * @author xcapt
   * @author klinst
	 */
	class TradersFileAction extends TranslatedAction 
  {
		/**
     * Serial ID 
     */
    private static final long serialVersionUID = 3039985654275039342L;
    
    /**
     * Constructs a new action. 
     */
    TradersFileAction() 
    {
				super(getRef() + ".file");
		}

    /**
     * Gets the traders service from the application
     * toolkit and creates a new traders file panel
     * using the quote file from the service. 
     */
		public void execute() 
    {
      final TradersService traderssrv =
					(TradersService) AppToolkit.getService(TradersService.class);
			final TradersFilePanel panel = new TradersFilePanel();
			final String initialquotefile = traderssrv.getTradersFile();
			panel.setFile(initialquotefile);

			DefaultViewComponent c =
				new DefaultViewComponent(panel, "TRADERS_FILE_CONFIGURE");
      				getWindowManager().makeWindow(c).setVisible(true);
			}
      
			public boolean isEnabled() 
      {
				return true;
			}
		}
}
