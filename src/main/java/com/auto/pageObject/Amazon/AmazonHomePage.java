package com.auto.pageObject.Amazon;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class AmazonHomePage {
	
	@FindBy(how = How.ID, using = "twotabsearchtextbox")
	public static WebElement search;
	
	@FindBy(how = How.XPATH, using = ".//*[@class='nav-submit-input'][@type='submit'][@title='Go']")
	public static WebElement go;

}
