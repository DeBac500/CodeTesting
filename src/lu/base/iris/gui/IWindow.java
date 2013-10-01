package lu.base.iris.gui;



/**
 * IWindow description : interface for the implementation of an IWindow. Iwindow
 * are non-blocking. show() is non-blocking for IWindow
 */
public interface IWindow extends IView
{
	/**
	 * Method minimize.
	 */
	void minimize();
	/**
	 * Method maximize.
	 */
	void maximize();
	/**
	 * Method setSize.
	 * @param w
	 * @param h
	 */
	void setSize(int w, int h);
	/**
	 * Method setResizable.
	 * @param resizing
	 */
	void setResizable(boolean resizing);
}


