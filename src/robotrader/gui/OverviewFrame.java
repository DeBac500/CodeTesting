package robotrader.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Date;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;

import lu.base.iris.AppToolkit;
import lu.base.iris.services.UserPrefService;

import robotrader.engine.event.Event;
import robotrader.engine.event.IObserver;
import robotrader.engine.model.IEngine;
import robotrader.quotedb.IQuoteRepository;
import robotrader.stats.DataModel;
import robotrader.trader.AbstractTrader;

/**
 * OverviewFrame description: main frame for the 
 * application. Provides a market pricing chart, 
 * the trader evaluation chart, the trader yearly
 * change in their prositions and a report for
 * all the traders.
 * 
 * @author xcapt
 * @author klinst
*/
public class OverviewFrame extends JFrame implements IObserver 
{
  /**
   * The Serial ID
   */
  private static final long serialVersionUID = -177913974084529650L;

  /**
   * The trading statistics data model
   */
	private DataModel _model;
  
  /**
   * The repository of quotes
   */
	private IQuoteRepository _quotedb;
  
  /**
   * The current date
   */
	private Date _currdate = null;
  
  /**
   * The previous date
   */
	private Date _prevdate = null;
  
  /**
   * The main engine used for processing
   */
	private IEngine _engine;
  
  /**
   * The market panel
   */
	private JPanel _marketpane;
  
  /**
   * The market output for this overview.
   */
	private MarketOutput _marketoutput = new MarketOutput(true);
  
  /**
   * The market pricing chart
   */
	private MarketChart _mchart;
  
  /**
   * The Trader profit char
   */
	private ProfitChart _pchart;
  
  /**
   * The instrument the trading is based on
   */
	private String _instrument;
  
  /**
   * The profit and loss trader chart
   */
	private TraderChart _tchart;
  
  /**
   * The trader names
   */
	private String[] _titles;
  
  /**
   * The number of market updates (
   * i.e. quotes) 
   */
	private long _updtcount = 0;
  
  /**
   * The refreshing rate for the
   * market pricing
   */
	private long _updtmarket = 5;
  
  /**
   * The refreshing rate for the
   * trader evaluation
   */
	private long _updttrader = 10;
	
	private boolean _hideChartsDuringSim = false;

	/**
	 * Creates a new OverviewFrame object.
	 * @param engine robotrader core engine
	 * @param quotedb quote repository
	 */
	public OverviewFrame(IEngine engine, IQuoteRepository quotedb, DataModel model) {
		setTitle("robotrader");
		_engine = engine;
		_model = model;
		_engine.setObserver(_model);
		_model.addObserver(this);
		_quotedb = quotedb;
	}

	/**
	 * Method getPanel: retrieves the main panel component.
	 * @return JPanel panel component
	 */
	public JPanel getPanel() {
		if (_marketpane == null) {
			
			JPanel top = new JPanel(new BorderLayout());
			JPanel bottom = new JPanel(new GridLayout(1,2));
			
			top.add(getProfitChart().getPanel(), BorderLayout.CENTER);
			bottom.add(getMarketChart().getPanel());
			bottom.add(getTraderChart().getPanel());
			JPanel overviewFrame = new JPanel(new GridLayout(2,1));
			overviewFrame.add(top);
			overviewFrame.add(bottom);
			
			_marketpane = new JPanel(new BorderLayout());
			_marketpane.removeAll();
			_marketpane.add(overviewFrame);
			_marketpane.add(new EngineControlPane(_engine, _quotedb),
								BorderLayout.NORTH);
		}

		return _marketpane;
	}

