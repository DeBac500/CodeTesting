package robotrader.market.indicator.impl;

import java.util.Iterator;

import robotrader.market.AbstractIndicator;
import robotrader.market.HistoricData;
import robotrader.market.IIndicator;

/**
 * Developed by Larry Williams, Williams %R is a momentum 
 * indicator that works much like the Stochastic Oscillator. 
 * It is especially popular for measuring overbought and oversold 
 * levels. The scale ranges from 0 to -100 with readings from 0 
 * to -20 considered overbought, and readings from -80 to -100 
 * considered oversold.<br><br>
 * 
 * William %R, sometimes referred to as %R, shows the relationship 
 * of the close relative to the high-low range over a set period 
 * of time. The nearer the close is to the top of the range, the 
 * nearer to zero (higher) the indicator will be. The nearer the 
 * close is to the bottom of the range, the nearer to -100 (lower) 
 * the indicator will be. If the close equals the high of the 
 * high-low range, then the indicator will show 0 (the highest 
 * reading). If the close equals the low of the high-low range, 
 * then the result will be -100 (the lowest reading).
 * 
 * @author xcapt
 * @author klinst
 */
public class WilliamsR extends AbstractIndicator 
{
	/**
	 * The buying level
	 */
	private float _buylevel = 80;
	
	/**
	 * The selling level
	 */
	private float _selllevel = 20;
	
	/**
	 * The training period
	 */
	private int _period = 20;

	/**
	 * Creates a new WilliamsR object.
	 * The indicator is created with a default buy level of 80
	 * and a default sell level of 20.
	 * 
	 * @param period time period in days to compute indicator
	 * @param inst instrument(stock or index) of indicator
	 */
	public WilliamsR(int period, String inst) {
		_period = period;
		_instrument = inst;
	}

	/**
	 * Creates a new WilliamsR object.
	 * 
	 * @param period time period in days to compute indicator
	 * @param inst instrument(stock or index) of indicator
	 * @param buylvl buy level (50-100)
	 * @param selllvl sell level (0-50)
	 * 
	 * @throws IllegalArgumentException an exception is thrown when
	 * when the sell level is >= the buy level.
	 */
	public WilliamsR(int period, String inst, float buylvl, float selllvl) {
		_period = period;
		_instrument = inst;

		if (selllvl >= buylvl) {
			throw new IllegalArgumentException("Williams%R : buy must be > sell level");
		}

		_buylevel = buylvl;
		_selllevel = selllvl;
	}

	/**
	 * Adds a new quote to this object. If enough
	 * data has been added, the indicator is ready
	 * to signal. 
	 */
	public void add(HistoricData data) 
	{
		float current = data.getClose();
		Value v = new Value(data.getHigh(), data.getLow());

		if (!_ready) {
			_data.add(v);

			if (_data.size() >= _period) {
				_ready = true;
				generateIndicator(current);
			}
		} else {
			_data.remove(0);
			_data.add(v);
			generateIndicator(current);
		}
	}

	/**
	 * returns a description of the indicator
	 * 
	 * @return short name
	 */
	public String getName() {
		return toString(_period, _buylevel, _selllevel);
	}

	/**
	 * 
	 * @param period
	 * @param buylevel
	 * @param selllevel
	 * @return
	 */
	public static String toString(
		int period,
		float buylevel,
		float selllevel) {
		return "Williams%R" + period + "[" + buylevel + ":" + selllevel + "]";
	}

	/**
	 * Calculates the Williams%R indicator using the
	 * current high (currhigh) and low prices (currlow) over the
	 * training period. The indicator is then calculated as
	 * 
	 * <pre>
	 * 
	 * wr = (100 * (currhigh - current)) / (currhigh - currlow);
	 * 
	 * </pre>
	 * 
	 * if wr is greater than the buy level or lower than
	 * the sell level, and OVERSOLD or OVERBOUGHT signal is
	 * issued, respectively. If neither happens, and the
	 * previous signal was OVERSOLD (OVERBOUGHT), then
	 * a BUY (SELL) is signalled. Otherwise HOLD.
	 * 
	 * @param current
	 */
	private void generateIndicator(float current) 
	{
		float currhigh = current;
		float currlow = current;

		for (Iterator it = _data.iterator(); it.hasNext();) {
			Value v = (Value) it.next();

			if (v.getHigh() > currhigh) {
				currhigh = v.getHigh();
			} else if (v.getLow() < currlow) {
				currlow = v.getLow();
			}
		}

		float wr = (100 * (currhigh - current)) / (currhigh - currlow);

		if (wr >= _buylevel) {
			_direction = IIndicator.OVERSOLD;
		} else if (wr <= _selllevel) {
			_direction = IIndicator.OVERBOUGHT;
		} else {
			if (_direction == IIndicator.OVERBOUGHT) {
				_direction = IIndicator.SELL;
			} else if (_direction == IIndicator.OVERSOLD) {
				_direction = IIndicator.BUY;
			} else {
				_direction = IIndicator.HOLD;
			}
		}
	}

	/**
	 * A Value object storing the high and low value.
	 *  
	 * @author klinst
	 */
	class Value {
		private float _high;
		private float _low;

		Value(float high, float low) {
			_high = high;
			_low = low;
		}

		float getHigh() {
			return _high;
		}

		float getLow() {
			return _low;
		}
	}
}