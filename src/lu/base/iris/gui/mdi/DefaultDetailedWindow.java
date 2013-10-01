package lu.base.iris.gui.mdi;

import lu.base.iris.gui.IDetailedWindow;
import lu.base.iris.gui.IView;
import lu.base.iris.gui.IWindow;

/**
 * DefaultDetailedWindow description :XXX
 */
public class DefaultDetailedWindow extends DefaultFrameWindow
	implements IDetailedWindow
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 463108699757914430L;
	private IView _details;
	
	void setDetails(IView details)
	{
		_details = details;
	}
    /**
     * @see IDetailedWindow#getDetails()
     */
    public IView getDetails()
    {
        return _details;
    }


    /**
     * @see IWindow#minimize()
     */
    public void minimize()
    {
    	super.minimize();
    }


    /**
     * @see IWindow#maximize()
     */
    public void maximize()
    {
    	super.maximize();
    }


    /**
     * @see IWindow#setSize(int, int)
     */
    public void setSize(int w, int h)
    {
    	super.setSize(w,h);
    }


    /**
     * @see IWindow#setResizable(boolean)
     */
    public void setResizable(boolean resizing)
    {
    	super.setResizable(resizing);
    }


    /**
     * @see IView#setVisible()
     */
    public void setVisible(boolean b)
    {
		super.setVisible(b);
    }


    /**
     * @see IView#hide()
     */
    public void hide()
    {
    	super.setVisible(false);
    }


}


