package robotrader.trader.impl.junit;


import robotrader.trader.impl.StreakFollower;
import junit.framework.TestCase;

public class StreakFollowerTest extends TestCase{
	private StreakFollower _s;
	
	public StreakFollowerTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		_s = new StreakFollower();
		_s.init();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testtoString(){
		assertTrue(_s.toString().equals("StreakFollower"));
	}
	public void testName(){
		assertTrue(_s.getName().equals("StreakFollowerUp3Down0"));
	}
	public void testPropertySTREAKUPSIZE(){
		_s.setProperty("STREAKUPSIZE", "20");
		assertTrue(_s.getProperty("STREAKUPSIZE").equals("20"));
	}
	public void testSTREAKUPSIZE(){
		_s.setStreakUp(20);
		assertTrue(_s.getProperty("STREAKUPSIZE").equals("20"));
	}
	public void testPropertySTREAKDOWNSIZE(){
		_s.setProperty("STREAKDOWNSIZE", "11");
		assertTrue(_s.getProperty("STREAKDOWNSIZE").equals("11"));
	}
	public void testSTREAKDOWNSIZE(){
		_s.setStreakDown(11);
		assertTrue(_s.getProperty("STREAKDOWNSIZE").equals("11"));
	}
	
	public void testPropertyMetaData(){
		assertTrue(_s.getPropertyMetaData().equals(new StreakFollower().getPropertyMetaData()));
	}
}
