package robotrader.gui;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import lu.base.iris.AppToolkit;
import lu.base.iris.services.ResourceService;

import robotrader.engine.model.IEngine;
import robotrader.gui.traders.TradersService;

import robotrader.market.HistoricData;
import robotrader.market.impl.ListMarketEngine;
import robotrader.quotedb.IQuoteRepository;
import robotrader.quotedb.swing.EngineStartPanel;
import robotrader.quotedb.swing.QuoteSelectionPanel;

/**
 * Engine control panel: panel with control buttons for 
 * robotrader.engine.Engine
 * 
 * @author xcapt
 * @author klinst
 */
class EngineControlPane extends JPanel 
{
  /**
   * Serial ID 
   */
  private static final long serialVersionUID = -6767136301503734021L;

  /**
   * The date format
   */
  private static SimpleDateFormat _yyyymmdddf =
    new SimpleDateFormat("yyyyMMdd");

  /**
   * The engine responsible for updating the
   * quotes and displays.
   */
	private IEngine _engine;
  
  /**
   * The repository providing the quotes.
   */
	private IQuoteRepository _quotedb;

  
  /**
   * The JLabel displaying the traders file
   */
	private JLabel _traderslabel;

  /**
   * The Action for loading traders from xml
   */
	private LoadTradersAction _loadtradersaction;
  
  /**
   * The Action for starting the traders
   */
	private StartAction _startaction;
  
  /**
   * The Action for stopping the traders
   */
	private StopAction _stopaction;

	
	/**
   * Creates new control pane.
   *  
   * @param engine The Engine for loading and updating quote
   * @param quotedb The Repository providing all the quotes
	 */
  EngineControlPane(IEngine engine, 
  		IQuoteRepository quotedb) 
  {
		this.setLayout(new GridLayout(1, 0));
		_engine = engine;
		_quotedb = quotedb;
		
		init();
	}

	/**
   * initialise the pane. The Toolbar is created
   * and the relevant actions are added to
   * the buttons. 
   *
	 */
  private void init() 
  {
		_startaction = new StartAction(this);
		_stopaction = new StopAction();
		QuoteDbAction qdbAction = new QuoteDbAction(this);
		_loadtradersaction = new LoadTradersAction();
		
		ResourceService res = (ResourceService)AppToolkit.getService(ResourceService.class);
		if(res != null){
			_startaction.putValue(AbstractAction.SMALL_ICON, res.getIcon("MENU_ICON_StartEngine"));
			_stopaction.putValue(AbstractAction.SMALL_ICON, res.getIcon("MENU_ICON_StopEngine"));
			qdbAction.putValue(AbstractAction.SMALL_ICON, res.getIcon("MENU_ICON_QuoteDbSmall"));
			_loadtradersaction.putValue(AbstractAction.SMALL_ICON, res.getIcon("MENU_ICON_TraderSmall"));
		}
		

		JToolBar toolbar = new JToolBar();
		toolbar.add(new JButton(_startaction));
		toolbar.add(new JButton(_stopaction));
		toolbar.add(new JButton(qdbAction));
		toolbar.add(new JButton(_loadtradersaction));
		toolbar.setFloatable(false);
		this.add(toolbar);
		
		/*JMenuBar jmb = new JMenuBar();
		JMenu jm = new JMenu("Engine");
		jm.add(_startaction);
		jm.add(_stopaction);
		jm.add(new QuoteDbAction(this));
		jm.add(_loadtradersaction);
		jmb.add(jm);
		this.add(jmb);*/
	}

	
	/**
		 *  QuoteDbAction  description: opens up the quote repository dialog to
		 * select an instrument.
		 * <br>
		*/
    class QuoteDbAction extends AbstractAction 
    {
		/**
         * Serial ID
         */
        private static final long serialVersionUID = 6023766181099643905L;

        private Component _parent;
    
        /**
         * constructs the action.
         *
         */
        QuoteDbAction(Component parent){
            super("Quote DB");
            _parent = parent;
        }

        /**
         * Changes the instrument. Creates a new Market
         * Engine for reading the quotes.
         * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
        public void actionPerformed(ActionEvent evt) {
            QuoteSelectionPanel pane = new QuoteSelectionPanel(_quotedb);
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
				== JOptionPane.OK_OPTION) {
                    String instrument = pane.getSelectedInstrument();
				
                    if (instrument != null){
                       _engine.setMarketEngine(
                           new ListMarketEngine(
                           _quotedb.getQuotes(instrument),
                           instrument));
                   }    
			}
		}
	}

	/**
		 *  LoadTradersAction  description: opens file selection dialog for
		 * traders.xml file
		 * <br>
		*/
    class LoadTradersAction extends AbstractAction
    {
    /**
     * Serial ID
     */
    private static final long serialVersionUID = 7299725318498947225L;

