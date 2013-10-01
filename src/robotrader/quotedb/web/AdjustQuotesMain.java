package robotrader.quotedb.web;

import robotrader.market.HistoricData;
import robotrader.quotedb.QuoteRepositoryImpl;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.List;

import org.apache.log4j.BasicConfigurator;

/**
 * Adjusts the historic prices of instruments
 * for splits
 */
public class AdjustQuotesMain 
{
  static DateFormat _dateformat = DateFormat.getDateInstance();
  
	public static void main(String[] args) 
  {
		// arguments checking
		if (args.length < 2) 
    {
			System.out.println(
				"usage : "
					+ AdjustQuotesMain.class.getName()
					+ " tickersfilepath xmlmasterquotefile");
			return;
		}
    
    BasicConfigurator.configure();
    
		try 
    {
			// open tickers list
			FileReader filereader = new FileReader(args[0]);
			BufferedReader reader = new BufferedReader(filereader);

			// open quote repository
			QuoteRepositoryImpl quotedb = new QuoteRepositoryImpl();
			quotedb.setMasterQuoteFile(args[1]);
			quotedb.load();
			
      // parse file
			String line;
			while ((line = reader.readLine()) != null) 
      {
        System.out.println("Processing " + line);
        List quotes = quotedb.getQuotes(line);

        HistoricData current = null, previous = null;
        
        for (int i = 0; i < quotes.size(); i++)
        {
          current = (HistoricData)quotes.get(i);
          
          if (previous != null)
          {
            float current_close = current.getClose();
            float previous_close = previous.getClose();
            float factor = previous_close / current_close;
            
            if (factor > 1.25)
            {
              factor = guessFactor(previous_close, current_close);
              
              System.out.print("Suspicious change in closing price ");
              System.out.print("between " + _dateformat.format(previous.getDate()));
              System.out.println(" and " + _dateformat.format(current.getDate()));
              System.out.println(" Would you like to adjust the prices by " + factor + " to 1?");
              
              BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
              String input = in.readLine();
              
              if (input.equalsIgnoreCase("y"))
              {
                adjust(quotes, i, (float)factor);
                System.out.println("Adjusted prices for " + line + " until " + _dateformat.format(previous.getDate()));
              }
            }
          }
          
          previous = (HistoricData)quotes.get(i);
        }

        System.out.println("saving quote DB...");
        quotedb.save();

      }
		} 
    catch (Exception e) 
    {
			System.out.println(e);
			e.printStackTrace();
			System.exit(-1);
			return;
		}

		System.out.println("done.");
		System.exit(0);

	}
  
  private static void adjust(List quotes, 
    int to, float factor)
  {
    for (int i = 0; i < to; i++)
    {
      HistoricData quote = (HistoricData)quotes.get(i);
      
      quote.setClose(getValue(quote.getClose(), factor));
      quote.setOpen(getValue(quote.getOpen(), factor));
      quote.setHigh(getValue(quote.getHigh(), factor));
      quote.setLow(getValue(quote.getLow(), factor));
    }
  }
  
  private static float getValue(float original, float factor)
  {
    original /= factor;
    int cent = Math.round(original * 100.0f);
    float result = (float)cent / 100.0f;
    return result;
  }
  
  private static float guessFactor(float old, float now)
  {
    float factor = old / now;
    
    if (factor > 1.25 && factor < 1.38)
    {
      return 1.333333333333f;
    }
    if (factor > 1.4 && factor < 1.6)
    {
      return 1.5f;
    }
    if (factor > 1.6 && factor < 2.2)
    {
      return 2.0f;
    }
    if (factor > 2.6 && factor < 3.2)
    {
      return 3.0f;
    }
    if (factor > 3.6 && factor < 4.2)
    {
      return 4.0f;
    }
    if (factor > 4.6 && factor < 5.2)
    {
      return 5.0f;
    }
    if (factor > 5.6 && factor < 6.2)
    {
      return 6.0f;
    }
    if (factor > 6.6 && factor < 7.2)
    {
      return 7.0f;
    }
    if (factor > 7.6 && factor < 8.2)
    {
      return 8.0f;
    }
    if (factor > 8.6 && factor < 9.2)
    {
      return 9.0f;
    }
    if (factor > 9.6 && factor < 10.2)
    {
      return 10.0f;
    }
    if (factor > 98 && factor < 102)
    {
      return 100.0f;
    }

    System.out.println("Factor not found " + old + " " + now);
    System.exit(-1);
    
    return 0.0f;
  }
}
