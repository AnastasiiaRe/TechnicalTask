package framework.platform.html;

import com.google.common.base.Optional;
import framework.Logger;
import framework.Settings;
import framework.adapters.WebDriverManager;
import framework.platform.BrowserType;
import framework.platform.html.support.HtmlElementUtils;
import framework.platform.utilities.Utils;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * One of the framework core classes.
 * <p>
 * Object which is used to work with abstract HTML elements.
 */
public class WebObject extends By {
    private String locator;
    private String controlName;
    private Map<String, String> propMap = new HashMap<>();
    private Optional<String> expectedErrorMessage;
    private WebDriver driver;

    /**
     * Instance method used to call static class method locateElement.
     *
     * @return the web element found by locator
     */
    public RemoteWebElement getElement() {
        RemoteWebElement foundElement = null;
        try {
            foundElement = HtmlElementUtils.locateElement(driver, getLocator());
        } catch (NoSuchElementException n) {
            addInfoForNoSuchElementException(n);
        }
        return foundElement;
    }

    /**
     * Instance method used to call static class method locateElements.
     *
     * @return the list of web elements found by locator
     */
    public List<WebElement> getElements() {
        return getElements("");
    }

    public List<WebElement> getElements(String value) {
        List<WebElement> foundElements = null;
        try {
            foundElements = HtmlElementUtils.locateElements(driver, String.format(locator, value));
        } catch (NoSuchElementException n) {
            addInfoForNoSuchElementException(n);
        }

        return foundElements;
    }

    /**
     * A utility method to provide additional information to the user when a NoSuchElementException is thrown.
     *
     * @param cause The associated cause for the exception.
     */
    private void addInfoForNoSuchElementException(NoSuchElementException cause) {

        StringBuilder msg = new StringBuilder("Unable to find webElement ");

        if (this.controlName != null) {
            msg.append(this.controlName).append(" on ");
        }

        msg.append(" using the locator {").append(locator).append("}");
        throw new NoSuchElementException(msg.toString(), cause);
    }

    /**
     * Constructs an AbstractElement with locator.
     *
     * @param locator
     */
    public WebObject(WebDriver driver, String locator) {
        this.locator = locator;
        this.driver = driver;
    }

    /**
     * Constructs an AbstractElement with locator and controlName.
     *
     * @param locator     the element locator
     * @param controlName the control name used for logging
     */
    public WebObject(WebDriver driver, String locator, String controlName) {
        this.locator = locator;
        this.controlName = controlName;
        this.expectedErrorMessage = Optional.absent();
        this.driver = driver;
    }

    /**
     * Retrieves the locator (id/name/xpath/css locator) for the current {@link WebObject} element.
     *
     * @return The value of locator.
     */
    public String getLocator() {
        return locator;
    }

    /**
     * Retrieves the current instance of webdriver.
     */
    public WebDriver getDriver() {
        return driver;
    }

    /**
     * Retrieves the control name for the current {@link WebObject} element.
     *
     * @return The value of controlName.
     */
    public String getControlName() {
        return controlName;
    }

    /**
     * Finds element on the page and returns the visible (i.e. not hidden by CSS) innerText of this element, including
     * sub-elements, without any leading or trailing whitespace.
     *
     * @return The innerText of this element.
     */
    public String getText() {
        return getElement().getText();
    }

    /**
     * Checks if element is present in the html dom. An element that is present in the html dom does not mean it is
     * visible. To check if element is visible, use {@link #getElement()} to get {@link WebElement} and then invoke
     * {@link WebElement#isDisplayed()}.
     *
     * @return True if element is present, false otherwise.
     */
    public boolean isElementPresent() {
        boolean returnValue = false;
        try {
            if (getElement() != null) {
                returnValue = true;
            }
        } catch (NoSuchElementException e) {
            returnValue = false;
        }
        return returnValue;
    }

    /**
     * Checks if element is present in the html DOM?
     * TODO: Check for duplicate in comparison with method above.
     */
    public boolean isPresent() {
        waitForJQueryComplete();
            try {
                return this.getElement() != null && (this.getElement().isDisplayed());
            } catch (NoSuchElementException var2) {
                return var2.getCause().getMessage().contains("Element is not usable");
            }
    }

    /**
     * Is this element displayed or not? This method avoids the problem of having to parse an element's "style"
     * attribute.
     *
     * @return Whether or not the element is displayed
     */
    public boolean isVisible() {
        try {
            return getElement() != null && getElement().isDisplayed();
        } catch (ElementNotVisibleException var1) {
            return false;
        } catch (NoSuchElementException var2) {
            return false;
        } catch (StaleElementReferenceException var3) {
            return false;
        }
    }

