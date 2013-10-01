package robotrader.trader.impl;

import robotrader.engine.model.PropertyMetaData;
import robotrader.trader.AbstractTrader;

/**
 * Simple trader that spreads its cash over the
 * entire trading period in regular intervals.
 * 
 * @author klinst
 */
public class CostAverageTrader extends AbstractTrader 
{
  /**
   * The meta data information
   */
  private static final PropertyMetaData[] METADATA =
    new PropertyMetaData[] {
      new PropertyMetaData("PERIOD", PropertyMetaData.TYPE_INTEGER),
      };

  /**
   * The length of the period between
   * purchases
   */
	private int _invest_period = 50;
  
  /**
   * The amount of cash spent regularly
   */
  private float _invest_cash = 1.0f; 
  
  /**
   * The number of periods passed
   */
  private int _current_period = 0;
  
	/**
	 * gets the name of the trader
	 * 
	 * @return name
	 */
	public String getName() 
  {
		return "Cost Average " + _invest_period;
	}

	/**
	 * calculates the amount of cash to be spend
   * at each interval
	 */
	public void init() 
  {
    int num_quotes = _market.getDataSize();
    float initial_cash = _account.getCash();
    
    int num_investments = num_quotes / _invest_period;
    _invest_cash = initial_cash / (float)num_investments;
	}

	/**
   * after each of the intervals, invest the same partial
   * amount of cash into the stock
	 */
	public void update() 
	{
    int i = ++_current_period % _invest_period;
    
    if (i == 0)
    {
      addAmountOrder("", _invest_cash);
    }
	}
  
  /**
   * set the property values of this trader
   * 
   * @param key the key identifiying the value
   * @param value the value associated with the key
   */
  public void setProperty(String key, String value) 
  {
    if (key.equals("PERIOD")) 
    {
      _invest_period = Integer.parseInt(value);
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
    if (key.equals("PERIOD")) 
    {
      return Integer.toString(_invest_period);
    } 
    
    return "50";
  }
  
  public String toString()
  {
    return "Cost Average";
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