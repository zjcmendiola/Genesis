package GOMOLibrary;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import utility.Constant;
import utility.Control;
import utility.Generic;

public class Shop {

	public static void goToDataOffer() throws Exception {
		try	{
			Control.customWait("Shop", "SeeAllDataOffer", 20);
			Control.takeScreenshot();
			Control.click("Shop", "SeeAllDataOffer");
		}
		catch (Exception e) {
			Control.takeScreenshot();
		  	e.printStackTrace();
		}
	}
	
	public static void selectCategoryTab(String category) throws Exception {
		try	{
			switch(category) {
			case "All":
				Control.click("Shop", "AllTab");
				break;
			case "Data Only":
				Control.click("Shop", "DataOnlyTab");
				break;
			case "Unli Data":
			case "UNLI Data":
				Control.click("Shop", "UnliDataTab");
				break;
			case "Data Bundle":
				Control.click("Shop", "SeeAllDataOffer");
				break;
			}
			Control.takeScreenshot();
		}
		catch (Exception e) {
			Control.takeScreenshot();
		  	e.printStackTrace();
		}
	}
	
	public static void selectOffer(String offer) throws Exception {
		try	{
			Map<String, Object> params = new HashMap<>();
			int i=10;
			String elementOffer = "//*[contains(@text,\""+ offer + "\")] | //*[contains(@value,\""+ offer + "\")]";
			Constant.Map.get("Shop").put(offer, "xpath#"+elementOffer);
	        params.put("start","40%,60%");
	        params.put("end","40%,20%");
			while(!Constant.driver.getPageSource().contains(offer))
			{
		        params.put("duration","3");
		        Constant.driver.executeScript("mobile:touch:swipe",params);
				i--;
				if(i==0) {	
					break;
				}
			}
			try {
				Assert.assertTrue(Control.objExists("Shop", offer, true)); 
				Control.takeScreenshot();
				Control.click("Shop", offer);
				Control.customWait("Shop", "ProceedToPaymentButton", 20);
				Assert.assertTrue(Control.objExists("Shop", "ProceedToPaymentButton", true)); 	
				Control.takeScreenshot();	
			}
			catch(Exception e) {
				Constant.driver.executeScript("mobile:touch:swipe",params);
				Control.click("Shop", offer);
				Control.customWait("Shop", "ProceedToPaymentButton", 20);
				Assert.assertTrue(Control.objExists("Shop", "ProceedToPaymentButton", true)); 	
				Control.takeScreenshot();
			}			
		}
		catch (Exception e) {
			Generic.WriteTestData("Select Offer", "", "", "Should be able to select: " + offer, "Failed to select", "Fail");
			Control.takeScreenshot();
		  	e.printStackTrace();
		}
	}
	
	public static void proceedPayment(String offer) throws Exception {
		try	{
			String price = Generic.ReadFromExcel(offer, "Shop", 1);
			String[] offerTrim = offer.split(" ");
			String elementOffer = "//*[contains(@text,\""+ offerTrim[0] + "\")] | //*[contains(@value,\""+ offerTrim[0] + "\")]";
			String elementPrice = "//*[contains(@text,\""+ price + "\")] | //*[contains(@value,\""+ price + "\")]";
			Constant.Map.get("Shop").put(offerTrim[0], "xpath#"+elementOffer);
			Constant.Map.get("Shop").put(price, "xpath#"+elementPrice);
			Control.objExists("Shop", offerTrim[0], true);
			Control.objExists("Shop", price, true);
			Control.takeScreenshot();
			Control.click("Shop", "ProceedToPaymentButton");
			Control.customWait("PaymentMethod", "PaymentHeader", 20);
			Assert.assertTrue(Control.objExists("PaymentMethod", "PaymentHeader", true));
			Control.takeScreenshot();
		}
		catch (Exception e) {
			Generic.WriteTestData("Proceed to payment", "", "", "Should be able to purchase: " + offer, "Failed to purchase", "Fail");
			Control.takeScreenshot();
		  	e.printStackTrace();
		}
	}
	
