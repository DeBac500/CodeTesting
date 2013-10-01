package robotrader.quotedb.web;

import org.apache.log4j.Logger;
import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;

/**
 * Quotes loader for Yahoo! US (www.yahoo.com) quotes in csv format. since
 * 01.2003, month and days are zero based
 */
public class YahooUSHistoricLoader extends BaseYahooLoader {
	
	/** trace object */
	private static Logger trace =
		Logger.getLogger(YahooUSHistoricLoader.class);

	/**
	 * The regular expression object
	 */
	private static RE _datere;

	/**
	 * Method makeUrlString.
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
		buf.append("http://table.finance.yahoo.com/table.csv?s=");
		buf.append(instrument);
		buf.append("&a=");
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
		buf.append("&g=d&y=0&z=");
		buf.append(instrument);
		buf.append("&ignore=.csv");
		return buf.toString();
	}

	/**
	 * 
	 * @return
	 */
	private static RE getDateRE() 
	{
		if (_datere == null) 
		{
			try 
			{
				_datere = new RE("^([\\d]+)-([\\d]+)-([\\d]+)");
			} 
			catch (RESyntaxException e) 
			{
				trace.error("Regular expression syntax wrong!", e);
			}
		}
		return _datere;
	}

	/**
	 * transforms the yahoo date into our
	 * simple date format.
	 * 
	 * @param date The yahoo date as string
	 * @return The date in format yyyymmdd
	 */
	protected String transformDate(String date) 
	{
		if (getDateRE().match(date)) {
			StringBuffer buf = new StringBuffer();
			
			String yr = getDateRE().getParen(1);
			buf.append(yr);
			
			String mon = getDateRE().getParen(2);
			buf.append(mon);
			
			String day = getDateRE().getParen(3);
			if (day.length() == 1) {
				buf.append("0");
			}
			buf.append(day);
			return buf.toString();
		}
		return null;
	}

	/**
	 * Gets the name of the loader
	 * @return Its name
	 */
	public String toString() 
	{
		return "Yahoo US";
	}
}
