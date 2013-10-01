package robotrader.market.indicator.impl;

import java.util.ArrayList;
import java.util.List;

import robotrader.market.AbstractIndicator;
import robotrader.market.HistoricData;
import robotrader.market.IIndicator;

/**
 * Developed by Gerald Appel, Moving Average 
 * Convergence Divergence (MACD) is one of the simplest
 * and most reliable indicators available. MACD uses 
 * moving averages, which are lagging indicators, to 
 * include some trend-following characteristics. These 
 * lagging indicators are turned into a momentum 
 * oscillator by subtracting the longer moving average 
 * from the shorter moving average. The resulting plot 
 * forms a line that oscillates above and below zero, 
 * without any upper or lower limits. MACD is a centered
 * oscillator and the guidelines for using centered 
 * oscillators apply.
 * 
 * @author bob
 * @author klinst 
 */
public class MACD extends AbstractIndicator 
{
	/**
	 * The List of MACDs computed
	 */
	private List _avgdata = new ArrayList();
	
	/**
	 * The length of the long period
	 */
	private int _longperiod = 26;
	
	/**
	 * The length of the short period
	 */
	private int _shortperiod = 12;
	
	/**
	 * The length of the signalling period
	 */
	private int _signalperiod = 9;

	/**
	 * Creates a new MACD object.
	 * 
	 * @param longperiod The length of the long period
	 * @param shortperiod The length of the short period
	 * @param signalperiod The length of the signalling period
	 * @param inst The instrument (not used)
	 */
	public MACD(
		int longperiod,
		int shortperiod,
		int signalperiod,
		String inst) {
		_longperiod = longperiod;
		_shortperiod = shortperiod;
		_signalperiod = signalperiod;
		_instrument = inst;
	}

	/**
	 * Adds a new quote to the indicator. The daily MACD
	 * is computed using the difference of the
	 * moving averages over the short and long
	 * periods. If sufficient MACDs have been 
	 * computed then the indicator is enabled.
	 * 
	 * @param data The stock data (in chronological order)
	 */
	public void add(HistoricData data) 
	{
		_data.add(data);

		if (_ready) {
			_data.remove(0);
		}
		
		if (_data.size() >= _longperiod) 
		{
			float MACD = shortMovAvg() - longMovAvg();
			_avgdata.add(new Float(MACD));
			
			if (_avgdata.size() > _signalperiod + 1) 
			{
				_avgdata.remove(0);
			}
		}
		
		if (_data.size() > _longperiod + _signalperiod) 
		{
			_ready = true;
			generateIndicator();
		}
	}

	/**
	 * calculates the average closing price
	 * over the long period
	 * 
	 * @return The average long period
	 */
	private float longMovAvg() 
	{
		int end = _data.size() - 1;
		int start = _data.size() - _longperiod;
		float sum = 0;
		for (int i = start; i < end; i++) {
			sum += ((HistoricData) _data.get(i)).getClose();
		}
		return sum / _longperiod;
	}
	
	/**
	 * calculates the average closing price
	 * over the short period.
	 * 
	 * @return The short average price
	 */
	private float shortMovAvg() 
	{
		int end = _data.size() - 1;
		int start = _data.size() - _shortperiod;
		float sum = 0;
		for (int i = start; i < end; i++) {
			sum += ((HistoricData) _data.get(i)).getClose();
		}
		return sum / _shortperiod;
	}

	/**
	 * Generates the indicator. If the previous average 
	 * of the MACDs over the signalling period is smaller
	 * than the previous MACD and the current average
	 * of the MACDS over the signalling periof is
	 * greater then the current MACD, then a sell
	 * signal is given. A buy signal is issued in
	 * the opposite case, otherwise hold.
	 */
	private void generateIndicator() 
	{
		float mov1 = 0;
		float mov2 = 0;

		float p1 = ((Float) _avgdata.get(_signalperiod - 1)).floatValue();
		float p2 = ((Float) _avgdata.get(_signalperiod)).floatValue();

		for (int i = 0; i < _signalperiod; i++) {
			mov1 += ((Float) _avgdata.get(i)).floatValue();
		}

		for (int i = 1; i <= _signalperiod; i++) {
			mov2 += ((Float) _avgdata.get(i)).floatValue();
		}

		mov1 = mov1 / _signalperiod;
		mov2 = mov2 / _signalperiod;

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
	 * @return Its name
	 */
	public String getName() {

		return toString(_longperiod, _shortperiod, _signalperiod);
	}

	/**
	 * Gets the string representation of a MACD
	 * 
	 * @param longperiod The long period
	 * @param shortperiod The short period
	 * @param signalperiod The signalling periof
	 * @return The string representation of MACD
	 */
	public static String toString(
		int longperiod,
		int shortperiod,
		int signalperiod) {
		return "MACD("
			+ longperiod
			+ ","
			+ shortperiod
			+ ","
			+ signalperiod
			+ ")";
	}

}