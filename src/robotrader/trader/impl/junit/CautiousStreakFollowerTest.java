package robotrader.trader.impl.junit;

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
	
	public void toStringTest() {
		String b = "CautiosStreakFollower";
		String c = a.toString();
		assertTrue(c==b);
	}
}
