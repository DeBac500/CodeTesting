package robotrader.market;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract indicator. Provides the toString()
 * method implementation which ensures that it
 * is consistent for all indicators.
 *  
 * @author klinst
 */
public abstract class AbstractIndicator implements IIndicator 
{
	/** The indicator signals down */
	public static final int DOWN = -1;

	/** The indicator signals unchanged */
	public static final int UNCH = 0;

	/** The indicator signal up */
	public static final int UP = 1;
	
	/**
	 * List of Historic Data objects
	 * used for generating the indicator
	 */
	protected List _data = new ArrayList();

	/**
	 * The instrument
	 */
	protected String _instrument;
	
	/**
	 * Is the indicator ready
	 */
	protected boolean _ready = false;
	
	/**
	 * The direction of the indicator
	 */
	protected int _direction = NA;

	/**
	 * returns a description of the indicator
	 * 
	 * @return short name
	 */
	public final String toString() {
		return getName() + " -> " + toString(getDirection());
	};

	/**
	 * @return the name of the indicator
	 */
	public abstract String getName();

	/**
	 * translates the direction value
	 * into a string representation
	 * 
	 * @param dir the direction the indicator points towards
	 * @return a string translation of the direction
	 */
	public static String toString(int dir) 
	{
		switch (dir) {
			case IIndicator.BUY :
				return "BUY";

			case IIndicator.SELL :
				return "SELL";

			case IIndicator.HOLD :
				return "HOLD";

			case IIndicator.OVERSOLD :
				return "OVERSOLD";

			case IIndicator.OVERBOUGHT :
				return "OVERBOUGHT";

			default :
				return "NA";
		}
	}
	
	/**
	 * Does the indicator signal IIndicator.BUY
	 * @return yes or no
	 */
	public boolean isBuy() 
	{
		return (_direction == IIndicator.BUY);
	}

	/**
	 * What direction does the indicator point to
	 * @return The direction
	 */
	public int getDirection() 
	{
		return _direction;
	}

	/**
	 * Does the indicator signal IIndicator.HOLD
	 * @return yes or no
	 */
	public boolean isHold() 
	{
		return (_direction == IIndicator.HOLD);
	}

	/**
	 * Is the indicator ready to signal?
	 * @return yes or no
	 */
	public boolean isReady() 
	{
		return _ready;
	}

	/**
	 * Does the indicator signal IIndicator.SELL
	 * @return yes or no
	 */
	public boolean isSell() 
	{
		return (_direction == IIndicator.SELL);
	}

}
