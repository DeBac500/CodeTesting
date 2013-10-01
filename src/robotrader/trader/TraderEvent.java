package robotrader.trader;

/**
 * represents a trading event that is either
 * executed or rejected.
 */
public class TraderEvent {
	/**
	 * represents an executed order
	 */
	public static final int ORDER_EXEC = -1;
	
	/**
	 * represents a rejected order
	 */
	public static final int ORDER_REJECT = -2;

	/**
	 * The event type of this event
	 */
	private int _type;

	/**
	 * creates a new trader event with the
	 * given type
	 * 
	 * @param type either TraderEvent.ORDER_EXEC or 
	 * TraderEvent.ORDER_REJECT
	 */
	public TraderEvent(int type) {
		_type = type;
	}

	/**
	 * returns the trader event type
	 * 
	 * @return either TraderEvent.ORDER_EXEC or 
	 * TraderEvent.ORDER_REJECT
	 */
	public int getType() {
		return _type;
	}
}
