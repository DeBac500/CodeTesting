package robotrader.quotedb.web;

import robotrader.quotedb.QuoteRepositoryImpl;
import java.io.FileReader;
import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.BasicConfigurator;

/**
 * Update quotes til up to date
 */
public class UpdateQuotesMain 
{

	public static void main(String[] args) 
  {
		// arguments checking
		if (args.length < 2) 
    {
			System.out.println(
				"usage : "
					+ UpdateQuotesMain.class.getName()
					+ " tickersfilepath xmlmasterquotefile [loadfrom US(default)|FR]");
			return;
		}
    
    BasicConfigurator.configure();
    
		// make start and enddate
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date currentdate = new Date();
		String current = df.format(currentdate);
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(currentdate);
		calendar.add(GregorianCalendar.MONTH, -6);
		Date prevdate = calendar.getTime();
		String last = df.format(prevdate);

		// 
		try {
			IQuotesLoader quotesloader;
			// 
			if ((args.length > 2) && (args[2].equals("FR"))) {
				quotesloader = new YahooFRHistoricLoader();
			} else {
				quotesloader = new YahooUSHistoricLoader();
			}

			// open tickers list
			FileReader filereader = new FileReader(args[0]);
			BufferedReader reader = new BufferedReader(filereader);

			// open quote repository
			QuoteRepositoryImpl quotedb = new QuoteRepositoryImpl();
			quotedb.setMasterQuoteFile(args[1]);
			quotedb.load();
			// parse file
			String line;
			while ((line = reader.readLine()) != null) {
				// get dates from quotedb
				String start = quotedb.getStartDate(line);
				if (start == null) {
					start = last;
				}
				String end = quotedb.getEndDate(line);
				if (end == null) {
					end = last;
				}
				// 
				String from = (last.compareTo(start) < 0) ? last : end;
				String to = (current.compareTo(end) > 0) ? current : start;
				if (from.compareTo(to) <= 0) {
					System.out.println(
						quotesloader.toString()
							+ ": loading "
							+ line
							+ " "
							+ from
							+ "-"
							+ to);
					quotedb.addQuoteList(
						quotesloader.loadQuotes(line, from, to));
					System.out.println(quotesloader.getStatus());
					System.out.println("done " + line + ".");
				}
			}
			System.out.println("saving quote DB...");
			quotedb.save();
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			System.exit(-1);
			return;
		}

		System.out.println("done.");
		System.exit(0);

	}
}
