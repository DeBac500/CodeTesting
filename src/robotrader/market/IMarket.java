package robotrader.market;

import java.util.Date;


/**
 * The market interface. Provides pricing information
 * for stocks.
 * 
 * @author bob
 * @author klinst 
 */
public interface IMarket 
{
	/**
	 * Get the closing price for the instrument
	 * 
	 * @param instrument The instrument ticker
	 * @return The closing price
	 */
	float getClose(String instrument);

	/**
	 * Get the date for the pricing information
	 * 
	 * @return The date
	 */
	Date getDate();

	/**
	 * Get the current high price
	 * @param instrument The instrument ticker
	 * @return The high price
	 */
	float getHigh(String instrument);

	/**
	 * Get the current low price
	 * @param instrument The instrument ticker
	 * @return The low price
	 */
	float getLow(String instrument);

	/**
	 * Get the current open price
	 * @param instrument The instrument ticker
	 * @return The open price
	 */
	float getOpen(String instrument);

	/**
	 * Get the current volume
	 * 
	 * @param instrument The instrument ticker
	 * @return The volume
	 */
	float getVolume(String instrument);
  
  /**
   * Get the current adjusted close
   * 
   * @param instrument The instrument ticker
   * @return The adjusted close
   */  
  float getAdjustedClose(String instrument);

	/**
	 * Get the current quote information
	 * @return The current quote object
	 */
	HistoricData current();

	/**
	 * Gets the container for indicators
	 * @return The indicator container
	 */
	IIndicatorContainer getIndicatorContainer();
}