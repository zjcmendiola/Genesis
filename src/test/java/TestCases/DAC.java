package TestCases;

import GOMOLibrary.Dashboard;
import GOMOLibrary.MoCreds;
import utility.Generic;

public class DAC {
	public static void moCredsValidation(String pack) throws Exception
	{
		String promo = Generic.ReadFromExcel(pack, "MoCreds", 0);
		String category = Generic.ReadFromExcel(pack, "MoCreds", 2);
		
		Generic.WriteTestCase("Mo Creds", "Should be able to convert pack: " + promo, "Expected Result", "Actual Result");
		Dashboard.getDashboardAvailableBalance();
		Dashboard.goToMoCreds();
		MoCreds.selectFilter(category, "Highest First");
		MoCreds.selectMoCreds(promo);
		MoCreds.confirmConversion(promo);
		Generic.TestScriptEnds();
	}
}
