package robotrader.market.indicator.impl;

import java.util.Iterator;

import robotrader.market.AbstractIndicator;
import robotrader.market.HistoricData;
import robotrader.market.IIndicator;

/**
 * Developed by J. Welles Wilder and introduced in 
 * his 1978 book, New Concepts in Technical Trading 
 * Systems, the Relative Strength Index (RSI) is an 
 * extremely useful and popular momentum oscillator. 
 * The RSI compares the magnitude of a stock's recent 
 * gains to the magnitude of its recent losses and 
 * turns that information into a number that ranges 
 * from 0 to 100. It takes a single parameter, the 
 * number of time periods to use in the calculation. 
 * In his book, Wilder recommends using 14 periods.
 * <br><br>
 * 
 * The RSI's full name is actually rather unfortunate 
 * as it is easily confused with other forms of
 * Relative Strength analysis such as John Murphy's 
 * "Relative Strength" charts and IBD's 
 * "Relative Strength" rankings. Most other 
 * kinds of "Relative Strength" stuff involve using 
 * more than one stock in the calculation. Like most 
 * true indicators, the RSI only needs one stock to 
 * be computed. In order to avoid confusion, many 
 * people avoid using the RSI's full name and just 
 * call it "the RSI."
 * 
 * @author bob 
 * @author klinst
 */
public class RSI extends AbstractIndicator 
{
	/**
	 * The buying level
	 */
	private double _buylevel = 30;
	
	/**
	 * The previous price
	 */
	private double _previous = -1;
	
	/**
	 * The selling level
	 */
	private double _selllevel = 70;
	
	/**
	 * The training period
	 */
	private int _period = 20;

	/**
	 * Creates a new RSI object.
	 * 
	 * @param period The length of the training period
	 * @param inst The stock
	 */
	public RSI(int period, String inst) 
	{
		_period = period;
		_instrument = inst;
	}

	/**
	 * Creates a new RSI object.
	 * 
	 * @param period The training period
	 * @param inst The stock
	 * @param buylevel The buying level
	 * @param selllvl The selling level
	 * @throws IllegalArgumentException If buy level >= selling level
	 */
	public RSI(int period, String inst, double buylevel, double selllvl) 
	{
		_period = period;
		_instrument = inst;

		if (buylevel >= selllvl) {
			throw new IllegalArgumentException("RSI : buy level must be < sell level");
		}

		_buylevel = buylevel;
		_selllevel = selllvl;
	}

	/**
	 * Adds a new quote to the object.
	 * If the training period has been
	 * filled with data, the indicator
	 * is generated.
	 */
	public void add(HistoricData data) {
		// if (evt.isDaily())
		double current = data.getClose();

		if (_previous > 0) {
			if (!_ready) {
				_data.add(makeValue(current));

				if (_data.size() >= _period) {
					_ready = true;
					generateIndicator();
				}
			} else {
				_data.remove(0);
				_data.add(makeValue(current));
				generateIndicator();
			}
		}

		_previous = current;
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
	 * The string representation of the object.
	 * 
	 * @param period The training period
	 * @param buylevel The buying level
	 * @param selllevel The selling level
	 * @return The String
	 */
	public static String toString(
		int period,
		double buylevel,
		double selllevel) 
	{
		return "RSI" + period + "[" + buylevel + ":" + selllevel + "]";
	}

	/**
	 * Calculates the RSI. The RSI is calculated
	 * using the sum of prices that increased
	 * on the previous prices (upsum) and
	 * the sum of prices that decreased (downsum).
	 * The RSI is then calculated as follows:
	 * 
	 * <pre>
	 * 
	 * rsi = (100d - 100d / (upsum / downsum + 1d))
	 * 
	 * </pre>
	 * 
	 * If the RSI is smaller than buy level a BUY
	 * is signalled. If it is greater then the
	 * selling level, a SELL is signalled. Otherwise
	 * HOLD.
	 *
	 */
	private void generateIndicator() {
		double upsum = 0;
		double downsum = 0;

		for (Iterator it = _data.iterator(); it.hasNext();) 
		{
			Value v = (Value) it.next();

			switch (v.getDirection()) {
				case AbstractIndicator.UP :
					upsum += v.getPrice();

					break;

				case AbstractIndicator.DOWN :
					downsum += v.getPrice();

					break;

				default :
					break;
			}
		}

		double rsi = (100d - 100d / (upsum / downsum + 1d));

		if (rsi <= _buylevel) {
			_direction = IIndicator.BUY;
		} else if (rsi >= _selllevel) {
			_direction = IIndicator.SELL;
		} else {
			_direction = IIndicator.HOLD;
		}
	}

	/**
	 * Creates a new Value object. The
	 * direction of the Value is determined
	 * by the sign of the difference of
	 * current price and previous price.
	 *   
	 * @param current The current price
	 * @return The Value object
	 */
	private Value makeValue(double current) 
	{
		int direction = IIndicator.NA;

		if (current > _previous) {
			direction = AbstractIndicator.UP;
		} else if (current < _previous) {
			direction = AbstractIndicator.DOWN;
		} else {
			direction = AbstractIndicator.UNCH;
		}

		Value val = new Value(current, direction);

		return val;
	}

	/**
	 * Value object for storing the price
	 * and direction.
	 * 
	 * @author klinst
	 */
	class Value 
	{
		private double _price;
		private int _direction;

		Value(double price, int direction) {
			_price = price;
			_direction = direction;
		}

		int getDirection() {
			return _direction;
		}

		double getPrice() {
			return _price;
		}
	}
}