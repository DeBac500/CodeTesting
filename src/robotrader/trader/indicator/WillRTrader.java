package robotrader.trader.indicator;

import robotrader.engine.model.PropertyMetaData;
import robotrader.market.indicator.impl.WilliamsR;

/**
 * Trader based on the Williams%R indicator
 * 
 * time period in days to compute indicator
 * instrument(stock or index) of indicator
 * buy level (50-100)
 * sell level (0-50)
 * 
 * @see robotrader.market.indicator.impl.WilliamsR
 * @author xcapt
 */
public class WillRTrader extends AbstractIndicatorTrader {
	private float _buylvl = 85;
	private float _selllvl = 15;
	private int _period = 20;

	private static final PropertyMetaData[] METADATA =
		new PropertyMetaData[] {
			new PropertyMetaData("PERIOD", PropertyMetaData.TYPE_INTEGER),
			new PropertyMetaData("BUYLVL", PropertyMetaData.TYPE_DOUBLE),
			new PropertyMetaData("SELLLVL", PropertyMetaData.TYPE_DOUBLE)};
	/**
	 * retrieve meta data for trader properties
	 */
	public PropertyMetaData[] getPropertyMetaData() {
		return METADATA;
	};

	/**
	 * Creates a new WillRTrader object.
	 */
	public WillRTrader() {
	}

	public void init() {
		String key = WilliamsR.toString(_period, _buylvl, _selllvl);
		WilliamsR indic =
			(WilliamsR) getMarket().getIndicatorContainer().get(key);
		if (indic == null) {
			indic = new WilliamsR(_period, "", _buylvl, _selllvl);
			getMarket().getIndicatorContainer().register(key, indic);
		}
		setIndicator(indic);
	}

	public void setProperty(String key, String value) 
  {
		if (key.equals("PERIOD")) 
    {
			_period = Integer.parseInt(value);
		} 
    else if (key.equals("BUYLVL")) 
    {
			_buylvl = Float.parseFloat(value);
		} 
    else if (key.equals("SELLLVL")) 
    {
			_selllvl = Float.parseFloat(value);
		}
	}
  
  public String getProperty(String key) 
  {
    if (key.equals("PERIOD")) 
    {
      return Integer.toString(_period);
    } 
    else if (key.equals("BUYLVL")) 
    {
      return Float.toString(_buylvl);
    } 
    else if (key.equals("SELLLVL")) 
    {
      return Float.toString(_selllvl);
    }
    
    return "20";
  }
	
	public String getName()
	{
		return WilliamsR.toString(_period, _buylvl, _selllvl);
	}
  
  public String toString()
  {
    return "Williams%R";
  }
}