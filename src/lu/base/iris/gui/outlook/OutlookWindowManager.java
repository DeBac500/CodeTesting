package lu.base.iris.gui.outlook;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import lu.base.iris.gui.IDetailedWindow;
import lu.base.iris.gui.IViewComponent;
import lu.base.iris.gui.IWindow;
import lu.base.iris.gui.IWindowManager;
import lu.base.iris.gui.mdi.DefaultWindowManager;
import lu.base.iris.gui.mdi.ViewComponentEvent;

/**
 * OutlookWindowManager description : window manager implementation allowing to
 * create views arranged in an outlook like presentation.
 */
public class OutlookWindowManager extends DefaultWindowManager {
	private OutlookMainView _mainview;
	private HashMap _windowMap = new HashMap();
	private HashMap _viewcomponentMap = new HashMap();
	private int _eventcount = 0;
	private boolean _useScrollPane = false;

	/**
	* MenuActionListener description : menu activation listener, creates a popup
	* menu showing the current menu. Used to have individual menus for each
	* window in the outlook main pane.
	*/
	class MenuActionListener implements ActionListener {
		private JMenu _menu;
		private JPopupMenu _popup;

		/**
		 * Method MenuActionListener: creates an action listener with the
		 * current menu.
		 * @param menu
		 */
		MenuActionListener(JMenu menu) {
			_menu = menu;
		}

		/**
		 * shows the menu at the source position
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof JButton) {
				getPopupMenu().show(
					(JButton) e.getSource(),
					0,
					((JButton) e.getSource()).getSize().height);
			}
		}

		/**
		 * Method getPopupMenu: creates the popup menu with the items contained
		 * in the member menu.
		 * @return JPopupMenu
		 */
		private JPopupMenu getPopupMenu() {
			if (_popup == null) {
				_popup = new JPopupMenu();
				Component[] comps = (Component[]) _menu.getMenuComponents();
				for (int j = 0; j < comps.length; j++) {
					_popup.add(comps[j]);
				}
				_popup.setLightWeightPopupEnabled(false);
			}
			return _popup;
		}
	}

	/**
	 * OutlookWindowManager constructor: private to implement singleton pattern
	 */
	private OutlookWindowManager() {
	}

	/**
	 * factory method for singleton pattern: only one instance can run at any
	 * time.
	 * @see lu.base.iris.gui.mdi.DefaultWindowManager#getInstance()
	 */
	public static IWindowManager getInstance() {
		if (_winmgr == null) {
			_winmgr = new OutlookWindowManager();
		}
		return _winmgr;
	}

	/**
	 * Method getOutlookMainView: gets the single instance of the
	 * OutlookMainView.
	 * @return OutlookMainView main view
	 */
	private OutlookMainView getOutlookMainView() {
		if (_mainview == null) {
			_mainview = OutlookMainView.getInstance("Vigil - Olyscript");
		}
		return _mainview;
	}

	/**
	 * @see lu.base.iris.gui.IWindowManager#getMainView()
	 */
	public IWindow getMainView() {
		return getOutlookMainView();
	}

	/**
	 * @see lu.base.iris.gui.IWindowManager#makeWindow(lu.base.iris.gui.IViewComponent)
	 */
	public IWindow makeWindow(IViewComponent component) {
		Object o = _windowMap.get(component);
		DefaultWindowPanel mainpane;
		if (o == null) {
			mainpane = new DefaultWindowPanel();
			mainpane.setLayout(new BorderLayout());			
			if(_useScrollPane)
			{
				JScrollPane spane = new JScrollPane();
				spane.setViewportView(component.getComponent());
				mainpane.add(spane, BorderLayout.CENTER);
			}
			else
			{
				mainpane.add(component.getComponent(), BorderLayout.CENTER);
			}
			
			JToolBar menubar = new JToolBar();
			JMenu[] menus = component.getMenus();
			if (menus != null) {
				for (int i = 0; i < menus.length; i++) {

					JButton but = new JButton(menus[i].getText());
					but.addActionListener(new MenuActionListener(menus[i]));

					menubar.add(but);
				}
				mainpane.add(menubar, BorderLayout.NORTH);
			}

			mainpane.setTitle(component.getTitle());
			register(component, mainpane);
		} else {
			mainpane = (DefaultWindowPanel) o;
		}
		return mainpane;
	};

