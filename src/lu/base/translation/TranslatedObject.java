package lu.base.translation;

import java.beans.PropertyChangeListener;
/**
 * Basic object that is translated.
 * on Local change, the object registered to a TranslationSupport
 * will be notified a propertyChange(PropertyChangeEvent)
 * with the property name : TranslatedObject.PROPERTY_LANGUAGE_CHANGE
 * <br>
 * Creation date: (15/11/01 10:42:41)
 * @author: Yoann Jagoury
 */
public interface TranslatedObject extends PropertyChangeListener
{
	public static final String PROPERTY_LANGUAGE_CHANGE = "language change";
}
