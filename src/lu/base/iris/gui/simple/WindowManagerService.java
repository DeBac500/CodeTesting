/*
 * Created on Aug 13, 2003
*/
package lu.base.iris.gui.simple;

import java.util.MissingResourceException;

import javax.swing.ImageIcon;

import lu.base.iris.AbstractService;
import lu.base.iris.AppToolkit;
import lu.base.iris.gui.AbstractAppMainView;
import lu.base.iris.gui.AbstractGUIApplication;
import lu.base.iris.gui.GUIToolkit;
import lu.base.iris.gui.IDetailedWindow;
import lu.base.iris.gui.IDialog;
import lu.base.iris.gui.IView;
import lu.base.iris.gui.IViewComponent;
import lu.base.iris.gui.IWindow;
import lu.base.iris.gui.IWindowManager;
import lu.base.iris.services.*;

import org.apache.log4j.Logger;

/**
 * WindowManagerService description : service implementation encapsulating a
 * window manager
 */
public class WindowManagerService
	extends AbstractService
	implements IWindowManager {
	private IWindowManager _winmgr;
	private static WindowManagerService _winsrv = null;

	private static Logger trace = Logger.getLogger(WindowManagerService.class);

	/**
	 * @see java.lang.Object#Object()
	 */
	public WindowManagerService() {
		super("WindowManagerService");
		if (_winsrv == null) {
			_winsrv = this;
		}
	}

	/**
	 * @see ch.eri.iris.IService#init()
	 */
	public boolean init() {
		AbstractGUIApplication app =
			(AbstractGUIApplication) AppToolkit.getApp();
		app.setMainView((AbstractAppMainView) getMainView());

		ResourceService res =
			(ResourceService) getService(ResourceService.class);

		try {
			ImageIcon icon = res.getIcon("APP_LOGO");
			if ((icon != null) && (GUIToolkit.getMainFrame() != null)) {
				GUIToolkit.getMainFrame().setIconImage(icon.getImage());
			}
		} catch (MissingResourceException e) {
			trace.warn("No application icon found "+e.getMessage());
		}

		return true;
	}

	/**
	 * @see ch.eri.iris.IService#cleanup()
	 */
	public void cleanup() {
		return;
	}

	/**
	 * Method getWindowManager.
	 * @return IWindowManager
	 */
	private IWindowManager getWindowManager() {
		if (_winmgr == null) {
			//_winmgr = OutlookWindowManager.getInstance();
			_winmgr = SimpleWindowManager.getInstance();
		}
		return _winmgr;
	}

	/**
	 * @see ch.eri.iris.gui.mdi.IWindowManager#makeDialog(ch.eri.iris.gui.mdi.IViewComponent)
	 */
	public IDialog makeDialog(IViewComponent component) {
		return getWindowManager().makeDialog(component);
	};
	/**
	 * @see ch.eri.iris.gui.mdi.IWindowManager#makeDialog(ch.eri.iris.gui.mdi.IViewComponent, ch.eri.iris.gui.mdi.IView)
	 */
	public IDialog makeDialog(IViewComponent component, IView parent) {
		return getWindowManager().makeDialog(component, parent);
	};
	/**
	 * @see ch.eri.iris.gui.mdi.IWindowManager#makeWindow(ch.eri.iris.gui.mdi.IViewComponent)
	 */
	public IWindow makeWindow(IViewComponent component) {
		return getWindowManager().makeWindow(component);
	};
	/**
	 * @see ch.eri.iris.gui.mdi.IWindowManager#getMainView()
	 */
	public IWindow getMainView() {
		return getWindowManager().getMainView();
	};

	/**
	 * Method getInstance.
	 * @return WindowManagerService
	 */
	public static WindowManagerService getInstance() {
		if (_winsrv == null) {
			_winsrv = new WindowManagerService();
		}
		return _winsrv;
	}

	/**
	 * @see ch.eri.iris.gui.mdi.IWindowManager#makeDetailedWindow(ch.eri.iris.gui.mdi.IViewComponent)
	 */
	public IDetailedWindow makeDetailedWindow(IViewComponent component) {
		return getWindowManager().makeDetailedWindow(component);
	}

	/**
	 * @see ch.eri.iris.gui.mdi.IWindowManager#updateDetailedWindow(ch.eri.iris.gui.mdi.IDetailedWindow, ch.eri.iris.gui.mdi.IViewComponent)
	 */
	public void updateDetailedWindow(
		IDetailedWindow parent,
		IViewComponent details) {
		getWindowManager().updateDetailedWindow(parent, details);
	}
}
