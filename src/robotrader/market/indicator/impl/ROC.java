package robotrader.market.indicator.impl;

import robotrader.market.AbstractIndicator;
import robotrader.market.HistoricData;
import robotrader.market.IIndicator;

/**
 * The Rate-of-Change (percent) is a very simple yet 
 * effective momentum oscillator that measures the 
 * percent change in price from one period to the next. 
 * ROC calculation compares the current price with the 
 * price n periods ago. For example, a 10 period rate 
 * of change would be calculated as follows:
 * 
 * <pre>
 * ROC = 100 * (Today's close - Close 10 periods ago) / 
 * (Close 10 periods ago)
 * </pre>
 * 
 * The plot forms an oscillator that fluctuates above 
 * and below the zero line as the rate-of-change moves 
 * from positive to negative. The oscillator can be 
 * used as any other momentum oscillator by looking for 
 * higher lows, lower highs, positive and negative 
 * divergences, and crosses above and below zero for 
 * signals. 
 * 
 * @author xcapt
 * @author klinst
 */
public class ROC extends AbstractIndicator {
	
	/**
	 * The buying level
	 */
	private float _buylevel = -8;
	
	/**
	 * The selling level
	 */
	private float _selllevel = 8;
	
	/**
	 * The training period
	 */
	private int _period = 12;

	/**
	 * Creates a new ROC object.
	 * The indicator is created with a default buy level of -8
	 * and a default sell level of 8 and a period of 12.
	 * 
	 * @param period time period in days to compute indicator
	 * @param inst instrument(stock or index) of indicator
	 */
	public ROC(int period, String inst) 
	{
		_period = period;
		_instrument = inst;
	}

	/**
	 * Creates a new ROC object.
	 * 
	 * @param period time period in days to compute indicator
	 * @param inst instrument(stock or index) of indicator
	 * @param buylvl buy level 
	 * @param selllvl sell level 
	 * 
	 * @throws IllegalArgumentException an exception is thrown when
	 * when the sell level is >= the buy level.
	 */
	public ROC(int period, String inst, float buylvl, float selllvl) {
		_period = period;
		_instrument = inst;

		if (buylvl >= selllvl) {
			throw new IllegalArgumentException("ROC : buy must be < sell level");
		}

		_buylevel = buylvl;
		_selllevel = selllvl;
	}

	/**
	 * Adds a Quote to the trader. When enough
	 * data has been presented, the indicator
	 * is generated and ready to signal.
	 */
	public void add(HistoricData data) 
	{
		float current = data.getClose();
		Value v = new Value(current);

		if (!_ready) 
		{
			_data.add(v);

			if (_data.size() >= _period) 
			{
				_ready = true;
				generateIndicator(current);
			}
		} 
		else 
		{
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
	public String getName() 
	{
		return toString(_period, _buylevel, _selllevel);
	}

	/**
	 * Returns a name of the indicator
	 * 
	 * @param period The training period
	 * @param buylevel The buy level
	 * @param selllevel The sell level
	 * @return
	 */
	public static String toString(
		int period,
		float buylevel,
		float selllevel) {
		return "ROC" + period + "[" + buylevel + ":" + selllevel + "]";
	}

	/**
	 * Calculates the ROC as <br><br>
	 * 
	 * <pre>
	 * 100 * (current - oldclose) / oldclose
	 * 
	 * </pre>
	 * 
	 * If the ROC falls below buy level, then
	 * a OVERSOLD is issued, if it rises above
	 * sell level, a OVERBOUGHT is signalled.
	 * If none of the above happen, and the
	 * previous direction was either OVERSOLD
	 * or OVERBOUGHT then either a BUY or
	 * SELL is issued, respectively. Otherwise
	 * HOLD.
	 * 
	 * @param current
	 */
	private void generateIndicator(float current) 
	{
		float oldclose = ((Value) _data.get(0)).getClose();

		float roc = 100 * (current - oldclose) / oldclose;

		if (roc <= _buylevel) 
		{
			_direction = IIndicator.OVERSOLD;
		} 
		else if (roc >= _selllevel) 
		{
			_direction = IIndicator.OVERBOUGHT;
		} 
		else 
		{
			if (_direction == IIndicator.OVERBOUGHT) 
			{
				_direction = IIndicator.SELL;
			} 
			else if (_direction == IIndicator.OVERSOLD) 
			{
				_direction = IIndicator.BUY;
			} 
			else 
			{
				_direction = IIndicator.HOLD;
			}
		}
	}

	/**
	 * A value object for the closing price.
	 * 
	 * @author klinst
	 */
	class Value 
	{
		private float _close;

		Value(float close) {
			_close = close;
		}

		float getClose() {
			return _close;
		}
	}
}