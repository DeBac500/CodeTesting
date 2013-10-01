package robotrader.engine.event;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Simple event generator. If there has been
 * a change in day, month or year the
 * DAY, MONTH or YEAR event is generated.
 * 
 * @author bob 
 * @author klinst
 */
public class EventGenerator 
{
	/**
	 * The Gregorian Calendar
	 */
	private static Calendar _calendar = GregorianCalendar.getInstance();
	
	/**
	 * The last encountered date
	 */
	private Date _date = null;
	
	/**
	 * The last encountered month
	 */
	private int _month = -1;
	
	/**
	 * The last encountered year
	 */
	private int _year = -1;

	/**
	 * Gets either a DAY, MONTH or YEAR
	 * event depending on the the
	 * difference of the date and
	 * the previous date
	 *
	 * @param date The most recent date
	 * @return An instance of an event
	 */
	public Event getEvent(Date date) 
	{
		Event evt = null;

		_calendar.setTime(date);

		int year = _calendar.get(Calendar.YEAR);
		int month = _calendar.get(Calendar.MONTH);

		if (_date == null) 
		{
			evt = Event.DAY_EVENT;
		} 
		else 
		{
			if (_year != year) 
			{
				evt = Event.YEAR_EVENT;
			} 
			else if (_month != month) 
			{
				evt = Event.MONTH_EVENT;
			} 
			else 
			{
				evt = Event.DAY_EVENT;
			}
		}

		_date = date;
		_year = year;
		_month = month;

		return evt;
	}

	/**
	 * initialises the event
	 * generator.
	 */
	public void init() 
	{
		_date = null;
		_month = -1;
		_year = -1;
	}
}