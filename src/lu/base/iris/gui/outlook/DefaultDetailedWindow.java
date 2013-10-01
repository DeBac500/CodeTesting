package lu.base.iris.gui.outlook;


import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import lu.base.iris.gui.IDetailedWindow;
import lu.base.iris.gui.IView;


/**
 * DefaultDetailedWindow description : window implementation extending the
 * DefaultWindowPanel to manage two panes, an upper mainpane and a lower detail
 * pane. This can be used to present a navigator or overview pane as upper pane
 * and the details of the selected item in the lower pane.
 * 
 * @author xcapt
 * @author klinst
 */
public class DefaultDetailedWindow
    extends DefaultWindowPanel
    implements IDetailedWindow
{
	/**
   * The serial id 
   */
  private static final long serialVersionUID = 401752221774607607L;

  /**
   * The divider location 
   */
  private static final double DEFAULT_DIVIDER_LOCATION = 0.55d;
	
  /**
   * The detailed view
   */
	private IView _details;
  
  /**
   * The split pane dividing the main and
   * detail pane
   */
  private JSplitPane _splitpane;
  
  /**
   * The main pane
   */
  private JPanel _mainpane = new JPanel(new BorderLayout());
  
  /**
   * The detail pane
   */
  private JPanel _detailpane = new JPanel(new BorderLayout());


  /**
   * DefaultDetailedWindow constructor: creates the window
   */
  DefaultDetailedWindow()
  {
    init();
  }

  /**
   * Method init: initializes the pane components.
   */
	private void init()
  {
    _splitpane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, _mainpane, _detailpane);
    _splitpane.setDividerLocation(DEFAULT_DIVIDER_LOCATION);
    this.add(_splitpane);        
  }

  /**
   * Method setDetails: stores the view containing the details.
   * @param details The view with the details
   */
  void setDetails(IView details)
  {
    _details = details;
  }
  
  /**
  * Method clearDetails: clears the details of the view.
  */
	void clearDetails()
	{
		_detailpane.removeAll();
		_splitpane.setDividerLocation(DEFAULT_DIVIDER_LOCATION);
		_detailpane.revalidate();
		_detailpane.repaint();
	}
    
  /**
   * Gets the detailed view
	 * @see lu.base.iris.gui.IDetailedWindow#getDetails()
	 */
	public IView getDetails()
  {
    return _details;
  }

  /**
	 * Method setMainComponent: displays the component in 
   * the main upper pane.
	 * @param c component to display
	 */
	void setMainComponent(Component c)
  {
    _mainpane.removeAll();
    _mainpane.add(c);
    _splitpane.setDividerLocation(DEFAULT_DIVIDER_LOCATION);
    _mainpane.repaint();
  }

  /**
   * Method setDetailComponent: displays the component in 
   * the detail lower pane.
   * @param c component to display
   */
  void setDetailComponent(Component c)
  {     
    _detailpane.removeAll();
    _detailpane.add(c);
    _splitpane.setDividerLocation(DEFAULT_DIVIDER_LOCATION);
    _detailpane.revalidate();
    _detailpane.repaint();
  }
}