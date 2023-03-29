package GOMOLibrary;

import java.io.FileInputStream;

import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import utility.Constant;
import utility.Control;
import utility.Generic;

public class GCashApp {
	
	public static void loginGCash(String MSISDN, String MPIN) throws Exception {
		try {
			Thread.sleep(5000);
			Control.customWait("GCashAPP_Login", "EnterMPIN",20);
			if(Control.findElement("GCashAPP_Login", "EnterMPIN")!=null) {
				String formatNumber = mobileFormatLogin(MSISDN);
				System.out.println("Checking logged account: " + formatNumber + " | " + "0"+MSISDN);
				if(Constant.driver.getPageSource().contains(formatNumber) || Constant.driver.getPageSource().contains("0"+MSISDN) || Constant.driver.getPageSource().contains("+63-"+MSISDN)) {
					enterMPIN(MPIN);
				}
				else {
					notMyNumber();
					Control.customWait("GCashAPP_Login", "GCashHeader", 10);
					quickGCashLogin(MSISDN, MPIN);
				}
				Control.takeScreenshot();
			}
			else if(Control.findElementWithoutPrintingException("GCashAPP_Login", "GCashHeader")!=null) {
				quickGCashLogin(MSISDN, MPIN);
				Control.takeScreenshot();
			}
			else {
				Generic.WriteTestData("GCash Login", "", "", "Should be able to login", "Page not recognized", "Fail");
				Control.takeScreenshot();
			}
		}
		catch (Exception e) {
			Generic.WriteTestData("GCash Login", "", "", "Should be able to login using: " + MSISDN, "Failed to login", "Fail");
			Control.takeScreenshot();
		  	e.printStackTrace();
		}
	}

	public static void quickGCashLogin(String MSISDN, String MPIN) throws Exception {
		String Authentication;
		if(Constant.Platform.equalsIgnoreCase("ios")){
			Control.enterText("GCashAPP_Login", "MobileNumberField", MSISDN);
			Control.click("Keyboard", "DoneToolButton");
		}
		else {
			Control.click("GCashAPP_Login", "MobileNumberField");
			Control.enterKeyCodeEvent(MSISDN);
			Constant.driver.hideKeyboard();
		}
		Control.takeScreenshot(); 	
		Control.click("GCashAPP_Login", "NextButton");
		Control.customWait("GCashAPP_Login", "Glife", 10);
		if(Control.findElement("GCashAPP_Login", "AuthenticationHeader")!=null) {
			Control.click("GCashAPP_Login", "AuthenticationCode");
			
			if(Constant.Env.equalsIgnoreCase("preprod")) {
				Authentication = Control.EnterValueManually();
				String correctOTP = "No";
			    String ObjButtons[] = {"Yes","No"};
				while(correctOTP.equals("No"))
				{
				    int PromptResult = JOptionPane.showOptionDialog(null, 
				        "Is the OTP: '" + Authentication + "' correct", "Confirm Authentication", 
				        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, 
				        ObjButtons,ObjButtons[1]);
				    if(PromptResult==0) {
				      correctOTP = "Yes";
				    }
				    else {
				    	Authentication = Control.EnterValueManually();
				    	correctOTP = "No";
				    }
				}
			}
			else {
				Authentication = "000000";
			}
			if(Constant.Platform.equalsIgnoreCase("ios")){
				Control.EnterKeys(Authentication);
				Control.takeScreenshot();
				Control.click("GCashAPP_Login", "SubmitButton");
			}
			else {
				Control.enterKeyCodeEvent(Authentication);
				Control.sendKeyEvent("66", 0);
				Control.takeScreenshot();
				Control.click("GCashAPP_Login", "SubmitButton");
			} 	
			Control.customWait("GCashAPP_Login", "EnterMPIN", 10);
			if(Control.findElement("GCashAPP_Login", "EnterMPIN")!=null) {
				enterMPIN(MPIN);
			} 
			else if(Control.findElement("GCashAPP_Login", "EnterMPINButton")!=null) {
				Control.click("GCashAPP_Login", "EnterMPINButton");
				enterMPIN(MPIN);
			}
			else {
				Generic.WriteTestData("GCash Login", "", "", "Should be able to arrived at MPIN", "Failed to arrived", "Fail");
				Control.takeScreenshot();
			}
		}
		else {
			Generic.WriteTestData("GCash Login", "", "", "Should be able to arrived at Authentication", "Failed to arrived", "Fail");
			Control.takeScreenshot();
		}
	}
	
