package lu.base.command;

import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.Cursor;

import javax.swing.AbstractAction;
import javax.swing.Icon;
/**
 * Abstract implementation of the Command pattern on the basis of Swing's
 * AbstractAction.
 * This encapsulate the call to execute() on concrete implementations of
 * this class whenever an action is performed.
 * Additionally it provides automatic wait cursor setting.
 *
 * <br>
 * @author: Yoann Jagoury
 */
public abstract class ExecutableAction extends AbstractAction
	implements IExecutable
{
/**
 * ExecutableAction constructor comment.
 */
public ExecutableAction(String name) {
	super(name);
}
/**
 * ExecutableAction constructor comment.
 */
public ExecutableAction(String name, Icon icon) {
	super(name, icon);
}
/**
 *  actionPerformed is called whenever the action is acted upon.
 *	@param e the action event
 */
public void actionPerformed(ActionEvent e) 
{
	Object o = e.getSource();
	Component c = null;
	if((o!=null)&&(o instanceof Component))
	{
		c = (Component)o;
	}
	
	if (c!=null) 
	{
		Cursor cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
		c.setCursor(cursor);
	}

	execute();

	if (c!=null) 
	{
		Cursor cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
		c.setCursor(cursor);
	}
}
}
