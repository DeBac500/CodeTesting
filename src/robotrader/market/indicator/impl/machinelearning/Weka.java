package robotrader.market.indicator.impl.machinelearning;

import java.util.Properties;

import robotrader.market.AbstractIndicator;
import robotrader.market.HistoricData;
import robotrader.market.IIndicator;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.ProtectedProperties;

/**
 * Generic machine learning indicator that uses
 * the WEKA library.  
 *  
 * @author klinst
 */
public class Weka extends AbstractIndicator
{
  /**
   * The classifier used for classification
   */
  private Classifier _classifier;
  
  /**
   * The attribute describing the price
   */
  private Attribute _price_attribute;
  
  /**
   * The attribute describing the date
   */
  private Attribute _period_attribute;
  
  /**
   * The learning instances
   */
  private Instances _instances;
  
  /**
   * The number of dates for learning
   */
  private int _periods;
  
  /**
   * The current date
   */
  private int _current_period;
  
  /**
   * The minimum change required 
   */
  private float _percentage_change;
  
  /**
   * The number of dates to look into
   * the future
   */
  private int _forecast_period;
  
  /**
   * Construct Weka. 
   * 
   * @param classifier The classifier used
   * @param periods The periods used for learning
   * @param forecast The periods to look into the future
   * @param change The minimum required change
   */
  public Weka(Classifier classifier, int periods, int forecast, float change)
  {
    Properties props = new Properties();
    ProtectedProperties properties = new ProtectedProperties(props);
    
    _period_attribute = new Attribute("period", properties);
    _price_attribute = new Attribute("adjusted_close");
    _classifier = classifier;
    
    FastVector vector = new FastVector();
    vector.addElement(_period_attribute);
    vector.addElement(_price_attribute);
    
    _instances = new Instances("prices", vector, periods);
    _instances.setClassIndex(_instances.numAttributes() - 1); 
    
    _periods = periods;
    _current_period = 0;
    _percentage_change = change;
    _forecast_period = forecast;
  }
  
  /**
   * get its name 
   */
  public String getName()
  {
    return "Weka";
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
    float percentage_change) 
  {
    return name + "(" + training_period + "," + forecast_period + "," + percentage_change + ")";
  }

  /**
   * Train the classifier.
   */
  public void add(HistoricData data)
  {
    Instance instance = new Instance(2);
    instance.setValue(_period_attribute, _current_period);
    instance.setValue(_price_attribute, data.getAdjustedClose());
  
    instance.setDataset(_instances);
    _instances.add(instance);

    if (!_ready)
    {      
      if (_current_period == _periods)
      {
        _direction = IIndicator.HOLD;
        _ready = true;
      }
    }
    else
    {
      try
      {
        instance = new Instance(2);
        instance.setValue(_period_attribute, _current_period + _forecast_period);

        _classifier.buildClassifier(_instances);
        double prediction = _classifier.classifyInstance(instance);
        
        float current_price = data.getAdjustedClose();
        
        if (prediction > (current_price + (current_price * _percentage_change)))
        {
          _direction = IIndicator.BUY;
        }
        else if (prediction < (current_price - (current_price * _percentage_change)))
        {
          _direction = IIndicator.SELL;
        }
        else
        {
          _direction = IIndicator.HOLD;
        }
        
        _instances.delete(0);
      }
      catch (Exception e)
      {
        System.out.println("Exception!" + e.getMessage());
      }    
    }

    _current_period++;
  }
}
