package framework.components;

import framework.Logger;
import framework.Settings;
import framework.adapters.WebDriverManager;
import framework.platform.html.WebObject;
import framework.platform.web.Locator;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A Base class from which all page classes should be derived.
 * <p>
 * It contains the code to initialize {$project}.pages, initialize WebObject's, and interact in various ways with the
 * page(s).
 */
public abstract class BasicPage extends AbstractPage {
    public static WebDriver basedriver;

    /** Constructor. */
    public BasicPage() {
        super();
        basedriver = WebDriverManager.getDriver();
        initializeWebObjects(this);
        waitForPageToLoad();
    }

    @Override
    public void waitForPageToLoad() {
        waitForAjaxRequestToBeFinished();
    }

    /** Calling {@code waitForAjaxRequestToBeFinished} with 5 seconds maximum timeout. */
    protected void waitForAjaxRequestToBeFinished() {
        waitForAjaxRequestToBeFinished(5000);
    }

    /** Will wait with specified maximum timeout until page is loaded completely and all instances of ajax
     *  are finished their actions.
     *  @param timeoutInMilliseconds
     *                              Maximum timeout in milliseconds (1000 = 1 second)
     */
    protected void waitForAjaxRequestToBeFinished(int timeoutInMilliseconds) {
        int sleepTime = 500;
        JavascriptExecutor jse = (JavascriptExecutor) basedriver;
        for (int i = 0; i < timeoutInMilliseconds / sleepTime; i++) {
            sleepFor(sleepTime / 2);
            if ((Boolean) jse.executeScript(
                    //window.jQuery != undefined && jQuery.active == 0 (those can be used optionally)
                    "return document.readyState == 'complete'")) {
                return;
            }
            sleepFor(sleepTime / 2);
        }
        Logger.info("[BasicPage] Wait for ajax encountered an error, but trying to continue the test.");
    }

    /**
     * Stops program execution for specified amount of time.
     *
     * @param milliseconds
     *                  Amount of time to wait in milliseconds (1000 = 1 second)
     */
    public void sleepFor(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialization of all fields of type {@link WebObject} which are complemented with the annotation {@link Locator}.
     *
     * @param whichClass
     *                  Page class to parse.
     */
    public void initializeWebObjects(Object whichClass) {
        Class<?> incomingClass = whichClass.getClass();
        ArrayList<Field> fields = new ArrayList<>();

        Class<?> tempIncomingClass = incomingClass;
        do {
            fields.addAll(Arrays.asList(tempIncomingClass.getDeclaredFields()));
            tempIncomingClass = tempIncomingClass.getSuperclass();
        } while (tempIncomingClass != null);

        String errorDesc = " while initializing locators for WebObjects. Root cause:";
        try {
            for (Field field : fields)
                if (field.isAnnotationPresent(Locator.class)) {
                    Annotation annotation = field.getAnnotation(Locator.class);
                    Locator locatorAnnotation = (Locator) annotation;

                    field.setAccessible(true);

                    Class<?> dataMemberClass = Class.forName(field.getType().getName());
                    Class<?> parameterTypes[] = new Class[3];

                    String locator = null;
                    switch (Settings.getPlatform()) {
                        case DESKTOP:
                            locator = locatorAnnotation.main();
                            break;
                        case MOBILE:
                            if (locatorAnnotation.mobile().isEmpty()) {
                                locator = locatorAnnotation.main();
                            } else {
                                locator = locatorAnnotation.mobile();
                            }
                            break;
                        case TABLET:
                            if (locatorAnnotation.mobile().isEmpty()) {
                                locator = locatorAnnotation.main();
                            } else {
                                locator = locatorAnnotation.tablet();
                            }
                            break;
                        default:
                            break;
                    }

                    parameterTypes[0] = WebDriver.class;
                    parameterTypes[1] = String.class;
                    parameterTypes[2] = String.class;
                    Constructor<?> constructor = dataMemberClass.getDeclaredConstructor(parameterTypes);

                    Object[] constructorArgList = new Object[3];

                    constructorArgList[0] = basedriver;
                    constructorArgList[1] = locator;
                    constructorArgList[2] = field.getName();
                    Object retobj = constructor.newInstance(constructorArgList);
                    field.set(whichClass, retobj);
                }
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException("Class not found" + errorDesc + exception, exception);
        } catch (IllegalArgumentException exception) {
            throw new RuntimeException("An illegal argument was encountered" + errorDesc + exception, exception);
        } catch (InstantiationException exception) {
            throw new RuntimeException("Could not instantantiate object" + errorDesc + exception, exception);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException("Could not access data member" + errorDesc + exception, exception);
        } catch (InvocationTargetException exception) {
            throw new RuntimeException("Invocation error occured" + errorDesc + exception, exception);
        } catch (SecurityException exception) {
            throw new RuntimeException("Security error occured" + errorDesc + exception, exception);
        } catch (NoSuchMethodException exception) {
            throw new RuntimeException("Method specified not found" + errorDesc + exception, exception);
        }
        pageInitialized = true;
    }
}
