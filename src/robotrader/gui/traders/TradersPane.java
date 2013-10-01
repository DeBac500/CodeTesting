package robotrader.gui.traders;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import lu.base.iris.AppToolkit;

import org.apache.log4j.Logger;

import robotrader.gui.EngineService;
import robotrader.trader.AbstractTrader;
import robotrader.trader.XmlTraderLoader;

/**
 * TradersPane shows a list of traders.
 * 
 * @author xcapt
 * @author klinst
 */
public class TradersPane extends JPanel 
{
	/**
	 * The serial Version
	 */
	private static final long serialVersionUID = 5747188509796876701L;
	
  private static final Logger trace = Logger.getLogger(TradersPane.class);

  /**
   * The List of trader names
   */
  private JList _jlist;

	/**
	 * The list model containing the traders
	 */
	private DefaultListModel _traderlist;
  
  private XmlTraderLoader _loader;
  
  private String _traders_file;
	
  /**
   * Action for Removing a Quote from Database
   * 
   * @author klinst
   */
  class RemoveTraderAction extends AbstractAction 
  {
    /**
     * Serial ID
     */
    private static final long serialVersionUID = -3641760964280521749L;

    private Component _parent;
    
    /**
     * Constructs the Action.
     *
     */
    RemoveTraderAction(Component parent) 
    {
      super("Remove");
      _parent = parent;
    }
    
    /**
     * Removes the Quotes for a selected
     * number of instruments from database
     */
    public void actionPerformed(ActionEvent evt) 
    {
      int[] selected = _jlist.getSelectedIndices();
      
      if ((selected.length > 0)
        && JOptionPane.showConfirmDialog(
          _parent,
          "Do you really want to remove the selected traders ?",
          "Remove traders",
          JOptionPane.YES_NO_OPTION)
          == JOptionPane.YES_OPTION) 
      {
        for (int i = 0; i < selected.length; i++)
        {
          _traderlist.remove(selected[i]);  
        }
        
        saveTraders();
      }
    }
  }

  class EditTradersAction extends AbstractAction 
  {
    /**
     * Serial id 
     */
    private static final long serialVersionUID = -7358557294707657077L;

    private Component _parent;
    
    /**
     * Constructs new action
     *
     */
    EditTradersAction(Component parent) 
    {
      super("Edit");
      _parent = parent;
    }
    
    /**
     * 
     */
    public void actionPerformed(ActionEvent evt) 
    {
      AbstractTrader trader = (AbstractTrader)_jlist.getSelectedValue();
      final ConfigurationPane pane = new ConfigurationPane(trader);
      
      if (trader != null)
      {
        if (JOptionPane
            .showOptionDialog(
              _parent,
              pane,
              "Edit dialog",
              JOptionPane.OK_CANCEL_OPTION,
              JOptionPane.PLAIN_MESSAGE,
              null,
              null,
              null)
            == JOptionPane.OK_OPTION) 
          {
            try
            {
              pane.getChangedTrader();  
            }
            catch (Exception e)
            {
              trace.error("Cannot get changed trader", e);
            }
            
            saveTraders();
          }
        
      }
      
    }
  }

  class AddTradersAction extends AbstractAction 
  {
    /**
     * Serial id 
     */
    private static final long serialVersionUID = -7358557294707657077L;

    private Component _parent;
    
    /**
     * Constructs new action
     *
     */
    AddTradersAction(Component parent) 
    {
      super("Add");
      _parent = parent;
    }
    
    /**
     * 
     */
    public void actionPerformed(ActionEvent evt) 
    {
      final ConfigurationPane pane = new ConfigurationPane();
      
      if (JOptionPane
          .showOptionDialog(
            _parent,
            pane,
            "Edit dialog",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            null,
            null)
          == JOptionPane.OK_OPTION) 
        {
          try
          {
            _traderlist.addElement(pane.getChangedTrader());
            saveTraders();
          }
          catch (Exception e)
          { 
            trace.error("Cannot get changed trader", e);
          }
        }
    }
  }

	/**
	 * Constructs the Traders Panel which contains
	 * a list of trader names.
	 *
	 */
	public TradersPane(String traders_file)
	{
		_traders_file = traders_file;
		_loader = new XmlTraderLoader();
    
		_traderlist = new DefaultListModel();
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    
		JToolBar toolbar = new JToolBar();
		toolbar.add(new AddTradersAction(this));
		toolbar.add(new EditTradersAction(this));
		toolbar.add(new RemoveTraderAction(this));
		toolbar.setFloatable(false);

		this.add(toolbar, BorderLayout.NORTH);

		_jlist = new JList(_traderlist);
		_jlist.setCellRenderer(new TradersRenderer());
		this.add(new JScrollPane(_jlist), BorderLayout.CENTER);
	}
	
	/**
	 * Sets the Traders to be shown in the list
	 * 
	 * @param traders The traders
	 */
	public void setTraderList(AbstractTrader[] traders)
	{
		_traderlist.removeAllElements();
		for(int i = 0; i < traders.length; i++)
		{
			_traderlist.addElement(traders[i]);
		}
	}
  
  void saveTraders()
  {
    try
    {
      int size = _traderlist.size();
      AbstractTrader[] traders = new AbstractTrader[size];
      
      for (int i = 0; i < size; i++)
      {
        traders[i] = (AbstractTrader)_traderlist.get(i);  
      }
      
      FileWriter writer = new FileWriter(_traders_file);
      _loader.save(traders, writer);
      writer.close();              
    }
    catch(IOException ioe)
    {
      trace.error("Cannot save trader master file", ioe);
    }
    
    final EngineService enginesrv =
      (EngineService) AppToolkit.getService(EngineService.class);
    final TradersService traderssrv =
      (TradersService) AppToolkit.getService(TradersService.class);
    
    enginesrv.loadTradersFile(_traders_file);
    traderssrv.setTradersFile(_traders_file);
    
    repaint();
  }
	
	/**
	 * The Renderer for the traders
	 */
	class TradersRenderer extends DefaultListCellRenderer 
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1575639118232272350L;

		/**
		 * Sets the traders name as the text
		 * value of the Label.
		 */
			public Component getListCellRendererComponent(
				JList list,
				Object value,
				int index,
				boolean isSelected,
				boolean cellHasFocus) {
				Component c =
					super.getListCellRendererComponent(
						list,
						value,
						index,
						isSelected,
						cellHasFocus);
					AbstractTrader trader = (AbstractTrader) value;

				((JLabel) c).setText(trader.getName());
				return c;
			}
	}

}
