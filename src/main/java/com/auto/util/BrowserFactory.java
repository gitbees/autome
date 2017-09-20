package com.auto.util;

import java.awt.Toolkit;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.auto.constants.Browsers;

public class BrowserFactory {

	// private static final String BROWSER_PROP_KEY =
	// CukeConstants.TEST_DATA.BROWSER;

	private static final String BROWSER_PROP_KEY = System
			.getProperty("browser");

	private static final String SERVER_KEY = System.getProperty("server");

	private static final String HOST = System.getProperty("host");

	private static final String PORT = System.getProperty("port");

	private static final Logger LOGGER = Logger.getLogger(BrowserFactory.class
			.getName());

	/**
	 * creates the browser driver specified in the system property "browser" if
	 * no property is set then a firefox browser driver is created. The allow
	 * properties are firefox, ie and chrome e.g to run with firefox, pass in
	 * the option -Dbrowser=ff at runtime
	 * 
	 * @return WebDriver
	 * @throws MalformedURLException
	 */
	public static WebDriver getBrowser() {
		Browsers browser;
		WebDriver driver = null;

		if (SERVER_KEY != null && SERVER_KEY.equalsIgnoreCase("remote")) {
			try {
				driver = createRemoteDriver();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else if (SERVER_KEY == null || SERVER_KEY.equalsIgnoreCase("local")) {

			if (BROWSER_PROP_KEY == null) {
				browser = Browsers.FF;
			} else {
				browser = Browsers.browserForName(BROWSER_PROP_KEY);
			}
			// LOGGER.info("Launchin the browser: " +browser);
			switch (browser) {
			case HD:
				driver = intiateHtmlUnit();
				break;
			case CH:
				driver = initiateChrome();
				break;
			case IE:
				driver = initiateInternetExplorer();
				break;
			case SF:
				driver = intiateSafari();
				break;
			case FF:
			default:
				driver = createFirefoxDriver(getFirefoxProfile());
				break;
			}
		}
		addAllBrowserSetup(driver);
		return driver;
	}

	public static WebDriver initiateInternetExplorer() {

		File file = new File("C:\\driver\\IEDriverServer.exe");
		System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
		DesiredCapabilities ieCapabilities = DesiredCapabilities
				.internetExplorer();
		ieCapabilities
				.setCapability(
						InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
						true);
		ieCapabilities.setBrowserName("iexplore");
		ieCapabilities.setPlatform(org.openqa.selenium.Platform.WINDOWS);

		ieCapabilities.setCapability(
				InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
		ieCapabilities.setCapability(
				InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, true);
		ieCapabilities.setCapability(
				InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, true);
		return new InternetExplorerDriver(ieCapabilities);

	}

	public static WebDriver initiateChrome() {
		File file = new File("C:\\driver\\chromedriver.exe");
		System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
		return new ChromeDriver();

	}

	public static WebDriver intiateHtmlUnit() {
		return new HtmlUnitDriver();

	}

	private static boolean isSupportedPlatform() {
		Platform current = Platform.getCurrent();
		return Platform.MAC.is(current) || Platform.WINDOWS.is(current);
	}

	public static WebDriver intiateSafari() {
		return new SafariDriver();
	}

	private static WebDriver createFirefoxDriver(FirefoxProfile firefoxProfile) {
		DesiredCapabilities ieCapabilities = DesiredCapabilities.firefox();
		ieCapabilities.setBrowserName("firefox");
		ieCapabilities.setPlatform(org.openqa.selenium.Platform.ANY);
		return new FirefoxDriver(firefoxProfile);
	}

	private static FirefoxProfile getFirefoxProfile() {
		FirefoxProfile firefoxProfile = new FirefoxProfile();
		return firefoxProfile;
	}

	public static WebDriver createRemoteDriver() throws MalformedURLException {

		DesiredCapabilities capability = null;
		String browser = null;

		if (BROWSER_PROP_KEY == null) {
			browser = Browsers.FF.toString();
		} else {
			browser = BROWSER_PROP_KEY;
		}
		if (browser.equalsIgnoreCase("ff")) {
			capability = DesiredCapabilities.firefox();
			capability.setBrowserName("firefox");
			capability.setPlatform(org.openqa.selenium.Platform.ANY);
			System.setProperty("https.proxyHost", "proxy.jpmchase.net");
			System.setProperty("https.proxyPort", "8443");
		} else if (browser.equalsIgnoreCase("ie")) {
			File file = new File("C:\\driver\\IEDriverServer.exe");
			System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
			System.out.println("iexplore");
			capability = DesiredCapabilities.internetExplorer();
			capability.setBrowserName("iexplore");
			capability.setCapability(
					InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
			capability.setVersion("");
			capability.setCapability(
					InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, true);
			capability.setCapability(
					InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, true);
			capability
					.setCapability(
							InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
							true);
			capability.setPlatform(org.openqa.selenium.Platform.WINDOWS);
			System.setProperty("https.proxyHost", "proxy.jpmchase.net");
			System.setProperty("https.proxyPort", "8443");
		} else if (browser.equalsIgnoreCase("ch")) {
			capability = DesiredCapabilities.chrome();
			capability.setBrowserName("chrome");
			capability.setPlatform(org.openqa.selenium.Platform.ANY);
			System.setProperty("https.proxyHost", "proxy.jpmchase.net");
			System.setProperty("https.proxyPort", "8443");
		}

		else if (browser.equalsIgnoreCase("sf")) {

			capability = DesiredCapabilities.safari();

			capability.setBrowserName("safari");
		}

		WebDriver driver = new RemoteWebDriver(new URL("http://" + HOST
				+ ":4444/wd/hub"), capability);
		return driver;

	}

	private static void addAllBrowserSetup(WebDriver driver) {
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(90, TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
		driver.manage().window().setPosition(new Point(0, 0));
		java.awt.Dimension screenSize = Toolkit.getDefaultToolkit()
				.getScreenSize();
		Dimension dim = new Dimension((int) screenSize.getWidth(),
				(int) screenSize.getHeight());
		driver.manage().window().setSize(dim);
	}

}
