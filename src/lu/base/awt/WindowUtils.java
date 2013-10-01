package lu.base.awt;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
/**
 * Tools to manipulate java.awt.Window
 * <br>
 * Creation date: (09/11/01 12:06:05)
 * @author: Yoann Jagoury
 */
public class WindowUtils
{
    /**
    * Centers in the screen the Window given has parameter 
    * @param Window myFrame window to center
    */
    public static final void centerFrame(Window myFrame)
    {
        Dimension frameSize = myFrame.getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int x = screenSize.width / 2 - frameSize.width / 2;
        int y = screenSize.height / 2 - frameSize.height / 2;

        myFrame.setLocation((x > 0) ? x : 0, (y > 0) ? y : 0);
    }
}