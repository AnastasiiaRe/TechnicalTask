package framework.adapters;

import org.openqa.selenium.remote.RemoteWebDriver;

/**
 *  Provides instantiation and access to instance of webdriver.
 */
public class WebDriverManager {
    private static ThreadLocal<RemoteWebDriver> webDriver = new ThreadLocal<>();

    public static RemoteWebDriver getDriver() {
        return webDriver.get();
    }

    public static void setWebDriver(RemoteWebDriver driver) {
        webDriver.set(driver);
    }
}
