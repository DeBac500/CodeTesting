package lu.base.iris.gui;

import java.awt.Component;

import javax.swing.JMenu;
import javax.swing.JToolBar;

/**
 * IViewComponent description : view component to be embedded in a IView object.
 */
public interface IViewComponent
{
	
	/**
	 * Method getComponent: gets the component to be embedded in IView
	 * @return Component component to be embedded in IView
	 */
	Component getComponent();
	/**
	 * Method getTitle: returns view title.
	 * @return String view title
	 */
	String getTitle();
	/**
	 * Method getMenus : retrieves the menus for the view.
	 * @return JMenu[] view menu
	 */
	JMenu[] getMenus();
	/**
	 * Method getToolBar: retrieves the toolbar available for the view.
	 * @return JToolBar view toolbar
	 */
	JToolBar getToolBar();
	/**
	 * Method onEvent: event publishing method.
	 * @param evt event sent by event dispatcher
	 */
	void onEvent(IViewComponentEvent evt);
}


