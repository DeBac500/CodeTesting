package robotrader.engine.model;

import java.util.List;

import robotrader.market.IMarket;

/**
 * Interface for a Trader.<br>
 * When extending this class :
 * The following methods must be implemented:<br>
 * getName, update, and init.<br>
 * 
 * @author xcapt
 * @author klinst
 */
public interface ITrader {
	/**
	 * sets the Account for this trader
	 * 
	 * @param account the account the trader uses for
	 * managing its transactions
	 */
	void setAccount(IAccount account);	
	/**
	 * sets the market the trader uses for executing
	 * its transactions
	 * 
	 * @param market the market
	 */
	void setMarket(IMarketEngine market);
	
	/**
	 * retrieve IMarket object
	 * 
	 * @return IMarket market data access object
	 */
	IMarket getMarket();
	
	/**
	* retrieve account of the trader
	* @return IAccount 
	*/
	IAccount getAccount();

	/**
	 * retrieve available cash on the trader's account
	 * 
	 * @return double cash
	 */
	float getCash();

	/**
	 * retrieve evaluation of trader's account, adding the
	 * cash amount and position valued at current price.
	 * 
	 * @return double evaluation
	 */
	float getEvaluation();

	/**
	 * Name of the trader
	 * 
	 * @return String name
	 */
	String getName();

	/**
	 * retrieve current profit and lost according to
	 * current evaluation (cash+position)
	 * 
	 * @return double pnl
	 */
	float getPnL();

	/**
	 * retrieve the current open position for the given instrument
	 * on the trader's account
	 * 
	 * @param instrument
	 * 
	 * @return open position
	 */
	float getPosition(String instrument);

	/**
	 * retrieve price of one share of instrument
	 * 
	 * @param instrument code used for instrument
	 * 
	 * @return price
	 */
	float getPrice(String instrument);

	/**
	 * retrieve the number of transactions made on the trader's account
	 * 
	 * @return number of transactions
	 */
	int getTransactionCount();
	
	/**
	 * retrieve pending orders list
	 */
	List getPendingOrders();
	
	/**
	 * retrieve event list for last tick
	 */
	List getTickEvents();

	/**
	 * This method allows the trader to make add an order
	 * at current market price, by giving the stock code and
	 * the number of shares (positive means buy, negative means
	 * sell).<br>
	 * NB: quantity must not be an integer<br>
	 * order will be processed on the next market tick as a
	 * market order
	 * 
	 * @param instrument stock to be traded
	 * @param quantity number of shares to trade     
	 */
	void addQuantityOrder(String instrument, float quantity);
	
	/**
	 * This method allows the trader to make add an order
	 * at current market price, by giving the stock code and
	 * the cash amount (positive means buy, negative means
	 * sell).<br>
	 * NB: cash amount must not be an integer<br>
	 * order will be processed on the next market tick as a
	 * market order
	 * When the order is processed quantity attributed will
	 * be automatically calculated.
	 * 
	 * @param instrument stock to be traded
	 * @param amount to trade     
	 */
	void addAmountOrder(String instrument, float amount);

	/**
	 * This method is called by the core engine each time the
	 * market is updated.
	 * Implement this method to react to price changes and update
	 * your position.
	 */
	void update();

	/**
	 * retrieves list of transactions 
	 *
	 * @return list of IAccount.Transaction objects
	 */
	List getTransactions();
	
	/**
	 * used to set additional properties to configure a
	 * trader object
	 */
	void setProperty(String key, String value);

  /**
   * 
   * @param key
   * @return
   */
  String getProperty(String key);

  
	/**
	 * retrieves the property meta data : keys allowed,
	 * value types, description
	 */
	PropertyMetaData[] getPropertyMetaData();

	/**
	 * called after construction and properties loading
	 */
	void init();
	
	/**
	 * retrieve last transaction
	 * @return the last transaction
	 */
	 Transaction getLastTransaction();
}