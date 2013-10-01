package robotrader.market;

/**
* IndicatorToolKit    description: This class contains simple methods to
* manipulate data from IIndicator objects, like converting an int direction to a
* readable String.
 * <br>
 */
public class IndicatorToolKit {
	/**
	 * Method toString: converts a direction given by an IIndicator object to a
	 * printable String:<br>
	 * <pre>
	 * "BUY", "SELL", "HOLD", "OVERSOLD", "OVERBOUGHT", "NA"
	 * </pre>
	 * @param dir direction given by an IIndicator object, for valid values 
	 * @see IIndicator#getDirection() 
	 * @return String 
	 */
	public static String toString(int dir) {
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
}
