package lu.base.iris.services;

import java.util.Locale;
import java.util.MissingResourceException;

import org.apache.log4j.Logger;

import lu.base.iris.AbstractService;
import lu.base.translation.BundleTranslator;
import lu.base.translation.Translator;

/**
 * TranslationService description : service implementation for translation
 * loading from a properties file.
 * 
 * @author xcapt
 * @author klinst
 */
public class TranslationService extends AbstractService 
{
  /**
   * The logger instance
   */
  private static Logger trace = Logger.getLogger(TranslationService.class);
  
  /**
   * The translator doing the translation work
   */
	private static Translator _trans;
  
  /**
   * The first instance of the service
   */
	private static TranslationService _first;

	/**
	 * Method TranslationService: initializes the translation service from a
	 * resource name loaded with BundleTranslator.
   * 
	 * @param conffile resource name for translation
	 */
	public TranslationService(String conffile) 
  {
		super();

		BundleTranslator.setProperties(conffile);
		if (_first == null) 
    {
			_first = this;
		}
	}

	/**
	 * Gets the translator instance.
   * 
	 * @return Translator
	 */
	private Translator getTranslation() 
  {
		if (_trans == null) 
    {
			_trans = Translator.getInstance();
		}
		return _trans;
	}

	/**
	 * sets the local and country from the configuration file 
   * for the translation with LOCALE_LANGUAGE and 
   * LOCALE_COUNTRY entries.
	 * 
	 */
	public boolean init() 
  {
		try 
    {
			ConfigurationService confservice =
				(ConfigurationService) getService(ConfigurationService
					.class
					.getName());
			
      // set locale
			String lang = confservice.getString("LOCALE_LANGUAGE");
			String country = confservice.getString("LOCALE_COUNTRY");
			set(lang, country);
		} 
    catch (MissingResourceException e) 
    {
      trace.error("Locale Resource missing!", e);
		} 
    catch (NullPointerException e) 
    {
			trace.error("Locale Null Pointer!", e);
		}

		return true;
	}

	/**
	 * Does nothing
	 */
	public void cleanup() 
  {
	}

	/**
	 * Method getTranslation: retrieves the translation for 
   * a resource key.
	 * 
   * @param key translation key
	 * @return String translated text or key if text not found
	 */
	public String getTranslation(String key) 
  {
		try 
    {
			return getTranslation().getTranslation(key);
		} 
    catch (MissingResourceException e) 
    {
			return key;
		}
	}

	/**
	 * Method getInstance: retrieves an instance of the 
   * TranslationService.
   * 
	 * @return TranslationService
	 */
	public static TranslationService getInstance() 
  {
		return _first;
	}

	/**
	 * Method set: updates the locale used for translation.
   * 
	 * @param language
	 * @param country
	 */
	public void set(String language, String country) 
  {
		getTranslation().updateLocale(new Locale(language, country));
	}
}