package com.auto.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.Select;

import cucumber.api.Scenario;

public class Page {

	private static final Logger LOGGER = Logger.getLogger(Page.class.getName());

	private static final String SERVER_KEY = System.getProperty("server");

	public static void ClickLinkById(String id) throws WebDriverException {

		BrowserDriver.getElementById(id).click();

	}

	public static void ClickLinkByName(String name) throws WebDriverException {

		BrowserDriver.getElementByName(name).click();

	}

	public static void ClickLinkByXpath(String xpath) throws WebDriverException {
		BrowserDriver.getElementByXpath(xpath).click();

	}

	public static void ClickLinkByText(String text) throws WebDriverException {
		BrowserDriver.getElementByText(text).click();

	}

	public static void ClickLinkByPartialText(String partialText)
			throws WebDriverException {
		BrowserDriver.getElementByPartialLinkText(partialText).click();

	}

	public static void ClearById(String id) throws WebDriverException {
		BrowserDriver.getElementById(id).clear();

	}

	public static void ClearByName(String name) throws WebDriverException {
		BrowserDriver.getElementByName(name).clear();

	}

	public static void ClearByXpath(String xpath) throws WebDriverException {
		BrowserDriver.getElementByXpath(xpath).clear();

	}

	public static void InputById(String id, String value)
			throws WebDriverException {
		BrowserDriver.getElementById(id).sendKeys(value);

	}

	public static void InputByName(String name, String value)
			throws WebDriverException {
		BrowserDriver.getElementByName(name).sendKeys(value);

	}

	public static void InputByXpath(String xpath, String value)
			throws WebDriverException {
		BrowserDriver.getElementByXpath(xpath).sendKeys(value);

	}

	public static void dropTextById(String id, String visibleText)
			throws WebDriverException {
		new Select(BrowserDriver.getElementById(id))
				.selectByVisibleText(visibleText);
	}

	public static void dropTextByName(String name, String visibleText)
			throws WebDriverException {
		new Select(BrowserDriver.getElementByName(name))
				.selectByVisibleText(visibleText);
	}

	public static void dropTextByXpath(String xpath, String visibleText)
			throws WebDriverException {
		new Select(BrowserDriver.getElementByXpath(xpath))
				.selectByVisibleText(visibleText);
	}

	public static void dropValueById(String id, String value)
			throws WebDriverException {
		new Select(BrowserDriver.getElementById(id)).selectByValue(value);
	}

	public static void dropValueByName(String name, String value)
			throws WebDriverException {
		new Select(BrowserDriver.getElementByName(name)).selectByValue(value);
	}

	public static void dropValueByXpath(String xpath, String value)
			throws WebDriverException {
		new Select(BrowserDriver.getElementByXpath(xpath)).selectByValue(value);
	}

	public static void clickByCss(String selector) throws WebDriverException {
		BrowserDriver.getElementByCss(selector).click();

	}

	public static void refresh() {
		BrowserDriver.getCurrentDriver().navigate().refresh();
	}

	public static boolean isTextPresent(String text) {

		boolean b = BrowserDriver.getCurrentDriver().getPageSource()
				.contains(text);

		return b;
	}

