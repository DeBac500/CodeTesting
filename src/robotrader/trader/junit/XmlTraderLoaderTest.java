package robotrader.trader.junit;

import java.io.FileReader;
import java.io.FileWriter;

import robotrader.engine.SimpleAccount;
import robotrader.engine.model.PropertyMetaData;
import robotrader.quotedb.junit.QuoteMonsterMarketEngine;
import robotrader.trader.AbstractTrader;
import robotrader.trader.XmlTraderLoader;
import junit.framework.TestCase;

public class XmlTraderLoaderTest extends TestCase {

  QuoteMonsterMarketEngine _market;
  SimpleAccount _account;
  XmlTraderLoader _loader;
  
	public XmlTraderLoaderTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
    _loader = new XmlTraderLoader();
    _market = new QuoteMonsterMarketEngine("conf/GE.txt");
    _account = new SimpleAccount(100.0f);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'robotrader.trader.XmlTraderLoader.load(Reader)'
	 */
	public final void testLoad() 
	{
		try 
		{
			FileReader reader = new FileReader("conf/traders.xml");
			AbstractTrader[] traders = _loader.load(reader);
			assertNotNull(traders);
			assertTrue(traders.length > 0);

			for (int i = 0; i < traders.length; i++) 
			{
        traders[i].setMarket(_market);
        traders[i].setAccount(_account);
				traders[i].init();
				System.out.println(i + ":" + traders[i].getName());
			}
      
      reader.close();
      
      FileWriter writer = new FileWriter("conf/test.xml");
      _loader.save(traders, writer);
      writer.close();

      reader = new FileReader("conf/test.xml");
      AbstractTrader[] traders2 = _loader.load(reader);
      assertNotNull(traders2);
      assertTrue(traders2.length > 0);
      assertEquals(traders.length, traders2.length);
      
      for (int i = 0; i < traders.length; i++) 
      {
        PropertyMetaData[] data = traders[i].getPropertyMetaData();
        PropertyMetaData[] data2 = traders2[i].getPropertyMetaData();
        
        assertEquals(data.length, data2.length);
        
        for (int j = 0; j < data.length; j++)
        {
          String prop = traders[i].getProperty(data[j].getKey());
          String prop2 = traders2[i].getProperty(data2[j].getKey());
          
          assertEquals(prop, prop2);
        }
      }
    } 
		catch (Exception e) 
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
