package com.auto.pageObject.Chase;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class ChaseHomePage {

	@FindBy(how = How.ID, using = "searchfield")
	public static WebElement search;
	
	@FindBy(how = How.XPATH, using = ".//*[@id='swatsearchid']/img")
	public static WebElement clickSearch;
	
	@FindBy(how = How.CSS, using = "a[href*='/mobile-banking/apple-pay']")
	public static WebElement applePay;
		

}
