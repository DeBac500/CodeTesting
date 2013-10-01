package lu.base.iris.gui;

import java.awt.Rectangle;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Iterator;

import lu.base.iris.AbstractService;
import lu.base.iris.services.UserPrefService;

import org.apache.log4j.Logger;

/**
 * UserLayoutService description : service implementation 
 * storing and restoring windows layout to/from a preference 
 * file.
 * 
 * @author xcapt
 * @author klinst
 */
public class UserLayoutService extends AbstractService
	implements ILayoutManager 
{
  /**
   * The preference service for serialising layout
   */
	private UserPrefService _pref;
  
  /**
   * A list of windows
   */
	private ArrayList _frames = new ArrayList();

	/**
	 * 
	 */
	public boolean init() 
  {
		GUIToolkit.setLayoutManager(this);
    
		// get user pref service
		_pref = (UserPrefService) getService(UserPrefService.class);

		if (_pref == null) 
    {
			return false;
		}

		return true;
	}

	/**
	 * stores the windows positions
	 * 
	 */
	public void cleanup() 
  {
		storeFramesPos();
	}

	/**
	 * registers the window for management and tries to 
   * retrieve the position from the preference file.
	 * @param win The window to manage
	 */
	public void manage(Window win) 
  {
		_frames.add(win);
		Rectangle rec = getFramePosition(win);
		win.setBounds(rec);
		win.validate();
	}

	/**
	 * Method getKey: get key to store the window preferences.
	 * @param win The window
	 * @return String The window class name
	 */
	private String getKey(Window win) 
  {
		return win.getClass().getName();
	}

	/**
	 * Method getFramePosition: retrieves the window size and position stored
	 * in the user preference file.
	 * @param win window
	 * @return Rectangle position and size
	 */
	private Rectangle getFramePosition(Window win){
		String key = getKey(win);
		String x,y,w,h;

		x = _pref.getStringValue(key + ".x");
		y = _pref.getStringValue(key + ".y");
		w = _pref.getStringValue(key + ".w");
		h = _pref.getStringValue(key + ".h");

		if(x == null || y == null || w == null || h == null){
			//If the old values could not be found, use default values.
			Logger.getLogger(this.getClass().getName()).debug("No values in user.pref for " + key + ", using default values");
			x = "50";
			y = "50";
			w = "500";
			h = "500";
		}

		return new Rectangle(
			Integer.parseInt(x),
			Integer.parseInt(y),
			Integer.parseInt(w),
			Integer.parseInt(h));
	}

	/**
	 * Method storeFramesPos: stores all windows positions.
	 */
	private void storeFramesPos() 
	{
		Window win = null;
		Rectangle rec = null;
		String key = null;

		for (Iterator it = _frames.iterator(); it.hasNext();) {
			win = (Window) it.next();
			rec = win.getBounds();
			key = getKey(win);
			_pref.setStringValue(key + ".x", Integer.toString(rec.x));
			_pref.setStringValue(key + ".y", Integer.toString(rec.y));
			_pref.setStringValue(key + ".w", Integer.toString(rec.width));
			_pref.setStringValue(key + ".h", Integer.toString(rec.height));
		}
	}
}