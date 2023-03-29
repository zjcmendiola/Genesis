package DriverScript;

import java.io.FileInputStream;
import java.util.Properties;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import GOMOLibrary.MoCreds;
import GOMOLibrary.Shop;
import TestCases.DAC;
import TestCases.PromoPurchase;
import GOMOLibrary.Dashboard;
import utility.Control;
import utility.Generic;
import utility.LaunchApp;
import TestCases.GMP;

public class ScriptPack {
	public static SoftAssert softAssertions = new SoftAssert();
	public static Properties prop;

	@BeforeTest
	public static void OpenApp() throws Exception {
		LaunchApp.getPlatform();
		Generic.TestScriptStart("GomoxRegression", utility.Constant.Platform);
		//Set to false if you dont need to install
		LaunchApp.SetUpDevice(false); 
		LaunchApp.OpenApplication();
		Thread.sleep(1000);
	}
	
	@BeforeMethod
	public void Soft() throws Exception {
		Control.setDriverTimeOut(30);
		Control.RestartApp();
		Thread.sleep(5000);
	}

	@AfterMethod
	public void tearDown() throws Exception {

	}

	@AfterTest
	public static void Close() throws Exception {
		//Constant.driver.quit();
		Control.GeneratePDFReport();
	}
	
	@Test (priority = 1, enabled = true)
	public static void login() throws Exception {
		Dashboard.loginMobileNumber("9271002480", "150051");
	}
	
	@Test (priority = 2, enabled = false)
	public static void logout() throws Exception {
		Dashboard.logout();
	}
	
	@Test (priority = 3, enabled = false)
	public static void promoPurchase() throws Exception {
		PromoPurchase.promoPurchase("Gomo_DataOnly_30GB", "genericCard");
	}
	
	@Test (priority = 4, enabled = false)
	public static void mocredsFilter() throws Exception {
		DAC.moCredsValidation("30 Texts");
	}
	
	@Test (priority = 5, enabled = false)
	public static void gcash() throws Exception {
		Shop.gcashPayment("9271002480");
	}
	
	@Test (priority = 6, enabled = true)
	public static void gcashLogin() throws Exception {
		GMP.allGMPLogin("GCashLogin","GMPLogin");
	}
}
