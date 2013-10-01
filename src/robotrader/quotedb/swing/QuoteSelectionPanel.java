package robotrader.quotedb.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import robotrader.quotedb.IQuoteRepository;
import robotrader.quotedb.QuoteRepositoryImpl;
import robotrader.quotedb.web.IQuotesLoader;

/**
 * QuoteSelectionPanel : provides a panel for quote selection
 * - select 
 * - import
 */
public class QuoteSelectionPanel extends JPanel 
{
	/**
	 * Serial Version ID
	 */
	private static final long serialVersionUID = -2871613350679385543L;

	/**
	 * The import action
	 * 
	 * @author Stefan
	 */
	class WebImportAction extends AbstractAction 
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
		WebImportAction(Component parent) 
		{
			super("Web Import");
			_parent = parent;
		}
		
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent evt) 
		{
			final ImportPane pane = new ImportPane();
			
			if (JOptionPane
				.showOptionDialog(
					_parent,
					pane,
					"Import dialog",
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE,
					null,
					null,
					null)
				== JOptionPane.OK_OPTION) 
			{
				final IQuotesLoader qloader = pane.getSource();
				
				_status.setText(
					qloader.toString()
						+ ": loading quote:"
						+ pane.getInstrument());

				final Thread loaderthread = new Thread() 
				{
					public void run() 
					{
						setEnabled(false);
						List quotes;
						try 
						{
							quotes =
								qloader.loadQuotes(
									pane.getInstrument(),
									pane.getFrom(),
									pane.getTo());
							_quotedb.addQuoteList(quotes);
							_status.setText("done. " + qloader.getStatus());
							_listmodel.addElement(pane.getInstrument());
						} 
						catch (Exception e) 
						{
							_status.setText("Error. " + e.getMessage());
							e.printStackTrace();
						}
						setEnabled(true);
					}
				};
				
				Thread updater = new Thread() 
				{
					public void run() 
					{
						while (loaderthread.isAlive()) 
						{
							Runnable run = new Runnable() 
							{
								public void run() 
								{
									if (loaderthread.isAlive()) 
									{
										_status.setText(qloader.getStatus());
									}
								}
							};

							SwingUtilities.invokeLater(run);

							try {
								Thread.sleep(500);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				};
				
				loaderthread.start();
				updater.start();
			}
		}
	}
	
	/**
	 * Renderer for Instrument names.
	 * 
	 * @author Stefan
	 */
	class InstrumentRenderer extends DefaultListCellRenderer 
	{
		/**
		 * Serial ID 
		 */
		private static final long serialVersionUID = 3224220773348441824L;

		/**
		 * 
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
			String instr = (String) value;

			((JLabel) c).setText(
				instr
					+ " "
					+ _quotedb.getStartDate(instr)
					+ "-"
					+ _quotedb.getEndDate(instr));
			return c;
		}
	}

	/**
	 * 
	 * @author Stefan
	 */
	class WebUpdateAction extends AbstractAction 
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 8955250293402417379L;
		
    private Component _parent;
    
		/**
		 * Constructs an update action 
		 *
		 */
		WebUpdateAction(Component parent) 
		{
			super("Web Update");
      _parent = parent;
		}
		
		/**
		 * The action to perform when updating
     * the quotes
		 */
		public void actionPerformed(ActionEvent evt) {
			Object selected = _jlist.getSelectedValue();
			if (selected == null) 
			{
				return;
			}
			
			final ImportPane pane = new ImportPane();
			pane.setInstrument((String) selected);
			
			if (JOptionPane
				.showOptionDialog(
					_parent,
					pane,
					"Import dialog",
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE,
					null,
					null,
					null)
				== JOptionPane.OK_OPTION) 
			{
				final IQuotesLoader qloader = pane.getSource();
				_status.setText(
					qloader.toString()
						+ ": loading quote:"
						+ pane.getInstrument());

				final Thread loaderthread = new Thread() {
					public void run() {
						setEnabled(false);
						List quotes;
						try {
							quotes =
								qloader.loadQuotes(
									pane.getInstrument(),
									pane.getFrom(),
									pane.getTo());
							_quotedb.addQuoteList(quotes);
							_status.setText("done. " + qloader.getStatus());
							_listmodel.addElement(pane.getInstrument());

						} 
						catch (Exception e) 
						{
							_status.setText("Error. " + e.getMessage());
							e.printStackTrace();
						} 
						setEnabled(true);
					}
				};
				
				Thread updater = new Thread() {
					public void run() {
						while (loaderthread.isAlive()) {
							Runnable run = new Runnable() {
								public void run() {
									if (loaderthread.isAlive()) {
										_status.setText(qloader.getStatus());
									}
								}
							};

							SwingUtilities.invokeLater(run);

							try {
								Thread.sleep(500);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				};
				loaderthread.start();
				updater.start();
			}
		}
	}

	/**
	 * Action for Removing a Quote from Database
	 * 
	 * @author klinst
	 */
	class RemoveQuoteAction extends AbstractAction 
	{
		/**
		 * Serial ID
		 */
		private static final long serialVersionUID = -3641760964280521749L;

		/**
		 * Constructs the Action.
		 *
		 */
		RemoveQuoteAction() 
		{
			super("Remove");
		}
		
		/**
		 * Removes the Quotes for a selected
		 * number of instruments from database
		 */
		public void actionPerformed(ActionEvent evt) 
		{
			Object[] selected = _jlist.getSelectedValues();
			if ((selected.length > 0)
				&& JOptionPane.showConfirmDialog(
					null,
					"Do you really want to remove the selected quotes ?",
					"Remove quotes",
					JOptionPane.YES_NO_OPTION)
					== JOptionPane.YES_OPTION) 
			{
				
				for (int i = 0; i < selected.length; i++) 
				{
					String sel = (String) selected[i];
					_quotedb.removeQuotes(sel);
					_listmodel.removeElement(sel);
				}
			}

		}
	}

	/**
	 * The Repository of Quotes
	 */
	private IQuoteRepository _quotedb;
	
	/**
	 * The List of instrument names
	 */
	private JList _jlist;
	
	/**
	 * The model for the instrument JList
	 */
	private DefaultListModel _listmodel;
	
	/**
	 * The Status Label
	 */
	private JLabel _status;

	/**
	 * Constructs a new Quote Selection Panel.
	 * @param quotedb The repository to use
	 */
	public QuoteSelectionPanel(IQuoteRepository quotedb) 
	{
		_quotedb = quotedb;
		initComponents();
	}

	public static void main(String[] args) {
		// load repository
		QuoteRepositoryImpl repo = new QuoteRepositoryImpl();
		repo.load();
		System.out.println("Instruments:");
		for (Iterator it = repo.getInstruments().iterator(); it.hasNext();) {
			String instr = (String) it.next();
			System.out.println(
				instr
					+ " "
					+ repo.getStartDate(instr)
					+ "-"
					+ repo.getEndDate(instr)
					+ " ("
					+ repo.getQuotes(instr).size()
					+ " days)");
		}

		// open frame
		QuoteSelectionPanel pane = new QuoteSelectionPanel(repo);
		JFrame frame = new JFrame("test QuoteSelectionPanel");
		frame.getContentPane().add(pane);
		frame.setSize(200, 300);
		frame.setVisible(true);
	}

	/**
	 * initialises all the Swing Components and
	 * adds the actions to the buttons of the
	 * toolbar.
	 *
	 */
	public void initComponents() 
	{	
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		toolbar.add(new WebImportAction(this));
		toolbar.add(new WebUpdateAction(this));
		toolbar.add(new RemoveQuoteAction());
		_status = new JLabel(" ");
		
		this.setLayout(new BorderLayout());
		this.add(toolbar, BorderLayout.NORTH);
		this.add(new JScrollPane(getJList()), BorderLayout.CENTER);
		this.add(_status, BorderLayout.SOUTH);
	}

	/**
	 * fills the list model with the available
	 * instrument from the quote repository
	 * 
	 * @return The Swing JList of instruments
	 */
	private JList getJList() 
	{
		if (_jlist == null) 
		{
			_listmodel = new DefaultListModel();
			
			for (Iterator it = _quotedb.getInstruments().iterator();
				it.hasNext();
				) 
			{
				String instr = (String) it.next();
				_listmodel.addElement(instr);
			}
			
			_jlist = new JList(_listmodel);
			_jlist.setCellRenderer(new InstrumentRenderer());
		}
		return _jlist;
	}

	/**
	 * Gets the currently selected instrument
	 * 
	 * @return The instruments name
	 */
	public String getSelectedInstrument() 
	{
		return (String) _jlist.getSelectedValue();
	}
}
