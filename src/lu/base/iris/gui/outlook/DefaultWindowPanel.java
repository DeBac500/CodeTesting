package lu.base.iris.gui.outlook;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import lu.base.iris.gui.IWindow;

/**
 * DefaultWindowPanel description : window implementation as a JPanel to be
 * included in the outlook main view.
 * 
 * @author xcapt
 */
public class DefaultWindowPanel extends JPanel implements IWindow
{
	/**
	 * The serial ID
	 */
	private static final long serialVersionUID = -5400581277650643588L;
	
	/**
	 * The title of the panel
	 */
	private String _title;
	
	/**
	 * DefaultWindowPanel constructor : creates a JPanel with a 
	 * border layout
	 */
	DefaultWindowPanel()
	{
		this.setLayout(new BorderLayout());		
	}
	
	/**
	 * does nothing, in outlook layout, only the main view manages the size
	 * @see lu.base.iris.gui.IWindow#maximize()
	 */
	public void maximize()
	{
	}
	
	/**
	 * does nothing, in outlook layout, only the main view manages the size
	 * @see lu.base.iris.gui.IWindow#minimize()
	 */
	public void minimize()
	{
	}
	
	/**
	 * does nothing, in outlook layout, only the main view manages the size
	 * @see lu.base.iris.gui.IWindow#setResizable(boolean)
	 */
	public void setResizable(boolean resizing)
	{
	}
	
	/**
	 * @see lu.base.iris.gui.IView#setTitle(java.lang.String)
	 */
	public void setTitle(String title)
	{
		_title = title;
	}
	
	/**
	 * Method getTitle: retrieves the current panel title.
	 * @return String panel title
	 */
	public String getTitle()
	{
		return _title;
	}
	
	/**
	 * shows the window panel in the Outlook main view.
	 */
	public void setVisible(boolean b)
	{
		OutlookMainView.getInstance()
			.show(getTitle(), this);
	}
	
	/**
	 * @see lu.base.iris.gui.IView#setStatus(java.lang.String)
	 */
	public void setStatus(String txt)
	{
		OutlookMainView.getInstance().setStatus(txt);
	}
}