	public static void enterMPIN(String MPIN) throws Exception {
		WebElement keypad = null;
		String value = null;
		String[] enteredPin = MPIN.split("");
		Control.takeScreenshot(); 		
		if(Constant.Platform.equalsIgnoreCase("ios")) {
			for(int i = 0; i < enteredPin.length; i++ )// i++
			{
				String element = enteredPin[i];
				Thread.sleep(500);
				Constant.driver.findElement(By.xpath("//XCUIElementTypeButton[@label='"+ element +"']")).click();	
			}
		}
		else { 
			/*
			for (int i = 0; i < enteredPin.length; i++) {
				
				switch (enteredPin[i]) {
				case "0":
					value = "zero";
					break;		
				case "1":
					value = "one";
					break;
				case "2":
					value = "two";
					break;
				case "3":
					value = "three";
					break;
				case "4":
					value = "four";
					break;
				case "5":
					value = "five";
					break;
				case "6":
					value = "six";
					break;
				case "7":
					value = "seven";
					break;
				case "8":
					value = "eight";
					break;
				case "9":
					value = "nine";
					break;
				}
			
				keypad = Constant.driver.findElement(By.xpath("//*[@resource-id=\"com.globe.gcash.android:id/tv_" + value + "\"] | //*[@resource-id=\"com.globe.gcash.android.uat:id/tv_" + value + "\"]"));
				keypad.click();
			}	
		}
		*/
		
			for (int i = 0; i < enteredPin.length; i++) {
				keypad = Constant.driver.findElement(By.xpath("//*[@resource-id=\"com.globe.gcash.android.uat:id/btn_number" + enteredPin[i] + "\"] | //*[@resource-id=\"com.globe.gcash.android:id/btn_number" + enteredPin[i] + "\"] | //*[@value= '" + enteredPin[i] +"']"));
				keypad.click();
			}
			
			if(Control.findElementWithoutPrintingException("GCashAPP_Login", "SomethingWentWrong")!=null) {
				Control.click("GCashAPP_Login", "TryAgainButton");
			}
		}
	}
	
	public static void clickSkip() throws Exception {
		Control.customWait("GCashApp_Menu", "SkipButton", 10);
		if(Control.findElementWithoutPrintingException("GCashApp_Menu", "SkipButton")!=null) {
			Control.takeScreenshot();
			Control.click("GCashApp_Menu", "SkipButton");
		}
	}
	
	public static void clickLater() throws Exception {
		Control.customWait("GCashApp_GOMO", "LaterButton", 10);
		if(Control.findElementWithoutPrintingException("GCashApp_GOMO", "LaterButton")!=null) {
			Control.takeScreenshot();
			Control.click("GCashApp_GOMO", "LaterButton");
		}
	}
	
	public static String mobileFormatLogin(String MSISDN) throws Exception {
		String[] mobile = MSISDN.split("");
		String mobileFormat = "0" + mobile[0]+mobile[1]+mobile[2] + " " + mobile[3]+mobile[4]+mobile[5] + " " + mobile[6]+mobile[7]+mobile[8]+mobile[9];
		System.out.println(mobileFormat);
		return mobileFormat;
	}
	
	public static void notMyNumber() throws Exception {
		if(Constant.Platform.equalsIgnoreCase("ios")) {
			Control.TapCoordinates2("50%","50%"); 
		}
		Control.click("GCashAPP_Login", "ChangeNumber");
		Control.customWait("GCashAPP_Login", "ChangeNumberMessage", 10);
		Control.click("GCashAPP_Login", "OkButton");
	}
	
	public static void skipLoginGuide() throws Exception {
		Thread.sleep(2000);
		if(Control.findElementWithoutPrintingException("GCashApp_Menu", "DiscoverPopup")!=null) {
			Control.takeScreenshot();
			Control.click("GCashApp_Menu", "DiscoverPopup");
			Thread.sleep(500);
			Control.click("GCashApp_Menu", "TapToContinue");
			Thread.sleep(500);
			Control.click("GCashApp_Menu", "DismissPopup");
		}
	}
	
