package robotrader.quotedb.swing;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import robotrader.quotedb.IQuoteRepository;

/**
 * Panel to configure the start and end dates of
 * the engine
 * 
 * <br>
 */
public class EngineStartPanel extends JPanel 
{
	/**
	 * The required serial version UID
	 */
	private static final long serialVersionUID = -8089268323387348674L;;
	
	/**
	 * Instrument drop down.
	 */
	private JComboBox instrumentDropDown;
	
	/**
	 * The from date text field
	 */
	private JTextField fromDate = new JTextField();
	
	/**
	 * The to date text field
	 */
	private JTextField toDate = new JTextField();
	
	/**
	 * The quote repository to use for selection options and date values.
	 */
	private IQuoteRepository _quoteDB;

    
    public EngineStartPanel(IQuoteRepository quoteDB){
    	_quoteDB = quoteDB;
        instrumentDropDown = new JComboBox(quoteDB.getInstruments().toArray());
        instrumentDropDown.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent ae){
        		String selectedTicker = (String)((JComboBox)ae.getSource()).getSelectedItem();
        		EngineStartPanel.this.updateDates(selectedTicker);
        	};
        });
		if(instrumentDropDown.getItemCount() > 0){
			instrumentDropDown.setSelectedIndex(0);
		}
        initComponents();
    }

    private void updateDates(String instrument){
    	fromDate.setText(_quoteDB.getStartDate(instrument));
    	toDate.setText(_quoteDB.getEndDate(instrument));
    }

	/**
	 * Method initComponents: creates the components embedded in the panel.
	 */
	private void initComponents() 
	{
		this.setLayout(new GridLayout(0, 2));

		this.add(new JLabel("Instrument"));
		this.add(instrumentDropDown);
		this.add(new JLabel("From (YYYYMMDD)"));
		this.add(fromDate);
		this.add(new JLabel("To (YYYYMMDD)"));
		this.add(toDate);
	}

	/**
	 * Method getInstrument: retrieve instrument from dialog.
	 * @return String instrument (quote code).
	 */
	public String getSelectedInstrument() {
		return (String)instrumentDropDown.getSelectedItem();
	}
  
	/**
	 * Method getFrom: retrieve start of period to retrieve quote.
	 * @return String start date
	 */
	public String getFrom() {
		return fromDate.getText();
	}
  
	/**
	* Method getFrom: retrieve end of period to retrieve quote.
	* @return String end date
	*/
	public String getTo() {
		return toDate.getText();
	}

}
