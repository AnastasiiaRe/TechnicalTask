package framework.adapters;

import framework.Logger;
import framework.Settings;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

import java.util.concurrent.TimeUnit;

/**
 * One of the framework core classes.
 * <p/>
 * Custom listener which is used for instantiation of new WebDriver for each test method.
 */
public class WebDriverListener implements IInvokedMethodListener {

	/**
	 * Overload of the testng listener that will start new instance of webdriver
	 */
	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		RemoteWebDriver driver = new Settings().createInstance();
		driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(120, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		WebDriverManager.setWebDriver(driver);
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		if (!method.isTestMethod()) {
			try {
				WebDriverManager.getDriver().quit();
			} catch (Exception ignored) {
				Logger.info("Can not quit driver");
			}
		}
	}
}
