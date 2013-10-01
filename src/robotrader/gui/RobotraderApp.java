package robotrader.gui;


import lu.base.iris.gui.simple.AbstractSimpleApp;
import robotrader.gui.quotedb.QuoteModule;
import robotrader.gui.quotedb.QuoteService;
import robotrader.gui.reports.ReportModule;
import robotrader.gui.reports.ReportService;
import robotrader.gui.traders.TradersModule;
import robotrader.gui.traders.TradersService;

/**
 * The robotrader gui application.  
 * 
 * @author xcapt
 * @author klinst
 */
public class RobotraderApp extends AbstractSimpleApp 
{
  /**
   * Constructs the Robotrader Application. Creates
   * and adds modules and services to the parent
   * containers. 
   */
	RobotraderApp() 
  {
		super("Robotrader", "robotrader");
		// add all services
		services();
		// add all modules
		modules();
	}

  /**
   * start the application
   */
	public void start() 
  {
		show();
		super.start();
	}

  /**
   * @return true
   */
	protected boolean closeAppDlg() 
  {
		return true;
	}
	
  /**
   * Adds the required services to the application
   */
	private void services()
	{
		this.addService(new QuoteService());
		this.addService(new TradersService());
		this.addService(new EngineService());
		this.addService(new ReportService());
	}
	
  /**
   * Adds the required modules to application
   */
	private void modules()
	{
		//this.addModule(new EngineModule());
		this.addModule(new SimpleViewEngineModule());
		this.addModule(new QuoteModule());
		this.addModule(new TradersModule());
		this.addModule(new ReportModule());
		this.addModule(new OptionsModule());
	}
}
