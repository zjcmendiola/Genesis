package GOMOLibrary;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.AndTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;

import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.testng.asserts.SoftAssert;

import com.perfectomobile.selenium.MobileElement;

import org.testng.Assert;
import utility.Constant;
import utility.Control;
import utility.Generic;
import utility.LaunchApp;

public class Dashboard {
	public static int PromoPrice;
	public static int IntPromoPrice;

	public static boolean loginMobileNumber(String mobileNumber, String PIN) throws Exception {
		try	{
			Control.customWait("Login", "LoginButton", 20);
			Assert.assertTrue(Control.objExists("Login", "LoginButton", true));  
			Control.takeScreenshot();
			Control.click("Login", "LoginButton");
			Control.customWait("Login", "PleaseEnterYourGOMOLabel", 20);
			Assert.assertTrue(Control.objExists("Login", "PleaseEnterYourGOMOLabel", true));  
			Control.takeScreenshot();
			Control.enterText("Login", "MobileNumberField", mobileNumber);
			Control.enterText("Login", "PINField", PIN);
			if(Constant.Platform.equals("Android")) {
				Control.keypadBackButton_android();
			}
			Control.takeScreenshot();
			Control.click("Login", "LoginButton");
			Control.customWait("Dashboard", "HomeNavigation", 60);
			Thread.sleep(5000);
			String formattedNumber = mobileFormat(mobileNumber);
			if(Constant.driver.getPageSource().contains(formattedNumber)) {
				Generic.WriteTestData("GOMO Login", "", "", "Should be able to login using GOMO number: " + formattedNumber, "Successfully login", "Pass");
				Control.takeScreenshot();
				return true;
			}
			else {
				Generic.WriteTestData("GOMO Login", "", "", "Should be able to login using GOMO number: " + formattedNumber, "Failed to login", "Fail");
				Control.takeScreenshot();
				return false;
			}
		}
		catch (Exception e) {
			Generic.WriteTestData("GOMO Login", "", "", "Should be able to login using GOMO number: " + mobileNumber, "Failed to login", "Fail");
			Control.takeScreenshot();
		  	e.printStackTrace();
		  	return false;
		}	
	}
	
	public static void logout() throws Exception {
		try	{
			Control.customWait("Dashboard", "AccountNavigation", 20);
			Assert.assertTrue(Control.objExists("Dashboard", "AccountNavigation", true));  
			Control.takeScreenshot();
			Control.click("Dashboard", "AccountNavigation");
			Control.customWait("Account", "LogoutLabel", 20);
			Assert.assertTrue(Control.objExists("Account", "LogoutLabel", true));  
			Control.takeScreenshot();
			Control.click("Account", "LogoutLabel");
			Control.customWait("Account", "LogoutConfirmationLabel", 20);
			Assert.assertTrue(Control.objExists("Account", "LogoutConfirmationLabel", true));  
			Control.takeScreenshot();
			Control.click("Account", "LogoutConfirmButton");
			Control.customWait("Login", "LoginButton", 20);
			if(Control.findElementWithoutPrintingException("Login", "LoginButton")!=null) {
				Generic.WriteTestData("GOMO Logout", "", "", "Should be able to logout", "Successfully logout", "Pass");
				Control.takeScreenshot();
			}
			else {
				Generic.WriteTestData("GOMO Logout", "", "", "Should be able to logout", "Failed to logout", "Fail");
				Control.takeScreenshot();
			}
		}
		catch (Exception e) {
			Generic.WriteTestData("GOMO Logout", "", "", "Should be able to logout", "Failed to logout", "Fail");
			Control.takeScreenshot();
		  	e.printStackTrace();
		}	
	}
	
