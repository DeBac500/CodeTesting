package lu.base.iris.gui.mdi;


import java.awt.Dialog;
import java.awt.Frame;

import lu.base.awt.WindowUtils;
import lu.base.iris.gui.IDialog;
import lu.base.swing.dialog.StandardDialog;
import lu.base.translation.Translator;


/**
 * DefaultStandardDialog description : simple implementation of the Idialog
 * interface, as a ch.eri.jutils.swing.dialog.StandardDialog
 */
public class DefaultStandardDialog extends StandardDialog implements IDialog
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9160739193450062715L;
	public static Translator _trans = Translator.getInstance(); 
	
	public DefaultStandardDialog()
	{
	};
	
	public DefaultStandardDialog(Frame parent, String title)
	{
		super(parent,_trans.getTranslation(title));
	}
	
	public DefaultStandardDialog(Dialog parent, String title)
	{
		super(parent,_trans.getTranslation(title));
	}
	
	protected String getOkTitle()
	{
		return _trans.getTranslation("COMMON_DLG_OK");
	}
	protected String getCancelTitle()
	{
		return _trans.getTranslation("COMMON_DLG_CANCEL");
	}
	public void setVisible(boolean b)
	{
		this.pack();
		WindowUtils.centerFrame(this);
		super.setVisible(b);
	}
	public void setTitle(String title)
	{
		super.setTitle(_trans.getTranslation(title));
	}
	
	public void setStatus(String txt)
	{}
}


