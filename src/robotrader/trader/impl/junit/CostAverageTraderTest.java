package robotrader.trader.impl.junit;



	import java.io.File;
	import java.net.URL;

	import robotrader.trader.impl.CostAverageTrader;


	import junit.framework.TestCase;
	import junit.framework.Assert;


	public class CostAverageTraderTest extends TestCase {
		
		CostAverageTrader a;
		
		public CostAverageTraderTest(String name) {
			super(name);
		}
		protected void setUp() throws Exception {
			super.setUp();
			 a = new CostAverageTrader();
		}
		protected void tearDown() throws Exception {
			super.tearDown();
		}
		
		public final void testLoad()
		{
			try 
			{
				assertTrue(a.toString().equals("Cost Average"));
				
				assertFalse(a.toString().equals("CAverage"));
				
				a.setProperty("PERIOD", "15");
				assertTrue(a.getProperty("PERIOD").equals("15"));
				
				assertTrue(a.getPropertyMetaData().equals(a.getPropertyMetaData()));
				
				assertFalse(a.getName().equals("Hi"));
				
				
			}
			catch(Exception e)
			{
				fail(e.getMessage());
			}	
		}
		
	}


