package robotrader.market;

import java.util.Date;

/**
 * Historic Data represents the pricing information
 * of a stock (instrument) for a single trading
 * day.
 * 
 * @author bob
 * @author klinst 
 */
public class HistoricData 
{
	/**
	 * The date of the pricing
	 */
	private Date _date;
	
	/**
	 * The instrument (stock)
	 * @TODO this is never used
	 */
	private String _instrument;
	
	/**
	 * The closing value of the instrument
	 */
	private float _close;
	
	/**
	 * The daily high value of the instrument
	 */
	private float _high;

	/**
	 * The daily low value of the instrument
	 */
	private float _low;
	
	/**
	 * The open value of the instrument
	 */
	private float _open;

	/**
	 * The volume traded of this stock
	 */
	private float _volume;
  
  private float _adjusted_close;

	/**
	 * Creates a new HistoricData object.
	 * 
	 * @param inst the instrument (stock)
	 * @param date the date of the pricing information
	 * @param open the open price of the instrument
	 * @param high the highest price of the instrument
	 * @param low the lowest price of the instrument
	 * @param close the closing price of the instrument
	 * @param volume the volume of trade
	 */
	public HistoricData(
		String inst,
		Date date,
		float open,
		float high,
		float low,
		float close,
		float volume,
    float adjusted_close) 
	{
		_instrument = inst;
		_date = date;
		_open = open;
		_high = high;
		_low = low;
		_close = close;
		_volume = volume;
    _adjusted_close = adjusted_close;
	}

	/**
	 * get closing price
	 * 
	 * @return closing price
	 */
	public float getClose() 
	{
		return _close;
	}
  
  /**
   * sets the closing price
   * 
   * @param close
   */
  public void setClose(float close)
  {
    _close = close;
  }

	/**
	 * get the date for the pricing information
	 * 
	 * @return date
	 */
	public Date getDate() 
	{
		return _date;
	}

	/**
	 * gets the high value of the day
	 * 
	 * @return high value
	 */
	public float getHigh() 
	{
		return _high;
	}

  /**
   * Sets the high price
   * 
   * @param high
   */
  public void setHigh(float high) 
  {
    _high = high;
  }

	/**
	 * gets the instrument (stock)
	 * 
	 * @return instrument
	 */
	public String getInstrument() 
	{
		return _instrument;
	}

	/**
	 * gets the low value of the day
	 * 
	 * @return low
	 */
	public float getLow() 
	{
		return _low;
	}

  /**
   * Sets the low price
   * 
   * @param low
   */
  public void setLow(float low) 
  {
    _low = low;
  }

	/**
	 * get the open value of the day
	 * 
	 * @return open value
	 */
	public float getOpen() 
	{
		return _open;
	}

  /**
   * Sets the open price
   * 
   * @param open
   */
  public void setOpen(float open) 
  {
    _open = open;
  }

	/**
	 * gets the fraction of the
	 * difference of the closing
	 * price and the reference value.
	 * 
	 * @param refvalue the reference value
	 * 
	 * @return the variation
	 */
	public float getVariation(float refvalue) 
	{
		return (_close - refvalue) / refvalue;
	}

	/**
	 * gets the volume of trade for the day
	 * 
	 * @return volume
	 */
	public float getVolume() 
	{
		return _volume;
	}

  /**
   * gets the adjusted close of trade for the day
   * 
   * @return _adjusted_close
   */
  public float getAdjustedClose()
  {
    return _adjusted_close;
  }
  
	/**
	 * gets the day and the closing
	 * price as a string.
	 * 
	 * @return string
	 */
	public String toString() 
	{
		return _date + " " + _close;
	}
}