package framework.platform;

import framework.Logger;
import framework.Settings;
import framework.adapters.WebDriverManager;
import org.openqa.selenium.support.PageFactory;

import java.util.concurrent.TimeUnit;

/**
 * This class provides methods for initialization of page objects.
 * <p>
 *     All custom site navigators must be derived from this class.
 */
public class SiteNavigatorBase {

    public static void openPage(String url) {
        openPage(url, "");
    }

    public static <T> T openPage(String url, Class<T> expectedPage) {
        openPage(url, "");
        return PageFactory.initElements(WebDriverManager.getDriver(), expectedPage);
    }

    public static void openPage(String url, String subDomain) {
        Settings settings = new Settings();
        String environment = settings.getModifiedUrl(subDomain);
        Logger.info("Environment: " + environment);
        WebDriverManager.getDriver().navigate().to(environment);
        if (Settings.isDesktop()) {
            WebDriverManager.getDriver().manage().window().maximize();
        }
        WebDriverManager.getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    protected static <T> T goToPage(String page, Class<T> expectedPage) {
        Logger.info("Opening page " + expectedPage.getSimpleName());
        openPage(page);
        return PageFactory.initElements(WebDriverManager.getDriver(), expectedPage);
    }

}
