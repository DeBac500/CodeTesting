/*
 * Created on Aug 14, 2003
 */
package robotrader.quotedb.swing;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lu.base.iris.services.TranslationService;

/**
 * The quote configuration panel. Contains the
 * Quote label and the text field.
 */
public class QuoteConfigurationPanel extends JPanel 
{
	/**
	 * The serial version UID
	 */
	private static final long serialVersionUID = 4204308706688639523L;
	
	/**
	 * The quote file text field 
	 */
	private JTextField _quotefile;

	/**
	 * Constructs a new quote config panel
	 */
	public QuoteConfigurationPanel() 
	{
		String txtlabel =
			TranslationService.getInstance().getTranslation("QUOTE_FILE_LABEL");
		this.add(new JLabel(txtlabel + ":"));
		_quotefile = new JTextField();
		this.add(_quotefile);
	}

	/**
	 * Sets the quote file name.
	 * 
	 * @param filename The file name
	 */
	public void setQuoteFile(String filename) 
	{
		_quotefile.setText(filename);
	}

	/**
	 * Gets the quote file name
	 * 
	 * @return The file name
	 */
	public String getQuoteFile() 
	{
		return _quotefile.getText();
	}
}
