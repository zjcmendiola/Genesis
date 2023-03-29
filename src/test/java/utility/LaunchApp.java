package utility;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;

public class LaunchApp {

	static String platform;
	public static final String BASH_PATH = "C:/Program Files/Git/bin/sh.exe";
	public static String SCRIPT_NAME = null;
	public static SoftAssert softAssertions = new SoftAssert();
	static String currentDirectory;
	public static Properties prop;
	static String Main_Folder = null;
	static String AppPathPerfecto = null;
	static String MSISDN = null;

	public static void startTest(String platform) throws Exception {
		LaunchApp.platform = platform;
		if (platform.equalsIgnoreCase("Android")) {
			// AndroidDriver driver;
			Constant.driver = startAppium_Android();
		} else {
			// RemoteWebDriver driver;
			Constant.driver = startAppium_IOS();
		}
	}	
	/*
	 * This method is used for initiate the AppiumDriver with caps and connection
	 * protocol
	 */
	private static AppiumDriver<AndroidElement> startAppium_Android() throws Exception {		
		if (Constant.driver == null) {					
			String Host = null;
			DesiredCapabilities capabilities = new DesiredCapabilities("", "", Platform.ANY);
			if (prop.getProperty("Env").equals("public")) {
				Host = "techm-globe-public";	
				capabilities.setCapability("securityToken",
				PerfectoLabUtils.fetchSecurityToken(Generic.ReadFromExcel("SecurityToken", "AI_TestData", 1)));
				
				capabilities.setCapability("manufacturer", Generic.ReadFromExcel("Manufacturer", "AI_TestData", 1));
				capabilities.setCapability("model", Generic.ReadFromExcel("Model", "AI_TestData", 1));
				capabilities.setCapability("enableAppiumBehavior", true);			
				Constant.SecurityToken = Generic.ReadFromExcel("SecurityToken", "AI_TestData", 1);
				
				String activeSession = Generic.ReadFromExcel("UseSessionID", "AI_TestData", 1);
				if(activeSession.equals("Yes")) {
					capabilities.setCapability("deviceSessionId", Generic.ReadFromExcel("SessionID", "AI_TestData", 1));
				}			
			}
			else {
				Host = "techm-globe";
				capabilities.setCapability("securityToken", PerfectoLabUtils.fetchSecurityToken(Generic.ReadFromExcel("SecurityTokenPrivate", "AI_TestData", 1)));
				capabilities.setCapability("manufacturer", Generic.ReadFromExcel("Manufacturer", "AI_TestData", 1));
				capabilities.setCapability("model", Generic.ReadFromExcel("Model", "AI_TestData", 1));
				capabilities.setCapability("enableAppiumBehavior", true);				
				Constant.SecurityToken = Generic.ReadFromExcel("SecurityToken", "AI_TestData", 1);
				
				String activeSession = Generic.ReadFromExcel("UseSessionID", "AI_TestData", 1);
				if(activeSession.equals("Yes")) {
					capabilities.setCapability("deviceSessionId", Generic.ReadFromExcel("SessionID", "AI_TestData", 1));
				}	
			}
			Constant.driver = new AndroidDriver(new URL("https://"+Host+".perfectomobile.com/nexperience/perfectomobile/wd/hub"), capabilities);			
		}
		Constant.driver.manage().timeouts().implicitlyWait(Constant.defaultBrowserTimeOut, TimeUnit.SECONDS);		
		return Constant.driver;
	}	
	/*
	 * This method is used for initiate the IOSDriver with caps and connection
	 * protocol
	 */
	public static AppiumDriver<IOSElement> startAppium_IOS() throws Exception {
		if (Constant.driver == null) {
			String Host = null;
			DesiredCapabilities capabilities = new DesiredCapabilities("", "", Platform.ANY);
			if (prop.getProperty("Env").equals("public")) {
				Host = "techm-globe-public";
				if((!(Generic.ReadFromExcel("SessionID", "AI_TestData", 1)==null))||Generic.ReadFromExcel("SessionID", "AI_TestData", 1).equals(""))
				capabilities.setCapability("securityToken",Generic.ReadFromExcel("SecurityToken","AI_TestData", 1));			
				
				capabilities.setCapability("manufacturer", Generic.ReadFromExcel("Manufacturer", "AI_TestData", 1));
				capabilities.setCapability("model", Generic.ReadFromExcel("Model", "AI_TestData", 1));
				capabilities.setCapability("baseAppiumBehavior", "Yes");		
				capabilities.setCapability("iOSResign", true);
				Constant.SecurityToken = Generic.ReadFromExcel("SecurityToken", "AI_TestData", 1);
				
				String activeSession = Generic.ReadFromExcel("UseSessionID", "AI_TestData", 1);
				if(activeSession.equals("Yes")) {
					capabilities.setCapability("deviceSessionId", Generic.ReadFromExcel("SessionID", "AI_TestData", 1));
				}	
				
			} 
			else {
				Host = "techm-globe";
				capabilities.setCapability("securityToken", PerfectoLabUtils.fetchSecurityToken(Generic.ReadFromExcel("SecurityTokenPrivate", "AI_TestData", 1)));
				
				capabilities.setCapability("manufacturer", Generic.ReadFromExcel("Manufacturer", "AI_TestData", 1));
				capabilities.setCapability("model", Generic.ReadFromExcel("Model", "AI_TestData", 1));
				Constant.SecurityToken = Generic.ReadFromExcel("SecurityTokenPrivate", "AI_TestData", 1);
				
				String activeSession = Generic.ReadFromExcel("UseSessionID", "AI_TestData", 1);
				if(activeSession.equals("Yes")) {
					capabilities.setCapability("deviceSessionId", Generic.ReadFromExcel("SessionID", "AI_TestData", 1));
				}		
			}		
			Constant.driver = new IOSDriver(new URL("https://" + Host + ".perfectomobile.com/nexperience/perfectomobile/wd/hub"), capabilities);
		}
		return Constant.driver;
	}
	
