package robotrader.market.indicator.impl;

import java.util.Iterator;

import robotrader.market.AbstractIndicator;
import robotrader.market.HistoricData;
import robotrader.market.IIndicator;

/**
 * Demarker Technical Indicator is based on the 
 * comparison of the period maximum with the 
 * previous period maximum. If the current period 
 * (bar) maximum is higher, the respective 
 * difference between the two will be registered. 
 * If the current maximum is lower or equaling 
 * the maximum of the previous period, the naught 
 * value will be registered. The differences 
 * received for N periods are then summarized. 
 * The received value is used as the numerator of 
 * the DeMarker and will be divided by the same 
 * value plus the sum of differences between the 
 * price minima of the previous and the current 
 * periods (bars). If the current price minimum 
 * is greater than that of the previous bar, the 
 * naught value will be registered.<br><br>
 * 
 * When the indicator falls below 30, the bullish 
 * price reversal should be expected. When the 
 * indicator rises above 70, the bearish price 
 * reversal should be expected.<br><br>
 * 
 * If you use periods of longer duration, when 
 * calculating the indicator, you’ll be able to 
 * catch the long term market tendency. Indicators 
 * based on short periods let you enter the market 
 * at the point of the least risk and plan the 
 * time of transaction so that it falls in with 
 * the major trend.<br>
 * 
 * @author bob 
 * @author klinst
 */
public class DeMarker extends AbstractIndicator
{
	/**
	 * the buying level
	 */
	private float _buylevel = 30;
	
	/**
	 * the recorded high at the last time step
	 */
	private float _prevhigh = -1;
	
	/**
	 * the recorded low at the last time step
	 */
	private float _prevlow = -1;
	
	/**
	 * The selling level
	 */
	private float _selllevel = 70;
	
	/**
	 * the number of consecutive dates
	 * required for training the indicator
	 */
	private int _period = 20;

	/**
	 * Creates a new DeMarker object.
	 * 
	 * @param period the period the indicator requires for training
	 * @param inst the underlying instrument
	 */
	public DeMarker(int period, String inst) 
	{
		_period = period;
		_instrument = inst;
	}

	/**
	 * Creates a new DeMarker object.
	 * 
	 * @param period the period required for training the indicator
	 * @param inst the instrument (stock)
	 * @param buylevel the buying level (default 30)
	 * @param selllvl the selling level (default 70)
	 * @throws IllegalArgumentException if sellvl < buylevel
	 */
	public DeMarker(int period, String inst, float buylevel, float selllvl) 
	{
		_period = period;
		_instrument = inst;

		if (buylevel >= selllvl) 
		{
			throw new IllegalArgumentException("DeMarker : buy level must be < sell level");
		}

		_buylevel = buylevel;
		_selllevel = selllvl;
	}

	/**
	 * Adds historic data to the 
	 * Indicator. The indicator is trained
	 * over the given period and is
	 * constantly updated after the
	 * period has been exceeded.
	 * 
	 * @see robotrader.trader.indicator.IIndicator#updated(IEvent)
	 */
	public void add(HistoricData data) 
	{
		float high = data.getHigh();
		float low = data.getLow();

		// if the previous high has been recorded
		// and a minimum number of demin and
		// demax values have been calculated
		// then the indicator is generated
		if (_prevhigh > 0) 
		{
			if (!_ready) 
			{
				_data.add(makeValue(high, low));

				if (_data.size() >= _period) 
				{
					_ready = true;
					generateIndicator();
				}
			} 
			// update the indicator by
			// removing the oldest values
			// and adding the most recent
			else 
			{
				_data.remove(0);
				_data.add(makeValue(high, low));
				generateIndicator();
			}
		}

		_prevhigh = high;
		_prevlow = low;
	}

	/**
	 * returns a description of the indicator
	 * 
	 * @return short name
	 */
	public String getName() 
	{
		return toString(_period, _buylevel, _selllevel);
	}
	
	/**
	 * gets the DeMarker description as a string
	 *  
	 * @param period the period required for training
	 * @param buylevel the buying level
	 * @param selllevel the selling level
	 * @return
	 */
	public static String toString(
		int period,
		float buylevel,
		float selllevel) 
	{
		return "DeMarker" + period + "[" + buylevel + ":" + selllevel + "]";
	}

	/**
	 * Generates the indicator. The increases in
	 * the high price and decreases in the low
	 * price are summed up over the training 
	 * period. If the percentage of the sum
	 * of increasing prices over the sum of decreasing
	 * prices is smaller than the buy level, then
	 * the indicator returns a buy signal. On
	 * the other hand, if the level is larger
	 * than the sell level, a sell signal is
	 * issued.
	 */
	private void generateIndicator() 
	{
		float upsum = 0;
		float downsum = 0;

		for (Iterator it = _data.iterator(); it.hasNext();) 
		{
			Value v = (Value) it.next();
			upsum += v.getDeMax();
			downsum += (v.getDeMax() + v.getDeMin());
		}

		float demarker = (100 * upsum) / downsum;

		if (demarker <= _buylevel) 
		{
			_direction = IIndicator.BUY;
		} 
		else if (demarker >= _selllevel) 
		{
			_direction = IIndicator.SELL;
		} 
		else 
		{
			_direction = IIndicator.HOLD;
		}
	}

	/**
	 * creates a value pair (demax, demin) where
	 * demax is max(0, high - previous high) and
	 * demin is max(0, previous low - low)
	 * 
	 * @param high the current high  
	 * @param low the current low
	 * @return object pair of (demin, demax)
	 */
	private Value makeValue(float high, float low) 
	{
		float demax = 0;
		float demin = 0;

		if (high > _prevhigh) 
		{
			demax = high - _prevhigh;
		}

		if (low < _prevlow) 
		{
			demin = _prevlow - low;
		}

		Value val = new Value(demax, demin);

		return val;
	}

	/**
	 * Data object storing the minimum
	 * and maximum DeMarker values
	 * 
	 * @author klinst
	 */
	class Value 
	{
		/**
		 * the increase over the high price
		 */
		private float _demax;
		
		/**
		 * the decrease over the low price
		 */
		private float _demin;

		/**
		 * construct a Value object.
		 * 
		 * @param demax the maximum increase in price
		 * @param demin the minimum decrease in price
		 */
		Value(float demax, float demin) 
		{
			_demin = demin;
			_demax = demax;
		}

		/**
		 * 
		 * @return the increase over the high price
		 */
		float getDeMax() 
		{
			return _demax;
		}

		/**
		 * 
		 * @return the decrease over the low price
		 */
		float getDeMin() 
		{
			return _demin;
		}
	}
}