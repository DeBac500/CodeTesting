package lu.base.iris.gui.mdi;


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import lu.base.iris.gui.AbstractAppMainView;
import lu.base.iris.gui.IWindow;
import lu.base.awt.WindowUtils;

/**
 * ToolBarMainView description : simple FDI implementation of the application
 * main view with a simple toolbar as main view.
 */
public class ToolBarMainView extends AbstractAppMainView 
	implements IWindow
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5644671303872565144L;
	private static ToolBarMainView _mainview;
	
    public static ToolBarMainView getInstance()
    {
    	if(_mainview==null)
    	{
    		_mainview = new ToolBarMainView("VIGIL - Olyscript");
    	}
    	return _mainview;
    }
    
	private ToolBarMainView(String title) {
		super(title);
		//Create the menu bar.
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
	}


	public void init() {


		WindowUtils.centerFrame(this);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
	}

	public void start() {
		setVisible(true);
	}


	public void close() {
		_application.stop();
	}
	
    public void maximize()
    {}
    public void minimize()
    {}
    
    public void setStatus(String txt)
	{}
}