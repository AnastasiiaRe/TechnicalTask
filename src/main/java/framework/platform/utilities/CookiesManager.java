package framework.platform.utilities;

import framework.adapters.WebDriverManager;
import framework.platform.CookieName;
import org.openqa.selenium.Cookie;

/**
 * Utilitarian class which provides methods that helps to work with cookies.
 */
public class CookiesManager {

	/** Checks whether given cookie file is empty or not. */
	public static boolean isCookieEmpty(CookieName cookie) {
		Cookie cookieValue = WebDriverManager.getDriver().manage().getCookieNamed(cookie.toString());
		String cookie_val = cookieValue.getValue();
		return cookie_val.isEmpty();
	}

	/** Get and return contains of given cookie file. */
	public static String getCookieValue(CookieName cookie) {
		Cookie cookieValue = WebDriverManager.getDriver().manage().getCookieNamed(cookie.toString());
		String cookie_val = cookieValue.getValue();
		return cookie_val;

	}

	/** Sets cookie with specified name and value which it contains. */
	public static void setCookieValue(CookieName cookie, String value) {
		Cookie ck = new Cookie(cookie.toString(), value);
		WebDriverManager.getDriver().manage().addCookie(ck);
	}

	/** Checks whether given cookie is present or not. */
	public static boolean isCookiePresent(CookieName cookie) {
		Cookie nullCookie = new Cookie(cookie.toString(), null);
		return WebDriverManager.getDriver().manage().getCookies().contains(nullCookie.equals(null));
	}

	/** Deletes all cookies. */
	public static void deleteAllCookies() {
		WebDriverManager.getDriver().manage().deleteAllCookies();
	}

}
