package robotrader.trader.impl.junit;







import java.io.File;
import java.net.URL;


import robotrader.trader.impl.Keeper;


import junit.framework.TestCase;
import junit.framework.Assert;


public class KeeperTest extends TestCase {
	
	Keeper a;
	
	public KeeperTest(String name) {
		super(name);
	}
	protected void setUp() throws Exception {
		super.setUp();
		 a = new Keeper();
	}
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public final void testLoad()
	{
		try 
		{
			a.init();
			
			assertFalse(a.getName().equals("HI"));
			assertTrue(a.getName().equals("Keeper"));
			assertTrue(a.toString().equals("Keeper"));
			//a.update();
			
		}
		catch(Exception e)
		{
			fail(e.getMessage());
		}	
	}
	
}




