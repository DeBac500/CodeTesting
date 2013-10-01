package robotrader.trader.indicator;

import robotrader.engine.model.PropertyMetaData;
import robotrader.market.indicator.impl.MACD;

/**
 * Trader based on the Moving Average indicator
 * This trader is configured by the values needed by
 * its indicator:
 * PERIOD : period in days used to calculate indicator, default value is 20
 *
 * @see robotrader.market.indicator.impl.MACD
*/
public class MACDTrader extends AbstractIndicatorTrader {
	private int _longperiod = 26;
	private int _shortperiod = 12;
	private int _signalperiod = 9;

	private static final PropertyMetaData[] METADATA =
		new PropertyMetaData[] {
			new PropertyMetaData("LONGPERIOD", PropertyMetaData.TYPE_INTEGER),
			new PropertyMetaData("SHORTPERIOD", PropertyMetaData.TYPE_INTEGER),
			new PropertyMetaData(
				"SIGNALPERIOD",
				PropertyMetaData.TYPE_INTEGER)};
	/**
	 * retrieve meta data for trader properties
	 */
	public PropertyMetaData[] getPropertyMetaData() {
		return METADATA;
	};

	/**
	 * Creates a new MovingAverageTrader object.
	 */
	public MACDTrader() {
	}

	public void init() {
		String key = MACD.toString(_longperiod, _shortperiod, _signalperiod);
		MACD indic = (MACD) getMarket().getIndicatorContainer().get(key);
		if (indic == null) {
			indic = new MACD(_longperiod, _shortperiod, _signalperiod, "");
			getMarket().getIndicatorContainer().register(key, indic);
		}
		setIndicator(indic);
	}

	public void setProperty(String key, String value) {
		if (key.equals("LONGPERIOD")) {
			_longperiod = Integer.parseInt(value);
		} else if (key.equals("SHORTPERIOD")) {
			_shortperiod = Integer.parseInt(value);
		} else if (key.equals("SIGNALPERIOD")) {
			_signalperiod = Integer.parseInt(value);
		}
	}
  
  public String getProperty(String key) 
  {
    if (key.equals("LONGPERIOD")) 
    {
      return Integer.toString(_longperiod);
    } 
    else if (key.equals("SHORTPERIOD")) 
    {
      return Integer.toString(_shortperiod);
    } 
    else if (key.equals("SIGNALPERIOD")) 
    {
      return Integer.toString(_signalperiod);
    }
    
    return "10";
  }
  
	public String getName() 
  {
		return MACD.toString(_longperiod, _shortperiod, _signalperiod);
	}
  
  public String toString()
  {
    return "MACD";
  }
}