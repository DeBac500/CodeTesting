package robotrader.gui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import robotrader.stats.DataModel;
import robotrader.stats.TraderStats;
import robotrader.trader.AbstractTrader;

/**
 * HtmlReportDialog description: dialog box presenting a report of the last
 * engine run <br>
 * 
 * @author xcapt
 * @author klinst
 */
public class HtmlReport {
	/**
	 * The data model containing the trading statistics
	 */
	private DataModel _model;

	/**
	 * The traders names (from the data model)
	 */
	private String[] _titles;

	/**
	 * The number format for the locale
	 */
	private NumberFormat _nf = NumberFormat.getInstance();

	/**
	 * Our date format
	 */
	private SimpleDateFormat _df = new SimpleDateFormat("yyyyMMdd");

	/**
	 * The html file as a byte array
	 */
	private byte[] _htmlContent;

	/**
	 * The file name to write to
	 */
	private String _filename;

	/**
	 * HtmlReportDialog constructor.
	 * 
	 * @param model
	 *            The data model to use
	 */
	public HtmlReport(DataModel model) {
		_model = model;
		_titles = model.getTitles();
	}

	/**
	 * Method generate: generates the HTML file to display.
	 * 
	 * @return content The byte array containing the HTML output
	 */
	public byte[] getContent() throws IOException {
		if (_nf instanceof DecimalFormat) {
			((DecimalFormat) _nf).applyLocalizedPattern("#0.00#");
		}

		if (_htmlContent == null) {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(output);
			writer.write("\n");
			// report header
			writer.write("<h3>");
			writer.write(_model.getMarket().current().getInstrument());
			writer.write(" : ");
			writer.write(_df.format(_model.getStartDate()));
			writer.write(" - ");
			writer.write(_df.format(_model.getEndDate()));
			writer.write("</h3>\n");

			// table header
			writer.write("<table><tr>\n");
			writer.write("<th>name</th><th>pnl</th>");
			writer.write("<th># trans.</th><th>pnl/trans</th>");
			writer.write("<th>avg risk</th>");
			writer.write("<th>leveraged pnl</th>");
			writer.write("</tr>\n");

			List traders = _model.getTraders();
			Collections.sort(traders, MarketOutput.getPnl());

			for (int j = 0; j < _titles.length; j++) {
				AbstractTrader trader = (AbstractTrader) traders.get(j);
				TraderStats stat = _model.getTraderStats(trader);

				writer.write("<tr bgcolor=\"FF0000\">\n");

				writer.write("<td>" + trader.getName() + "</td>\n");
				writer.write("<td>" + _nf.format(trader.getPnL()) + "</td>\n");
				writer.write("<td>" + trader.getTransactionCount() + "</td>\n");
				writer.write("<td>"
						+ _nf.format((trader.getTransactionCount() == 0) ? 0
								: trader.getPnL()
										/ trader.getTransactionCount())
						+ "</td>\n");
				writer.write("<td>" + _nf.format(stat.getMeanRisk())
						+ "</td>\n");
				writer.write("<td>"
						+ _nf.format((stat.getMeanRisk() == 0f ? 0f : trader
								.getPnL()
								/ stat.getMeanRisk())) + "</td>\n");
				writer.write("</tr>\n");
			}

			// write html trailer
			writer.write("</table>\n");
			writer.flush();

			_htmlContent = output.toByteArray();
		}
		return _htmlContent;
	}

	/**
	 * Makes the report panel.
	 * 
	 * @param content
	 *            The html content as byte array
	 * @param action
	 *            The underlying action
	 * @return The report panel
	 * @throws IOException
	 *             if problem reading from content
	 */
	public JScrollPane makePanel(byte[] content) throws IOException {
		JTextPane tpane = new JTextPane();
		tpane.setContentType("text/html");
		tpane.read(new ByteArrayInputStream(content), null);

		JScrollPane spane = new JScrollPane(tpane);
		return spane;
	}

	/**
	 * Creates a file name using the instrument name, start and end date.
	 * 
	 * @return The file name
	 */
	public String createFileName() {
		if (_filename == null) {
			StringBuffer buf = new StringBuffer();
			buf.append("result_");
			buf.append(_model.getMarket().current().getInstrument());
			buf.append("_");
			buf.append(_df.format(_model.getStartDate()));
			buf.append("_");
			buf.append(_df.format(_model.getEndDate()));
			buf.append(".html");
			_filename = buf.toString();
		}
		return _filename;
	}

	/**
	 * Saves the content to file.
	 * 
	 * @param file
	 *            The file to write to
	 * @param content
	 *            The html content
	 * @throws IOException
	 */
	public void saveToFile(File file, byte[] content) throws IOException {
		FileWriter writer = new FileWriter(file);
		InputStreamReader reader = new InputStreamReader(
				new ByteArrayInputStream(content));

		// copy html file to selected file
		char[] buffer = new char[5000];
		int read = -1;
		while ((read = reader.read(buffer)) > 0) {
			writer.write(buffer, 0, read);
		}
		writer.close();
		reader.close();
	}
}
