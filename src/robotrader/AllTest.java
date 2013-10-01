package robotrader;


import robotrader.trader.junit.XmlTraderLoaderTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTest {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for robotrader");
		//$JUnit-BEGIN$
	
		
		suite.addTestSuite(XmlTraderLoaderTest.class);
		
		suite.addTestSuite(robotrader.trader.impl.junit.CautiousStreakFollowerTest.class);
		suite.addTestSuite(robotrader.trader.impl.junit.CostAverageTraderTest.class);
		suite.addTestSuite(robotrader.trader.impl.junit.FollowerTest.class);
		suite.addTestSuite(robotrader.trader.impl.junit.KeeperTest.class);
		
		
		//$JUnit-END$
		return suite;
	}

}