package robotrader.trader.impl.machinelearning;

import net.sourceforge.openforecast.ForecastingModel;
import robotrader.engine.model.PropertyMetaData;
import robotrader.market.indicator.impl.machinelearning.OpenForecast;
import robotrader.trader.indicator.AbstractIndicatorTrader;

/**
 * A base trader for all traders using open forecast
 * indicators. 
 * 
 * @see robotrader.market.indicator.impl.machinelearning.OpenForecast
 * @author klinst
 */
public abstract class OpenForecastTrader extends AbstractIndicatorTrader
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
   * The minimum change in win required to issue signal
   */
  protected float _min_win_percentage_change;
  
  /**
   * The maximum change in loss to issue a signal
   */
  protected float _max_loss_percentage_change;
  
  /**
   * The model to use
   */
  protected ForecastingModel _model;
  
  private static final PropertyMetaData[] METADATA =
    new PropertyMetaData[] {
      new PropertyMetaData("TRAINING_PERIOD", PropertyMetaData.TYPE_INTEGER),
      new PropertyMetaData("FORECAST_PERIOD", PropertyMetaData.TYPE_INTEGER),
      new PropertyMetaData("MINIMUM_WIN_PERCENTAGE", PropertyMetaData.TYPE_DOUBLE),
      new PropertyMetaData("MAXIMUM_LOSS_PERCENTAGE", PropertyMetaData.TYPE_DOUBLE)
    };
  
  /**
   * retrieve meta data for trader properties
   */
  public PropertyMetaData[] getPropertyMetaData() 
  {
    return METADATA;
  }

  /**
   * Constructs the Open Forecast Trader.
   *  
   * Default length of training period = 50
   * Default length of forecasting period = 50 
   * Default minimum change = 0.1
   */
  public OpenForecastTrader()
  {
    _training_period = 50;
    _forecast_period = 50;
    _min_win_percentage_change = 0.1f;
    _max_loss_percentage_change = 0.05f;
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
    
    if (key.equals("MINIMUM_WIN_PERCENTAGE")) 
    {
      _min_win_percentage_change = Float.parseFloat(value);
    }

    if (key.equals("MAXIMUM_LOSS_PERCENTAGE")) 
    {
      _max_loss_percentage_change = Float.parseFloat(value);
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
    
    if (key.equals("MINIMUM_WIN_PERCENTAGE")) 
    {
      return Float.toString(_min_win_percentage_change);
    }
    
    if (key.equals("MAXIMUM_LOSS_PERCENTAGE")) 
    {
      return Float.toString(_max_loss_percentage_change);
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
    
    OpenForecast indic = (OpenForecast) getMarket().getIndicatorContainer().get(key);
    if (indic == null) 
    {      
      indic = getOpenForecast();
      getMarket().getIndicatorContainer().register(key, indic);
    }
    setIndicator(indic);
  }
  
  /**
   * Get the open forecast indicator.
   * 
   * @return
   */
  protected abstract OpenForecast getOpenForecast();
}
