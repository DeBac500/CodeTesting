package robotrader.market;

/**
 * Container for IIndicator objects.
 */
public interface IIndicatorContainer {
	/**
	 * method to register indicators with a key in a map
	 */
	void register(String key, IIndicator indicator);
	/**
	 * clear all registered indicators
	 */
	void clear();
	/**
	 * retrieve IIndicator
	 */
	IIndicator get(String key);
}
