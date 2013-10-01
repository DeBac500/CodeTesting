package robotrader.trader;

import java.util.ArrayList;
import java.util.List;

import robotrader.engine.model.IAccount;
import robotrader.engine.model.IMarketEngine;
import robotrader.engine.model.ITrader;
import robotrader.engine.model.Order;
import robotrader.engine.model.PropertyMetaData;
import robotrader.engine.model.Transaction;
import robotrader.market.IMarket;

/**
 * This class provides the basis implementation to implement
 * a Trader.<br>
 * When extending this class :
 * The following methods must be implemented:<br>
 * getName, update, and init.<br>
 * The following methods may be overriden for advanced functionnality:<br>
 * setProperty, getPropertyMetaData
 * 
 * @author xcapt
 * @author klinst
 */
public abstract class AbstractTrader implements ITrader{
	
	/**
	 * The account the trader is using managing its transactions
	 */
	protected IAccount _account;
	
	/**
	 * the market the trader is trading in
	 */
	protected IMarketEngine _market;
	
	/**
	 * a list of orders that are pending
	 */
	private ArrayList _pendingOrders = new ArrayList();
	
	/**
	 * a list of all events for the last tick
	 */
	private ArrayList _tickevents = new ArrayList();

	/**
	 * sets the Account for this trader
	 * 
	 * @param account the account the trader uses for
	 * managing its transactions
	 */
	public final void setAccount(IAccount account) 
  {
		_account = account;
	}
	
	/**
	 * sets the market the trader uses for executing
	 * its transactions
	 * 
	 * @param market the market
	 */
	public final void setMarket(IMarketEngine market) 
  {
		_market = market;
	}
	
	/**
	 * retrieve IMarket object
	 * 
	 * @return IMarket market data access object
	 */
	public final IMarket getMarket() {
		return _market;
	}
	
	/**
	* retrieve account of the trader
	* @return IAccount 
	*/
	public final IAccount getAccount() {
		return _account;
	}

	/**
	 * retrieve available cash on the trader's account
	 * 
	 * @return double cash
	 */
	public final float getCash() {
		return getAccount().getCash();
	}

	/**
	 * retrieve evaluation of trader's account, adding the
	 * cash amount and position valued at current price.
	 * 
	 * @return double evaluation
	 */
	public final float getEvaluation() {
		return _account.getEvaluation();
	}

	/**
	 * Name of the trader
	 * 
	 * @return String name
	 */
	public abstract String getName();

	/**
	 * retrieve current profit and lost according to
	 * current evaluation (cash+position)
	 * 
	 * @return double pnl
	 */
	public final float getPnL() {
		return _account.getPnL();
	}

	/**
	 * retrieve the current open position for the given instrument
	 * on the trader's account
	 * 
	 * @param instrument
	 * 
	 * @return open position
	 */
	public final float getPosition(String instrument) 
  {
		return _account.getPosition(instrument);
	}

	/**
	 * retrieve price of one share of instrument
	 * 
	 * @param instrument code used for instrument
	 * 
	 * @return price
	 */
	public final float getPrice(String instrument) 
  {
		return _account.getPrice(instrument);
	}

	/**
	 * retrieve the number of transactions made on the trader's account
	 * 
	 * @return number of transactions
	 */
	public final int getTransactionCount() {
		return _account.getTransactionCount();
	}
	
	/**
	 * retrieve pending orders list
	 */
	public final List getPendingOrders() {
		return _pendingOrders;
	}
	
	/**
	 * retrieve event list for last tick
	 */
	public final List getTickEvents() {
		return _tickevents;
	}

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
	public final void addQuantityOrder(String instrument, float quantity) 
  {
		_pendingOrders.add(new Order(instrument, true, quantity));
	}
	
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
	public final void addAmountOrder(String instrument, float amount) 
  {
		_pendingOrders.add(new Order(instrument, false, amount));
	}

	/**
	 * This method is called by the core engine each time the
	 * market is updated.
	 * Implement this method to react to price changes and update
	 * your position.
	 */
	public abstract void update();

	/**
	 * retrieves list of transactions 
	 *
	 * @return list of IAccount.Transaction objects
	 */
	public final List getTransactions() {
		return _account.getTransactions();
	}
	
	/**
	 * used to set additional properties to configure a
	 * trader object
	 */
	public void setProperty(String key, String value) 
  {
	}

  /**
   * 
   * @param key
   * @return
   */
  public String getProperty(String key)
  {
    return "0";
  }
  
	/**
	 * retrieves the property meta data : keys allowed,
	 * value types, description
	 */
	public PropertyMetaData[] getPropertyMetaData() 
  {
		return new PropertyMetaData[0];
	}

	/**
	 * called after construction and properties loading
	 */
	public abstract void init();
	
	/**
	 * retrieve last transaction
	 * @return the last transaction
	 */
	public Transaction getLastTransaction() 
  {
		if (getTransactions().size() < 1) 
    {
			return null;
		}
		return (Transaction) getTransactions().get(
			getTransactions().size() - 1);
	}
}