	public static void goToMoCreds() throws Exception {
		try {
			Control.customWait("Dashboard", "MoCredsNavigation", 20);
			Assert.assertTrue(Control.objExists("Dashboard", "MoCredsNavigation", true));  
			Control.takeScreenshot();
			Control.click("Dashboard", "MoCredsNavigation");
			Thread.sleep(3000);
			if(Constant.Platform.equals("Android")) {
				Assert.assertTrue(Control.isElementSelected("Dashboard", "MoCredsNavigation", true));  
			}
			else {
				String isSelected = Control.getAttribute("Dashboard", "MoCredsNavigation", "value");
				if(isSelected.equals("1")) {
					Generic.WriteTestData("Mo Creds Navigation", "", "", "Should be able to arrived at MO Creds Page", "Successfully navigated", "Pass");
				}
				else {
					Generic.WriteTestData("Mo Creds Navigation", "", "", "Should be able to arrived at MO Creds Page", "Failed to navigate", "Fail");
				}
			}
			Control.takeScreenshot();		
		}
		catch (Exception e) {
			Generic.WriteTestData("Mo Creds Navigation", "", "", "Should be able to arrived at MO Creds Page", "Failed to navigate", "Fail");
			Control.takeScreenshot();
		  	e.printStackTrace();
		}	
	}
	
	public static void goToShop() throws Exception {
		try {
			Control.customWait("Dashboard", "ShopNavigation", 20);
			Assert.assertTrue(Control.objExists("Dashboard", "ShopNavigation", true));  
			Control.takeScreenshot();
			Control.click("Dashboard", "ShopNavigation");
			Thread.sleep(3000);
			if(Constant.Platform.equals("Android")) {
				Assert.assertTrue(Control.isElementSelected("Dashboard", "ShopNavigation", true));  
			}
			else {
				String isSelected = Control.getAttribute("Dashboard", "ShopNavigation", "value");
				if(isSelected.equals("1")) {
					Generic.WriteTestData("Shop Navigation", "", "", "Should be able to arrived at Shop Page", "Successfully navigated", "Pass");
				}
				else {
					Generic.WriteTestData("Shop Navigation", "", "", "Should be able to arrived at Shop Page", "Failed to navigate", "Fail");
				}
			}
			Control.takeScreenshot();		
		}
		catch (Exception e) {
			Generic.WriteTestData("Shop Navigation", "", "", "Should be able to arrived at Shop Page", "Failed to navigate", "Fail");
			Control.takeScreenshot();
		  	e.printStackTrace();
		}	
	}
	
	public static void getDashboardAvailableBalance() throws Exception {
		try {
			String attribute;
			if (Constant.Platform.equalsIgnoreCase("ios")) {
				attribute = "value";
			} else {
				attribute = "text";
			}
			Constant.Data = Control.getAttribute("Dashboard","DataHexagon",attribute);
			Generic.WriteTestData("Dashboard Available Data", "", "", "Should be able to retrieve available data", "Data: " + Constant.Data,"Pass");
			Control.takeScreenshot();
			Control.click("Dashboard", "CallHexagon");
			Constant.Call = Control.getAttribute("Dashboard","CallHexagon",attribute);
			Generic.WriteTestData("Dashboard Available Call", "", "", "Should be able to retrieve available call", "Call: " + Constant.Call,"Pass");
			Control.takeScreenshot();
			Constant.Text = Control.getAttribute("Dashboard","TextHexagon",attribute);
			Control.click("Dashboard", "TextHexagon");
			Generic.WriteTestData("Dashboard Available Text", "", "", "Should be able to retrieve available text", "Text: " + Constant.Text,"Pass");
			Control.takeScreenshot();
		}
		catch (Exception e) {
			Generic.WriteTestData("Dashboard Available Balance", "", "", "Should be able to retrieve available dashboard balance", "Failed to retrieve available balance","Pass");
			Control.takeScreenshot();
		  	e.printStackTrace();
		}
	}
	
