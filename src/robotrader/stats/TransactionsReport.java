package robotrader.stats;

import java.util.Iterator;
import java.util.List;

import robotrader.engine.model.Transaction;
import robotrader.market.IMarket;
import robotrader.trader.AbstractTrader;

/**
 * The transaction report for the traders. All
 * transactions of all traders are examined
 * in terms of the number of wins and losses
 * made as well as the average win/ loss per
 * transaction.
 * 
 * @author bob
 * @author klinst
 */
public class TransactionsReport 
{
	/**
	 * The list of traders
	 */
	private List _traders;
	
	/**
	 * The market providing the quotes
	 */
	private IMarket _market;

	/**
	 * The number of winning transactions
	 * per trader
	 */
	private int[] _winning;
	
	/**
	 * The number of losing transactions
	 * per trader
	 */
	private int[] _losing;

	/**
	 * The average win per trader
	 */
	private double[] _winavg;
	
	/**
	 * The average loss per 
	 * trader
	 */
	private double[] _losavg;	

	/**
	 * Creates a new transaction report.
	 * 
	 * @param traders The list of traders
	 * @param market The market
	 */
	public TransactionsReport(List traders, IMarket market) 
	{
		_traders = traders;
		_market = market;
	}

	/**
	 * Generates the transaction report for
	 * each trader. Uses the current value
	 * of the trader's position and the value
	 * of the transactions used to purchase
	 * the stock. The difference of both
	 * values is used to update the number
	 * of winning and losing transactions and
	 * the average win/ loss.
	 * 
	 */
	public void generate() 
	{
		int l = _traders.size();
		_winning = new int[l];
		_losing = new int[l];
		_winavg = new double[l];
		_losavg = new double[l];

		for (int i = 0; i < l; i++) 
		{
			double position = 0;
			double amount = 0;
			Transaction t = null;
			List trans = ((AbstractTrader) _traders.get(i)).getTransactions();

			for (Iterator it = trans.iterator(); it.hasNext();) 
			{
				t = (Transaction) it.next();

				double newpos = position + t.getQuantity();
				
				// if stock is sold
				if (t.getAmount() < 0) 
				{
					// if no positions left over (i.e.
					// all stock is sold), update
					// the winning days/ losing days
					// and winning avg/ losing avg
					if (newpos < 0.0001) 
					{
						double diff = amount + t.getAmount();

						if (diff < 0) 
						{
							_winning[i]++;
							_winavg[i] -= diff;
						} 
						else 
						{
							_losing[i]++;
							_losavg[i] += diff;
						}
					}
				}
				
				amount += t.getAmount();
				position += t.getQuantity();
			}

			// if the trader has some positions
			// in the stock, and the current
			// value is larger than the amount
			// spent to purchase the stock
			// positions, then increase the
			// winning days and the winning averages,
			// otherwise increase losing days
			// and averages
			if (position > 0) 
			{
				double price = _market.getClose(t.getInstrument());
				double diff = amount - position * price;
				
				if (diff < 0) 
				{
					_winning[i]++;
					_winavg[i] -= diff;
				} 
				else if(diff > 0)
				{
					_losing[i]++;
					_losavg[i] += diff;
				}
			}

			if (_winning[i] > 0) 
			{
				_winavg[i] = _winavg[i] / _winning[i];
			}
			
			if (_losing[i] > 0) 
			{
				_losavg[i] = _losavg[i] / _losing[i];
			}
		}
	}

	/**
	 * Gets the number of losing transactions
	 * for the trader.
	 * 
	 * @param traderno The number of the trader within the list
	 * @return the number of losing transactions
	 * for the trader
	 */
	public int getLosingCount(int traderno) 
	{
		return _losing[traderno];
	}
	/**
	 * Gets the number of winning transactions
	 * for the trader.
	 * 
	 * @param traderno The number of the trader within the list
	 * @return the number of winning transactions
	 * for the trader
	 */	
	public int getWinningCount(int traderno) 
	{
		return _winning[traderno];
	}
	
	/**
	 * Gets the average loss per transaction
	 * for the trader.
	 * 
	 * @param traderno The number of the trader within the list
	 * @return the average loss per transaction
	 * for the trader
	 */
	public double getLosingAvg(int traderno) 
	{
		return _losavg[traderno];
	}

	/**
	 * Gets the average win per transaction
	 * for the trader.
	 * 
	 * @param traderno The number of the trader within the list
	 * @return the average win per transaction
	 * for the trader
	 */
	public double getWinningAvg(int traderno) 
	{
		return _winavg[traderno];
	}
}
