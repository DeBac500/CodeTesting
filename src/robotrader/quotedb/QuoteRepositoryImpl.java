package robotrader.quotedb;

import java.io.IOException;
import java.util.List;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collections;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import robotrader.market.HistoricData;

/**
 * IQuoteRepository implementation. The quotes
 * are manipulated via the InstrumentQuoteFile
 * objects and the metadata information is
 * kept in a XmlMasterQuoteFile.
 * 
 * @author xcapt
 * @author klinst
 */
public class QuoteRepositoryImpl implements IQuoteRepository 
{
	/** trace object */
	private static Logger trace =
		Logger.getLogger(QuoteRepositoryImpl.class);

	/**
	 * The master quote file object
	 */
	private XmlMasterQuoteFile _masterfile;
	
	/**
	 * A map of institution to its instrument
	 * quote file
	 */
	private HashMap _instmap = new HashMap();
	
	/**
	 * The master quote file name
	 */
	private String _masterquotefile = "conf/quotes.xml";
	
	/**
	 * Adds a list of historic quotes. The master
	 * xml file is updated as is the text file
	 * containing the actual quote data for
	 * the underlying instrument.
	 * 
	 * @param quotelist A list of historic quotes
	 * @see robotrader.quoterepository.IQuoteRepository#addQuoteList(java.util.List)
	 */
	public void addQuoteList(List quotelist) 
	{
		if (quotelist == null) 
		{
			return;
		}
		
		if (quotelist.size() == 0) 
		{
			return;
		}
		
		String instrument = ((HistoricData) quotelist.get(0)).getInstrument();
		InstrumentQuoteFile qfile = getQuoteFile(instrument);
		
		for (Iterator it = quotelist.iterator(); it.hasNext();) 
		{
			HistoricData data = (HistoricData) it.next();
			_masterfile.addQuote(data);
			qfile.addQuote(data);
		}
		
		save();
		_masterfile.saveFile();
	}

	/**
	 * Gets the Instrument Quote File Object
	 * responsible for serialising the data
	 * for the instrument. If it does not 
	 * exist, a new object is created for
	 * the instrument.
	 * 
	 * @param instrument The underlying stock
	 * @return The quote file object responsible
	 * for serialising the quotes.
	 */
	private InstrumentQuoteFile getQuoteFile(String instrument) 
	{
		InstrumentQuoteFile qfile =
			(InstrumentQuoteFile) _instmap.get(instrument);

		if (qfile == null) 
		{
			String filename = _masterfile.getFile(instrument);
			boolean create = false;
			
			if (filename == null) 
			{
				filename = _masterfile.newFileRef(instrument);
				create = true;
			}
			
			qfile =
				new InstrumentQuoteFile(
					_masterfile.getBasePath() + filename,
					create);
			_instmap.put(instrument, qfile);
		}
		return qfile;
	}

	/**
	 * Gets the quotes for the instrument. If the
	 * quote file exists and has been registered
	 * in the master xml file, then the quotes
	 * are loaded from file. If the quote file
	 * does not exist, then it is created. 
	 * 
	 * @param instrument The underlying stock
	 * @return A list of HistoricData objects
	 * @see robotrader.quotedb.IQuoteRepository#getQuotes(java.lang.String)
	 */
	public List getQuotes(String instrument) 
	{
		InstrumentQuoteFile qfile =
			(InstrumentQuoteFile) _instmap.get(instrument);
		
		if (qfile == null) 
		{
			String filename = _masterfile.getFile(instrument);
			
			if (filename == null) 
			{
				trace.error("The instrument quote file name for instrument "
						+ instrument + " cannot be found!");
				return null;
			}
			
			qfile =
				new InstrumentQuoteFile(
					_masterfile.getBasePath() + filename,
					false);
			_instmap.put(instrument, qfile);
		}
		
		return qfile.getQuotes();
	}

	/**
	 * Gets a list of available instruments as 
	 * specified by the xml master file.
	 * 
	 * @return A list of available instruments
	 * @see robotrader.quotedb.IQuoteRepository#getInstruments()
	 */
	public Collection getInstruments() 
	{
		ArrayList instruments = new ArrayList();
		for (Iterator it = _masterfile.getInstruments().iterator(); it.hasNext(); ) 
		{
			instruments.add(it.next());
		}
		Collections.sort(instruments);
		return instruments;
	}

	/**
	 * Method load.
	 */
	public void load() 
	{
		_masterfile = new XmlMasterQuoteFile(_masterquotefile);
	}

	/**
	 * Saves the master xml file as well as all
	 * the quote files for the available instruments.
	 *
	 */
	public void save() 
	{
		_masterfile.saveFile();
		for (Iterator it = _instmap.values().iterator(); it.hasNext();) 
		{
			InstrumentQuoteFile qfile = (InstrumentQuoteFile) it.next();
			
			try
			{
				qfile.save();	
			}
			catch (IOException e)
			{
				trace.error("The instrument quote file could not be saved! " + e.toString());
			}
		}
	}

	/**
	 * Sets the master quote file name.
	 * 
	 * @param masterquotefile The file name of the
	 * master quote file
	 */
	public void setMasterQuoteFile(String masterquotefile) 
	{
		_masterquotefile = masterquotefile;
	}

	/**
	 * Gets the master quote file
	 * @return The master quote file name
	 */
	public String getMasterQuoteFile() 
	{
		return _masterquotefile;
	}
	
	/**
	 * Method getStartDate.
	 * @param instrument
	 * @return String
	 */
	public String getStartDate(String instrument) 
	{
		return _masterfile.getStartDate(instrument);
	}
	
	/**
	 * Method getEndDate.
	 * @param instrument
	 * @return String
	 */
	public String getEndDate(String instrument) 
	{
		return _masterfile.getEndDate(instrument);
	}

	/**
	 * removes the instrument from the master
	 * quote file.
	 * 
	 * @param instrument The stock to delete
	 * @see robotrader.quotedb.IQuoteRepository#removeQuotes(java.lang.String)
	 */
	public void removeQuotes(String instrument) 
	{
		_masterfile.removeInstrument(instrument);
		//InstrumentQuoteFile qfile =
		//	(InstrumentQuoteFile) _instmap.remove(instrument);
		// removed file disabled, it does not clean up file system of quote file, bue this way 
		// the downloaded file may be used again for other purposes
		//qfile.delete();
		_masterfile.saveFile();
	}
}
