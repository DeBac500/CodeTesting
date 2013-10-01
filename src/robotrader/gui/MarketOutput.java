package robotrader.gui;

import java.io.StringWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import robotrader.stats.DataModel;
import robotrader.stats.TraderStats;
import robotrader.stats.TransactionsReport;
import robotrader.trader.AbstractTrader;


/**
 * A class for outputting market information including trader performance,
 * trader ranking, etc.
 * @author cycle
 *
 */
public class MarketOutput{
	private static final long serialVersionUID = -8919202368065439799L;
	/** 
	 * The number format for the current locale 
	 */
	private NumberFormat _nf = NumberFormat.getInstance();
	/** 
	 * The output string returned in the text pane
	 */
	private StringWriter _output = new StringWriter();
	/**
	 * Boolean to determine if output is written with html or not.
	 */
	private boolean outputHTML = false;
	
	/**
	 * Tags used in output. If outputHTML is true, these are defined
	 * as HTML tags instead of empty strings.
	 */
	private String yearStyle, traderStyle, sectionStyle;
	private String yearOpenTag = "";
	private String traderOpenTag = "";
	private String sectionOpenTag = "";
	private String closeTag = "";
	
	
	public MarketOutput(boolean HTML){
		outputHTML = HTML;
		if(outputHTML){
			defineTags();
			_output.write("<html>");
		}
		_nf.setMinimumFractionDigits(1);
		_nf.setMaximumFractionDigits(1);
	}
	
	/**
	 * Returns true if outputting in HTML, false otherwise.
	 */
	public boolean isHTMLOutput(){
		return outputHTML;
	}
	/**
	 * Set output for HTML
	 * @param HTML
	 */
	public void setHTMLOutput(boolean HTML){
		outputHTML = HTML;
	}
	/**
	 * Clear output.
	 */
	public void clear(){
		_output = new StringWriter();
		if(outputHTML){
			_output.write("<html>");
		}
	}
	/**
	 * Appends the text and a new line to output.
	 * @param text
	 */
	public void writeln(String text){
		if(outputHTML){
			_output.write(text+"<br>");	
		}
		else{
			_output.write(text+"\n");
		}
	}
	
	/**
	 * Get the output.
	 * @return
	 */
	public String getOutput(){
		if(outputHTML){
			return _output.toString()+"</html>";
		}
		else{
			return _output.toString();
		}
	}
	
	/**
	 * Set the style strings to actual styles.
	 */
	private void defineStyles(){
		yearStyle = "font-size:18pt; font-weight:bold;";
		traderStyle = "font-size:14pt; font-style:italic;";
		sectionStyle = "font-size:22pt; font-weight:bold;";
	}
	/**
	 * Set the HTML tags. This function should only
	 * be called if outputHTML is true.
	 */
	private void defineTags(){
		defineStyles();
		yearOpenTag = "<span style=\""+yearStyle+"\">";
		traderOpenTag = "<span style=\""+traderStyle+"\">";
		sectionOpenTag = "<span style=\""+sectionStyle+"\">";
		closeTag = "</span>";
	}
	
   /**
	* Gets the comparator for the profit and
	* loss of the traders.
	* 
	* @return The P&L comparator
	*/
	public static Comparator getPnl() {
		return new Comparator(){
			public boolean equals(Object obj) {
				return false;
			}

			public int compare(Object o1, Object o2){
					AbstractTrader t2 = (AbstractTrader) o2;
					AbstractTrader t1 = (AbstractTrader) o1;

					return Double.compare(
					    t2.getPnL(),
					    t1.getPnL());

			}
		};
	}

