package robotrader.market.indicator.impl.machinelearning;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.joone.engine.DelayLayer;
import org.joone.engine.FullSynapse;
import org.joone.engine.Layer;
import org.joone.engine.Monitor;
import org.joone.engine.NeuralNetListener;
import org.joone.engine.SigmoidLayer;
import org.joone.engine.Synapse;
import org.joone.engine.learning.TeachingSynapse;
import org.joone.io.MemoryInputSynapse;
import org.joone.io.MemoryOutputSynapse;
import org.joone.net.NeuralNet;
import org.joone.util.DynamicAnnealing;
import org.joone.util.MinMaxExtractorPlugIn;
import org.joone.util.MovingAveragePlugIn;
import org.joone.util.NormalizerPlugIn;

import robotrader.market.AbstractIndicator;
import robotrader.market.HistoricData;
import robotrader.market.IIndicator;

/**
 * An indicator based on the neural network
 * engine <A href="http://sourceforge.net/projects/joone">JOONE</A>.
 * Trying to predict the future prices using
 * historic prices as input to the network.
 *  
 * @author klinst
 */
public class JooneNeuralNetwork extends AbstractIndicator
	implements NeuralNetListener 
{
	/**
	 * The Logger
	 */
	private static final Logger trace = Logger.getLogger(JooneNeuralNetwork.class);
  
	/**
	 * The list of quotes
	 */
  private List _data = new ArrayList();

  /**
   * The number of training quotes
   */
  private int _training_rows = 150;
  
  /**
   * The number of forecast rows
   */
  private int _forecast_rows = 1;

  /**
   * Number of training epochs
   */
  private int _epochs = 1000;
  
  /**
   * The window used by
   * the delay layer
   */
  private int _temporal_window = 10;
  
  /**
   * The input layer
   */
  private DelayLayer _input_layer;
  
  /**
   * The rows of the hidden layers
   */
  private int[] _rows = { 20 };

  /**
   * The output layer
   */
  private SigmoidLayer output;
  
  /**
   * The teacher
   */
  private TeachingSynapse trainer;
  
  /**
   * The Neural Network
   */
  private NeuralNet nnet;

  /**
   * The minimum change for the MinMax Plugin
   * to recognise a turning point
   */
	private float _percentage_change;

	/**
	 * Default Constructor.
	 */
	public JooneNeuralNetwork()
	{
	}
	
  /**
   * Creates a new neural network.
   * 
   * @param layers The number of rows in each layer
   * @param training_patterns The number of training patterns
   * @param temporal_window The temporal window for prediction
   * @param change_percentage The percentage change for a turning point
   */
  public JooneNeuralNetwork(int[] layers, 
                            int training_patterns,
                            int temporal_window,
                            int epochs,
                            float change_percentage)
  {
  	this._rows = layers;
  	this._training_rows = training_patterns;
  	this._temporal_window = temporal_window;
    this._epochs = epochs;
  	this._percentage_change = change_percentage;
  	createNet();
  }
  
  /**
   * Get the name of this network
   */
	public String getName()
	{
		return toString(_rows, _training_rows, _temporal_window, _percentage_change);
	}

	/**
	 * Adds quotes to this trader
	 */
	public void add(HistoricData data)
	{
		_data.add(data);
    
    if (!_ready)
    {
      if (_data.size() >= _training_rows + _forecast_rows)
      {
      	double input[][] = new double[_training_rows][2];
        double output[][] = new double[_training_rows][1];
        	
      	for (int i = 0; i < _training_rows; i++)
      	{
      		HistoricData d = (HistoricData)_data.get(i);
      		input[i][0] = d.getAdjustedClose();
          input[i][1] = d.getVolume();
      		d = (HistoricData)_data.get(i + _forecast_rows);
      		output[i][0] = d.getAdjustedClose();
      	}
        
      	MemoryInputSynapse training = getMemoryInputSynapse("1,1,1,2");
      	training.addPlugIn(getNormalizerPlugin("1-4"));
      	training.addPlugIn(getMovingAveragePlugIn("2,3", "10,50"));
      	training.setInputArray(input);
      	training.setFirstRow(1);
      	training.setLastRow(_training_rows);
      	
      	MemoryInputSynapse desired = getMemoryInputSynapse("1");
        desired.addPlugIn(getNormalizerPlugin("1"));
      	desired.addPlugIn(getMinMaxPlugIn("1", _percentage_change));
      	desired.setInputArray(output);
      	desired.setFirstRow(1);
      	desired.setLastRow(_training_rows);

        _input_layer.addInputSynapse(training);
        trainer.setDesired(desired);

      	train();
      	_ready = true;
        _direction = IIndicator.HOLD;
        _data.clear();
      }      
    }
    else
    {
    	int size = _temporal_window;
    	
    	if (_data.size() >= size)
    	{
      	double input[][] = new double[size][2];
      	
      	for (int i = 0; i < size; i++)
      	{
      		HistoricData d = (HistoricData)_data.get(i);
      		input[i][0] = d.getAdjustedClose();
          input[i][1] = d.getVolume();
        }

      	_input_layer.removeAllInputs();
      	
      	MemoryInputSynapse recall = getMemoryInputSynapse("1,1,1,2");
      	NormalizerPlugIn plugin = getNormalizerPlugin("1-4");
      	recall.addPlugIn(plugin);
      	recall.addPlugIn(getMovingAveragePlugIn("2,3", "10,50"));
      	recall.setFirstRow(1);
      	recall.setLastRow(size);
      	recall.setInputArray(input);
      	_input_layer.addInputSynapse(recall);
      	
      	interrogate();
      	_data.remove(0);        
    	}
    }
	}
	
	/**
	 * Builds the network.
	 */
  private void createNet() 
  {	
    nnet = new NeuralNet();

    _input_layer = new DelayLayer();
    _input_layer.setTaps(_temporal_window-1);
    _input_layer.setRows(4);
    
    nnet.addLayer(_input_layer, NeuralNet.INPUT_LAYER);
    Layer previous = _input_layer;
    
    for (int i = 0; i < _rows.length; i++)
    {
    	SigmoidLayer hidden = new SigmoidLayer();
    	hidden.setRows(_rows[i]);
      nnet.addLayer(hidden, NeuralNet.HIDDEN_LAYER);
      connect(previous, new FullSynapse(), hidden);
      previous = hidden;
    }
    	    	
    output = new SigmoidLayer();
    output.setRows(1);
    connect(previous, new FullSynapse(), output);
    
    nnet.addLayer(output, NeuralNet.OUTPUT_LAYER);
    
    trainer = new TeachingSynapse();
    output.addOutputSynapse(trainer);
    setAnnealingPlugin();
  }

  /**
   * connects two layers with the synapse
   * @param layer1
   * @param syn
   * @param layer2
   */
  private void connect(Layer layer1, Synapse syn, Layer layer2) 
  {
    layer1.addOutputSynapse(syn);
    layer2.addInputSynapse(syn);
  }

  /**
   * trains the network
   *
   */
  private void train() 
  {
    Monitor mon = nnet.getMonitor();
    mon.setLearningRate(0.7);
    mon.setMomentum(0.6);
    mon.setTrainingPatterns(_training_rows);
    mon.setTotCicles(_epochs);
    mon.setPreLearning(_temporal_window);
    mon.setLearning(true);
    mon.addNeuralNetListener(this);

    nnet.start();
    mon.Go();
    nnet.join();
  }

  /**
   * 
   */
  public void cicleTerminated(org.joone.engine.NeuralNetEvent e) 
  {
    Monitor mon = (Monitor)e.getSource();
    int epoch = mon.getTotCicles() - mon.getCurrentCicle();
    if ((epoch > 0) && ((epoch % 100) == 0)) 
    {
        System.out.println("Epoch:"+epoch+" RMSE="+mon.getGlobalError());
    }
  }

  public void errorChanged(org.joone.engine.NeuralNetEvent e) 
  {
  }

  public void netStarted(org.joone.engine.NeuralNetEvent e) 
  {
  }

  public void netStopped(org.joone.engine.NeuralNetEvent e) 
  {
    Monitor mon = (Monitor)e.getSource();
    if (mon.isLearning()) 
    {
      int epoch = mon.getTotCicles() - mon.getCurrentCicle();
      trace.info("Epoch:"+epoch+" last RMSE="+mon.getGlobalError());
    }
  }

  public void netStoppedError(org.joone.engine.NeuralNetEvent e, String error) 
  {
  	trace.error("Error occurred");
  }

  /**
   * Predicts the turning points.
   */
  private void interrogate() 
  {
    Monitor mon = nnet.getMonitor();
    
    output.removeAllOutputs();
    MemoryOutputSynapse result = new MemoryOutputSynapse();
    output.addOutputSynapse(result);
    
    //mon.setTrainingPatterns(_temporal_window);
    mon.setTotCicles(1);
    mon.setLearning(false);
   
    nnet.start();
    mon.Go();
    nnet.join();
    
    double d[] = output.getLastOutputs();
    
    if (d != null && d.length > 0)
    {
    	if (d[0] < .1)
    	{
    		_direction = IIndicator.BUY;
    		
    	}
    	else if(d[0] > .9)
    	{
    		_direction = IIndicator.SELL;
    	}
    	else
    	{
    		_direction = IIndicator.HOLD;
    	}
    }
  }

  /**
   * Get the string representation.
   * 
   * @param layers The rows in each layer
   * @param training_patterns The number of patterns
   * @param temporal_window The window
   * @param percentage_change The % to indicate turning point
   * @return
   */
	public static String toString(int[] layers, 
    int training_patterns, int temporal_window,
    float percentage_change)
	{
		return "Joone " + layers.length + " " + 
			training_patterns + " " + temporal_window +
			" " + percentage_change;
	}

	/**
	 * creates a normalizer for the given series
   * 
	 * @param series
	 * @return
	 */
	private NormalizerPlugIn getNormalizerPlugin(String series)
	{
		NormalizerPlugIn plugin = new NormalizerPlugIn();
  	plugin.setAdvancedSerieSelector(series);
  	plugin.setMax(1.0);
  	plugin.setMin(0.0);
    plugin.setName("Normalizer");
  	return plugin;
	}
	
	/**
	 * creates a moving average plugin for the series and
   * specification
   * 
	 * @param series
	 * @param specification
	 * @return
	 */
	private MovingAveragePlugIn getMovingAveragePlugIn(String series,
			String specification)
	{
		MovingAveragePlugIn plugin = new MovingAveragePlugIn();
  	plugin.setAdvancedSerieSelector(series);
  	plugin.setAdvancedMovAvgSpec(specification);
    plugin.setName("MovingAverage");
  	return plugin;
	}
	
	/**
	 * 
	 * @param series
	 * @param percentage
	 * @return
	 */
	private MinMaxExtractorPlugIn getMinMaxPlugIn(String series,
			float percentage)
	{
		MinMaxExtractorPlugIn plugin = new MinMaxExtractorPlugIn();
  	plugin.setAdvancedSerieSelector(series);
  	plugin.setMinChangePercentage(percentage);
    plugin.setName("MinMax");
  	return plugin;
	}
	
	/**
	 * 
	 * @param column
	 * @return
	 */
	private MemoryInputSynapse getMemoryInputSynapse(String column)
	{
		MemoryInputSynapse synapse = new MemoryInputSynapse();
  	synapse.setAdvancedColumnSelector(column);
  	return synapse;
	}
	
	/**
	 * 
	 *
	 */
	private void setAnnealingPlugin()
	{
		DynamicAnnealing a =  new DynamicAnnealing();
		a.setRate(5);
		a.setStep(15);
		a.setNeuralNet(nnet);
	}
}
