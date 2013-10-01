package lu.base.translation;

import java.util.ResourceBundle;
import java.util.Locale;

/**
 * Translator from ResourceBundle. Translates text
 * strings into different languages if the bundle
 * for that language exists in a property file.
 * 
 * @author xcapt
 * @author klinst
 */
public class BundleTranslator
	implements ITranslator
{
  /**
   * The locale specific resource bundle
   * containing the translation properties 
   */
	private ResourceBundle _bundle;
  
  /**
   * The singleton instance
   */
	private static BundleTranslator _translator;
  
  /**
   * The property file name referred to by the
   * bundle
   */
	private static String _properties = "translation";
	
  /**
	 * set properties file, should be set before object
	 * instantiation
   * 
   * @param propertiesname The name of the translater property file
   * @throws java.lang.NullPointerException
	 */
	public static void setProperties(String propertiesname)
	{
		if(propertiesname==null)
		{
			throw new NullPointerException("Invalid properties name : null");
		}
		_properties = propertiesname;
	}
	
  /**
	 * retrieve singleton object
   * @return The singleton instance
	 */
	public static BundleTranslator getInstance()
	{
		if(_translator==null)
		{
			_translator = new BundleTranslator();
		}
		return _translator;
	}
	
  /**
	 * update default Locale used by the JVM
   * @param locale The new Locale to be used
	 */
	public void updateLocale(Locale locale)
	{
		if(locale!=null)
		{
			if(!Locale.getDefault().equals(locale))
			{
				Locale.setDefault(locale);
				_bundle = null;
			}
		}
	}
	
  /**
	 * retrieve properties bundle
   * @return The bundle loaded from a property file 
	 */
	private ResourceBundle getBundle()
	{
		if(_bundle==null)
		{
			_bundle = ResourceBundle.getBundle(_properties);
		}
		return _bundle;
	}
	
  /**
	 * retrieve translation from bundle
   * @param keytxt The key for the text to be translated
   * @return The translation
	 */
	public String getTranslation(String keytxt)
	{
		return getBundle().getString(keytxt);
	}
	
	/**
	 * retrieve translation from bundle
   * @param keytxt The key for the text to be translated
   * @param defaulttxt If the translation cannot be found, use 
   * defaulttxt instead 
   * @return The translated text
	 */
	public String getTranslation(String keytxt, String defaulttxt)
	{
		try
		{
			return getBundle().getString(keytxt);
		}
		catch(java.util.MissingResourceException e)
		{
			return defaulttxt;
		}
	}
	
  /**
	 * used for dynamic object translation on Locale update
	 */
	public void add(TranslatedObject obj)
	{}
	/**
	 * Returns the default locale
   * @return Default Locale
	 */
	public Locale getCurrentLocale()
	{
		return Locale.getDefault();
	}
}

