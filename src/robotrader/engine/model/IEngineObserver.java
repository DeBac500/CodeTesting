package robotrader.engine.model;

import robotrader.engine.event.IObserver;
import robotrader.market.IMarket;

/**
 * Standard interface for Engine observers (listeners)
 * 
 * @author xcapt
 * @author klinst
 */
public interface IEngineObserver extends IObserver 
{
	/**
	 * clears the observer
	 */
	void clear();

	/**
	 * Set the market 
	 * @param market The market
	 */
	void setMarket(IMarket market);

	/**
	 * Set the container of traders
	 * @param container The container
	 */
	void setTraderContainer(ITraderContainer container);

	/**
	 * Sets the size of the data
	 * @param size
	 */
	void setDataSize(int size);
}