package robotrader.market;

import java.util.HashMap;

/**
 * default IIndicatorContainer implementation using a simple
 * HashMap
 */
public class DefaultIndicatorContainer implements IIndicatorContainer {
	private HashMap _indicatormap = new HashMap();

	/**
	 * @see robotrader.market.IIndicatorContainer#register(String,
	 * IIndicator)
	 */
	public void register(String key, IIndicator indicator) {
		_indicatormap.put(key, indicator);
	}

	/**
	 * @see robotrader.market.IIndicatorContainer#clear()
	 */
	public void clear() {
		_indicatormap.clear();
	}

	/**
	 * @see robotrader.market.IIndicatorContainer#get(String)
	 */
	public IIndicator get(String key) {
		return (IIndicator) _indicatormap.get(key);
	}
}
