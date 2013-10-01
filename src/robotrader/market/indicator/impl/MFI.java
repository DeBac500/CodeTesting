package robotrader.market.indicator.impl;

import java.util.Iterator;

import robotrader.market.AbstractIndicator;
import robotrader.market.HistoricData;
import robotrader.market.IIndicator;

/**
 *
 *  The Money Flow Index (MFI) is a momentum 
 *  indicator that is similar to the Relative 
 *  Strength Index (RSI) in both interpretation 
 *  and calculation. However, MFI is a more rigid 
 *  indicator in that it is volume-weighted, and 
 *  is therefore a good measure of the strength of 
 *  money flowing in and out of a security. It 
 *  compares "positive money flow" to 
 *  "negative money flow" to create an indicator 
 *  that can be compared to price in order to 
 *  identify the strength or weakness of a trend. 
 *  Like the RSI, the MFI is measured on a 0 - 100 
 *  scale and is often calculated using a 14 day 
 *  period.
 *  
 * @author bob
 * @author klinst
 */
public class MFI extends AbstractIndicator 
{
	/**
	 * period over which to generate indicator
	 */
	private int _period = 14;
	
	/**
	 * 
	 */
	private float _buylevel = 20;
	
	/**
	 * 
	 */
	private float _selllevel = 80;

	/**
	 * stores the moneyflow and typical
	 * price as data object.
	 *  
	 * @author klinst
	 */
	class Value 
	{
		/**
		 * Moneyflow
		 */
		private float _moneyflow;
		
		/**
		 * Typical price
		 */
		private float _typicalprice;

		/**
		 * Construct the Value object
		 * 
		 * @param mf
		 * @param tp
		 */
		Value(float mf, float tp) 
		{
			_moneyflow = mf;
			_typicalprice = tp;
		}

		/**
		 * Get the money flow
		 * @return
		 */
		public float getMoneyFlow() 
		{
			return _moneyflow;
		}

		/**
		 * Get the typical price
		 * @return
		 */
		public float getTypicalPrice() 
		{
			return _typicalprice;
		}
	}
	
	/**
	 * Gets the direction the indicator is pointing
	 * towards.
	 * 
	 * @see robotrader.market.IIndicator
	 */
	public int getDirection() 
	{
		return _direction;
	}

	/**
	 * constructs a new MFI object.
	 * 
	 * @param period The period over which to apply the MFI
	 * @param inst The instrument (currently not used)
	 */
	public MFI(int period, String inst) 
	{
		_period = period;
		_instrument = inst;
	}
	
	/**
	 * Constructs a new MFI object.
	 * 
	 * @param period The period the MFI requires for training
	 * @param inst The instrument (currently not used)
	 * @param buylevel The buy level
	 * @param selllvl The sell level
	 */
	public MFI(int period, String inst, float buylevel, float selllvl) 
	{
		_period = period;
		_instrument = inst;
		
		if (buylevel >= selllvl) 
		{
			throw new IllegalArgumentException("RSI : buy level must be < sell level");
		}
		_buylevel = buylevel;
		_selllevel = selllvl;
	}

	/**
	 * Adds a new quote to the trader. Gets the
	 * daily average price and multiplies it
	 * with the volume of trade. If enough data
	 * has been presented (i.e. more than the
	 * period specified) then the indicator
	 * is generated. If more data is presented,
	 * then the indicator is updated by removing
	 * the oldest quote and adding the most
	 * recent.
	 * 
	 * @param data The stock quote information
	 */
	public void add(HistoricData data) 
	{
		float tp = data.getClose() + data.getLow() + data.getHigh();
		tp = tp / 3;
		float mf = tp * data.getVolume();

		if (!_ready) 
		{
			_data.add(new Value(mf, tp));
			if (_data.size() >= _period) 
			{
				_ready = true;
				generateIndicator();
			}
		} 
		else 
		{
			_data.remove(0);
			_data.add(new Value(mf, tp));
			generateIndicator();
		}
	}

	/**
	 * Generates the indicator.
	 * Sums the moneyflow in both the upward
	 * and downward direction. If no downward
	 * direction moneyflow, the indicator
	 * points to SELL. Otherwise the money ratio
	 * is calculated as upsum / downsum. 
	 * 
	 * <pre>
	 * mfi = 100 - 100 / (1 + moneyratio)
	 * </pre>
	 * 
	 * If the mfi is smaller than the buylevel,
	 * the direction is BUY, if it is larger than
	 * sellevel, the direction is sell, otherwise
	 * hold.
	 */
	private void generateIndicator() 
	{
		float upsum = 0;
		float downsum = 0;
		float last = -1;
		float current = 0;

		for (Iterator it = _data.iterator(); it.hasNext();) {
			Value v = (Value) it.next();
			current = v.getTypicalPrice();

			if (last >= 0) {
				if (current > last) {
					upsum += v.getMoneyFlow();
				} else if (current < last) {
					downsum += v.getMoneyFlow();
				}
			}
			last = v.getTypicalPrice();
		}

		if (downsum == 0) {
			_direction = IIndicator.SELL;
		}
		float moneyratio = upsum / downsum;
		float mfi = 100 - 100 / (1 + moneyratio);

		if (mfi <= _buylevel) {
			_direction = IIndicator.BUY;
		} else if (mfi >= _selllevel) {
			_direction = IIndicator.SELL;
		} else {
			_direction = IIndicator.HOLD;
		}
	}

	/**
	 * Gets the trader's name.
	 * @return Trader's name
	 */
	public String getName() 
	{
		return toString(_period, _buylevel, _selllevel);
	}

	/**
	 * Produces the string representation of the MFI.
	 * 
	 * @param period The training period
	 * @param buylevel The buying level
	 * @param selllevel The selling level
	 * @return The string representation
	 */
	public static String toString(
		int period,
		float buylevel,
		float selllevel) {
		return "MFI" + period + "[" + buylevel + ":" + selllevel + "]";
	}
}
