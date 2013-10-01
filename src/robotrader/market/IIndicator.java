package robotrader.market;


/**
 * IIndicator  description: interface for all technical indicator
 * implementations. An IIndicator has a unique name and is specific to one
 * instrument(=stock) only. It works by adding data sequentially (in
 * chronological order) via an addition method. When sufficient data has been
 * collected for the indicator, methods can be used to query the direction given
 * by the indicator.
 * <br>
*/
public interface IIndicator {
	/** direction value for SELL signal */
	public static final int SELL = -1;

	/** direction value for OVERBOUGHT signal */
	public static final int OVERBOUGHT = -2;

	/** direction value for HOLD signal */
	public static final int HOLD = 0;

	/** direction value for BUY signal */
	public static final int BUY = 1;

	/** direction value for OVERSOLD signal */
	public static final int OVERSOLD = 2;

	/** default direction value when indicator not ready(not enough values) */
	public static final int NA = 9;

	/**
	 * Method getName: Returns the name and description of the implemented
	 * indicator, these names should be unique, therefore include indicator
	 * type but also the instrument and the parameters : e.g: INTL_RSI14
	 * 
	 * @return String name of indicator
	 */
	String getName();
	/**
	 * Adds the data of the current to the indicator to compute thevalue
	 * of the indicator at the end of that day.<br>
	 * Data MUST BE added sequentially (in chronological order) via this method.
	 *
	 * @param data HistoricData to be added 
	 */
	void add(HistoricData data);

	/**
	 * Method isReady: returns readyness of the indicator.
	 * @return boolean returns true when enough data has been collected by the
	 * indicator to give a valid direction
	 */
	boolean isReady();

	/**
	 * Method getDirection: valid values are:<br>
	 * SELL, OVERBOUGHT, HOLD, BUY, OVERSOLD, NA(indicator not ready)
	 * </pre>
	 * @return int returns indicator direction and NA when not available
	 */
	int getDirection();

	/**
	 * Method isBuy.
	 * @return boolean true when indicator indicates to buy
	 */
	boolean isBuy();
	/**
	 * Method isHold.
	 * @return boolean true when indicator indicates to hold
	 */
	boolean isHold();
	/**
	 * Method isSell.
	 * @return boolean true when indicator indicates to sell
	 */
	boolean isSell();
}