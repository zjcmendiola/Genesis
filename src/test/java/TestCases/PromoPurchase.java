package TestCases;

import GOMOLibrary.Dashboard;
import GOMOLibrary.Shop;
import utility.Generic;

public class PromoPurchase {

	public static void promoPurchase(String promo, String payment) throws Exception
	{
		String price = Generic.ReadFromExcel(promo, "Shop", 1);
		String category = Generic.ReadFromExcel(promo, "Shop", 2);
		String paymentType = Generic.ReadFromExcel(payment, "PaymentDetails", 4);
		
		Generic.WriteTestCase("Promo Purchase", "Should be able to validate: " + promo, "Expected Result", "Actual Result");
		Dashboard.getDashboardAvailableBalance();
		Dashboard.goToShop();
		Shop.goToDataOffer();
		Shop.selectCategoryTab(category);
		Shop.selectOffer(promo);
		Shop.proceedPayment(promo);
		Generic.TestScriptEnds();
		
		Generic.WriteTestCase("Promo Purchase", "Should be able to purchase: " + promo + " using " + paymentType, "Expected Result", "Actual Result");
		Shop.paymentMethod(payment);
		Shop.paymentSummary(promo, payment);
		Generic.TestScriptEnds();
		
		Generic.WriteTestCase("Promo Purchase", "Should be able to purchase: " + promo + " using " + paymentType, "Expected Result", "Actual Result");
		Dashboard.postValidationPromoPurchase(promo);
		Generic.TestScriptEnds();
	}
	
}
