package robotrader.engine;

import java.io.FileNotFoundException;
import java.io.FileReader;

import robotrader.engine.event.Event;
import robotrader.engine.event.EventGenerator;
import robotrader.engine.model.IEngine;
import robotrader.engine.model.IEngineObserver;
import robotrader.engine.model.IMarketEngine;
import robotrader.engine.model.ITrader;
import robotrader.engine.model.ITraderContainer;
import robotrader.market.IMarket;
import robotrader.trader.AbstractTrader;
import robotrader.trader.TraderTool;
import robotrader.trader.XmlTraderLoader;

/**
 * Core engine: Can be used for synchronous or asynchronous processing of the
 * robotrader engine. It consumes market data, processes orders of the
 * participating trader objects, send an update signal to every order and send
 * an update event to the engine observer.<br>
 * 
 * @author xcapt
 */
public class Engine implements IEngine {
	// engine helper objects
	private EventGenerator _evtgenerator = new EventGenerator();
	private IEngineObserver _observer;
	private IMarketEngine _market;
	private TraderContainer _container;
	// configuration
	private String _instrument;
	private String _quoteref;
	private String _traderspath;
	// status
	private boolean _running = false;

	/**
	 * Creates a new Engine object: with a TraderContainer
	 */
	public Engine() {
		_container = new TraderContainer();
	}

	/**
	 * Method getInstrument.
	 * @return String instrument used for engine
	 */
	public String getInstrument() {
		return _instrument;
	}

	/**
	 * Method getMarket.
	 * @return IMarket current market data feed
	 */
	public IMarket getMarket() {
		return _market;
	}

	/**
	 * Method setObserver: sets an observing object for all engine events
	 * start, updates, stop.
	 * @param obs market observer
	 */
	public void setObserver(IEngineObserver obs) {
		_observer = obs;
	}

	/**
	 * gets the trader container: containing currently used traders
	 * 
	 * @return ITraderContainer trader container
	 */
	public ITraderContainer getTraderContainer() {
		return _container;
	}

	/**
	 * Method getTradersFilePath: retrieves the path to the file for
	 * loading traders.
	 * @return String path to traders.xml file
	 */
	public String getTradersFilePath() {
		return _traderspath;
	}

	/**
	 * Method getQuoteRef: gets the reference to the quote file, this may be
	 * the path of a quote file or when using the quote repository, this is the
	 * instrument code.
	 * @return String quote reference
	 */
	public String getQuoteRef() {
		return _quoteref;
	}

	/**
	 * Method setMarketEngine: sets market feed source.
	 * @param market object containing the market data
	 */
	public void setMarketEngine(IMarketEngine market) {
		_instrument =
			(String) market.getAvailableInstruments().iterator().next();
		_quoteref = _instrument;
		_market = market;
	}

	/**
	 * Method loadTradersFile: loads the Xml file containing trader classes and
	 * parameters to create trader object to be used in the robotrader engine.
	 * @param path traders.xml file 
	 */
	public void loadTradersFile(String path) {
		XmlTraderLoader loader = new XmlTraderLoader();
		FileReader reader = null;

		try {
			reader = new FileReader(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();

			return;
		}

		AbstractTrader[] traders = loader.load(reader);

		if (traders.length <= 0) {
			return;
		}

		_traderspath = path;
		removeTraders();

		for (int i = 0; i < traders.length; i++) {
			register(traders[i]);
		}
	}

	/**
	 * Adds a new trader to the TraderContainer, it also creates a new IAccount
	 * for the trader, this must be use before starting the engine.
	 * 
	 * @param trader
	 */
	public void register(ITrader trader) {
		_container.add(trader);
	}

	/**
	 * remove traders from TraderContainer
	 */
	public void removeTraders() {
		_container.clear();
	}

	/**
	 * Method start: When the engine is not already running and when the
	 * market engine has been set, it starts asynchronously(in a separate
	 * thread) the robotrader engine with a call to the loop() method.
	 */
	public void start() {
		if (!_running) {
			if (_market == null) {
				return;
			}

			started();

			Worker worker = new Worker();
			worker.start();
		}
	}

	/**
	 * Method stop: Used for asynchronous processing after a call to the
	 * start method. It sets the running flag to false, stopping the loopS.
	 */
	public void stop() {
		_running = false;
	}

	/**
	 * Method started: is called automatically when using start() to use the
	 * engine asynchronously, otherwise this method must be called before any
	 * loop() call.
	 */
	public void started() {
		_evtgenerator.init();
		_market.init();
		_container.setMarket(_market);
		_container.init();

		if (_observer != null) {

			_observer.clear();
			_observer.setMarket(_market);
			_observer.setTraderContainer(_container);
			_observer.setDataSize(_market.getDataSize());
			_observer.onStarted();
		}
	}

	/**
	 * Method stopped: calls onStopped for the observer, prints a report to the
	 * System.out
	 */
	private void stopped() {
		if (_observer != null) {
			_observer.onStopped();
		}

		TraderTool.printRank(_container.getTraders().toArray());
	}

	private void updateObserver() {
		if (_observer != null) {
			Event evt = _evtgenerator.getEvent(_market.getDate());
			_observer.onUpdate(evt);
		}
	}
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
	public void loop() {
		_running = true;
		_container.updateTraders();
		updateObserver();

		while (_market.hasNext() && _running) {
			_market.next();
			_container.processOrders();
			_container.updateTraders();
			updateObserver();
		}

		_running = false;
		stopped();
	}
	/**
	 * Worker description: internal class extending Thread for asynchronous
	 * processing of the Engine.loop() method.
	*/
	class Worker extends Thread {

		public void run() {
			loop();
		}
	}
}