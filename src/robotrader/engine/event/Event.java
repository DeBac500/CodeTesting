package robotrader.engine.event;

/**
 * An event for triggering changes in days, months
 * or years of quote information
 * 
 * @author bob
 * @author klinst 
 */
public class Event 
{
	/**
	 * Year constant
	 */
	private static final int YEAR = 0;
	
	/**
	 * Month constant
	 */
	private static final int MONTH = 1;
	
	/**
	 * Day constant
	 */
	private static final int DAY = 2;

	/** The event of a changing year */
	public static final Event YEAR_EVENT = new Event(YEAR);

	/** The event of a changing month */
	public static final Event MONTH_EVENT = new Event(MONTH);

	/** The event of a changing day */
	public static final Event DAY_EVENT = new Event(DAY);
	
	/**
	 * The event type (DAY, MONTH or YEAR)
	 */
	private int _type;

	/**
	 * Create new event of the given type.
	 * 
	 * @param type The event type
	 */
	Event(int type) 
	{
		_type = type;
	}

	/**
	 * Is it a DAY event
	 * 
	 * @return true if type is DAY, MONTH or YEAR
	 */
	public boolean isDayEvent() 
	{
		return ((_type == YEAR) || (_type == DAY) || (_type == MONTH));
	}

	/**
	 * Is it a MONTH event
	 * 
	 * @return true if type is MONTH or YEAR
	 */
	public boolean isMonthEvent() 
	{
		return ((_type == YEAR) || (_type == MONTH));
	}

	/**
	 * Get the event type
	 * 
	 * @return either DAY, MONTH or YEAR
	 */
	public int getType() 
	{
		return _type;
	}

	/**
	 * Is it a year event
	 * 
	 * @return true if type is YEAR
	 */
	public boolean isYearEvent() 
	{
		return (_type == YEAR);
	}
}