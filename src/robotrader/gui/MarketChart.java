package robotrader.gui;

import java.awt.GridLayout;
import java.util.Date;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeriesCollection;

/**
 * This class manages the View of the market data. It shows a
 * chart of price information over the time
 * 
 * @author xcapt
 * @author klinst
 */
class MarketChart 
{
  /**
   * The time series data model
   */
	private TimeSeries _data;
  
  /**
   * The Panel holding the chart
   */
	private JPanel _pane = new JPanel();
  
  /**
   * The chart title
   */
	private String _title;
  
  /**
   * The timeseries xy chart
   */
	private JFreeChart _chart;
  
  /**
   * Whether the chart needs updating
   */
	private boolean _update = false;

  /**
   * Constructs a new Market Chart.  
   */
	MarketChart() 
  {
		_pane.setLayout(new GridLayout(1, 1));
	}

	/**
	 * Gets the JPanel associated with the object. Only one
	 * panel will be created per object. The JPanel will contain
	 * the line chart alimented by a dynamic data set.
	 * 
	 * @return Returns a JPanel containing the line chart
	 */
	public JPanel getPanel() 
  {
		if (_update) 
    {
			_pane.removeAll();
			_data = new TimeSeries(_title);

			TimeSeriesCollection dataset = new TimeSeriesCollection(_data);
			_chart =
				ChartFactory.createTimeSeriesChart(
					_title,
					"Time",
					"Quote",
					dataset,
					false,
					true,
					false);
			XYPlot plot = _chart.getXYPlot();
			ValueAxis axis = plot.getDomainAxis();
			axis.setAutoRange(true);
			_pane.add(new ChartPanel(_chart));
			_update = false;
		}

		return _pane;
	}

	/**
	 * used to update market data in the chart. Will update the
	 * chart dynamically.
	 * Should not be called twice for with the same day.
	 * 
	 * @param date date of the update
	 * @param price closing price of the day
	 */
	public void update(Date date, float price) 
  {
		_data.add(new Day(date), price);
	}

	/**
	 * Method init: initialize the chart.
	 * @param title chart title
	 */
	public void init(String title) 
  {
		_title = title;
		_update = true;
	}
}