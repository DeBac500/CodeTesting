package robotrader.quotedb.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/**
 * Generic java Http loader: used to get web page to a temporary file.
 */
public class HttpLoader 
{
	/** trace object */
	private static Logger trace =
		Logger.getLogger(HttpLoader.class);

	/**
	 * The number of bytes loaded
	 */
	private int _loaded = 0;
	
	/**
	 * The number of bytes available for load
	 */
	private int _length = 0;

	/**
	 * HttpLoader constructor loads available connection properties
	 */
	public HttpLoader() 
	{
		init();
	}

	/**
	 * Load web page from url into a temporary file
	 * 
	 * @param pageUrl URL of wanted page
	 * @return File temporary file containing web page content, null when an
	 * exception occured
	 */
	public File load(URL url) throws IOException 
	{
		try 
		{
			URLConnection conn = url.openConnection();
			InputStream in = conn.getInputStream();
			_length = conn.getContentLength();
			byte[] buffer = new byte[4096];
			int read = 0;
			_loaded = 0;

			File tmpfile = File.createTempFile("www", null);
			FileOutputStream out = new FileOutputStream(tmpfile);

			while ((read = in.read(buffer)) > 0) 
			{
				out.write(buffer, 0, read);
				_loaded += read;
			}

			out.close();
			in.close();
			return tmpfile;
		} 
		catch (IOException e) 
		{
			trace.error(e.getMessage(), e);
			//return new AbstractTrader[0];
			return null;
		}

	}

	/**
	 * Method getLoadedLength: retrieves the number of bytes from the web pages loaded
	 * so far.
	 * @return int number of bytes loaded so far
	 */
	public int getLoadedLength() 
	{
		return _loaded;
	}

	/**
	 * Method getTotalLength: retrieves the size in bytes of the web page
	 * being downloaded. When this information is not available it returns -1
	 * @return int size of the web page, -1 when not available.
	 */
	public int getTotalLength() 
	{
		return _length;
	}

	/**
	 * initializes the http proxy settings from the conf/connection.properties file, 
	 * otherwise direct connection is assumed 
	 */
	private void init() 
	{
		try 
		{
			Properties sysprop = System.getProperties();
			ResourceBundle bundle = ResourceBundle.getBundle("connection");
			String host = bundle.getString("http.proxyHost");
			sysprop.put("http.proxyHost", host);
			String port = bundle.getString("http.proxyPort");
			sysprop.put("http.proxyPort", port);
			System.out.println(
				"using proxy for http connection " + host + ":" + port);
		} 
		catch (MissingResourceException e) 
		{
			// nothing done
			trace.info("Direct HTTP connection");
		}
	}
}
