package lu.base.iris.services;

import java.net.URL;

import lu.base.iris.AbstractService;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * LoggerService description : service implementation 
 * initializing the logging tool (log4j).
 * 
 * @author xcapt
 * @author klinst
 */
public class LoggerService extends AbstractService 
{
	/** standard trace object */
	private static Logger trace =
		Logger.getLogger(LoggerService.class.getName());
	
  /**
	 * LoggerService constructor
	 */
	public LoggerService() {
		super();
	}

	/**
	 * configures the service using log.properties
	 */
	public boolean init() 
  {
		configure();
		trace.info("starting " + getName());

		return true;
	}

	/**
	 * Method configure: configures log4j using log.properties.
	 */
	protected void configure() 
  {
		URL url = getClass().getClassLoader().getResource("log.properties");
		if (url != null) 
    {
			PropertyConfigurator.configure(url);
		}
	}

	/**
	 * Does nothing 
	 */
	public void cleanup() 
  {
		trace.info("stopping " + getName());
	}
}