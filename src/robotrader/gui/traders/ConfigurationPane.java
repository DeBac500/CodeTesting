package robotrader.gui.traders;

import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import robotrader.engine.model.PropertyMetaData;
import robotrader.trader.AbstractTrader;
import robotrader.trader.impl.CautiousStreakFollower;
import robotrader.trader.impl.CostAverageTrader;
import robotrader.trader.impl.Follower;
import robotrader.trader.impl.Keeper;
import robotrader.trader.impl.StreakFollower;
import robotrader.trader.impl.TurningPointTrader;
import robotrader.trader.impl.WinFollower;
import robotrader.trader.impl.machinelearning.DataFusionTrader;
import robotrader.trader.impl.machinelearning.JooneNeuralNetworkTrader;
import robotrader.trader.impl.machinelearning.LinearRegressionTrader;
import robotrader.trader.impl.machinelearning.PolynomialRegressionTrader;
import robotrader.trader.impl.machinelearning.RegressionTrader;
import robotrader.trader.impl.machinelearning.SMOTrader;
import robotrader.trader.impl.machinelearning.SimpleRegressionTrader;
import robotrader.trader.indicator.DeMarkerTrader;
import robotrader.trader.indicator.MACDTrader;
import robotrader.trader.indicator.MFITrader;
import robotrader.trader.indicator.MovingAverageTrader;
import robotrader.trader.indicator.OBVTrader;
import robotrader.trader.indicator.ROCTrader;
import robotrader.trader.indicator.RSITrader;
import robotrader.trader.indicator.WillRTrader;

/**
 * ImportPane description: Swing panel to configure the importation of quotes .
 * <br>
 */
public class ConfigurationPane extends JPanel 
  implements ItemListener
{
	/**
	 * The required serial version UID
	 */
	private static final long serialVersionUID = -8000268323387348674L;

  /**
   * The maximum number of propery fields
   */
  private static final int NUM_PROPERTIES = 5;
  
	/**
	 * The Combo box for the available loaders
	 */
	private JComboBox _tradercombo;

  /**
   * The available traders
   */
  private AbstractTrader[] _traders;
  
  /**
   * The selected trader
   */
  private AbstractTrader _trader;
  
  /**
   * The property labels
   */
  private JLabel[] _labels;
  
  /**
   * The property texts
   */
  private JTextField[] _text;
  
	/**
	 * ConfigurationPane constructor: creates the panel
	 */
	public ConfigurationPane() 
  {
    initTraders();
		initComponents();
	}
  
  /**
   * Constructs the Pane with a particular trader
   * @param trader
   */
  public ConfigurationPane(AbstractTrader trader)
  {
    _traders = new AbstractTrader[1];
    _traders[0] = trader;
    _trader = trader;
    initComponents();
  }

  /**
   * Create all the available 
   * traders.
   */
  private void initTraders()
  {
    _traders = new AbstractTrader[22];
    
    _traders[0] = new Keeper();
    _traders[1] = new Follower();
    _traders[2] = new WinFollower();
    _traders[3] = new StreakFollower();
    _traders[4] = new CautiousStreakFollower();
    _traders[5] = new CostAverageTrader();
    _traders[6] = new TurningPointTrader();
    _traders[7] = new DeMarkerTrader();
    _traders[8] = new MACDTrader();
    _traders[9] = new MFITrader();
    _traders[10] = new MovingAverageTrader();
    _traders[11] = new OBVTrader();
    _traders[12] = new ROCTrader();
    _traders[13] = new RSITrader();
    _traders[14] = new WillRTrader();
    _traders[15] = new DataFusionTrader();
    _traders[16] = new JooneNeuralNetworkTrader();
    _traders[17] = new RegressionTrader();
    _traders[18] = new PolynomialRegressionTrader();
    _traders[19] = new SimpleRegressionTrader();
    _traders[20] = new LinearRegressionTrader();
    _traders[21] = new SMOTrader();
  }
  
	/**
	 * Method initComponents: creates the components embedded in the panel.
	 */
	private void initComponents() 
	{   
    DefaultComboBoxModel model = new DefaultComboBoxModel(_traders);
    _tradercombo = new JComboBox(model);
    _tradercombo.addItemListener(this);

		this.setLayout(new GridLayout(0, 2));
    this.add(new JLabel("Trader"));
    this.add(_tradercombo);
    
    _labels = new JLabel[NUM_PROPERTIES];
    _text = new JTextField[NUM_PROPERTIES];
    
    for (int i = 0; i < NUM_PROPERTIES; i++)
    {
      _labels[i] = new JLabel("Property" + i + "    ");
      _text[i] = new JTextField("");
      _text[i].setEditable(false);
      this.add(_labels[i]);
      this.add(_text[i]);
    } 

    _tradercombo.setSelectedIndex(0);
    changeProperties(_traders[0]);
  }
  
  /**
   * If another list item has been selected,
   * change the property labels 
   */
  public void itemStateChanged(ItemEvent e)
  {   
    AbstractTrader trader = (AbstractTrader)e.getItem();
    changeProperties(trader);
  }
  
  /**
   * Change the property labels for the trader
   * 
   * @param trader
   */
  private void changeProperties(AbstractTrader trader)
  {
    PropertyMetaData[] data = trader.getPropertyMetaData();

    int length = Math.min(data.length, _labels.length);
    
    for (int i = 0; i < length; i++)
    {
      _labels[i].setText(data[i].getKey());
      _text[i].setText(trader.getProperty(data[i].getKey()));
      _text[i].setEditable(true);
    }
    
    for (int i = length; i < _labels.length; i++)
    {
      _labels[i].setText("Property" + i + "    ");
      _text[i].setText("");
      _text[i].setEditable(false);
    }    
  }
  
  /**
   * Get the Trader that had its properties changed
   * @return
   * @throws Exception
   */
  public AbstractTrader getChangedTrader() throws Exception
  {
    if (_trader == null)
    {
      AbstractTrader trader = (AbstractTrader)_tradercombo.getSelectedItem();
      Class tclass = trader.getClass();
      _trader = (AbstractTrader) tclass.newInstance();  
    }
    
    PropertyMetaData[] data = _trader.getPropertyMetaData();

    int length = Math.min(data.length, _labels.length);
    
    for (int i = 0; i < length; i++)
    {
      String key = _labels[i].getText();
      String value = _text[i].getText();
      _trader.setProperty(key, value);
    }
    
    return _trader;
  }
}
