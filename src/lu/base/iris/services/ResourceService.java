package lu.base.iris.services;

import java.net.URL;
import java.util.MissingResourceException;

import javax.swing.ImageIcon;

import lu.base.iris.AbstractService;

import org.apache.log4j.Logger;

/**
 * ResourceService description : service implementation for 
 * resource loading 
 * 
 * @author xcapt
 * @author klinst
 */
public class ResourceService extends AbstractService 
{
  /**
   * The Logging object
   */
	private Logger trace = Logger.getLogger(ResourceService.class);
  
  /**
   * The configuration service
   */
	private ConfigurationService _conf = null;
	
	/**
	 * initialises the service 
	 */
	public boolean init() 
  {
		if (getConfigurationService() == null)
			return false;

		return (_conf != null);
	}

	/**
	 * does nothing
	 */
	public void cleanup() 
  {
	}

	/**
	 * Method getIcon: retrieves the icon for the given reference key, using
	 * the configuration file to retrieve the resource name.
	 * @param ref configuration key
	 * @return ImageIcon icon
	 */
	public ImageIcon getIcon(String ref) 
  {
		try 
    {
			URL url = getURL(ref);
			if (url == null)
				return null;
			return new ImageIcon(url);
		} 
    catch (MissingResourceException e) 
    {
			trace.warn("Icon not found :" + ref);
			return null;
		}
	}
  
	/**
	 * Method getURL: retrieves the URL for the given reference key.
	 * @param ref configuration key
	 * @return URL url for the key
   * @throws java.util.MissingResourceException
	 */
	public URL getURL(String ref) throws MissingResourceException
  {
		String resourcename = getConfigurationService().getString(ref);
		URL url = getClass().getClassLoader().getResource(resourcename);
		return url;
	}

	/**
	 * Method getConfigurationService: get the configuration service.
	 * @return ConfigurationService
	 */
	private ConfigurationService getConfigurationService() 
  {
		if (_conf == null) 
    {
			_conf =
				(ConfigurationService) getService(ConfigurationService.class);
		}
		return _conf;
	}
}
