package lu.base.iris.gui.outlook;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import lu.base.iris.IApplication;
import lu.base.iris.gui.AbstractAppMainView;
import lu.base.iris.gui.AbstractSwingModule;
import lu.base.iris.gui.IWindow;
import lu.base.awt.WindowUtils;

/**
 * OutlookMainView description : main view of the application with outlook
 * presentation.
 */
public class OutlookMainView extends AbstractAppMainView implements IWindow {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2541985839642663242L;

	private static OutlookMainView _mainview = null;

	private IApplication _application;
	private OutlookBar _outlookbar = new OutlookBar();
	private JPanel _modulePanel = new JPanel(new BorderLayout());
	private JLabel _moduleLabel = new JLabel();
	private Component _current;
	protected JSplitPane _splitPanel = new JSplitPane();
	private StatusBar _statusbar = new StatusBar();

	/**
	 * OutlookMainView constructor: creates the main view.
	 * @param title 
	 */
	private OutlookMainView(String title) {
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

		JPanel panel = new JPanel(new BorderLayout());

		((BorderLayout) getLayout()).setVgap(5);
		_moduleLabel.setOpaque(true);
		_moduleLabel.setBackground(Color.gray);
		_moduleLabel.setForeground(Color.white);
		_moduleLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
		panel.add(_moduleLabel, BorderLayout.NORTH);
		panel.add(_modulePanel, BorderLayout.CENTER);

		_splitPanel.setRightComponent(panel);
		_splitPanel.setDividerLocation(110);
		_splitPanel.setLastDividerLocation(110);
		_splitPanel.setLeftComponent(_outlookbar);

		//javax.swing.JScrollPane mpane = new javax.swing.JScrollPane();
		//mpane.setViewportView(_splitPanel);

		JPanel mainpanel = new JPanel(new BorderLayout());
		mainpanel.add(_splitPanel, BorderLayout.CENTER);

		_statusbar.setText(getTitle());

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
	* Implements the control for item in Outlook bar.
	*/
	private class ItemControl extends JComponent {
		/**
		 * 
		 */
		private static final long serialVersionUID = 2426147939039161314L;
		public String _module = null;

		ItemControl(AbstractAction action, String module) {
			_module = module;
			this.setLayout(new BorderLayout());
			JButton button = new JButton(action);
			JLabel label = new JLabel(button.getText());
			JPanel panel = new JPanel();
			button.setText(null);
			/*if (button.getIcon() == null)
			  button.setIcon(CUtilities.loadGlobalImage(getMainForm(),
			    "icons/UnknownModule.gif"));*/
			label.setHorizontalAlignment(SwingConstants.CENTER);
			Font font = new Font(label.getFont().getName(), 0, 10);
			label.setFont(font);
			button.setMargin(new Insets(2, 2, 2, 2));
			panel.add(button);
			this.add(panel, BorderLayout.CENTER);
			this.add(label, BorderLayout.SOUTH);
		}
	}
	/**
	 * Implements a status bar
	 */
	private class StatusBar extends JTextField {
		/**
		 * 
		 */
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
	public static OutlookMainView getInstance(String title) {
		if (_mainview == null) {
			_mainview = new OutlookMainView(title);
		}
		return _mainview;
	}

	/**
	 * Method getInstance: retrieves the unique instance of the main view.
	 * @return OutlookMainView application main view
	 */
	public static OutlookMainView getInstance() {
		return getInstance("");
	}

	/**
	 * @see lu.base.iris.gui.AbstractAppMainView#putModule(lu.base.iris.gui.AbstractSwingModule)
	 */
	public void putModule(AbstractSwingModule module) {
		AbstractAction[] actions = module.getActions();

		for (int i = 0; i < actions.length; i++) {
			JPanel pane = (JPanel) _outlookbar.getPanel(module.getTitle());

			if (actions[i] != null) {
				actions[i].putValue(
					AbstractAction.SMALL_ICON,
					module.getIcon());
				ItemControl control =
					new ItemControl(actions[i], module.getTitle());
				if (pane == null) {
					pane = new JPanel();
					//pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
					pane.setLayout(
						new VerticalFlowLayout(
							FlowLayout.LEFT,
							5,
							5,
							true,
							false));
					_outlookbar.addPanel(module.getTitle(), pane);
				}
				pane.add(control);
			}
		}

		super.putModule(module);
	}

	/**
	 * Method show: changes the content the module panel (main pane) in the
	 * outlook presentation.
	 * @param title module label to display in the module title bar
	 * @param component module component to display in the module panel
	 */
	void show(String title, Component component) {
		if ((_current != null) && (_current instanceof IWindow)) {
			((OutlookWindowManager) OutlookWindowManager.getInstance()).onHide(
				(IWindow) _current);
		}

		_current = component;
		_moduleLabel.setText(title);
		_modulePanel.removeAll();
		_modulePanel.add(component);
		_modulePanel.revalidate();
		_modulePanel.repaint();
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