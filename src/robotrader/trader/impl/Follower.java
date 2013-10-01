package robotrader.trader.impl;

import robotrader.trader.AbstractTrader;

/**
 * The follower trader completely invests into
 * a stock if its price goes up in value.<br>
 * 
 * If the price is decreasing, then the trader
 * sells all its positions in the stock.
 * 
 * @author bob
 * @author klinst
 */
public class Follower extends AbstractTrader 
{
	
	/**
	 * the previous price of the stock
	 */
	private double _previous;

	/**
	 * gets the name of the trader
	 *
	 * @return name
	 */
	public String getName() {
		return "Follower";
	}

	/**
	 * fully invests the entire amount of cash
	 * if the current price is greater than
	 * the previous one.<br>
	 *
	 * The opposite holds true if the
	 * direction is decreasing as the trader
	 * sells its entire position in the
	 * stock at the next market price.
	 */
	public void update() {
		String inst = "";
		double current = this.getPrice(inst);

		if (_previous > 0) 
		{
			float cash = this.getCash();
			float quantity = this.getPosition(inst);

			if (_previous > current) 
			{
				if (quantity > 0) 
				{
					this.addQuantityOrder(inst, -quantity);
				}
			} 
			else if (_previous < current) 
			{
				if (cash > 0) 
				{
					addAmountOrder(inst, cash);
				}
			}
		}

		_previous = current;
	}

	/**
	 * dummy method 
	 */
	public void init() 
  {
	}
  
  public String toString()
  {
    return "Follower";
  }
}