	public static void clickRemindMeLater() throws Exception {
		Thread.sleep(2000);
		if(Control.findElementWithoutPrintingException("GCashApp_Menu", "RemindMeLater")!=null) {
			Control.takeScreenshot();
			Control.click("GCashApp_Menu", "RemindMeLater");
		}
	}
	
	public static void enterQR() throws Exception {
		Control.customWait("GCashApp_Menu", "QRMenu", 20);
		Thread.sleep(5000);
		Assert.assertTrue(Control.objExists("GCashApp_Menu", "QRMenu", true)); 
		Control.takeScreenshot();
		Control.click("GCashApp_Menu", "QRMenu");
		if(Control.findElementWithoutPrintingException("GCashApp_Menu", "SkipButton")!=null) {
			Control.click("GCashApp_Menu", "SkipButton");
		}
		Control.customWait("GCashApp_Menu", "UploadQR", 20);
		Control.takeScreenshot();
		Control.click("GCashApp_Menu", "UploadQR");
		Control.customWait("GCashApp_Menu", "QRImage", 20);
		Control.click("GCashApp_Menu", "QRImage");
	}
	
	public static void gomoLogin(String MSSDN, String MPIN) throws Exception {
		Control.customWait("GCashApp_GOMO", "GOMOHeader", 20);
		Assert.assertTrue(Control.objExists("GCashApp_GOMO", "GOMOHeader", true)); 
		Control.takeScreenshot();
		Control.click("GCashApp_Menu", "LoginHomeButton");
		Control.customWait("GCashApp_GOMO", "PleaseEnterYourGOMOLabel", 20);
		Assert.assertTrue(Control.objExists("GCashApp_GOMO", "PleaseEnterYourGOMOLabel", true)); 
		Control.takeScreenshot();
	}
	
	public static void quickLogin(String MSISDN, String MPIN) throws Exception {
		Control.customWait("GCashApp_GOMO", "LoginHomeButton", 20);
		if(Control.findElementWithoutPrintingException("GCashApp_GOMO", "LoginHomeButton")!=null) {
			Assert.assertTrue(Control.objExists("GCashApp_GOMO", "GOMOHeader", true)); 
			Control.takeScreenshot();
			Thread.sleep(5000);
			Control.click("GCashApp_GOMO", "LoginHomeButton");
		}
		Control.customWait("GCashApp_GOMO", "PleaseEnterYourGOMOLabel", 20);
		Assert.assertTrue(Control.objExists("GCashApp_GOMO", "PleaseEnterYourGOMOLabel", true)); 
		Control.takeScreenshot();
		if(Constant.Platform.equalsIgnoreCase("ios")){
			Control.enterText("GCashApp_GOMO", "MobileNumberField", MSISDN);
			Control.objExists("GCashApp_GOMO", "63Label", true);
			if(!Constant.driver.getPageSource().contains(MSISDN)) {
				Control.click("GCashApp_GOMO", "DeleteMobileNumberIcon");
				Control.iOSKeyboard_NumericOnly(MSISDN);
			}
		}
		else {
			Control.click("GCashApp_GOMO", "MobileNumberField");
			Control.objExists("GCashApp_GOMO", "63Label", true);
			Control.enterKeyCodeEvent(MSISDN);
		}
	
		Control.takeScreenshot();
		Control.click("GCashApp_GOMO", "6DigitPINField");
		if(Constant.Platform.equalsIgnoreCase("ios")){
			Control.EnterKeys(MPIN);
		}
		else {
			Control.enterPin(MPIN);
		}
		Thread.sleep(2000);
		Control.takeScreenshot();
		Control.click("GCashApp_GOMO", "LoginButton");
		Control.customWait("GCashApp_GOMO", "NotMyNumber", 60);
		String formatNumber = mobileFormat(MSISDN);
		if(Constant.driver.getPageSource().contains(formatNumber)) {
			Generic.WriteTestData("GLife GOMO Login", "", "", "Should be able to login", "Successfully login", "Pass");
			Control.takeScreenshot();
		}
		else {
			Generic.WriteTestData("GLife GOMO Shop", "", "", "Should be able to login", "Failed to login", "Fail");
			Control.takeScreenshot();
		}
	}
	
