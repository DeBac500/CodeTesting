package lu.base.iris.services;

import lu.base.iris.AbstractService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/**
 * UserPrefService : service implementation 
 * storing/restoring user preferences
 * 
 * @author xcapt
 * @author klinst
 */
public class UserPrefService extends AbstractService 
{
  /**
   * The logger instance
   */
  private static final Logger trace = Logger.getLogger(UserPrefService.class);
  
  /**
   * The comment for the property file
   */
	public final static String HEADER = "User Preferences";
  
  /**
   * The preference file name
   */
	private String _preffile = null;
  
  /**
   * The default resource bundle file name
   */
	private String _defaultres = null;
  
  /**
   * The default resource bundle loaded
   * from default property file
   */
	private ResourceBundle _defaultbundle = null;
  
  /**
   * The user preference properties
   */
	private Properties _prop = null;

	/**
	 * UserPref constructor.
   * 
   * @param preffile The preference property file name
   * @param defaultres The default resource file name
	 */
	public UserPrefService(String preffile, String defaultres) 
  {
		super();

		_preffile = preffile;
		_defaultres = defaultres;
	}

	/**
	 * Saves the properties to the preference file.
   * 
   * @throws java.io.IOException If a problem
   * occurred while saving the properties
	 */
	private void saveParameters() throws IOException 
  {
		if (_prop != null) 
    {
      FileOutputStream stream = new FileOutputStream(_preffile);
			_prop.store(stream, HEADER);
      stream.close();
		}
	}

	/**
	 * @see ch.eri.iris.IService#cleanup()
	 */
	public void cleanup() throws IOException {
		try {
			saveParameters();
		} 
		catch (FileNotFoundException e) {			
			trace.error("File could not be found!", e);
			throw e;
		} 
		catch (IOException e) {
			trace.error("IOException occurred!", e);
			throw e;
		}
	}

	/**
	 * Initialises the service. Loads the default
   * resource bundle from the default resource
   * file. Loads the properties from the
   * preference file.
   * 
   * @throws java.io.IOException if either
   * file cannot be found or loaded
	 */
	public boolean init() throws IOException
  {
		try 
    {
			// check default file existence
			_defaultbundle = ResourceBundle.getBundle(_defaultres);			
			_prop = new Properties();
      FileInputStream stream = new FileInputStream(_preffile);
			_prop.load(stream);
      stream.close();
      
			return true;
		} 
    catch (FileNotFoundException e) 
    {
			trace.error("File could not be found!", e);
      throw e;
		} 
    catch (IOException e) 
    {
      trace.error("IOException occurred!", e);
      throw e;
		}    
	}

	/**
	 * Gets the property value for the given key.
   * If the property does not exist, the default
   * resource bundle value for the key is used.
   * If that does not exist, the defaultvalue
   * is returned.
   * 
   * @param key The property key
   * @param defaultValue The default value to use if
   * key value cannot be found
	 */
	public String getStringValue(String key, String defaultValue) 
	{
		String v = _prop.getProperty(key);
		
		if (v == null) {
			try {
				v = getDefaultParameters().getString(key);
			} 
			catch (java.util.MissingResourceException e) {
				return defaultValue;
			}
		}
		return (v == null) ? defaultValue : v;
	}

  /**
   * Gets the property value for the
   * given key. If the value does not 
   * exist, then it returns null.
   * 
   *  @param key The key for the value
   *  @param The value for the key
   */
	public String getStringValue(String key) {
		return getStringValue(key,null);
	}

	/**
	 * Associates the key with the value
   * @param key The key
   * @param value The value
	 */
	public void setStringValue(String key, String value) {
		_prop.setProperty(key, value);
	}
  
	/**
	 * Method getDefaultParameters.
	 * @return ResourceBundle
	 */
	private ResourceBundle getDefaultParameters() {
		return _defaultbundle;
	}
}