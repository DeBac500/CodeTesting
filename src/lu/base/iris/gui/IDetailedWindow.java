package lu.base.iris.gui;



/**
 * IDetailedWindow description : interface for the implementation of a two part
 * window: list and detail of selected item in list
 */
public interface IDetailedWindow extends IWindow
{
	/**
	 * Method getDetails.
	 * @return IView view object for the details
	 */
	IView getDetails();
}


