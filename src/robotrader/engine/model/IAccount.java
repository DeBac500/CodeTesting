package robotrader.engine.model;

import java.util.List;

/**
 *  IAccount  description: interface for the implementation of account object
 * managing cash, stock positions and transactions log.
 * <br>
*/
public interface IAccount {
	/**
	 * Method getCash: returns the available cash balance.
	 * @return double cash balance
	 */
	float getCash();

	/**
	 * Method getEvaluation: returns the current evaluation of the hopldings
	 * (cash+stock position).
	 * @return double account value
	 */
	float getEvaluation();

	/**
	 * Method getPnL: returns the difference of the current evaluation and
	 * theinitial cash balance.
	 * @return double profit and loss
	 */
	float getPnL();

	/**
	 * Method getPosition: returns the number of stocks held for a given
	 * instrument.
	 * @param instrument stock code
	 * @return double holded quantity of the stock 
	 */
	float getPosition(String instrument);

	/**
	 * Method getPrice: returns the current price of the stock on the market.
	 * @param instrument stock code
	 * @return double current market price
	 */
	float getPrice(String instrument);

	/**
	 * Method getTransactionCount: returns the number of executed orders
	 *  (trades) done so far on this account.
	 * @return int number of transactions
	 */
	int getTransactionCount();

	/**
	 * Method getTransactions: returns all executed orders done so far on this
	 * account.
	 * @return List list of Transaction objects
	 */
	List getTransactions();
}