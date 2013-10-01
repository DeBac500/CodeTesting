package lu.base.iris.gui;


import java.util.Iterator;

import lu.base.iris.AbstractApplication;
import lu.base.iris.IModule;

/**
 * AbstractGUIApplication description : base implementation for the Application
 * object of an application with a graphical user interface. It holds a
 * reference to an AbstractAppMainView object to display the graphical user
 * interface and manages AbstractSwingModule to create menus.
 */
public abstract class AbstractGUIApplication extends AbstractApplication {

    private AbstractAppMainView _mainview;
    private boolean _winmenu = false;

    public AbstractGUIApplication()
    {}
    
    /**
	 * Method setMainView: sets main view object and registers the application
	 * to the mainview.
	 * @param view main view object
	 */
	public void setMainView(AbstractAppMainView view) {
        view.init();
        _mainview = view;
        _mainview.setApplication(this);
    }

    /**
	 * Method getMainView: retrieve application main view.
	 * @return AbstractAppMainView
	 */
	public AbstractAppMainView getMainView() {
        return _mainview;
    }

	/**
	 * calls init() for all modules and registers AbstractSwingModule to the
	 * main view.
	 * @see lu.base.iris.AbstractApplication#initModules()
	 */
	public boolean initModules()
	{
		boolean result = super.initModules();
		for(Iterator it=_modcontainer.iterator();it.hasNext();)
		{
			IModule mod = (IModule)it.next();
			if(mod instanceof AbstractSwingModule)
			{
				getMainView().putModule((AbstractSwingModule)mod);
			}
		}
		return result;
	}

    /**
	 * Method show: shows the main view of the application.
	 */
	public void show() {
    	
        if (!_winmenu) {
            //getMainView().addWindowMenu();
            _winmenu = true;
        }

        getMainView().pack();
        getMainView().setVisible(true);
    }
}