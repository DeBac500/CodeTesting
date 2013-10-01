/*
 * Created on Aug 14, 2003
 */
package robotrader.engine.model;

import robotrader.market.IMarket;

/**
 * IEngine
 */
public interface IEngine {
	/**
	 * Method getInstrument.
	 * @return String instrument used for engine
	 */
	public abstract String getInstrument();
	/**
	 * Method getMarket.
	 * @return IMarket current market data feed
	 */
	public abstract IMarket getMarket();
	/**
	 * Method setObserver: sets an observing object for all engine events
	 * start, updates, stop.
	 * @param obs market observer
	 */
	public abstract void setObserver(IEngineObserver obs);
	/**
	 * gets the trader container: containing currently used traders
	 * 
	 * @return ITraderContainer trader container
	 */
	public abstract ITraderContainer getTraderContainer();
	/**
	 * Method getTradersFilePath: retrieves the path to the file for
	 * loading traders.
	 * @return String path to traders.xml file
	 */
	public abstract String getTradersFilePath();
	/**
	 * Method getQuoteRef: gets the reference to the quote file, this may be
	 * the path of a quote file or when using the quote repository, this is the
	 * instrument code.
	 * @return String quote reference
	 */
	public abstract String getQuoteRef();
	/**
	 * Method setMarketEngine: sets market feed source.
	 * @param market object containing the market data
	 */
	public abstract void setMarketEngine(IMarketEngine market);
	/**
	 * Method loadTradersFile: loads the Xml file containing trader classes and
	 * parameters to create trader object to be used in the robotrader engine.
	 * @param path traders.xml file 
	 */
	public abstract void loadTradersFile(String path);
	/**
	 * Adds a new trader to the TraderContainer, it also creates a new IAccount
	 * for the trader, this must be use before starting the engine.
	 * 
	 * @param trader
	 */
	public abstract void register(ITrader trader);
	/**
	 * remove traders from TraderContainer
	 */
	public abstract void removeTraders();
	/**
	 * Method start: When the engine is not already running and when the
	 * market engine has been set, it starts asynchronously(in a separate
	 * thread) the robotrader engine with a call to the loop() method.
	 */
	public abstract void start();
	/**
	 * Method stop: Used for asynchronous processing after a call to the
	 * start method. It sets the running flag to false, stopping the loopS.
	 */
	public abstract void stop();
	/**
	 * Method started: is called automatically when using start() to use the
	 * engine asynchronously, otherwise this method must be called before any
	 * loop() call.
	 */
	public abstract void started();
	/**
	 * Method loop: engine main loop, this method loops on market data as long
	 * as market data is available and as long as the stop() method is not
	 * called. <br>
	 * On every loop:<br>
	 * 1. the next available market data(next day) is read.<br> 2. pending
	 * trader orders are processed<br> 2. trader are updated by a call to
	 * AbstractTrader. update() that let them retrieve the latest market data
	 * and issue orders<br> 3. the engine observer is updated by a call to
	 * IEngineObserver.onUpdate()<br>
	 * This method is called in a separate thread when starting the engine with
	 * the start() method otherwise you may call directly this method for
	 * synchronous processing.
	 */
	public abstract void loop();
}