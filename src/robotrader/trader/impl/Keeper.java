package robotrader.trader.impl;

import robotrader.trader.AbstractTrader;

/**
 * Simple Trader which invests in the stock
 * and keeps the stock forever. Follows
 * the trend of the underlying stock.
 * 
 * @author xcapt
 * @author klinst
 */
public class Keeper extends AbstractTrader {
	
	/**
	 * gets the name of the trader
	 * 
	 * @return name
	 */
	public String getName() {
		return "Keeper";
	}

	/**
	 * dummy method
	 */
	public void init() {
	}

	/**
	 * invests the entire amount of cash
	 * available in this stock. never 
	 * sells any stock.
	 */
	public void update() 
	{
		float cash = getCash();

		if (cash > 0) 
		{
			addAmountOrder("", cash);
		}
	}
  
  /**
   * get the name
   */
  public String toString()
  {
    return "Keeper";
  }
}