package robotrader.quotedb.web.junit;

import java.text.SimpleDateFormat;
import java.util.List;

import robotrader.market.HistoricData;
import robotrader.quotedb.web.YahooDEHistoricLoader;
import junit.framework.TestCase;

/**
 * Simple test for loading quotes from Yahoo! DE
 * @author klinst
 */
public class YahooDEHistoricLoaderTest extends TestCase
{
	String instr = "DCX.DE";
	String startdate = "20001231";
	String enddate = "20030115";
	YahooDEHistoricLoader _loader;

	public YahooDEHistoricLoaderTest(String name)
	{
		super(name);
	}

	protected void setUp() throws Exception
	{
		super.setUp();
		 _loader = new YahooDEHistoricLoader();
	}

	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	/*
	 * Test method for 'robotrader.quotedb.web.BaseYahooLoader.loadQuotes(String, String, String)'
	 */
	public void testLoadQuotes()
	{
		try
		{
			List quotes = _loader.loadQuotes(instr, startdate, enddate);
			assertNotNull(quotes);
			assertTrue(quotes.size() > 0);
			
			// test that the first date is within a week of the requested start date
			SimpleDateFormat yyyymmdddf = new SimpleDateFormat("yyyyMMdd");
			HistoricData data = (HistoricData)(quotes.get(0));
			double diff = Math.abs(data.getDate().getTime() - yyyymmdddf.parse(startdate).getTime());

			assertTrue(((int)(diff/86400000))<7);
			
			// test that the last date is within a week of the requested end date
			data = (HistoricData)(quotes.get(quotes.size()-1));
			diff = Math.abs(data.getDate().getTime() - yyyymmdddf.parse(enddate).getTime());
			assertTrue(((int)(diff/86400000))<7);			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}	
	}


	/*
	 * Test method for 'robotrader.quotedb.web.BaseYahooLoader.getStatus()'
	 */
	public void testGetStatus()
	{
		String status = _loader.getStatus();
		assertNotNull(status);
	}

}
