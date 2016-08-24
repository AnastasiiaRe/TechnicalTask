package framework.platform.utilities;

import framework.adapters.WebDriverManager;

import java.util.Set;

import static org.testng.Assert.fail;

/**
 * Utilitarian class which provides switching between browser windows.
 */
public class WindowUtils {

	public static void switchToLastOpenedWindow() {
		Utils.waitFor(5000);
		try {
			String currentWindow = WebDriverManager.getDriver()
					.getWindowHandle();
			Set<String> windows = WebDriverManager.getDriver()
					.getWindowHandles();
			if (windows.size() > 1) {
				for (String window : windows) {
					WebDriverManager.getDriver().switchTo().window(window);
					if (!WebDriverManager.getDriver().getWindowHandle()
							.equals(currentWindow)) {
						WebDriverManager.getDriver().switchTo().window(window);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Problem switching windows " + e.getMessage());
		}
	}

	public static void switchToMainWindow() {
		try {
			String currentWindow = WebDriverManager.getDriver()
					.getWindowHandle();
			Set<String> windows = WebDriverManager.getDriver()
					.getWindowHandles();
			String mainWindow = null;
			for (String window : windows) {
				WebDriverManager.getDriver().switchTo().window(window);
				if (WebDriverManager.getDriver().getWindowHandle()
						.equals(currentWindow)) {
					WebDriverManager.getDriver().close();
				} else {
					mainWindow = WebDriverManager.getDriver().getWindowHandle();
				}
				WebDriverManager.getDriver().switchTo().window(mainWindow);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Problem switching back to main window " + e.getMessage());
		}
	}
}