package robotrader.trader.impl;

import java.util.ArrayList;

import robotrader.engine.model.PropertyMetaData;
import robotrader.trader.AbstractTrader;

/**
 * Simple example of a turning point 
 * trend simulation. If the price
 * increases by a minimum percentage
 * and stays below a maximum loss
 * over the period then the stock
 * is bought. If the price falls below
 * the maximum loss value and
 * the maximum loss level is below
 * the minimum buy level, then
 * sell the stock.
 * 
 * @author klinst
 */
public class TurningPointTrader extends AbstractTrader 
{
  /**
   * The maximum risk and streak size meta data
   * information.
   */
  private static final PropertyMetaData[] METADATA =
    new PropertyMetaData[] {
      new PropertyMetaData("MINWIN", PropertyMetaData.TYPE_DOUBLE),
      new PropertyMetaData("MAXLOSS", PropertyMetaData.TYPE_DOUBLE),
      new PropertyMetaData("MAXWIN", PropertyMetaData.TYPE_DOUBLE),
      new PropertyMetaData("PERIOD", PropertyMetaData.TYPE_INTEGER)
      };

	/**
	 * The time series data
	 */
	private ArrayList _data = new ArrayList();
	
	/**
	 * The maximum allowed loss
	 * within the period
	 */
	private float _maxloss = 0.10f;
	
	/**
	 * The minimum required win
	 * within the period
	 */
	private float _minwin = 0.05f;
	
	/**
	 * The maximum win required 
	 * within the period
	 */
	private float _maxwin = 0.25f;
	
	/**
	 * The training period
	 */
	private int _period = 50;
	
	/**
	 * The previous price
	 */
	private double _previous = -1;
	
	/**
	 * Sets the fraction of maximum loss
	 * that the trader is allowed to
	 * accept. The stock is sold if the
	 * maximum loss is exceeded.
	 * 
	 * @param maxloss 
	 */
	private void setMaxLoss(float maxloss) 
	{
		if (Math.abs(maxloss) < 1.0f)
		{
			_maxloss = Math.abs(maxloss);	
		}
	}

	/**
	 * Sets the fraction of minimum win
	 * required for the trader to buy
	 * positions in the stock.
	 * 
	 * @param maxwin the maximum win fraction ( > 0.0)
	 */
	private void setMinWin(float minwin) 
	{
		if (Math.abs(minwin) < 1.0f)
		{
			if (minwin < _maxwin)
			{
				_minwin = Math.abs(minwin);	
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
	private void setMaxWin(float maxwin)
	{
		if (Math.abs(maxwin) < 1.0f)
		{
			if (maxwin > _minwin)
			{
				_maxwin = Math.abs(maxwin);	
			}
		}
	}

	/**
	 * Gets the name of the trader
	 * 
	 * @return its name
	 */
	public String getName() 
	{
		return "TurningPoint (" + _period + "," + 
			_minwin + "," + _maxwin + "," + _maxloss
			+ ")";
	}

	/**
	 * Sets a property of this trader. 
	 *
	 * @param key The key of the property
	 * @param value the value of the property.
	 */
	public void setProperty(String key, String value) 
	{
		if (key.equals("MINWIN")) 
		{
			setMinWin(Float.parseFloat(value));
		} 
		else if (key.equals("MAXLOSS")) 
		{
			setMaxLoss(Float.parseFloat(value));
		}
		else if (key.equals("PERIOD")) 
		{
			_period = Integer.parseInt(value);
		}
		else if (key.equals("MAXWIN")) 
		{
			setMaxWin(Float.parseFloat(value));
		}
	}
  
  /**
   * Gets a property of this trader. 
   *
   * @param key The key of the property
   * @param value the value of the property.
   */
  public String getProperty(String key) 
  {
    if (key.equals("MINWIN")) 
    {
      return Float.toString(_minwin);
    } 
    else if (key.equals("MAXLOSS")) 
    {
      return Float.toString(_maxloss);
    }
    else if (key.equals("PERIOD")) 
    {
      return Integer.toString(_period);
    }
    else if (key.equals("MAXWIN")) 
    {
      return Float.toString(_maxwin);
    }
    
    return "1";
  }


	/**
	 * dummy method
	 */
	public void init() 
	{
	}

	/**
	 * Updates the time series data.
	 * If the minimum win is observed, the
	 * stock is bought. If the maximum
	 * win or the maximum loss is observed,
	 * the stock is sold.
	 */
	public void update() 
	{
		String inst = "";
		float cash = getCash();
		float quantity = getPosition(inst);
		float price = getPrice(inst);

		_data.add(new Float(price));
		
		if (_data.size() > _period)
		{
			_data.remove(0);
		
			float min = Float.MAX_VALUE;
			float max = Float.MIN_VALUE;
			
			for (int i = 0; i < _data.size(); i++)
			{
				Float value = (Float)_data.get(i);
				
				if (value.floatValue() > max)
				{
					max = value.floatValue();
				}
				
				if (value.floatValue() < min)
				{
					min = value.floatValue();
				}
			}
			
			float buylevel = min + (min * _minwin);
			float sellevel = max - (max * _maxloss);
			float sellevel2 = min + (min * _maxwin);
			
			// if price has risen by min win
			// but is below maximum win and
			// below maximum loss then buy
			if (price > buylevel && 
					price < sellevel2 &&
					buylevel < sellevel &&
					price > _previous)
			{
				if (cash > 0) 
				{
					addAmountOrder(inst, cash);
				}
			}
			
			if (price < sellevel &&
					buylevel > sellevel &&
					price < _previous)
			{
				if (quantity > 0)
				{
					addQuantityOrder(inst, -quantity);
				}
			}

			if (price > sellevel2 &&
					price > _previous)
			{
				if (quantity > 0)
				{
					addQuantityOrder(inst, -quantity);
				}			
			}
			
			_data.remove(0);
		}
		
		_previous = price;
	}
  
  public String toString()
  {
    return "TurningPointTrader";
  }
  
  /**
   * retrieve meta data for trader properties
   * 
   * @return the meta data for this trader
   */
  public PropertyMetaData[] getPropertyMetaData() 
  {
    return METADATA;
  }

}
