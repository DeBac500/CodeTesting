package robotrader.gui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import lu.base.iris.gui.BaseGUIModule;
import lu.base.iris.gui.DefaultViewComponent;

public class OptionsModule extends BaseGUIModule{

	boolean _hideChartsDuringSim = false;
	
	private AbstractAction[] _actions;
	
	public OptionsModule()
	{
		super("Options");
	}
	
	public AbstractAction[] getActions(){
		if(_actions == null){
			_actions = new AbstractAction[]{
								new EditOptionsAction()
						};
		}
		return _actions;
	}
	
	

	class EditOptionsAction extends AbstractAction{
		private static final long serialVersionUID = -265813061897967133L;

		public EditOptionsAction(){
			super("Edit Options");
		}
		
		public void actionPerformed(ActionEvent ae) 
		{		
			DefaultViewComponent c =
				new DefaultViewComponent(new OptionsPanel(), "Edit Options");
      		getWindowManager().makeWindow(c).setVisible(true);
		}
	}
}
