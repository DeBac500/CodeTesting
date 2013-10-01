package robotrader.gui.traders;

import java.io.FileReader;
import java.io.IOException;

import robotrader.trader.AbstractTrader;
import robotrader.trader.XmlTraderLoader;
import lu.base.iris.AbstractService;
import lu.base.iris.AppToolkit;
import lu.base.iris.services.UserPrefService;

import org.apache.log4j.Logger;

/**
 * A Traders Service. Responsible for loading
 * the traders from file and updating the
 * preference file with the latest trader
 * file used.
 * 
 * @author xcapt
 * @author klinst
 */
public class TradersService extends AbstractService
	implements ITradersRepository 
{
	/**
	 * The logger instance
	 */
	private static Logger trace = Logger.getLogger(TradersService.class);
	
	/**
	 * The XML traders file
	 */
	private String _tradersfile;
	
	/**
	 * The XML trader loader that
	 * loads the traders from the
	 * traders file
	 */
	private XmlTraderLoader _loader;
	
	/**
	 * The loaded traders array
	 */
	private AbstractTrader[] _traders;

	/** 
	 * Initialises the trader service. Gets the
	 * traders file name from the applications
	 * preference service.
	 * 
	 * @see lu.base.iris.IService#init()
	 */
	public boolean init() 
	{
		_loader = new XmlTraderLoader();
		
		// load trader's file name from user preferences
		UserPrefService prefserv =
			(UserPrefService) AppToolkit.getService(UserPrefService.class);
		String tradersfile = prefserv.getStringValue("TRADERS_FILE");
		
		// set trader's file
		setTradersFile(tradersfile);
		return true;
	}

	/**
	 * does nothing
	 * @see lu.base.iris.IService#cleanup()
	 */
	public void cleanup() 
	{
	}

	/**
	 * Gets the array of traders
	 * 
	 * @return Array of traders
	 * @see robotrader.traders.ITradersRepository#getAll()
	 */
	public AbstractTrader[] getAll() 
	{
		return _traders;
	}

	/**
	 * Gets the traders file name.
	 * 
	 * @return The traders file name
	 */
	public String getTradersFile() 
	{
		return _tradersfile;
	}

	/**
	 * Sets the traders file name. The XML
	 * trader loader loads all the traders
	 * from file. The user preference
	 * service updates the trader's file
	 * used.
	 * 
	 * @param tradersFile The xml traders file
	 */
	public void setTradersFile(String tradersFile) 
	{
		FileReader reader;
		try 
		{
			reader = new FileReader(tradersFile);
			_traders = _loader.load(reader);
			
			UserPrefService prefserv =
						(UserPrefService) AppToolkit.getService(UserPrefService.class);
			prefserv.setStringValue("TRADERS_FILE",tradersFile);
			_tradersfile = tradersFile;
			reader.close();
		} 
		catch (IOException e) 
		{
			trace.warn("Trader\'s file not found :" + tradersFile);
			_traders = new AbstractTrader[0];
		}
	}
}
