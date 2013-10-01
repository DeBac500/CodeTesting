package robotrader.market.indicator.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import robotrader.engine.TraderContainer;
import robotrader.engine.model.Order;
import robotrader.market.HistoricData;
import robotrader.market.IIndicator;
import robotrader.market.impl.ListMarketEngine;
import robotrader.quotedb.QuoteRepositoryImpl;
import robotrader.trader.AbstractTrader;
import robotrader.trader.XmlTraderLoader;

/**
 * The indicator panel shows the trading signals
 * of the list of traders for the latest pricing
 * information
 * 
 * @author bob
 * @author klinst
 */
public class IndicatorPanelMain 
{
  /** trace object */
  private static Logger trace =
    Logger.getLogger(IndicatorPanelMain.class);

	public static void main(String[] args) 
  {
		if (args.length < 3) 
    {
			System.out.println(
				"usage : "
					+ IndicatorPanelMain.class.getName()
					+ " tickerspath masterquotefilepath tradersfilepath");
			System.exit(0);
		}

    BasicConfigurator.configure();
    
		try 
    {
			String tickers_path = args[0];
      String traders_path = args[2];
      
			//		open tickers list
			FileReader tickers_filereader = new FileReader(tickers_path);
			BufferedReader tickers_reader = new BufferedReader(tickers_filereader);

			// open quote repository
			QuoteRepositoryImpl quotedb = new QuoteRepositoryImpl();
			quotedb.setMasterQuoteFile(args[1]);
			quotedb.load();
      			
      // parse file
			String line;
			ArrayList instruments = new ArrayList();
			
      while ((line = tickers_reader.readLine()) != null) 
      {
				instruments.add(line);
			}

      tickers_reader.close();
      tickers_filereader.close();
      
      //    open traders list
      FileReader traders_filereader = new FileReader(traders_path);
      BufferedReader traders_reader = new BufferedReader(traders_filereader);

      XmlTraderLoader traderloader = new XmlTraderLoader();
      AbstractTrader[] traders = traderloader.load(traders_reader);
      TraderContainer container = new TraderContainer(traders);
      
      traders_reader.close();
      traders_filereader.close();
      
			int row = instruments.size();
			int col = 3 + traders.length;
      
			String[] column_names = new String[col];
      column_names[0] = "Inst";
      column_names[1] = "Date";
      column_names[2] = "Price";

			Object[][] data = new Object[row][col];

			int i = 0;
			for (Iterator it = instruments.iterator(); it.hasNext(); i++) 
      {
				String inst = (String) it.next();
				data[i][0] = inst;

        trace.info("Dealing with instrument " + inst);
        
				List list = quotedb.getQuotes(inst);
        ListMarketEngine market = new ListMarketEngine(list, inst);

        container.setMarket(market);
        container.init();
        
        // skip forward to the last year
        for (int j = 0; j < market.getDataSize() - 300; j++)
        {
          market.next();
        }
        
        trace.info("Skipped to last year");
        
        while(market.hasNext())
        {
          container.processOrders();
          market.next();
          container.updateTraders();
        }
         
        trace.info("Processed all orders for instrument");
        
        data[i][1] = market.getDate();
        data[i][2] = Float.toString(market.getClose(inst));

        for (int j = 0; j < traders.length; j++)
        {
          column_names[j + 3] = traders[j].getName();
          
          List orders = traders[j].getPendingOrders();
          if (orders != null && orders.size() > 0)
          {
            Order order = (Order)orders.get(0);
            if (order.getValue() > 0.0)
            {
              data[i][j + 3] = Integer.toString(IIndicator.BUY);
            }
            else
            {
              data[i][j + 3] = Integer.toString(IIndicator.SELL);
            }
          }
          else
          {
            data[i][j + 3] = Integer.toString(IIndicator.HOLD);
          }
        }
			}

			JTable tab =
				new JTable(
					data,
					column_names);
			tab.setDefaultRenderer(Object.class, new IndicatorRenderer());

			JScrollPane scrollPane = new JScrollPane(tab);

			JFrame frame = new JFrame("Trader Panel");
			frame.getContentPane().add(scrollPane);
			frame.setSize(800, 600);
			frame.setVisible(true);
			frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static public void add(IIndicator trader, 
      List list) 
  {
		for (Iterator it = list.iterator(); it.hasNext();) 
    {
			HistoricData data = (HistoricData) it.next();
			trader.add(data);
		}
	}
}
