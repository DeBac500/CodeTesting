package robotrader.gui.traders;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import org.apache.log4j.Logger;

import lu.base.iris.AppToolkit;
import lu.base.iris.services.TranslationService;
import robotrader.gui.EngineService;
import robotrader.gui.XmlFileFilter;
import robotrader.trader.AbstractTrader;
import robotrader.trader.XmlTraderLoader;

/**
 * This panel contains the label and the
 * text field for the traders file name
 * 
 * @author xcapt
 * @author klinst
 */
public class TradersFilePanel extends JPanel  
{
  final static Logger _trace = Logger.getLogger(TradersFilePanel.class);
	/**
	 * The serial id
	 */
	private static final long serialVersionUID = -6520504937016550442L;
	
	/**
	 * The file name text field
	 */
	private JTextField _file;
  
	/**
	 * Creates a new Traders File Panel.
	 *
	 */
	public TradersFilePanel() 
	{
    this.setLayout(new BorderLayout());
    
		String txtlabel =
			TranslationService.getInstance().getTranslation("TRADERS_FILE_LABEL");
    
    JToolBar toolbar = new JToolBar();
    toolbar.setFloatable(false);
    toolbar.add(new LoadTradersAction(this));

    this.add(toolbar, BorderLayout.NORTH);
    
    JPanel panel = new JPanel();

		panel.add(new JLabel(txtlabel + ":"));
		_file = new JTextField(40);
    _file.setEditable(false);
		panel.add(_file);
    
    this.add(panel);
	}

	public TradersFilePanel(String filename){
		this();
		this.setFile(filename);
	}
	
	/**
	 * Sets the name displayed by the text field
	 * @param filename The file name
	 */
	public void setFile(String filename) 
	{
		_file.setText(filename);
	}

	/**
	 * Gets the file name displayed
	 * @return The file name
	 */
	public String getFile() 
	{
		return _file.getText();
	}
  
  /**
   * Action to perform when trader's file is to be loaded
   * 
   * @author klinst
   */
  private class LoadTradersAction extends AbstractAction
  {
    /**
     * 
     */
    private static final long serialVersionUID = -4908946014698420638L;
    
    /**
     * The parent component
     */
    private Component _parent;
    
    /**
     * Constructor
     * @param parent
     */
    public LoadTradersAction(Component parent)
    {
      super("Load Traders File");
      _parent = parent;
    }
    
    /**
     * 
     */
    public void actionPerformed(ActionEvent e)
    {
      JFileChooser chooser = new JFileChooser(_file.getText());
      chooser.setFileFilter(new XmlFileFilter());

      int returnVal = chooser.showOpenDialog(_parent);
      if(returnVal == JFileChooser.APPROVE_OPTION) 
      {
        if (chooser.getSelectedFile().getAbsolutePath().equalsIgnoreCase(getFile()))
        {
          return;
        }

        if (JOptionPane.showConfirmDialog(
            _parent,
            TranslationService
              .getInstance()
              .getTranslation(
              "TRADERS_FILE_CHANGE_CONFIRM"),
            TranslationService
              .getInstance()
              .getTranslation(
              "TRADERS_FILE_CHANGE_TITLE"),
            JOptionPane.YES_NO_OPTION)
          != JOptionPane.YES_OPTION) 
       {
         return;
       }

       if (chooser.getSelectedFile().exists())
       {
         _file.setText(chooser.getSelectedFile().getAbsolutePath());         
       }
       else
       {
         if (JOptionPane.showConfirmDialog(
             _parent,
             "Do you want to create the file?",
             "File does not exist",
             JOptionPane.YES_NO_OPTION)
           != JOptionPane.YES_OPTION)
         {
           return;  
         }
         
         try
         {
           FileWriter writer = new FileWriter(chooser.getSelectedFile());
           AbstractTrader[] traders = new AbstractTrader[0];
           
           XmlTraderLoader loader = new XmlTraderLoader();
           loader.save(traders, writer);
           writer.close();
         }
         catch(IOException ioe)
         {
           _trace.error("Could not save new traders file", ioe);
           return;
         }
         
         _file.setText(chooser.getSelectedFile().getAbsolutePath());
       }
         
       final TradersService traderssrv =
         (TradersService) AppToolkit.getService(TradersService.class);
       final EngineService enginessrv =
         (EngineService) AppToolkit.getService(EngineService.class);

       traderssrv.setTradersFile(getFile());
       enginessrv.loadTradersFile(getFile());
      }
    }  
  }  
}
