package lu.base.iris.gui;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import lu.base.iris.IApplication;

/**
 * AbstractAppMainView description : base implementation for application main
 * view using a JFrame
 */
public abstract class AbstractAppMainView extends JFrame {
	/** application object held in main view */
	protected IApplication _application;

	/**
	 * AbstractAppMainView constructor: creates a JFrame with the given
	 * title. registers the created main frame to the GUIToolkit.
	 * @see java.awt.Frame#Frame(java.lang.String)
	 */
	public AbstractAppMainView(String title) {
		super(title);
		GUIToolkit.setMainFrame(this);
	}

	
	/**
	 * Method setApplication: set application object.
	 * @param application
	 */
	public void setApplication(IApplication application) {
		_application = application;
	}

	/**
	 * Method close: used to close the application from the main view object .
	 * calls quit() for the application.
	 */
	public void close() {
		_application.stop();
	}

	/**
	 * Method init.
	 */
	public abstract void init();

	public void addHelpMenu(JMenuItem[] menu) {
		for (int i = menu.length - 1; i > -1; i--) {
			getJMenuBar().add(menu[i]);
		}
	}

	/**
	 * Method putModule: adds a swing module to the main view to generate the
	 * menubar for the main view. This is done by calling getActions() on the
	 * module to get all possible menus.
	 * @param module swing module to retrieve actions
	 */
	public void putModule(AbstractSwingModule module) {
		AbstractAction actions[] = module.getActions();

		JMenu menu = new JMenu(module.getTitle());

		for (int i = 0; i < actions.length; i++) {
			if (actions[i] == null) {
				menu.addSeparator();
			} else {
				menu.add(new JMenuItem(actions[i]));
			}
		}

		getJMenuBar().add(menu);
	}
}