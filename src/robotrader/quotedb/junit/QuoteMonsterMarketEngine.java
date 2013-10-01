package robotrader.quotedb.junit;

import java.util.Date;

import org.apache.log4j.Logger;

import robotrader.engine.model.IMarketEngine;
import robotrader.market.DefaultIndicatorContainer;
import robotrader.market.HistoricData;
import robotrader.market.IIndicatorContainer;
import robotrader.quotedb.InstrumentQuoteFile;

/**
 * A market engine that reads the quotes
 * from a file.
 * 
 * @author bob 
 * @author klinst
 */
public class QuoteMonsterMarketEngine
	extends InstrumentQuoteFile
	implements IMarketEngine {

	/** trace object */
	private static Logger trace =
		Logger.getLogger(QuoteMonsterMarketEngine.class);
	
	/**
	 * The current quote object
	 */
	private HistoricData _current = null;
	
	/**
	 * The pointer to the current quote object
	 */
	private int _index = 0;

	/**
	 * The indicator container
	 * (what is it used for?)
	 */
	private IIndicatorContainer _indicatorcontainer = null;

	/**
	 * Gets the container for indicators.
	 * 
	 * @return The indicator container with the indicators
	 * @see robotrader.market.IMarket#getIndicatorContainer()
	*/
	public IIndicatorContainer getIndicatorContainer() {
		if (_indicatorcontainer == null) {
			_indicatorcontainer = new DefaultIndicatorContainer();
		}
		return _indicatorcontainer;
	}

	/**
	 * Creates a new QuoteMonsterReader object.
	 * 
	 * @param name The filename containing the quotes
	 */
	public QuoteMonsterMarketEngine(String filename) 
	{
		super(filename, false);
		init();
	}

	/**
	 * Get ths closing price.
	 * 
	 * @param inst The instrument (currently not used)
	 * @return The current closing price
	 */
	public float getClose(String inst) 
	{
		return current().getClose();
	}

	/**
	 * Gets the current quote date
	 * 
	 * @return The date
	 */
	public Date getDate() 
	{
		return current().getDate();
	}

	/**
	 * Gets the current quote high
	 * 
	 * @param inst The instrument (currently not used)
	 * @return The high price
	 */
	public float getHigh(String inst) 
	{
		return current().getHigh();
	}

	/**
	 * Get the current low price.
	 * 
	 * @param inst The instrument (not used)
	 * @return The low price
	 */
	public float getLow(String inst) 
	{
		return current().getLow();
	}

	/**
	 * Get the current open price
	 * 
	 * @param inst The instrument (currently not used)
	 * @return The open price
	 */
	public float getOpen(String inst) 
	{
		return current().getOpen();
	}

	/**
	 * Get the current volume of trade.
	 * 
	 * @param inst The instrument (not used)
	 * @return The volume
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
	 * Gets the current quote
	 * 
	 * @return The quote 
	 */
	public HistoricData current() 
	{
		if (_current == null) 
		{
			_current = (HistoricData) _data.get(_index);
		}

		return _current;
	}

	/**
	 * Are there more quotes available
	 * 
	 * @return Yes or no
	 */
	public boolean hasNext() 
	{
		return (_index < _data.size() - 1);
	}

	/**
	 * increments the index of the
	 * current quote object.
	 */
	public void next() 
	{
		if (hasNext())
		{
			_index++;
			_current = null;
		}
		else
		{
			trace.error("next() called but no more quotes are available!");
		}
	}

	/**
	 * removes all indicators. resets the
	 * pointer to the current quote.
	 */
	public void init() 
	{
		_index = 0;
		_current = null;
		getIndicatorContainer().clear();
	}
}