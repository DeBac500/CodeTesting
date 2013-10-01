package lu.base.iris.gui.simple;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lu.base.iris.IApplication;
import lu.base.iris.gui.AbstractAppMainView;
import lu.base.iris.gui.AbstractSwingModule;
import lu.base.iris.gui.IWindow;
import lu.base.awt.WindowUtils;

/**
 * SimpleMainView description : main view of the application with simple
 * presentation.
 */
public class SimpleMainView extends AbstractAppMainView implements IWindow {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2541985839642663242L;

	/**
	 * Singleton main view.
	 */
	private static SimpleMainView _mainview = null;

	private IApplication _application;
	private JPanel _modulePanel = new JPanel(new BorderLayout());
	private JLabel _moduleLabel = new JLabel();
	private StatusBar _statusbar = new StatusBar();

	/**
	 * SimpleMainView constructor: creates the main view.
	 * @param title 
	 */
	private SimpleMainView(String title) {
		super(title);
		//Create the menu bar.
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
	}

	/**
	 * initializes all components of the application
	 * @see lu.base.iris.gui.AbstractAppMainView#init()
	 */
	public void init() {
		WindowUtils.centerFrame(this);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});


		((BorderLayout) getLayout()).setVgap(5);
		_moduleLabel.setOpaque(true);
		_moduleLabel.setBackground(Color.gray);
		_moduleLabel.setForeground(Color.white);
		_moduleLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
		
		_statusbar.setText(getTitle());
		
		String welcomeMessage = "<html><h2>Welcome to Robotrader.</h2> <p>To begin a simulation, click on Simulation -> View, <br>and then click Start.</p></html>";
		JLabel welcomeLabel = new JLabel(welcomeMessage);
		welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
		welcomeLabel.setVerticalAlignment(JLabel.CENTER);
		_modulePanel.add(welcomeLabel);
		
		JPanel mainpanel = new JPanel(new BorderLayout());
		mainpanel.add(_moduleLabel, BorderLayout.NORTH);
		mainpanel.add(_modulePanel, BorderLayout.CENTER);
		mainpanel.add(_statusbar, BorderLayout.SOUTH);
		setContentPane(mainpanel);
	}

	/**
	 * @see lu.base.iris.gui.AbstractAppMainView#setApplication(lu.base.iris.IApplication)
	 */
	public void setApplication(IApplication application) {
		_application = application;
	}

	/**
	 * @see lu.base.iris.gui.AbstractAppMainView#close()
	 */
	public void close() {
		_application.stop();
	}

	/**
	 * Implements a status bar
	 */
	private class StatusBar extends JTextField {
		private static final long serialVersionUID = 3183680280290451207L;
		StatusBar() {
			super.setEditable(false);
		}
	}

	/**
	 * does nothing
	 * @see lu.base.iris.gui.IWindow#minimize()
	 */
	public void minimize() {
	};
	/**
	 * does nothing
	 * @see lu.base.iris.gui.IWindow#maximize()
	 */
	public void maximize() {
	};

	/**
	 * Method getInstance: retrieves the unique instance of the main view.
	 * @param title title used to crate the main view when not existing
	 * @return OutlookMainView application main view
	 */
	public static SimpleMainView getInstance(String title) {
		if (_mainview == null) {
			_mainview = new SimpleMainView(title);
		}
		return _mainview;
	}

	/**
	 * Method getInstance: retrieves the unique instance of the main view.
	 * @return SimpleMainView application main view
	 */
	public static SimpleMainView getInstance() {
		return getInstance("");
	}

	/**
	 * @see lu.base.iris.gui.AbstractAppMainView#putModule(lu.base.iris.gui.AbstractSwingModule)
	 */
	public void putModule(AbstractSwingModule module){
		AbstractAction[] actions = module.getActions();
		for (int i = 0; i < actions.length; i++) {
			if (actions[i] != null) {
				actions[i].putValue(
					AbstractAction.SMALL_ICON,
					module.getIcon());
			}
		}
		super.putModule(module);
	}

	/**
	 * Method show: changes the content of the module panel (main pane) in the
	 * simple presentation.
	 * @param title module label to display in the module title bar
	 * @param component module component to display in the module panel
	 */
	void show(String title, Component component) {
		_moduleLabel.setText(title);
		_modulePanel.removeAll();
		_modulePanel.add(component);
		_modulePanel.revalidate();
		_modulePanel.repaint();
	}

	public JPanel getMainPanel(){
		return _modulePanel;
	}
	/**
	 * updates text in the status bar.
	 * @see lu.base.iris.gui.IView#setStatus(java.lang.String)
	 */
	public void setStatus(String txt) {
		_statusbar.setText(txt);
		_statusbar.repaint();
	}

}