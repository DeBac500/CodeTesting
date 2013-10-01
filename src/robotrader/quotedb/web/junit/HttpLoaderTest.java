package robotrader.quotedb.web.junit;

import java.io.File;
import java.net.URL;

import robotrader.quotedb.web.HttpLoader;
import junit.framework.TestCase;

/**
 * Test Case for the HttpLoader 
 * 
 * @author klinst
 */
public class HttpLoaderTest extends TestCase
{
	HttpLoader _loader;
	
	public HttpLoaderTest(String name)
	{
		super(name);
	}

	protected void setUp() throws Exception
	{
		super.setUp();
		_loader = new HttpLoader();
	}

	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	/*
	 * Test method for 'robotrader.quotedb.web.HttpLoader.load(URL)'
	 */
	public final void testLoad()
	{
		try 
		{
			File tmpfile = _loader.load(new URL("http://www.yahoo.fr"));
			assertNotNull(tmpfile);
			tmpfile = _loader.load(new URL("http://www.yahoo.de"));
			assertNotNull(tmpfile);
			
			int length = _loader.getLoadedLength();
			assertTrue(length > 0);
			length = _loader.getTotalLength();
			assertTrue(length > 0);
			
			tmpfile.delete();
		}
		catch(Exception e)
		{
			fail(e.getMessage());
		}	
	}
}
