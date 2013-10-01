package robotrader.quotedb;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Vector;

import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;

import org.apache.log4j.Logger;

import robotrader.market.HistoricData;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.IXMLParser;
import net.n3.nanoxml.IXMLReader;
import net.n3.nanoxml.StdXMLReader;
import net.n3.nanoxml.XMLElement;
import net.n3.nanoxml.XMLParserFactory;
import net.n3.nanoxml.XMLWriter;

/**
 * The master quote file reads the stock quote
 * metadata information from an xml file. The
 * metadata information includes the quote
 * text file name, the start and end date of
 * the quotes as well as the code for the
 * instrument.
 *  
 * @author bob
 * @author klinst
 */
class XmlMasterQuoteFile 
{
	/** trace object */
	private static Logger trace = Logger.getLogger(XmlMasterQuoteFile.class);

	/**
	 * The date format used for formatting date strings
	 */
	private static SimpleDateFormat _df = new SimpleDateFormat("yyyyMMdd");

	/**
	 * The map of instruments to their corresponding
	 * codes.
	 */
	private HashMap _eltmap = new HashMap();
	
	/**
	 * Has the object been updated?
	 */
	private boolean _updated = false;
	
	/**
	 * The base path 
	 */
	private String _basepath;
	
	/**
	 * The root xml element
	 */
	private IXMLElement _root;
	
	/**
	 * The filename used for serialisation
	 */
	private String _filename;

	/**
	 * Creates a new xml master quote file object.
	 * Reads the Xml structure from file and
	 * extracts the necessary information
	 * 
	 * @param filename The Xml file name
	 */
	XmlMasterQuoteFile(String filename) 
	{
		_filename = filename;
		try {

			FileReader r = new FileReader(filename);
			IXMLParser parser = XMLParserFactory.createDefaultXMLParser();
			IXMLReader reader = new StdXMLReader(r);
			parser.setReader(reader);
			_root = (IXMLElement) parser.parse();
			_basepath = _root.getAttribute("basepath", "conf/");
			parseEntries(_root);
			r.close();
		} 
		catch (IOException e) 
		{
			trace.error("XmlMasterQuoteFile " + filename + " could not be initialised", e);
		} 
		catch (Exception e) 
		{
			trace.error("XmlMasterQuoteFile " + filename + " could not be initialised", e);
			e.printStackTrace();
		}
	}

	/**
	 * Parses the XML document for the instrument
	 * names and their corresponding codes.
	 * 
	 * @param elt The root element to search from
	 */
	private void parseEntries(IXMLElement elt) {

		Vector telts = elt.getChildrenNamed("instrument");
		for (Iterator it = telts.iterator(); it.hasNext();) {
			IXMLElement current = (IXMLElement) it.next();
			String inst = current.getAttribute("code", "");
			_eltmap.put(inst, current);
		}
	}

	/**
	 * Get the set of available instruments.
	 * 
	 * @return The set of instruments
	 */
	public Collection getInstruments() {
		return _eltmap.keySet();
	}

	/**
	 * Get the filename for the given instrument.
	 * 
	 * @param instrument The stock
	 * @return The file name containing the quote data
	 * for the stock
	 */
	String getFile(String instrument) 
	{
		IXMLElement elt = getXmlElement(instrument);
		
		if (elt == null) {
			trace.debug("The filename for instrument " + instrument + " cannot be found!");
			return null;
		}

		return elt.getAttribute("filename", "");
	}
	
	/**
	 * Gets the start date for the quotes for the
	 * instrument.
	 * 
	 * @param instrument The stock
	 * @return The start date as a string
	 */
	String getStartDate(String instrument) 
	{
		IXMLElement elt = getXmlElement(instrument);
		if (elt == null) {
			trace.error("The filename for instrument " + instrument + " cannot be found!");
			return null;
		}

		return elt.getAttribute("startdate", "");
	}
	
	/**
	 * Gets the end date for the quotes for the
	 * instrument.
	 * 
	 * @param instrument The stock
	 * @return The end date as a string
	 */
	String getEndDate(String instrument) {
		IXMLElement elt = getXmlElement(instrument);
		if (elt == null) {
			trace.error("The filename for instrument " + instrument + " cannot be found!");
			return null;
		}

		return elt.getAttribute("enddate", "");
	}

	/**
	 * Gets the xml element for the instrument.
	 * 
	 * @param instrument The stock
	 * @return The xml element
	 */
	private IXMLElement getXmlElement(String instrument) 
	{
		return (IXMLElement) _eltmap.get(instrument);
	}

	/**
	 * Saves the xml document to file if
	 * it has been updated. 
	 *
	 */
	void saveFile() 
	{
		if (!_updated) 
		{
			return;
		}

		try 
		{
			FileWriter w = new FileWriter(_filename);
			XMLWriter writer = new XMLWriter(w);
			writer.write(_root);
			w.close();

			_updated = false;
		} 
		catch (IOException e) 
		{
			trace.error("The Xml Master file could not be saved", e);
		}
	}

	/**
	 * Adds the stock quote information to the
	 * master file. If the instrument did not have
	 * any quote information before, then the 
	 * information is created. Otherwise, the
	 * start and end dates are updated.
	 *  
	 * @param data A quote data object.
	 */
	void addQuote(HistoricData data) 
	{
		_updated = true;
		String date = _df.format(data.getDate());
		IXMLElement elt = getXmlElement(data.getInstrument());
		if (elt == null) 
		{
			elt = new XMLElement("instrument");
			_eltmap.put(data.getInstrument(), elt);
			elt.setAttribute("code", data.getInstrument());
			elt.setAttribute("filename", data.getInstrument()+".txt");
			elt.setAttribute("startdate", date);
			elt.setAttribute("enddate", date);
			_root.addChild(elt);
		} 
		else 
		{
			elt = (IXMLElement) _eltmap.get(data.getInstrument());
		}
		
		if ((date.compareTo(elt.getAttribute("startdate", "")) < 0)
			|| (elt.getAttribute("startdate", "").equals(""))) 
		{
			elt.setAttribute("startdate", date);
		} 
		else if (date.compareTo(elt.getAttribute("enddate", "")) > 0) 
		{
			elt.setAttribute("enddate", date);
		}
	}

	/**
	 * Gets the base path for the master file.
	 * 
	 * @return The base path
	 */
	String getBasePath() 
	{
		return _basepath;
	}

	/**
	 * Creates a new file reference for a
	 * new stock if it does not yet exist.
	 * 
	 * @param instrument The stock
	 * @return The filename for the quote
	 * file for the instrument
	 */
	String newFileRef(String instrument) 
	{
		IXMLElement elt = getXmlElement(instrument);
		if (elt == null) 
		{
			elt = new XMLElement("instrument");
			elt.setAttribute("code", instrument);
			_eltmap.put(instrument, elt);
			elt.setAttribute("filename", instrument + ".txt");
			_root.addChild(elt);
		}
		return elt.getAttribute("filename", "");
	}

	/**
	 * Removes a single instrument from the
	 * master xml document.
	 * 
	 * @param instrument The instrument
	 */
	void removeInstrument(String instrument) {
		_updated = true;
		_root.removeChild(getXmlElement(instrument));
		_eltmap.remove(instrument);
	}
}
