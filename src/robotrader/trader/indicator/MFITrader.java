package robotrader.trader.indicator;

import robotrader.engine.model.PropertyMetaData;
import robotrader.market.indicator.impl.MFI;

/**
 * Trader based on the MFI indicator
 * This trader is configured by the values needed by
 * its indicator:
 * PERIOD : period in days used to calculate indicator, default value is 20
 * BUYLEVEL : buy level, default value is 20
 * SELLLEVEL : sell level, default value is 80
 *
 * @see robotrader.market.indicator.impl.MFI
 * @author xcapt
 */
public class MFITrader extends AbstractIndicatorTrader 
{
	private float _buylvl = 20;
	private float _selllvl = 80;
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
	 * Creates a new MFITrader object.
	 */
	public MFITrader() {
		super();
	}

	public void init() 
  {
		String key = MFI.toString(_period, _buylvl, _selllvl);
		MFI indic = (MFI) getMarket().getIndicatorContainer().get(key);
		if (indic == null) {
			indic = new MFI(_period, "", _buylvl, _selllvl);
			getMarket().getIndicatorContainer().register(key, indic);
		}
		setIndicator(indic);
	}

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
    
    return "10";
  }

  public String getName() 
  {
		return MFI.toString(_period, _buylvl, _selllvl);
	}
  
  public String toString()
  {
    return "MFI";
  }
}