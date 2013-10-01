package robotrader.gui.traders;

import robotrader.trader.AbstractTrader;

/**
 * ITradersRepository
 */
public interface ITradersRepository {
	
	AbstractTrader[] getAll();

}
