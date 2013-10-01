package robotrader.quotedb.web;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.apache.regexp.RESyntaxException;
/**
 * Quotes loader interface
 */
public interface IQuotesLoader 
{
	/**
	* load quotes .
	* @param instrument ticker symbol
	* @param startdate start date for quote loading with format: YYYYMMDD
	* @param enddate end date for quote loading with format: YYYYMMDD
	* @return List list of HistoricData objects ordered from oldest to
	* newest
	*/
	List loadQuotes(String instrument, String startdate, String enddate)
		throws IOException, ParseException, RESyntaxException;
	
	/**
	 * Method getStatus.
	 * @return String
	 */
	String getStatus();

	/**
	 * Method getName.
	 * @return String
	 */
	String toString();
}