    /**
     * Constructs the Action.
     *
     */
    LoadTradersAction(){
        super("Load Traders");
    }
		
       /**
     	* Changes the trader file containing the traders
     	* used.
     	* 
		* @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		*/
        public void actionPerformed(ActionEvent evt){
			JFileChooser chooser = new JFileChooser(".");
			chooser.setDialogTitle("Choose xml trader");
			chooser.setFileFilter(new XmlFileFilter());

			if (JFileChooser.APPROVE_OPTION != chooser.showOpenDialog(null)) {
				return;
			}

			String path = chooser.getSelectedFile().getPath();

			_engine.loadTradersFile(path);
			final TradersService traderssrv = (TradersService) AppToolkit.getService(TradersService.class);
			traderssrv.setTradersFile(path);
			_traderslabel.setText("traders file: " + path);
		}
	}
	/**
   * 
	 * StartAction     description: starts the robotrader engine that
	 * is launched asynchronously (separate Thread)
   * <br>
   */
	class StartAction extends AbstractAction
	{
		/**
	     * Serial ID
	     */
	    private static final long serialVersionUID = -8078967510554577284L;
	    
	    private Component _parent;
	
	    /**
	     * Constructs the Action.
	     *
	     */
	    StartAction(Component parent){
	    	super("Start");
	    	_parent = parent;
		}
	    
			/**
			 * Starts the Engine.
	     * 
	     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
		public void actionPerformed(ActionEvent evt) 
	    {
	      //String instrument = _engine.getQuoteRef();
		  String instrument;
	     
	      EngineStartPanel pane = new EngineStartPanel(_quotedb);
	      
	      if (JOptionPane
	        .showOptionDialog(
	          _parent,
	          pane,
	          "Select Dates",
	          JOptionPane.OK_CANCEL_OPTION,
	          JOptionPane.PLAIN_MESSAGE,
	          null,
	          null,
	          null)
	        == JOptionPane.OK_OPTION) 
	      {
	    	instrument = pane.getSelectedInstrument();
	        Date start = null;
	        Date end = null;          
	
	        try
	        {
	          start = _yyyymmdddf.parse(pane.getFrom());
	          end = _yyyymmdddf.parse(pane.getTo());          
	        }
	        catch (ParseException e)
	        {
	          try
	          {
	            start = _yyyymmdddf.parse(_quotedb.getStartDate(instrument));
	            end = _yyyymmdddf.parse(_quotedb.getEndDate(instrument));            
	          }
	          catch(ParseException ex)
	          {
	        	System.err.println(ex.getMessage());
	        	ex.printStackTrace();
	            return;
	          }
	        }
	        
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(start);
	        calendar.add(Calendar.DATE, -1);
	        start = calendar.getTime();
	
	        calendar.setTime(end);
	        calendar.add(Calendar.DATE, 1);
	        end = calendar.getTime();
	
	        
	        List quotes = _quotedb.getQuotes(instrument);
	        List currentQuotes = new ArrayList();
	        
	        for (int i = 0; i < quotes.size(); i++)
	        {
	          HistoricData quote = (HistoricData)quotes.get(i);
	          Date date = quote.getDate();
	          if(date.after(start) && date.before(end))
	          {
	            currentQuotes.add(quote);
	          }
	        }
	        
	        if (currentQuotes.size() > 0)
	        {
	          _engine.setMarketEngine(
	              new ListMarketEngine(
	                  currentQuotes,
	                  instrument));
	        } 
	        
	        _engine.start();
	      }
				
		}
	}

   /**
    *  StopAction  description: stops the robotrader engine that has been
    * previously started with the StartAction
    * <br>
    */
    class StopAction extends AbstractAction {
        /**
         * Serial ID
         */
        private static final long serialVersionUID = -1222654966107233052L;

        /**
         * Constructs the Action
         *
         */
        StopAction() {
            super("Stop");
        }

	 /**
        * Stops the engine.
        * 
        * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
        */
        public void actionPerformed(ActionEvent evt){
            _engine.stop();
        }
    }
}