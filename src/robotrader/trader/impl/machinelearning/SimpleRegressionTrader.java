package robotrader.trader.impl.machinelearning;

import robotrader.market.indicator.impl.machinelearning.Weka;
import weka.classifiers.functions.SimpleLinearRegression;

/**
 * A trader based on simple linear regression. 
 * 
 * @see robotrader.market.indicator.impl.machinelearning.Weka
 * @author klinst
 */
public class SimpleRegressionTrader extends WekaTrader
{
  /**
   * The name of the indicator = Regression
   */
  public static final String NAME = "SimpleLinearRegression";
  
  /**
   * The open forecast indicator to use
   */
  private Weka _indicator;
      
  /**
   * Constructs the Regression Trader. 
   * Default length of training period = 50
   * Default length of forecasting period = 50 
   * Default minimum change = 0.1
   */
  public SimpleRegressionTrader()
  {
    super();
    _classifier = new SimpleLinearRegression();
  }
  
  /**
   * Gets the name of the trader
   */
  public String getName()
  {
    return Weka.toString(NAME, _training_period, _forecast_period, _percentage_change);
  }
  
  /**
   * Gets the weka indicator
   * to use for trading.
   * 
   * @return The weka indicator.
   */
  public Weka getWeka()
  {
    if (_indicator == null)
    {
      _indicator = new Weka(_classifier, _training_period, _forecast_period, _percentage_change);
    }
    
    return _indicator;
  }
  
  public String toString()
  {
    return "SimpleLinearRegression";
  }
}
