package robotrader.trader.indicator;

import robotrader.engine.model.PropertyMetaData;
import robotrader.market.indicator.impl.DeMarker;

/**
 * Implementation of an automated trader using the DeMarker
 * Indicator to buy or sell.
 * This trader is configured by the values needed by the
 * DeMarker indicator:
 * PERIOD : period in days used to calculate indicator, default value is 22
 * BUYLEVEL : buy level, default value is 30
 * SELLLEVEL : sell level, default value is 70
 * 
 *  @see    robotrader.market.indicator.impl.DeMarker
 */
public class DeMarkerTrader extends AbstractIndicatorTrader 
{
	/**
	 * The BUYLEVEL
	 */
	private float _buylvl = 30;
	
	/**
	 * The SELLLEVEL
	 */
	private float _selllvl = 70;
	
	/**
	 * The PERIOD
	 */
	private int _period = 22;

	/**
	 * The Metadata information for the
	 * properties of this trader.
	 */
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
	 * Creates a new DeMarkerTrader object.
	 */
	public DeMarkerTrader() {
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
		
		DeMarker indic =
			(DeMarker) getMarket().getIndicatorContainer().get(key);
		
		if (indic == null) {
			indic = new DeMarker(_period, "", _buylvl, _selllvl);
			getMarket().getIndicatorContainer().register(key, indic);
		}
		setIndicator(indic);
	}
	
	/**
	 * sets either the PERIOD, BUYLVL or SELLLVL
	 * property.
	 * 
	 * @param key The key (any of the 3 values above)
	 * @param value The value for the key
	 */
	public void setProperty(String key, String value) 
	{
		if (key.equals("PERIOD")) {
			_period = Integer.parseInt(value);
		} else if (key.equals("BUYLVL")) {
			_buylvl = Float.parseFloat(value);
		} else if (key.equals("SELLLVL")) {
			_selllvl = Float.parseFloat(value);
		}
	}

  /**
   * gets either the PERIOD, BUYLVL or SELLLVL
   * property.
   * 
   * @param key The key (any of the 3 values above)
   * @return value The value for the key
   */
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
    
    return "30";
  }
  
	/**
	 * Gets the name of the trader.
	 * 
	 * @return The trader's name
	 */
	public String getName() 
	{
		return DeMarker.toString(_period, _buylvl, _selllvl);
	}
  
  public String toString()
  {
    return "DeMarker";
  }
}