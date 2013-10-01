package robotrader.gui;

import java.util.MissingResourceException;

import lu.base.iris.AbstractService;
import lu.base.iris.AppToolkit;
import lu.base.iris.services.UserPrefService;
import robotrader.engine.Engine;
import robotrader.engine.event.IObserver;
import robotrader.engine.model.IEngine;
import robotrader.engine.model.IEngineObserver;
import robotrader.engine.model.IMarketEngine;
import robotrader.engine.model.ITrader;
import robotrader.engine.model.ITraderContainer;
import robotrader.gui.quotedb.QuoteService;
import robotrader.gui.traders.TradersService;
import robotrader.market.IMarket;
import robotrader.market.impl.ListMarketEngine;
import robotrader.stats.DataModel;

/**
 * EngineService. Provides the functionality of the
 * engine as a service.
 * 
 * @author xcapt
 * @author klinst
 */
public class EngineService extends AbstractService implements IEngine 
{
  /**
   * The engine providing the services
   * for the engine service
   */
	private Engine _engine;
  
  /**
   * The data model providing statistics
   */
	private DataModel _model;

	/**
	 * @see robotrader.engine.model.IEngine#getInstrument()
	 */
	public String getInstrument() 
  {
		return _engine.getInstrument();
	}

	/** 
	 * @see robotrader.engine.model.IEngine#getMarket()
	 */
	public IMarket getMarket() {
    
		return _engine.getMarket();
	}

	/**
	 * @see robotrader.engine.model.IEngine#setObserver(robotrader.engine.model.IEngineObserver)
	 */
	public void setObserver(IEngineObserver obs) 
  {
		_engine.setObserver(obs);
	}

	/**
	 * @see robotrader.engine.model.IEngine#getTraderContainer()
	 */
	public ITraderContainer getTraderContainer() 
  {
		return _engine.getTraderContainer();
	}

	/**
	 * @see robotrader.engine.model.IEngine#getTradersFilePath()
	 */
	public String getTradersFilePath() 
  {
		return _engine.getTradersFilePath();
	}

	/**
	 * @see robotrader.engine.model.IEngine#getQuoteRef()
	 */
	public String getQuoteRef() 
  {
		return _engine.getQuoteRef();
	}

	/**
	 * @see robotrader.engine.model.IEngine#setMarketEngine(robotrader.engine.model.IMarketEngine)
	 */
	public void setMarketEngine(IMarketEngine market) 
  {
		_engine.setMarketEngine(market);
	}

	/** 
	 * @see robotrader.engine.model.IEngine#loadTradersFile(java.lang.String)
	 */
	public void loadTradersFile(String path) 
  {
		_engine.loadTradersFile(path);
	}

	/**
	 * @see robotrader.engine.model.IEngine#register(robotrader.engine.model.ITrader)
	 */
	public void register(ITrader trader) 
  {
		_engine.register(trader);
	}

	/**
	 * @see robotrader.engine.model.IEngine#removeTraders()
	 */
	public void removeTraders() 
  {
		_engine.removeTraders();
	}

	/**
	 * @see robotrader.engine.model.IEngine#start()
	 */
	public void start() 
  {
		_engine.start();
	}

	/**
	 * @see robotrader.engine.model.IEngine#stop()
	 */
	public void stop() 
  {
		_engine.stop();
	}

	/**
	 * @see robotrader.engine.model.IEngine#started()
	 */
	public void started() 
  {
		_engine.started();
	}

	/**
	 * @see robotrader.engine.model.IEngine#loop()
	 */
	public void loop() 
  {
		_engine.loop();
	}

	/**
   * initialises the engine. Loads the 
   * traders file from xml. Gets the instrument
   * from the user preference service. Loads
   * the quotes fro that instrument into a
   * list market engine.
   * 
	 * @see lu.base.iris.IService#init()
	 */
	public boolean init() 
  {
		_engine = new Engine();
    
		// set traders file from user preferences
		TradersService traderssrv =
			(TradersService) AppToolkit.getService(TradersService.class);
		_engine.loadTradersFile(traderssrv.getTradersFile());

		// set instrument from user preferences
		UserPrefService prefserv =
			(UserPrefService) AppToolkit.getService(UserPrefService.class);
		
    try 
    {
			String instr = prefserv.getStringValue("QUOTE_INSTRUMENT");
			QuoteService quotesrv =
					(QuoteService) AppToolkit.getService(QuoteService.class);

			_engine.setMarketEngine(
				new ListMarketEngine(quotesrv.getQuotes(instr), instr));
		} 
    catch (MissingResourceException e) 
    {
		}
		return true;
	}

	/**
	 * @see lu.base.iris.IService#cleanup()
	 */
	public void cleanup() 
  {
		UserPrefService prefserv =
			(UserPrefService) AppToolkit.getService(UserPrefService.class);
		prefserv.setStringValue("QUOTE_INSTRUMENT", _engine.getInstrument());
	}
	
  /**
   * Gets the data model 
   * @return
   */
	public DataModel getModel()
	{
		if(_model==null)
		{
			_model = new DataModel();
		}
		return _model;
	}
  
  /**
   * Adds an observer to the data model
   * 
   * @param observer The observer
   */
	public void addObserver(IObserver observer)
	{
		getModel().addObserver(observer);
	}
}
