package lu.base.iris.gui.simple;


import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import lu.base.iris.gui.*;
import lu.base.iris.gui.mdi.DefaultDetailedWindow;
import lu.base.iris.gui.mdi.DefaultStandardDialog;
import lu.base.iris.services.*;


/**
 * default implementation of the IWindowManager interface 
 * that is an FDI (floating document interface) where all 
 * windows of the application have a separated window and
 * menu bar.
 */
public class SimpleWindowManager implements IWindowManager
{
	protected static TranslationService translator = TranslationService.getInstance();
	
	protected static IWindowManager _winmgr;
	
	/**
	 * protected constructor to force the use of getInstance()
	 * to have only one instance of the object running (singleton)
	 */
	protected SimpleWindowManager()
	{}
	/**
	 * retrieve an instance of the IWindowManager, there is 
	 * always only one instance created and its is a 
	 * DefaultWindowManager object that is created.
	 */
	public static IWindowManager getInstance()
	{
		if(_winmgr==null)
		{
			_winmgr = new SimpleWindowManager();
		}
		return _winmgr;
	}
	/**
	 * create an IDialog with no parent IView
	 */
	public IDialog makeDialog(IViewComponent component)
	{
		return makeDialog(component, null);
	};
	/**
	 * create an IDialog with a parent IView
	 */
	public IDialog makeDialog(IViewComponent component, IView parent)
	{
		DefaultStandardDialog dlg;
		
		if(parent==null)
		{
			dlg = new DefaultStandardDialog();
			dlg.setTitle(component.getTitle());
		}
		else if(parent instanceof Frame)
		{
			dlg = new DefaultStandardDialog((Frame)parent, component.getTitle());
		}
		else if(parent instanceof Dialog)
		{
			dlg = new DefaultStandardDialog((Dialog)parent, component.getTitle());
		}
		else
		{
			dlg = new DefaultStandardDialog();
			dlg.setTitle(component.getTitle());
		}
		
		Component c = component.getComponent();
		if(c instanceof Container)
		{
			dlg.setContentPane((Container)c);
		}
		else
		{
			JPanel panel = new JPanel();
			panel.add(c);
			dlg.setContentPane(panel);
		}
		
		return dlg;
	};
	
	public IWindow makeWindow(IViewComponent component)
	{
		
		DefaultFrameWindow frame = new DefaultFrameWindow();
		frame.setTitle(component.getTitle());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);


		Component c = component.getComponent();
		if(c instanceof Container)
		{
			frame.setContentPane((Container)c);
		}
		else
		{
			JPanel panel = new JPanel();
			panel.add(c);
			frame.setContentPane(panel);
		}
		
		JMenuBar menu = new JMenuBar();
		
		JMenu[] menus = component.getMenus();
		
		if(menus != null){
			for(int i=0;i<menus.length;i++){
				menu.add(menus[i]);
			}
		}
		
		if(menu!=null)
		{
			frame.setJMenuBar(menu);
		}
		
		GUIToolkit.getLayoutManager().manage(frame);

		return frame;
	};
	/**
	 * retrieve main view
	 */
	public IWindow getMainView()
	{
		return SimpleMainView.getInstance();
	}
	
	public void setMainPanel(IViewComponent c){
		SimpleMainView smv = SimpleMainView.getInstance();
		smv.show(c.getTitle(), c.getComponent());
	}
	/**
	 * creates a window with a linked detail window
	 */
	public IDetailedWindow makeDetailedWindow(IViewComponent component)
	{
	    DefaultDetailedWindow win = new DefaultDetailedWindow();
	    win.setTitle(component.getTitle());
		win.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		Component c = component.getComponent();
		if(c instanceof Container)
		{
			win.setContentPane((Container)c);
		}
		else
		{
			JPanel panel = new JPanel();
			panel.add(c);
			win.setContentPane(panel);
		}
		
		return win;
	};
	/**
	 * update detail window detail content
	 */
	public void updateDetailedWindow(IDetailedWindow win, IViewComponent details)
	{
		//((DefaultDetailedWindow)win)
		//	.setDetails(makeDialog(details, win));		
	};
	
	/**
	 * <p>Displays the specified component in the main window,
	 * as opposed to a new window.</p>
	 * @param c The component to display.
	 */
	public void displayInMainView(Component c){
		SimpleMainView smv = SimpleMainView.getInstance();
		if(smv != null){
			smv.show("",c);
		}
	}
	
}


