package lu.base.translation;

import java.util.Locale;
import java.util.MissingResourceException;

/**
 * single access point for translation and retrieve 
 * TranslationSupport.
 * 
 * @author xcapt
 * @author klinst
 */
public class Translator implements ITranslator
{
  /**
   * The singleton translater generated
   * by the translator manager
   */
	static ITranslator _trans;
  
  /**
   * The singleton object of this class
   */
	static Translator _singleton;
	
  /**
   * Gets the singleton translator object from
   * the translater manager
   * 
   * @return The generated translater object
   */
	private static final ITranslator getTranslator()
	{
		if(_trans==null)
		{
			_trans = TranslatorManager.getTranslator();
		}
		return _trans; 
	}
	
  /**
	 * to force getInstance() usage
	 */
	private Translator()
	{}
	
  /**
	 * retrieve unique Translation object (singleton pattern)
   * @return The translator object
	 */
	public static Translator getInstance()
	{
		if(_singleton==null)
		{
			_singleton = new Translator();
		}
		return _singleton;
	}
	
	/**
	 * get translation for the given key text
	 * 
   * @param text The key for identifying the text
   * @return The translation string
	 * @throws java.util.MissingResourceException eventually 
	 * when key not found in properties.
	 */
	public String getTranslation(String text)
	{
		try
		{
			return getTranslator().getTranslation(text);
		}
		catch(MissingResourceException e)
		{
			return text;
		}
	};
	/**
	 * get translation for the given key text, when the key
	 * text is not found, the default text is sent as result
   * 
   * @param text The key for identifying the text
   * @param defaultValue The default translation string
   * @return The translation string
   * @throws java.util.MissingResourceException eventually 
   * when key not found in properties.
	 */
	public String getTranslation(String text, String defaultValue)
	{
		try
		{
			return getTranslator().getTranslation(text, defaultValue);
		}
		catch(MissingResourceException e)
		{
			return defaultValue;
		}
	}
  
	/**
	 * retrieve Locale used currently for translation
   * @return The current Locale
	 */
	public Locale getCurrentLocale()
	{
		return getTranslator().getCurrentLocale();
	}
  
	/**
	 * set the Locale used currently for translation
   * @param locale The locale to be set
	 */
	public void updateLocale(Locale locale)
	{
		getTranslator().updateLocale(locale);
	}
  
	/**
	 * register an object for automatic translation on Locale
	 * change (currently not in use)
   * @param object to be added
	 */
	public void add(TranslatedObject object)
	{
		getTranslator().add(object);
	}
}