    /**
     * Is the element currently enabled or not? This will generally return true for everything but disabled input
     * elements.
     *
     * @return True if element is enabled, false otherwise.
     */
    public boolean isEnabled() {
        return getElement().isEnabled();
    }

    /**
     * Is the element currently selected or not?
     *
     * @return True if element is selected, false otherwise.
     */
    public boolean isSelected() {
        return getElement().isSelected();
    }

    /**
     * Is the element contains text?
     *
     * @param text - text to search
     * @return True if element contains text, false otherwise.
     */
    public boolean isTextPresent(String text) {
        return getElement().getText().contains(text);
    }

    public boolean isAllEnabled() {
        try {
            return getElements().stream().anyMatch(WebElement::isDisplayed);
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * This method will wait until element will be visible.
     */
    public WebObject waitUntilVisible() {
        Logger.debug("Waiting for element " + getControlName());
            try {
                this.waitForCondition().until(this.elementIsDisplayed());
            } catch (Throwable var2) {
                Logger.err(">>>>> ELEMENT " + getControlName() + " is not found <<<<<<");
                this.throwErrorWithCauseIfPresent(var2, var2.getMessage());
            }
            waitForJQueryComplete();

            return this;
    }

    /**
     * This method will wait until element will be visible on specified page.
     */
    public WebObject waitUntilVisibleOnPage(Object page) {
        Logger.debug("Waiting for page " + page.getClass().getSimpleName() + " to load");
            try {
                this.waitForCondition().until(elementIsDisplayed());
            } catch (Throwable var2) {
                var2.printStackTrace();
                Logger.err(">>>>> TEST WAS NOT ON EXPECTED PAGE - " + page.getClass().getSimpleName() + " <<<<<<");
                this.throwErrorWithCauseIfPresent(var2, var2.getMessage());
            }
            waitForJQueryComplete();

            return this;
    }

    /**
     * This method will wait until element will be enabled.
     */
    public WebObject waitUntilEnabled() {
            try {
                this.waitForCondition().until(this.elementIsEnabled());
            } catch (Throwable var2) {
                this.throwErrorWithCauseIfPresent(var2, var2.getMessage());
            }
            waitForJQueryComplete();

            return this;
    }

    /**
     * This method will wait until all elements are enabled.
     */
    public WebObject waitElementsReady() {
            try {
                this.waitForCondition().until(this.allElementsEnabled());
            } catch (Throwable var2) {
                this.throwErrorWithCauseIfPresent(var2, var2.getMessage());
            }
            waitForJQueryComplete();

            return this;
    }

    /**
     * This method will return instance of {@link FluentWait} which can be used to wait until specific condition.
     */
    public Wait<WebDriver> waitForCondition() {
        return (new FluentWait(this.driver))
                .withTimeout(30000L, TimeUnit.MILLISECONDS)
                .pollingEvery(250L, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class, NoSuchFrameException.class);
    }

    /**
     * This method will select option with given value from select list.
     */
    public void selectByValue(String value) {
        new Select(getElement()).selectByValue(value);
    }

    /**
     * This method will select option which contains given text from select list.
     */
    public void selectByText(String text) {
        waitUntilEnabled();
        Select dropdown = new Select(getElement());
        dropdown.selectByVisibleText(text);
        waitForJQueryComplete();
    }

    /**
     * This element will return text of selected option from the select list.
     */
    public String getSelectedText() {
        waitUntilEnabled();
        Select dropdown = new Select(getElement());
        return dropdown.getFirstSelectedOption().getText();
    }

    /**
     * This method will switch focus to the frame which is represented by {@link WebObject}
     */
    public WebObject switchToFrame() {
        driver.switchTo().frame(getElement());

        return this;
    }

    /**
     * Get the value of a the given attribute of the element. Will return the current value, even if this has been
     * modified after the page has been loaded. More exactly, this method will return the value of the given attribute,
     * unless that attribute is not present, in which case the value of the property with the same name is returned. If
     * neither value is set, null is returned. The "style" attribute is converted as best can be to a text
     * representation with a trailing semi-colon. The following are deemed to be "boolean" attributes, and will return
     * either "true" or null: async, autofocus, autoplay, checked, compact, complete, controls, declare, defaultchecked,
     * defaultselected, defer, disabled, draggable, ended, formnovalidate, hidden, indeterminate, iscontenteditable,
     * ismap, itemscope, loop, multiple, muted, nohref, noresize, noshade, novalidate, nowrap, open, paused, pubdate,
     * readonly, required, reversed, scoped, seamless, seeking, selected, spellcheck, truespeed, willvalidate. Finally,
     * the following commonly mis-capitalized attribute/property names are evaluated as expected: class, readonly
     *
     * @param attributeName the attribute name to get current value
     * @return The attribute's current value or null if the value is not set.
     */
    public String getAttribute(String attributeName) {
        return getElement().getAttribute(attributeName);
    }

    /**
     * Get the value of a the given css attribute of the element. Will return the current value, even if this has been
     * modified after the page has been loaded.
     */
    public String getCssValue(String cssValue) {
        return getElement().getCssValue(cssValue);
    }

    /**
     * Gets the (whitespace-trimmed) value of an input field (or anything else with a value parameter). For
     * checkbox/radio elements, the value will be "on" or "off" depending on whether the element is checked or not.
     *
     * @return the element value, or "on/off" for checkbox/radio elements
     */
    public String getValue() {
        return getAttribute("value");
    }

    /**
     * Sets value in property map {@link #propMap}.
     *
     * @param key
     * @param value
     */
    public void setProperty(String key, String value) {
        propMap.put(key, value);
    }

    /**
     * Gets value from property map {@link #propMap}.
     *
     * @param key the key to retrieve a value from the property map
     * @return the value to which the specified key is mapped, or null if this map contains no mapping for the key
     */
    public String getProperty(String key) {
        return propMap.get(key);
    }

    public WebObject and() {
        return this;
    }

    public WebObject then() {
        return this;
    }

    protected void processScreenShot() {
        String title = "Default Title";
        try {
            title = driver.getTitle();
        } catch (WebDriverException thrown) {
            Logger.info("An exception occured while getting page title");
        }
    }

    /**
     * Checks for presence of alert.
     */
    protected void validatePresenceOfAlert() {
        try {
            driver.switchTo().alert();
            String errorMsg = "Encountered an alert. Cannot wait for an element when an operation triggers an alert.";
            throw new InvalidElementStateException(errorMsg);
        } catch (NoAlertPresentException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * This method will return first element from the select list.
     */
    public WebElement getFirstElementFromList() {
        int counter = 0;
        while (counter < 10) {
            try {
                return getElements()
                        .stream()
                        .findFirst()
                        .get();
            } catch (StaleElementReferenceException | java.util.NoSuchElementException e) {
                counter++;
                Logger.info(String.format("Trying to recover after Exception %s", e.getMessage()));
            }
        }
        return null;
    }

    /**
     * This method will return first element from the select list that corresponds to search criteria.
     *
     * @param filter - search criteria
     */
    public WebElement getFirstRelatedElementFromList(String filter) {
        int counter = 0;

        while (counter < 10) {
            try {
                return getElements()
                        .stream().filter(tab -> tab.getText()
                                .contains(filter))
                        .findFirst().get();
            } catch (StaleElementReferenceException | java.util.NoSuchElementException e) {
                counter++;
                Logger.info(String.format("Trying to recover after Exception %s", e.getMessage()));
            }
        }
        return null;
    }

    /**
     * This method will return first element from the select list that corresponds to search criteria.
     *
     * @param filter1 - first search criteria
     * @param filter2 - second search criteria
     */
    public WebElement getFirstRelatedElementFromList(String filter1, String filter2) {
        int counter = 0;

        while (counter < 10) {
            try {
                return getElements()
                        .stream().filter(tab ->
                                tab.getText().contains(filter1)
                                        && tab.getText().contains(filter2))
                        .findFirst().get();
            } catch (StaleElementReferenceException | java.util.NoSuchElementException e) {
                counter++;
                Logger.info(String.format("Trying to recover after Exception %s", e.getMessage()));
            }
        }
        return null;
    }

    /**
     * Is element present in select list? This method will return true if there is an element in select list
     * that contains given text.
     */
    public boolean isElementPresentInList(String filter) {
        int counter = 0;

        while (counter < 10) {
            try {
                return getElements()
                        .stream()
                        .anyMatch(row -> row.getText().contains(filter));
            } catch (StaleElementReferenceException | java.util.NoSuchElementException e) {
                counter++;
                Logger.info(String.format("Trying to recover after Exception %s", e.getMessage()));
            }
        }
        return false;
    }

    /**
     * Is element present in select list? This method will return true if there is an element in select list
     * that corresponds to search criteria.
     *
     * @param filter1 - first search criteria
     * @param filter2 - second search criteria
     */
    public boolean isElementPresentInList(String filter1, String filter2) {
        int counter = 0;

        while (counter < 10) {
            try {
                return getElements()
                        .stream()
                        .anyMatch(row -> row.getText().contains(filter1)
                                && row.getText().contains(filter2));
            } catch (StaleElementReferenceException | java.util.NoSuchElementException e) {
                counter++;
                Logger.info(String.format("Trying to recover after Exception %s", e.getMessage()));
            }
        }
        return false;
    }

    /**
     * This method will return child element of {@link WebObject} which corresponds to given locator.
     */
    public WebElement getChildElement(String filter, By locator) {
        int counter = 0;

        while (counter < 10) {
            try {
                return getElements()
                        .stream()
                        .filter(tab ->
                                tab.getText().contains(filter))
                        .findFirst().get()
                        .findElement(locator);
            } catch (StaleElementReferenceException | java.util.NoSuchElementException e) {
                counter++;
                Logger.info(String.format("Trying to recover after Exception %s", e.getMessage()));
            }
        }
        return null;
    }

    /**
     * The click function and wait for page to load
     */
    public void click() {
        waitForJQueryComplete();
        if (Settings.browser.equals(BrowserType.CHROME)) {
            scrollToElement();
        }
        getElement().click();
    }

    /**
     * This method will uncheck checkbox if it's already checked.
     */
    public void uncheck() {
        if (isSelected()) {
            click();
        }
    }

    /**
     * TODO: I dunno how to use it.
     */
    public void selectCheckbox(boolean flag) {
        if (isSelected() != flag) {
            getElement().click();
        }
    }

    /**
     * This method will perform double click on the element using {@link Actions}
     */
    public void actionDoubleClick() {
        new Actions(driver).doubleClick(getElement()).build().perform();
    }

    /**
     * This method will perform single click on the element using {@link Actions}
     */
    public void actionClick() {
        new Actions(driver).click(getElement()).build().perform();
    }

    /**
     * This method will clear input field and send given text to it.
     */
    public void type(String value) {
        waitForJQueryComplete();
        RemoteWebElement element = getElement();
        element.clear();
        element.sendKeys(value);
        waitForJQueryComplete();
    }

    /**
     * This method will send given sequence of characters to the element.
     */
    public void sendKeys(CharSequence... keysToSend) {
        getElement().sendKeys(keysToSend);
    }

    /**
     * This method will clear input field.
     */
    public void clear() {
        RemoteWebElement element = getElement();
        element.clear();
        waitForJQueryComplete();
    }

    protected String getErrorMessage(String defaultErrorMessage) {
        return this.expectedErrorMessage.or(defaultErrorMessage);
    }

    // TODO
    private ExpectedCondition elementIsDisplayed() {
        return driver -> WebObject.this.isVisible();
    }

    // TODO
    private ExpectedCondition elementIsEnabled() {
        return driver -> Boolean.valueOf(WebObject.this.isEnabled());
    }

    // TODO
    private ExpectedCondition allElementsEnabled() {
        return driver -> Boolean.valueOf(WebObject.this.isAllEnabled());
    }

    /** Will wait until page is loaded completely and all instances of ajax
     *  are finished their actions.
     */
    private void waitForJQueryComplete() {
        int sleepTime = 500;
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        for (int i = 0; i < 5000 / sleepTime; i++) {
            if ((Boolean) jse.executeScript(
                         // && window.jQuery != undefined && jQuery.active == 0 (those can be used optionally)
                    "return document.readyState == 'complete'")) {
                return;
            }
            hardwait();
        }
        Logger.info("[WebObject] Wait for ajax encountered an error, but trying to continue the test.");
    }

    //TODO
    private void hardwait() {
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //TODO
    private void failWithMessage(String errorMessage) {
        throw new AssertionError(this.getErrorMessage(errorMessage));
    }

    //TODO
    private void throwErrorWithCauseIfPresent(Throwable timeout, String defaultMessage) {
        String timeoutMessage = timeout.getCause() != null ? timeout.getCause().getMessage() : timeout.getMessage();
        String finalMessage = StringUtils.isNotEmpty(timeoutMessage) ? timeoutMessage : defaultMessage;
        throw new ElementNotVisibleException(finalMessage, timeout);
    }

    /** This method will wait until element will be clickable. */
    public void waitUntilClickable() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(getElement()));
    }

    /** This method will wait until element will be invisible. */
    public void waitUntilInvisible() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            if (locator.contains("css=") || locator.contains("id=")) {
                String loc = locator.replaceAll("id=", "#").replaceAll("css=", "");
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(loc)));
            } else if (locator.contains("xpath=")) {
                String loc = locator.replaceAll("xpath=", "");
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(loc)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        waitForJQueryComplete();
    }

    /** Wait until an element is no longer attached to the DOM. */
    public void waitUntilStalenessOfElement() {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.stalenessOf(getElement()));
    }

