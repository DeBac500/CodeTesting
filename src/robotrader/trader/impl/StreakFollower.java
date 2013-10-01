package robotrader.trader.impl;

import robotrader.engine.model.PropertyMetaData;
import robotrader.trader.AbstractTrader;

/**
 * This trader follows the streak if it 
 * continues to increase or decrease in
 * value for a given minimum number of
 * streaks. In contrast to the Cautious
 * Streak Follower, this trader is always
 * completely invested and does not spread
 * its cash across multiple possible
 * market prices.
 * 
 * @author bob
 * @author klinst 
 */
public class StreakFollower extends AbstractTrader {
	
	/**
	 * The meta data information
	 */
	private static final PropertyMetaData[] METADATA =
		new PropertyMetaData[] {
			new PropertyMetaData("STREAKUPSIZE", PropertyMetaData.TYPE_INTEGER),
			new PropertyMetaData(
				"STREAKDOWNSIZE",
				PropertyMetaData.TYPE_INTEGER),
			};
	
	/**
	 * the previous streak of the stock
	 */
	private double _previous;
	
	/**
	 * the previous direction of the streak
	 */
	private int _direction = 0;
	
	/**
	 * the number of streaks going into that
	 * direction
	 */
	private int _streakcount = 0;
	
	/**
	 * the minimum number of down streaks
	 * required for this trader to sell
	 * all its positions.
	 */
	private int _streakdownsize = 0;
	
	/**
	 * the minimum number of up streaks
	 * required for this trader spend
	 * all its cash on the stock.
	 */
	private int _streakupsize = 3;

	/**
	 * gets the name of the trader.
	 * 
	 * @return name
	 */
	public String getName() {
		return "StreakFollowerUp" + _streakupsize + "Down" + _streakdownsize;
	}

	/**
	 * sets the property key and value pairs for
	 * this trader.
	 *
	 * @param key the key identifying the value
	 * @param value the value corresponding to the key
	 */
	public void setProperty(String key, String value) 
  {
		if (key.equals("STREAKUPSIZE")) 
    {
			_streakupsize = Integer.parseInt(value);
		} 
    else if (key.equals("STREAKDOWNSIZE")) 
    {
			_streakdownsize = Integer.parseInt(value);
		}
	}

  /**
   * gets the property value for the key for
   * this trader.
   *
   * @param key the key identifying the value
   * @return value the value corresponding to the key
   */
  public String getProperty(String key) 
  {
    if (key.equals("STREAKUPSIZE")) 
    {
      return Integer.toString(_streakupsize);
    } 
    else if (key.equals("STREAKDOWNSIZE")) 
    {
      return Integer.toString(_streakdownsize);
    }
    
    return "0";
  }
  
	/**
	 * retrieve meta data for trader properties
	 * 
	 * @return the meta data
	 */
	public PropertyMetaData[] getPropertyMetaData() {
		return METADATA;
	}

	/**
	 * set the minimum number of streaks
	 * required to go down for the
	 * trader to sell all its positions
	 * in the stock.
	 * 
	 * @param nb number of streaks
	 */
	public void setStreakDown(int nb) {
		_streakdownsize = nb;
	}

	/**
	 * set the minimum number of streaks
	 * required to go up for the
	 * trader to spend all its cash
	 * on the stock.
	 * 
	 * @param nb number of streaks
	 */
	public void setStreakUp(int nb) {
		_streakupsize = nb;
	}

	/**
	 * dummy method
	 */
	public void init() {
	}

	/**
	 * Updates traders action. If the streaks
	 * keep going in the same direction for at
	 * least streak down or streak up number
	 * of times, then the trader either
	 * completely sells all its positions
	 * or it invests all its cash in the
	 * stock. 
	 * 
	 */
	public void update() 
	{
		String inst = "";
		double current = this.getPrice(inst);

		if (_previous > 0) 
		{
			float cash = this.getCash();
			float quantity = this.getPosition(inst);
			int direction = getDirection(_previous, current);

			if (direction == _direction) 
			{
				_streakcount++;

				if ((_direction < 0) && (_streakcount >= _streakdownsize)) 
				{
					if (quantity > 0) 
					{
						this.addQuantityOrder(inst, -quantity);
					}
				} 
				else if (
					(_direction > 0) && (_streakcount >= _streakupsize)) 
				{
					if (cash > 0) 
					{
						addAmountOrder(inst, cash);
					}
				}
			} 
			else 
			{
				_streakcount = 0;
			}

			_direction = direction;
		}

		_previous = current;
	}

	/**
	 * determines the direction of the price time
	 * series.
	 * 
	 * @param prev the previous price
	 * @param curr the current price
	 * @return 1 if curr > prev, -1 if curr < prev, 0 otherwise
	 */
	private int getDirection(double prev, double curr) {
		if (curr > prev) {
			return 1;
		} else if (curr < prev) {
			return -1;
		} else {
			return 0;
		}
	}
  
  public String toString()
  {
    return "StreakFollower";
  }
}