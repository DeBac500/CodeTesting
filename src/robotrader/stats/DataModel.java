package robotrader.stats;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import robotrader.engine.event.Event;
import robotrader.engine.event.IObserver;
import robotrader.engine.model.IEngineObserver;
import robotrader.engine.model.ITraderContainer;
import robotrader.market.IMarket;
import robotrader.trader.AbstractTrader;

/**
 * A statistic data model containing yearly report
 * for an instrument, the trading statistics
 * for all traders of the instrument in the
 * market.
 * 
 * @author bob
 * @author klinst
 */
public class DataModel implements IEngineObserver 
{
	/**
	 * A Gregorian Calendar instance
	 */
	private static Calendar _calendar = GregorianCalendar.getInstance();

	/**
	 * The size of the model
	 */
	private int _datasize = -1;

	/**
	 * The current date
	 */
	private Date _date;
	
	/**
	 * The start date
	 */
	private Date _startdate;
	
	/**
	 * The end date
	 */
	private Date _enddate;

	/**
	 * The current year
	 */
	private int _year;
	
	/**
	 * The container of traders
	 */
	private ITraderContainer _tcontainer;
	
	/**
	 * The market providing instrument quotes
	 */
	private IMarket _market;
	
	/**
	 * The list of observers of this object
	 */
	private List _observers = new ArrayList();

	/**
	 * The percentage change for an instrument
	 */
	private YearlyReport _report;
	
	/**
	 * The overall report of winning and
	 * losing transactions for a trader
	 */
	private TransactionsReport _treport;

	/**
	 * A map of traders to their trader statistics
	 */
	private HashMap _traderstats = new HashMap();

	/**
	 * Get the current date
	 * @return current date
	 */
	public Date getDate() 
	{
		return _date;
	}
	
	/**
	 * Get the start date
	 * @return start date
	 */
	public Date getStartDate() 
	{
		return _startdate;
	}

	/**
	 * Get the end date
	 * @return end date
	 */
	public Date getEndDate() 
	{
		return _enddate;
	}

	/**
	 * Update the market object.
	 * 
	 * @param The market providing quote information
	 * @see robotrader.engine.model.IEngineObserver#setMarket(robotrader.market.IMarket)
	 */
	public void setMarket(IMarket market) 
	{
		_market = market;
	}

	/**
	 * Sets the trader container. Produces a yearly
	 * report for all traders. Creates a new trader
	 * stats object for each trader.
	 * 
	 * @param container The container of traders
	 * @see robotrader.engine.model.IEngineObserver#setTraderContainer
	 * (ITraderContainer)
	 */
	public void setTraderContainer(ITraderContainer container) 
	{
		_tcontainer = container;
		_report = new YearlyReport(_tcontainer.getTraders());

		for (Iterator it = _tcontainer.getTraders().iterator();
			it.hasNext();
			) 
		{
			_traderstats.put(it.next(), new TraderStats());
		}
	}

	/**
	 * Updates the current year,date and the
	 * yearly report. Calls all its observers
	 * that it has been stopped.
	 * 
	 * @see robotrader.engine.model.IEngineObserver#onStopped()
	 */
	public void onStopped() 
	{
		_date = _market.getDate();
		_calendar.setTime(_date);
		_year = _calendar.get(Calendar.YEAR);
		_report.yearUpdate();

		_enddate = _market.getDate();
		for (Iterator it = _observers.iterator(); it.hasNext();) 
		{
			((IObserver) it.next()).onStopped();
		}
	}

	/**
	 * Resets the date informations. Calls all
	 * its observers that it was started.
	 */
	public void onStarted() 
	{
		_startdate = null;
		_enddate = null;
		_date = null;
		_year = 0;
		for (Iterator it = _observers.iterator(); it.hasNext();) 
		{
			((IObserver) it.next()).onStarted();
		}
	}

	/**
	 * @see robotrader.engine.model.IEngineObserver#onUpdate()
	 */
	public void onUpdate(Event evt) 
	{
		if (_date == null) 
		{
			_startdate = _market.getDate();
		}

		_date = _market.getDate();
		updateTraderStats();

		if (evt.isYearEvent()) 
		{
			_calendar.setTime(_date);
			_year = _calendar.get(Calendar.YEAR);
			_report.yearUpdate();
		}
		
		for (Iterator it = _observers.iterator(); it.hasNext();) 
		{
			((IObserver) it.next()).onUpdate(evt);
		}
	}
	
	/**
	 * Adds a new observer to the list of observers
	 * @param observer The observer to be added
	 */
	public void addObserver(IObserver observer) 
	{
		_observers.add(observer);
	}
	
	/**
	 * Get the market providing the stock pricing.
	 * 
	 * @return The market
	 */
	public IMarket getMarket() 
	{
		return _market;
	}
	
	/**
	 * Get all traders from the trader container.
	 * @return A list of traders 
	 */
	public List getTraders() 
	{
		return _tcontainer.getTraders();
	}

	/**
	 * Gets a string array of trader's names
	 * @return The trader's names
	 */
	public String[] getTitles() 
	{
		//	create traders titls table
		List list = getTraders();
		String[] titles = new String[list.size()];

		for (int i = 0; i < list.size(); i++) 
		{
			titles[i] = ((AbstractTrader) list.get(i)).getName();
		}
		return titles;
	}
	
	/**
	 * Get the current year
	 * @return
	 */
	public int getYear() 
	{
		return _year;
	}

	/**
	 * Get the current year report
	 * @return The report
	 */
	public YearlyReport getYearReport() 
	{
		return _report;
	}
	
	/**
	 * Gets the report of transactions of the
	 * traders in the market
	 * 
	 * @return The transaction report
	 */
	public TransactionsReport getTransactionsReport() 
	{
		if (_treport == null) 
		{
			_treport =
				new TransactionsReport(_tcontainer.getTraders(), _market);
		}
		return _treport;
	}

	/**
	 * Sets the data size
	 * @param The new size
	 */
	public void setDataSize(int size) 
	{
		_datasize = size;
	}

	/**
	 * Gets the data size
	 * @return The size
	 */
	public int getDataSize() 
	{
		return _datasize;
	}

	/**
	 * Updates the trader statistics. Increments the
	 * up and down days if the traders position
	 * evaluation is up or down on the previous day.
	 *
	 */
	private void updateTraderStats() 
	{
		for (Iterator it = _tcontainer.getTraders().iterator();
			it.hasNext();
			) 
		{
			AbstractTrader trader = (AbstractTrader) it.next();
			TraderStats stat = (TraderStats) _traderstats.get(trader);

			float pos = trader.getPosition("");
			float eval = trader.getEvaluation();
			float cash = trader.getCash();
			
			if (stat.getOpenPosition() == pos) 
			{
				if (eval > stat.getLastEval()) 
				{
					stat.incUpPosDays();
				}

				if (eval < stat.getLastEval()) 
				{
					stat.incDownPosDays();
				}
			}
			
			stat.setLastEval(eval);
			stat.setOpenPosition(pos);
			stat.incDays();
			stat.addRisk(1f - cash / eval);
		}
	}

	/**
	 * Gets the trader statistics for the given
	 * trader
	 * 
	 * @param trader The trader
	 * @return The statistics
	 */
	public TraderStats getTraderStats(Object trader) 
	{
		return (TraderStats) _traderstats.get(trader);
	}

	/**
	 * Clear the trader statistics and the
	 * transaction report.
	 */
	public void clear() 
	{
		_traderstats.clear();
		_treport = null;
	}
}