	public static void paymentMethod(String account) throws Exception {
		try	{
			String paymentType = Generic.ReadFromExcel(account, "PaymentDetails", 4);
			switch(paymentType) {
			case "GCash":
				Control.click("PaymentMethod","GcashButton");
				Control.takeScreenshot();
				Control.click("PaymentMethod","PayButton");
				gcashPayment(account);
				break;
			case "CreditCard":
			case "DebitCard":
				Control.click("PaymentMethod","CreditCardButton");
				Control.takeScreenshot();
				Control.click("PaymentMethod","PayButton");
				Control.takeScreenshot();
				Control.customWait("PaymentMethod", "CreditCardMethod", 20);
				Control.click("PaymentMethod","CreditCardMethod");
				cardPayment(account);
				break;
			}
		}
		catch (Exception e) {
			Control.takeScreenshot();
		  	e.printStackTrace();
		}
	}
	
	public static void gcashPayment(String account) throws Exception {
		String mobileNumber = Generic.ReadFromExcel(account, "PaymentDetails", 1);
		String mpin = Generic.ReadFromExcel(account, "PaymentDetails", 2);
		try	{
			Control.customWait("GCash", "GcashHeader", 60);
			Assert.assertTrue(Control.objExists("GCash", "GcashHeader", true));
			Control.enterText("GCash", "MobileNumberField", mobileNumber);
			Control.takeScreenshot();
			Control.click("GCash","NextButton");
		}
		catch (Exception e) {
			Control.takeScreenshot();
		  	e.printStackTrace();
		}
	}
	
	public static void cardPayment(String account) throws Exception {
		String number = Generic.ReadFromExcel(account, "PaymentDetails", 1);
		String expiryDate = Generic.ReadFromExcel(account, "PaymentDetails", 2);
		String cvc = Generic.ReadFromExcel(account, "PaymentDetails", 3);
		try	{
			Control.customWait("CreditCard", "CreditCardHeader", 20);
			Assert.assertTrue(Control.objExists("CreditCard", "CreditCardHeader", true));
			Control.enterText("CreditCard", "CardNumberField", number);
			Control.enterText("CreditCard", "ExpiryDateField", expiryDate);
			Control.enterText("CreditCard", "CvcField", cvc);
			Control.takeScreenshot();
			Control.click("CreditCard","PayButton");
		}
		catch (Exception e) {
			Control.takeScreenshot();
		  	e.printStackTrace();
		}
	}
	
	public static void paymentSummary(String promo, String payment) throws Exception {
		String attribute;
		String price = Generic.ReadFromExcel(promo, "Shop", 1);
		String paymentType = Generic.ReadFromExcel(payment, "PaymentDetails", 4);
		if (Constant.Platform.equalsIgnoreCase("ios")) {
			attribute = "value";
		} else {
			attribute = "text";
		}
		try	{
			Control.customWait("PaymentSummary", "SendEmailConfirmationHeader", 60);
			Assert.assertTrue(Control.objExists("PaymentSummary", "SendEmailConfirmationHeader", true));
			Control.objExists("PaymentSummary", "OrderSummary", true);
			Control.compareTextByAttribute("PaymentSummary", "PromoName", promo, attribute);
			Control.objExists("PaymentSummary", "PaymentMethodLabel", true);
			if (paymentType.equals("GCash")) {
				Control.objExists("PaymentSummary", "GCashLabel", true);		
			} else {
				Control.objExists("PaymentSummary", "CreditDebitCardLabel", true);
			}
			Control.objExists("PaymentSummary", "TotalAmountLabel", true);
			Control.compareTextByAttribute("PaymentSummary", "TotalAmountValue", price, attribute);
			Control.takeScreenshot();
			//Control.click("PaymentSummary", "BackToShopButton");	
		}
		catch (Exception e) {
			Generic.WriteTestData("Payment Summary", "", "", "Should be able to purchase: " + promo + " using " + paymentType, "Failed to purchase", "Fail");
			Control.takeScreenshot();
		  	e.printStackTrace();
		}
	}
}