	public static boolean isElementExistById(String id) {
		try {
			BrowserDriver.getCurrentDriver().findElement(By.id(id));
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}

	public static boolean isElementExistByXpath(String xpath) {
		try {
			BrowserDriver.getCurrentDriver().findElement(By.xpath(xpath));
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}

	public static boolean isLinkExist(String linkText) {
		try {
			BrowserDriver.getCurrentDriver().findElement(By.linkText(linkText));
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}

	public static void scrollMidPage() {

		JavascriptExecutor jse = (JavascriptExecutor) BrowserDriver
				.getCurrentDriver();
		jse.executeScript("window.scrollBy(0,400)", "");
	}

	public static void scrollEnd() {

		JavascriptExecutor jse = (JavascriptExecutor) BrowserDriver
				.getCurrentDriver();
		jse.executeScript("window.scrollBy(0,1000)", "");
	}

	public static void captureScreenshot(Scenario scenario) {
		if (scenario.isFailed()) {

			if (SERVER_KEY != null && SERVER_KEY.equalsIgnoreCase("remote")) {
				WebDriver augmentedDriver = new Augmenter()
						.augment(BrowserDriver.getCurrentDriver());
				byte[] screenshot = ((TakesScreenshot) augmentedDriver)
						.getScreenshotAs(OutputType.BYTES);
				scenario.embed(screenshot, "image/png");

			} else if (SERVER_KEY == null
					|| SERVER_KEY.equalsIgnoreCase("local")) {
				try {

					byte[] screenshot = ((TakesScreenshot) BrowserDriver
							.getCurrentDriver())
							.getScreenshotAs(OutputType.BYTES);
					scenario.embed(screenshot, "image/png");
				} catch (WebDriverException wde) {
					System.err.println(wde.getMessage());
				} catch (ClassCastException cce) {
					cce.printStackTrace();
				}
			}
		}
	}

	public static boolean checkPDFContent(String... checkValue)
			throws IOException {

		for (String winHandle : BrowserDriver.getCurrentDriver()
				.getWindowHandles()) {
			BrowserDriver.getCurrentDriver().switchTo().window(winHandle);
		}

		boolean flag = false;
		URL url = new URL(BrowserDriver.getCurrentDriver().getCurrentUrl());
		// URLConnection yc = url.openConnection();
		// yc.setReadTimeout(1000);
		// yc.setConnectTimeout(1000);
		BufferedInputStream fileToParse = new BufferedInputStream(
				url.openStream());
		PDFParser parser = new PDFParser(fileToParse);
		try {
			parser.parse();
		} catch (IOException io) {
			io.printStackTrace();
			// parser.
		}
		String output = new PDFTextStripper().getText(parser.getPDDocument());
		int len = checkValue.length;
		for (int i = 0; i <= (len - 1); i++) {
			if (output.contains(checkValue[i])) {
				LOGGER.info((checkValue[i] + " is present in the PDF file"));
				flag = true;
			} else {
				LOGGER.info((checkValue[i] + " is not present in the PDF file"));
				flag = false;
			}
		}

		parser.getPDDocument().close();
		return flag;
	}

	public static boolean[] checkPDF(String... checkValue) throws IOException,
			InterruptedException {

		boolean[] flag = new boolean[100];

		for (String winHandle : BrowserDriver.getCurrentDriver()
				.getWindowHandles()) {
			BrowserDriver.getCurrentDriver().switchTo().window(winHandle);
		}

		BrowserDriver.getCurrentDriver().manage().window().maximize();

		
		saveStatement();
		PDDocument doc = PDDocument.load("C:/temp/pdf/Statement.pdf");
		PDFTextStripper stripper = new PDFTextStripper();
		String output = stripper.getText(doc);
		int len = checkValue.length;
		for (int i = 0; i <= (len - 1); i++) {
			if (output.contains(checkValue[i])) {
				LOGGER.info((checkValue[i] + " is present in the PDF file"));
				flag[i] = true;
			} else {
				LOGGER.info((checkValue[i] + " is not present in the PDF file"));
				flag[i] = false;
			}
		}

		doc.close();
		deleteFiles();
		return flag;
	}

	public static void deleteFiles() {

		String files;
		File file = new File("C:\\temp\\pdf");
		file.exists();
		File[] listOfFiles = file.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				files = listOfFiles[i].getName();
				System.out.println(files);
				if (!files.equalsIgnoreCase(".pdf")) {
					boolean issuccess = new File(listOfFiles[i].toString())
							.delete();
					System.err.println("Deletion Success " + issuccess);
				}
			}
		}
	}

	public static void saveStatement() throws IOException, InterruptedException {
		deleteFiles();
		File f = new File("/autoit-v3/saveStatement.exe");
		System.out.println(f.getName());
		System.out.println(f.getAbsolutePath());
		Thread.sleep(9000);
		Process p = Runtime.getRuntime().exec("C:/autoit-v3/saveStatement.exe");
		p.waitFor();

	}

	public static int getFile() {
		File file = new File("C:\\temp\\pdf");
		int i = file.list().length;
		return i;
	}
}
