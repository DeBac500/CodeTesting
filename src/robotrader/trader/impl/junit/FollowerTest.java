package robotrader.trader.impl.junit;





import java.io.File;
import java.net.URL;


import robotrader.trader.impl.Follower;


import junit.framework.TestCase;
import junit.framework.Assert;


public class FollowerTest extends TestCase {
	
	Follower a;
	
	public FollowerTest(String name) {
		super(name);
	}
	protected void setUp() throws Exception {
		super.setUp();
		 a = new Follower();
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
			assertTrue(a.getName().equals("Follower"));
			assertTrue(a.toString().equals("Follower"));
			a.update();
			
		}
		catch(Exception e)
		{
			fail(e.getMessage());
		}	
	}
	
}



