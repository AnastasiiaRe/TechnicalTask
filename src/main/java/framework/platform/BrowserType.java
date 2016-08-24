package framework.platform;

import framework.Settings;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumerable collection of available browser types which can be used for test execution.
 * <p>
 *     Used in {@link Settings} class for driver initialization.
 */
public enum BrowserType {
	FIREFOX,
	CHROME,
	SAFARI,
	IE,
	MOBILE_CHROME,
	MOBILE_SAFARI,
	FIREFOX_NO_GRID;

	private static Map<String, BrowserType> browsersMap = new HashMap<String, BrowserType>();

	/** Provides initialization of string keys for browser types. */
	static {
		browsersMap.put("mobilechrome", BrowserType.MOBILE_CHROME);
		browsersMap.put("mobilesafari", BrowserType.MOBILE_SAFARI);
		browsersMap.put("firefoxnogrid", BrowserType.FIREFOX_NO_GRID);
		browsersMap.put("firefox", BrowserType.FIREFOX);
		browsersMap.put("chrome", BrowserType.CHROME);
		browsersMap.put("safari", BrowserType.SAFARI);
		browsersMap.put("ie", BrowserType.IE);
	}

	/**
	 * Used for access to browser type through key value.
	 *
	 * @param name
	 * 			String parameter which stands for key value for specific browser type.
	 * @return	Browser type which corresponds to entered key value.
     */
	public static BrowserType Browser(String name) {
		BrowserType browserType = null;
		if (name != null) {
			browserType = browsersMap.get(name.toLowerCase().trim());
			if (browserType == null) {
				throw new UnknownBrowserException("Unknown browser [" + name + "]. Use one of following: "
						+ StringUtils.join(browsersMap.keySet().toArray(), ", "));
			}
		}

		return browserType;
	}

}
