package lu.base.iris.gui.simple;


import javax.swing.JFrame;

import lu.base.awt.WindowUtils;
import lu.base.iris.gui.IWindow;


/**
 * DefaultFrameWindow description : default implementation of an IWindow as a
 * simple JFrame
 */
public class DefaultFrameWindow extends JFrame implements IWindow
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3732377659610035989L;
	/**
	 * default implementation does nothing
	 * @see lu.base.iris.gui.IWindow#maximize()
	 */
	public void maximize()
	{
	};
	/**
	 * default implementation does nothing
	 * @see lu.base.iris.gui.IWindow#minimize()
	 */
	public void minimize()
	{
	};
	/**
	 * packs, centers and shows the frame
	 * @see IWindow#setVisible()
	 */
	public void setVisible(boolean b)
	{
		this.pack();
		WindowUtils.centerFrame(this);
		super.setVisible(b);
	}
	/**
	 * default implementation does nothing
	 * @see lu.base.iris.gui.IView#setStatus(java.lang.String)
	 */
	public void setStatus(String txt)
	{}
	
}


