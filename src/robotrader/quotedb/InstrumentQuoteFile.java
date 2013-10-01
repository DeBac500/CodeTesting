package robotrader.quotedb;

import org.apache.log4j.Logger;
import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;

import robotrader.market.HistoricData;

/**
 * The instrument quote file represents an object
 * which reads quotes from / writes quotes to a
 * text file.
 * 
 * @author bob
 * @author klinst
 */
public class InstrumentQuoteFile 
{
  /** trace object */
  private static Logger trace =
    Logger.getLogger(InstrumentQuoteFile.class);

	/**
	 * The regular expression for parsing
	 * a quote file
	 */
	private static RE _re;
	
	/**
	 * The simple date format
	 */
	private static SimpleDateFormat _df = new SimpleDateFormat("yyyyMMdd");

	/**
	 * The list of Historic Data loaded
	 */
	protected List _data = new ArrayList();
	
	/**
	 * The set of available Historic Data
	 * @TODO not used anywhere?
	 */
	protected HashSet _available = new HashSet();
	
	/**
	 * The filename the stock quote data
	 * are loaded from / saved to
	 */
	private String _filename;
	
	/**
	 * Have the stock quotes been updated?
	 */
	//private boolean _updated = false;

	/**
	 * Creates a new instrument quote file object. The
	 * historic quotes are loaded from the filename
	 * if the create flag is false. The quotes are
	 * later written to the quote file.
	 * 
	 * @param filename The file name to read from/ write to
	 * @param create Whether to read quotes from the file
	 * (false) or to create them (true)
	 */
	public InstrumentQuoteFile(String filename, boolean create) 
	{
		_filename = filename;
		
		if (!create) 
		{
			try
			{
				loadFile(filename);
			}
			catch(Exception e)
			{
        trace.error("Could not load file " + filename + " because " + e.getMessage());
			}
		}
	}

	/**
	 * The regular expression describing the
	 * pattern saved to/ loaded from file.
	 * 
	 * @return The regular expression for the
	 * underlying string pattern
	 */
	private static RE getRE() 
	{
		if (_re == null) 
		{
			try 
			{
				StringBuffer buf = new StringBuffer();
				buf.append("([\\w\\.]+),([\\d]+),([\\d.]+),");
				buf.append("([\\d.]+),([\\d.]+),([\\d.]+),([\\d.(E)?]+),([\\d.]+)");

				_re = new RE(buf.toString());
			} 
			catch (RESyntaxException e) 
			{
				System.err.println(e);
			}
		}

		return _re;
	}

	/**
	 * Loads historic stock quotes from a file.
	 * 
	 * @param filename The file name of the file
	 * containing the quotes
	 */
	private void loadFile(String filename) 
		throws IOException, ParseException
	{
		FileReader file_reader = null;
		BufferedReader buffered_reader = null;
		
		try 
		{
			file_reader = new FileReader(filename);
			buffered_reader = new BufferedReader(file_reader);
			String line = null;

			while ((line = buffered_reader.readLine()) != null) 
			{
				HistoricData data = parseLine(line);
				if (data != null)
				{
					_data.add(data);
				}
			}
		} 
		finally
		{
			buffered_reader.close();
			file_reader.close();
		}
	}

	/**
	 * Parses a single line into a stock quote.
	 * 
	 * @param line The line to be parsed
	 * @return A new stock quote pricing information
	 * extracted from the line
	 * @throws ParseException If the parsing of the
	 * line was unsuccessful
	 */
	private HistoricData parseLine(String line) 
		throws ParseException
	{
		if (getRE().match(line)) 
		{      
			HistoricData data =
				new HistoricData(
					getRE().getParen(1),
					_df.parse(getRE().getParen(2)),
					Float.parseFloat(getRE().getParen(3)),
					Float.parseFloat(getRE().getParen(4)),
					Float.parseFloat(getRE().getParen(5)),
					Float.parseFloat(getRE().getParen(6)),
					Float.parseFloat(getRE().getParen(7)),
          Float.parseFloat(getRE().getParen(8)));
			_available.add(data.getInstrument());
			return data;
		}

		return null;
	}

	/**
	 * Gets the HashSet of available instruments.
	 * 
	 * @return
	 * @TODO not used?
	 */
	public Collection getAvailableInstruments() 
	{
		return _available;
	}

	/**
	 * The number of stock quotes stored in this
	 * object.
	 * 
	 * @return The number of stock quotes
	 */
	public int getDataSize() 
	{
		return _data.size();
	}

	/**
	 * Gets the list of stock quotes
	 * 
	 * @return A list of HistoricData objects
	 */
	public List getQuotes() 
	{
		return _data;
	}
	
	/**
	 * Adds a historic data quote. Only adds quotes
	 * if they are older/ more recent than the
	 * already loaded quotes
	 * 
	 * @param data The stock quote to add
	 */
	public void addQuote(HistoricData data) 
	{
		if (_data.size() == 0) 
		{
			_data.add(data);
			//_updated = true;
		} 
		else 
		{
			HistoricData begdata = (HistoricData) _data.get(0);
			HistoricData enddata = (HistoricData) _data.get(_data.size() - 1);

			if (data.getDate().compareTo(begdata.getDate()) < 0) 
			{
				_data.add(0, data);
				//_updated = true;
			} 
			else if (data.getDate().compareTo(enddata.getDate()) > 0) 
			{
				_data.add(data);
				//_updated = true;
			}
		}
	}

	/**
	 * Saves the stock quote data to a file.
	 *
	 */
	public void save() throws IOException
	{
		/*if (!_updated) 
		{
			return;
		}*/
		
		FileWriter writer = new FileWriter(_filename);

		for (Iterator it = _data.iterator(); it.hasNext();) 
		{
			HistoricData data = (HistoricData) it.next();
			writer.write(toString(data));
			writer.write("\r\n");
		}
		writer.close();
		//_updated = false;
	}

	/**
	 * Transforms a stock quote object to a string
	 * representation.
	 * 
	 * @param data
	 * @return
	 */
	static String toString(HistoricData data) 
	{
		//		GE,19900102,4.05,4.19,4.01,4.18,1843400
		StringBuffer buf = new StringBuffer(data.getInstrument());
		buf.append(",");
		buf.append(_df.format(data.getDate()));
		buf.append(",");
		buf.append(data.getOpen());
		buf.append(",");
		buf.append(data.getHigh());
		buf.append(",");
		buf.append(data.getLow());
		buf.append(",");
		buf.append(data.getClose());
		buf.append(",");
		buf.append(data.getVolume());
    buf.append(",");
    buf.append(data.getAdjustedClose());

		return buf.toString();
	}
}
