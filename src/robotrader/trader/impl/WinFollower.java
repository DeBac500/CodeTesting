package robotrader.trader.impl;

import robotrader.engine.model.PropertyMetaData;
import robotrader.trader.AbstractTrader;

/**
 * This trader invests in the stock whenever
 * its price goes up. The stock is then
 * subsequently sold when the gain or
 * loss at the current market price
 * exceeds a maximum value.
 * 
 * @author xcapt
 * @author klinst
 */
public class WinFollower extends AbstractTrader {
	
	/**
	 * The maximum win and loss meta data properties
	 */
	private static final PropertyMetaData[] METADATA =
		new PropertyMetaData[] {
			new PropertyMetaData("MAXWIN", PropertyMetaData.TYPE_DOUBLE),
			new PropertyMetaData("MAXLOSS", PropertyMetaData.TYPE_DOUBLE)};
	
	/**
	 * the maximum loss fraction allowed
	 * for a stock position to incur
	 */
	private float _maxloss = 0.05f;
	
	/**
	 * the maximum fraction of gain after which a
	 * stock position is sold
	 */
	private float _maxwin = 0.1f;
	
	/**
	 * the previous price of the stock
	 */
	private float _previous;
	
	/**
	 * the value of the last transactions
	 */
	private float _value = 0f;

	/**
	 * the value of the last transactions
	 */
	private float _max_value = 0f;

	/**
	 * Sets the fraction of maximum loss
	 * that the trader is allowed to
	 * accept. The stock is sold if the
	 * maximum loss is exceeded.
	 * 
	 * @param maxloss the maximum allowed loss fraction
	 * (-1.0 < maxloss < 0.0)
	 */
	public void setMaxLoss(float maxloss) 
	{
		if (Math.abs(maxloss) < 1.0f)
		{
			if (maxloss > 0.0f)
			{
				_maxloss = -maxloss;	
			}
			else if (maxloss < 0.0f)
			{
				_maxloss = maxloss;
			}	
		}
	}

	/**
	 * Sets the fraction of maximum win
	 * required for the trader to sell
	 * its positions in the stock.
	 * 
	 * @param maxwin the maximum win fraction ( > 0.0)
	 */
	public void setMaxWin(float maxwin) 
	{
		if (maxwin > 0.0f)
		{
			_maxwin = maxwin;
		}
	}

	/**
	 * Gets the name of the trader
	 * 
	 * @return its name
	 */
	public String getName() 
	{
		return "WinFollowerW" + _maxwin + "L" + _maxloss;
	}

	/**
	 * Sets a property of this trader. The
	 * two properties are MAXWIN and MAXLOSS.
	 *
	 * @param key The key of the property
	 * @param value the value of the property.
	 */
	public void setProperty(String key, String value) 
	{
		if (key.equals("MAXWIN")) 
		{
			setMaxWin(Float.parseFloat(value));
		} 
		else if (key.equals("MAXLOSS")) 
		{
			setMaxLoss(Float.parseFloat(value));
		}
	}
  
  public String getProperty(String key)
  {
    if (key.equals("MAXWIN")) 
    {
      return Float.toString(_maxwin);
    } 
    else if (key.equals("MAXLOSS")) 
    {
      return Float.toString(_maxloss);
    }   
    
    return "0";
  }

	/**
	 * retrieve meta data for trader properties
	 * @return DOCUMENT ME!
	 */
	public PropertyMetaData[] getPropertyMetaData() 
	{
		return METADATA;
	}

	/**
	 * dummy method
	 */
	public void init() 
	{
	}

	/**
	 * Updates the position of this trader.
	 * It invests all its cash into the
	 * stock when the price is increasing for
	 * a single streak. If the transaction
	 * was successful, then the position
	 * in the stock is sold whenever a
	 * maximum gain/ loss would incur at
	 * the current market price.
	 */
	public void update() 
	{
		String inst = "";
    float current = this.getPrice(inst);
    float cash = this.getCash();
		float quantity = this.getPosition(inst);
		float price = this.getPrice(inst);

		if(quantity > 0)
		{
			// if the current value of the
			// stock positions would incur
			// a maximum gain/ loss then
			// sell the stock
			float curval = quantity * price;
      float p1 = (curval - _value) / _value;
      float p2 = (curval - _max_value) / _max_value;
			
			if (p1 >= _maxwin) 
			{
				addQuantityOrder(inst, -quantity);
			} 
			else if (p2 <= _maxloss) 
			{
				addQuantityOrder(inst, -quantity);
			} 
			
			if (curval > _value)
			{
				_max_value = curval;
			}
		} 
		// either no positions in the stock or
		// last transaction unsuccessful
		// and direction of streak increasing
		// then spend the remaining cash
		// on the stock
		else if ((_previous > 0) && (_previous < current)) 
		{
			if (cash > 0) 
			{
				addAmountOrder(inst, cash);
				_value = cash;
				_max_value = cash;
			}
		}

		_previous = current;
	}
  
  public String toString()
  {
    return "WinFollower";
  }
}