  /**
   * Gets the comparator for the profit and
   * loss per transaction.
   * 
   * @return The comparator for the Profit and Loss
   * per Transaction
   */
	private Comparator getPnlPerTrans(){
		return new Comparator() {
			public boolean equals(Object obj){
				return false;
			}

			public int compare(Object o1, Object o2) {
				AbstractTrader t2 = (AbstractTrader) o2;
				AbstractTrader t1 = (AbstractTrader) o1;

				if ((t1.getTransactionCount() == 0)
					&& (t2.getTransactionCount() == 0)) {
					return 0;
				}

				if (t1.getTransactionCount() == 0) {
					return 1;
				}

				if (t2.getTransactionCount() == 0) {
					return -1;
				}

				return Double.compare(
					t2.getPnL()/t2.getTransactionCount(),
					t1.getPnL()/t1.getTransactionCount());	
			}
		};
	}
	
	
	  /**
	   * Ranks the traders and appends the
	   * report to the market output.
	   */
		protected void rank(String _instrument, DataModel _model) {

			writeln("");
			writeln(sectionOpenTag + "***       REPORT        ***" + closeTag);
			writeln(
				_instrument
					+ ": "
					+ _model.getStartDate()
					+ " -> "
					+ _model.getEndDate());
			//writeln(sectionOpenTag + "***       ******        ***" + closeTag);
			writeln("");

			writeln(sectionOpenTag + "--- Trader ranks PnlPerTrans ---" + closeTag);

			Comparator comp = getPnlPerTrans();

			List list = _model.getTraders();
			ArrayList traders = new ArrayList();

			for (Iterator it = list.iterator(); it.hasNext();) {
				traders.add(it.next());
			}

			Collections.sort(traders, comp);

			int i = 1;

			for (Iterator it = traders.iterator(); it.hasNext();) {
				AbstractTrader trader = (AbstractTrader) it.next();

				//IAccount account = (IAccount)_accounts.get(trader);
				double score =
					(trader.getTransactionCount() == 0)
						? 0
						: (trader.getPnL() / trader.getTransactionCount());

				writeln(
					i
						+ ") "
						+ traderOpenTag
						+ trader.getName()
						+ closeTag
						+ " pnl/trans "
						+ _nf.format(score)
						+ " #: "
						+ trader.getTransactionCount());
				i++;
			}

			writeln("");
			
			writeln(sectionOpenTag + "--- Trader ranks per Pnl ---" + closeTag);
			i = 1;
			comp = getPnl();
			Collections.sort(traders, comp);

			for (Iterator it = traders.iterator(); it.hasNext();) {
				AbstractTrader trader = (AbstractTrader) it.next();

				writeln(
					i
						+ ") "
						+ traderOpenTag
						+ trader.getName()
						+ closeTag
						+ " pnl: "
						+ _nf.format(trader.getPnL())
						+ " #: "
						+ trader.getTransactionCount());
				i++;
			}

			//writeln(sectionOpenTag + "---   ---" + closeTag);
			writeln("");
			
			TransactionsReport report = _model.getTransactionsReport();
			report.generate();

			writeln(sectionOpenTag + "--- Trader Transaction Performance ---" + closeTag);
			String[] titles = _model.getTitles();
			for (int j = 0; j < titles.length; j++) {
				AbstractTrader trader = (AbstractTrader) _model.getTraders().get(j);
				writeln("");
				writeln(traderOpenTag + "--- " + trader.getName() + " ---" + closeTag);
				TraderStats stat = _model.getTraderStats(trader);

				writeln("pnl :" + _nf.format(trader.getPnL()));
				writeln("nbr transactions :" + trader.getTransactionCount());
				writeln(
					"pnl/trans :"
						+ _nf.format(
							(trader.getTransactionCount() == 0)
								? 0
								: trader.getPnL() / trader.getTransactionCount()));

				writeln("down pos days :" + stat.getDownPosDays());
				writeln("up pos days :" + stat.getUpPosDays());
				writeln("avg risk :" + stat.getMeanRisk());

				writeln("realized wins count :" + report.getWinningCount(j));
				writeln("realized win avg :" + _nf.format(report.getWinningAvg(j)));
				writeln("realized loss count :" + report.getLosingCount(j));
				writeln("realized loss avg :" + _nf.format(report.getLosingAvg(j)));
			}
		}
		
		/**
		 * Append year end report to market output
		 * @param stopped Boolean indicating if the yearly reports stopped on this year
		 * @param titles The trader titles
		 * @param year The reporting year
		 * @param yearvalues Values for each trader for the year
		 */
		public void appendYearEndReport(boolean stopped, String[] titles, int year, float[] yearvalues){
			writeln(yearOpenTag + "-- END OF " + year + " " + (stopped ? "stopped" : "") + "--" + closeTag);
			for (int i = 0; i < titles.length; i++) {
				writeln(traderOpenTag + titles[i] + closeTag + ":  " + _nf.format(yearvalues[i]));
			}
		}
}
