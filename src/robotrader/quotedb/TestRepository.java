package robotrader.quotedb;

import java.util.Iterator;
/**
 * @author bob
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class TestRepository {

	public static void main(String[] args) {
		QuoteRepositoryImpl repo = new QuoteRepositoryImpl();
		repo.load();
		System.out.println("Instruments:");
		for (Iterator it = repo.getInstruments().iterator(); it.hasNext();) {
			String instr = (String) it.next();
			System.out.println(
				instr
					+ " "
					+ repo.getStartDate(instr)
					+ "-"
					+ repo.getEndDate(instr)
					+ " ("
					+ repo.getQuotes(instr).size()
					+ " days)");
		}
	}
}
