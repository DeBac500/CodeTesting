package robotrader.trader.impl.junit;

import robotrader.trader.impl.WinFollower;
import junit.framework.TestCase;

public class WinFollowerTest extends TestCase{
	private WinFollower _w;
	
	public WinFollowerTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		_w = new WinFollower();
		_w.init();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testtoString(){
		assertTrue(_w.toString().equals("WinFollower"));
	}
	public void testgetProperty(){
		assertTrue(_w.getProperty("Hallo").equals("0"));
	}
}
