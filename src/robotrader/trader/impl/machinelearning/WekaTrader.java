package robotrader.trader.impl.machinelearning;

import robotrader.engine.model.PropertyMetaData;
import robotrader.market.indicator.impl.machinelearning.Weka;
import robotrader.trader.indicator.AbstractIndicatorTrader;
import weka.classifiers.Classifier;

/**
 * A base trader for all traders using weka
 * indicators. 
 * 
 * @see robotrader.market.indicator.impl.machinelearning.Weka
 * @author klinst
 */
public abstract class WekaTrader extends AbstractIndicatorTrader
{ 
  /**
   * The number of periods required for training
   */
  protected int _training_period;
  
  /**
   * The number of periods required for forecasting
   */
  protected int _forecast_period;
  
  /**
   * The minimum change required to issue signal
   */
  protected float _percentage_change;
  
  /**
   * The model to use
   */
  protected Classifier _classifier;
  
  private static final PropertyMetaData[] METADATA =
    new PropertyMetaData[] {
      new PropertyMetaData("TRAINING_PERIOD", PropertyMetaData.TYPE_INTEGER),
      new PropertyMetaData("FORECAST_PERIOD", PropertyMetaData.TYPE_INTEGER),
      new PropertyMetaData("PERCENTAGE_CHANGE", PropertyMetaData.TYPE_DOUBLE),
    };
  
  /**
   * retrieve meta data for trader properties
   */
  public PropertyMetaData[] getPropertyMetaData() 
  {
    return METADATA;
  }
  
  /**
   * Constructs the Weka Trader.
   *  
   * Default length of training period = 50
   * Default length of forecasting period = 50 
   * Default minimum change = 0.1
   */
  public WekaTrader()
  {
    _training_period = 50;
    _forecast_period = 50;
    _percentage_change = 0.1f;
  }
  
  /**
   * Set the properties of this trader.
   * 
   * @param key possible values are TRAINING_PERIOD,
   * FORECAST_PERIOD, PERCENTAGE_CHANGE
   * @param value the values for the keys 
   */
  public void setProperty(String key, String value) 
  {
    if (key.equals("TRAINING_PERIOD")) 
    {
      _training_period = Integer.parseInt(value);
    }
    
    if (key.equals("FORECAST_PERIOD")) 
    {
      _forecast_period = Integer.parseInt(value);
    }
    
    if (key.equals("PERCENTAGE_CHANGE")) 
    {
      _percentage_change = Float.parseFloat(value);
    }
  }
  
  public String getProperty(String key) 
  {
    if (key.equals("TRAINING_PERIOD")) 
    {
      return Integer.toString(_training_period);
    }
    
    if (key.equals("FORECAST_PERIOD")) 
    {
      return Integer.toString(_forecast_period);
    }
    
    if (key.equals("PERCENTAGE_CHANGE")) 
    {
      return Float.toString(_percentage_change);
    }
    
    return "10";
  }

  /**
   * Initialise the trader. Registers the
   * indicator with the container.
   */
  public void init()
  {
    String key = getName();
    
    Weka indic = (Weka) getMarket().getIndicatorContainer().get(key);
    if (indic == null) 
    {      
      indic = getWeka();
      getMarket().getIndicatorContainer().register(key, indic);
    }
    setIndicator(indic);
  }
  
  /**
   * Get the weka indicator.
   * 
   * @return
   */
  protected abstract Weka getWeka();
}
