package robotrader.market.indicator.impl.machinelearning;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import net.sourceforge.openforecast.DataPoint;
import net.sourceforge.openforecast.DataSet;
import net.sourceforge.openforecast.ForecastingModel;
import net.sourceforge.openforecast.Observation;
import robotrader.market.AbstractIndicator;
import robotrader.market.HistoricData;
import robotrader.market.IIndicator;

/**
 * Indicator for all forecasting models provided
 * by the <A href="http://sourceforge.net/projects/opentrader">OpenForecast</A>
 * project. These include simple Linear Regression,
 * Polynomial Regression, Exponential Smoothing and
 * moving average models.
 * 
 * @author klinst
 */
public class OpenForecast extends AbstractIndicator
{
  /**
   * The logger
   */
  private static final Logger trace = Logger.getLogger(OpenForecast.class);
  
  /**
   * The list of Observations
   */
  private ArrayList _data;
  
  /**
   * The length of the training period 
   */
  protected int _training_period;
  
  /**
   * The most recent period
   */
  protected int _current_period;
  
  /**
   * The length of the forecast period (e.g. how far
   * to look into the future)
   */
  protected int _forecast_period;
  
  /**
   * The minimum change required
   * for the indicator to change its
   * signal
   */
  private float _min_win_percentage_change;
  
  /**
   * 
   */
  private float _max_loss_percentage_change;
  
  /**
   * The OpenForecast forecasting
   * model to use
   */
  private ForecastingModel _model;
  
  /**
   * The name of the indicator
   * (e.g. Regression)
   */
  private String _name;
  
  /**
   * Constructs the Open forecaster.
   * 
   * @param model The forecasting model to use
   * @param name The name of the indicator
   * @param training_period The number of quotes required
   * for training
   * @param forecast_period The number of time steps
   * to forecast
   * @param percentage_change The minimum percentage
   * change for a signal
   */
  public OpenForecast(ForecastingModel model,
      String name,
      int training_period, 
      int forecast_period,
      float min_win_percentage_change,
      float max_loss_percentage_change)
  {
    _data = new ArrayList();
    _model = model;
    _name = name;
    _training_period = training_period;
    _forecast_period = forecast_period;
    _min_win_percentage_change = min_win_percentage_change;
    _max_loss_percentage_change = max_loss_percentage_change;
    _current_period = 0;
  }

  /**
   * Adds quotes in chronological order to
   * the indicator.
   * 
   * @param data The quote
   */
  public void add(HistoricData data)
  {
    DataPoint point = new Observation(data.getClose());
    point.setIndependentValue("Period", _current_period++);
    _data.add(point);

    if (!_ready)
    {
      if (_data.size() >= _training_period)
      {
        _ready = true;
        generateIndicator(data.getClose());
      }
    }
    else
    {
      _data.remove(0);
      generateIndicator(data.getClose());
    }
  }
  
  /**
   * Generates the indicator for the current
   * close price.
   * 
   * @param price The closing price
   */
  private void generateIndicator(float price)
  {
    DataSet training_data = new DataSet();   
    training_data.setTimeVariable("Period");
    training_data.addAll(_data);
    
    _model.init(training_data);
    
    try
    {
      DataPoint point = new Observation(0.0f);
      point.setIndependentValue("Period", _current_period + _forecast_period);

      float forecast = (float)_model.forecast(point);
      float change_up = price * _min_win_percentage_change;
      float change_down = price * this._max_loss_percentage_change;
      
      if (forecast > (price + change_up))
      {
        _direction = IIndicator.BUY;
      }
      else if (forecast < (price - change_down))
      {
        _direction = IIndicator.SELL;
      }
      else
      {
        _direction = IIndicator.HOLD;
      }
    }
    catch(Exception e)
    {
      trace.error("Problem occurred while forecasting!", e);
    } 
   }
  
  /**
   * Gets a string representation for the
   * trader
   * 
   * @param name The name of the indicator
   * @param training_period The training period
   * @param forecast_period The forecast period
   * @param percentage_change The minimum change
   * @return The string
   */
  public static String toString(String name,
    int training_period,
    int forecast_period,
    float min_win_percentage_change,
    float max_loss_percentage_change) 
  {
    return name + "(" + training_period + "," + forecast_period + "," + 
      min_win_percentage_change + "," + max_loss_percentage_change + ")";
  }
  
  /**
   * Gets the name of the indicator.
   * 
   * @return Its name
   */
  public String getName()
  {
    return toString(_name, _training_period, 
        _forecast_period, _min_win_percentage_change,
        _max_loss_percentage_change);
  }
}
