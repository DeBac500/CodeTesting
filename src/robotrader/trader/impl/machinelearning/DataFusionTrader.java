package robotrader.trader.impl.machinelearning;

import robotrader.engine.model.PropertyMetaData;
import robotrader.market.indicator.impl.machinelearning.DataFusion;
import robotrader.trader.indicator.AbstractIndicatorTrader;

/**
 * The trader for the data fusion indicator.
 * 
 * @see robotrader.market.indicator.impl.machinelearning.DataFusion
 * @author klinst
 */
public class DataFusionTrader extends AbstractIndicatorTrader
{
  /**
   * The signalling period
   */
  private int _period = 20;
  
  private static final PropertyMetaData[] METADATA =
    new PropertyMetaData[] {
      new PropertyMetaData("TRAINING_PERIOD", PropertyMetaData.TYPE_INTEGER),
    };
  
  /**
   * retrieve meta data for trader properties
   */
  public PropertyMetaData[] getPropertyMetaData() 
  {
    return METADATA;
  }

	/**
	 * Default Constructor
	 */
	public DataFusionTrader()
	{
	}

	/**
	 * Get the name of the trader as the
	 * name of the indicator
	 */
	public String getName()
	{
		return DataFusion.toString(_period);
	}

	/**
	 * Initialse the trader by creating
	 * and registering the indicator
	 * with the indicator container.
	 */
	public void init() 
	{
		String key = DataFusion.toString(5);
		DataFusion indic =
			(DataFusion) getMarket().getIndicatorContainer().get(key);
		if (indic == null) 
		{
			indic = new DataFusion(_period);
			getMarket().getIndicatorContainer().register(key, indic);
		}
		setIndicator(indic);
	}
  
  /**
   * Set the properties of this trader.
   * 
   * @param key possible values are TRAINING_PERIOD
   * @param value the values for the keys 
   */
  public void setProperty(String key, String value) 
  {
    if (key.equals("TRAINING_PERIOD")) 
    {
      _period = Integer.parseInt(value);
    }
  }
  
  public String getProperty(String key) 
  {
    if (key.equals("TRAINING_PERIOD")) 
    {
      return Integer.toString(_period);
    }
    
    return "20";
  }

  public String toString()
  {
    return "Data Fusion";
  }
}
