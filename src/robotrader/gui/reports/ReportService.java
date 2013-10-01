package robotrader.gui.reports;

import lu.base.iris.AbstractService;
import javax.swing.ListModel;
import javax.swing.DefaultListModel;

import robotrader.gui.HtmlReport;

/**
 * A service for HTML reports.
 * 
 * @author xcapt
 * @author klinst
 */
public class ReportService extends AbstractService 
{
  /**
   * The model containing the HTML reports
   */
	private DefaultListModel _reports;

  /**
   * initialises an empty HTML report list model
   */
	public boolean init() 
  {
		_reports = new DefaultListModel();
		return true;
	}

  /**
   * removes all HTML reports from the model
   */
	public void cleanup() 
  {
		_reports.removeAllElements();
	}

  /**
   * Adds a report to the list model
   * 
   * @param report The report to be added
   */
	public void addReport(HtmlReport report) 
  {
		_reports.addElement(report);
	}

  /**
   * Removes a report from the list model.
   * 
   * @param report The report to be removed
   * @return Successful or not
   */
	public boolean removeReport(HtmlReport report) 
  {
		return _reports.removeElement(report);
	}

  /**
   * Get ths HTML report list model
   * 
   * @return The HMTL report list model
   */
	public ListModel getListModel()
	{
		return _reports;
	}
}
