package lu.base.iris.services;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import lu.base.iris.AbstractService;

/**
 * This class implements an IService for loading configuration (.properties)
 * file using the ResourceBundle.
 * 
 * @author xcapt
 * @author klinst
 */
public class ConfigurationService extends AbstractService 
{
  /**
   * The Logging object
   */
  private Logger trace = Logger.getLogger(ConfigurationService.class);

  /**
   * The bundle for the properties
   */
	private ResourceBundle _basebundle;
  
  /**
   * The configuration properties file
   */
	String _conffile;
  
	/**
	 * Method ConfigurationService.
	 * @param conffile resource name for 
   * primary configuration file
	 */
	public ConfigurationService(String conffile) 
  {
		super();
		_conffile = conffile;
	}

	/**
	 * loads the main bundle and retrieves the available 
   * configuration from it.
	 */
	public boolean init() 
  {
    try
    {
      _basebundle = ResourceBundle.getBundle(_conffile);
      trace.info("Configuration file " + _conffile + " successfully loaded!");
    }
    catch(Exception e)
    {
      trace.error("Configuration file " + _conffile + " cannot be loaded!", e);
      return false;
    }
    
		return true;
	}

	/**
	 * does nothing
	 */
	public void cleanup() 
  {
	}

	/**
	 * Method getString: retrieves the value found in the configuration for
	 * the key. Tries to get the String from the primary configuration file (not
	 * configuration name dependant), and eventually tries to get the String.
   * 
	 * @param key configuration key
	 * @return String value for the configuration key 
	 * @throws MissingResourceException when key not found
	 */
	public String getString(String key) throws MissingResourceException 
  {
		if (_basebundle != null) 
    {
      try 
      {
        return _basebundle.getString(key);
      } 
      catch (MissingResourceException e) 
      {
        trace.warn("The configuration key " + key + " could not be found!");
      }
		}
    
    return key;
	}
}