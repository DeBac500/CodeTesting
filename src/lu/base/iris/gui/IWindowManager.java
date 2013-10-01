package lu.base.iris.gui;



/**
 * IWindowManager description : interface for window manager, responsible for
 * creating IWindow, IDialog and IDetailedWindow objects. This factory
 * approach for windows and dialogs allows a better management of windows
 * accross an application
 */
public interface IWindowManager
{
	IDialog makeDialog(IViewComponent component);
	IDialog makeDialog(IViewComponent component, IView parent);
	IWindow makeWindow(IViewComponent component);
	/**
	 * creates a window with a linked detail window
	 */
	IDetailedWindow makeDetailedWindow(IViewComponent maincomp);
	/**
	 * update detail window detail content
	 */
	void updateDetailedWindow(IDetailedWindow maincomp, IViewComponent details);
	/**
	 * get main view IWindow object
	 */
	IWindow getMainView();
}