	/**
	 * @see lu.base.iris.gui.IWindowManager#makeDetailedWindow(lu.base.iris.gui.IViewComponent)
	 */
	public IDetailedWindow makeDetailedWindow(IViewComponent component) {
		Object o = _windowMap.get(component);
		DefaultDetailedWindow win;
		if (o == null) {
			//JScrollPane spane = new JScrollPane();     
			JPanel pane = new JPanel();
			pane.setLayout(new BorderLayout());
			//spane.setViewportView();
			pane.add(component.getComponent(), BorderLayout.CENTER);

			JToolBar menubar = new JToolBar();
			JMenu[] menus = component.getMenus();
			if (menus != null) {
				menus = (JMenu[]) menus.clone();
				for (int i = 0; i < menus.length; i++) {

					JButton but = new JButton(menus[i].getText());
					but.addActionListener(new MenuActionListener(menus[i]));

					menubar.add(but);
				}
				pane.add(menubar, BorderLayout.NORTH);
			}

			win = new DefaultDetailedWindow();
			win.setMainComponent(pane);
			win.setTitle(component.getTitle());
			register(component, win);
		} else {
			win = (DefaultDetailedWindow) o;
		}
		return win;
	};

	/**
	 * @see lu.base.iris.gui.IWindowManager#updateDetailedWindow(lu.base.iris.gui.IDetailedWindow, lu.base.iris.gui.IViewComponent)
	 */
	public void updateDetailedWindow(
		IDetailedWindow mainview,
		IViewComponent component) {
		//JScrollPane spane = new JScrollPane();     
		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
		//spane.setViewportView(component.getComponent());
		//pane.add(spane, BorderLayout.CENTER);
		pane.add(component.getComponent(), BorderLayout.CENTER);

		JToolBar menubar = new JToolBar();
		JMenu[] menus = component.getMenus();
		if (menus != null) {
			for (int i = 0; i < menus.length; i++) {

				JButton but = new JButton(menus[i].getText());
				but.addActionListener(new MenuActionListener(menus[i]));

				menubar.add(but);
			}
			pane.add(menubar, BorderLayout.NORTH);
		}

		DefaultDetailPanel win =
			new DefaultDetailPanel((DefaultDetailedWindow) mainview);
		win.add(pane);
		win.setTitle(component.getTitle());
		win.setVisible(true);
	};

	/**
	 * Method register: registers the model (view component) with the created
	 * window.
	 * @param component view component used to create the window
	 * @param win window created from the view component.
	 */
	private void register(IViewComponent component, IWindow win) {
		_windowMap.put(component, win);
		_viewcomponentMap.put(win, component);
	}

	/**
	 * Method onHide: used to send the hide event to the view component that
	 * was used to create the window. Allows to process actions in the model
	 * when closing the window.
	 * @param win window being hide
	 */
	void onHide(IWindow win) {
		
		if(win instanceof DefaultDetailedWindow)
		{
			((DefaultDetailedWindow)win).clearDetails();
		}
		
		IViewComponent comp = (IViewComponent) _viewcomponentMap.get(win);
		if (comp != null) {
			comp.onEvent(
				new ViewComponentEvent(
					this,
					ViewComponentEvent.HIDE,
					_eventcount++,
					null));
		}
	}
	/**
	 * Sets the useScrollPane : allows to use scroll panes in contained windows.
	 * @param useScrollPane The useScrollPane to set
	 */
	public void setUseScrollPane(boolean useScrollPane) {
		_useScrollPane = useScrollPane;
	}

	/**
	 * Returns the useScrollPane.
	 * @return boolean
	 */
	public boolean isUseScrollPane() {
		return _useScrollPane;
	}

}