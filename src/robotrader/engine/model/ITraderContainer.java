package robotrader.engine.model;

import java.util.List;

/**
 *  ITraderContainer  description: interface for implementation of a trader
 * container, holding and managing all traders.
 * <br>
*/
public interface ITraderContainer {
	/**
	 * Method getTraders: returns the list of all registered traders.
	 * @return List list of AbstractTrader object
	 */
	List getTraders();
	/**
	 * Method init: initializes the trader container, calls init on all traders.
	 */
	void init();
}