	/**
	 * Method init: initializes the frame content.
	 */
	public void init() {
		// init components
		getContentPane().add(getPanel());
		pack();
	}

	
	/**
   * initialises all the charts.
	 * @see robotrader.engine.event.IObserver#onStarted()
	 */
	public void onStarted() {
		_marketoutput.clear();
		_instrument = _engine.getInstrument();
		// compute refresh rates
		int datasize = _model.getDataSize();

		if (datasize > 0) {
			_updtmarket = (datasize / 200) + 1;
			_updttrader = (datasize / 200) + 1;
		}

		// create traders titls table
		_titles = _model.getTitles();

		// update market pane
		getMarketChart().init("Market " + _instrument);
		getMarketChart().getPanel();
		// update profit pane
		getProfitChart().init(_titles);
		getProfitChart().getPanel();
		// update trader chart
		getTraderChart().init("Traders Evaluation", _titles);
		getTraderChart().getPanel();

		
		_hideChartsDuringSim = isChartHidingEnabled();
		if(_hideChartsDuringSim){
			getMarketChart().getPanel().setVisible(false);
			getTraderChart().getPanel().setVisible(false);
			getProfitChart().getPanel().setVisible(false);
			
			/*_simulationProgressBar = new JProgressBar(0, 1000);
			_simulationProgressBar.setValue(0);

			if(barPanel == null){
				barPanel = new JPanel();
				barPanel.add(_simulationProgressBar);
			}
				getPanel().add(barPanel);
			
			barPanel.setVisible(true);*/
		}
	}

	
	/**
   * Updates the yearly report and ranks the
   * traders.
   * 
	 * @see robotrader.engine.event.IObserver#onStopped()
	 */
	public void onStopped() {
		if(_hideChartsDuringSim){
			getMarketChart().getPanel().setVisible(true);
			getTraderChart().getPanel().setVisible(true);
			getProfitChart().getPanel().setVisible(true);
		}
		
		updateYearlyReport(true);
		_marketoutput.rank(_instrument,_model);
		getProfitChart().addTextSummary(_marketoutput.getOutput(), _marketoutput.isHTMLOutput());
	}

	/**
   * Updates the market and trader charts. If the
   * year has changed, a yearly report is produced.
   * 
	 * @see robotrader.engine.event.IObserver#onUpdate(robotrader.engine.event.Event)
	 */
	public void onUpdate(Event evt) {
		_currdate = _model.getDate();
		_updtcount++;
		updateMarket();
		updateTraders();

		/*if(_hideChartsDuringSim){
			barPanel.setVisible(true);
			_simulationProgressBar.setValue((int)(((double)_updtcount/(double)_model.getDataSize())*1000));
		}*/
		
		if (evt.isYearEvent()) {
			updateYearlyReport(false);
		}

		_prevdate = _currdate;
	}
	/**
   * Gets or creates the market chart.
   * 
   * @return The trader chart
   */
	protected MarketChart getMarketChart(){
		if (_mchart == null) {
			_mchart = new MarketChart();
		}

		return _mchart;
	}

  /**
   * Gets or creates the profit chart.
   * 
   * @return The profit chart
   */
	private ProfitChart getProfitChart() 
	{
		if (_pchart == null) {
			_pchart = new ProfitChart();
		}

		return _pchart;
	}

  /**
   * Gets or creates the trader chart.
   * 
   * @return The trader chart
   */
	protected TraderChart getTraderChart(){
		if (_tchart == null) {
			_tchart = new TraderChart();
		}

		return _tchart;
	}

  /**
   * Updates the market chart if the
   * refresh cycle has been completed.
   */
	protected void updateMarket() 
  {
		if ((_updtcount % _updtmarket) == 0) 
    {
			_mchart.update(
				_model.getMarket().getDate(),
				_model.getMarket().getAdjustedClose(_instrument));
		}
	}

  /**
   * If the refresh cycle has been completed, then
   * update the trader chart with the current
   * evaluation of the positions for each of the
   * traders.
   */
	protected void updateTraders() 
  {
		if ((_updtcount % _updttrader) == 0){
			int i = 0;

			for (Iterator it = _model.getTraders().iterator(); it.hasNext();) {
				_tchart.update(
					i,
					_model.getDate(),
					((AbstractTrader) it.next()).getEvaluation());
				i++;
			}
		}
	}

  /**
   * Gets the yearly percentage change in portfolio
   * for each of the traders. Updates the profit
   * chart with the changes for each of the traders.
   * 
   * @param complete Is the year complete
   */
	private void updateYearlyReport(boolean stopped) {
		if (_prevdate != null) {
			int pyear = _model.getYear() + (stopped ? 0 : -1);
			float[] yearvalues = new float[_model.getTraders().size()];
			for (int i = 0; i < _titles.length; i++) {
				yearvalues[i] = _model.getYearReport().getYearChange(i);
			}
			_marketoutput.appendYearEndReport(stopped, _titles, pyear, yearvalues);
			getProfitChart().add(pyear, yearvalues, stopped);
		}
	}
	
	/**
	 * Method to retrieve user defined option of hiding charts.
	 * 
	 * @return
	 */
	protected boolean isChartHidingEnabled(){
		UserPrefService ups = (UserPrefService)AppToolkit.getService(UserPrefService.class);
		if(ups != null){
			String curVal = ups.getStringValue("Options.HideChartsDuringSimulation", Boolean.toString(false));
			return Boolean.valueOf(curVal).booleanValue();
		}
		return false;
	}
}