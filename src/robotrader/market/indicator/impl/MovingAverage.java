package robotrader.market.indicator.impl;

import robotrader.market.AbstractIndicator;
import robotrader.market.HistoricData;
import robotrader.market.IIndicator;

/**
 * Computes the indicator based on changes
 * in the moving average of the prices over
 * a given period. Increasing moving
 * averages above the closing price issue a SELL, while 
 * decreasing below the closing price issue a BUY order.
 * 
 * @author bob 
 * @author klinst
 */
public class MovingAverage extends AbstractIndicator {
    
  /**
   * The training period
   */
	private int _period = 20;

	/**
	 * Creates a new MovingAverage object.
	 * 
	 * @param period The period over which the average is taken
	 * @param inst The instrument
	 */
	public MovingAverage(int period, String inst) {
		_period = period;
		_instrument = inst;
	}

	/**
	 * Adds a new quote in chronological order to the
   * indicator. If the minimum number has been
   * reached, the indicator is ready to signal.
   * 
   * @param data The quote
	 */
	public void add(HistoricData data) {
		_data.add(data);

		if (_ready) {
			_data.remove(0);
		}

		if (_data.size() > _period) {
			_ready = true;
			generateIndicator();
		}
	}

  /**
   * Generates the indicator. If the previous moving
   * average over the period is smaller than the previous
   * closing price and the current moving average is
   * greater than the closing price, then issue a SELL.
   * In the opposite case issue a BUY, otherwise HOLD.
   */
	private void generateIndicator() {
		float mov1 = 0;
		float mov2 = 0;

		float p1 = ((HistoricData) _data.get(_period - 1)).getClose();
		float p2 = ((HistoricData) _data.get(_period)).getClose();

		for (int i = 0; i < _period; i++) {
			mov1 += ((HistoricData) _data.get(i)).getClose();
		}

		for (int i = 1; i <= _period; i++) {
			mov2 += ((HistoricData) _data.get(i)).getClose();
		}

		mov1 = mov1 / _period;
		mov2 = mov2 / _period;

		if ((mov1 < p1) && (mov2 > p2)) {
			_direction = IIndicator.SELL;
		} else if ((mov1 > p1) && (mov2 < p2)) {
			_direction = IIndicator.BUY;
		} else {
			_direction = IIndicator.HOLD;
		}
	}

  /**
   * Get the name of the indicator
   */
	public String getName() {

		return toString(_period);
	}

  /**
   * Get the string representation of the indicator
   */
	public static String toString(int period) {
		return "MovAvg" + period;
	}

}