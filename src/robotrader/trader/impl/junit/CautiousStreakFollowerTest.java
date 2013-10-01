package robotrader.trader.impl.junit;

import java.io.File;
import java.net.URL;

import robotrader.trader.impl.CautiousStreakFollower;


import junit.framework.TestCase;
import junit.framework.Assert;


public class CautiousStreakFollowerTest extends TestCase {
	
	CautiousStreakFollower a;
	
	public CautiousStreakFollowerTest(String name) {
		super(name);
	}
	protected void setUp() throws Exception {
		super.setUp();
		 a = new CautiousStreakFollower();
	}
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public final void testLoad()
	{
		try 
		{
			String b = "CautiosStreakFollower";
			String c = a.toString();
			assertTrue(c==b);
			
	        b = "CautsiosStreakFollower";
			c = a.toString();
			assertFalse(c==b);
			
			a.setProperty("STREAKSIZE", "15");
			a.setStreak(15);
			assertTrue(a.getProperty("STREAKSIZE").equals("15"));
			
			a.setProperty("abc", "46");
			assertFalse(a.getProperty("abs").equals("46"));
			
			a.setProperty("MAXRISK", "0.15");
			a.setMaxRisk(0.15f);
			assertTrue(a.getProperty("MAXRISK").equals("0.15"));
			
			
			
			
		}
		catch(Exception e)
		{
			fail(e.getMessage());
		}	
	}
	
}
