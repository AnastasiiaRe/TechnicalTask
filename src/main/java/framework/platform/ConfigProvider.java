package framework.platform;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utilitarian class which provides initialization of all project properties as well access to them.
 */
public class ConfigProvider {

	private final Properties properties = new Properties();

	private final String devPublicSiteUrl;
	private final String qaPublicSiteUrl;
	private final String stagePublicSiteUrl;
	private final String prodPublicSiteUrl;
	private final String localPublicSiteUrl;
	private final String grid;
	private final String port;
	private final String platformVersion;
	private final String device;
	private final String browser;
	private final String environment;
	private final String project;
	private final String headerAuth;
	private final String defaultUserPassword;
	private final String mainUser;
	private final String appiumUrl;
	private final String threadsCount;

	/**
	 * Constructor. Provides initialization of all class fields.
	 */
	public ConfigProvider() {
		try (InputStream propertyStream = ConfigProvider.class.getResourceAsStream("/selenium.properties")) {
			properties.load(propertyStream);
		} catch (IOException e) {
			throw new RuntimeException("An error occurred while loading selenium.properties", e);
		}
		devPublicSiteUrl = getConfigParameter("dev.public.site.url");
		qaPublicSiteUrl = getConfigParameter("qa.public.site.url");
		stagePublicSiteUrl = getConfigParameter("stage.public.site.url");
		prodPublicSiteUrl = getConfigParameter("prod.public.site.url");
		localPublicSiteUrl = getConfigParameter("local.public.site.url", "localhost");
		grid = getConfigParameter("selenium.grid", "localhost");
		appiumUrl = getConfigParameter("appium.url", "http://localhost:4723/wd/hub");
		port = getConfigParameter("selenium.grid.port", "5555");
		platformVersion = getConfigParameter("platform.version", "8.4");
		device = getConfigParameter("device", "iPhone 6");
		project = getConfigParameter("project", "rozetka");
		environment = getConfigParameter("environment", "prod");
		browser = getConfigParameter("selenium.browser");
		headerAuth = getConfigParameter("header.auth", "");
		mainUser = getConfigParameter("main.user", "");
		defaultUserPassword = getConfigParameter("user.password", "");
		threadsCount = getConfigParameter("thread.count", "1");
	}

	public String getAppiumUrl() {
		return appiumUrl;
	}

	public String getMainUserName() {
		return mainUser;
	}
	
	public String getDefaultUserPassword() {
		return defaultUserPassword;
	}

	public String getQaPublicSiteUrl() {
		return qaPublicSiteUrl;
	}
	
	public String getStagePublicSiteUrl() {
		return stagePublicSiteUrl;
	}
	
	public String getHeaderAuth() {
		return headerAuth;
	}

	public String getDevPublicSiteUrl() {
		return devPublicSiteUrl;
	}

	public String getProdPublicSiteUrl() {
		return prodPublicSiteUrl;
	}

	public String getLocalPublicSiteUrl() {
		return localPublicSiteUrl;
	}

	public String getBrowser() {
		return browser;
	}

	private String getConfigParameter(String key) {
		return getConfigParameter(key, null);
	}

	private String getConfigParameter(String key, String defaultValue) {
		String value = System.getProperty(key);
		if (value == null) {
			if (properties.getProperty(key) != null) {
				return properties.getProperty(key);
			} else if (defaultValue != null) {
				return defaultValue;
			}
			throw new RuntimeException("Configuration value not found for key '" + key + "'");
		}
		return value;
	}

	public String getProject() {
		return project;
	}

	public String getEnvironment() {
		return environment;
	}

	public String getGrid() {
		return grid;
	}

	public String getPort() {
		return port;
	}
	
	public String getPlatformVersion() {
		return platformVersion;
	}

	public String getDevice() {
		return device;
	}

	public String getThreadsCount() {
		return threadsCount;
	}

}
