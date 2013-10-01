package robotrader.market.indicator.impl.junit;

import java.util.ArrayList;

import org.apache.log4j.BasicConfigurator;

import net.sourceforge.openforecast.ForecastingModel;
import net.sourceforge.openforecast.models.PolynomialRegressionModel;
import net.sourceforge.openforecast.models.RegressionModel;
import robotrader.market.IIndicator;
import robotrader.market.indicator.impl.DeMarker;
import robotrader.market.indicator.impl.MovingAverage;
import robotrader.market.indicator.impl.RSI;
import robotrader.market.indicator.impl.WilliamsR;
import robotrader.market.indicator.impl.machinelearning.JooneNeuralNetwork;
import robotrader.market.indicator.impl.machinelearning.OpenForecast;
import robotrader.quotedb.junit.QuoteMonsterMarketEngine;
import robotrader.trader.impl.machinelearning.PolynomialRegressionTrader;
import robotrader.trader.impl.machinelearning.RegressionTrader;
import junit.framework.TestCase;

/**
 * Test Case for the all indicators.
 * 
 * @author klinst
 */
public class IndicatorTest extends TestCase
{
	QuoteMonsterMarketEngine _market;
	ArrayList _indicators;
	int _period;
	
	public IndicatorTest(String name)
	{
		super(name);
	}

	protected void setUp() throws Exception
	{
		super.setUp();
		BasicConfigurator.configure();
		
		_period = 10;
    _indicators = new ArrayList();
		_market = new QuoteMonsterMarketEngine("conf/GE.txt");
    
    IIndicator demarker = new DeMarker(_period, "GE");
    
    IIndicator rsi = new RSI(_period, "", 20, 80);
    IIndicator wil = new WilliamsR(_period, "", 80, 20);
    IIndicator ma = new MovingAverage(_period, "");

    ForecastingModel model = new RegressionModel("Period");
    IIndicator regression = new OpenForecast(model, RegressionTrader.NAME, _period, 5, 0.05f, 0.05f);

    model = new PolynomialRegressionModel("Period");
    IIndicator smoothing = new OpenForecast(model, PolynomialRegressionTrader.NAME, _period, 5, 0.05f, 0.05f);
    
    JooneNeuralNetwork neural = new JooneNeuralNetwork();
    
    _indicators.add(demarker);
    _indicators.add(regression);
    _indicators.add(smoothing);
    _indicators.add(rsi);
    _indicators.add(wil);
    _indicators.add(ma);
    _indicators.add(neural);
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
    for (int i = 0; i < _indicators.size(); i++)
    {
      IIndicator indicator = (IIndicator)_indicators.get(i);
      String name = indicator.getName();
      assertNotNull(name);
      assertTrue(name.length() > 0);
    }
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
      i++;
      
      for (int j = 0; j < _indicators.size(); j++)
      {
        IIndicator indicator = (IIndicator)_indicators.get(j);
        indicator.add(_market.current());
        
        if (indicator.isReady())
        {
          assertTrue(indicator.getDirection() != IIndicator.NA);
        }
        else
        {
          assertTrue(indicator.getDirection() == IIndicator.NA);
        }
      }
		}
		System.out.println("Success!");
	}
	
	
}
