package robotrader.engine;

import java.util.ArrayList;
import java.util.List;

import robotrader.engine.model.IAccount;
import robotrader.engine.model.Transaction;
import robotrader.market.IMarket;

/**
 *  SimpleAccount  description: simple implementation of 
 *  the IAccount interface,
 *  providing basis functionality for cash and stocks 
 *  portfolio accounting.
 * <br>
 */
public class SimpleAccount implements IAccount 
{
	/**
	 * The market providing the price information
	 */
	private IMarket _market;
	
	/**
	 * The current balance of the account
	 */
	private float _balance = 0;
	
	/**
	 * The initial balance of the account
	 */
	private float _initbalance = 0;
	
	/**
	 * The current position in the stock 
	 */
	private float _position = 0;
	
	/**
	 * The number of transactions
	 */
	private int _transactioncount = 0;
	
	/**
	 * The list of transactions
	 */
	private ArrayList _transactions = new ArrayList();

	/**
	 * Method SimpleAccount: creates a new account.
	 * @param balance initial cash balance
	 */
	public SimpleAccount(float balance) 
	{
		_balance = balance;
		_initbalance = balance;
	}

	/**
	 * Gets the balance of the account
	 * 
	 * @see robotrader.engine.model.IAccount#getCash()
	 */
	public float getCash() 
	{
		return _balance;
	}

	/**
	 * Gets the cash and the value of the 
	 * current positions
	 * 
	 * @see robotrader.engine.model.IAccount#getEvaluation()
	 */
	public float getEvaluation() 
	{
		return getCash() + (getPosition("") * getPrice(""));
	}

	/**
	 * Gets the difference of the evaluation
	 * and the initial balance
	 * 
	 * @see robotrader.engine.model.IAccount#getPnL()
	 */
	public float getPnL() 
	{
		return getEvaluation() - _initbalance;
	}

	/**
	 * Gets the position in the stock
	 * 
	 * @see robotrader.engine.model.IAccount#getPosition(String)
	 */
	public float getPosition(String instrument) 
	{
		return _position;
	}

	/**
	 * @see robotrader.engine.model.IAccount#getPrice(String)
	 */
	public float getPrice(String instrument) 
	{
		return _market.getAdjustedClose(instrument);
	}

	/**
	 * Gets the number of transactions on this
	 * account.
	 * 
	 * @see robotrader.engine.model.IAccount#getTransactionCount()
	 */
	public int getTransactionCount() 
	{
		return _transactioncount;
	}

	/**
	* This methods allows the trader to make a transaction
	* at current market price, by giving the stock code and
	* the number of shares (positive means buy, negative means
	* sell).<br>
	* NB: quantity must not be an integer<br>
	* total price of the transaction may be:<br>
	* . positive : sold stocks
	* . negative : bought stocks
	* . zero : could not make transaction (insufficient cash or stocks).
	* 
	* @param instrument stock to be traded
	* @param quantity number of shares to trade
	* 
	* @return total price of the transaction 
	*/
	public double makeQuantityTransaction(String instrument, float quantity) 
	{
		float q = roundQuantity(quantity);
		float price = roundAmount(_market.getAdjustedClose(instrument));
		float value = roundAmount(q * price);

		if (Math.abs(value) < 0.01) 
		{
			return 0;
		} 
		else if (value > 0) 
		{
			if (_balance >= value) 
			{
				_balance -= value;
				_position += q;
				
				_position = roundQuantity(_position);
				_balance = roundAmount(_balance);
				
				_transactions.add(
					new Transaction(_market.getDate(), instrument, q, value));
				_transactioncount++;

				return value;
			} 
			else 
			{
				return 0;
			}
		} 
		else 
		{
			if (_position >= -q) 
			{
				_balance -= value;
				_position += q;
				_transactions.add(
					new Transaction(_market.getDate(), instrument, q, value));
				_transactioncount++;

				_position = roundQuantity(_position);
				_balance = roundAmount(_balance);
				
				return -value;
			} 
			else 
			{
				return 0;
			}
		}
	}

	/**
	 * Method makeAmountTransaction: computes the quantity 
	 * and calls makeQuantityTransaction.
	 * 
	 * @param instrument stock code
	 * @param amount cash amount of the order
	 * @return double value of the order
	 */
	public double makeAmountTransaction(String instrument, float amount) 
	{
		float q = roundQuantity(amount / roundAmount(_market.getAdjustedClose(instrument)));
		return makeQuantityTransaction(instrument, q);
	}

	/**
	 * @see robotrader.engine.model.IAccount#getTransactions()
	 */
	public List getTransactions() 
	{
		return _transactions;
	}

	/**
	 * Method init: reinitializes the cash balance and clears the transactions.
	 */
	public void init() 
	{
		_balance = _initbalance;
		_transactions.clear();
		_transactioncount = 0;
		_position = 0;
	}

	/**
	 * Method setMarket: sets the depending Market providing prices and order
	 * executions.
	 * @param market 
	 */
	public void setMarket(IMarket market) 
	{
		_market = market;
	}
	
	/**
	 * Rounds any quantity to 5 floating points
	 * accuracy.
	 *  
	 * @param quantity The quantity to be rounded
	 * @return The rounded quantity
	 */
	private float roundQuantity(float quantity)
	{
		quantity *= 100000.0f;
		float l = 0.0f;
		
		if (quantity > 0.0)
		{
			l = (float)Math.floor(quantity);
		}
		else
		{
			l = (float)Math.ceil(quantity);
		}
			
		float q = l / 100000.0f;
		if (Math.abs(q) <= 0.0001) q = 0.0f;
		return q;
	}
	
	/**
	 * Rounds the amount to 2 digits accuracy.
	 * 
	 * @param amount The amount to be rounded
	 * @return The rounded amount
	 */
	private float roundAmount(float amount)
	{
		amount *= 1000.0f;
		float l = 0.0f;
		
		if (amount > 0)
		{
			l = (float)Math.floor(amount);
		}
		else
		{
			l = (float)Math.ceil(amount);
		}
		
		float a = l / 1000.0f;
		if (Math.abs(a) <= 0.01) a = 0.0f;
		return a;
	}
}