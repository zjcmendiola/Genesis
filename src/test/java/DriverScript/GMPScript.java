package DriverScript;

import java.util.Properties;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import GOMOLibrary.GCashApp;
import utility.Control;
import utility.Generic;
import utility.LaunchApp;
import TestCases.GMP;

public class GMPScript {
	public static SoftAssert softAssertions = new SoftAssert();
	public static Properties prop;

	@BeforeTest
	public static void OpenApp() throws Exception {
		LaunchApp.getPlatform();
		Generic.TestScriptStart("GomoxRegression", utility.Constant.Platform);
		//Set to false if you dont need to install
		LaunchApp.SetUpDevice(false); 
		LaunchApp.launchGCash();
		Thread.sleep(1000);
	}
	
	@BeforeMethod
	public void Soft() throws Exception {
		Control.setDriverTimeOut(30);
		LaunchApp.restartGCashApp();
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
	public static void gcashLogin() throws Exception {
		GMP.allGMPLogin("GCashLogin","GMPLogin");
		GMP.validateSIMandOfferList();
		
	}
}
