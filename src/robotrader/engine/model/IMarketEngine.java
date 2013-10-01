package robotrader.engine.model;

import robotrader.market.IMarket;
import java.util.Collection;

/**
 *  IMarketEngine  description: interface extending IMarket for the
 * implementation of a market provider engine.  It provides sequential
 * (chronological) access to historic daily data.
 * <br>
*/
public interface IMarketEngine extends IMarket {
	/**
	 * Method hasNext: checks if the processing of next() is possible.
	 * @return boolean true if next() can be called
	 */
	boolean hasNext();

	/**
	 * Method next: process to the next available day.
	 */
	void next();

	/**
	 * Method init: rewinds the day counter to go bakc to the first day.
	 */
	void init();

	/**
	 * Method getDataSize: returns the number of daily data available.
	 * @return int number days stored in the engine, -1 when unknown
	 */
	int getDataSize();

	/**
	 * Method getAvailableInstruments: list of available instruments.
	 * @return Collection collection of Stringobjects holding the stock code of
	 * available objects.
	 */
	Collection getAvailableInstruments();
}