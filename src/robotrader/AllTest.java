package robotrader;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTest extends TestSuite{
	
	public static Test suite(){
		TestSuite mys = new TestSuite("Robotrader Test Suite");
		mys.addTestSuite(robotrader.trader.junit.XmlTraderLoaderTest.class);
		return mys;
	}

	public static void main(String[] args){
		junit.textui.TestRunner.run(robotrader.AllTest.class);
	}
}