	public static void login(String MSISDN, String MPIN) throws Exception {
		try {
			Control.customWait("GCashApp_GOMO", "NotMyNumber", 30);
			if(Control.findElementWithoutPrintingException("GCashApp_GOMO", "NotMyNumber")==null) {
				quickLogin(MSISDN,MPIN);
			}
			else {
				String formatNumber = mobileFormat(MSISDN);
				if(Constant.driver.getPageSource().contains(formatNumber)) {
					Generic.WriteTestData("GLife GOMO Login", "", "", "Already Login", "Skip Login Steps", "Pass");
					Control.takeScreenshot();
				}
				else {
					unlinkGOMO();
					quickLogin(MSISDN,MPIN);
				}
			}
		}
		catch (Exception e) {
			Generic.WriteTestData("GLife GOMO Login", "", "", "Should be able to login in GLife GOMO", "Failed to login", "Fail");
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
	
	public static void unlinkGOMO() throws Exception {
		Control.click("GCashApp_GOMO", "NotMyNumber");
		Control.customWait("GCashApp_GOMO", "UnlinkLabel", 10);
		Control.takeScreenshot();
		Control.click("GCashApp_GOMO", "ConfirmButton");
	}
	
	public static void validateSIMList() throws Exception {
		try {
			WebElement promoElement;
			WebElement priceElement;
			String getPriceValue = null;
			FileInputStream ExcelFile= new FileInputStream(Constant.TestDataFilePath);
			XSSFWorkbook workbook = new XSSFWorkbook(ExcelFile);
			XSSFSheet Worksheet =  workbook.getSheet("GMP_SIM");	
			int totalRows=Worksheet.getLastRowNum();
			DataFormatter formatter = new DataFormatter();
			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator(); 
			
			for(int i=5; i<=totalRows; i++)
			{
				String getPromo = formatter.formatCellValue(Worksheet.getRow(i).getCell(0));
				String rawPromo = getPromo;
				if(getPromo.contains("UNLI")) {
					getPromo = "UNLI Data";
				}
				else {
					String[] promoSplit = getPromo.split("GB");
					getPromo = promoSplit[0] + "GB";
				}
				
				String getPrice = "P"+formatter.formatCellValue(Worksheet.getRow(i).getCell(1));
				promoElement =  Control.findXPath("//*[@class='android.widget.RadioGroup']//*[@text='"+getPromo+"'] | (//*[@label='" + getPromo + "'])[1]");
				String isAvailable = formatter.formatCellValue(Worksheet.getRow(i).getCell(4));

				if(isAvailable.equals("Yes")) {
					if(promoElement==null) {
						Generic.WriteTestData("GOMO SIM List", "", "", rawPromo + " promo should be visible", "Promo not found", "Fail");
					}
					else {
						System.out.println("Validating SIM: " + rawPromo ); 
						if(promoElement.isDisplayed()) {
							Generic.WriteTestData("GLife GOMO SIM List", "", "", rawPromo + " promo should be visible", "Promo is visible", "Pass");	
							priceElement =  Control.findXPath("//*[@class=\"android.widget.RadioGroup\"]//*[@text='"+getPromo+"']/parent::*/following-sibling::*[last()] | (//*[@label='" + getPromo + "'])[1]/parent::*/following-sibling::*[1]/child::*[1]");			
							if(Constant.Platform.equalsIgnoreCase("ios")){
								getPriceValue = priceElement.getAttribute("label");
							}
							else {
								getPriceValue = priceElement.getAttribute("text");
							}
							if(getPrice.equals(getPriceValue)) {
								Generic.WriteTestData("GOMO SIM List", "", "", rawPromo + " promo price should be " + getPrice, "Promo price is correct", "Pass");		
							}
							else {
								Generic.WriteTestData("GOMO SIM List", "", "", rawPromo + " promo price should be " + getPrice, "Promo price ("+getPriceValue+") is incorrect", "Fail");	
							}
						}
						else {
							Generic.WriteTestData("GOMO SIM List", "", "", rawPromo + " promo should be visible", "Promo is not visible", "Fail");
						}
					}
				}
			}
			Control.takeScreenshot();
		}
		catch (Exception exception) {
			exception.printStackTrace();
			Generic.WriteTestData("GLife GOMO SIM List", "", "", "Should be able to validate all SIM", "Failed to validate", "Fail");
			Control.takeScreenshot();
		}
	}
	
	public static boolean validateSIMBanner() throws Exception {
		WebElement promoElement;
		WebElement runtimeElement;
		String promoName = Generic.ReadFromExcel("MainSIM", "GMP_SIM", 1);
		String promoRuntime = Generic.ReadFromExcel("MainSIM", "GMP_SIM", 2);
		String withDF = Generic.ReadFromExcel(promoName, "GMP_SIM", 3);
		
		if(promoName.contains("UNLI")) {
			promoName = "UNLI DATA";
		}
		else {
			String[] promoNameTrim = promoName.split("GB");
			promoName = promoNameTrim[0] + "GB";
		}
				
		Control.customWait("GCashApp_GOMO", "GOMOSIMLabel", 10);
		Control.SwipeToCoordinates("50%","55%","50%","20%");
		Control.objExists("GCashApp_GOMO", "GOMOSIMLabel", true);
		
		promoElement =  Control.findXPath("//*[@text='GOMO SIM']/parent::*//*[contains(@text,'" + promoName + "')] | (//*[@label=\"GOMO SIM\"]/parent::*/parent::*//*[contains(@label,'" + promoName + "')])[1]");
		if(promoElement==null) {
			Generic.WriteTestData("GOMO SIM Banner", "", "", promoName + " promo should be visible", "Promo is not visible", "Fail");	
		}
		else {
			Generic.WriteTestData("GOMO SIM Banner", "", "", promoName + " promo should be visible", "Promo is visible", "Pass");	
		}
		if(withDF.equals("Yes")) {
			Control.objExists("GCashApp_GOMO", "FreeSIMCardOnly", true);
		}
		else {
			Control.objExists("GCashApp_GOMO", "FreeSIMCardAndDelivery", true);
		}
		if(promoRuntime == null || promoRuntime.length() == 0) {
			System.out.println("SKIP SIM PROMO RUNTIME VALIDATION");
		}
		else {
			runtimeElement = Control.findXPath("//*[@text='GOMO SIM']/parent::*//*[contains(@text,'" + promoRuntime + "')] | (//*[@label='GOMO SIM']/parent::*/parent::*//*[contains(@label,'" + promoRuntime + "')])[1]");
			if(runtimeElement==null) {
				Generic.WriteTestData("GOMO SIM Banner", "", "", promoName + " promo runtime should be " + promoRuntime, "Runtime is not visible", "Fail");
			}
			else {
				Generic.WriteTestData("GOMO SIM Banner", "", "", promoName + " promo runtime should be " + promoRuntime, "Runtime is visible", "Pass");		
			}
		}
		
		Control.objExists("GCashApp_GOMO", "GOMOSIMBuyButton", true);
		Control.takeScreenshot();
		Control.click("GCashApp_GOMO", "GOMOSIMBuyButton");
		Control.customWait("GCashApp_GOMO", "CheckoutLabel", 10);
		if(Control.findElement("GCashApp_GOMO", "CheckoutLabel")!=null) {
			Generic.WriteTestData("GOMO SIM Banner", "", "", "Should be able to proceed to SIM Purchase Checkout", "Successfully arrived at Checkout Page", "Pass");
			Control.takeScreenshot();
			return true;
		}
		else {
			Generic.WriteTestData("GOMO SIM Banner", "", "", "Should be able to proceed to SIM Purchase Checkout", "Failed to arrive at Checkout Page", "Fail");
			Control.takeScreenshot();
			return false;
		}
	}
	
	public static void selectSIM(String SIM) throws Exception {
		WebElement promoElement;
		String promoName = Generic.ReadFromExcel(SIM, "GMP_SIM", 0);
		String promoPrice = Generic.ReadFromExcel(SIM, "GMP_SIM", 1);
		String promoLimited = Generic.ReadFromExcel(SIM, "GMP_SIM", 2);
		String promoDF = Generic.ReadFromExcel(SIM, "GMP_SIM", 3);
		
		if(promoName.contains("UNLI")) {
			promoName = "UNLI DATA";
		}
		else {
			String[] promoNameTrim = promoName.split("GB");
			promoName = promoNameTrim[0] + "GB";
		}
		
		Constant.Map.get("GCashApp_GOMO").put(promoName, "xpath#//*[@text='GOMO SIM']/parent::*//*[contains(@text,'" + promoName + "')] | (//*[@label=\\\"GOMO SIM\\\"]/parent::*/parent::*//*[contains(@label,'" + promoName + "')])[1]");	
		Control.click("GCashApp_GOMO", promoName);
	}
	
}
