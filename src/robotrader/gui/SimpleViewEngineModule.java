package robotrader.gui;

import javax.swing.AbstractAction;


import lu.base.iris.gui.DefaultViewComponent;
import lu.base.iris.gui.simple.SimpleWindowManager;

/**
 * An Engine Model made to work more specifically with a SimpleWindowManager
 * @author dave
 *
 */
public class SimpleViewEngineModule extends EngineModule {
	public AbstractAction[] getActions(){
		if (_actions == null){
			_actions = new AbstractAction[] {new ViewAction()};
		}

		return _actions;
}
	public class ViewAction extends EngineModule.ViewAction
	{
		private static final long serialVersionUID = 2421757600044635508L;

		public void execute(){
			DefaultViewComponent c =
				new DefaultViewComponent(getMainFrame().getPanel(), "ENGINE_VIEW");
			SimpleWindowManager swm = (SimpleWindowManager)SimpleWindowManager.getInstance();
			swm.setMainPanel(c);
		}	
	}
}
