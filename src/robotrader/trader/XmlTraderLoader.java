package robotrader.trader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import robotrader.engine.model.PropertyMetaData;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.IXMLParser;
import net.n3.nanoxml.IXMLReader;
import net.n3.nanoxml.StdXMLReader;
import net.n3.nanoxml.XMLElement;
import net.n3.nanoxml.XMLParserFactory;
import net.n3.nanoxml.XMLWriter;

/**
 * Loads trader name and properties from XML document. 
 * Instantiates the traders and adds the properties
 * to them.
 * 
 * @author bob
 * @author klinst
 */
public class XmlTraderLoader 
{
	private static final Logger trace = Logger.getLogger(XmlTraderLoader.class);
	
	/**
	 * loads the list of traders using a reader that reads
	 * from a XML document. The traders are instantiated using
	 * recursion.
	 * 
	 * @param r the reader for the XML document
	 * @return an array of traders corresponding to the traders in the XML document 
	 */
	public AbstractTrader[] load(Reader r) 
	{
		Vector traders = new Vector();
		try 
		{
			IXMLParser parser = XMLParserFactory.createDefaultXMLParser();
			IXMLReader reader = new StdXMLReader(r);
			parser.setReader(reader);
			IXMLElement root = (IXMLElement) parser.parse();
			parseTraders(root, traders);
		} 
		catch (Exception e) 
		{
			trace.error("Could not load trader list", e);
			return new AbstractTrader[0];
		}

		AbstractTrader[] result = new AbstractTrader[traders.size()];
		for (int i = 0; i < traders.size(); i++) 
		{
			result[i] = (AbstractTrader) traders.get(i);
		}
		return result;
	}

  public void save(AbstractTrader[] traders, Writer w)
  {
    XMLWriter writer = new XMLWriter(w);
    IXMLElement traders_element = new XMLElement("traders");
    
    for (int i = 0; i < traders.length; i++)
    {
      IXMLElement trader_element = new XMLElement("trader");
      String classname = traders[i].getClass().getName();
      trader_element.setAttribute("class", classname);
      
      PropertyMetaData[] data = traders[i].getPropertyMetaData();
      
      for (int j = 0; j < data.length; j++)
      {
        String property = traders[i].getProperty(data[j].getKey());
        
        if (property != null && property.length() > 0)
        {
          IXMLElement property_element = new XMLElement("property");
          property_element.setAttribute("key", data[j].getKey());
          property_element.setAttribute("value", property);
          trader_element.addChild(property_element);
        }
      }
      
      traders_element.addChild(trader_element);
    }
    
    try
    {
      writer.write(traders_element);
    }
    catch(IOException ioe)
    {
      trace.error("Could not save trader list", ioe);
    }
      
  }
  
	/**
	 * gets all children nodes in the tree that are named
	 * "trader". parses those nodes and instantiates the
	 * corresponding traders.
	 * 
	 * @param elt the root of the node hierarchy
	 * @param traders the Vector of traders
	 */
	private void parseTraders(IXMLElement elt, Vector traders) 
	{
		Vector telts = elt.getChildrenNamed("trader");
		for (Iterator it = telts.iterator(); it.hasNext();) 
		{
			IXMLElement child = (IXMLElement) it.next();			
			AbstractTrader trader = parseTrader(child);
			
			if (trader != null) 
			{
				traders.add(trader);
			}
		}
	}

	/**
	 * instantiates a single trader using the name
	 * and properties of the XML node.
	 *  
	 * @param elt the XML element containing the trader information
	 * @return the instantiated trader injected with properties
	 */
	private AbstractTrader parseTrader(IXMLElement elt) 
	{
		String classname = elt.getAttribute("class", "");
		
		if ((classname == null) || (classname.trim().equals(""))) 
		{
			trace.warn("Could not find trader with class " + classname);
			return null;
		}

		try 
		{
			Class tclass = Class.forName(classname);
			AbstractTrader t = (AbstractTrader) tclass.newInstance();
			parseProperties(t, elt);

			return t;
		} 
		catch (Exception e) 
		{
			trace.error("Could not instantiate trader " + classname, e);
			return null;
		}
	}

	/**
	 * adds all property - value pairs of the
	 * xml element to the given trader.
	 * 
	 * @param trader the trader to add the properties to
	 * @param elt the xml element containing the properties
	 */
	private void parseProperties(AbstractTrader trader, IXMLElement elt) 
	{
		Vector pelts = elt.getChildrenNamed("property");
		
		for (Iterator it = pelts.iterator(); it.hasNext();) 
		{
			IXMLElement pelt = (IXMLElement) it.next();
			String key = pelt.getAttribute("key", "");
			String value = pelt.getAttribute("value", "");
			
			if (((key != null) && (!key.trim().equals("")))
				&& ((value != null) && (!value.trim().equals("")))) 
			{
				trader.setProperty(key, value);
			}
		}
	}
}
