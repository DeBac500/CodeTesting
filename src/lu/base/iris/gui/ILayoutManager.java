package lu.base.iris.gui;

import java.awt.Window;

/**
 * ILayoutManager description : layout manager interface allows to manage
 * windows to be registered to a LayoutManager for position storing and
 * restoring in/from files for example.
 */
public interface ILayoutManager
{
	/**
	 * Method manage: called in the gui framework during the window creation.
	 * @param win Window component to be managed, properties are set during this
	 * method(size, position...)
	 */
	void manage(Window win);
}


