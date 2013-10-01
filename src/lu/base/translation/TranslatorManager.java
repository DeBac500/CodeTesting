package lu.base.translation;
/**
 * Factory for Translator. Creates a translator instance
 * of the translater class.
 * 
 * @author xcapt
 * @author klinst
 */
public class TranslatorManager
{
  /**
   * The default class of the Translator object
   */
	static Class _translatorClass = BundleTranslator.class;
	
	/**
	 * no accessible constructor
	 */
	private TranslatorManager()
	{}
	
	/**
	 * Method setTranslatorClass: set default translator class to create when
	 * using getInstance().
	 * @param c class implementing ITranslator
	 */
	public static void setTranslatorClass(Class c)
	{
		_translatorClass = c;
	}
  
	/**
	 * Method getTranslator: get the singleton instance of translator.
	 * @return ITranslator The instantiated translater class 
   * @throws java.lang.RuntimeException If the class could not be 
   * instantiated
	 */
	public static ITranslator getTranslator()
	{
		if(_translatorClass!=null)
		{
			try
			{
				Object o = _translatorClass.newInstance();
				return (ITranslator)o;
			}
			catch(Exception e)
			{
				throw new RuntimeException("could not create Translator : "+_translatorClass.getName());
			}
		}
		return new BundleTranslator();
	}
}

