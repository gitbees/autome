package com.test.stepDefs;

import gherkin.formatter.model.Feature;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.Properties;

import com.auto.constants.CukeConstants;
import com.auto.util.BrowserDriver;
import com.auto.util.Page;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CommonStepDefs extends TestCase {

	public static Scenario scenario;

	public static Feature feature;

	private static final Logger LOGGER = Logger.getLogger(CommonStepDefs.class
			.getName());

	static Object currentPage = null;
	static Object pageObj = null;
	static String pageName = null;

	// static Object o = null;

	static Class pageObjectClass = null;
	static String pkg = null;

	static String currentUser = null;
	static String currentPassword = null;
	static String currentRole = null;
	static String currentEnvRegion = null;

	String valueFromApplication = null;

	static Object o = null;

	@Before
	public void before(Scenario scenario) {
		this.scenario = scenario;
	}

	public static String getPageUrl() throws Exception {

		Properties pageinfoprops = new Properties();
		InputStream in = CukeConstants.class.getClassLoader()
				.getResourceAsStream("pageinfo.properties");
		pageinfoprops.load(in);

		String expectedUrl = pageinfoprops.getProperty(pageName + ".url");
		LOGGER.info("This is Page: " + pageName);

		return expectedUrl;

	}

	public static void pageIsLoaded() throws Exception {

		String expectedUrl;
		initializeClass();

		Object obj = pageObjectClass.newInstance();
		// Object obj = pageObjectClass.getClass();

		Class[] argTypes = new Class[] { String[].class };

		Method poMethod = pageObjectClass.getDeclaredMethod("isLoaded",
				String.class);

		LOGGER.info("This is Page: " + pageName);

	}

	public static String getPageTitle() throws Exception {

		initializeClass();

		String thisTitleValue = pageName + "TITLE";
		String expectedTitle = (String) CukeConstants.testdataprops
				.get(thisTitleValue);

		LOGGER.info("This is Expected Page Title : " + expectedTitle);
		return expectedTitle;

	}

	// Should this be in SuperClass BasicPage?
	public static void atExpectedPage() throws Exception {

		String currentUrl = BrowserDriver.getCurrentDriver().getCurrentUrl();
		LOGGER.info("This is Current Page URl: " + currentUrl);
		assertEquals(getPageUrl(), currentUrl);
		// return getDriver().getTitle().contentEquals("Google");

		String currentTitle = BrowserDriver.getCurrentDriver().getTitle();
		LOGGER.info("This is Current Page Title: " + currentTitle);
		assertEquals(getPageTitle(), currentTitle);
	}

	public static void goToUrl() throws Exception {
		initializeClass();
		BrowserDriver.loadPage(getPageUrl());
	}

	@Given("^I navigate to the ([^\"]*) site$")
	public void logged_in_via_bypass(String site) throws Throwable {

		Properties pageinfoprops = new Properties();
		InputStream in = CommonStepDefs.class.getClassLoader()
				.getResourceAsStream("testdata.properties");
		pageinfoprops.load(in);

		String expectedUrl = pageinfoprops.getProperty(site + ".url");

		BrowserDriver.loadPage(expectedUrl);

		Page.refresh();

	}

	@Given("^I am on the ([^\"]*) page$")
	public static void on_the_page(String pageValue) throws Throwable {

		pageName = pageValue.replaceAll(" ", "") + "Page";
		pkg = PackageInfo.getDescription(pageName);

		// WebDriver wb = new HtmlUnitDriver();
		currentPage = PageFactory.initElements(
				BrowserDriver.getCurrentDriver(),
				Class.forName(pkg + "." + pageName));

		LOGGER.info("This is My Current Page Class :"
				+ currentPage.getClass().getName());

		initializeClass();

		// //TBD based on gathering page URL's
		/*
		 * pageIsLoaded();
		 * 
		 * atExpectedPage();
		 */
	}

	@When("^I choose the ([^\"]*) (?:drop down|check box|radio button) with ([^\"]*) value$")
	public static void choose_value_from_many_values(String elementNm,
			String value) throws Throwable {

		WebElement webElement = getElement(elementNm);
		ElementWait();
		new Select(webElement).selectByValue(value);
	}

	@When("^I select the ([^\"]*) (?:radio button) with ([^\"]*) value$")
	public static void choose_radio_values(String elementNm, String value)
			throws Throwable {
		int elementIndex = 0;
		WebElement webElements = getElement(elementNm);
		WebElement webElement = webElements.findElement(By.xpath("//*[@value='"
				+ value + "']"));
		webElement.click();
	}

	// Link & Button - Sub Section

	@When("^I select the ([^\"]*) (?:link|button)$")
	public static void select_element(String elementNm) throws Throwable {

		WebElement webElement = getElement(elementNm);

		ElementWait();
		webElement.click();

	}

	// Radio Button & Check box - Sub Section

	// When I choose the rb Recipient radio button
	// When I choose the Option 1 check box
	@When("^I choose the ([^\"]*) (?:radio button|check box)$")
	// TODO: <Santhosh to Venkat> Please remove this :public static void
	// choose_radio_check(int elementIndex, String elementNm,String value)
	// throws Throwable {
	public static void choose_radio_check(String elementNm) throws Throwable {
		select_element(elementNm);
		// TODO: Remove select_element(elementNm);

	}

	// When I choose the 4th card radio button
	// When I choose the 3rd Money Types check box
	@When("^I choose the (\\d+)(?:st|nd|rd|th) ([^\"]*) (?:radio button|check box)$")
	public static void choose_radio_button_by_index(int elementIndex,
			String elementNm) throws Throwable {
		initializeClass();
		WebElement webElements = getElement(elementNm);

		List<WebElement> ElementsGroup = (List<WebElement>) new Select(
				webElements);

		ElementsGroup.get(elementIndex).click();

	}

	// Drop Down - Sub Section

	@When("^I (?:choose|should see) ([^\"]*) (?:from the|in the) ([^\"]*) drop down$")
	public static void choose_text_from_the_drop_down(String value,
			String elementNm) throws Throwable {

		initializeClass();
		WebElement webElement = getElement(elementNm);

		Select select = new Select(webElement);
		select.selectByVisibleText(value);

	}

	@Then("^I should not see ([^\"]*) in the ([^\"]*) dropdown$")
	public static void validate_text_from_the_drop_down(String value,
			String elementNm) throws Throwable {

		initializeClass();

		try {
			WebElement webElement = getElement(elementNm);

			Select select = new Select(webElement);
			select.selectByVisibleText(value);

		} catch (NoSuchElementException e) {
			assertFalse(false);
		}

	}

	// When I choose the Recipient radio button with 2587501 value
	@Then("^I should see the ([^\"]*) selected in the ([^\"]*) dropdown$")
	public static void drop_selected(String value, String elementNm)
			throws Throwable {

		WebElement webElement = getElement(elementNm);
		ElementWait();
		new Select(webElement).selectByValue(value);
		assertTrue(true);
	}

	// Text Field - Sub Section

	// When I enter Admin for the User Id field
	// When I enter Hi Abc for the Wire Amount field
	@When("^I enter ([^\"]*) for the ([^\"]*) (?:text field|field)$")
	public static void enter_value_for_the_field(String value, String elementNm)
			throws Throwable {
		initializeClass();
		WebElement webElement = getElement(elementNm);

		webElement.clear();
		webElement.sendKeys(value);

	}

	@When("^I (?:choose|select) the ([^\"]*) (?:link|button|radio button|check box) from ([^\"]*) (?:menu|options|page)$")
	public static void select_from_mega_menu(String elementName, String pageName)
			throws Throwable {

		on_the_page(pageName);
		select_element(elementName);

	}

	// === THEN SECTION == ASSERTION CONDITIONS

	// Page - Sub Section

	// public void validate_frequently_text() throws Throwable {
	// Then I should see My Information text on the page
	@Then("I should see \"([^\"]*)\" text on the page$")
	public void should_see_text_on_the_page(String expectedValue)
			throws Throwable {

		assertEquals(true, Page.isTextPresent(expectedValue));

	}

	// Then I should see the Bypass SecAuth page
	@Then("^I should see the ([^\"]*) page$")
	public static void see_the_page(String pageValue) throws Throwable {

		on_the_page(pageValue);

	}

	// Then I should see the Display the "Account Value" and "Change" columns
	// text on the page
	@Then("^I should see the (.*) text on the page$")
	public void should_see_text_on_page_no_qoutes(String expectedValue)
			throws Throwable {

		assertEquals(true, Page.isTextPresent(expectedValue));

	}

	@Then("^the ([^\"]*) (?:link|button|radio button|drop down|check box) should be displayed$")
	public void element_displayed(String elementNm) throws Throwable {

		WebElement webelement = getElement(elementNm);

		assertTrue(webelement.isDisplayed());

	}

	// @When("^I should see the ([^\"]*) (?:text field) with ([^\"]*) text$")

	@Then("^I should see the ([^\"]*) (?:link|button|radio button|drop down|check box|field)$")
	public void see_element(String elementNm) throws Throwable {
		element_displayed(elementNm);

	}

	@Then("^the ([^\"]*) (?:link|button|radio button|drop down|check box) should not be displayed$")
	public void element_not_be_displayed(String elementNm) throws Throwable {

		element_not_displayed(elementNm);

	}

	@Then("^I should not see the ([^\"]*) (?:link|button|radio button|drop down|check box|field)$")
	public void element_not_displayed(String elementNm) throws Throwable {

		try {
			WebElement webelement = getElement(elementNm);
			ElementWait();
		} catch (NoSuchElementException e) {
			assertFalse(false);
		}

	}

	// Then the Payments Transfers link should be enabled
	// Then the Ok button should be enabled
	@Then("^the ([^\"]*) (?:link|button|radio button|drop down|check box) should be enabled$")
	public static void element_enabled(String elementNm) throws Throwable {

		WebElement webelement = getElement(elementNm);

		assertEquals(true, webelement.isEnabled());

	}

	@Then("^the ([^\"]*) (?:link|button|radio button|drop down|check box) is disabled$")
	public static void element_disabled(String elementNm) throws Throwable {

		WebElement webelement = getElement(elementNm);

		assertEquals(false, webelement.isEnabled());

	}

	@Then("^the ([^\"]*) (?:link|radio button|check box) is unselected$")
	public void element_unselected(String elementNm) throws Throwable {

		WebElement webelement = getElement(elementNm);

		assertEquals(false, webelement.isSelected());

	}

	@Then("^the ([^\"]*) (?:radio button|check box) is selected$")
	public void element_selected(String elementNm) throws Throwable {

		WebElement webelement = getElement(elementNm);

		assertEquals(true, webelement.isSelected());

	}

	@Then("I should not see the ([^\"]*) text")
	public void should_not_see_text(String expectedValue) throws Throwable {

		assertEquals(false, Page.isTextPresent(expectedValue));

	}

	// Then I print text of the Transaction Number element by getText
	@Then("^I print text of the ([^\"]*) element by getText$")
	public void i_print_text_of_the_element(String elementNm) throws Throwable {

		WebElement webelement = getElement(elementNm);
		scenario.write("Web Element: " + elementNm + " has getText value: "
				+ webelement.getText());

		valueFromApplication = webelement.getText().trim();

	}

	// @Then("^I should see dondelmfpp(\\d+) value of the Label User Id using getText$"
	// Then I should see dondelmfpp211 value of the Label User Id using getText
	@Then("^I should see ([^\"]*) value of the ([^\"]*) using getText$")
	public void verify_text_of_the_element_by_gettext(String value,
			String elementNm) throws Throwable {

		WebElement webelement = getElement(elementNm);

		assertEquals(value, webelement.getText());

	}

	@Then("^I should see a New Browser window$")
	public void switchToNewlyOpenedWindow() {
		// get the handle of latest window
		String oldWinHandle = BrowserDriver.getCurrentDriver()
				.getWindowHandle();
		String latestWinHandle = null;
		for (String winHandle : BrowserDriver.getCurrentDriver()
				.getWindowHandles())
			latestWinHandle = winHandle;
		BrowserDriver.getCurrentDriver().switchTo().window(latestWinHandle);
		assertNotNull(latestWinHandle);
		// return oldWinHandle;
	}

	@Then("^I should see a New Popup window$")
	public void switchToNewPopup() throws InterruptedException {

		for (String winHandle : BrowserDriver.getCurrentDriver()
				.getWindowHandles()) {
			BrowserDriver.getCurrentDriver().switchTo().window(winHandle);
			String latestWinHandle = winHandle;
			BrowserDriver.getCurrentDriver().switchTo().window(latestWinHandle);
		}

		Thread.sleep(2000);

	}

	@Then("^I should validate ([^\"]*) in pdf$")
	public void validate_data_in_statement_pdf(String content) throws Throwable {
		/*
		 * boolean[] b = Page.checkPDF(STMNTS.ASSERT_STMNTS_TEXT1,
		 * STMNTS.ASSERT_STMNTS_TEXT2, STMNTS.ASSERT_STMNTS_TEXT3);
		 * assertTrue(b[0]); assertFalse(b[1]); assertTrue(b[2]);
		 */

		boolean[] b = Page.checkPDF(content);
		assertTrue(b[0]);

	}

	private static void initializeClass() throws Exception {

		pkg = PackageInfo.getDescription(pageName);

		pageObjectClass = Class.forName(pkg + "." + pageName);

		System.out.println("This is Current Class: "
				+ pageObjectClass.getName());
	}

	public static WebElement getElement(String elementNm)
			throws InstantiationException, IllegalAccessException,
			SecurityException, NoSuchFieldException {

		String elementName = elementNm.replaceAll(" ", "");
		String element = elementName.substring(0, 1).toLowerCase()
				+ elementName.substring(1);

		System.out.println("This is Current Element: " + element);

		System.out.println("This is Current Class for Element: "
				+ pageObjectClass.getName());

		Field field = pageObjectClass.getField(element);

		System.out.println("This is current Field: " + field.getName());

		field.setAccessible(true);

		WebElement webElement = (WebElement) field.get(element);

		return webElement;

	}

	public static void ElementWait() {
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@After
	public void AfterTest(Scenario scenario) {
		Page.captureScreenshot(scenario);
	}

	public static void main(String[] args) throws Throwable {

	}

}
