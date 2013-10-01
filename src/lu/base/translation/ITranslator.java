package lu.base.translation;

import java.util.Locale;
/**
 * interface to implement for a Translator.
 * <br>
 * Creation date: (20/12/01 08:32:27)
 * @author: Administrator
 */
public interface ITranslator 
{
	/**
	 * get translation for the given key text
	 * 
	 * @throws java.util.MissingResourceException eventually 
	 * when key not found in properties.
	 */
	public String getTranslation(String text);
	/**
	 * get translation for the given key text, when the key
	 * text is not found, the default text is sent as result
	 */
	public String getTranslation(String text, String defaultValue);
	/**
	 * retrieve Locale used currently for translation
	 */
	public Locale getCurrentLocale();
	/**
	 * set the Locale used currently for translation
	 */
	public void updateLocale(Locale locale);
	/**
	 * register an object for automatic translation on Locale
	 * change
	 */
	public void add(TranslatedObject object);
}