	public static void SetUpDevice(boolean InstallApp) throws Exception {
		LaunchApp.platform = platform;		
		Map<String, Object> params = new HashMap<>();
		if (Constant.Platform.equalsIgnoreCase("Android")) {
			// AndroidDriver driver;
			params.put("file", "PRIVATE:Runner.apk");
			params.put("resign", "true");
			params.put("instrument", "noinstrument");
			Constant.driver = startAppium_Android();
		} else {
			// RemoteWebDriver driver;
			Constant.driver = startAppium_IOS();
			params.put("file", "PRIVATE:Runner.ipa");
			params.put("resign", "true");
			params.put("instrument", "noinstrument");
		}		
		if(InstallApp) {
			Constant.driver.executeScript("mobile:application:install", params);
		}		
		Thread.sleep(5000);
		OpenApplication();	
	}
	
	public static void OpenApplication() throws Exception {
		//declare the Map for script parameters
		Map<String, Object> params = new HashMap<>();
		String Environment = Generic.ReadFromExcel("Env", "AI_TestData", 1);
		switch(Environment) {
		  case "stg":
			  if(Constant.Platform.equals("ios")) {
				  params.put("identifier", Generic.ReadFromExcel("BundleID_Stg_iOS", "AI_TestData", 1));
				  Constant.AppID = Generic.ReadFromExcel("BundleID_Stg_iOS", "AI_TestData", 1); 
			  }
			  else {
				  params.put("identifier", Generic.ReadFromExcel("BundleID_Stg_Android", "AI_TestData", 1));
				  Constant.AppID = Generic.ReadFromExcel("BundleID_Stg_Android", "AI_TestData", 1); 
			  }	  
		    break;
		  case "dev":
			  params.put("identifier", Generic.ReadFromExcel("BundleID_Dev", "AI_TestData", 1));
			  Constant.AppID = Generic.ReadFromExcel("BundleID_Dev", "AI_TestData", 1);
		    break;
		  case "preprod":
			  params.put("identifier", Generic.ReadFromExcel("BundleID_Preprod", "AI_TestData", 1));
			  Constant.AppID = Generic.ReadFromExcel("BundleID_Preprod", "AI_TestData", 1);
		    break;
		}
		Constant.driver.executeScript("mobile:application:open", params);
	}

	public static String runCommand(String... params) {
		System.out.println("Start bash for downloading the app");
		ProcessBuilder pb = new ProcessBuilder(params);
		Process p;
		StringJoiner joiner = new StringJoiner(System.getProperty("line.separator"));
		try {
			p = pb.start();
			System.out.println("Download Started...");
			final BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			System.out.println("Waiting...");
			reader.lines().iterator().forEachRemaining(joiner::add);
			System.out.println("Waiting for the download process to end..");
			p.waitFor();
			p.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return joiner.toString();
	}

	public static void setLocation(String Location) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("address", Location);
		Constant.driver.executeScript("mobile:location:set", params); 
		System.out.println("Location Set: "+Location);
	}
	
