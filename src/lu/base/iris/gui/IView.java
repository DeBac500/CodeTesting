package lu.base.iris.gui;
/**
 * IView description : interface for implementation of a concrete view
 */
public interface IView
{
	/**
	 * Method setVisible: display or hide view .
	 */
	void setVisible(boolean visible);
  
	/**
	 * Method hide : hide view.
	 */
	void hide();
	/**
	 * Method setTitle : set view title.
	 * @param title
	 */
	void setTitle(String title);
	/**
	 * Method setStatus : set status bar text
	 * @param txt status 
	 */
	void setStatus(String txt);
}


