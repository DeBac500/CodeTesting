package robotrader.gui;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.Date;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

/**
 * Chart of comparison of competing trader
 * evaluation(cash+position) over
 * time (day after day...well, given a refreshing rate).
 * 
 * @author xcapt
 * @author klinst
 */
class TraderChart 
{
  /**
   * The main chart panel
   */
	private JPanel _pane = new JPanel();
  
  /**
   * Has the panel been updated
   */
	private boolean _update = false;
  
  /**
   * The title of the chart
   */
	private String _title;
  
  /**
   * The timeseries for the traders
   */
	private TimeSeries[] _data;

	TraderChart() 
  {
		_pane.setLayout(new GridLayout(1, 1));
	}

	/**
	 * Method init: initialize chart .
	 * @param title chart title
	 * @param titles trader names used in the legend.
	 */
	public void init(String title, String[] titles) 
  {
		_update = true;
		_title = title;
		_data = new TimeSeries[titles.length];

		for (int i = 0; i < _data.length; i++) 
    {
			_data[i] = new TimeSeries(titles[i]);
		}
	}

	/**
	 * Method getPanel. Adds all the traders to
   * the collection of time series and creates
   * the chart. Adds the chart panel to the 
   * JPanel and returns it.
   * 
	 * @return JPanel retrieve the panel object
	 */
	public JPanel getPanel() 
  {
		if (_update) 
    {
			_pane.removeAll();
			TimeSeriesCollection dataset = new TimeSeriesCollection();

			for (int i = 0; i < _data.length; i++) 
      {
				dataset.addSeries(_data[i]);
			}

			JFreeChart chart =
				ChartFactory.createTimeSeriesChart(
					_title,
					"Time",
					"Eval",
					dataset,
					true,
					true,
					false);
			XYPlot plot = chart.getXYPlot();
			ValueAxis axis = plot.getDomainAxis();
			axis.setAutoRange(true);

      LegendTitle title = chart.getLegend();
      title.setItemFont(new Font("Arial", Font.PLAIN, 8));
      
			_pane.add(new ChartPanel(chart));
			_update = false;
		}

		return _pane;
	}

	/**
	 * used to update market data in the chart. Will update the
	 * chart dynamically.
	 * Should not be called twice for with the same day.
	 * 
	 * @param index index of the trader in the trader list
	 * @param date date of the update
	 * @param evaluation evaluation of trader at this date
	 */
	public void update(int index, Date date, double evaluation) 
  {
		_data[index].add(new Day(date), evaluation);
	}
}