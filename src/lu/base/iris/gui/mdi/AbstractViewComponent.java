package lu.base.iris.gui.mdi;

import java.awt.Component;

import javax.swing.JMenu;
import javax.swing.JToolBar;

import lu.base.iris.gui.IViewComponent;
import lu.base.iris.gui.IViewComponentEvent;

public abstract class AbstractViewComponent implements IViewComponent {

	/**
	 * @see IViewComponent#getComponent()
	 */
	public abstract Component getComponent();

	/**
	 * @see IViewComponent#getTitle()
	 */
	public abstract String getTitle();

	/**
	 * @see IViewComponent#getJMenuBar()
	 */
	public JMenu[] getMenus() {
		return null;
	}
	/**
	 * @see IViewComponent#getToolBar()
	 */
	public JToolBar getToolBar() {
		return null;
	}
	/**
	 * @see lu.base.iris.gui.IViewComponent#onEvent(lu.base.iris.gui.
	 * IViewComponentEvent)
	 */
	public void onEvent(IViewComponentEvent evt) {
	}
}
