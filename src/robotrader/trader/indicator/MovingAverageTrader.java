package robotrader.trader.indicator;

import robotrader.engine.model.PropertyMetaData;
import robotrader.market.indicator.impl.MovingAverage;

/**
 * Trader based on the Moving Average indicator
 * This trader is configured by the values needed by
 * its indicator:
 * PERIOD : period in days used to calculate indicator, default value is 20
 *
 * @see robotrader.market.indicator.impl.MovingAverage
 * @author xcapt
 */
public class MovingAverageTrader extends AbstractIndicatorTrader {
	private int _period = 20;

	private static final PropertyMetaData[] METADATA =
		new PropertyMetaData[] {
			 new PropertyMetaData("PERIOD", PropertyMetaData.TYPE_INTEGER)};
	/**
	 * retrieve meta data for trader properties
	 */
	public PropertyMetaData[] getPropertyMetaData() {
		return METADATA;
	};

	/**
	 * Creates a new MovingAverageTrader object.
	 */
	public MovingAverageTrader() {
	}

	public void init() {
		String key = MovingAverage.toString(_period);
		MovingAverage indic =
			(MovingAverage) getMarket().getIndicatorContainer().get(key);
		if (indic == null) {
			indic = new MovingAverage(_period, "");
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
	}

  public String getProperty(String key) 
  {
    if (key.equals("PERIOD")) 
    {
      return Integer.toString(_period);
    }
    
    return "50";
  }

	public String getName() 
  {
		return MovingAverage.toString(_period);	
	}
  
  public String toString()
  {
    return "MovingAverage";
  }
}