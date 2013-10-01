package robotrader.quotedb.swing;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import robotrader.quotedb.web.IQuotesLoader;
import robotrader.quotedb.web.YahooFRHistoricLoader;
import robotrader.quotedb.web.YahooDEHistoricLoader;
import robotrader.quotedb.web.YahooUSHistoricLoader;

/**
 * ImportPane description: Swing panel to configure the importation of quotes .
 * <br>
 */
public class ImportPane extends JPanel 
{
	/**
	 * The required serial version UID
	 */
	private static final long serialVersionUID = -8000268323387348674L;

	/**
	 * The instrument code text filed
	 */
	private JTextField _tfinst = new JTextField();
	
	/**
	 * The from date text field
	 */
	private JTextField _tffrom = new JTextField();
	
	/**
	 * The to date text field
	 */
	private JTextField _tfto = new JTextField();
	
	/**
	 * The Combo box for the available loaders
	 */
	private JComboBox _loadercombo = new JComboBox();

	/**
	 * ImportPane constructor: creates the panel
	 */
	public ImportPane() {
		initComponents();
	}

	/**
	 * Method main: test method. creates a dialog with the import pane.
	 * @param args
	 */
	public static void main(String[] args) {
		ImportPane pane = new ImportPane();
		JOptionPane.showOptionDialog(
			null,
			pane,
			"Import dialog",
			JOptionPane.OK_CANCEL_OPTION,
			JOptionPane.PLAIN_MESSAGE,
			null,
			null,
			null);
	}

	/**
	 * Method initComponents: creates the components embedded in the panel.
	 */
	private void initComponents() 
	{
		_loadercombo.addItem(new YahooUSHistoricLoader());
		_loadercombo.addItem(new YahooDEHistoricLoader());
		_loadercombo.addItem(new YahooFRHistoricLoader());
		_loadercombo.setSelectedIndex(0);

		this.setLayout(new GridLayout(0, 2));

		this.add(new JLabel("source"));
		this.add(_loadercombo);
		this.add(new JLabel("instrument"));
		this.add(_tfinst);
		this.add(new JLabel("from (YYYYMMDD)"));
		this.add(_tffrom);
		this.add(new JLabel("to (YYYYMMDD)"));
		this.add(_tfto);
	}

	/**
	 * Method getInstrument: retrieve instrument from dialog.
	 * @return String instrument (quote code).
	 */
	public String getInstrument() {
		return _tfinst.getText();
	}
	/**
	 * Method getFrom: retrieve start of period to retrieve quote.
	 * @return String start date
	 */
	public String getFrom() {
		return _tffrom.getText();
	}
	/**
	* Method getFrom: retrieve end of period to retrieve quote.
	* @return String end date
	*/
	public String getTo() {
		return _tfto.getText();
	}
	/**
	 * Method getSource: retrieve IQuoteLoader object to retrieve quote from.
	 * @return IQuotesLoader loader object
	 */
	public IQuotesLoader getSource() {
		return (IQuotesLoader) _loadercombo.getSelectedItem();
	}

	/**
	 * Method setInstrument: set default instrument code for the dialog.
	 * @param inst instrument code
	 */
	public void setInstrument(String inst) {
		_tfinst.setText(inst);
	}
	/**
	 * Method setFrom: set default start period for the dialog.
	 * @param from start date
	 */
	public void setFrom(String from) {
		_tffrom.setText(from);
	}
	/**
	* Method setTo: set default ent period for the dialog.
	* @param to end date
	*/
	public void setTo(String to) {
		_tfto.setText(to);
	}
}
