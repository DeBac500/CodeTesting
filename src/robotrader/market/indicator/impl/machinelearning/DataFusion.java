package robotrader.market.indicator.impl.machinelearning;

import java.util.ArrayList;
import java.util.List;

import robotrader.market.AbstractIndicator;
import robotrader.market.HistoricData;
import robotrader.market.IIndicator;
import robotrader.market.indicator.impl.DeMarker;
import robotrader.market.indicator.impl.MACD;
import robotrader.market.indicator.impl.MFI;
import robotrader.market.indicator.impl.OBV;
import robotrader.market.indicator.impl.ROC;
import robotrader.market.indicator.impl.WilliamsR;

/**
 * This indicator takes the signals of a selection
 * of other indicators and "fuses" their signals
 * into one consensus trading signal. The 
 * majority vote is taken, i.e. if the majority
 * of indicators signal BUY, a buy signal is
 * issued. So far, the indicators are fixed
 * as the following:<br>
 * 
 * <ul>
 * <li>De Marker</li>
 * <li>MACD</li>
 * <li>MFI</li>
 * <li>ROC</li>
 * <li>WilliamsR</li>
 * <li>OBV</li>
 * </ul>
 * 
 * @author klinst
 */
public class DataFusion extends AbstractIndicator
{
	/**
	 * The list of underlying indicators
	 */
	private List _indicators;
	
	/**
	 * Default Constructor.
	 */
	public DataFusion(int period)
	{
		_indicators = new ArrayList();
		_indicators.add(new DeMarker(period, ""));
		_indicators.add(new MACD(26, 12, period, ""));
		_indicators.add(new MFI(period, ""));
		_indicators.add(new ROC(period, ""));
		_indicators.add(new WilliamsR(period, ""));
    _indicators.add(new OBV(period, ""));
	}
	
	/**
	 * Constructor with the list of indicators 
	 * to use.
	 * 
	 * @param indicators
	 */
	public DataFusion(List indicators)
	{
		_indicators = indicators;
	}
	
	/**
	 * Adds an indicator to the list.
	 * @param indicator
	 */
	public void addIndicator(IIndicator indicator)
	{
		_indicators.add(indicator);
	}
	
	/**
	 * Gets the name of this indicator
	 */
	public String getName()
	{
		return toString(_indicators.size());
	}

	/**
	 * Adds historic quote information.
	 * Waits until all indicators are 
	 * ready, then decides on the
	 * signal by the majority vote
	 * of the underlying indicators.
	 */
	public void add(HistoricData data)
	{
		if (!_ready)
		{
			int ready = 0;
			
			for (int i = 0; i < _indicators.size(); i++)
			{
				IIndicator indicator = (IIndicator)_indicators.get(i);
				indicator.add(data);
				
				if (indicator.isReady()) ready++;
	 		}

			if (ready == _indicators.size())
			{
				_ready = true;
			}
		}
		else
		{
			int buy = 0, sell = 0, hold = 0;
			
			for (int i = 0; i < _indicators.size(); i++)
			{
				IIndicator indicator = (IIndicator)_indicators.get(i);
				indicator.add(data);
				
				if (indicator.isBuy()) buy++;
				if (indicator.isSell()) sell++;
				if (indicator.isHold()) hold++;
			}
			
			if (buy > sell && buy > hold)
			{
				_direction = IIndicator.BUY;
			}
			else if (sell > buy && sell > hold)
			{
				_direction = IIndicator.SELL;
			}
			else
			{
				_direction = IIndicator.HOLD;
			}
		}
	}
	
	/**
	 * Get string representation of
	 * indicator 
	 */
	public static String toString(int size)
	{
		return "Data Fusion " + size;
	}
}
