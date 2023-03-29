package TestCases;

import GOMOLibrary.GCashApp;
import utility.Constant;
import utility.Generic;

public class GMP {

	public static void allGMPLogin(String GCash, String GOMO) throws Exception
	{
		Generic.WriteTestCase("GMP", "Proceed to GMP GLife", "Expected Result", "Actual Result");
		String platform;
		
		String MSISDN = Generic.ReadFromExcel(GCash, "Account", 1);
		String MPIN = Generic.ReadFromExcel(GCash, "Account", 2);
		
		String GMP_MSISDN = Generic.ReadFromExcel(GOMO, "Account", 1);
		String GMP_MPIN = Generic.ReadFromExcel(GOMO, "Account", 2);
		
		GCashApp.loginGCash(MSISDN, MPIN);
		GCashApp.skipLoginGuide();
		GCashApp.clickSkip();
		GCashApp.clickRemindMeLater();
		GCashApp.enterQR();
		GCashApp.login(GMP_MSISDN, GMP_MPIN);
		Generic.TestScriptEnds();
	}
	
	public static void validateSIMandOfferList() throws Exception
	{
		Generic.WriteTestCase("Shop Screen Page", "Should be validate Shop Screen Page", "Expected Result", "Actual Result");
		GCashApp.validateSIMBanner();
		Generic.TestScriptEnds();
		
		Generic.WriteTestCase("Multiple SIM View", "Should be redirected to the checkout screen and see these SIM details", "Expected Result", "Actual Result");
		GCashApp.validateSIMList();
		Generic.TestScriptEnds();
		
	}
	
}
