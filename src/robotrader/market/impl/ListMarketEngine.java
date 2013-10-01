package robotrader.market.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Collection;

import robotrader.engine.model.IMarketEngine;
import robotrader.market.DefaultIndicatorContainer;
import robotrader.market.HistoricData;
import robotrader.market.IIndicatorContainer;

/**
 * A Market implementation based on a list of
 * historic stock quote information. 
 * 
 * @author bob 
 * @author klinst
 */
public class ListMarketEngine implements IMarketEngine 
{
	/**
	 * a list of historic data objects
	 */
	private List _list;
	
	/**
	 * the current (latest) historic data
	 */
	private HistoricData _current = null;
	
	/**
	 * the current index in the historic data list
	 */
	private int _index = 0;
	
	/**
	 * a list of instruments that have pricing
	 * information available (1 for now)
	 */
	private ArrayList _available;

	/**
	 * The indicator container which contains
	 * this indicator
	 */
	private IIndicatorContainer _indicatorcontainer = null;

	/**
	 * gets the indicator container which contains
	 * this indicator. If the container does not 
	 * exist, a new default one is generated.
	 * 
	 * @return indicator container
	 * @see robotrader.market.IMarket#getIndicatorContainer()
	 * 
	 */
	public IIndicatorContainer getIndicatorContainer() 
	{
		if (_indicatorcontainer == null) 
		{
			_indicatorcontainer = new DefaultIndicatorContainer();
		}
		return _indicatorcontainer;
	}

	/**
	 * Creates a new ListMarketEngine object.
	 * 
	 * @param list the list of historic data
	 * @param instrument the underlying instrument
	 */
	public ListMarketEngine(List list, String instrument) 
	{
		_list = list;
		_available = new ArrayList();
		_available.add(instrument);
		init();
	}

	/**
	 * Gets the closing price of the instrument
	 * 
	 * @param inst the underlying instrument
	 * @return closing price
	 */
	public float getClose(String inst) 
	{
		return current().getClose();
	}

	/**
	 * Gets the date of the latest pricing
	 * information.
	 * 
	 * @return date
	 */
	public Date getDate() 
	{
		return current().getDate();
	}

	/**
	 * Gets the latest high price for the instrument.	 * 
	 * 
	 * @param inst the instrument 
	 * @return the latest high price
	 */
	public float getHigh(String inst) 
	{
		return current().getHigh();
	}

	/**
	 * Gets the latest low price for the
	 * instrument.
	 * 
	 * @param inst the instrument
	 * @return the latest low price
	 */
	public float getLow(String inst) 
	{
		return current().getLow();
	}

	/**
	 * Gets the latest open price
	 * 
	 * @param inst the instrument
	 * @return the latest open price
	 */
	public float getOpen(String inst) 
	{
		return current().getOpen();
	}

	/**
	 * Gets the latest trading volume. 
	 * 
	 * @param inst the instrument 
	 * @return the latest trading volume
	 */
	public float getVolume(String inst) 
	{
		return current().getVolume();
	}

  /**
   * Gets the latest adjusted close. 
   * 
   * @param inst the instrument 
   * @return the latest adjusted close
   */
  public float getAdjustedClose(String instrument)
  {
    return current().getAdjustedClose();
  }

	/**
	 * Gets the latest stock quote information.
	 * 
	 * @return the quote information
	 */
	public HistoricData current() 
	{
		if (_current == null) 
		{
			_current = (HistoricData) _list.get(_index);
		}

		return _current;
	}

	/**
	 * Are more prices available?
	 * 
	 * @return true or false
	 */
	public boolean hasNext() 
	{
		return (_index < _list.size() - 1);
	}

	/**
	 * Gets the next pricing information.
	 * Should always be preceded by hasNext().
	 * 
	 * @see robotrader.market.impl#hasNext()
	 */
	public void next() 
	{
		if (hasNext())
		{
			_index++;
			_current = null;			
		}
	}

	/**
	 * initialises or resets the object. 
	 */
	public void init() 
	{
		_index = 0;
		_current = null;
		getIndicatorContainer().clear();
	}

	/**
	 * returns the number of prices available.
	 * @return number of pricing information
	 */
	public int getDataSize() 
	{
		return _list.size();
	}

	/**
	 * gets the list of available instruments
	 * @return available instruments
	 */
	public Collection getAvailableInstruments() 
	{
		return _available;
	}
}