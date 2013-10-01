package robotrader.trader.impl.junit;

import robotrader.trader.impl.TurningPointTrader;
import junit.framework.TestCase;

public class TurningPointTraderTest extends TestCase{
	private TurningPointTrader _t;
	
	public TurningPointTraderTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		_t = new TurningPointTrader();
		_t.init();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testtoString(){
		assertTrue(_t.toString().equals("TurningPointTrader"));
	}
	public void testName(){
		assertTrue(_t.getName().equals("TurningPoint (50,0.05,0.25,0.1)"));
	}
	public void testPropertyMINWIN(){
		_t.setProperty("MINWIN", "0.2");
		assertTrue(_t.getProperty("MINWIN").equals("0.2"));
	}
	public void testPropertyMAXLOSS(){
		_t.setProperty("MAXLOSS", "0.5");
		assertTrue(_t.getProperty("MAXLOSS").equals("0.5"));
	}
	public void testPropertyPERIOD(){
		_t.setProperty("PERIOD", "4");
		assertTrue(_t.getProperty("PERIOD").equals("4"));
	}
	public void testPropertyMAXWIN(){
		_t.setProperty("MAXWIN", "0.4");
		assertTrue(_t.getProperty("MAXWIN").equals("0.4"));
	}
	public void testPropertyMetaData(){
		assertTrue(_t.getPropertyMetaData().equals(new TurningPointTrader().getPropertyMetaData()));
	}
}
