package robotrader.market.indicator.impl;

import java.util.ArrayList;
import java.util.List;

import robotrader.market.AbstractIndicator;
import robotrader.market.HistoricData;
import robotrader.market.IIndicator;

/**
 * Joe Granville introduced the On Balance Volume (OBV)
 * indicator in his 1963 book, <em>Granville's New Key to Stock Market Profits</em>.
 * This was one of the first and most popular indicators to measure
 * positive and negative volume flow. The concept behind the indicator:
 * volume precedes price. OBV is a simple indicator that adds a period's
 * volume when the close is up and subtracts the period's volume when the
 * close is down. A cumulative total of the volume additions and
 * subtractions forms the OBV line. This line can then be compared with
 * the price chart of the underlying security to look 
 * for divergences or confirmation.
 *  
 * @author klinst
 */
public class OBV extends AbstractIndicator
{
  /**
   * The list of obv values over the period
   */
  private List _obv;
  
  /**
   * The list of closing prices over the period
   */
  private List _data;
  
  /**
   * The training period length
   */
  private int _period;
  
	/**
	 * Creates a new OBV object.
	 * 
	 * @param period the period the indicator requires for training
	 * @param inst the underlying instrument
	 */
	public OBV(int period, String inst) 
	{
		_period = period;
		_instrument = inst;
    _obv = new ArrayList();
    _data = new ArrayList();
	}

	/**
	 * Adds historic data to the 
	 * Indicator. The indicator is trained
	 * over the given period and is
	 * constantly updated after the
	 * period has been exceeded.
	 * 
	 * @see robotrader.trader.indicator.IIndicator#add(HistoricData)
	 */
	public void add(HistoricData data) 
	{
		float current_close = data.getAdjustedClose();
    float previous_close = 0;
    float volume = data.getVolume();
    
    float current_obv = 0;
    float previous_obv = 0;
    
    if (_obv.size() > 0)
    {
      Float f = (Float)_obv.get(_obv.size() - 1);
      previous_obv = f.floatValue();
      
      f = (Float)_data.get(_data.size() - 1);
      previous_close = f.floatValue();
    }

    if (current_close > previous_close)
    {
      current_obv = previous_obv + volume;
    }
    else if (current_close < previous_close)
    {
      current_obv = previous_obv - volume;
    }
    else
    {
      current_obv = previous_obv;
    }
    
    _obv.add(new Float(current_obv));
    _data.add(new Float(current_close));
    
    if (_obv.size() == _period)
    {
      _ready = true;
      
      Float f = (Float)_obv.get(0);
      float start_obv = f.floatValue();
      
      f = (Float)_obv.get(_period - 1);
      float end_obv = f.floatValue();

      f = (Float)_data.get(0);
      float start_price = f.floatValue();
      
      f = (Float)_data.get(_period - 1);
      float end_price = f.floatValue();
      
      if (end_price > start_price)
      {
        if (end_obv <= start_obv)
        {
          _direction = IIndicator.SELL;  
        }
        else if (end_obv > start_obv)
        {
          _direction = IIndicator.BUY;
        }
        else
        {
          _direction = IIndicator.HOLD;
        }
      }
      else if (end_price < start_price)
      {
        if (end_obv > start_obv)
        {
          _direction = IIndicator.SELL;  
        }
        else
        {
          _direction = IIndicator.HOLD;
        }
      }
      else
      {
        _direction = IIndicator.HOLD;
      }
      
      _obv.remove(0);
      _data.remove(0);
    }
	}

  
	/**
	 * returns a description of the indicator
	 * 
	 * @return short name
	 */
	public String getName() 
	{
		return toString(_period);
	}
	
	/**
	 * gets the OBV description as a string
	 *  
	 * @param period the period required for training
	 * @param buylevel the buying level
	 * @param selllevel the selling level
	 * @return
	 */
	public static String toString(
		int period) 
	{
		return "OBV" + period;
	}
}