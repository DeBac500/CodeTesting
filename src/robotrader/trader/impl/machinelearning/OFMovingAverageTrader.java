package robotrader.trader.impl.machinelearning;

import net.sourceforge.openforecast.models.MovingAverageModel;
import robotrader.market.indicator.impl.machinelearning.OpenForecast;

/**
 * A trader based on moving average. 
 * 
 * @see net.sourceforge.openforecast.models.MovingAverageModel
 * @see robotrader.market.indicator.impl.machinelearning.OpenForecast
 * @author klinst
 */
public class OFMovingAverageTrader extends OpenForecastTrader
{
  /**
   * The name of the trader = OF Mvg. Avg.
   */
  public static final String NAME = "OF Mvg. Avg.";
  
  /**
   * The open forecast indicator to use
   */
  private OpenForecast _indicator;
  
  /**
   * Constructs the Moving Average Trader. 
   * Default length of training period = 50
   * Default length of forecasting period = 50 
   * Default minimum change = 0.1
   */
  public OFMovingAverageTrader()
  {
    super();
  }
  

  /**
   * Gets the name of the trader
   */
  public String getName() 
  {
    return OpenForecast.toString(NAME, _training_period, _forecast_period, _min_win_percentage_change, _max_loss_percentage_change);
  }

  /**
   * Gets the open forecast indicator
   * to use for trading.
   * 
   * @return The open forecast indicator.
   */
  public OpenForecast getOpenForecast()
  {
    if (_indicator == null)
    {
      _model = new MovingAverageModel(_training_period);
      _indicator = new OpenForecast(_model, NAME, _training_period, _forecast_period, _min_win_percentage_change, _max_loss_percentage_change);
    }
    
    return _indicator;
  }
}
