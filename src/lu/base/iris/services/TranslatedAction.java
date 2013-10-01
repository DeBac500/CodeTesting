package lu.base.iris.services;

import lu.base.command.ExecutableAction;

/**
 * TranslatedAction description : base class of ExecutableAction implementing
 * the translation of the action name.
 * 
 * @author xcapt
 * @author klinst
 */
public abstract class TranslatedAction extends ExecutableAction 
{	
  /**
   * The service for loading the translation from
   * property files
   */
	private static TranslationService _trans = TranslationService.getInstance();
	
	/**
	 * Creates an ExecutableAction with a translated name using the standard
	 * translation service.
   * 
	 * @see javax.swing.AbstractAction#AbstractAction(java.lang.String)
	 */
	public TranslatedAction(String name) 
	{
		super(_trans.getTranslation(name),null);
	}
}