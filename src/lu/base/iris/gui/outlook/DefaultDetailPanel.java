package lu.base.iris.gui.outlook;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import lu.base.iris.gui.IView;

/**
 * DefaultDetailPanel description : view implementation
 * 
 * @author xcapt
 * @author klinst
 */
public class DefaultDetailPanel extends JPanel implements IView
{
	/**
	 * The serial ID
	 */
	private static final long serialVersionUID = -1880576702605236595L;
	
	/**
	 * The parent window that is used
	 * for displaying this panel
	 */
  private DefaultDetailedWindow _window;

   /**
	 * Method DefaultDetailPanel.
	 * @param win
	 */
	DefaultDetailPanel(DefaultDetailedWindow win)
	{
		setLayout(new BorderLayout());
		_window = win;
	}

  /**
   * does nothing
	 * @see lu.base.iris.gui.IView#setTitle(java.lang.String)
	 */
	public void setTitle(String title)
  {}

	/**
	 * shows this panel in the detailed lower
	 * pane of the Default Detailed Window
	 */
  public void setVisible(boolean b)
  {
    _window.setDetailComponent(this);
  }
    
  /**
   * Sets the status text.
   * @param txt The status text
   */
  public void setStatus(String txt)
	{
		_window.setStatus(txt);
	}
}