package robotrader.quotedb.web;

import org.apache.log4j.Logger;
import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;

/**
 * Quotes loader for Yahoo! DE (www.yahoo.de) quotes in csv format.
 * 
 * @author klinst
 */
public class YahooDEHistoricLoader extends BaseYahooLoader 
{
	/** trace object */
	private static Logger trace =
		Logger.getLogger(YahooDEHistoricLoader.class);

	/**
	 * The date Regular expression
	 */
	private static RE _datere;
	
	/**
	 * constructor
	 */
	public YahooDEHistoricLoader()
	{
		super();
		_splitMonth = true; // Yahoo DE does not seem to allow 
							// downloading data before 2002 in one chunk
	}
	/**
	 * Transforms the Yahoo date to
	 * our date format.
	 * @param date The Yahoo date
	 * @return The robotrader date
	 */
	protected String transformDate(String date) 
		throws RESyntaxException
	{
		if (getDateRE().match(date)) 
		{
			StringBuffer buf = new StringBuffer();
			String yr = getDateRE().getParen(1);
			buf.append(yr);
						
			String mon = getDateRE().getParen(2);
			buf.append(mon);
			
			String day = getDateRE().getParen(3);	
			if (day.length() == 1) 
			{
				buf.append("0");
			}
			buf.append(day);
			return buf.toString();

		}
		return null;
	}
		
	/**
	 * Method makeUrlString. Creates the Url for
	 * loading from Yahoo! De
	 * 
	 * @param instrument ticker symbol for Yahoo
	 * @param startdate start date for quote loading with format: YYYYMMDD
	 * @param enddate end date for quote loading with format: YYYYMMDD
	 * @return String url for yahoo file loading
	 */
	protected String makeUrlString(
		String instrument,
		String startdate,
		String enddate) 
	{

		StringBuffer buf = new StringBuffer();
		buf.append("http://de.table.finance.yahoo.com/table.csv?a=");

		buf.append(zeroBased(startdate.substring(4, 6)));
		buf.append("&b=");
		buf.append(startdate.substring(6, 8));
		buf.append("&c=");
		buf.append(startdate.substring(0, 4));
		buf.append("&d=");
		buf.append(zeroBased(enddate.substring(4, 6)));
		buf.append("&e=");
		buf.append(enddate.substring(6, 8));
		buf.append("&f=");
		buf.append(enddate.substring(0, 4));
		buf.append("&s=");
		buf.append(instrument);
		buf.append("&y=0&g=d&ignore=.csv");
		return buf.toString();
	}

	/**
	 * Get the name of the loader.
	 */
	public String toString() 
	{
		return "Yahoo DE";
	}

	/**
	 * Gets the Regular Expression used for this
	 * loader.
	 *  
	 * @return The RE
	 */
	private static RE getDateRE() 
		throws RESyntaxException
	{
		if (_datere == null) 
		{
			try 
			{
				_datere = new RE("^([\\d]+)-([\\d]+)-([\\d]+)");
			} 
			catch (RESyntaxException e) 
			{
				trace.error("Regular expression could not be parsed", e);
				throw e;
			}
		}
		return _datere;
	}
}
