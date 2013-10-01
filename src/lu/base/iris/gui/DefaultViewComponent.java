package lu.base.iris.gui;

import java.awt.Component;

import javax.swing.JMenu;
import javax.swing.JToolBar;

import lu.base.iris.services.TranslationService;

/**
 * DefaultViewComponent: A visual awt component with
 * a translated title
 * 
 * @author xcapt
 * @author klinst
 */
public class DefaultViewComponent implements IViewComponent 
{
  /**
   * The view component
   */
	private Component _component;
  
  /**
   * The translated title
   */
	private String _title;

  /**
   * Creates a new default view component.
   * 
   * @param comp The component
   * @param title The title (will be translated by
   * the Translation Service)
   */
	public DefaultViewComponent(Component comp, String title)
	{
		_component = comp;
		_title = TranslationService.getInstance().getTranslation(title);
	}
	
	/**
	 * Returns the component
   * 
   * @return The component
   * @see lu.base.iris.gui.IViewComponent#getComponent()
	 */
	public Component getComponent() 
  {
		return _component;
	}

	/**
   * Gets the translated title.
   * 
   * @return The title of the component
	 * @see lu.base.iris.gui.IViewComponent#getTitle()
	 */
	public String getTitle() 
  {
		return _title;
	}

	/**
   * @return null
	 * @see lu.base.iris.gui.IViewComponent#getMenus()
	 */
	public JMenu[] getMenus() 
  {
		return null;
	}

	/** 
   * @return null
	 * @see lu.base.iris.gui.IViewComponent#getToolBar()
	 */
	public JToolBar getToolBar() 
  {
		return null;
	}

	/** 
   * does nothing
	 * @see lu.base.iris.gui.IViewComponent#onEvent(lu.base.iris.gui.IViewComponentEvent)
	 */
	public void onEvent(IViewComponentEvent evt) 
  {
	}

}
