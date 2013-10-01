package robotrader.quotedb.swing;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Panel to configure the start and end dates of
 * the engine
 * 
 * <br>
 */
public class DateBoundaryPanel extends JPanel 
{
	/**
	 * The required serial version UID
	 */
	private static final long serialVersionUID = -8089268323387348674L;

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
	 * constructor: creates the panel
	 */
	public DateBoundaryPanel(String instrument, String from, String to) 
  {
    _tfinst.setText(instrument);
    _tfinst.setEditable(false);
    _tffrom.setText(from);
    _tfto.setText(to);
		initComponents();
	}

	/**
	 * Method initComponents: creates the components embedded in the panel.
	 */
	private void initComponents() 
	{
		this.setLayout(new GridLayout(0, 2));

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
