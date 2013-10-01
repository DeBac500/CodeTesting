package robotrader.trader.impl;

import robotrader.engine.model.PropertyMetaData;
import robotrader.trader.AbstractTrader;

/**
 * This trader follows the current trend of
 * a streak if it continued in the same
 * direction for a certain minimum number of streaks.<br>
 * 
 * If the direction is downwards, then all
 * the positions in the stock are sold. <br>
 * 
 * If the direction is upwards, then a fraction
 * of the cash is spend on it on the next
 * market price.
 * 
 * @author bob 
 * @author klinst
 */
public class CautiousStreakFollower extends AbstractTrader 
{
	
	/**
	 * the fraction of the cash available
	 * allowed to spend on a single transaction
	 */
	private float _maxrisk = 0.5f;
	
	/**
	 * the minimum number of streaks required
	 * to follow
	 */
	private int _streaksize = 3;
	
	/**
	 * the previous price of the stock
	 */
	private float _previous;
	
	/**
	 * the direction that the price changed in the
	 * last time step
	 */
	private int _direction = 0;
	
	/**
	 * the number of streaks the stock
	 * has continued to move in the
	 * same direction.
	 */
	private int _streakcount = 0;

	/**
	 * The maximum risk and streak size meta data
	 * information.
	 */
	private static final PropertyMetaData[] METADATA =
		new PropertyMetaData[] {
			new PropertyMetaData("MAXRISK", PropertyMetaData.TYPE_DOUBLE),
			new PropertyMetaData("STREAKSIZE", PropertyMetaData.TYPE_INTEGER)};
	
	/**
	 * retrieve meta data for trader properties
	 * 
	 * @return the meta data for this trader
	 */
	public PropertyMetaData[] getPropertyMetaData() {
		return METADATA;
	};
	
	/**
	 * Set the maximum risk this trader is allowed
	 * to take. This risk is used to calculate
	 * the maximum cash the trader is allowed to
	 * spend on a single transaction. 
	 *
	 * @param maxrisk the maximum risk (0 < maxrisk <= 1)
	 */
	public void setMaxRisk(float maxrisk) {
		_maxrisk = maxrisk;
	}

	/**
	 * returns the name of the trader
	 *
	 * @return the name
	 */
	public String getName() {
		return "CautiousStreakFollower" + _streaksize + "r" + _maxrisk;
	}

	/**
	 * Sets the minimum number of streaks required
	 * in the same direction for an action to be
	 * taken.
	 *
	 * @param nb the number of streaks
	 */
	public void setStreak(int nb) {
		_streaksize = nb;
	}

	/**
	 * The trader updates its position if 
	 * the direction of the price difference remains 
	 * unchanged for a minimum number of streaks.<br>
	 *
	 * If the direction is downwards and the trader holds
	 * positions in the stock, then all of the stock is
	 * sold at the next market price. <br>
	 * 
	 * If the direction is upwards and the trader
	 * has some cash remaining, then parts of this cash is spent
	 * on the stock. The total amount spend = cash * maxrisk.
	 */
	public void update() 
	{
		String inst = "";
		float current = this.getPrice(inst);

		if (_previous > 0) 
		{
			float cash = this.getCash();
			float quantity = this.getPosition(inst);
			int direction = getDirection(_previous, current);

			if (direction == _direction) 
			{
				_streakcount++;

				if (_streakcount >= _streaksize) 
				{
					_streakcount = 0;

					if (_direction < 0) 
					{
						if (quantity > 0) 
						{
							this.addQuantityOrder(inst, -quantity);
						}
					} 
					else if (_direction > 0) 
					{
						if (cash > 0) 
						{
							float q = cash * _maxrisk;
							addAmountOrder(inst, q);
						}
					}
				}
			} else {
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
	private int getDirection(float prev, float curr) {
		if (curr > prev) {
			return 1;
		} else if (curr < prev) {
			return -1;
		} else {
			return 0;
		}
	}

	/**
	 * dummy method
	 */
	public void init() {

	}

	/**
	 * set the property values of this trader
	 * 
	 * @param key the key identifiying the value
	 * @param value the value associated with the key
	 */
	public void setProperty(String key, String value) 
  {
		if (key.equals("MAXRISK")) 
    {
			_maxrisk = Float.parseFloat(value);
		} 
    else if (key.equals("STREAKSIZE")) 
    {
			_streaksize = Integer.parseInt(value);
		}
	}

  /**
   * set the property values of this trader
   * 
   * @param key the key identifiying the value
   * @param value the value associated with the key
   */
  public String getProperty(String key) 
  {
    if (key.equals("MAXRISK")) 
    {
      return Float.toString(_maxrisk);
    } 
    else if (key.equals("STREAKSIZE")) 
    {
      return Integer.toString(_streaksize);
    }
    
    return "1";
  }

  public String toString()
  {
    return "CautiosStreakFollower";
  }
}