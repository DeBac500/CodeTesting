package robotrader.trader.impl.machinelearning;

import java.util.StringTokenizer;

import robotrader.engine.model.PropertyMetaData;
import robotrader.market.indicator.impl.machinelearning.JooneNeuralNetwork;
import robotrader.trader.indicator.AbstractIndicatorTrader;

/**
 * Creates a trader for the joone 
 * neural network indicator.
 * 
 * @see robotrader.market.indicator.impl.machinelearning.JooneNeuralNetwork
 * @author klinst
 */
public class JooneNeuralNetworkTrader extends AbstractIndicatorTrader 
{
	/**
	 * The default training period
	 */
	private int _period = 150;
	
	/**
	 * The change for a turning point
	 */
	private float _percentage_change = 0.05f;
	
	/**
	 * The temporal window of training
	 * patterns
	 */
	private int _temporal_window = 10;
	
  private int _epochs = 1000;
  
	/**
	 * The number of columns in each row
	 * of (sigmoid) neurons
	 */
	private int[] _rows = { 
			20 
	};

  /**
   * The Metadata information for the
   * properties of this trader.
   */
  private static final PropertyMetaData[] METADATA =
    new PropertyMetaData[] {
      new PropertyMetaData("PERCENTAGE_CHANGE", PropertyMetaData.TYPE_DOUBLE),
      new PropertyMetaData("TRAINING_PERIOD", PropertyMetaData.TYPE_INTEGER),
      new PropertyMetaData("TEMPORAL_WINDOW", PropertyMetaData.TYPE_INTEGER),
      new PropertyMetaData("EPOCHS", PropertyMetaData.TYPE_INTEGER),
      new PropertyMetaData("LAYERS", PropertyMetaData.TYPE_INTARRAY)
      };
  /**
   * retrieve meta data for trader properties
   */
  public PropertyMetaData[] getPropertyMetaData() {
    return METADATA;
  };

	/**
	 * Creates a new JooneNeuralNetworkTrader object.
	 */
	public JooneNeuralNetworkTrader() {
	}

	/**
	 * Initialse the trader by creating
	 * and registering the indicator
	 * with the indicator container.
	 */
	public void init() 
  {
		String key = JooneNeuralNetwork.toString(_rows, _period, _temporal_window, _percentage_change);
		JooneNeuralNetwork indic =
			(JooneNeuralNetwork) getMarket().getIndicatorContainer().get(key);
		if (indic == null) {
			indic = new JooneNeuralNetwork(_rows, _period, 
				_temporal_window, _epochs, _percentage_change);
			getMarket().getIndicatorContainer().register(key, indic);
		}
		setIndicator(indic);
	}

	/**
	 * Get the properties from the xml file.
	 * The properties to set are
	 * <ul>
	 * <li>PERCENTAGE_CHANGE</li>
	 * <li>TRAINING_PERIOD</li>
	 * <li>TEMPORAL_WINDOW</li>
	 * <li>LAYERS</li>
	 * </ul>
	 */
	public void setProperty(String key, String value) 
	{
		if (key.equals("PERCENTAGE_CHANGE")) 
		{
			_percentage_change = Float.parseFloat(value);			
		}
		
		if (key.equals("TRAINING_PERIOD"))
		{
			_period = Integer.parseInt(value);
		}
		
		if (key.equals("TEMPORAL_WINDOW"))
		{
			_temporal_window = Integer.parseInt(value);
		}
    
    if (key.equals("EPOCHS"))
    {
      _epochs = Integer.parseInt(value);
    }
		
		if (key.equals("LAYERS"))
		{
			StringTokenizer st = new StringTokenizer(value, ",; ");
			_rows = new int[st.countTokens()];
			int i = 0;
			
			while (st.hasMoreTokens())
			{
				_rows[i++] = Integer.parseInt(st.nextToken());
			}
		}
	}
  
  public String getProperty(String key) 
  {
    if (key.equals("PERCENTAGE_CHANGE")) 
    {
      return Float.toString(_percentage_change);     
    }
    
    if (key.equals("TRAINING_PERIOD"))
    {
      return Integer.toString(_period);
    }
    
    if (key.equals("TEMPORAL_WINDOW"))
    {
      return Integer.toString(_temporal_window);
    }
    
    if (key.equals("EPOCHS"))
    {
      return Integer.toString(_epochs);
    }
    
    if (key.equals("LAYERS"))
    {
      StringBuffer sb = new StringBuffer();
      
      for (int i = 0; i < _rows.length; i++)
      {
        sb.append(Integer.toString(_rows[i]));
        
        if (i < _rows.length - 1)
        {
          sb.append(",");
        }
      }

      return sb.toString();
    }
    
    return "10";
  }
	
	/**
	 * Get the name of the trader
	 */
	public String getName() 
	{
		return JooneNeuralNetwork.toString(_rows, _period, _temporal_window, _percentage_change);
	}
  
  public String toString()
  {
    return "Joone Neural Net";
  }
}