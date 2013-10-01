package robotrader.quotedb.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;

import robotrader.market.HistoricData;

/**
 * Base class for loading quotes from Yahoo
 * <br>
*/
public abstract class BaseYahooLoader implements IQuotesLoader 
{
	/** trace object */
	private static Logger trace =
		Logger.getLogger(BaseYahooLoader.class);

	/**
	 * The date format used by YAHOO
	 */
	private static SimpleDateFormat _yahoodf = new SimpleDateFormat("yyyyMMdd");
	
	/**
	 * The date format
	 */
	private static SimpleDateFormat _yyyymmdddf =
		new SimpleDateFormat("yyyyMMdd");

	/**
	 * The regular expression object
	 */
	private static RE _re;
	
	/**
	 * The HTTP Loader used for getting loading
	 * quote data from URL.
	 */
	private HttpLoader _httploader;
	
	/**
	 * Whether to split the transfer into chunks
	 * of months or not
	 */
	protected boolean _splitMonth = true;

	/** 
	 * byte counter for total number of bytes loaded during the call
	 */
	private int _loadedBytes;
	
	/**
	 * Method makeUrlString.
	 * 
	 * @param instrument ticker symbol for Yahoo
	 * @param startdate start date for quote loading with format: YYYYMMDD
	 * @param enddate end date for quote loading with format: YYYYMMDD
	 * @return String url for yahoo file loading
	 * 
	 */
	protected abstract String makeUrlString(
		String instrument,
		String startdate,
		String enddate);

	/**
	 * Method zeroBased: change number to zero based number.
	 * @param nbr
	 * @return String nbr-1
	 */
	protected final static String zeroBased(String nbr) 
	{
		return Integer.toString(Integer.parseInt(nbr) - 1);
	}
	
	/**
	 * load quotes from yahoo .
	 * @param instrument ticker symbol for Yahoo
	 * @param startdate start date for quote loading with format: YYYYMMDD
	 * @param enddate end date for quote loading with format: YYYYMMDD
	 * @return List list of HistoricData objects ordered from oldest to
	 * newest
	 */
	public final List loadQuotes(
		String instrument,
		String startdate,
		String enddate)
		throws IOException, ParseException, RESyntaxException 
	{

		Date start = _yyyymmdddf.parse(startdate);
		Date end = _yyyymmdddf.parse(enddate);
		ArrayList result = new ArrayList();
		
		_loadedBytes = 0; // initialize number of loaded bytes

		if (_splitMonth) 
		{
			//		split loading in one month pieces since yahoo does not allow too long periods
			Date current = start;
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(start);

			calendar.add(Calendar.MONTH, 6);
			Date next = calendar.getTime();

			// for each period load the data	
			while (next.getTime() < end.getTime()) {
				result.addAll(loadQuotesChunk(instrument, current, next));
				current = next;
				calendar.add(Calendar.MONTH, 6);
				next = calendar.getTime();
			}

			result.addAll(loadQuotesChunk(instrument, current, end));
		} else 
		{
			result.addAll(loadQuotesChunk(instrument, start, end));
		}
		return result;
	}

	/**
	 * 
	 * @param instrument
	 * @param startdate
	 * @param enddate
	 * @return
	 * @throws IOException
	 */
	private List loadQuotesChunk(
		String instrument,
		Date startdate,
		Date enddate)
		throws IOException, ParseException, RESyntaxException
	{
		String urlstr =
			makeUrlString(
				instrument,
				_yyyymmdddf.format(startdate),
				_yyyymmdddf.format(enddate));
		
		trace.debug(urlstr + " download");
		
		File tmpfile = getHTTPLoader().load(new URL(urlstr));
		if (tmpfile == null) 
		{
			trace.error(urlstr + " cannot be loaded!");
			return new ArrayList();
		}
		_loadedBytes += getHTTPLoader().getLoadedLength();
		List result = parseFile(instrument, tmpfile);
		tmpfile.delete();
		return result;
	}

	/**
	 * Method parseFile.
	 * @param instrument
	 * @param file
	 * @return List
	 * @throws IOException
	 */
	protected final List parseFile(String instrument, File file)
		throws IOException, ParseException, RESyntaxException
	{
		FileReader fr = new FileReader(file);
		BufferedReader reader = new BufferedReader(fr);
		String line = reader.readLine();
		if (line == null) 
		{
			throw new IOException("empty file found : " + file.getPath());
		}
		ArrayList<HistoricData> data = new ArrayList();
		
		while ((line = reader.readLine()) != null) 
		{
			HistoricData entry = parseLine(instrument, line);
			if (entry != null) 
			{
				// reversing sequence
				data.add(0, entry);
			}
		}
		
		reader.close();
		fr.close();
		
		return data;
	}

	/**
	 * Method parseLine.
	 * @param instrument
	 * @param line
	 * @return HistoricData
	 */
	protected final HistoricData parseLine(String instrument, String line)
	  throws ParseException, RESyntaxException
	{
		line.trim();
		if (getRE().match(line)) 
		{
			try {

				HistoricData data =
					new HistoricData(
						instrument,
						_yahoodf.parse(transformDate(getRE().getParen(1))),
						Float.parseFloat(getRE().getParen(2)),
						Float.parseFloat(getRE().getParen(3)),
						Float.parseFloat(getRE().getParen(4)),
						Float.parseFloat(getRE().getParen(5)),
						Float.parseFloat(getRE().getParen(6)),
						Float.parseFloat(getRE().getParen(7)));
				return data;
			} 
			catch (ParseException e) 
			{
				trace.error("error at line :" + line, e);
				throw e;
			}
		}
		return null;
	}

	/**
	 * Get the Regular Expression Object for
	 * Parsing the Yahoo date string.
	 * 
	 * @return The RE object
	 */
	private final static RE getRE() throws RESyntaxException 
	{
		if (_re == null) 
		{
			try 
			{
				_re =
					new RE("^([^,]+),([\\d.]+),([\\d.]+),([\\d.]+),([\\d.]+),([\\d.(E)?]+),([\\d.]+)");
			} 
			catch (RESyntaxException e) 
			{
				trace.error("RE Syntax could not be parsed", e);
				throw e;
			}
		}
		return _re;
	}
	
	/**
	 * Transforms the yahoo date to our date format 
	 * 
	 * @param date The Yahoo date in string format
	 * @return The date in string format
	 */
	protected abstract String transformDate(String date)
		throws RESyntaxException;

	/**
	 * Gets the status of the loading process
	 * @return The Status message
	 */
	public final String getStatus() 
	{
		if (_loadedBytes == 0) 
		{
			return "connecting";
		}
		
		return "loading... "
				+ _loadedBytes
				+ " bytes so far...";
	}

	/**
	 * Gets the HTTP Loader.
	 * 
	 * @return The Http Loader
	 */
	private HttpLoader getHTTPLoader() 
	{
		if (_httploader == null) 
		{
			_httploader = new HttpLoader();
		}
		return _httploader;
	}
}
