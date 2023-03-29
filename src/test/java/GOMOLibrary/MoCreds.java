package GOMOLibrary;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.testng.Assert;

import utility.Constant;
import utility.Control;
import utility.Generic;

public class MoCreds {
	
	public static void selectFilter(String category, String sort) throws Exception {
		try	{
			Control.customWait("MoCreds", "FilterButton", 20);
			Assert.assertTrue(Control.objExists("MoCreds", "FilterButton", true));  
			Control.takeScreenshot();
			Control.click("MoCreds", "FilterButton");
			Control.customWait("MoCreds", "FilterLabel", 20);
			Assert.assertTrue(Control.objExists("MoCreds", "FilterLabel", true)); 
			Control.takeScreenshot();
			switch(category) {
			case "Call and Text":
			case "Call & Text":
				Assert.assertTrue(Control.objExists("MoCreds", "Filter_CallAndTextLabel", true)); 
				Control.click("MoCreds", "Filter_CallAndTextLabel");
				Control.takeScreenshot();
				break;
			case "Essential":
				Assert.assertTrue(Control.objExists("MoCreds", "Filter_EssentialLabel", true)); 
				Control.click("MoCreds", "Filter_EssentialLabel");
				Control.takeScreenshot();
				break;
			case "Lifestyle":
				Assert.assertTrue(Control.objExists("MoCreds", "Filter_LifestyleLabel", true)); 
				Control.click("MoCreds", "Filter_LifestyleLabel");
				Control.takeScreenshot();
				break;
			case "Dating":
				Assert.assertTrue(Control.objExists("MoCreds", "Filter_DatingLabel", true)); 
				Control.click("MoCreds", "Filter_DatingLabel");
				Control.takeScreenshot();
				break;
			case "Shopping":
				Assert.assertTrue(Control.objExists("MoCreds", "Filter_ShoppingLabel", true)); 
				Control.click("MoCreds", "Filter_ShoppingLabel");
				Control.takeScreenshot();
				break;	
			case "":
				System.out.println("NO SELECTED FILTER CATEGORY");
				break;
			}
			
			switch(sort) {
			case "A-Z":
				Assert.assertTrue(Control.objExists("MoCreds", "Filter_A-Z", true)); 
				Control.click("MoCreds", "Filter_A-Z");
				Control.takeScreenshot();
				break;
			case "Z-A":
				Assert.assertTrue(Control.objExists("MoCreds", "Filter_Z-A", true)); 
				Control.click("MoCreds", "Filter_Z-A");
				Control.takeScreenshot();
				break;
			case "Highest First":
				Assert.assertTrue(Control.objExists("MoCreds", "Filter_GB:HighestFirst", true)); 
				Control.click("MoCreds", "Filter_GB:HighestFirst");
				Control.takeScreenshot();
				break;
			case "Lowest First":
				Assert.assertTrue(Control.objExists("MoCreds", "Filter_GB:LowestFirst", true)); 
				Control.click("MoCreds", "Filter_GB:LowestFirst");
				Control.takeScreenshot();
				break;
			case "Newest First":
				Assert.assertTrue(Control.objExists("MoCreds", "Filter_Newestfirst", true)); 
				Control.click("MoCreds", "Filter_Newestfirst");
				Control.takeScreenshot();
				break;
			case "Oldest First":
				Assert.assertTrue(Control.objExists("MoCreds", "Filter_Oldestfirst", true)); 
				Control.click("MoCreds", "Filter_Oldestfirst");
				Control.takeScreenshot();
				break;
			case "":
				System.out.println("NO SELECTED FILTER SORT BY");
				break;	
			}
			Control.click("MoCreds", "ApplyFilterButton");
			Thread.sleep(3000);
			Control.takeScreenshot();
		}
		catch (Exception e) {
			if(StringUtils.isEmpty(category)) {
				Generic.WriteTestData("Mo Creds Filter", "", "", "Should be able to select sort by: " + sort, "Failed to select", "Fail");
			}
			else if(StringUtils.isEmpty(sort)) {
				Generic.WriteTestData("Mo Creds Filter", "", "", "Should be able to select category: " + category, "Failed to select", "Fail");
			}	
			else {
				Generic.WriteTestData("Mo Creds Filter", "", "", "Should be able to select category - sort by: " + category + " - " + sort, "Failed to select", "Fail");
			}	
			Control.takeScreenshot();
		  	e.printStackTrace();
		}	
	}
	
	public static void selectMoCreds(String moCreds) throws Exception {
		try	{
			Map<String, Object> params = new HashMap<>();
			int i=10;
	        params.put("start","40%,60%");
	        params.put("end","40%,20%");
			while(!Constant.driver.getPageSource().contains(moCreds))
			{
		        params.put("duration","3");
		        Constant.driver.executeScript("mobile:touch:swipe",params);
				i--;
				if(i==0) {	
					break;
				}
			}
			try {
				Control.clickText(moCreds);				
			}
			catch(Exception e) {
				Constant.driver.executeScript("mobile:touch:swipe",params);
				Control.clickText(moCreds);
			}		
		}
		catch (Exception e) {
			Generic.WriteTestData("Select Mo Creds", "", "", "Should be able to select: " + moCreds, "Failed to select", "Fail");
			Control.takeScreenshot();
		  	e.printStackTrace();
		}
	}
	
	public static void confirmConversion(String moCreds) throws Exception {
		try	{
			String attribute;
			String price = Generic.ReadFromExcel(moCreds, "MoCreds", 1);
			Constant.Map.get("MoCreds").put(moCreds, "xpath#//*[@text=\""+ moCreds +"\"]");	
			Constant.Map.get("MoCreds").put(price, "xpath#//*[@text=\""+ price +"\"]");	
			Control.customWait("MoCreds", "ConfirmConversionButton", 20);
			Control.objExists("MoCreds", moCreds, true);
			Control.objExists("MoCreds", price, true);
			Control.takeScreenshot();
			Control.click("MoCreds", "ConfirmConversionButton");
			Assert.assertTrue(Control.objExists("MoCreds", "SuccessfulConversionLabel", true)); 
			Control.objExists("MoCreds", "MoCredsSummaryLabel", true);
			Control.objExists("MoCreds", "SuccessfulConversionLabel", true);
			if (Constant.Platform.equalsIgnoreCase("ios")) {
				attribute = "value";
			} else {
				attribute = "text";
			}
			Control.objExists("MoCreds", moCreds, true);
			Control.compareTextByAttribute("MoCreds","TotalDataConvertedValue",moCreds,attribute);
			Control.takeScreenshot();
			Control.click("MoCreds", "BackToMoCreds");
			Control.customWait("MoCreds", "HomeNavigation", 20);
			Assert.assertTrue(Control.objExists("Dashboard", "HomeNavigation", true)); 
			Control.takeScreenshot();
			Control.click("Dashboard", "HomeNavigation");
			Control.takeScreenshot();
		}
		catch (Exception e) {
			Generic.WriteTestData("Mo Creds Conversion", "", "", "Should be able to convert: " + moCreds, "Failed to convert", "Fail");
			Control.takeScreenshot();
		  	e.printStackTrace();	
		}
	}
	
	public static void availVoucher(String moCreds) throws Exception {
		try	{
			
		}
		catch (Exception e) {
			Control.takeScreenshot();
		  	e.printStackTrace();
		}
	}

}
