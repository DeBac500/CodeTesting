package robotrader.gui.quotedb;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import lu.base.iris.AppToolkit;
import lu.base.iris.gui.BaseGUIModule;
import lu.base.iris.gui.DefaultViewComponent;
import lu.base.iris.gui.IViewComponentEvent;
import lu.base.iris.gui.mdi.ViewComponentEvent;
import lu.base.iris.services.TranslatedAction;
import lu.base.iris.services.TranslationService;
import robotrader.quotedb.swing.QuoteConfigurationPanel;
import robotrader.quotedb.swing.QuoteSelectionPanel;

/**
 * The quote module creates and contains the
 * quote download and configuration actions
 * which specify the steps to be taken for
 * initialising, changing, downloading and
 * displaying quote information.
 * 
 * @author xcapt
 * @author klinst
 */
public class QuoteModule extends BaseGUIModule 
{
  /**
   * The array of actions made up of
   * a ConfigurationAction and
   * DownloadAction
   */
	private AbstractAction[] _actions;

  /**
   * Creates the quote module.
	 */
	public QuoteModule() 
  {
		super("QuoteModule");
	}

  /**
   * Gets the array containing the
   * download and configuration actions.
   * 
   * @return An array with the two actions
   */
	public AbstractAction[] getActions() 
  {
		if (_actions == null) 
    {
			_actions =
				new AbstractAction[] {
					new DownloadAction(),
					new ConfigurationAction()};
		}

		return _actions;
	}

  /**
   * An action for configuring quotes.
   * 
   * @author xcapt
   * @author klinst
   */
	public class ConfigurationAction extends TranslatedAction 
  {
		/**
     * The Serial ID 
     */
    private static final long serialVersionUID = -4795567055323585940L;
    
    /**
     * Constructs a new Configuration Action.
     */
    ConfigurationAction() 
    {
			super(getRef() + ".configuration");
		}

    /**
     * Executes the action. Gets the quote service
     * from the application toolkit and creates a
     * new quote configuration panel with the
     * master quote file from the service. If the
     * file name is changed by the user, then the
     * change is made permanent after check after
     * checking that everything is ok.
     */
		public void execute() 
    {
			final QuoteService quotesrv =
				(QuoteService) AppToolkit.getService(QuoteService.class);
			final QuoteConfigurationPanel panel = new QuoteConfigurationPanel();
			final String initialquotefile = quotesrv.getMasterQuoteFile();
			panel.setQuoteFile(initialquotefile);

			DefaultViewComponent c =
				new DefaultViewComponent(panel, "QUOTE_CONFIGURE") 
        {
				public void onEvent(IViewComponentEvent evt) {
					if ((evt.getType() == ViewComponentEvent.HIDE)
						&& (!initialquotefile.equals(panel.getQuoteFile()))) {
						if (JOptionPane
							.showConfirmDialog(
								null,
								TranslationService
									.getInstance()
									.getTranslation(
									"QUOTE_FILE_CHANGE_TITLE"),
								TranslationService
									.getInstance()
									.getTranslation(
									"QUOTE_FILE_CHANGE_CONFIRM"),
								JOptionPane.YES_NO_OPTION)
							!= JOptionPane.YES_OPTION) 
            {
							return;
						}

						quotesrv.setMasterQuoteFile(panel.getQuoteFile());
					}
				}
			};
			getWindowManager().makeWindow(c).setVisible(true);

		}
		
    /**
     * @return true
     */
    public boolean isEnabled() 
    {
			return true;
		}
	}
  
  /**
   * An action for downloading quotes.
   * 
   * @author xcapt
   * @author klinst
   */
	public class DownloadAction extends TranslatedAction 
  {
		/**
     * The serial id. 
     */
    private static final long serialVersionUID = 1454930715468453997L;
    
    /**
     * Constructs a new download action.
     */
    DownloadAction() 
    {
			super(getRef() + ".download");
		}

    /**
     * Gets the quote service from the
     * application toolkit. creates a new quote
     * selection panel with the service and
     * gets the window manager to display the
     * panel.
     */
		public void execute() 
    {
			QuoteService quotesrv =
				(QuoteService) AppToolkit.getService(QuoteService.class);
			QuoteSelectionPanel panel = new QuoteSelectionPanel(quotesrv);

			DefaultViewComponent c =
				new DefaultViewComponent(panel, "QUOTE_DOWNLOAD");
			getWindowManager().makeWindow(c).setVisible(true);
		}
    
    /**
     * @return true
     */
		public boolean isEnabled() {
			return true;
		}
	}
}
