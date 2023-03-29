/*
 *Description: Control Functions library 
'Author :Sunanda Tirunagari and Ankit Kumar
 */

package utility;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.asserts.SoftAssert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
//import io.testproject.sdk.drivers.ios.IOSDriver;

import java.net.URL;

public class Constant {
	
/*
 * ***********************************************************
 * User-specific parameters - can be modified by END users
 * ***********************************************************
*/
	
	public static String TestDataFilePath="TestData.xlsx";
	
	public static String senderList="8080,2652,2882,9451926523,2916,2884,AutoLoadMAX,GLOBE,3373";//comma seperated values
	
	public static String path_to_python_scripts = null;
	//public static final String path_to_python_scripts="C:\\PythonSetup\\PythonSetup\\Python27_Excel_PDF\\Python27_Excel_PDF\\";
	//public static final String path_to_python_scripts="C:\\PDF_SolutionComp\\";
	
	public static float smsPercentageMatch=0.70f;
	
	public static int smsWaitingTime=30; //in seconds
	
	public static int sleepTimeBeforeCheckingMessages=120000;//milliseconds
	
	public static String WorkSpace = System.getProperty("user.dir");

	public static String ProjectName = "Projects";

	/*
	 * *********************************************************************************************
	 * *********************************************************************************************
	 * *********************************************************************************************
	*/
	
	
	
	/*
	 * ***********************************************************
	 * System Parameters - shall NOT be modified by END users
	 * ***********************************************************
	*/

	public static String ipToListen=null;
	public static final String Environment = "SIT";
	public static int SeqID = 1;
	public static int StepIndex = 0;
	public static int TestStepIndex = 0;
	public static int StepStatus = 0;
	public static int TestCaseIndex = 0;
	public static int TestCaseNumber = 0;
	public static int PassedCases = 0;
	public static int FailedCases = 0;
	public static HashMap<String, HashMap<String, String>> Map = new HashMap<String,HashMap<String,String>>();
	public static HashMap<String, HashMap<String, String>> Map2 = new HashMap<String,HashMap<String,String>>();

	public static final String Brand[] = null;

	public static boolean MultipleTCInOneMethod = false;

	public static int MaxDriverTimeOut =30;
	public static String UserStoryName = null;
	public static String ResultFilePath =".\\target\\Resuts\\";
	public static String ScreenshotFolderName = null;
	public static String strScenarioDesc=null;
	public static String strExpectedResult=null;
	public static String strActualResult=null;
	public static String PageName=null;
	public static String locator=null;
	public static String RecentScreenshot=null;
	public static WebDriver driver_w = null;
	public static AppiumDriver driver;
	public static AndroidDriver<AndroidElement> driver1;
	public static AndroidDriver <AndroidElement> androiddriver;
	public static AppiumDriver ad;
    public static WebElement webelement;	
	public static boolean DefaultoptionalFlag = true;
	
	public static int flag_init=0;
	public static String lastSMSTime="";
	public static long youtubeElapsedTime=0;
	public static long updatedStartTime=0;
	public static boolean timerRunning=false;
	
	public static int retryOnFailureCount=3;
	
	public static int numberOfFreeMessages=0;
	
	public static WebElement youtubeScreen=null;
	public static int youtubeX=-1;
	public static int youtubeY=-1;
	public static String lastReadSMSFileName="";
	
	
	public static int comparisonType=2;//ignore tagged texts
	public static String dynamicLine="";
	public static float numberOfFreetexts=0;
	
	public static double lastSmsOrUssdTime=0;
	public static boolean flagMultipleMessages=false;
	
	
	public static float lastSmsPercentMatch=0.0f;
	public static boolean flagOnlySpecificSenders=true;
	
	
	public static String textViewerPackageName="com.example.hp_pc.simpletextviewer";
	public static String textViewerActivityName="com.example.hp_pc.simpletextviewer.MainActivity";
	public static String[] specificSenderList=senderList.split(",");
	public static int numberOfExpectedSms=1;
	public static int numberOfExpectedReply=1;
	public static int maxNumberOfColumns=50;
	public static int globalRowNumber=0;
	public static int lastRepliedRowNumber=0;
	public static String brandType="Regular";
	public static boolean alreadyWaited=false;
	public static String commandPullSS = "adb shell input keyevent 4";
	public static String commandPullSSS = "adb shell input touchscreen swipe 830 1120 530 1120";

	public static boolean flag = true;

	public static boolean Broadband = false;

	public static boolean SGTier1 = false;

	public static int scrolLimit=0;
	
	public static SoftAssert softAssertions;

	public static String midX;

	public static String Failures;

	public static boolean paperlessBillinhDisabled = false;

	public static boolean GoGreen = false;

	public static int defaultBrowserTimeOut = 30;
	 
	public static int defaultBrowserTimeOut1 = 5;
	
	public static int NumberOfTriesToDownloadApp = 3;
	
	public static boolean BillGenerated = true;
	
	public static int MinDriverTimeOut = 2;
	
	public static boolean AppLaunched = false;
	public static HashMap<String, HashMap<String, String>> labelMap = new HashMap<String, HashMap<String, String>>();
	
	public static String UnitNo ;
	public static String FirstName;
	public static String LastName;
	public static String StreetName;
	public static String Province;
	public static String City;
	public static String Barangay;
	public static String Village;
	public static String ZipCode;
	public static String UnitNumber;
	public static String Platform;
	public static String Env;
	public static String AppID;
	public static String GMP_APP;
	public static String Email;
	public static String Mobile;
	public static String MSISDN;
	public static String Location;
	public static String PromoData;	
	public static String MoCreds;	
	public static String Quantity = "1";	
	public static String Promo;

	public static String Data;	
	public static String Text;	
	public static String Call;
	
	public static boolean LandingPage;
	
	public static String SecurityToken;
	
	public static String [] Luzon = {"BENGUET", "KALINGA", "ILOCOS NORTE", "ILOCOS SUR", "LA UNION", "PANGASINAN", 
			"CAGAYAN", "ISABELA", "NUEVA VIZCAYA", "BATAAN", "BULACAN", "NUEVA ECIJA", "PAMPANGA", "TARLAC", 
			"ZAMBALES", "BATANGAS", "CAVITE", "LAGUNA", "QUEZON", "RIZAL", "MARINDUQUE", "CAMARINES SUR"};
	
	public static String [] Visayas = {"AKLAN", "ANTIQUE", "CAPIZ", "ILOILO", "NEGROS OCCIDENTAL", "BOHOL", "BILIRAN", 
			"CEBU", "NEGROS ORIENTAL",  "EASTERN SAMAR", "NORTHERN SAMAR", "SAMAR", "LEYTE", "SOUTHERN LEYTE"};
	
	public static String [] Mindanao = {"ZAMBOANGA DEL NORTE", "ZAMBOANGA DEL SUR", "ZAMBOANGA SIBUGAY", "LANAO DEL NORTE", 
			"MISAMIS OCCIDENTAL", "MISAMIS ORIENTAL","DAVAO DEL NORTE", "DAVAO DEL SUR", "COTABATO", 
			"SOUTH COTABATO", "AGUSAN DEL NORTE", "SURIGAO DEL NORTE", "MAGUINDANAO"};
}
