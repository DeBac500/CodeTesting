package robotrader.gui;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLEditorKit;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;

/**
 * Yearly chart of every trader profit over the year, a new Tab in the
 * JTaggedPane is created for each yearly report.
 * 
 * @author xcapt
 * @author klinst
 */
class ProfitChart 
{
  /**
   * The panel holding all yearly profit
   * tabbed panes
   */
	private JPanel _pane = new JPanel();
  
  /**
   * Whether the chart needs to be updated
   */
	private boolean _update = false;
  
  /**
   * The current tabbed pane with the profit
   * chart for the year
   */
	private JTabbedPane _tpane;
  
  /**
   * The names of the traders 
   */
	private String[] _names;

  /**
   * Constructs a new profit chart.
   */
	ProfitChart() {
		_pane.setLayout(new GridLayout(1, 1));
	}

	/**
	 * Method init.
	 * @param names array of trader names
	 */
	public void init(String[] names) {
		_names = names;
		_update = true;
	}

	/**
	 * Method getPanel.
	 * @return JPanel retrieve panel object
	 */
	public JPanel getPanel() {
		if (_update) {
			_pane.removeAll();
			_tpane = new JTabbedPane();
			_pane.add(_tpane);
			_update = false;
		}
		return _pane;
	}

	/**
	 * Method add: create a new bar chart for the current year.
	 * @param year current year
	 * @param values array of %pnl for the year, the values must be be ordered
	 * as in the init() method to have the correct vaule for every given trader
	 * name
	 * @param complete false when the current year was not completed (stopped)
	 */
	public void add(int year, float[] values, boolean stopped) {
		String title = Integer.toString(year) + (stopped ? "(stopped)" : "");
		ChartPanel p = makeYearChart(title, values);

		_tpane.addTab(title, p);
	}
	
	/**
	 * Add a summary tab that contains a text pane with the specified text.
	 * @param text
	 * @param HTML Boolean indicating whether or not to make the text pane recognize HTML
	 */
	public void addTextSummary(String text, boolean HTML){
		JTextPane jtp = new JTextPane();
		if(HTML) jtp.setEditorKit(new HTMLEditorKit());
		jtp.setText(text);
		JScrollPane jsp = new JScrollPane(jtp);
		_tpane.add("Summary", jsp);
		
	}

  /**
   * Creates a new bar chart for a year. The domain axis
   * is populated with the traders and the range is the
   * percentage of profit and loss. 
   * 
   * @param title The title of the panel
   * @param values Array of %pnl for the year
   * @return The chart panel with the pnl bar charts
   */
	private ChartPanel makeYearChart(String title, float[] values) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < _names.length; i++) {
			dataset.addValue(values[i], _names[i], "");
		}
    
		JFreeChart chart =
			ChartFactory.createBarChart(title, // chart title
					null, //"traders", // domain axis label
					"% pnl", // range axis label
					dataset, // data
					PlotOrientation.VERTICAL, // orientation
					true, // do not include legend
					true, // include tooltips
					false); // include URLs

		ChartPanel cp = new ChartPanel(chart);
    
		LegendTitle ltitle = chart.getLegend();
		ltitle.setItemFont(new Font("Arial", Font.PLAIN, 8));
		ltitle.setPosition(RectangleEdge.RIGHT);

		return cp;
	}
}