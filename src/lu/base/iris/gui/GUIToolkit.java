package lu.base.iris.gui;

import javax.swing.JFrame;

/**
 * GUIToolkit description : static helper class holding reference to objects
 * that will be unique for an application.
 * Holds references to: a main Frame, a LayoutManager (optional)
 */
public class GUIToolkit 
{
  /**
   * The main frame of the application
   */
  private static JFrame _mainwnd;
  
  /**
   * The layout manager for registering and
   * positioning of windows
   */
  private static ILayoutManager _layoutmgr;
    
	/**
	 * Method setMainFrame: called in GUI framework when the main frame is
	 * created.
   * 
	 * @param mainwnd The main window of the application
	 */
	public static void setMainFrame(JFrame mainwnd)
  {
   	_mainwnd = mainwnd;
  }
  
    /**
	 * Method getMainFrame: retrieve main frame of the application.
	 * @return Frame retrieve main frame
	 */
	public static JFrame getMainFrame()
  {
   	return _mainwnd;
  }
    
    /**
	 * Method setLayoutManager: called in GUI framework when the LayoutManager
	 * is created.
	 * @param mgr The layout manager
	 */
	public static void setLayoutManager(ILayoutManager mgr)
  {
  	_layoutmgr = mgr;
  }
    
    /**
	 * Method getLayoutManager: retrieve layout manager of the application.
	 * @return ILayoutManager The Layout manager
	 */
	public static ILayoutManager getLayoutManager()
  {
    	return _layoutmgr;
  }
}


