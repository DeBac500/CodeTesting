package robotrader.quotedb.junit;

import java.util.Collection;
import java.util.List;

import robotrader.quotedb.QuoteRepositoryImpl;
import junit.framework.TestCase;

/**
 * Junit TestCase for QuoteRepositoryImpl.
 * 
 * @see robotrader.quotedb.QuoteRepositoryImpl
 * @author klinst
 */
public class QuoteRepositoryImplTest extends TestCase {

	QuoteRepositoryImpl _repo;
	
	public QuoteRepositoryImplTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
		_repo = new QuoteRepositoryImpl();
		_repo.load();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}



	/*
	 * Test method for 'robotrader.quotedb.QuoteRepositoryImpl.getQuotes(String)'
	 */
	public void testGetQuotes() {
		List quotes = _repo.getQuotes("GE");
		assertNotNull(quotes);
		assertTrue(quotes.size() > 0);
	}

	/*
	 * Test method for 'robotrader.quotedb.QuoteRepositoryImpl.getInstruments()'
	 */
	public void testGetInstruments() {
		Collection c = _repo.getInstruments();
		assertNotNull(c);
		assertTrue(c.size() > 0);

	}

	/*
	 * Test method for 'robotrader.quotedb.QuoteRepositoryImpl.save()'
	 */
	public void testSave() {
		_repo.save();
	}

	/*
	 * Test method for 'robotrader.quotedb.QuoteRepositoryImpl.setMasterQuoteFile(String)'
	 */
	public void testSetMasterQuoteFile() {
		_repo.setMasterQuoteFile("dummy.xml");
		String dummy = _repo.getMasterQuoteFile();
		assertTrue(dummy.equals("dummy.xml"));
	}

	/*
	 * Test method for 'robotrader.quotedb.QuoteRepositoryImpl.getMasterQuoteFile()'
	 */
	public void testGetMasterQuoteFile() {
		String master = _repo.getMasterQuoteFile();
		assertNotNull(master);
		assertTrue(master.indexOf("xml") > 0);
	}

	/*
	 * Test method for 'robotrader.quotedb.QuoteRepositoryImpl.getStartDate(String)'
	 */
	public void testGetStartDate() {
		String date = _repo.getStartDate("GE");
		assertNotNull(date);
	}

	/*
	 * Test method for 'robotrader.quotedb.QuoteRepositoryImpl.getEndDate(String)'
	 */
	public void testGetEndDate() {
		String date = _repo.getEndDate("GE");
		assertNotNull(date);
	}

	/*
	 * Test method for 'robotrader.quotedb.QuoteRepositoryImpl.removeQuotes(String)'
	 */
	public void testRemoveQuotes() {
		List list = _repo.getQuotes("GE");
		assertNotNull(list);
		_repo.removeQuotes("GE");
		_repo.addQuoteList(list);
		List list2 = _repo.getQuotes("GE");
		assertNotNull(list2);
		assertEquals(list, list2);
	}

}
