package robotrader.trader.indicator;

import robotrader.engine.model.PropertyMetaData;
import robotrader.market.indicator.impl.ROC;

/**
 * Trader based on the ROC indicator
 * This trader is configured by the values needed by
 * its indicator:
 * PERIOD : period in days used to calculate indicator, default value is 12
 * BUYLEVEL : buy level, default value is -8
 * SELLLEVEL : sell level, default value is 8
 *
 * @see robotrader.market.indicator.impl.ROC
 * @author xcapt
 */
public class ROCTrader extends AbstractIndicatorTrader 
{
	private float _buylvl = -8;
	private float _selllvl = 8;
	private int _period = 12;

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
	 * Creates a new RSITrader object.
	 */
	public ROCTrader() {
	}

	public void init() {
		String key = ROC.toString(_period, _buylvl, _selllvl);
		ROC indic = (ROC) getMarket().getIndicatorContainer().get(key);
		if (indic == null) {
			indic = new ROC(_period, "", _buylvl, _selllvl);
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
			return ROC.toString(_period, _buylvl, _selllvl);
	}
  
  public String toString()
  {
    return "ROC";
  }
}