package robotrader.quotedb;

import java.util.Collection;
import java.util.List;
/**
 * Interface for a quote repository
 */
public interface IQuoteRepository {
	/**
	 * Method addQuoteList.
	 * @param quotelist
	 */
	void addQuoteList(List quotelist);

	/**
	 * Method getQuotes.
	 * @param instrument
	 * @return List
	 */
	List getQuotes(String instrument);

	/**
	 * Method getInstruments.
	 * @return Collection
	 */
	Collection getInstruments();

	/**
	 * Method getStartDate.
	 * @param instrument
	 * @return String
	 */
	String getStartDate(String instrument);
	/**
	 * Method getEndDate.
	 * @param instrument
	 * @return String
	 */
	String getEndDate(String instrument);

	/**
	 * Method removeQuotes.
	 * @param instrument
	 */
	void removeQuotes(String instrument);
}
