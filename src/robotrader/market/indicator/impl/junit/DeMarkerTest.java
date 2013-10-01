package robotrader.market.indicator.impl.junit;

import robotrader.market.IIndicator;
import robotrader.market.indicator.impl.DeMarker;
import robotrader.quotedb.junit.QuoteMonsterMarketEngine;
import junit.framework.TestCase;

/**
 * Test Case for the DeMarker indicator.
 * 
 * @author klinst
 */
public class DeMarkerTest extends TestCase
{

	QuoteMonsterMarketEngine _market;
	DeMarker _indicator;
	int _period;
	
	public DeMarkerTest(String name)
	{
		super(name);
	}

	protected void setUp() throws Exception
	{
		super.setUp();
		_period = 20;
		_market = new QuoteMonsterMarketEngine("conf/GE.txt");
		_indicator = new DeMarker(_period, "GE");;
	}

	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	/*
	 * Test method for 'robotrader.indicator.impl.DeMarker.getName()'
	 */
	public final void testGetName()
	{
		String name = _indicator.getName();
		assertNotNull(name);
		assertTrue(name.length() > 0);
	}

	/*
	 * Test method for 'robotrader.indicator.impl.DeMarker.add(HistoricData)'
	 */
	public final void testAdd()
	{
		int i = 0;
		
		while(_market.hasNext())
		{
			_market.next();
			_indicator.add(_market.current());
			
			if (++i > _period)
			{
				assertTrue(_indicator.isReady());
				assertTrue(_indicator.getDirection() != IIndicator.NA);
			}
			else
			{
				assertFalse(_indicator.isReady());
				assertTrue(_indicator.getDirection() == IIndicator.NA);
			}
		}
	}

}
