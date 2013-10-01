package robotrader.gui;

import javax.swing.AbstractAction;

import lu.base.iris.AppToolkit;
import lu.base.iris.gui.BaseGUIModule;
import lu.base.iris.gui.DefaultViewComponent;
import lu.base.iris.services.TranslatedAction;
import robotrader.gui.quotedb.QuoteService;

/**
 * Engine module supplying the actions for
 * constructing the overview frame.
 * 
 * @author xcapt
 * @author klinst
 */
public class EngineModule extends BaseGUIModule 
{
  /**
   * The view action array
   */
	protected AbstractAction[] _actions;
  
  /**
   * The main frame of the application 
   */
	protected OverviewFrame _mainframe;
  
	/**
   * Constructor.
	 */
	public EngineModule() 
  {
		super("EngineModule");
	}

	/**
   * Gets the View action as a 1D array.
   * @return The abstract action array
	 * @see lu.base.iris.gui.AbstractSwingModule#getActions()
	 */
	public AbstractAction[] getActions() {
		if (_actions == null) {
			_actions =
				new AbstractAction[] {
					new ViewAction()};
		}
		return _actions;
	}

  /**
   * Action for viewing the main frame.
   * 
   * @author klinst
   */
	public class ViewAction extends TranslatedAction 
    {
	   /**
	     * The serial id 
	     */
	    private static final long serialVersionUID = -7378038932039701650L;
	    
	     /**
	     * 
	     * The constructor.
	     */
	    ViewAction() 
	    {
			super(getRef() + ".view");
		}

	    /**
	     * Gets the engine view panel
	     * and displays it
	     */
		public void execute() {
			DefaultViewComponent c =
				new DefaultViewComponent(getMainFrame().getPanel(), "ENGINE_VIEW");
			getWindowManager().makeWindow(c).setVisible(true);
		}
    
	    /**
	     * @return true 
	     */
		public boolean isEnabled() {
			return true;
		}
	}
  
  /**
   * Gets or Creates an Overview frame with
   * the engine service and quote service
   * supplied by the Application toolkit.
   * 
   * @return The main frame
   */
	protected OverviewFrame getMainFrame()
	{
		if(_mainframe==null)
		{
			EngineService enginesrv =
							(EngineService) AppToolkit.getService(EngineService.class);
			QuoteService quotesrv =
							(QuoteService) AppToolkit.getService(QuoteService.class);
			
			_mainframe = new OverviewFrame(enginesrv, quotesrv, enginesrv.getModel());
			_mainframe.init();
	
		}
		return _mainframe;
	}
}
