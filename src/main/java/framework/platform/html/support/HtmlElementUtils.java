package framework.platform.html.support;

import com.google.common.base.Preconditions;
import framework.platform.html.WebObject;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ByIdOrName;

import java.util.ArrayList;
import java.util.List;

/**
 * Utilitarian class which serves as search engine of HTML elements for {@link WebObject}.
 * <p>
 *     Contains methods for identification of locators and search of elements.
 */
public class HtmlElementUtils {
    private static final String INVALID_LOCATOR_ERR_MSG = "Locator cannot be null (or) empty.";

    /** Constructor. */
    private HtmlElementUtils() {
        //Utility class. Hide the constructor to prevent instantiation.
    }

    /**
     * Parses locator string to identify the proper By subclass before calling Selenium
     * {@link WebElement#findElement(By)} to locate the web element.
     *
     * @param locator - A String that represents the means to locate this element (could be id/name/xpath/css locator).
     * @return - A {@link RemoteWebElement} that represents the html element that was located using the locator
     * provided.
     */
    public static RemoteWebElement locateElement(WebDriver driver, String locator) {
        Preconditions.checkArgument(StringUtils.isNotBlank(locator), INVALID_LOCATOR_ERR_MSG);
        By locatorBy = resolveByType(locator);
        RemoteWebDriver rwd = (RemoteWebDriver) driver;

        RemoteWebElement element = (RemoteWebElement) rwd.findElement(locatorBy);

        return element;
    }

    /**
     * Parses locator string to identify the proper By subclass before calling Selenium
     * {@link WebElement#findElements(By)} to locate the web elements.
     *
     * @param locator - A String that represents the means to locate this element (could be id/name/xpath/css locator).
     * @return A {@link WebElement} list that represents the html elements that was located using the locator provided.
     */
    public static List<WebElement> locateElements(WebDriver driver, String locator) {
        Preconditions.checkArgument(StringUtils.isNotBlank(locator), INVALID_LOCATOR_ERR_MSG);
        By locatorBy = resolveByType(locator);

        RemoteWebDriver rwd = (RemoteWebDriver) driver;
        List<WebElement> webElementsFound = rwd.findElements(locatorBy);
        if (webElementsFound.isEmpty()) {
            throw new NoSuchElementException(generateUnsupportedLocatorMsg(locator));
        }
        return webElementsFound;
    }

    /**
     * Method to split the locator string with delimiter '|' to return a valid {@link By } type.
     *
     * @param locator - A String that represents the means to locate this element (could be id/name/xpath/css locator).
     * @return A {@link By} object that represents the actual locating strategy that would be employed.
     */
    public static By resolveByType(String locator) {
        Preconditions.checkArgument(StringUtils.isNotBlank(locator), INVALID_LOCATOR_ERR_MSG);
        By locatorBy = null;
        locator = locator.trim();

        if (locator.indexOf("|") == -1) {
            locatorBy = getFindElementType(locator);
        } else {
            String[] locators = locator.split("\\Q|\\E");
            List<By> result = new ArrayList<>();
            for (String temp : locators) {
                result.add(getFindElementType(temp));
            }
            locatorBy = new ByOrOperator(result);
        }

        return locatorBy;
    }

    /**
     * Detects Selenium {@link org.openqa.selenium.By By} type depending on what the locator string starts with.
     *
     * @param locator - A String that represents the means to locate this element (could be id/name/xpath/css locator).
     * @return The {@link By} sub-class that represents the actual location strategy that will be used.
     */
    public static By getFindElementType(String locator) {
        Preconditions.checkArgument(StringUtils.isNotBlank(locator), INVALID_LOCATOR_ERR_MSG);
        By valueToReturn = null;
        locator = locator.trim();
        if (locator.startsWith("id=")) {
            valueToReturn = By.id(locator.substring("id=".length()));
        } else if (locator.startsWith("name=")) {
            valueToReturn = By.name(locator.substring("name=".length()));
        } else if (locator.startsWith("link=")) {
            valueToReturn = By.linkText(locator.substring("link=".length()));
        } else if (locator.startsWith("xpath=")) {
            valueToReturn = By.xpath(locator.substring("xpath=".length()));
        } else if (locator.startsWith("/") || locator.startsWith("./")) {
            valueToReturn = By.xpath(locator);
        } else if (locator.startsWith("css=")) {
            valueToReturn = By.cssSelector(locator.substring("css=".length()));
        } else {
            valueToReturn = new ByIdOrName(locator);
        }

        return valueToReturn;
    }

    /** This method generates error message for unsupported locator. */
    private static String generateUnsupportedLocatorMsg(String locator) {
        return "Unsupported locator {" + locator
                + "}. Locator has to be either a name, id, link text, xpath, or css selector.";
    }

}
