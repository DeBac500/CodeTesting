package robotrader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.BasicConfigurator;

import robotrader.engine.Engine;
import robotrader.market.impl.ListMarketEngine;
import robotrader.quotedb.QuoteRepositoryImpl;
import robotrader.trader.AbstractTrader;
import robotrader.trader.XmlTraderLoader;

/**
 * This class is the starting point for the command line  version of the
 * robotrader game. It loads some traders and computes their win and
 * losses
 * 
 * @author xcapt
 */
public class ConsoleMain 
{
  /** The location of the quotes master file */
	private static String _quotepath = "conf/quotes.xml";
  
  /** The location of the trader configuration file */
	private static String _traderpath = "conf/traders.xml";
  
  /** The instrument */
	private static String _instrument = "-all";

  /** The cumulative profit and loss per trader */ 
	private HashMap _resultmap = new HashMap();
  
  /** The cumulative transactions per trader */
  private HashMap _transactionmap  = new HashMap();
  
  private NumberFormat _nf = NumberFormat.getInstance();

	/**
	 * This is the main method for starting the command line version
	 * of robotrader which will start the engine with market data
	 * from a file and make traders compete with this data. 
	 * 
	 * @param args comand line arguments when starting the application
	 * as follows : xmlmasterquotefile xmltraderfile instrument|-all.
	 */
	public static void main(String[] args) 
  {
    BasicConfigurator.configure();
    
		if (args.length < 3) 
    {
			commandLineHelp();
		} 
    else 
    {
			_quotepath = args[0];
			File f = new File(_quotepath);
			if (f.exists()) 
      {
				_traderpath = args[1];
				File tf = new File(_traderpath);
				if (tf.exists()) 
        {
					_instrument = args[2];
					ConsoleMain app = new ConsoleMain();
					app.start();
				} 
        else 
        {
					System.out.println(
						"unable to find xml trader file:" + args[1]);
					commandLineHelp();
				}
			} 
      else 
      {
				System.out.println("unable to find quotefile :" + args[0]);
				commandLineHelp();
			}
		}

		System.exit(0);

	}
	/**
	 * This methods shows the command-line text for help.
	 */
	private static void commandLineHelp() {
		System.out.println(
			"usage : "
				+ ConsoleMain.class.getName()
				+ " xmlmasterquotefile xmltraderfile instrument|-all");
	}

	/**
	 * sets the file path to load quotes
	 * @param String path to quote file
	 */
	void setQuotePath(String path) {
		_quotepath = path;
	}

	/**
	 * Method start.
	 */
	void start() 
  {
    if (_nf instanceof DecimalFormat)
    {
      ((DecimalFormat)_nf).applyPattern("#0.00#");
    }

		QuoteRepositoryImpl quotedb;
		// load quotes
		quotedb = new QuoteRepositoryImpl();
		quotedb.setMasterQuoteFile(_quotepath);
		quotedb.load();
		// creates engine
		Engine engine = new Engine();
		// load traders
		XmlTraderLoader loader = new XmlTraderLoader();
		FileReader reader = null;
		try 
    {
			reader = new FileReader(_traderpath);
		} 
    catch (FileNotFoundException e) 
    {
			e.printStackTrace();
			System.exit(0);
		}
		AbstractTrader[] traders = loader.load(reader);
		for (int i = 0; i < traders.length; i++) 
    {
			engine.register(traders[i]);
		}
		
    // starts the engine
		if (_instrument.equals("-all")) 
    {
			// computation for all available instruments
			startAllInstruments(engine, quotedb, traders);
		} else 
    {
			// computation for all traders but only one instrument
			startOneInstrument(engine, quotedb);
		}
	}

	/**
	 * Method startAllInstruments.
	 * @param engine
	 * @param quotedb
	 * @param traders
	 */
	private void startAllInstruments(
		Engine engine,
		QuoteRepositoryImpl quotedb,
		AbstractTrader[] traders) 
  {
		for (int i = 0; i < traders.length; i++) 
    {
			_resultmap.put(traders[i], new Float(0));
      _transactionmap.put(traders[i], new Integer(0));
		}

		for (Iterator it = quotedb.getInstruments().iterator();
			it.hasNext();
			) 
    {
			String instrument = (String) it.next();
			System.out.println("*** " + instrument + " ***");
			ListMarketEngine market =
				new ListMarketEngine(quotedb.getQuotes(instrument), instrument);
			engine.setMarketEngine(market);

			engine.started();
			engine.loop();

			List list = engine.getTraderContainer().getTraders();
			
      for (int i = 0; i < list.size(); i++) 
      {
				AbstractTrader trader = (AbstractTrader) list.get(i);
				float current_profit =
					((Float) _resultmap.get(trader)).floatValue();
        current_profit += trader.getPnL();
				_resultmap.put(trader, new Float(current_profit));
        
        int current_transactions =
          ((Integer) _transactionmap.get(trader)).intValue();
        current_transactions += trader.getTransactionCount();
        _transactionmap.put(trader, new Integer(current_transactions));
			}
		}
		System.out.println("***************************");
		System.out.println("*    END OF COMPUTE ALL   *");
		System.out.println("***************************");
		List tlist = engine.getTraderContainer().getTraders();
		Collections.sort(tlist, new ResultMapComparator());
		
    for (Iterator it = tlist.iterator(); it.hasNext();) 
    {
			AbstractTrader trader = (AbstractTrader) it.next();
			float profit = ((Float) _resultmap.get(trader)).floatValue();
			System.out.print(trader.getName() + "\toverall profit:\t" + _nf.format(profit) + "\t");

      int transactions = ((Integer) _transactionmap.get(trader)).intValue();
      float profit_per_trans = (transactions > 0) ? profit / transactions : 0;
      System.out.println("profit per transaction\t" + _nf.format(profit_per_trans));
		}
	}

	/**
	 * Method startOneInstrument.
	 * @param engine
	 * @param quotedb
	 */
	private void startOneInstrument(
		Engine engine,
		QuoteRepositoryImpl quotedb) {

		ListMarketEngine market =
			new ListMarketEngine(quotedb.getQuotes(_instrument), _instrument);
		engine.setMarketEngine(market);

		// engine started synchronously
		engine.started();
		engine.loop();

	}

	/**
	*  ResultMapComparator description: compares a map of traders given there results from 
	* a result map storing the score of each trader  
	* <br>
	*/
	class ResultMapComparator implements Comparator 
  {
		public int compare(Object o1, Object o2) 
    {
			return ((Float) _resultmap.get(o2)).compareTo((Float)_resultmap.get(o1));
		}
	}
}