	public static void postValidationMoCreds(String moCreds) throws Exception {
		try {
			String attribute;
			String[] tempSplit;
			String currentData, currentCall, currentText;
			String previousData, previousCall, previousText;
			double computedData;
			DecimalFormat df = new DecimalFormat("0.00");
			String price = Generic.ReadFromExcel(moCreds, "MoCreds", 1);
			tempSplit = price.split(" ");
			price = tempSplit[0];
			String category = Generic.ReadFromExcel(moCreds, "MoCreds", 2);
			if (Constant.Platform.equalsIgnoreCase("ios")) {
				attribute = "value";
			} else {
				attribute = "text";
			}
			currentData = Control.getAttribute("Dashboard","DataHexagon",attribute);
			tempSplit = Constant.Data.split(" ");
			previousData = tempSplit[0];
			computedData = Double.parseDouble(previousData) - Double.parseDouble(price);
			String finalData = df.format(computedData) + " GB";
			if (currentData.equals(finalData)) {
				Generic.WriteTestData("Data deduction", "", "", "Should have correct data deduction ("+finalData+")", "Successful data deduction", "Pass");
			} else {
				Generic.WriteTestData("Data deduction", "", "", "Should have correct data deduction ("+finalData+")", "Expected " + finalData + "data is not equal with " + currentData + "data on dashboard", "Fail");
			}
			Control.takeScreenshot();
			if(category.equals("Call & Text")) {
				Control.click("Dashboard", "CallHexagon");
				currentCall = Control.getAttribute("Dashboard","CallHexagon",attribute);
				tempSplit = Constant.Call.split(" ");
				previousCall = tempSplit[0];
				Control.takeScreenshot();
				Control.click("Dashboard", "TextHexagon");
				currentText = Control.getAttribute("Dashboard","TextHexagon",attribute);
				tempSplit = Constant.Text.split(" ");
				previousText = tempSplit[0];
				Control.takeScreenshot();
			}
		}
		catch (Exception e) {
			Generic.WriteTestData("Mo Creds Post Validation", "", "", "Should be able to retrieve available dashboard balance", "Failed to retrieve available balance","Pass");
			Control.takeScreenshot();
		  	e.printStackTrace();
		}
	}
	
	public static void postValidationPromoPurchase(String promo) throws Exception {
		try {
			if(promo.equals("UNLI DATA")) {
				String attribute;
				if(Constant.Platform.equalsIgnoreCase("ios")) {
					attribute = "label";
				}
				else {
					attribute = "content-desc";
				}	
				
				String getPromo = Control.getAttribute("Dashboard", "DataHexagonUnli", attribute);
				if(getPromo.contains("UNLI DATA")) {
					Generic.WriteTestData("Promo Purchase", "", "", "Should have Unli Data Promo ongoing", "With Unli Data", "Pass");
					Control.takeScreenshot();
				}
				else {
					Generic.WriteTestData("Promo Purchase", "", "", "Should have Unli Data Promo ongoing", "No Unli Data", "Fail");
					Control.takeScreenshot();
				}
			}
			else {
				String attribute;
				String[] tempSplit;
				String currentData;
				String previousData;
				double computedData;
				DecimalFormat df = new DecimalFormat("0.00");
				tempSplit = promo.split("GB");
				promo = tempSplit[0];
				if (Constant.Platform.equalsIgnoreCase("ios")) {
					attribute = "value";
				} else {
					attribute = "text";
				}
				currentData = Control.getAttribute("Dashboard","DataHexagon",attribute);
				tempSplit = Constant.Data.split("GB");
				previousData = tempSplit[0];
				computedData = Double.parseDouble(previousData) + Double.parseDouble(promo);
				String finalData = df.format(computedData) + " GB";
				if (currentData.equals(finalData)) {
					Generic.WriteTestData("Promo Purchase Post Validation", "", "", "Should have correct data ("+finalData+")", "Successful data deduction", "Pass");
					Control.takeScreenshot();
				} else {
					Generic.WriteTestData("Promo Purchase Post Validation", "", "", "Should have correct data ("+finalData+")", "Expected " + finalData + "data is not equal with " + currentData + "data on dashboard", "Fail");
					Control.takeScreenshot();
				}
			}
		}
		catch (Exception e) {
			Generic.WriteTestData("Promo Purchase Post Validation", "", "", "Should be able to add available dashboard balance", "Failed to retrieve available balance","Pass");
			Control.takeScreenshot();
		  	e.printStackTrace();
		}
	}
	
	public static String mobileFormat(String MSISDN) throws Exception {
		String[] mobile = MSISDN.split("");
		String mobileFormat = "+63 " + mobile[0]+mobile[1]+mobile[2] + " " + mobile[3]+mobile[4]+mobile[5] + " " + mobile[6]+mobile[7]+mobile[8]+mobile[9];
		System.out.println(mobileFormat);
		return mobileFormat;
	}
	
}