    /** An expectation for checking that an element, known to be present on the DOM of a page, is visible. */
    public void waitUntilElementIsVisible() {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOf(getElement()));
    }

    /** An expectation for checking that all elements present on the web page that match the locator are visible. */
    public void waitUntilVisibilityOfAllElements() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(locator)));
    }

    /** An expectation for checking that there is at least one element present on a web page. */
    public void waitUntilPresenceOfAllElements() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(locator)));
    }

    /** Move mouse to the element using {@link Actions} */
    public void mouseHover() {
        new Actions(driver).moveToElement(getElement()).build().perform();
    }

    /** This method will return number of elements on page. */
    public int getElementsCount() {
        try {
            return getElements().size();
        } catch (NoSuchElementException ignored) {
            return 0;
        }
    }

    /** This method will return number of visible elements on page. */
    public int getVisibleElementsCount() {
        int numberOfVisibleElements = 0;
        for (int elementNumber = 0; elementNumber <= getElementsCount() - 1; elementNumber++) {
            try {
                getElements().get(elementNumber).isDisplayed(); //is it will pass this step element is visible
                if (!getElements().get(elementNumber).getAttribute("style").contains("display: none;")) {
                    if (!getElements().get(elementNumber).getSize().toString().equals("(0, 0)")) {
                        numberOfVisibleElements++;
                    }
                }
            } catch (Exception ignored) {
                Logger.info("Element number " + elementNumber + " not visible");
            }
        }
        return numberOfVisibleElements;
    }

    /** This method will return number elements on page which are both visible and clickable. */
    public int getNumberOfVisibleAndClickableElements() {
        Logger.info("Getting number of clickable and visible elements");
        int numberOfVisibleElements = 0;
        for (WebElement element : getElements()) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, 1);
                wait.until(ExpectedConditions.elementToBeClickable(element));
                numberOfVisibleElements++;
            } catch (TimeoutException ignored) {
                Logger.debug("element is not visible");
            }
        }
        Logger.info("Number of visible elements: " + numberOfVisibleElements);
        return numberOfVisibleElements;
    }

    /** This method will return first found visible and also clickable element on page. */
    public int getFirstVisibleAndClickableElement() {
        Logger.info("Getting current first clickable element");
        int elementNumber = 1;
        WebDriverManager.getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //need that to avoid waiting for 10 seconds in try each time
        for (WebElement element : getElements()) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, 1);
                wait.until(ExpectedConditions.elementToBeClickable(element));
                Logger.info("First Clickable element number is " + elementNumber);
                WebDriverManager.getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                return elementNumber;
            } catch (TimeoutException ignored) {
                elementNumber++;
            }
        }
        WebDriverManager.getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return -1; //will not get here
    }

    /** This method will execute click on element with given number. */
    public void clickOnElementNumber(int number) {
        if (number == 0) {
            number = 1;
        }
        getElements().get(number - 1).click();
    }

    /** This method will return specified attribute for element with given number. */
    public String getAttributeOfElementNumber(int number, String attribute) {
        return getElements().get(number).getAttribute(attribute);
    }

    /** This method will execute click using javascript on the element. */
    public void clickWithJS() {
        waitForJQueryComplete();
        waitFor(500);
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        if (locator.contains("xpath=") || locator.contains("//")) {
            jse.executeScript("arguments[0].click();", getElement());
        } else {
            String loc = locator.replaceAll("id=", "#").replaceAll("css=", "");
            jse.executeScript("document.querySelector(\"" + loc + "\").click();");
        }
    }

    /** This method will scroll page to the element. */
    public void scrollToElement() {
        int elementY = getElement().getLocation().getY();
        int currentLocation = Integer.parseInt(Utils.getJSResult("return document.documentElement.scrollTop;"));
        int visibleY = (Settings.isDesktop()) ? 800 : 500;
        Logger.debug("elementY: " + elementY);
        Logger.debug("currentLocation: " + currentLocation);
        if (elementY > visibleY || currentLocation > 10) {
            Utils.scrollPage(elementY - 150);
            waitFor(500);
        }
    }

    //TODO
    public void waitFor(int miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //TODO
    @Override
    public List<WebElement> findElements(SearchContext context) {
        return null;
    }

    /** This method will move slider element to given offset using {@link Action} */
    public void moveSlider(int xOffset, int yOffset) {
        new Actions(driver).dragAndDropBy(getElement(), xOffset, yOffset).build().perform();
    }

    public int getElementWidth() {
        return getElement().getSize().getWidth();
    }

    public String getHrefOfElementNumber(int element) {
        return getElements().get(element - 1).getAttribute("href");
    }
}
