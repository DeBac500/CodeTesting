package lu.base.iris.gui;


import java.util.Collection;
import java.util.Collections;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import lu.base.iris.IModule;

/**
 * AbstractSwingModule description : base implementation of an IModule with
 * Swing interface. A swing module as a title, an icon, and allowed user actions
 * that may be called from the user interface.
 */
public abstract class AbstractSwingModule implements IModule {
    private String _name;
    private String _title;
	private String _ref;


    /**
     *  AbstractSwingModule constructor.
	 * @param ref unique module reference, used to retrieve translation of
	 * module name.
	 */
	public AbstractSwingModule(String ref) {
		_ref = ref;
        _title = ref;
    }
	/**
	 * Method setTitle.
	 * @param title
	 */
	public void setTitle(String title)
	{
		_title = title;
	}


    /**
     * returns the class name
	 * @see lu.base.iris.IModule#getName()
	 */
	public String getName() {
		if(_name==null)
		{
			_name = getClass().getName();
		}
        return _name;
    }
    
    /**
	 * Method getRef: retrieve module reference.
	 * @return String
	 */
	public String getRef()
    {
    	return _ref;
    }

    /**
	 * Method getActions: retrieve user allowed actions.
	 * @return AbstractAction[]
	 */
	public abstract AbstractAction[] getActions();
    
    /**
	 * Method getTitle: retrieve the module title.
	 * @return String
	 */
	public String getTitle()
    {
    	return _title;
    }   


    /**
	 * @see lu.base.iris.IModule#getServices()
	 */
	public Collection getServices() {
        return Collections.EMPTY_LIST;
    }
    
    /**
	 * Method getIcon: retrieve module icone.
	 * @return ImageIcon
	 */
	public abstract ImageIcon getIcon();
	
    
	public void cleanup()
	{};
}