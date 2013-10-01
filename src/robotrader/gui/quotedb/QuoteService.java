/*
 * Created on Aug 14, 2003
 */
package robotrader.gui.quotedb;

import java.util.Collection;
import java.util.List;

import lu.base.iris.AbstractService;
import lu.base.iris.AppToolkit;
import lu.base.iris.services.UserPrefService;
import robotrader.quotedb.IQuoteRepository;

import robotrader.quotedb.QuoteRepositoryImpl;

/**
 * QuoteService
 */
public class QuoteService extends AbstractService implements IQuoteRepository {

	private QuoteRepositoryImpl _quoterepo;

	/* (non-Javadoc)
	 * @see robotrader.quotedb.IQuoteRepository#addQuoteList(java.util.List)
	 */
	public void addQuoteList(List quotelist) {
		_quoterepo.addQuoteList(quotelist);
	}

	/* (non-Javadoc)
	 * @see robotrader.quotedb.IQuoteRepository#getQuotes(java.lang.String)
	 */
	public List getQuotes(String instrument) {
		return _quoterepo.getQuotes(instrument);
	}

	/* (non-Javadoc)
	 * @see robotrader.quotedb.IQuoteRepository#getInstruments()
	 */
	public Collection getInstruments() {
		return _quoterepo.getInstruments();
	}

	/* (non-Javadoc)
	 * @see robotrader.quotedb.IQuoteRepository#getStartDate(java.lang.String)
	 */
	public String getStartDate(String instrument) {
		return _quoterepo.getStartDate(instrument);
	}

	/* (non-Javadoc)
	 * @see robotrader.quotedb.IQuoteRepository#getEndDate(java.lang.String)
	 */
	public String getEndDate(String instrument) {
		return _quoterepo.getEndDate(instrument);
	}

	/* (non-Javadoc)
	 * @see robotrader.quotedb.IQuoteRepository#removeQuotes(java.lang.String)
	 */
	public void removeQuotes(String instrument) {
		_quoterepo.removeQuotes(instrument);
	}

	/* (non-Javadoc)
	 * @see lu.base.iris.IService#init()
	 */
	public boolean init() {
		// create quote repository implementation
		_quoterepo = new QuoteRepositoryImpl();
		// retrieve master quote file configuration
		UserPrefService prefserv = (UserPrefService)AppToolkit.getService(UserPrefService.class);
		_quoterepo.setMasterQuoteFile(prefserv.getStringValue("QUOTE_MASTER_FILE"));
		// load file
		_quoterepo.load();
		return true;
	}

	/* (non-Javadoc)
	 * @see lu.base.iris.IService#cleanup()
	 */
	public void cleanup() {
		_quoterepo.save();
	}

	public void setMasterQuoteFile(String quotefile)
	{
		_quoterepo.save();
		_quoterepo.setMasterQuoteFile(quotefile);
		_quoterepo.load();
	}
	public String getMasterQuoteFile()
	{
		return _quoterepo.getMasterQuoteFile();
	}
}
