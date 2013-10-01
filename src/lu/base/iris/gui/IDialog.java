package lu.base.iris.gui;


/**
 * IDialog description : interface for implementations of IView with a blocking
 * dialog box. show() is blocking for IDialog. Dialog boxes have only 2 return
 * methods : Ok or cancel
 */
public interface IDialog extends IView
{
	/**
	 * Method hasUserCancelled: retrieve return method.
	 * @return boolean true if user has cancelled dialog, false if user
	 * confirmed dialog
	 */
	boolean hasUserCancelled();
	/**
	 * Method setSize: set size attributes.
	 * @param w width in pixels
	 * @param h height in pixels
	 */
	void setSize(int w, int h);
	/**
	 * Method setResizable: set resizable attribute.
	 * @param resizing set resizable attribute
	 */
	void setResizable(boolean resizing);
}