	public static void getPlatform() throws Exception {
		FileInputStream fis = null;
		prop = new Properties();
		if(System.getProperty("os")==null)
			System.setProperty("os", Generic.ReadFromExcel("Platform", "AI_TestData", 1).toLowerCase());
		if(System.getProperty("env")==null)
			System.setProperty("env", Generic.ReadFromExcel("Env", "AI_TestData", 1).toLowerCase());
		currentDirectory = System.getProperty("user.dir");	
		if(System.getProperty("os").equalsIgnoreCase("ios"))
			fis = new FileInputStream(currentDirectory + "\\ios.properties");
		else if (System.getProperty("os").equalsIgnoreCase("Android"))
			fis = new FileInputStream(currentDirectory + "\\android.properties");
		prop.load(fis);
		utility.Constant.Platform = prop.getProperty("platform");
		utility.Constant.Env = prop.getProperty("env");
		if(prop.getProperty("platform").equalsIgnoreCase("ios")) {
			SCRIPT_NAME = "./app/iOS.sh";
			Main_Folder = System.getProperty("user.dir") + "\\app\\Runner.ipa";
			AppPathPerfecto= "PRIVATE:Runner.ipa";
			//utility.Constant.MSISDN = Generic.ReadFromExcel("LoginValidation", "MobileNumbers_iOS", 1);
		}else {
			SCRIPT_NAME = "./app/Android.sh";
			Main_Folder = System.getProperty("user.dir") + "\\app\\app.apk";
			AppPathPerfecto= "PRIVATE:Runner.apk";
			//utility.Constant.MSISDN = Generic.ReadFromExcel("LoginValidation", "MobileNumbers_Android", 1);
		}			
		if (prop.getProperty("Env").equals("public")){			
			/*
			 * String output = LaunchApp.runCommand(BASH_PATH, "-c", SCRIPT_NAME);
			 * System.out.println(output); PerfectoLabUtils.uploadMedia("mobilecloud",
			 * PerfectoLabUtils.fetchSecurityToken(Generic.ReadFromExcel("SecurityToken",
			 * "AI_TestData", 1)), Main_Folder, AppPathPerfecto);
			 */
		} else {
			/*
			 * String output = LaunchApp.runCommand(BASH_PATH, "-c", SCRIPT_NAME);
			 * System.out.println(output); PerfectoLabUtils.uploadMedia("techm-globe",
			 * PerfectoLabUtils
			 * .fetchSecurityToken(Generic.ReadFromExcel("SecurityTokenPrivate",
			 * "AI_TestData", 1)), Main_Folder, AppPathPerfecto );
			 */
		}
	}
	
	public static void OpenGmail() throws Exception {
		//declare the Map for script parameters
		Map<String, Object> params = new HashMap<>();
		String Environment = Generic.ReadFromExcel("Env", "AI_TestData", 1);
		params.put("identifier", "com.google.android.gm");
		Constant.driver.executeScript("mobile:application:open", params);
	}
	
	public static void launchGCash() throws Exception {
		//declare the Map for script parameters
		Map<String, Object> params = new HashMap<>();
		String bundleID = null;
		if(Constant.Platform.equalsIgnoreCase("ios")) {
			bundleID = Generic.ReadFromExcel("BundleID_Gcash_iOS", "AI_TestData", 1);
		}
		else {
			bundleID = Generic.ReadFromExcel("BundleID_Gcash_Android", "AI_TestData", 1);
		}
		Constant.GMP_APP = bundleID;
		params.put("identifier", bundleID);
		Constant.driver.executeScript("mobile:application:open", params);
	}
	
	public static void restartGCashApp() throws Exception
	{
		Map<String, Object> params = new HashMap<>();
		String bundleID = null;
		if(Constant.Platform.equalsIgnoreCase("ios")) {
			bundleID = Generic.ReadFromExcel("BundleID_Gcash_iOS", "AI_TestData", 1);
		}
		else {
			bundleID = Generic.ReadFromExcel("BundleID_Gcash_Android", "AI_TestData", 1);
		}
		Constant.GMP_APP = bundleID;
		params.put("identifier", bundleID);
		Constant.driver.executeScript("mobile:application:close", params);
		Constant.driver.executeScript("mobile:application:open", params);

	}	
}
