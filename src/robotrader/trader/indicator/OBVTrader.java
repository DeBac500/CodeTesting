package robotrader.trader.indicator;

import robotrader.engine.model.PropertyMetaData;
import robotrader.market.indicator.impl.OBV;

/**
 * 
 *  @see    robotrader.market.indicator.impl.OBV
 */
public class OBVTrader extends AbstractIndicatorTrader 
{
	/**
	 * The PERIOD
	 */
	private int _period = 20;

	/**
	 * The Metadata information for the
	 * properties of this trader.
	 */
	private static final PropertyMetaData[] METADATA =
		new PropertyMetaData[] {
			new PropertyMetaData("PERIOD", PropertyMetaData.TYPE_INTEGER)
    };
  
	/**
	 * retrieve meta data for trader properties
	 */
	public PropertyMetaData[] getPropertyMetaData() {
		return METADATA;
	};

	/**
	 * Creates a new DeMarkerTrader object.
	 */
	public OBVTrader() {
	}

	/**
	 * initialises the DeMarker Trader. gets the
	 * indicator from the Indicator Container or
	 * creates a new one using the supplied 
	 * attributes.
	 */
	public void init() 
	{
		String key = getName();
		
		OBV indic =
			(OBV) getMarket().getIndicatorContainer().get(key);
		
		if (indic == null) {
			indic = new OBV(_period, "");
			getMarket().getIndicatorContainer().register(key, indic);
		}
		setIndicator(indic);
	}
	
	/**
	 * sets either the PERIOD
	 * property.
	 * 
	 * @param key The key 
	 * @param value The value for the key
	 */
	public void setProperty(String key, String value) 
	{
		if (key.equals("PERIOD")) 
    {
			_period = Integer.parseInt(value);
		}
	}

  /**
   * gets the PERIOD
   * property.
   * 
   * @param key The key 
   * @param value The value for the key
   */
  public String getProperty(String key) 
  {
    if (key.equals("PERIOD")) 
    {
      return Integer.toString(_period);
    }
    
    return "0";
  }

	/**
	 * Gets the name of the trader.
	 * 
	 * @return The trader's name
	 */
	public String getName() 
	{
		return OBV.toString(_period);
	}
  
  public String toString()
  {
    return "OBV";
  }
}