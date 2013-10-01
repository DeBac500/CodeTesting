package robotrader.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import robotrader.engine.model.IAccount;
import robotrader.engine.model.IMarketEngine;
import robotrader.engine.model.ITrader;
import robotrader.engine.model.ITraderContainer;
import robotrader.engine.model.Order;
import robotrader.trader.AbstractTrader;
import robotrader.trader.TraderEvent;

/**
 *  TraderContainer  description: basic implementation of ITraderContainer
 * providing additional functionality to respond to engine events : updates,
 * order processing, trader accounts managing. 
 * <br>
*/
public class TraderContainer implements ITraderContainer 
{
  /**
   * The factory for the accounts objects
   */
	private AccountFactory _actfactory;
  
  /**
   * Initial amounts of cash allocated for each
   * trader
   */
	private float _inicash = 100;
	
  /**
   * The list of traders
   */
	private List _traders = new ArrayList();
  
  /**
   * The account for each trader
   */
	private Map _accounts = new HashMap();
  
  /**
   * The market providing the pricing info
   */
	private IMarketEngine _market;

	/**
	 * TraderContainer constructor.
	 */
	public TraderContainer() 
  {
		_actfactory = new AccountFactory();
		_actfactory.setInitialCash(_inicash);
	}
  
  public TraderContainer(AbstractTrader[] traders)
  {
    this();
    for (int i = 0; i < traders.length; i++)
    {
      add(traders[i]);
    }
  }

	/**
	 * @see robotrader.engine.model.ITraderContainer#getTraders()
	 */
	public List getTraders() 
  {
		return _traders;
	}
	/**
	 * Method clear: clears the list of held accounts and traders.
	 */
	public void clear() 
  {
		_traders.clear();
		_accounts.clear();
	}

	/**
	 * Method add: adds a new trader to the trader container, and cretes the
	 * associated account.
	 * @param trader trader to register
	 */
	void add(ITrader trader) 
  {
		IAccount account = _actfactory.getInstance();
		_traders.add(trader);
		trader.setAccount(account);
		_accounts.put(trader, account);
	}

	/**
	 * Method updateTraders: calls update on all registered traders.
	 */
	public void updateTraders() 
  {
		for (Iterator it = _traders.iterator(); it.hasNext();) {
			((AbstractTrader) it.next()).update();
		}
	}

	/**
	 * Method processOrders: process pending orders for each trader.
	 */
	public void processOrders() 
  {
		for (Iterator it = _traders.iterator(); it.hasNext();) 
    {
			AbstractTrader trader = (AbstractTrader) it.next();
			if (!trader.getPendingOrders().isEmpty()) 
      {
				SimpleAccount account = (SimpleAccount) _accounts.get(trader);
        
				for (Iterator itord = trader.getPendingOrders().iterator();
					itord.hasNext();
					) 
        {
					Order order = (Order) itord.next();
					processOrder(trader, account, order);
					itord.remove();
				}
			}
		}
	}

	/**
	 * Method processOrder: process the order to make the transaction. Add
	 * events to the trader.getTickEvents() according to the result of the
	 * transaction: ORDER_REJECT or ORDER_EXEC.
	 * @param trader
	 * @param account 
	 * @param order order to process
	 */
	private void processOrder(
		AbstractTrader trader,
		SimpleAccount account,
		Order order) {
		double result = 0;
		// make transaction
		if (order.isQuantityOrder()) {
			result =
				account.makeQuantityTransaction(
					order.getInstrument(),
					order.getValue());
		} else {
			result =
				account.makeAmountTransaction(
					order.getInstrument(),
					order.getValue());
		}
		// make trader event
		trader.getTickEvents().clear();
		if (result == 0) {
			trader.getTickEvents().add(
				new TraderEvent(TraderEvent.ORDER_REJECT));
		} else {
			trader.getTickEvents().add(new TraderEvent(TraderEvent.ORDER_EXEC));
		}
	}

	/**
	 * @see robotrader.engine.model.ITraderContainer#init()
	 */
	public void init() {
		for (Iterator it = _traders.iterator(); it.hasNext();) {
			AbstractTrader trader = (AbstractTrader) it.next();
			SimpleAccount account = (SimpleAccount) _accounts.get(trader);
			trader.setMarket(_market);
			account.setMarket(_market);
			account.init();
			trader.init();
		}
	}

	/**
	 * Method setMarket: sets the market to be used for prices and order
	 * processing.
	 * @param market
	 */
	public void setMarket(IMarketEngine market) {
		_market = market;
	}
}