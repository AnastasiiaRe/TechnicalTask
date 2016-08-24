package framework.platform.utilities;

import framework.Logger;
import framework.adapters.WebDriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;

/**
 * Utilitarian class which contains various methods for executing JavaScript's and performing custom {@link Actions}. TODO
 */
public class Utils {

	/** Method which will execute given javascript. */
	public static void executeJS(String jsScript){
		JavascriptExecutor js = (JavascriptExecutor) WebDriverManager.getDriver();
		js.executeScript(jsScript);
	}

	/** Method which will execute given javascript and return it's execution result as string. */
	public static String getJSResult(String jsScript){
		JavascriptExecutor js = (JavascriptExecutor) WebDriverManager.getDriver();
		return js.executeScript(jsScript).toString();
	}

	/**
	 *  TODO Clarification.
	 */
	public static void scrollPage(Integer y) {
		executeJS("scroll(0, " + y.toString() + ");");
	}

	/** Checks whether source code of the current web page contains given text. */
	public static boolean isPageSourceContains(String value) {
		Logger.info("Verify " + value + " value exists on page: " + WebDriverManager.getDriver().getCurrentUrl());
		return WebDriverManager.getDriver().getPageSource().toLowerCase().contains(value.toLowerCase());
	}

	/** Checks whether current web page is using secure HTTP<b>S</b> connection of not. */
	public static boolean isConnectionHTTPS() {
		return WebDriverManager.getDriver().getCurrentUrl().contains("https://");
	}

	/** Checks whether current web page is using usual HTTP connection or not. */
	public static boolean isConnectionHTTP() {
		return WebDriverManager.getDriver().getCurrentUrl().contains("http://");
	}

	/** Checks whether current URL contains given text or not. */
	public static boolean currentUrlContains(String urlPart) {
		return WebDriverManager.getDriver().getCurrentUrl().contains(urlPart);
	}

	/**
	 *  TODO Clarification.
	 */
	public static void waitFor(int miliseconds) {
		try {
			Thread.sleep(miliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/** This method will send given sequence of keyboard keys to the browser using {@link Actions}. TODO */
	public static void sendKeysToBrowser(CharSequence... keysToSend) {
		new Actions(WebDriverManager.getDriver()).sendKeys(keysToSend).perform();
	}
}
