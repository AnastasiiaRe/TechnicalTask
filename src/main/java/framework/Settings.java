package framework;

import framework.platform.BrowserType;
import framework.platform.ConfigProvider;
import framework.platform.Device;
import framework.platform.UnknownBrowserException;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

import static org.testng.Assert.fail;

/**
 * Utilitarian class that provides initialization of browser for specified webdriver and
 * information about current environment.
 */
public class Settings {
    public static BrowserType browser;
    public static ConfigProvider config;

    /** Constructor. */
    public Settings() {
        loadSettings();
    }

    /** Will load settings based on properties from {@link ConfigProvider} */
    private void loadSettings() {
        config = new ConfigProvider();
        browser = BrowserType.Browser(config.getBrowser());
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Jdk14Logger");
    }

    /**
     * Provides information about current platform.
     * @return
     *           Type of the current platform.
     */
    public static Device getPlatform() {
        if (isMobile()) {
            return Device.MOBILE;
        }
        if (isDesktop()) {
            return Device.DESKTOP;
        } else {
            return Device.TABLET;
        }
    }

    /** Checks whether is current platform Mobile or not. */
    public static boolean isMobile() {
        return (browser.equals(BrowserType.MOBILE_CHROME) || browser.equals(BrowserType.MOBILE_SAFARI)) && !isTablet();
    }

    /** Checks whether is current platform Desktop or not. */
    public static boolean isDesktop() {
        return !isMobile() && !isTablet();
    }

    /** Checks whether is current platform Tablet or not. */
    public static boolean isTablet() {
        return config.getDevice().toLowerCase().contains("ipad")
                || config.getDevice().equalsIgnoreCase("Nexus 7")
                || config.getDevice().equalsIgnoreCase("Nexus 9");
    }

    /** Creates new instance of webdriver. */
    public static RemoteWebDriver createInstance() {
        return getDriver(browser);
    }

    /**
     * Provides information about current selenium grid hub URL.
     *
     * @return
     *          URL which is specific for current selenium grid hub.
     */
    private static URL getRemoteURL() {
        try {
            if (isMobile() || isTablet()) {
                return new URL(config.getAppiumUrl());
            } else {
                return new URL(String.format("http://%s:%s/wd/hub", config.getGrid(), config.getPort()));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail("There was an error. Please see log");
            return null;
        }
    }

    /**
     * Creates instance of webdriver for specific {@link BrowserType}
     *
     * @param browserType
     *          Type of browser which will be user for webdriver initialization.
     * @return
     *          New instance of webdriver.
     */
    private static RemoteWebDriver getDriver(BrowserType browserType) {
        DesiredCapabilities capabilities;
        Logger.info("Hub URL: " + getRemoteURL());
        switch (browserType) {
            case FIREFOX:
                capabilities = DesiredCapabilities.firefox();
                return new RemoteWebDriver(getRemoteURL(), capabilities);
            case FIREFOX_NO_GRID:
                return new FirefoxDriver();
            case CHROME:
                capabilities = DesiredCapabilities.chrome();
                return new RemoteWebDriver(getRemoteURL(), capabilities);
            case SAFARI:
                capabilities = DesiredCapabilities.safari();
                return new RemoteWebDriver(getRemoteURL(), capabilities);
            case IE:
                capabilities = DesiredCapabilities.internetExplorer();
                capabilities.setCapability("nativeEvents", false);
                capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
                return new RemoteWebDriver(getRemoteURL(), capabilities);
            case MOBILE_CHROME:
                capabilities = DesiredCapabilities.android();
                capabilities.setCapability("browserName", "chrome");
                capabilities.setCapability("device", "android");
                capabilities.setCapability("newCommandTimeout", "180");
                capabilities.setCapability("platformVersion", "5.1");
                capabilities.setCapability("platformName", org.openqa.selenium.Platform.ANDROID);
                capabilities.setCapability("deviceName", config.getDevice());
                return new AndroidDriver(getRemoteURL(), capabilities);
            case MOBILE_SAFARI:
                capabilities = DesiredCapabilities.iphone();
                capabilities.setCapability("browserName", "safari");
                capabilities.setCapability("device", "iphone");
                capabilities.setCapability("platformName", "ios");
                capabilities.setCapability("platformVersion", config.getPlatformVersion());
                capabilities.setCapability("deviceName", config.getDevice());
                capabilities.setCapability("newCommandTimeout", "180");
                capabilities.setCapability("autoAcceptAlerts", "true");
                return new IOSDriver(getRemoteURL(), capabilities);
            default:
                throw new UnknownBrowserException("Cannot create driver for unknown browser type");
        }
    }

    /**
     * Provides information about default environment URL.
     * @return
     *          URL which is default for specified environment.
     */
    public static String getDefaultUrl() {
        return getModifiedUrl("");
    }

    /**
     * Provides modified base URL that adds specific subdomain.
     * @param subDomain
     *          Subdomain which must be implemented into default URL.
     * @return
     *          URL for specific subdomain.
     */
    public static String getModifiedUrl(String subDomain) {
        String publicUrl = "";
        String connectionType = "";
        String headerAuth = "";
        if (config.getEnvironment().equalsIgnoreCase("dev")) {
            publicUrl = config.getDevPublicSiteUrl();
        }
        if (config.getEnvironment().equalsIgnoreCase("qa")) {
            publicUrl = config.getQaPublicSiteUrl();
        }
        if (config.getEnvironment().equalsIgnoreCase("stage")) {
            publicUrl = config.getStagePublicSiteUrl();
            headerAuth = config.getHeaderAuth();
        }
        if (config.getEnvironment().equalsIgnoreCase("prod")) {
            publicUrl = config.getProdPublicSiteUrl();
        }
        if (config.getEnvironment().equalsIgnoreCase("local")) {
            publicUrl = config.getLocalPublicSiteUrl();
        }
        if (subDomain.isEmpty()) {
            connectionType = "http://";
        }
        return String.format(connectionType + headerAuth + subDomain + publicUrl);
    }
}
