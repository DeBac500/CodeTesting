package robotrader.trader.impl.machinelearning;

import net.sourceforge.openforecast.models.PolynomialRegressionModel;
import robotrader.market.indicator.impl.machinelearning.OpenForecast;

/**
 * A trader based on polynomial regression. 
 * 
 * @see net.sourceforge.openforecast.models.PolynomialRegressionModel
 * @see robotrader.market.indicator.impl.machinelearning.OpenForecast
 * @author klinst
 */
public class PolynomialRegressionTrader extends OpenForecastTrader
{
  /**
   * The name of the indicator = Polynomial Regression
   */
  public static final String NAME = "Polynomial Regression";
   
  /**
   * The open forecast indicator to use
   */
  private OpenForecast _indicator;
  
  /**
   * Constructs the Regression Trader. 
   * Default length of training period = 50
   * Default length of forecasting period = 50 
   * Default minimum change = 0.1
   */
  public PolynomialRegressionTrader()
  {
    super();
    _model = new PolynomialRegressionModel("Period");
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
      _indicator = new OpenForecast(_model, NAME, _training_period, _forecast_period, _min_win_percentage_change, _max_loss_percentage_change);
    }
    
    return _indicator;
  }
  
  public String toString()
  {
    return "Polynomial Regression";
  }
}
