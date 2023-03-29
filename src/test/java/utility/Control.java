package utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.DecimalFormat;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.assertj.core.api.SoftAssertions;
//import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.net.UrlChecker;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.ShakesDevice;
import static com.epam.jdi.light.mobile.MobileUtils.executeDriverMethod;

import static io.appium.java_client.ios.IOSMobileCommandHelper.shakeCommand;
import io.appium.java_client.CommandExecutionHelper;
import io.appium.java_client.ExecutesMethod;

//simport com.sun.jna.StringArray;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;

import io.appium.java_client.MobileBy;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.remote.MobileCapabilityType;
import javax.swing.*;  
import com.perfecto.reportium.client.ReportiumClient;

public class Control {
	private static XSSFWorkbook workbook;
	private static XSSFSheet Worksheet;
	private static XSSFCell cell;
	private static SoftAssertions softAssertions;	
	static ReportiumClient reportiumClient;
	static Dimension windowSize = null;
	
	public static void delete(File file) throws IOException {
		if (file.isDirectory()) { 
			if (file.list().length == 0) {
				file.delete();
			} else {
				// list all the directory contents
				String files[] = file.list();
				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);
					// recursive delete
					delete(fileDelete);
				}
				// check the directory again, if empty then delete it
				if (file.list().length == 0) {
					file.delete();
					//System.out.println("Directory is deleted : "
						//	+ file.getAbsolutePath());
				}
			}
		} else {
			// if file, then delete it
			file.delete();
		}
	}
	
	public static void deleteTempFile() throws UnknownHostException {
		String property = "java.io.tmpdir";
		String temp = System.getProperty(property);
		File directory = new File(temp);
		try {
			delete(directory);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public static void OpenApplication(String browserName, String URL) throws UnknownHostException {
		deleteTempFile();
		if (browserName.equalsIgnoreCase("Firefox")) {
			System.setProperty("webdriver.gecko.driver", "C:\\WebDrivers\\geckodriver.exe");
			 Constant.driver_w= new FirefoxDriver();
		} else if (browserName.equalsIgnoreCase("safari")) {
			System.setProperty("webdriver.SafariDriver.driver", "C:\\WebDrivers\\SafariDriver.exe");
			Constant.driver_w = new SafariDriver();
		} else if (browserName.equalsIgnoreCase("Chrome")) {
			System.setProperty("webdriver.chrome.driver", "C:\\WebDrivers\\chromedriver.exe");
			Constant.driver_w= new ChromeDriver();
		} else if (browserName.equalsIgnoreCase("IE")) {
			System.setProperty("webdriver.ie.driver", "C:\\WebDrivers\\IEDriverServer.exe");
			Constant.driver_w =  new InternetExplorerDriver();
		} else if (browserName.equalsIgnoreCase("Opera")) {
			System.setProperty("webdriver.opera.driver", "C:\\WebDrivers\\operadriver.exe");
			Constant.driver_w = new OperaDriver();
		} else if (browserName.equalsIgnoreCase("Edge")) {
			System.setProperty("webdriver.edge.driver", "C:\\WebDrivers\\MicrosoftWebDriver.exe");
			Constant.driver_w = new EdgeDriver();
		}
		Constant.driver_w.manage().timeouts().implicitlyWait(Constant.defaultBrowserTimeOut, TimeUnit.SECONDS);
		Constant.driver_w.manage().deleteAllCookies();
		/*Constant.driver.manage().deleteAllCookies();
		Constant.driver.manage().window().maximize();*/
		//return driver;
		//Added By Ankit
		try {
			//Thread.sleep(10000);
			Constant.driver.get(URL);	
			Generic.WriteTestData("Open the Browser and URL",browserName,URL,"Able to Open the URL '"+URL+"' in Browser '"+browserName+"'","Opened  the URL '"+URL+"' in the Browser '"+browserName+"' successfully","Pass");
			} catch (Exception e) {	
		}
	}
	
	public static void highlightElement(WebDriver driver, WebElement element) {
//		JavascriptExecutor js = (JavascriptExecutor) driver;
//		js.executeScript("arguments[0].setAttribute('style', arguments[1]);",element, "border: 2px solid DeepPink;");
 	}
	
	public static void js_click(String PageName, String locator) throws Exception {
		Thread.sleep(3000);
		Constant.PageName=PageName;
		Constant.locator=locator;
		
		try {
			WebElement element = findElement(PageName,locator);
			if (element != null)
			{
				JavascriptExecutor js = (JavascriptExecutor) Constant.driver;  
				//Perform Click on LOGIN button using JavascriptExecutor    
				js.executeScript("arguments[0].click();", element);
				Generic.WriteTestData("Click on '"+locator+"'",locator,"","Able to click on '"+locator+"' button.","Clicked on '"+locator+"' button.","Passed");
			}
			else {
				Generic.WriteTestData("Click on '"+locator+"'",locator,"","Element should be available '"+locator+"' button.","Element was not found  '"+locator+"'","Failed");
			}
			element = null;		

		} catch (Exception e) {
			Generic.WriteTestData("Click on '"+locator+"'",locator,"","Able to click on '"+locator+"' button.","Clicking on '"+locator+"' button is unsuccessful.","Failed");
			System.out.println(" Error occured whlie click on the element "
					+ locator + " *** " + e.getMessage());
		}
	}   
	
	public static WebElement findElement(String PageName, String locatorName) throws InterruptedException   {
		
		Constant.webelement = null;
		String locator,locatorTag,objectLocator;
		System.out.println("Page name is : "+PageName);
		System.out.println("locatorName name is : "+locatorName);
		if (locatorName != null) {
			locator= Constant.Map.get(PageName).get(locatorName);
			String[] arrLocator = locator.split("#");
			 locatorTag = arrLocator[0].trim();
			 objectLocator = arrLocator[1].trim();
			 
			 System.out.println(locatorTag);
			 System.out.println(objectLocator);
			 
			try {				
				if (locatorTag.equalsIgnoreCase("id")) {
					Constant.webelement = Constant.driver.findElement(By.id(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				} else if (locatorTag.equalsIgnoreCase("name")) {
					Constant.webelement = Constant.driver.findElement(By.name(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				} else if (locatorTag.equalsIgnoreCase("xpath")) {
					Constant.webelement = Constant.driver.findElement(By.xpath(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				} else if (locatorTag.equalsIgnoreCase("linkText")) {
					Constant.webelement = Constant.driver.findElement(By.linkText(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				} else if (locatorTag.equalsIgnoreCase("Text")) {
					Constant.webelement = Constant.driver.findElement(By.partialLinkText(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				} else if (locatorTag.equalsIgnoreCase("class")) {
					Constant.webelement = Constant.driver.findElement(By.className(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				} else if (locatorTag.equalsIgnoreCase("tagName")) {
					Constant.webelement = Constant.driver.findElement(By.tagName(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);	
				} else if (locatorTag.equalsIgnoreCase("css")) {
					Constant.webelement = Constant.driver.findElement(By.cssSelector(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				} 
				else if (locatorTag.equalsIgnoreCase("accessID")) {
					Constant.webelement = Constant.driver.findElement(MobileBy.AccessibilityId((objectLocator)));
					highlightElement(Constant.driver, Constant.webelement);	
				} 
				else if (locatorTag.equalsIgnoreCase("css")) {
					Constant.webelement = Constant.driver.findElement(By.cssSelector(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				}
					else {
					String error = "Please Check the Given Locator Syntax :"+ locator;
					error = error.replaceAll("'", "\"");
					return null;
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
		
    return Constant.webelement;
	}

	public static WebElement findElementWithoutPrintingException(String PageName, String locatorName) throws InterruptedException   {
		
		Control.setDriverTimeOut(Constant.MinDriverTimeOut);
		Constant.webelement = null;
		String locator,locatorTag,objectLocator;
		System.out.println("Page name is : "+PageName);
		System.out.println("locatorName name is : "+locatorName);
		if (locatorName != null) {
			locator= Constant.Map.get(PageName).get(locatorName);
			String[] arrLocator = locator.split("#");
			 locatorTag = arrLocator[0].trim();
			 objectLocator = arrLocator[1].trim();
			 
			 System.out.println(locatorTag);
			 System.out.println(objectLocator);
			 
			try {
				if (locatorTag.equalsIgnoreCase("id")) {
					Constant.webelement = Constant.driver.findElement(By.id(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				} else if (locatorTag.equalsIgnoreCase("name")) {
					Constant.webelement = Constant.driver.findElement(By.name(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				} else if (locatorTag.equalsIgnoreCase("xpath")) {
					Constant.webelement = Constant.driver.findElement(By.xpath(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				} else if (locatorTag.equalsIgnoreCase("linkText")) {
					Constant.webelement = Constant.driver.findElement(By.linkText(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				} else if (locatorTag.equalsIgnoreCase("Text")) {
					Constant.webelement = Constant.driver.findElement(By.partialLinkText(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				} else if (locatorTag.equalsIgnoreCase("class")) {
					Constant.webelement = Constant.driver.findElement(By.className(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				} else if (locatorTag.equalsIgnoreCase("tagName")) {
					Constant.webelement = Constant.driver.findElement(By.tagName(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);	
				} else if (locatorTag.equalsIgnoreCase("css")) {
					Constant.webelement = Constant.driver.findElement(By.cssSelector(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				} 
				else if (locatorTag.equalsIgnoreCase("accessID")) {
					Constant.webelement = Constant.driver.findElement(MobileBy.AccessibilityId((objectLocator)));
					highlightElement(Constant.driver, Constant.webelement);	
				}
				else if (locatorTag.equalsIgnoreCase("css")) {
					Constant.webelement = Constant.driver.findElement(By.cssSelector(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				}
				else if (locatorTag.equalsIgnoreCase("predicate") || locatorTag.equalsIgnoreCase("iospredicate")) {
					Constant.webelement = Constant.driver.findElement(MobileBy.iOSNsPredicateString(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);	
				}
				else if (locatorTag.equalsIgnoreCase("classchain") || locatorTag.equalsIgnoreCase("iosclasschain")) {
					Constant.webelement = Constant.driver.findElement(MobileBy.iOSClassChain(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);	
				}
				else {
					String error = "Please Check the Given Locator Syntax :"+ locator;
					error = error.replaceAll("'", "\"");
					Control.setDriverTimeOut(Constant.MaxDriverTimeOut);
					return null;
				}
			} catch (Exception exception) {
				//exception.printStackTrace();
				Control.setDriverTimeOut(Constant.MaxDriverTimeOut);
			}
		}
		
    return Constant.webelement;
	}

	public static WebElement findElementWithTimeOut(String PageName, String locatorName, int timeout) throws InterruptedException   {	
		Control.setDriverTimeOut(timeout);
		Constant.webelement = null;
		String locator,locatorTag,objectLocator;
		System.out.println("Page name is : "+PageName);
		System.out.println("locatorName name is : "+locatorName);
		if (locatorName != null) {
			locator= Constant.Map.get(PageName).get(locatorName);
			String[] arrLocator = locator.split("#");
			
			 locatorTag = arrLocator[0].trim();
			 objectLocator = arrLocator[1].trim();
			 
			 System.out.println(locatorTag);
			 System.out.println(objectLocator);
			 
			try {
				if (locatorTag.equalsIgnoreCase("id")) {
					Constant.webelement = Constant.driver.findElement(By.id(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				} else if (locatorTag.equalsIgnoreCase("name")) {
					Constant.webelement = Constant.driver.findElement(By.name(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				} else if (locatorTag.equalsIgnoreCase("xpath")) {
					Constant.webelement = Constant.driver.findElement(By.xpath(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				} else if (locatorTag.equalsIgnoreCase("linkText")) {
					Constant.webelement = Constant.driver.findElement(By.linkText(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				} else if (locatorTag.equalsIgnoreCase("Text")) {
					Constant.webelement = Constant.driver.findElement(By.partialLinkText(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				} else if (locatorTag.equalsIgnoreCase("class")) {
					Constant.webelement = Constant.driver.findElement(By.className(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				} else if (locatorTag.equalsIgnoreCase("tagName")) {
					Constant.webelement = Constant.driver.findElement(By.tagName(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);	
				} else if (locatorTag.equalsIgnoreCase("css")) {
					Constant.webelement = Constant.driver.findElement(By.cssSelector(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				} 
				else if (locatorTag.equalsIgnoreCase("accessID")) {
					Constant.webelement = Constant.driver.findElement(MobileBy.AccessibilityId((objectLocator)));
					highlightElement(Constant.driver, Constant.webelement);	
				} 
				else if (locatorTag.equalsIgnoreCase("css")) {
					Constant.webelement = Constant.driver.findElement(By.cssSelector(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				}
					else {
					String error = "Please Check the Given Locator Syntax :"+ locator;
					error = error.replaceAll("'", "\"");
					Control.setDriverTimeOut(Constant.MaxDriverTimeOut);
					return null;
				}
			} catch (Exception exception) {
				//exception.printStackTrace();
				Control.setDriverTimeOut(Constant.MaxDriverTimeOut);
			}
		}
		
    return Constant.webelement;
	}

	public static WebElement simpleFindElement(String PageName, String locatorName) throws InterruptedException   {
		
		String locator,locatorTag,objectLocator;
		if (locatorName != null) {
			Thread.sleep(1000);
			locator= Constant.Map.get(PageName).get(locatorName);
			String[] arrLocator = locator.split("#");
			 locatorTag = arrLocator[0].trim();
			 objectLocator = arrLocator[1].trim();
			 
			 System.out.println(locatorTag);
			 System.out.println(objectLocator);
			 
			try {
				if (locatorTag.equalsIgnoreCase("id")) {
					Constant.webelement = Constant.driver.findElement(By.id(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				} else if (locatorTag.equalsIgnoreCase("name")) {
					Constant.webelement = Constant.driver.findElement(By.name(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				} else if (locatorTag.equalsIgnoreCase("xpath")) {
					Constant.webelement = Constant.driver.findElement(By.xpath(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				} else if (locatorTag.equalsIgnoreCase("linkText")) {
					Constant.webelement = Constant.driver.findElement(By.linkText(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				} else if (locatorTag.equalsIgnoreCase("Text")) {
					Constant.webelement = Constant.driver.findElement(By.partialLinkText(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				} else if (locatorTag.equalsIgnoreCase("class")) {
					Constant.webelement = Constant.driver.findElement(By.className(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				} else if (locatorTag.equalsIgnoreCase("tagName")) {
					Constant.webelement = Constant.driver.findElement(By.tagName(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);	
				} else if (locatorTag.equalsIgnoreCase("css")) {
					Constant.webelement = Constant.driver.findElement(By.cssSelector(objectLocator));
					highlightElement(Constant.driver, Constant.webelement);
				} else {
					String error = "Please Check the Given Locator Syntax :"+ locator;
					error = error.replaceAll("'", "\"");
					return null;
				}
			} catch (Exception exception) {

			}
		}
		
		
		return Constant.webelement;
	}
	
	public static String getAttribute(String PageName,String locator, String attributeName) {
		String attributeValue = null;
		try {

			WebElement element = findElement( PageName,locator);
			if (element != null)
				attributeValue = element.getAttribute(attributeName);
			element = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attributeValue;
	}
	
	public static String compareTextByAttribute(String pageName,String locator, String text, String attributeName) {
		String attributeValue = null;
		try {

			WebElement element = findElement(pageName,locator);
			if (element != null) {
				attributeValue  = element.getAttribute(attributeName);

				if(attributeValue.contains(text)) {
					Generic.WriteTestData(pageName, "", "", locator + " should have '" + text + "' text", "Expected text is correct", "Pass");
				}
				else {
					Generic.WriteTestData(pageName, "", "", locator + " should have '" + text + "' text", "Expected text " + text + " is not the same with " + attributeValue, "Fail");
					Control.takeScreenshot();
				}
				
			}
			else {
				Generic.WriteTestData(pageName, "", "", "Should have be able to get text from" + locator, "Element is null", "Fail");
				Control.takeScreenshot();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return attributeValue;
	}

	public static void enterText(String PageName, String locator, String value) throws Exception {
		Constant.PageName=PageName;
		Constant.locator=locator;
		try {
			WebElement element = findElement(PageName,locator);
			if (element != null) 
			{
				element.click();
				Thread.sleep(5000);
				sendKeys(value);
				Generic.WriteTestData("Entering text '"+value+"' in '"+locator+"' text field",locator,value,"Should able to enter data'"+value+"' into  '"+locator+"' text field","Entered data'"+value+"' into  '"+locator+"' text field successfully","Pass");
			}else {
				Generic.WriteTestData("Entering text '"+value+"' in '"+locator+"' text field",locator,value,"Unable to identify the '"+locator+"' text field","Not able to identify the  '"+locator+"' text field","Fail");
            	Constant.Failures += "Failed to enter text in "+locator+ "  in page "+PageName+ "\n";
				softAssertions.fail("Failed to enter text in "+locator+ "  in page "+PageName);

			}		
			element = null;
		} catch (Exception e) {
			Generic.WriteTestData("Entering text '"+value+"' in '"+locator+"' text field",locator,value,"Should able to enter data'"+value+"' into  '"+locator+"' text field","Not able to enter data'"+value+"' into  '"+locator+"' text field successfully","Fail");
			e.printStackTrace();
        	//Constant.Failures += "Failed to enter text in "+locator+ "  in page "+PageName+ "\n";
			//softAssertions.fail("Failed to enter text in "+locator+ "  in page "+PageName);
		}
	}
	
	public static void sendKeys1(String PageName, String label, String text) throws Exception {
		String getLabel = null;
		if (label != null) {
			if (PageName != "") {
				System.out.println(Constant.labelMap);
				getLabel = Constant.labelMap.get(PageName).get(label);
			}
			else
				getLabel = label;
		}
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("report", "Screenshot");
			params.put("report.resolution", "Medium");
			params.put("label", getLabel);
			params.put("text", text);
			String res = (String) Constant.driver.executeScript("mobile:edit-text:set", params);		
			Thread.sleep(1000);
			Control.takeScreenshot();
			Generic.WriteTestData("Sending text in to the text box", label, text,
					"Should be able to send text: " + text + " to the: " + label, "Same as expected", "Pass");

		} catch (Exception e) {
			e.printStackTrace();
			Generic.WriteTestData("Sending text in to the text box", label, text,
					"Should be able to send text: " + text + " to the: " + label, "Not the same as  expected", "Fail");
		}
		Thread.sleep(4000);
	}

	public static void clearEnterText(String PageName, String locator, String value) throws Exception {
		Constant.PageName=PageName;
		Constant.locator=locator;
		try {
			WebElement element = findElement(PageName,locator);
			if (element != null) {
				try {
					element.clear();
				} catch (Exception e) {}
				Thread.sleep(2000);
				element.click();
				Thread.sleep(2000);
				Actions action =new Actions(Constant.driver);
				sendKeys(value);
				Generic.WriteTestData("Entering text '"+value+"' in '"+locator+"' text field",locator,value,"Should able to enter data'"+value+"' into  '"+locator+"' text field","Entered data'"+value+"' into  '"+locator+"' text field successfully","Pass");
			}
			else {
				Generic.WriteTestData("Entering text '"+value+"' in '"+locator+"' text field",locator,value,"Unable to identify the '"+locator+"' text field","Not able to identify the  '"+locator+"' text field","Fail");
            	Constant.Failures += "Failed to enter text in "+locator+ "  in page "+PageName+ "\n";
				softAssertions.fail("Failed to enter text in "+locator+ "  in page "+PageName);
			}		
			element = null;
		} catch (Exception e) {
			Generic.WriteTestData("Entering text '"+value+"' in '"+locator+"' text field",locator,value,"Should able to enter data'"+value+"' into  '"+locator+"' text field","Not able to enter data'"+value+"' into  '"+locator+"' text field successfully","Fail");
			e.printStackTrace();
		}
	}
	
	public static void clickWithoutReporting(String PageName, String locator) throws Exception {
		Constant.PageName=PageName;
		Constant.locator=locator;
		System.out.println(PageName);
		System.out.println(locator);
		
		try {
			WebElement element = findElement(PageName,locator);
			if (element != null) {
				
				
				int x=element.getLocation().getX();
				int y=element.getLocation().getY();
				String clickCommand="adb shell input tap "+x+" "+y;
				Control.RunProcessWithOutput(clickCommand, 30, true);
				Generic.WriteTestData("Click on '"+locator+"'",locator,"","Able to click on '"+locator+"' button.","Clicked on '"+locator+"' button.","Pass");
			
			}
			else {
				//Generic.WriteTestData("Click on '"+locator+"'",locator,"","Element should be available '"+locator+"' button.","Element was not found  '"+locator+"'","Fail");
			}
			element = null;		
			}
		catch (Exception e) {		
			e.printStackTrace();			
		}				
	}

	public static void click(String PageName, String locator) throws Exception {
	    Constant.PageName=PageName;
	    Constant.locator=locator;
	    
		if(locator.equalsIgnoreCase("Back")) {
			Thread.sleep(7000);
		}

		try {
			WebElement element = findElement(PageName,locator);
			
			if (element != null) {				
				element.click();	
				Generic.WriteTestData("Click on '"+locator+"'",locator,"","Able to click on '"+locator+"' button.","Clicked on '"+locator+"' button.","Pass");	
			}
			else {
				Generic.WriteTestData("Click on '"+locator+"'",locator,"","Element should be available '"+locator+"' button.","Element was not found  '"+locator+"'","Fail");
				 Constant.Failures += "Failed to click "+locator+ "in page "+PageName + ", \n";
							
			}
			element = null;		
		}
		catch (Exception e) {
		  	e.printStackTrace();
			System.out.println(" Error occured while click on the element " + locator + " *** " + e.getMessage());
			Constant.Failures += "Failed to click "+locator+ "in page "+PageName + ", \n";
			throw new Exception ("Failed to click "+locator+ "in page "+PageName);
		}	
	}	
	
	public static void click_when_exception_occurs(String PageName, String locator) throws Exception {
		
		Constant.PageName=PageName;
		Constant.locator=locator;
		
		System.out.println(PageName);
		System.out.println(locator);
		
		int z=0;
		int counter=0;
	    WebElement element=null;
		while(counter<=3)
		{
		 try
			{
				System.out.println("counter is"+counter);
				element=Control.findElement(PageName,locator);
				if(element!=null)
				{
					while(z<3)
					{
						z++;
						element.click();
						//String clickCommand="adb shell input tap "+x+" "+y;
						//Control.RunProcessWithOutput(clickCommand, 30, false);
						break;
					}
					//element.click();	
					Generic.WriteTestData("Click on '"+locator+"'",locator,"","Able to click on '"+locator+"' button.","Clicked on '"+locator+"' button.","Pass");
					break;
				} 
				}
				catch (Exception e) 
				{
				 e.printStackTrace();
				}
				Thread.sleep(1000);
				counter+=1;
				}
			}

	public static void hover(String PageName, String locator) throws Exception {
		Constant.PageName=PageName;
		Constant.locator=locator;
		
		try {
			WebElement element = findElement(PageName,locator);
			Actions action = new Actions(Constant.driver);
			if (element != null) {
				action.moveToElement(element).build().perform();
				Generic.WriteTestData("Hover on '"+locator+"'",locator,"","Able to Hover on '"+locator+"' button.","Hovered on '"+locator+"' .","Pass");
			}
			else {
				Generic.WriteTestData("Hover on '"+locator+"'",locator,"","Element should be available '"+locator+"' .","Element was not found  '"+locator+"'","Fail");
			}
			element = null;		

		} catch (Exception e) {
			Generic.WriteTestData("Hover on '"+locator+"'",locator,"","Able to Hover on '"+locator+"' .","Hover on '"+locator+"'  is unsuccessful.","Fail");
			System.out.println(" Error occured whlie Hover on the element "
					+ locator + " *** " + e.getMessage());
			
		}
		
	}
	
	public static void GoToUrl(String url) {
		try {
			Constant.driver.get(url);			
			} catch (Exception e) {
		}
	}
	
	public static void listSelect(String PageName, String locator, String listValue) {
		Constant.PageName=PageName;
		Constant.locator=locator;
		
		try {
			WebElement element = findElement(PageName,locator);
		
			if (element != null) {
				Select dropdown= new Select(element);
				dropdown.selectByVisibleText(listValue);
				element.click();			
				Generic.WriteTestData("select from '"+locator+"'",locator,"","Able to select '"+listValue+"' from '"+locator+ "'" ,"Able to select '"+listValue+"' from '"+locator ,"Pass");
			}
			else {
				
				Generic.WriteTestData("select from '"+locator+"'",locator,"","Able to select '"+listValue+"' from '"+locator+ "'" ,"Not able to select '"+listValue+"' from '"+locator ,"Fail");
			}
			element = null;		

		} catch (Exception e) {
			System.out.println(" Error occured whlie click on the element "
					+ locator + " *** " + e.getMessage());	
		}	
	}
	
	public static void Checkbox(String PageName, String locator, String CheckBoxVal) {
		Constant.PageName=PageName;
		Constant.locator=locator;
		boolean boxVal ;
		if (CheckBoxVal == "ON") {
			boxVal = true;
		}
		else {
				boxVal = false;
		}
		
		boolean bValue = false;
		try {
			WebElement element = findElement(PageName,locator);
			
			//WebElement element = findElement(PageName,locator);
			if (element != null) {
				bValue =  element.isSelected();
				if (bValue != boxVal) {
					element.click();	
				}
				Generic.WriteTestData("Click on '"+locator+"'",locator,"","Able to click on '"+locator+"' Checkbox/RadioButton.","Clicked on '"+locator+"' Checkbox/RadioButton.","Pass");
			}
			else {
				
				Generic.WriteTestData("Click on '"+locator+"'",locator,"","Able to click on '"+locator+"' Checkbox/RadioButton.","Clicking on '"+locator+"' Checkbox/ is unsuccessful.","Fail");
			}
			element = null;		

		} catch (Exception e) {
			System.out.println(" Error occured whlie click on the element "
					+ locator + " *** " + e.getMessage());	
		}
	}	
	
	public static void RadioButton(String PageName, String locator) {
		Constant.PageName=PageName;
		Constant.locator=locator;
		try {
			WebElement element = findElement(PageName,locator);
			System.out.println(element);
			if (element != null) {
				element.click();	
				Generic.WriteTestData("Click on '"+locator+"'",locator,"","Able to click on '"+locator+"' button.","Clicked on '"+locator+"' button.","Pass");
			}
			else {
				
				Generic.WriteTestData("Click on '"+locator+"'",locator,"","Able to click on '"+locator+"' button.","Clicking on '"+locator+"' button is unsuccessful.","Fail");
			}
			element = null;		

		} catch (Exception e) {
			System.out.println(" Error occured whlie click on the element "
					+ locator + " *** " + e.getMessage());	
		}
	}

	public static void compareText(String PageName,String locator,String ExpectedText) throws Exception
	{
		ExpectedText=ExpectedText.replace("RECEIVE_TEXTMSG_", "").trim();
		Constant.PageName=PageName;
        Constant.locator=locator;
        String text = null;
        
         WebElement element = findElement(PageName,locator);      
         if(element==null)
         {
        	 element = findElement(PageName,locator); 
         }
        try {
            if (element != null)                
                text = element.getText();
            if(ExpectedText.equals(text)) {
                Generic.WriteTestData("Comparing text of field '"+locator+"' in Page '"+PageName,locator,ExpectedText,"The ExpectedText '"+ExpectedText+"' should be equal to Actual text from Application","The ExpectedText '"+ExpectedText+"' is same as the Acutal Text '"+text+"'","Pass");
            }
            else if(text.contains(ExpectedText)) {
                Generic.WriteTestData("Comparing text of field '"+locator+"' in Page '"+PageName,locator,ExpectedText,"The ExpectedText '"+ExpectedText+"' should be equal to Actual text from Application","The ExpectedText '"+ExpectedText+"' is same as the Acutal Text '"+text+"'","Pass");

            }
            else {
            	Generic.WriteTestData("Comparing text of field '"+locator+"' in Page '"+PageName,locator,ExpectedText,"The ExpectedText '"+ExpectedText+"' should be equal to Actual text from Application","The ExpectedText '"+ExpectedText+"' is not same as the Acutal Text '"+text+"'","Fail");
     			softAssertions.fail("The ExpectedText '"+ExpectedText+"' is not same as the Acutal Text '"+text);
            	Constant.Failures += "The ExpectedText '"+ExpectedText+"' is not same as the Acutal Text '"+text+"+, \n";
            }        
        }catch (Exception e) {
        	//Control.takeScreenshot();
        	Generic.WriteTestData("Comparing text of field '"+locator+"' in Page '"+PageName,locator,ExpectedText,"The ExpectedText '"+ExpectedText+"' should be equal to Actual text from Application","The ExpectedText '"+ExpectedText+"' is not same as the Acutal Text '"+text+"'","Fail");
			 Constant.Failures += "The ExpectedText '"+ExpectedText+"' is not same as the Acutal Text '"+text+"+, \n";
        	e.printStackTrace();
        }
        element = null;
    }    
	
	public static boolean compareSMSWithDynamicTags(String ExpectedText, String text)
	{

		text=text.toLowerCase();
		ExpectedText=ExpectedText.toLowerCase();
		
		if(ExpectedText.contains("<"))
		{
			String[] msgArray=ExpectedText.split("<");
			if(text.contains(msgArray[0].trim()))
			{
				//since the match is already in place, lets remove it from incoming message
				text.replaceFirst(msgArray[0], "");
				
				//moving on to match the next part
				ExpectedText=msgArray[1];
				
				
				if(ExpectedText.contains(">"))
				{
					try
					{
						//> is the last char
						msgArray=ExpectedText.split(">");
					}
					catch(Exception e)
					{
						//true as there is nothing left to compare
						if(ExpectedText.charAt(ExpectedText.length()-1)=='>')
							{
								System.out.println("Returning true as this is the last character");
								return true;
							}
							
						else
							{
								System.out.println("Returning false as exception occured : "+e.getMessage());
								e.printStackTrace();
								return false;	
							}
							
					}
					
					try
					{
						if(text.contains(msgArray[1].trim()))
						{
							text.replaceFirst(msgArray[1], "");
							System.out.println("Returning true as match");
							return true;
						}
						
					}
					catch(Exception e)
					{
						if(ExpectedText.charAt(ExpectedText.length()-1)=='>')
						{
							System.out.println("Returning true as this is the last character");
							return true;
						}
						else
						{
							System.out.println("Exception encountered : "+e.getMessage());
							e.printStackTrace();
							return false;
						}
					}
					
					
	
					compareSMSWithDynamicTags(msgArray[1], text);
					
				}
				else
				{
					//< tag was not closed, do entire string comparison
					if(text.contains(ExpectedText))
					{
						//pass
						System.out.println("Returning true as match (tag was not closed)");
						return true;
					}
					else
					{
						//fail
						System.out.println("Returning true as not match (tag was not closed)");
						return false;
					}
					
					
				}
			}
			
		}
		else
		{
			//just a regular comparison
			
			System.out.println("Regular comparison : \nText : "+text+"\nExpected Text : "+ExpectedText);
			
			
			if(text.contains(ExpectedText))
			{
				//pass
				System.out.println("Returning true on Regular Comparison");
				return true;
			}
	
		}
		System.out.println("Returning false by default");
		return false;
		
	}
	
	public static boolean compareTextSMSBackend(String ExpectedText, String text) throws Exception{
		
	//text = SMS Received
	//this does not write any data to the excel, please handle outside the function explicitly
	
		Generic.printSimilarity(ExpectedText, text);
		if(ExpectedText==null)
		{
			ExpectedText="";
			return true;
		}

		else
		{
			try
			{
				 if(text.contains(ExpectedText) || compareSMSWithDynamicTags(ExpectedText,text) || Float.compare(Generic.printSimilarity(text, ExpectedText.replace("<", "").replace(">", "")),Constant.smsPercentageMatch)>=0) {
		                //Generic.WriteTestData("Comparing SMS Received vs checkpoint","",ExpectedText,"The ExpectedText '"+ExpectedText+"' should be equal to Actual text from Application","The ExpectedText '"+ExpectedText+"' is same as the Acutal Text '"+text+"'","Pass");
		                return true;
				 	}
	
		            else
		            {
		            	//Control.takeScreenshot();
		            	//Generic.WriteTestData("Comparing SMS Received vs checkpoint","",ExpectedText,"The ExpectedText '"+ExpectedText+"' should be equal to Actual text from Application","The ExpectedText '"+ExpectedText+"' is not same as the Acutal Text '"+text+"'","Fail");
		            	return false;
		            }
			}
	           
	            
	        catch (Exception e) {
	        	//Control.takeScreenshot();
	        	//Generic.WriteTestData("Comparing SMS Received vs checkpoint","",ExpectedText,"The ExpectedText '"+ExpectedText+"' should be equal to Actual text from Application","The ExpectedText '"+ExpectedText+"' is not same as the Acutal Text '"+text+"'","Fail");
	            e.printStackTrace();
	        }
	       
		}
	
		
		
		return false;
        
        
    }    
    
	public static void compareName(String Page, String Locator, String Text) throws Exception
	{
		String Textfield = null;
		try 
		{
			Textfield = Control.findElement(Page, Locator).getAttribute("name");
			if(Textfield.equals(Text))
			{
				Generic.WriteTestData("Comparing text of field '"+Locator+"' in Page '"+Page, Locator, Text,"The ExpectedText '"+Text+"' should be equal to Actual text from Application","The ExpectedText '"+Text+"' is the same as the Acutal Text '"+Textfield+"'","Pass");
			}
			else
			{
				Generic.WriteTestData("Comparing text of field '"+Locator+"' in Page '"+Page, Locator, Text,"The ExpectedText '"+Text+"' should be equal to Actual text from Application","The ExpectedText '"+Text+"' is not same as the Acutal Text '"+Textfield+"'","Fail");
			}
		}
		catch (Exception e) 
		{
        	Generic.WriteTestData("Comparing text of field '"+Locator+"' in Page '"+Page, Locator, Text,"The ExpectedText '"+Text+"' should be equal to Actual text from Application","The ExpectedText '"+Text+"' is not same as the Acutal Text '"+Textfield+"'","Fail");
        	e.printStackTrace();
		}
	}
	
	public static String ReadBal(String PageName,String locator) throws Exception{
        Thread.sleep(5000);
         Constant.PageName=PageName;
        Constant.locator=locator;
        String text = null;
        String[] arr1 = null;
        
         WebElement element = findElement(PageName,locator);      
         if(element==null)
         {
        	 element = findElement(PageName,locator); 
         }
        try {
            if (element != null)                
                text = element.getText();
            	String[] arr = text.split("\n");
            	arr1 = arr[0].split(" ");
            	//arr2 = arr1[0].split("Bal");
            	
            	System.out.println(arr1[1]);
        }catch (Exception e) {
        	//Control.takeScreenshot();
        	 e.printStackTrace();
        }
        element = null;
		
		return arr1[1];  
    }    
    
	
	public static void objProperty(String PageName,String locator,String ExpectedPropertyToBeVerified, String ExpectedPropertyValue) throws InterruptedException {
        Constant.PageName=PageName;
        Constant.locator=locator;
        String text = null;
    
        WebElement element = findElement(PageName,locator);
        try {
            if (element != null)    
            
                text = element.getAttribute(ExpectedPropertyToBeVerified);
            if(ExpectedPropertyValue.equalsIgnoreCase(text)) {
                Generic.WriteTestData("Comparing property of field '"+locator+"' in Page '"+PageName+"'",locator,ExpectedPropertyValue,"The Expected property '"+ExpectedPropertyValue+"' should be equal to Actual property from Application","Acutal Property '"+text+"' equals to Expected Property '"+ExpectedPropertyValue+"'","Pass");
            }
            
            else
            {
                takeScreenshot();
                Generic.WriteTestData("Comparing property of field '"+locator+"' in Page '"+PageName+"'",locator,ExpectedPropertyValue,"The Expected property '"+ExpectedPropertyValue+"' should be equal to Actual property from Application","Acutal property '"+text+"' is NOT equals to Expected property '"+ExpectedPropertyValue+"'","Fail");
            }            
        }catch (Exception e) {
            e.printStackTrace();
        }
        element = null;
    }
	
	@Test
	public static boolean objExists(String PageName,String locator,boolean ExistsOnPage) throws InterruptedException {
		Constant.PageName=PageName;
		Constant.locator=locator;
		boolean text = false;
		WebElement element = findElement(PageName,locator);
		try {
			if (element != null)   
				text = element.isDisplayed();
			if(ExistsOnPage) {
				if(text== true) {
					Generic.WriteTestData("Check visibility of locator '"+locator+"' in Page '"+PageName+"'",locator,"","'"+locator+"' should be visible on the Application","'"+locator+"' is visible on the page '"+PageName+"'","Pass");    
				}
				else
				{
					takeScreenshot();
					Generic.WriteTestData("Check visibility of locator '"+locator+"' in Page '"+PageName+"'",locator,"","'"+locator+"' should be visible on the Application","'"+locator+"' is Not visible on the page '"+PageName+"'","Fail");
					Constant.Failures += locator+ " not found in page "+PageName + ", \n";
				}                
			}            
			else
			{
				if(text == true) {
					takeScreenshot();
					Generic.WriteTestData("Check visibility of locator '"+locator+"' in Page '"+PageName+"'",locator,"","'"+locator+"' should not be visible on the Application","'"+locator+"' is visible on the page '"+PageName+"'","Fail");
		            Constant.Failures += "Failed to enter text in "+locator+ "  in page "+PageName+ "\n";
					Constant.Failures += locator+ " not found in page "+PageName + ", \n";
				}
				else
				{
					Generic.WriteTestData("Check visibility of locator '"+locator+"' in Page '"+PageName+"'",locator,"","'"+locator+"' should not be visible on the Application","'"+locator+"' is not visible on the page '"+PageName+"'","Pass");
				}
			}        
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		element = null;
		return text;
	}    
	
	public static boolean isElementSelected(String PageName,String locator,boolean ExistsOnPage) throws InterruptedException {
		Constant.PageName=PageName;
		Constant.locator=locator;
		boolean text = false;
		WebElement element = findElement(PageName,locator);
		try {
			if (element != null)   
				text = element.isSelected();
			if(ExistsOnPage) {
				if(text== true) {
					Generic.WriteTestData("Check if locator '"+locator+"' in Page '"+PageName+"' is selected",locator,"","'"+locator+"' should be selected on the Application","'"+locator+"' is selected on the page '"+PageName+"'","Pass");    
				}
				else
				{
					takeScreenshot();
					Generic.WriteTestData("Check if locator '"+locator+"' in Page '"+PageName+"' is selected",locator,"","'"+locator+"' should be selected on the Application","'"+locator+"' is not selected on the page '"+PageName+"'","Fail");
					Constant.Failures += locator+ " not found in page "+PageName + ", \n";
				}                
			}            
			else
			{
				if(text == true) {
					takeScreenshot();
					Generic.WriteTestData("Check if locator '"+locator+"' in Page '"+PageName+"' is selected",locator,"","'"+locator+"' should not be selected on the Application","'"+locator+"' is selected on the page '"+PageName+"'","Fail");
		            Constant.Failures += "Failed to enter text in "+locator+ "  in page "+PageName+ "\n";
					Constant.Failures += locator+ " not found in page "+PageName + ", \n";
				}
				else
				{
					Generic.WriteTestData("Check if locator '"+locator+"' in Page '"+PageName+"' is selected",locator,"","'"+locator+"' should not be selected on the Application","'"+locator+"' is not selected on the page '"+PageName+"'","Pass");
				}
			}        
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		element = null;
		return text;
	}    
	
	public static String HaWindleWebAlert(boolean Accept) throws Exception{
    	Thread.sleep(20000);
        Alert alert = Constant.driver.switchTo().alert();
        String textonalert = alert.getText();
        if(Accept) { 
            alert.accept();
        }
        else
        {
            alert.dismiss();
        }
        return textonalert;    
    }
	
	public static void enterPin1(String pin) throws Exception {
		String[] enteredPin = pin.split("");
		String value = null;
		String keyCode = null;
		try {
			System.out.println("Start entering: " + pin);
			for (int i = 0; i < enteredPin.length; i++) {
				value = enteredPin[i];
				keyCode = getKeyCode(value);
				sendKeyEvent(keyCode, 0);
			}
			Generic.WriteTestData("Able to enter pin", "", pin, "Able to enter pin: " + pin, "Same as expected",
					"Pass");
			Thread.sleep(4500);
			Control.takeScreenshot();
		} catch (Exception e) {
			Generic.WriteTestData("Able to enter pin", "", pin, "Able to enter pin: " + pin, "Not the same as expected",
					"Fail");
			Control.takeScreenshot();
		}

	}

	public static void clickByLocation1(String location) throws Exception {
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("location", location);
         	params.put("duration" , 1);
			System.out.println("Click by location: " + location);
			if(Constant.driver==null) {
				Object res = Constant.androiddriver.executeScript("mobile:touch:tap", params);
			}
			else {
				Object res = Constant.driver.executeScript("mobile:touch:tap", params);
			}
			Thread.sleep(2000);
			Control.takeScreenshot();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public static void takeScreenshot()throws Exception {
    	takeScreenshot(Constant.DefaultoptionalFlag);
    }
    
    public static void ScreenshotViaAdb(String SSPathWithName)
    {
    	System.out.print("cant capture ss through ADB");
    }
    
    public static void takeScreenshot(boolean optionalFlag)throws Exception {
    	Thread.sleep(5);
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
		Date date= new Date();
		String Date1 = dateFormat.format(date);
		//DateFormat dateFormat1 = new SimpleDateFormat("HHMMSS");
		DateFormat dateFormat1 = new SimpleDateFormat("HHmmssSSS");
		Date date2= new Date();
		String screenshotFilePath;
		String Date3 = dateFormat1.format(date2);
		
		if(Constant.locator==null)
			Constant.locator=""+System.currentTimeMillis();
		if(Constant.PageName==null)
			Constant.PageName=""+System.currentTimeMillis();
		
		 screenshotFilePath=Constant.ScreenshotFolderName+File.separator+Constant.PageName+"_" + Constant.locator + "_"+Date1+"_"+Date3+".png";

		
		 Constant.RecentScreenshot = Constant.PageName+"_" + Constant.locator + "_"+Date1+"_"+Date3+".png";
		 TakesScreenshot scrShot;
		try{                                        
			//int x=1/0;

				 scrShot = ((TakesScreenshot)Constant.driver);
	        File srcFile = scrShot.getScreenshotAs(OutputType.FILE);
	        File DestFile = new File(screenshotFilePath);
	        FileHandler.copy(srcFile,DestFile);	        
			    if(optionalFlag) {
			        Generic.setScreenshothyperlink(screenshotFilePath);}
			    else {
			    	Constant.DefaultoptionalFlag = true;
			    	
			    }
	         }catch (Exception e) {
            //e.printStackTrace();
	        	 e.printStackTrace();
            //Control.ScreenshotViaAdb(screenshotFilePath);
		    if(optionalFlag) {
		        //Generic.setScreenshothyperlink(screenshotFilePath);
		    	}
		    else {
		    	Constant.DefaultoptionalFlag = true;
		    	
		    }
        }
    }
    
    
    
    public static String RunProcessWithOutputInternet(String command,int timeout,Boolean returnOutput) 
    {
    	


    	String line="",line1="";
    	try
    	{
			ProcessBuilder builder = new ProcessBuilder(
			      "cmd.exe", "/c", command);
			  builder.redirectErrorStream(true);
			  Process p=builder.start();
			  p.waitFor(10,TimeUnit.SECONDS);
			 
			  if(p.isAlive())
			  {
				  System.out.println("Destryoing forcibly");
				  p.destroyForcibly();     
				  
			  }
			  
			  if(returnOutput)
			  {
				  
	                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
	                
	                while (r.ready() && ((line1=r.readLine())!=null)) 
	                {
	                	
	                	try
	                	{
	                		line+=line1+" ";
	                	}
	                    catch(Exception e)
	                    {
	                        System.out.println("Exception reading from prompt "+e.getMessage());
	                        e.printStackTrace();
	                        break;
	                    }
	                }
	                //System.out.println("Returning from shell : "+line);
	                return line;
			  
			  	}
    	}
    	catch(Exception e)
    	{
    		System.out.println("Exception in process of executing the following command : "+command+"\nException is : "+e.getMessage());
    		e.printStackTrace();
    	}
    	//System.out.println("Returning : null");
    	return null;

    	
    
    	
    
    }
    

    
    
    public static String RunProcessWithOutput(String command,int timeout,Boolean returnOutput) 
    {String line="",line1="";
	try
	{
		ProcessBuilder builder = new ProcessBuilder(
		      "cmd.exe", "/c", command);
		  builder.redirectErrorStream(true);
		  Process p=builder.start();
		  p.waitFor(timeout,TimeUnit.SECONDS);
		 
		  if(p.isAlive())
		  {
			  System.out.println("Destryoing forcibly");
			  p.destroyForcibly();     
			  
		  }
		  
		  if(returnOutput)
		  {
			  
                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                
                while (r.ready() && ((line1=r.readLine())!=null)) 
                {
                	
                	try
                	{
                		line+=line1+" ";
                	}
                    catch(Exception e)
                    {
                        System.out.println("Exception reading from prompt "+e.getMessage());
                        e.printStackTrace();
                        break;
                    }
                }
                //System.out.println("Returning from shell : "+line);
                
                if(line==null)
                {
                	line="";
                }
                
                try
                {
                	line=line.trim();
                }
                catch(Exception e)
                {
                	System.out.println("Exception RunProcessWithOutput() while trimming line. Line is <Start>"+line+"<end>");
                	e.printStackTrace();
                }
                return line;
		  
		  	}
	}
	catch(Exception e)
	{
		System.out.println("Exception in process of executing the following command : "+command+"\nException is : "+e.getMessage());
		e.printStackTrace();
	}
	//System.out.println("Returning : null");
	return null;
	
    }
    
    
    
    public static void initAppium() throws Exception{
    	
    	
    	LaunchMobileApp("",Generic.ReadFromExcel("DeviceDetails","AI_TestData",1),Generic.ReadFromExcel("DeviceDetails","AI_TestData",2) , "Android", Generic.ReadFromExcel("DeviceDetails","AI_TestData",3), Generic.ReadFromExcel("DeviceDetails","AI_TestData",4),Generic.ReadFromExcel("DeviceDetails","AI_TestData",5) , "4723","");
    	
    	String VersionCommand="adb shell getprop ro.build.version.release";
    	String Version=Control.RunProcessWithOutput(VersionCommand, 10, true);
    	String deviceNameCommand="adb shell getprop ro.build.product";
    	String deviceName=Control.RunProcessWithOutput(deviceNameCommand, 10, true);
    	String platformName="Android";
    	String HostID="4723";String ServerIP="127.0.0.1";
    	
    	System.setProperty("webdriver.http.factory", "apache");
    	
    	try
    	{
    		if(Constant.flag_init!=0)
    		{
    			Constant.driver.close();
    		}
    	}
    	catch(Exception e)
    	{
    		
    	}
    	
    	
    	DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, platformName);
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, Version);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);


        capabilities.setCapability("noReset", true);
//        Constant.driver = (io.testproject.sdk.drivers.ios.IOSDriver<? extends MobileElement>) new IOSDriver<>(new URL("http://"+ServerIP+":"+HostID+"/wd/hub"), capabilities);
        Constant.driver.manage().timeouts().implicitlyWait(Constant.defaultBrowserTimeOut, TimeUnit.SECONDS);    
        Constant.flag_init=1;

         
    }
    
    private static boolean waitUntilIsRunning(String SERVER_URL,int timeout) throws Exception {
        final URL status = new URL(SERVER_URL + "/sessions");
        try {
            new UrlChecker().waitUntilAvailable(timeout, TimeUnit.MILLISECONDS, status);
            return true;
        } catch (UrlChecker.TimeoutException e) {
            return false;
        }
    }
    
    
    public static String getAppiumHost()
    {
    	String SERVER_URL1 = "http://127.0.0.1:4723/wd/hub";
    	try 
    	{
			if(Control.waitUntilIsRunning(SERVER_URL1,15)==true)
				return "127.0.0.1";
		} 
    	catch (Exception e) 
    	{
			System.out.println("Exception while getting appium host : "+e.getMessage());
			e.printStackTrace();	
		}
    	
    	
		return null;
    	
       
    }

    public static void LaunchMobileApp(String BrowserName,String Version, String deviceName, String platformName, String appPackage, String appActivity,String ServerIP, String HostID, String URL ) throws Exception{
    	
    	System.setProperty("webdriver.http.factory", "apache");
    	
    	try
    	{
    		if(Constant.flag_init!=0)
    		{
    			Constant.driver.close();
    		}
    	}
    	catch(Exception e)
    	{
    		
    	}
    	
    	String versionCommand="adb shell getprop ro.build.version.release";
    	String deviceCommand="adb shell getprop ro.product.device";

    	versionCommand="adb shell getprop ro.build.version.release";
    	deviceCommand="adb shell getprop ro.product.device";
    	Version=(Control.RunProcessWithOutput(versionCommand, 15, true)).trim();
    	deviceName=(Control.RunProcessWithOutput(deviceCommand, 15, true)).trim();
    	platformName="android";
    	ServerIP=Generic.ReadFromExcel("DeviceDetails","AI_TestData",5);
    	HostID="4723";
    	
    	System.out.println("Getting device details :\nDevice Name : "+deviceName+"\nVersion : "+Version);

    	if(getAppiumHost()=="127.0.0.1")
    	{
    		ServerIP="127.0.0.1";
    	}
    		
    	Constant.ipToListen=ServerIP;

    	System.out.println("Appium is running on Host : "+ServerIP);
    	
    	DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, platformName);
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, Version);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        if(!BrowserName.equals("")) {
        	capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, BrowserName);
        }

        if(!appPackage.equals("")) {
        capabilities.setCapability("appPackage",appPackage );
        capabilities.setCapability("appActivity",appActivity);
        }   
        capabilities.setCapability("noReset", true);
//        Constant.driver = (io.testproject.sdk.drivers.ios.IOSDriver<? extends MobileElement>) new IOSDriver<>(new URL("https://"+ServerIP+":"+HostID+"/wd/hub"), capabilities);
        Constant.driver.manage().timeouts().implicitlyWait(Constant.defaultBrowserTimeOut, TimeUnit.SECONDS);    
        Constant.flag_init=1;
        if(!BrowserName.equals("")) {
        	Constant.driver.get(URL);
        }
         
    }
    

//this function is only for launching an app based on Activity and package name
public static void LaunchMobileAppEasy(String appPackage, String appActivity) throws Exception{
    	
    	System.setProperty("webdriver.http.factory", "apache");
    	
    	try
    	{
    		if(Constant.flag_init!=0)
    		{
    			Constant.driver.close();
    		}
    	}
    	catch(Exception e)
    	{
    		
    	}
    	
    	
    	String URL="";
    	String versionCommand="adb shell getprop ro.build.version.release";
    	String deviceCommand="adb shell getprop ro.product.device";
    	String Version=(Control.RunProcessWithOutput(versionCommand, 15, true)).trim();
    	String deviceName=(Control.RunProcessWithOutput(deviceCommand, 15, true)).trim();
    	String platformName="android";
    	

    	//String ServerIP=Generic.ReadFromExcel("DeviceDetails","AI_TestData",5);
    	String ServerIP="127.0.0.1";
    	if(getAppiumHost()=="127.0.0.1")
    		ServerIP="127.0.0.1";
    	
    	String HostID="4723";
    	String BrowserName="";
    	
    	System.out.println("Server IP is : "+ServerIP);
    	
    	DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, platformName);
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, Version);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        
        /*if(!BrowserName.equals("")) {
        	capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, BrowserName);
        }*/

        if(!appPackage.equals("")) {
        capabilities.setCapability("appPackage",appPackage );
        capabilities.setCapability("appActivity",appActivity);
        }   
       
        capabilities.setCapability("noReset", true);
        //capabilities.setCapability (MobileCapabilityType.CLEAR_SYSTEM_FILES, true);
        //capabilities.setCapability("newCommandTimeout", 120);
        //capabilities.setCapability("automationName", "uiautomator2");
        
        System.out.println("http://"+ServerIP+":"+HostID+"/wd/hub");
        //Thread.sleep(10000);
//        Constant.driver = (io.testproject.sdk.drivers.ios.IOSDriver<? extends MobileElement>) new IOSDriver<>(new URL("http://"+ServerIP+":"+HostID+"/wd/hub"), capabilities);
        
        //Constant.driver_a = new AppiumDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        
        
      //  Constant.driver = new AndroidDriver(new URL("https://127.0.0.1:8080/wd/hub"), capabilities);
        Constant.driver.manage().timeouts().implicitlyWait(Constant.defaultBrowserTimeOut, TimeUnit.SECONDS);    
        Constant.flag_init=1;
        if(!BrowserName.equals("")) {
        	Constant.driver.get(URL);
        }
         
    }
    
    
 public static void LaunchMobileAppWithReset(String BrowserName,String Version, String deviceName, String platformName, String appPackage, String appActivity,String ServerIP, String HostID, String URL ) throws Exception{
    	
    	System.setProperty("webdriver.http.factory", "apache");
    	
    	try
    	{
    		if(Constant.flag_init!=0)
    		{
    			Constant.driver.close();
    		}
    	}
    	catch(Exception e)
    	{
    		
    	}
    	
    	
    	DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, platformName);
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, Version);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        if(!BrowserName.equals("")) {
        	capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, BrowserName);
        }

        if(!appPackage.equals("")) {
        capabilities.setCapability("appPackage",appPackage );
        capabilities.setCapability("appActivity",appActivity);
        }   
        capabilities.setCapability("noReset", false);
        //Constant.driver = new IOSDriver<>(new URL("http://"+ServerIP+":"+HostID+"/wd/hub"), capabilities);
        Constant.driver.manage().timeouts().implicitlyWait(Constant.defaultBrowserTimeOut, TimeUnit.SECONDS);    
        Constant.flag_init=1;
        if(!BrowserName.equals("")) {
        	Constant.driver.get(URL);
        }
         
    }
    
    public static void getMessageContent(String PageName, String locator) throws InterruptedException {
    	 Constant.PageName=PageName;
         Constant.locator=locator;
         String Messagecontent = null;
         WebElement element = findElement(PageName,locator);
         try {
 			if (element != null) {
 				Messagecontent = element.getText();	
 				Generic.WriteTestData("Able to get the text '"+locator+"'",locator,"","Able to fetch  '"+locator+"'","text value is : "+Messagecontent,"Pass");
 			}
 			else {
 				
 				Generic.WriteTestData("Not Able to get the text '"+locator+"'",locator,"","Not Able to fetch  '"+locator+"'","text value is not found :","Fail");
 			}
 			element = null;		

 		} catch (Exception e) {
 			System.out.println(" Error occured whlie click on the element "
 					+ locator + " *** " + e.getMessage());
 			
 		}
    	    	
    }
	public static void DriverScriptForUSSD(int TotalTestCases) throws Exception {    	
			String InputValue=null;
			String CheckValue=null; 
			String InputValue2 = null;
			for(int RowNo=1; RowNo<=TotalTestCases;RowNo++) {
				Generic.WriteTestCase(Constant.Map2.get("TestCase"+ RowNo).get("TestCaseNo"), Constant.Map2.get("TestCase"+ RowNo).get("TestCaseName"), Constant.Map2.get("TestCase"+ RowNo).get("Expected_result"), Constant.Map2.get("TestCase"+ RowNo).get("Actual Result"));
				click("DialerApp", "btnNumbers");    		
				for(int Col_input= 1; Col_input<=20; Col_input++) {
					InputValue=Constant.Map2.get("TestCase"+ RowNo).get("Input_"+Col_input);
					
					if(InputValue.equals("")) {
						break;
						}
					WebElement element = findElement("DialerApp","tfldDialling");
					if (element != null) { 
							enterText("DialerApp","tfldDialling",InputValue);
							takeScreenshot();
							click("DialerApp","tfldDialling");
							click("DialerApp","btnDialler");
							takeScreenshot();
						}else{
						  	click("DialerApp","Input_field");
							enterText("DialerApp","Input_field",InputValue);
							takeScreenshot();
							click("DialerApp","SendButton");
						    takeScreenshot();
					    }	
					    
						CheckValue=Constant.Map2.get("TestCase"+ RowNo).get("CheckPoint_"+Col_input);
						System.out.println("\n************\n"+CheckValue+"\n************\n");
						compareText("DialerApp_Response","txtOutputMessage",CheckValue);
						takeScreenshot();
					}	  
						WebElement element = findElement("DialerApp","okButton");
					if (element==null) {
						element = findElement("DialerApp","okButton");	
					}
					if (element != null) {
						click("DialerApp","okButton");
					}
					else {
						click("DialerApp","CancelButton");
					     }
					//write 
					
					String Unsubscribe_KW = Constant.Map2.get("TestCase"+ RowNo).get("Unsubscribe_Via_SMS");
					String Notification = Constant.Map2.get("TestCase"+ RowNo).get("SMS_Notification");
					
					if(Unsubscribe_KW != "") {
						Control.LaunchMobileApp("", "8.1.0", "OnePlus5T", "Android", "com.android.mms", "com.android.mms.ui.ConversationList", "10.225.138.136", "4723","");
						Control.click("MessageNotification", "messgeMenu");
						Control.enterText("MessageNotification", "messageEditBox", Unsubscribe_KW);
						Control.click("MessageNotification", "messageSendButton");
						Thread.sleep(1000);
						Control.compareText("MessageNotification", "messagetext", Notification);
					}else {
						Control.LaunchMobileApp("", "8.1.0", "OnePlus5T", "Android", "com.android.mms", "com.android.mms.ui.ConversationList", "10.225.138.136", "4723","");
						Control.click("MessageNotification", "messgeMenu");
						Control.compareText("MessageNotification", "messagetext", Notification);
						//Control.getMessageContent("MessageNotification", "messagetext");
					}
						Generic.TestScriptEnds();
						Thread.sleep(10000);
			}
		}
    
	public static void DriverScriptForUSSD1(int TotalTestCases) throws Exception {    	
		String InputValue=null;
		String CheckValue=null; 
		String InputValue2 = null;
		for(int RowNo=1; RowNo<=TotalTestCases;RowNo++) {
			Generic.WriteTestCase(Constant.Map2.get("TestCase"+ RowNo).get("TestCaseNo"), Constant.Map2.get("TestCase"+ RowNo).get("TestCaseName"), Constant.Map2.get("TestCase"+ RowNo).get("Expected_result"), Constant.Map2.get("TestCase"+ RowNo).get("Actual Result"));
			String Unsubscribe_KW = Constant.Map2.get("TestCase"+ RowNo).get("Unsubscribe_Via_SMS");
			String Notification = Constant.Map2.get("TestCase"+ RowNo).get("SMS_Notification");
			if(Unsubscribe_KW != "") {
				LaunchMobileApp("",Generic.ReadFromExcel("DeviceDetails","AI_TestData",1),Generic.ReadFromExcel("DeviceDetails","AI_TestData",2) , "Android", Generic.ReadFromExcel("DeviceDetails","AI_TestData",3), Generic.ReadFromExcel("DeviceDetails","AI_TestData",4),Generic.ReadFromExcel("DeviceDetails","AI_TestData",5) , "4723","");
				Control.click("MessageNotification", "messageSenderIcon");
				Control.enterText("MessageNotification", "noEnterText", "8080");
				Control.enterText("MessageNotification", "messageEditBox", Unsubscribe_KW);
				Control.click("MessageNotification", "messageSendButton");
				takeScreenshot();
				longPress("MessageNotification", "messagetext");
				Control.click("MessageNotification", "deleteMessage");
				Control.click("MessageNotification", "deleteConfirmation");
				Thread.sleep(5000);
				Control.click("MessageNotification", "messgeMenu");
				Control.compareText("MessageNotification", "messagetext", Notification);
				takeScreenshot();
				longPress("MessageNotification", "messagetext");
				Control.click("MessageNotification", "deleteMessage");
				Control.click("MessageNotification", "deleteConfirmation");
			}else {
//				Date date= new Date();
//		    	long time1 = date.getTime();
//		    	Timestamp ts = new Timestamp(time1);
				LaunchMobileApp("", Generic.ReadFromExcel("DeviceDetails","AI_TestData",1),Generic.ReadFromExcel("DeviceDetails","AI_TestData",2), "Android", "com.simpler.dialer", "com.simpler.ui.activities.ContactsAppActivity", Generic.ReadFromExcel("DeviceDetails","AI_TestData",5), "4723","");
				click("DialerApp", "DialPad"); 
				for(int Col_input= 1; Col_input<=20; Col_input++) {
					InputValue=Constant.Map2.get("TestCase"+ RowNo).get("Input_"+Col_input);
					if(InputValue.equals("")) {
						break;
						}
					WebElement element = findElement("DialerApp","Dialer");
					if (element != null) { 
							enterText("DialerApp","Dialer",InputValue);
							takeScreenshot();
							click("DialerApp","Dialer");
							click("DialerApp","btnDialler");
							takeScreenshot();
						}else{
						  	click("DialerApp","InputOption");
							enterText("DialerApp","InputOption",InputValue);
							takeScreenshot();
							click("DialerApp","Send");
						    takeScreenshot();
					    }	
						CheckValue=Constant.Map2.get("TestCase"+ RowNo).get("CheckPoint_"+Col_input);
						//System.out.println("\n************\n"+CheckValue+"\n************\n");
						compareText("DialerApp_Response","txtOutputMessage",CheckValue);
						takeScreenshot();
					}	
					WebElement element = findElement("DialerApp","ok");
					if (element==null) {
						element = findElement("DialerApp","ok");	
					}
					if (element != null) {
						click("DialerApp","ok");
					}
					else {
						click("DialerApp","Cancel");
					     }	
				Control.LaunchMobileApp("",Generic.ReadFromExcel("DeviceDetails","AI_TestData",1),Generic.ReadFromExcel("DeviceDetails","AI_TestData",2), "Android", "com.android.mms", "com.android.mms.ui.ConversationList", Generic.ReadFromExcel("DeviceDetails","AI_TestData",5), "4723","");
				Control.click("MessageNotification", "messgeMenu");
				Control.compareText("MessageNotification", "messagetext", Notification);
				takeScreenshot();
				longPress("MessageNotification", "messagetext");
				Control.click("MessageNotification", "deleteMessage");
				Control.click("MessageNotification", "deleteConfirmation");
			}		
			Generic.TestScriptEnds();
			Thread.sleep(10000);
		}
	}
	
	public static void DriverScriptForUSSDSamsung(int TotalTestCases) throws Exception {    	
		String InputValue=null;
		String CheckValue=null; 
		String InputValue2 = null;
		for(int RowNo=4; RowNo<=TotalTestCases;RowNo++) {
			Generic.WriteTestCase(Constant.Map2.get("TestCase"+ RowNo).get("TestCaseNo"), Constant.Map2.get("TestCase"+ RowNo).get("TestCaseName"), Constant.Map2.get("TestCase"+ RowNo).get("Expected_result"), Constant.Map2.get("TestCase"+ RowNo).get("Actual Result"));
//			String Unsubscribe_KW = Constant.Map2.get("TestCase"+ RowNo).get("Unsubscribe_Via_SMS");
			String Notification = Constant.Map2.get("TestCase"+ RowNo).get("SMS_Notification");
//			if(Unsubscribe_KW != "") {
//				LaunchMobileApp("",Generic.ReadFromExcel("DeviceDetails","AI_TestData",1),Generic.ReadFromExcel("DeviceDetails","AI_TestData",2) , "Android", Generic.ReadFromExcel("DeviceDetails","AI_TestData",3), Generic.ReadFromExcel("DeviceDetails","AI_TestData",4),Generic.ReadFromExcel("DeviceDetails","AI_TestData",5) , "4723","");
////				Control.click("SamsungMessage", "Arrow");
////				//Constant.driver.findElement(By.id("com.samsung.android.messaging:id/from")).
////				if(Constant.driver.findElement(By.id("com.samsung.android.messaging:id/from")).isDisplayed()) {
////					longPress("SamsungMessage", "8080");
////					Control.click("SamsungMessage","DELETE");
////					Control.click("SamsungMessage", "deleteConfirmation");
////					Control.click("SamsungMessage", "ComposeIcon");
////					Control.click("SamsungMessage", "ComposeButton");
////				}else {
////					Control.click("SamsungMessage", "ComposeIcon");
////					Control.click("SamsungMessage", "ComposeButton");	
////				}
//				takeScreenshot();
//				Control.enterText("SamsungMessage", "Receipient", "8080");
//				takeScreenshot();
//				Control.enterText("SamsungMessage", "messageEditBox", Unsubscribe_KW);
//				Control.click("SamsungMessage", "messageSendButton");
//				Thread.sleep(4000);
//				takeScreenshot();
////				String abc1 =  Constant.driver.findElement(By.id("com.samsung.android.messaging:id/list_item_text_view")).getText();
////				System.out.println("identifier name " + abc1);
//				Control.compareText("SamsungMessage", "messagetext", Unsubscribe_KW);
//				longPress("SamsungMessage", "SentMessage");
//				Thread.sleep(1000);
//				Control.click("SamsungMessage", "Delete");
//				Thread.sleep(1000);
//				takeScreenshot();
//				Control.click("SamsungMessage", "deleteConfirmation");
//				Thread.sleep(4000);
//				if(Constant.driver.findElement(By.id("com.samsung.android.messaging:id/delete")).isDisplayed()) {
//					Control.click("SamsungMessage","DELETE");
//					Control.click("SamsungMessage", "deleteConfirmation");
//				}else {
//					System.out.println("inside else");
//					//Control.click("SamsungMessage","DELETE");
//					Control.click("SamsungMessage", "deleteConfirmation");
//				}
//				
//					Control.click("SamsungMessage", "messageSendButton");

//			}else {

				LaunchMobileApp("", Generic.ReadFromExcel("DeviceDetails","AI_TestData",1),Generic.ReadFromExcel("DeviceDetails","AI_TestData",2), "Android", "com.simpler.dialer", "com.simpler.ui.activities.ContactsAppActivity", Generic.ReadFromExcel("DeviceDetails","AI_TestData",5), "4723","");
				click("DialerApp", "DialPad");
				Thread.sleep(1000);
				for(int Col_input= 1; Col_input<=20; Col_input++) {
					InputValue=Constant.Map2.get("TestCase"+ RowNo).get("Input_"+Col_input);
					if(InputValue.equals(null)) {
						break;
						}
					WebElement element = findElement("DialerApp","Dialer");
					if (element != null) { 
							enterText("DialerApp","Dialer",InputValue);
							takeScreenshot();
							click("DialerApp","Dialer");
							click("DialerApp","btnDialler");
							takeScreenshot();
						}else{
						  	click("DialerApp","InputOption");
							enterText("DialerApp","InputOption",InputValue);
							takeScreenshot();
							click("DialerApp","Send");
						    takeScreenshot();
					    }	
						CheckValue=Constant.Map2.get("TestCase"+ RowNo).get("CheckPoint_"+Col_input);
						//System.out.println("\n************\n"+CheckValue+"\n************\n");
						//compareText("DialerApp_Response","txtOutputMessage",CheckValue);
						takeScreenshot();
					}	
					Thread.sleep(4000);
					WebElement element = findElement("DialerApp","ok");
					Thread.sleep(4000);
					if (element==null) {
						element = findElement("DialerApp","ok");	
					}
					if (element != null) {
						click("DialerApp","ok");
					}
					else {
						click("DialerApp","Cancel");
					     }	
				Control.LaunchMobileApp("",Generic.ReadFromExcel("DeviceDetails","AI_TestData",1),Generic.ReadFromExcel("DeviceDetails","AI_TestData",2), "Android",Generic.ReadFromExcel("DeviceDetails","AI_TestData",3), Generic.ReadFromExcel("DeviceDetails","AI_TestData",4),Generic.ReadFromExcel("DeviceDetails","AI_TestData",5) , "4723","");
				Control.click("SamsungMessage", "Arrow");
				Thread.sleep(4000);
				click("SamsungMessage", "8080");
				System.out.println(Notification);
				Control.compareText("SamsungMessage", "messagetext", Notification);
				takeScreenshot();
				longPress("SamsungMessage", "SentMessage");
				Control.click("SamsungMessage", "Delete");
				WebElement element1 = findElement("SamsungMessage","DELETE");
				if(element != null) {
					
					Control.click("SamsungMessage","DELETE");
				}
				Thread.sleep(1000);
				Control.click("SamsungMessage", "deleteConfirmation");
//			}		
			Generic.TestScriptEnds();
			
		}
	}
	
	
	public static void deleteAllMessageSamsungApp() throws Exception
	{
		Control.LaunchMobileApp("",Generic.ReadFromExcel("DeviceDetails","AI_TestData",1),Generic.ReadFromExcel("DeviceDetails","AI_TestData",2), "Android",Generic.ReadFromExcel("SamsungMessagePackageName","ObjectRepository",1), Generic.ReadFromExcel("SamsungMessageActivityName","ObjectRepository",1),Generic.ReadFromExcel("DeviceDetails","AI_TestData",5) , "4723","");
		
		int counter=0;
		Generic.FindAndClick("SamsungMessage","Arrow",15000);
		if(Generic.FindWithTimeOut("SamsungMessage", "FirstMsg", 10000))
			Control.longPress("SamsungMessage", "FirstMsg");
		else
			return;
		//Thread.sleep(1000);
		
		
		//if second msg is present then use this
		if(Generic.FindWithTimeOut("SamsungMessage", "SecondMsg", 2000))
			Generic.FindAndClick("SamsungMessage", "SelectAllMessagesCheckBox", 7000);
		else
			System.out.print("There is only one message thread, deleting...");
		
		try
		{
			Generic.FindAndClick("SamsungMessage", "AllMessageDELETE", 5000);
			Generic.FindAndClick("SamsungMessage", "AllMessageDELETEConfirmation", 5000);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		System.out.print("All messages deleted");
		
	}
	
	
	public static boolean sendKeyword(int RowNo) throws Exception
	{
		
		String keyword = Constant.Map2.get("TestCase"+ RowNo).get("sendSMS");
		System.out.println(keyword);
		
		System.out.println("Keyword is : "+keyword);
		
		
		if(keyword.equals("YES") || keyword.equals("OK")){
			System.out.println("Able to get inside if ");
			//System.exit(-1);
			
			Constant.lastRepliedRowNumber=RowNo;
			String messageToMatch = Constant.Map2.get("TestCase"+ RowNo).get("SMS_Notification_8080_1");
			//need to match this message
			String SmsToReplyOn=verifyMessageContents(messageToMatch, "dummy");
			
			if(SmsToReplyOn==null)
			{
				SmsToReplyOn="";
			}
			
			if(SmsToReplyOn.trim().equals(""))
			{
				//cant reply
			}
			else
			{
				//need to reply on the last message received
				//ReplyOnLastSMS(keyword);
				//--Constant.numberOfExpectedSms;
				String BNumber=SmsToReplyOn.split("_")[1];
				Generic.sendSMS(keyword, BNumber);
				return true;
			}
			

		}
		return false;
	}
	
	
	
	public static void DriverScriptForUSSDSamsung1(int FromTestCaseNo,int ToTestCaseNo) throws Exception {    	
		String InputValue=null;
		String CheckValue=null; 
		String InputValue2 = null;
		String keywor= null;
		String bal1=null,bal2=null;
		Float balance1=0.0f,balance2=0.0f;
		
		System.out.println("Brand type is : "+Constant.brandType);
		//System.exit(-1);
		
		for(int RowNo=FromTestCaseNo; RowNo<=ToTestCaseNo;RowNo++) {
			
			try
			{
				System.out.println("\n\n*******************************************************");
				System.out.println("*******************[ Test Case : "+RowNo+"]******************");
				System.out.println("*******************************************************\n");
				Generic.WriteTestCase(Constant.Map2.get("TestCase"+ RowNo).get("TestCaseNo"), Constant.Map2.get("TestCase"+ RowNo).get("TestCaseName"), Constant.Map2.get("TestCase"+ RowNo).get("Expected_result"), Constant.Map2.get("TestCase"+ RowNo).get("Actual Result"));

				Constant.lastSmsOrUssdTime=System.currentTimeMillis();
				Constant.globalRowNumber=RowNo;
				moveAllSMSFiles();
				
				closeFlashMsg();
				
				if(!(Constant.brandType.toUpperCase().equals("POSTPAID") || Constant.brandType.toUpperCase().equals("AMAX")))
				{
					try
					{
						bal1=checkbal("*143*2*2*1#");
						if(bal1!=null)
						{
							balance1=Float.parseFloat(bal1);
						}
						else
						{
							Generic.WriteTestData("Failed while parsing User Account Balance",bal1,bal1,"Should be able to parse User Balance","Not able to parse User Balance","Fail");
							
						}
					}
					catch(Exception e)
					{
						Generic.WriteTestData("Failed while parsing User Account Balance",bal1,bal1,"Should be able to parse User Balance","Not able to parse User Balance","Fail");
						
					}
					
					closeFlashMsg();
				}
				
				
				
				keywor = Constant.Map2.get("TestCase"+ RowNo).get("sendSMS");
				
				if(keywor.equals("GOUNLI STOP") || keywor.equals("TEXT STOP") || keywor.equals("UNLITEXT STOP") || keywor.equals("SURF STOP") || keywor.equals("CALL STOP")){
					
					Generic.sendSMS(keywor, "8080");
					
					ArrayList<String> allExpectedMessages=numberOfExptectedMsgs(RowNo);
					Constant.numberOfExpectedSms=allExpectedMessages.size();
					boolean resultSendKeyword=sendKeyword(RowNo);
					
					/*if(resultSendKeyword)
					{
						//remove the first element from the array list
						if(allExpectedMessages.size()>0)
							allExpectedMessages.remove(0);
					}*/
					
					
					
					
					for(String s:allExpectedMessages)
					{
						ReadFirstMessageForSpecificSenders(s, "dummy");
					}
					
					//read8080Message(RowNo);
					
				}else {
					
					LaunchMobileApp("", Generic.ReadFromExcel("DeviceDetails","AI_TestData",1),Generic.ReadFromExcel("DeviceDetails","AI_TestData",2), "Android", "com.simpler.dialer", "com.simpler.ui.activities.ContactsAppActivity", Generic.ReadFromExcel("DeviceDetails","AI_TestData",5), "4723","");
					
					System.out.println("testcase No:" +  Constant.Map2.get("TestCase"+ RowNo).get("TestCaseNo"));
					
					click("DialerApp", "DialPad");
					for(int Col_input= 1; Col_input<=20; Col_input++) {
						InputValue=Constant.Map2.get("TestCase"+ RowNo).get("Input_"+Col_input);
						System.out.println("TestCase # : "+RowNo);
						System.out.println("TestStep # : "+Col_input);
						if(InputValue == null || InputValue.equals("")) {
							break;
							}
						WebElement element = findElement("DialerApp","Dialer");
						if (element != null) { 
								enterText("DialerApp","Dialer",InputValue);
								takeScreenshot();
								//click("DialerApp","Dialer");
								click("DialerApp","btnDialler");
								
							}else{
							  //click("DialerApp","InputOption");
								enterText("DialerApp","InputOption",InputValue);
								takeScreenshot();
								click("DialerApp","Send");
								Constant.lastSmsOrUssdTime=System.currentTimeMillis();
							   
						    }	
							CheckValue=Constant.Map2.get("TestCase"+ RowNo).get("CheckPoint_"+Col_input);
							
							WebElement element2 = Generic.GetElementWithTimeOut("SamsungMessage","ok",10000);
							
							//id for ok button and Send button are same, hence checking text additionally
							
							if(element2 != null && !(element2.getText().toLowerCase().equals("ok"))) {
								if(Generic.FindWithTimeOut("SamsungMessage", "txtOutputMessage", 5000)==true)
									compareText("SamsungMessage","txtOutputMessage",CheckValue);
								else
								{
									System.out.println("Not able to find element txtOutputMessage");
									compareText("SamsungMessage","txtOutputMessage",CheckValue);
								}
								
								Control.takeScreenshot();
								
							}else {
								if(Generic.FindWithTimeOut("SamsungMessage", "BeforeOk", 5000)==true)
									compareText("SamsungMessage","BeforeOk",CheckValue);
								else
								{
									//System.out.println("Not able to find element BeforeOk");
									compareText("SamsungMessage","BeforeOk",CheckValue);
								}
								Control.takeScreenshot();
									
							}
						
						}	
						Thread.sleep(500);
						WebElement element = findElement("SamsungMessage","ok");
						Thread.sleep(500);
						if (element==null) {
							//takeScreenshot();
							element = findElement("SamsungMessage","ok");	
						}
						if (element != null && element.getText().toLowerCase().equals("ok")) {
							Generic.WriteTestData("Will click OK button", "OK", "OK", "Will click OK", "Will click OK", "Pass");
							
							takeScreenshot();
							click("SamsungMessage","ok");
							
						}
						else {
							Generic.WriteTestData("Will click CANCEL button", "CANCEL", "CANCEL", "Will click CANCEL", "Will click CANCEL", "Pass");
							takeScreenshot();
							click("SamsungMessage","Cancel");
						 }
						
						ArrayList<String> allExpectedMessages=numberOfExptectedMsgs(RowNo);
						Constant.numberOfExpectedSms=allExpectedMessages.size();
						sendKeyword(RowNo);
						
						for(String s:allExpectedMessages)
						{
							ReadFirstMessageForSpecificSenders(s, "dummy");
						}
						
						
						//read8080Message(RowNo);
					
				}
				
				if(!Constant.brandType.toUpperCase().equals("POSTPAID"))
				{
					try
					{
						bal2=checkbal("*143*2*2*1#");
						if(bal2!=null)
						{
							balance1=Float.parseFloat(bal2);
						}
						else
						{
							Generic.WriteTestData("Failed while parsing User Account Balance",bal2,bal2,"Should be able to parse User Balance","Not able to parse User Balance","Fail");
							
						}
					}
					catch(Exception e)
					{
						Generic.WriteTestData("Failed while parsing User Account Balance",bal2,bal2,"Should be able to parse User Balance","Not able to parse User Balance","Fail");
						
					}
					
					finally
					{
						if(bal1!=null && bal2!=null)
						{
							balance2=Float.parseFloat(checkbal("*143*2*2*1#"));
							 int retval = Float.compare(balance1, balance2);
							
							if(retval==0)
							{
								//write to excel
								Generic.WriteTestData("No Balance deduction", "Balance 1: "+balance1, "Balance2:"+balance2, "Starting balance : "+balance1+" is same as ending balance: "+balance2, "Starting balance : "+balance1+" is same as ending balance: "+balance2, "Pass");
								
							}
							else if(retval>0)
							{
								Generic.WriteTestData("Balance deduction = "+(balance1-balance2), "Balance 1: "+balance1, "Balance2:"+balance2, "Starting balance : "+balance1+" is NOT same as ending balance: "+balance2, "Starting balance : "+balance1+" is NOT same as ending balance: "+balance2, "Pass");
								
							}
							
							else
							{
								//balance 1 is greater than balance2
								//Generic.WriteTestData("Balance increment = "+(balance1-balance2), "Balance", ""+balance2, "Available balance is "+balance2, "Available balance is "+balance2, "Pass");
								Generic.WriteTestData("Balance increment = "+(balance1-balance2), "Balance 1: "+balance1, "Balance2"+balance2, "Starting balance : "+balance1+" is NOT same as ending balance: "+balance2, "Starting balance : "+balance1+" is NOT same as ending balance: "+balance2, "Pass");
								
							}
						}
						else
						{
							//either of the above was null
							if(bal1==null)
								Generic.WriteTestData("Not able to calculate Balance deduction as initial balance was not fetched correctly", "Balance Compare", "Balance Compare", "Not able to calculate Balance deduction as initial balance was not fetched correctly", "Not able to calculate Balance deduction as initial balance was not fetched correctly", "Pass");
							
							else
								Generic.WriteTestData("Not able to calculate Balance deduction as final balance was not fetched correctly", "Balance Compare", "Balance Compare", "Not able to calculate Balance deduction as final balance was not fetched correctly", "Not able to calculate Balance deduction as final balance was not fetched correctly", "Pass");
							
						}
					}
					
				}
				
					
					
					
					
					
			}
			catch(Exception e)
			{
				System.out.println("Exception in Test Case #: "+RowNo);
				e.printStackTrace();
			}
			
			finally
			{
				System.out.println("\n\n\nCalling Test Script Ends");
				try
				{
					Generic.TestScriptEnds();
				}
				catch(Exception e)
				{
					System.out.print("\n\n\nException in Generic.TestScriptEnds();");
					e.printStackTrace();
				}
				
				
				
				
			}
			
			
			
		}
	}
	
	
	
	public static boolean openMessageAndTakeScreenshot(String content)
	{
		System.out.println("In function : Open msg and take screenshot");
		try
		{
			//write data to file
			String filePath="/storage/emulated/0/SMSViewer/";
			String fileName="file.txt";
			
		    FileWriter fileWriter = new FileWriter(""+fileName);
		    fileWriter.write(content);
		    fileWriter.close();
			
			String command="adb push "+fileName+" "+filePath;
			
			
			System.out.println(command);
			
			
			Control.RunProcessWithOutput(command, 15, false);
			
			Thread.sleep(1000);
			
			Control.LaunchMobileApp("",Generic.ReadFromExcel("DeviceDetails","AI_TestData",1),Generic.ReadFromExcel("DeviceDetails","AI_TestData",2), "Android",Constant.textViewerPackageName, Constant.textViewerActivityName,Generic.ReadFromExcel("DeviceDetails","AI_TestData",5) , "4723","");
	    	
			System.out.println("");
			
			if(Generic.FindWithTimeOut("TextViewer", "textView", 25000))
			{
				Control.takeScreenshot();
				System.out.println("Message Screenshot captured");
				//Generic.WriteTestData("Unable to capture Message Screenshot as app was not launched","","","Screenshot was captured","Screenshot was captured","Pass");
				
			}
			else
			{
				System.out.println("Unable to capture Message Screenshot as app was not launched");
				Generic.WriteTestData("Unable to capture Message Screenshot as app was not launched","","","TextViewer App should launch","Issue with TextViewerApp","Fail");
				
			}
		}
		
		catch(Exception e)
		{
    		System.out.println("Exception openMessageAndTakeScreenshot()"+e.getMessage());
    		e.printStackTrace();
		}
		
		return false;
	}
	
	
	public static void DriverScriptForUSSDSamsung_postpaid(int TotalTestCases) throws Exception {    	
		String InputValue=null;
		String CheckValue=null; 
		String InputValue2 = null;
		for(int RowNo=1; RowNo<=TotalTestCases;RowNo++) {
			Generic.WriteTestCase(Constant.Map2.get("TestCase"+ RowNo).get("TestCaseNo"), Constant.Map2.get("TestCase"+ RowNo).get("TestCaseName"), Constant.Map2.get("TestCase"+ RowNo).get("Expected_result"), Constant.Map2.get("TestCase"+ RowNo).get("Actual Result"));
//			String Unsubscribe_KW = Constant.Map2.get("TestCase"+ RowNo).get("Unsubscribe_Via_SMS");
			//String Notification = Constant.Map2.get("TestCase"+ RowNo).get("SMS_Notification");
			LaunchMobileApp("", Generic.ReadFromExcel("DeviceDetails","AI_TestData",1),Generic.ReadFromExcel("DeviceDetails","AI_TestData",2), "Android", "com.simpler.dialer", "com.simpler.ui.activities.ContactsAppActivity", Generic.ReadFromExcel("DeviceDetails","AI_TestData",5), "4723","");
			System.out.println("testcase No:" + Constant.Map2.get("TestCase"+ RowNo).get("TestCaseNo"));
			click("DialerApp", "DialPad");
			//Thread.sleep(1000);
			for(int Col_input= 1; Col_input<=20; Col_input++) {
				InputValue=Constant.Map2.get("TestCase"+ RowNo).get("Input_"+Col_input);
				
				if(InputValue == null || InputValue.equals("")) {
					break;
					}
				WebElement element = findElement("DialerApp","Dialer");
				if (element != null) { 
						enterText("DialerApp","Dialer",InputValue);
						takeScreenshot();
						click("DialerApp","Dialer");
						click("DialerApp","btnDialler");
						takeScreenshot();
					}else{
					  	click("DialerApp","InputOption");
						enterText("DialerApp","InputOption",InputValue);
						takeScreenshot();
						click("DialerApp","Send");
					    takeScreenshot();
				    }	
					CheckValue=Constant.Map2.get("TestCase"+ RowNo).get("CheckPoint_"+Col_input);
					//System.out.println("\n************\n"+CheckValue+"\n************\n");
					WebElement element2 = findElement("SamsungMessage","ok");
					if(element2 == null) {
						compareText("SamsungMessage","txtOutputMessage",CheckValue);
					}else {
						compareText("SamsungMessage","BeforeOk",CheckValue);
					}
					takeScreenshot();
				}	
				Thread.sleep(2000);
				WebElement element = findElement("SamsungMessage","ok");
				Thread.sleep(4000);
				if (element==null) {
					element = findElement("SamsungMessage","ok");	
				}
				if (element != null) {
					click("SamsungMessage","ok");
				}
				else {
					click("SamsungMessage","Cancel");
				     }	
				Control.LaunchMobileApp("",Generic.ReadFromExcel("DeviceDetails","AI_TestData",1),Generic.ReadFromExcel("DeviceDetails","AI_TestData",2), "Android",Generic.ReadFromExcel("DeviceDetails","AI_TestData",3), Generic.ReadFromExcel("DeviceDetails","AI_TestData",4),Generic.ReadFromExcel("DeviceDetails","AI_TestData",5) , "4723","");
				Thread.sleep(6000);
				Control.click("SamsungMessage", "Arrow");
				Thread.sleep(6000);
				String Message8080 = Constant.Map2.get("TestCase"+ RowNo).get("SMS_Notification_8080");
				String MessageGlobe = Constant.Map2.get("TestCase"+ RowNo).get("SMS_Notification_Globe");
				Thread.sleep(8000);
				if(Message8080 != "") {
					WebElement element1 = findElement("SamsungMessage","8080");
					element1.click();
					//Control.click("SamsungMessage", "8080MessageMenu");
					Thread.sleep(2000);
					Control.compareText("SamsungMessage", "messagetext", Message8080);
					Control.takeScreenshot();
					WebElement element2 = findElement("SamsungMessage","Arrow");
					element2.click();
//					Control.click("SamsungMessage", "Arrow");
					Control.longPress("SamsungMessage", "8080MessageMenu");
					Control.click("SamsungMessage","DELETE");
					Control.click("SamsungMessage", "deleteConfirmation");	
				}
				Thread.sleep(6000);
				if(MessageGlobe !="") {
					Control.click("SamsungMessage", "GlobeMessageMenu");
					Thread.sleep(2000);
					Control.compareText("SamsungMessage", "messagetext", MessageGlobe);
					Control.takeScreenshot();
					Control.click("SamsungMessage", "Arrow");
					Control.longPress("SamsungMessage", "GlobeMessage");
					Control.click("SamsungMessage","DELETE");
					Control.click("SamsungMessage", "deleteConfirmation");	
				}
				
				Generic.TestScriptEnds();
		}
	}
	
	
	public static void scroll(String PageName, String locator) throws InterruptedException {
		 Constant.PageName=PageName;
         Constant.locator=locator;
         WebElement element = findElement(PageName,locator);
         try {
 			if (element != null) {
 				JavascriptExecutor js = (JavascriptExecutor) Constant.driver; 
 				js.executeScript("arguments[0].scrollTop = arguments[1];",element, 250); 
 				Thread.sleep(1000); 
 				Generic.WriteTestData("Able to Scroll '"+locator+"'",locator,"","Able to Scroll  '"+locator+"'","Successfully scroll operation is performed","Pass");
 			}
 			else {
 				
 				Generic.WriteTestData("Not Able to Scroll '"+locator+"'",locator,"","Not Able to Scroll '"+locator+"'","Scroll operation is not performed","Fail");
 			}
 			element = null;		

 		} catch (Exception e) {
 			e.printStackTrace();
 		}
	}

	
	public static void longPress(String PageName, String locator) throws InterruptedException {
		Constant.PageName=PageName;
        Constant.locator=locator;
        WebElement element = findElement(PageName,locator);
        //uncomment for mobile device testing
        
        //changed by Altamash - casting PerformsTouchActions - during integration of Web/Mobile Automation
        TouchAction Ac = new TouchAction((PerformsTouchActions) Constant.driver);
        try {
			if (element != null) {
				//Ac.longPress(element).release().perform();
				
			}
			element = null;		
		} catch (Exception e) {
			System.out.println(" Error occured whlie click on the element "+ locator + " *** " + e.getMessage());
		}
		
	}
	
	
	private static String getcelldata1(int rownum, int colnum) throws Exception{
		String celldata=null;
		try{
			cell= Worksheet.getRow(rownum).getCell(colnum);
		
		 celldata=cell.getStringCellValue();
		
		}catch (Exception e) {			
		}
		return celldata;
	}
	
	public static void ussd(String sheetName) throws Exception {
		String  strValue,ColumnName,ColumnValue, testcasename;
		int totalrows,totalColumns;
		try {
			FileInputStream ExcelFile= new FileInputStream(Constant.TestDataFilePath);
			workbook = new XSSFWorkbook(ExcelFile);
			Worksheet =  workbook.getSheet(sheetName);
			totalrows = Worksheet.getLastRowNum();
			totalColumns= Worksheet.getRow(0).getLastCellNum();
			for(int i=0; i<=totalrows;i++) {
				testcasename = getcelldata1(i, 1);
				System.out.println(testcasename + "\n");
				Constant.Map2.put("TestCase"+ i , new HashMap<String,String>());
				for(int j=0; j <= totalColumns; j++){
						ColumnName= getcelldata1(0,j);		
						ColumnValue = getcelldata1(i,j);
						System.out.println(ColumnName + ":" + ColumnValue);
				}								
			}				
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		

	public static String checkbal1(String SeqNo) throws Exception {
		Control.LaunchMobileApp("", Generic.ReadFromExcel("DeviceDetails","AI_TestData",1),Generic.ReadFromExcel("DeviceDetails","AI_TestData",2), "Android", "com.simpler.dialer", "com.simpler.ui.activities.ContactsAppActivity", Generic.ReadFromExcel("DeviceDetails","AI_TestData",5), "4723","");
   	 	Control.click("DialerApp", "DialPad");
   	 	Control.enterText("DialerApp","Dialer",SeqNo);
		Control.click("DialerApp","btnDialler");
		Control.takeScreenshot();
		Thread.sleep(2000);
		WebElement element1 = Control.findElement("SamsungMessage","BeforeOk");
		String text1 = element1.getText();
		String[] bal= text1.split(" ");
		String bala = bal[7].replaceAll("P", "");
		Generic.WriteTestData("Available balance", "", bala, "Available balance is "+bala, "Available balance is "+bala, "Pass");
		Thread.sleep(4000);
		Control.click("SamsungMessage","ok");
		return bala;
	}
	
	
	public static String checkbal(String dummy) throws Exception {
		/*Control.LaunchMobileApp("", Generic.ReadFromExcel("DeviceDetails","AI_TestData",1),Generic.ReadFromExcel("DeviceDetails","AI_TestData",2), "Android", "com.simpler.dialer", "com.simpler.ui.activities.ContactsAppActivity", Generic.ReadFromExcel("DeviceDetails","AI_TestData",5), "4723","");
   	 	Control.click("DialerApp", "DialPad");
   	 	Control.enterText("DialerApp","Dialer","*143*2*2*1#");
		Control.click("DialerApp","btnDialler");
		Control.takeScreenshot();
		WebElement element1=null;*/
		//Thread.sleep(2000);
		
		String defaultReturnValue="-999";
		String InputValue="*143*2*2*1#";
		
		if(Constant.brandType.toUpperCase().equals("TM"))
			 InputValue="*143*12*2*1#";
		
		
		//adb shell am start -a android.intent.action.DIAL -d tel:1234
			
		String USSDCode=InputValue.replace("#", "%23");
		
		String dialUssdCommand="adb shell am start -a android.intent.action.DIAL -d tel:\""+USSDCode+"\"";
		Control.RunProcessWithOutput(dialUssdCommand, 10, false);
		
		Thread.sleep(2000);
		
		Generic.WriteTestData("Entering B Number", "", "", "Should be able to enter", "Should be able to enter", "Pass");
		Control.takeScreenshot();

		String makeVoiceCall="adb shell am start -a android.intent.action.CALL -d tel:\""+USSDCode+"\"";
  		 
      ProcessBuilder builderCall = new ProcessBuilder(
      "cmd.exe", "/c", makeVoiceCall);
      builderCall.redirectErrorStream(true);
      builderCall.start().waitFor(10,TimeUnit.SECONDS);
		
		WebElement element1=null;
		
		
		if(Generic.FindWithTimeOut("SamsungMessage","BeforeOk",15000))
				element1 = Control.findElement("SamsungMessage","BeforeOk");
		
		
		
		if(element1==null)
		{
			System.out.println("Element 1 is null");
			Generic.WriteTestData("Failed while parsing User Account Balance","SamsungMessage"+"BeforeOk","SamsungMessage"+"BeforeOk","Should be able to parse User Balance","Not able to parse User Balance","Fail");
			return defaultReturnValue;
		}
		
		
		while(element1.getText().contains("USSD code running"))
		{
			continue;
		}
		
		String text1 = element1.getText();
		
		
		String bala="";
		String freeTexts="";
		try
		{
			 bala= (text1.split("is P")[1]).split(" valid")[0];
			 
		}
		catch(Exception e)
		{
			System.out.println("Exception while Parsing User Balance : "+e.getMessage());
			Generic.WriteTestData("Failed while parsing User Account Balance",text1,text1,"Should be able to parse User Balance","Not able to parse User Balance","Fail");
			return defaultReturnValue;
		}
		
		
		try
		{
			try
			{
				freeTexts=(text1.split("with ")[1]).split(" Free")[0];
			}
			catch(Exception e)
			{
				Constant.numberOfFreetexts=-999;
				System.out.println("Exception while parsing Free Texts : "+freeTexts);
				e.printStackTrace();
				return defaultReturnValue;
			}
			
			
			Constant.numberOfFreetexts=Float.parseFloat(freeTexts);
		}
		catch(Exception e)
		{
			Constant.numberOfFreetexts=-999;
			System.out.println("Exception while converting : "+freeTexts+" to long");
			e.printStackTrace();
			return defaultReturnValue;
		}

		Generic.WriteTestData("Available balance", "", bala, "Available balance is "+bala, "Available balance is "+bala, "Pass");
		Control.takeScreenshot();
		Generic.FindAndClick("SamsungMessage", "ok", 10000);
		return bala;
	}
	
	public static String checkBalheader() throws Exception {
		Control.LaunchMobileApp("", Generic.ReadFromExcel("DeviceDetails","AI_TestData",1),Generic.ReadFromExcel("DeviceDetails","AI_TestData",2), "Android", "com.simpler.dialer", "com.simpler.ui.activities.ContactsAppActivity", Generic.ReadFromExcel("DeviceDetails","AI_TestData",5), "4723","");
   	 	
		Control.click("DialerApp", "DialPad");
   	 	Control.enterText("DialerApp","Dialer","*144#");
   	 	Control.click("DialerApp","btnDialler");
		Control.takeScreenshot();
		Thread.sleep(2000);
		String Avl =Control.ReadBal("SamsungMessage", "txtOutputMessage");
		Generic.WriteTestData("Available balance", "", Avl, "Available balance is "+Avl, "Available balance is "+Avl, "Pass");
		Control.click("SamsungMessage","Cancel");
		return Avl;
	}
	
	
	public static void replyOnFirstMessage(String keyword) throws Exception
	{
		Control.LaunchMobileApp("",Generic.ReadFromExcel("DeviceDetails","AI_TestData",1),Generic.ReadFromExcel("DeviceDetails","AI_TestData",2), "Android",Generic.ReadFromExcel("DeviceDetails","AI_TestData",3), Generic.ReadFromExcel("DeviceDetails","AI_TestData",4),Generic.ReadFromExcel("DeviceDetails","AI_TestData",5) , "4723","");
    	
		Generic.FindAndClick("SamsungMessage", "Arrow", 30000);
		
		Thread.sleep(1000);   
		Control.takeScreenshot();
		Generic.FindAndClick("SamsungMessage", "8080",30000);
		
		
		System.out.println("Replying with Keyword :"+keyword+" on the first message");
	
			
		Control.enterText("SamsungMessage", "messageEditBox", keyword);
		Control.takeScreenshot();
		Control.click("SamsungMessage", "messageSendButton");
		

    }
	
	
	public static ArrayList<String>  numberOfExptectedMsgs(int RowNo)
	{
		ArrayList<String> list=new ArrayList<String>();
		for(int Col_input= 1; Col_input<=10; Col_input++) {
			String messageString = Constant.Map2.get("TestCase"+ RowNo).get("SMS_Notification_8080_"+Col_input);
			if(messageString == null || messageString.equals("")) {
				
			}else {
				list.add(Constant.Map2.get("TestCase"+ RowNo).get("SMS_Notification_8080_"+Col_input));
			}
		}
		
		return list;
	}
	
	public static void read8080Message(int RowNo) throws Exception {
		 String text = null;
		 String mytext = null;
		 String keyword = null;
		 boolean flag1 = false;
		ArrayList<String> list=new ArrayList<String>();
		for(int Col_input= 1; Col_input<=10; Col_input++) {
			String messageString = Constant.Map2.get("TestCase"+ RowNo).get("SMS_Notification_8080_"+Col_input);
			if(messageString == null || messageString.equals("")) {
				
			}else {
				list.add(Constant.Map2.get("TestCase"+ RowNo).get("SMS_Notification_8080_"+Col_input));
			}
		}		
		for(String x:list) {
			Control.LaunchMobileApp("",Generic.ReadFromExcel("DeviceDetails","AI_TestData",1),Generic.ReadFromExcel("DeviceDetails","AI_TestData",2), "Android",Generic.ReadFromExcel("DeviceDetails","AI_TestData",3), Generic.ReadFromExcel("DeviceDetails","AI_TestData",4),Generic.ReadFromExcel("DeviceDetails","AI_TestData",5) , "4723","");
	    	
			Control.click("SamsungMessage", "Arrow");
			Thread.sleep(1000);   
			Control.takeScreenshot();
			Control.click("SamsungMessage", "8080");
			
			keyword = Constant.Map2.get("TestCase"+ RowNo).get("sendSMS");
			System.out.println(keyword);
			if(keyword.equals("YES") && flag1!=true){
				flag1 = true;
				Control.enterText("SamsungMessage", "messageEditBox", keyword);
				Control.takeScreenshot();
				Control.click("SamsungMessage", "messageSendButton");
			}
//			}else if (keyword != null && flag1!=true) {
//				flag1 = true;
//				
//				Control.enterText("SamsungMessage", "Receipient", "8080");
//				Control.enterText("SamsungMessage", "messageEditBox", keyword);
//				Control.click("SamsungMessage", "messageSendButton");
//				Thread.sleep(4000);
//				
//			} 
			
			WebElement element = Control.findElement("SamsungMessage", "messagetext");   
			 if(element==null){
	       	 element = Control.findElement("SamsungMessage", "messagetext"); 
	        }
			 try {
					if (element != null)  
					text = element.getText();
					int size = list.size();
					int flag = 0;
					for(int i=0; i<size;i++)
					{
						 mytext = list.get(i);
						if(mytext.contains(text) || text.contains(mytext) )
						{
							flag = 1;
							break;
						}	
					}
						
		            if(flag == 1) {
		                Generic.WriteTestData("Comparing String","","","The ExpectedText '"+mytext+"' should be equal to Actual text from Application","The ExpectedText '"+mytext+"' is  same as the Acutal Text '"+text+"'","Pass");
		            
		            }else{
		            	Generic.WriteTestData("Comparing String","","","The ExpectedText '"+mytext+"' should be equal to Actual text from Application","The ExpectedText '"+mytext+"' is not same as the Acutal Text '"+text+"'","Fail");
		            }
		            
		        }catch (Exception e) {
		        	Generic.WriteTestData("Comparing text of field","","","The ExpectedText '"+mytext+"' should be equal to Actual text from Application","Unable to find the element","Fail");
		            e.printStackTrace();
		        }
		        Control.takeScreenshot();
		        Control.longPress("SamsungMessage", "SentMessage");
				Control.click("SamsungMessage", "Delete");
				Thread.sleep(500);
				WebElement element1 = Control.findElement("SamsungMessage","DELETE");
				if(element1 == null) {
					element1 = Control.findElement("SamsungMessage","DELETE");
				}else {
					Control.click("SamsungMessage","DELETE");
				}
				Control.click("SamsungMessage", "deleteConfirmation");	
				
		}
		list.clear();
		list.clear();
		
	}
	
	
	public static void moveOldSMSFiles()
	{
		String commandForLastSMSFile="adb shell ls -rt /storage/emulated/0/sms";
		String SMSFileName="";
		
		System.out.println("Moving old messages..");
		
		String SMSFileNames=Control.RunProcessWithOutput(commandForLastSMSFile, 15, true);
		System.out.println("Listing All SMS Files Present: \n"+SMSFileNames);

		
		if(SMSFileNames==null)
			SMSFileNames="";
		
		try
		{
			SMSFileNames=SMSFileNames.trim();
		}
		catch(Exception e)
		{
			System.out.println("Exception Moving Old Msgs while trimming SMS File Names: <start>"+SMSFileNames+"<end>");
			e.printStackTrace();
		}
		
		
		SMSFileNames=Control.RunProcessWithOutput(commandForLastSMSFile, 15, true);
		if(SMSFileNames.equals(""))
		{
			//no sms
			System.out.println("No Old files to move..");
			return;
			
		}
		
		//lets parse the SMSs first, and store in SMSFileNames
		else if(SMSFileNames.contains(" "))
		{
			//multiple sms, lets filter
			//check the timestamp of each message, and add to string based on condition
			
			String filesToConsider="";
			try
			{
				String[] filterSMS=SMSFileNames.split(" ");
				for(String tempString:filterSMS)
				{
					if(tempString.trim().equals(""))
						break;
					else
					{
						
						//check timestamp
						double fileTimestamp=Double.parseDouble((tempString.split("_")[2]));
						if(fileTimestamp<Constant.lastSmsOrUssdTime)
						{
							//only if this condition is met, move this file
							//filename=tempString
							String moveCommand="adb shell mv /storage/emulated/0/sms/"+tempString+" /storage/emulated/0/smsbackup*/";
							Control.RunProcessWithOutput(moveCommand, 15, false);
							return;
							
						}
					}
					
				}
				
				
			}
			catch(Exception e)
			{
				System.out.println("Exception while moving files with old timestamp, while parsing multiple sms file names : "+e.getMessage());
				e.printStackTrace();
			}
			
		}
		else
			{
					//for single SMS
					//check timestamp
					double fileTimestamp=Double.parseDouble((SMSFileNames.split("_")[2]));
					if(fileTimestamp<Constant.lastSmsOrUssdTime)
					{
						//only if this condition is met, move this file
						//filename=tempString
						String moveCommand="adb shell mv /storage/emulated/0/sms/"+SMSFileNames+" /storage/emulated/0/smsbackup*/";
						Control.RunProcessWithOutput(moveCommand, 15, false);
						return;
						
					}
				}
			
		}
	
	
	public static void moveAllSMSFiles()
	{
		String moveCommand="adb shell mv /storage/emulated/0/sms/* /storage/emulated/0/smsbackup*/";
		
		Control.RunProcessWithOutput(moveCommand, 20, false);
		
	}
	
	
	public static void ReplyOnLastSMS(String keyword) throws Exception
	{
		//waits for a new message to arrive from a specific number
		//replies with the keyword on that number
		//remove all msgs before the timestamp
		int timer=-1;
		
		while(++timer<Constant.smsWaitingTime)
		{
			String commandForLastSMSFile="adb shell ls -t /storage/emulated/0/sms";
			String SMSFileName="";
			
			System.out.println("Checking for latest message");
			
			String SMSFileNames=Control.RunProcessWithOutput(commandForLastSMSFile, 15, true);
			System.out.println("Listing All SMS Files Present: \n"+SMSFileNames);

			
			if(SMSFileNames==null)
				SMSFileNames="";
			
			try
			{
				SMSFileNames=SMSFileNames.trim();
			}
			catch(Exception e)
			{
				System.out.println("Exception Moving Old Msgs while trimming SMS File Names: <start>"+SMSFileNames+"<end>");
				e.printStackTrace();
			}
			
			
			SMSFileNames=Control.RunProcessWithOutput(commandForLastSMSFile, 15, true);
			if(SMSFileNames.equals(""))
			{
				//no sms
				System.out.println("No Old files to move..");
				return;
				
			}
			
			//lets parse the SMSs first, and store in SMSFileNames
			else if(SMSFileNames.contains(" "))
			{
				//multiple sms, lets filter
				//check the timestamp of each message, and add to string based on condition
				
				String filesToConsider="";
				try
				{
					String[] filterSMS=SMSFileNames.split(" ");
					for(String tempString:filterSMS)
					{
						if(tempString.trim().equals(""))
							break;
						else
						{
							
							//check timestamp
							double fileTimestamp=Double.parseDouble((tempString.split("_")[2]));
							String tempFileName=tempString.split("_")[1];
							if(fileTimestamp<Constant.lastSmsOrUssdTime)
							{
								//only if this condition is met, move this file
								//filename=tempString
								String moveCommand="adb shell mv /storage/emulated/0/sms/"+tempString+" /storage/emulated/0/smsbackup*/";
								Control.RunProcessWithOutput(moveCommand, 15, false);
								
								
							}
							else
							{
								//indication that a new message has arrived
								//check if it is from the desired sender, only then consider
								for(String tempSender:Constant.specificSenderList)
								{
									if(tempFileName.contains(tempSender))
									{
										//reply with keyword and exit
										System.out.println("Replying with keyword :"+keyword+" on number : "+tempFileName);
										Generic.sendSMS(keyword, tempFileName);
										return;
									}
								}
								
								
								
							}
						}
						
					}
					
					
				}
				catch(Exception e)
				{
					
					
					System.out.println("Exception while moving files with old timestamp, while parsing multiple sms file names : "+e.getMessage());
					e.printStackTrace();
				}
				
			}
			else
				{
						//for single SMS
						//check timestamp
						double fileTimestamp=Double.parseDouble((SMSFileNames.split("_")[2]));
						if(fileTimestamp<Constant.lastSmsOrUssdTime)
						{
							//only if this condition is met, move this file
							//filename=tempString
							String moveCommand="adb shell mv /storage/emulated/0/sms/"+SMSFileNames+" /storage/emulated/0/smsbackup*/";
							Control.RunProcessWithOutput(moveCommand, 15, false);
							return;
							
						}
					}
			
			Thread.sleep(1000);
		}
		
		
	}
	
	
	
	public static void readFirstMessage(String mytext,String BNumber) throws Exception {
		
		//2 scenarios here
		//1 checkpoint vs 1 msg
		//1 checkpoint vs multiple msgs
		//passing percentage : 0.70
		//in case of multiple messages, the one with max percentage wins
		//if passing percentage is not achieved, screenshot will be taken for the one with max percentage
		
		//need to consider only messages after the timestamp - Constant.SmsOrUssdTime
		//to achieve that, lets move the other files	
		moveOldSMSFiles();
		
		//wait for all messages to arrive
		//use this only incase of multiple messages
		//AI condition to check if the checkpoint contains multiple messages or single
		
		//if multiple messages expected, sleep else wont sleep
		String[] validSenders;
		//if(Constant.flagOnlySpecificSenders)
			//validSenders=Constant.senderList.split(",");
		
		if(Constant.numberOfExpectedSms>1)
		{
			System.out.println("Multiple messages expected, hence sleeping for : "+Constant.sleepTimeBeforeCheckingMessages+" milliseconds");
			Thread.sleep(Constant.sleepTimeBeforeCheckingMessages);
		}
			
		

		//get files in ascending order of their retrieval
		String commandForLastSMSFile="adb shell ls -rt /storage/emulated/0/sms";
		  
		String SMSFileName="";
		
		System.out.println("Checking for messages..");
		String SMSFileNames=Control.RunProcessWithOutput(commandForLastSMSFile, 15, true);
		System.out.println("SMS Files : "+SMSFileNames);
		
		if(SMSFileNames==null)
		{
			SMSFileNames="";
		}
		
		boolean flagNoSMS=false;
		boolean flagOneSMS=false;
		boolean flagMultipleSMS=false;
		boolean flagSenderMatched=false;
		
		int timer=-1;
		int numberOfIncomingMsgs=0;
		
		//if the number of expected msgs have not been received,wait..
		while(++timer<Constant.smsWaitingTime)
		{
			SMSFileNames=Control.RunProcessWithOutput(commandForLastSMSFile, 15, true);
			if(SMSFileNames.trim().contains(" "))
			{
				//multiple msgs, lets parse
				numberOfIncomingMsgs=SMSFileNames.split(" ").length;
				if(numberOfIncomingMsgs>=Constant.numberOfExpectedSms)
					break;
			}
			else
			{
				if(!SMSFileNames.trim().equals("") && Constant.numberOfExpectedSms>=1)
					break;
			}
		}
		
		timer=Constant.smsWaitingTime-2;
		while(++timer<Constant.smsWaitingTime)
		{
			System.out.println("Waiting for SMS - try#: "+timer+"/"+Constant.smsWaitingTime);
			SMSFileNames=Control.RunProcessWithOutput(commandForLastSMSFile, 15, true);
			
			if(SMSFileNames==null)
				SMSFileNames="";
			
			SMSFileNames=SMSFileNames.trim();
			
			if(SMSFileNames=="")
			{
				flagNoSMS=true;
				//Thread.sleep(1000);
				//continue;
			}
			

			  if(SMSFileNames.contains(" "))
			  {
				  	flagNoSMS=false;
				  	flagOneSMS=false;
				  	flagMultipleSMS=true;
				  	System.out.println("Multiple SMS Detected : "+SMSFileNames);
				  	

				  	//break only when the sender matches
				  		break;
			  }
			  
			  else
			  {
				  //may be 1 file or no file
				  System.out.print("In else : May be single or no SMS ");
				  
				  if(SMSFileNames.equals(""))
				  {
					  //no files
					  flagNoSMS=true;

					  	flagOneSMS=false;
					  	flagMultipleSMS=false;
					  System.out.println("No SMS detected");
				  }
				  

				  else if(!SMSFileNames.equals(""))
				  {

					  	 
					  	  flagOneSMS=true;
						  flagNoSMS=false;
						  flagMultipleSMS=false;
						  
						  SMSFileName=SMSFileNames;
						  System.out.println("Single SMS detected");

						  
						  break;
				  }
				 
			  }
			  
			Thread.sleep(1000);
			
			
			  
		}//while ends

		if(flagMultipleSMS || flagOneSMS)
		{
			//if there is only one message, compare text and publish results
			if(flagOneSMS)
			{
				String msgText="";
				String commandReadMsg="adb shell cat /storage/emulated/0/sms/"+SMSFileName;
				msgText=Control.RunProcessWithOutput(commandReadMsg, 15, true);
				boolean resultCompare=compareTextSMSBackend(mytext,msgText);
				Generic.printSimilarity(mytext.toLowerCase(), msgText.toLowerCase());
				if(resultCompare)
				{
					Generic.WriteTestData("Checking text message", "% match :"+Constant.lastSmsPercentMatch, "% match :"+Constant.lastSmsPercentMatch, "Expected Text: "+mytext, "Expected text "+mytext+"\nis similar to \nActual : "+msgText, "Pass");
					
					String command="adb shell mv /storage/emulated/0/sms/"+SMSFileName+" /storage/emulated/0/smsbackup*/";
					Control.RunProcessWithOutput(command, 15, false);
					
				}
				else
				{
					Generic.WriteTestData("Checking text message", "% match :"+Constant.lastSmsPercentMatch, "% match :"+Constant.lastSmsPercentMatch, "Expected Text: "+mytext, "Expected text "+mytext+"\nis NOT similar to \nActual : "+msgText, "Fail");
					
				}
				//SMS_8080_1546878233147_20190108_002353.txt
				String[] info=SMSFileName.split("_");
				String incomingNumber=info[1];
				String timestamp=info[3]+"_"+info[4];
				
				Control.openMessageAndTakeScreenshot("SMS from : "+incomingNumber+"\nTimestamp : "+timestamp+"\n\nContent : "+msgText);
			
			}
			
			
			//if there are multiple messages, read one msg, compare, if matches, then delete, else move to next message
			String matchedFileName="";
			float percentageMatch=0.0f;
			if(flagMultipleSMS)
			{
				//complex - get the percentage match, the one with max percentage is the one
				
				//count the number of messages
				int smsCount=SMSFileNames.split(" ").length;
				int ctr=-1;
				
				//iterate over each message and compare text
				String msgText="";
				boolean flagMatchSMS=false;
				while(++ctr<smsCount)
				{
					SMSFileName=SMSFileNames.split(" ")[ctr];
					

					
					//get files in ascending order of their retrieval
					commandForLastSMSFile="adb shell ls -rt /storage/emulated/0/sms";
					  
					
					SMSFileNames=Control.RunProcessWithOutput(commandForLastSMSFile, 15, true);
					
					
					String commandReadMsg="adb shell cat "+SMSFileName;
					msgText=Control.RunProcessWithOutput(commandReadMsg, 15, true);
					
					
					boolean resultCompare=compareTextSMSBackend(mytext,msgText);
					
					if(resultCompare)
					{
						//if matches here, no other verification needed, simply move the sms, write to excel,return
						Generic.WriteTestData("Checking text message", "% match :"+Constant.lastSmsPercentMatch, "% match :"+Constant.lastSmsPercentMatch, "Expected Text: "+mytext, "Expected text "+mytext+"\nis  same as \nActual : "+msgText, "Pass");
						String[] info=SMSFileName.split("_");
						String incomingNumber=info[1];
						String timestamp=info[3]+"_"+info[4];
						Control.openMessageAndTakeScreenshot("SMS from : "+incomingNumber+"\nTimestamp : "+timestamp+"\n\nContent : "+msgText);
						
						
						
						String command="adb shell mv "+SMSFileName+" /storage/emulated/0/smsbackup*/";
						String check=Control.RunProcessWithOutput(command, 15, false);
						System.out.println("Output of mv command : "+check);
						System.out.println("SMS File name : "+check);
						return;
					}
					
					if(Float.compare(Constant.lastSmsPercentMatch,percentageMatch)>=0)
					{
						percentageMatch=Constant.lastSmsPercentMatch;
						matchedFileName=SMSFileName;
					}

				}
				
				if(percentageMatch>Constant.smsPercentageMatch)
				{
					Generic.WriteTestData("Checking text message", "% match :"+percentageMatch, "% match :"+percentageMatch, "Expected Text: "+mytext, "Expected text "+mytext+"\nis similar to \nActual : "+msgText, "Pass");
					
					String command="adb shell mv "+matchedFileName+" /storage/emulated/0/smsbackup*/";
					Control.RunProcessWithOutput(command, 15, false);
				}
				else
				{
					Generic.WriteTestData("Checking text message", "% match :"+percentageMatch, "% match :"+percentageMatch, "Expected Text: "+mytext, "Expected text "+mytext+"\nis NOT similar to \nActual : "+msgText, "Fail");
					
				}
				
			}
			
			
		}
		
		
		else
		{
			//No msg was received
			Date dateForSMS=new Date();
			Generic.WriteTestData("Checking for new message", "Allowed Numbers : "+Constant.senderList, "Timeout seconds: "+Constant.smsWaitingTime, "Msg should be received", "No msg was received until : "+dateForSMS, "Fail");
			return;
		}
		

	
				
	}
		
	
	
	public static String verifyMessageContentsFromOneSpecificSender(String mytext,String BNumber) throws Exception
	{
		

		System.out.println("In verifyMessageContents");
		
		//2 scenarios here
		//1 checkpoint vs 1 msg
		//1 checkpoint vs multiple msgs
		//passing percentage : 0.70
		//in case of multiple messages, the one with max percentage wins
		//if passing percentage is not achieved, screenshot will be taken for the one with max percentage
		
		//need to consider only messages after the timestamp - Constant.SmsOrUssdTime
		//to achieve that, lets move the other files	
		//System.out.println("Moving old files");
		//moveOldSMSFiles();
		//System.out.println("Moved old files");
		
		//wait for all messages to arrive
		//use this only incase of multiple messages
		//AI condition to check if the checkpoint contains multiple messages or single
		
		//if multiple messages expected, sleep else wont sleep
		
	
		
		String[] validSenders;
		String SMSFileName="",SMSFileNames="",commandForLastSMSFile="";
		
		String[] reqNumber=new String[1];
		reqNumber[0]=BNumber;

		
		System.out.println("Checking for specific messages");
		
			for(String temp:reqNumber)
			{
				String commandForLastSMSFile1="adb shell ls -t /storage/emulated/0/sms/*\""+"_"+temp+"\"*";
				commandForLastSMSFile=Control.RunProcessWithOutput(commandForLastSMSFile1, 15, true);
				
				//System.out.println("commandForLastSMSFile : "+commandForLastSMSFile1);
				if(!commandForLastSMSFile.contains("No such"))
					SMSFileNames+=commandForLastSMSFile+" ";
			}
			
			
			
			
			if(SMSFileNames==null)
				SMSFileNames="";
			
			SMSFileNames=SMSFileNames.trim();
		
		//SMSFileNames=Control.RunProcessWithOutput(commandForLastSMSFile, 15, true);
		System.out.println("Selected SMS Files : "+SMSFileNames);
		
		boolean flagNoSMS=false;
		boolean flagOneSMS=false;
		boolean flagMultipleSMS=false;
		boolean flagSenderMatched=false;
		
		int timer=-1;
		int numberOfIncomingMsgs=0;
		
		//if the number of expected msgs have not been received,wait..
		while(++timer<Constant.smsWaitingTime)
		{
			if(Constant.alreadyWaited)
				break;
			
			
			SMSFileNames="";
			for(String temp:reqNumber)
			{
				String commandForLastSMSFile1="adb shell ls -t /storage/emulated/0/sms/*\""+"_"+temp+"\"*";
				commandForLastSMSFile=Control.RunProcessWithOutput(commandForLastSMSFile1, 15, true);
				
				System.out.println("commandForLastSMSFile : "+commandForLastSMSFile1);
				if(!commandForLastSMSFile.contains("No such"))
					SMSFileNames+=commandForLastSMSFile+" ";
			}
			
			
			System.out.println("SMS File Names : "+SMSFileNames);
			
			if(SMSFileNames.trim().contains(" "))
			{
				//multiple msgs, lets parse
				numberOfIncomingMsgs=SMSFileNames.split(" ").length;
				System.out.println("Condition for multiple sms : ");
				System.out.println("Number of incoming msgs : "+numberOfIncomingMsgs);
				System.out.println("Number of expected  : "+Constant.numberOfExpectedReply);
				
				if(numberOfIncomingMsgs>=Constant.numberOfExpectedReply)
					break;
			}
			else
			{
				if(!SMSFileNames.trim().equals("") && Constant.numberOfExpectedReply==1)
				{
					break;
				}
				else if(!SMSFileNames.trim().equals(""))
				{
					//numberOfIncomingMsgs=1;
					System.out.println("Number of incoming messages = 1");
				}
				else
				{
					System.out.println("No incoming messages yet");
				}
					
			}
			
			Thread.sleep(1000);
		}

		
		Constant.alreadyWaited=true;
		
		System.out.println("Outside while now..");
		
		timer=Constant.smsWaitingTime-2;
		
		while(++timer<Constant.smsWaitingTime)
		{
			System.out.println("Waiting for SMS - try#: "+timer+"/"+Constant.smsWaitingTime);
			//SMSFileNames=Control.RunProcessWithOutput(commandForLastSMSFile, 15, true);
			SMSFileNames="";
			for(String temp:Constant.specificSenderList)
			{
				String commandForLastSMSFile1="adb shell ls -t /storage/emulated/0/sms/*\""+"_"+temp+"\"*";
				commandForLastSMSFile=Control.RunProcessWithOutput(commandForLastSMSFile1, 15, true);
				
				//System.out.println("commandForLastSMSFile : "+commandForLastSMSFile1);
				if(!commandForLastSMSFile.contains("No such"))
					SMSFileNames+=commandForLastSMSFile+" ";
			}
			
			System.out.println("In second while : SMS File Names : "+SMSFileNames);
			
			if(SMSFileNames==null)
				SMSFileNames="";
			
			SMSFileNames=SMSFileNames.trim();
			
			if(SMSFileNames=="")
			{
				flagNoSMS=true;
				//Thread.sleep(1000);
				//continue;
			}
			
			
		/*
		 * 
		 * Here the condition of actual SMS verification starts
		 * 
		 * 	
		 * 
		 */
			
			

			  if(SMSFileNames.contains(" "))
			  {
				  	flagNoSMS=false;
				  	flagOneSMS=false;
				  	flagMultipleSMS=true;
				  	String[] eachMsg=SMSFileNames.split(" ");
				  	System.out.println("Multiple SMS Detected : "+SMSFileNames);

			  		break;
			  }
			  
			  else
			  {
				  //may be 1 file or no file
				  System.out.print("In else : May be single or no SMS ");
				  
				  if(SMSFileNames.equals(""))
				  {
					  //no files
					  flagNoSMS=true;
					  flagMultipleSMS=false;
			  			flagOneSMS=false;
					  System.out.println("No SMS detected");
				  }
				  

				  else if(!SMSFileNames.equals(""))
				  {
					  //one sms
					  	  flagOneSMS=true;
						  flagMultipleSMS=false;
					  		flagNoSMS=false;
						  SMSFileName=SMSFileNames;
						  System.out.println("Single SMS detected");
						  
						  break;
				  }
				 
			  }
			  
			  //Thread.sleep(1000);
			  break;

			  
		}//while ends

		System.out.println("Lets check the content of sms file names : "+SMSFileNames);
		System.out.println("Lets see the flags : \nflagMultipleSMS : "+flagMultipleSMS+"\nflagOneSMS : "+flagOneSMS+"\nflagNoSMS : "+flagNoSMS);
		
		if(flagMultipleSMS || flagOneSMS)
		{
			//if there is only one message, compare text and publish results
			if(flagOneSMS)
			{
				String msgText="";
				String commandReadMsg="adb shell cat "+SMSFileName;
				msgText=Control.RunProcessWithOutput(commandReadMsg, 15, true);
				boolean resultCompare=compareTextSMSBackend(mytext,msgText);
				Generic.printSimilarity(mytext, msgText);
				if(resultCompare)
				{
					Generic.WriteTestData("Checking text message", "% match :"+Constant.lastSmsPercentMatch, "% match :"+Constant.lastSmsPercentMatch, "Expected Text: "+mytext, "Expected text "+mytext+"\nis similar to \nActual : "+msgText, "Pass");
					
					//String command="adb shell mv "+SMSFileName+" /storage/emulated/0/smsbackup*/";
					//Control.RunProcessWithOutput(command, 15, false);
					
				}
				else
				{
					Generic.WriteTestData("Checking text message", "% match :"+Constant.lastSmsPercentMatch, "% match :"+Constant.lastSmsPercentMatch, "Expected Text: "+mytext, "Expected text "+mytext+"\nis NOT similar to \nActual : "+msgText, "Fail");
					
				}
				//SMS_8080_1546878233147_20190108_002353.txt
				String[] info=SMSFileName.split("_");
				String incomingNumber=info[1];
				String timestamp=info[3]+"_"+info[4];
				
				Control.openMessageAndTakeScreenshot("SMS from : "+incomingNumber+"\nTimestamp : "+timestamp+"\n\nContent : "+msgText);
				return SMSFileName;
			}
			
			
			//if there are multiple messages, read one msg, compare, if matches, then delete, else move to next message
			String matchedFileName="";
			float percentageMatch=0.0f;
			if(flagMultipleSMS)
			{
				//complex - get the percentage match, the one with max percentage is the one
				
				//count the number of messages
				int smsCount=SMSFileNames.split(" ").length;
				int ctr=-1;
				
				//iterate over each message and compare text
				String msgText="";
				boolean flagMatchSMS=false;
				while(++ctr<smsCount)
				{
					SMSFileName=SMSFileNames.split(" ")[ctr];
					
					String commandReadMsg="adb shell cat "+SMSFileName;
					msgText=Control.RunProcessWithOutput(commandReadMsg, 15, true);
					
					
					boolean resultCompare=compareTextSMSBackend(mytext,msgText);
					Generic.printSimilarity(mytext, msgText);
					
					if(resultCompare)
					{
						//if matches here, no other verification needed, simply move the sms, write to excel,return
						Generic.WriteTestData("Checking text message", "% match :"+percentageMatch, "% match :"+percentageMatch, "Expected Text: "+mytext, "Expected text "+mytext+"\nis  same as \nActual : "+msgText, "Pass");
						String[] info=SMSFileName.split("_");
						String incomingNumber=info[1];
						String timestamp=info[3]+"_"+info[4];
						Control.openMessageAndTakeScreenshot("SMS from : "+incomingNumber+"\nTimestamp : "+timestamp+"\n\nContent : "+msgText);
						
						//String command="adb shell mv "+SMSFileName+" /storage/emulated/0/smsbackup*/";
						//Control.RunProcessWithOutput(command, 15, false);
						return SMSFileName;
					}
					
					if(Float.compare(Constant.lastSmsPercentMatch,percentageMatch)>=0)
					{
						percentageMatch=Constant.lastSmsPercentMatch;
						matchedFileName=SMSFileName;
					}

				}
				
				
				String[] info=matchedFileName.split("_");
				String incomingNumber=info[1];
				String timestamp=info[3]+"_"+info[4];
				
				if(percentageMatch>Constant.smsPercentageMatch)
				{
					Generic.WriteTestData("Checking text message", "% match :"+percentageMatch, "% match :"+percentageMatch, "Expected Text: "+mytext, "Expected text "+mytext+"\nis similar to \nActual : "+msgText, "Pass");
					
					Control.openMessageAndTakeScreenshot("SMS from : "+incomingNumber+"\nTimestamp : "+timestamp+"\n\nContent : "+msgText);
					
					return matchedFileName;
				}
				else
				{

					Generic.WriteTestData("Checking text message", "% match :"+percentageMatch, "% match :"+percentageMatch, "Expected Text: "+mytext, "Expected text "+mytext+"\nis NOT similar to \nActual : "+msgText, "Fail");
					Control.openMessageAndTakeScreenshot("SMS from : "+incomingNumber+"\nTimestamp : "+timestamp+"\n\nContent : "+msgText);
					
					
				}
				
			}
			
			
		}
		
		
		else
		{
			//No msg was received
			Date dateForSMS=new Date();
			Generic.WriteTestData("Checking for new message from Allowed Numbers Only", "Allowed Numbers : "+Constant.senderList, "Timeout seconds: "+Constant.smsWaitingTime, "Msg should be received "+dateForSMS, "No msg was received until "+dateForSMS, "Fail");
			return null;
		}
		
		return null;
	}
	
	
	public static String verifyMessageContents(String mytext,String BNumber) throws Exception
	{
		

		System.out.println("In verifyMessageContents");
		System.exit(-1);
		//2 scenarios here
		//1 checkpoint vs 1 msg
		//1 checkpoint vs multiple msgs
		//passing percentage : 0.70
		//in case of multiple messages, the one with max percentage wins
		//if passing percentage is not achieved, screenshot will be taken for the one with max percentage
		
		//need to consider only messages after the timestamp - Constant.SmsOrUssdTime
		//to achieve that, lets move the other files	
		//System.out.println("Moving old files");
		//moveOldSMSFiles();
		//System.out.println("Moved old files");
		
		//wait for all messages to arrive
		//use this only incase of multiple messages
		//AI condition to check if the checkpoint contains multiple messages or single
		
		//if multiple messages expected, sleep else wont sleep
		
	
		
		String[] validSenders;
		String SMSFileName="",SMSFileNames="",commandForLastSMSFile="";

		
		System.out.println("Checking for specific messages");
		
			for(String temp:Constant.specificSenderList)
			{
				String commandForLastSMSFile1="adb shell ls -t /storage/emulated/0/sms/*\""+"_"+temp+"\"*";
				commandForLastSMSFile=Control.RunProcessWithOutput(commandForLastSMSFile1, 15, true);

				if(!commandForLastSMSFile.contains("No such"))
					SMSFileNames+=commandForLastSMSFile+" ";
			}
			
			
			
			
			if(SMSFileNames==null)
				SMSFileNames="";
			
			SMSFileNames=SMSFileNames.trim();
		
		//SMSFileNames=Control.RunProcessWithOutput(commandForLastSMSFile, 15, true);
		System.out.println("Selected SMS Files : "+SMSFileNames);
		
		boolean flagNoSMS=false;
		boolean flagOneSMS=false;
		boolean flagMultipleSMS=false;
		boolean flagSenderMatched=false;
		
		int timer=-1;
		int numberOfIncomingMsgs=0;
		
		//if the number of expected msgs have not been received,wait..
		while(++timer<Constant.smsWaitingTime)
		{
			if(Constant.alreadyWaited)
				break;
			
			
			SMSFileNames="";
			for(String temp:Constant.specificSenderList)
			{
				String commandForLastSMSFile1="adb shell ls -t /storage/emulated/0/sms/*\""+"_"+temp+"\"*";
				commandForLastSMSFile=Control.RunProcessWithOutput(commandForLastSMSFile1, 15, true);
				
				System.out.println("commandForLastSMSFile : "+commandForLastSMSFile1);
				if(!commandForLastSMSFile.contains("No such"))
					SMSFileNames+=commandForLastSMSFile+" ";
			}
			
			
			System.out.println("SMS File Names : "+SMSFileNames);
			
			if(SMSFileNames.trim().contains(" "))
			{
				//multiple msgs, lets parse
				numberOfIncomingMsgs=SMSFileNames.split(" ").length;
				System.out.println("Condition for multiple sms : ");
				System.out.println("Number of incoming msgs : "+numberOfIncomingMsgs);
				System.out.println("Number of expected  : "+Constant.numberOfExpectedReply);
				
				if(numberOfIncomingMsgs>=Constant.numberOfExpectedReply)
					break;
			}
			else
			{
				if(!SMSFileNames.trim().equals("") && Constant.numberOfExpectedReply==1)
				{
					break;
				}
				else if(!SMSFileNames.trim().equals(""))
				{
					//numberOfIncomingMsgs=1;
					System.out.println("Number of incoming messages = 1");
				}
				else
				{
					System.out.println("No incoming messages yet");
				}
					
			}
			
			Thread.sleep(1000);
		}

		
		Constant.alreadyWaited=true;
		
		System.out.println("Outside while now..");
		
		timer=Constant.smsWaitingTime-2;
		
		while(++timer<Constant.smsWaitingTime)
		{
			System.out.println("Waiting for SMS - try#: "+timer+"/"+Constant.smsWaitingTime);
			//SMSFileNames=Control.RunProcessWithOutput(commandForLastSMSFile, 15, true);
			SMSFileNames="";
			for(String temp:Constant.specificSenderList)
			{
				String commandForLastSMSFile1="adb shell ls -t /storage/emulated/0/sms/*\""+"_"+temp+"\"*";
				commandForLastSMSFile=Control.RunProcessWithOutput(commandForLastSMSFile1, 15, true);
				
				//System.out.println("commandForLastSMSFile : "+commandForLastSMSFile1);
				if(!commandForLastSMSFile.contains("No such"))
					SMSFileNames+=commandForLastSMSFile+" ";
			}
			
			System.out.println("In second while : SMS File Names : "+SMSFileNames);
			
			if(SMSFileNames==null)
				SMSFileNames="";
			
			SMSFileNames=SMSFileNames.trim();
			
			if(SMSFileNames=="")
			{
				flagNoSMS=true;
				//Thread.sleep(1000);
				//continue;
			}
			
			
		/*
		 * 
		 * Here the condition of actual SMS verification starts
		 * 
		 * 	
		 * 
		 */
			
			

			  if(SMSFileNames.contains(" "))
			  {
				  	flagNoSMS=false;
				  	flagOneSMS=false;
				  	flagMultipleSMS=true;
				  	String[] eachMsg=SMSFileNames.split(" ");
				  	System.out.println("Multiple SMS Detected : "+SMSFileNames);

			  		break;
			  }
			  
			  else
			  {
				  //may be 1 file or no file
				  System.out.print("In else : May be single or no SMS ");
				  
				  if(SMSFileNames.equals(""))
				  {
					  //no files
					  flagNoSMS=true;
					  flagMultipleSMS=false;
			  			flagOneSMS=false;
					  System.out.println("No SMS detected");
				  }
				  

				  else if(!SMSFileNames.equals(""))
				  {
					  //one sms
					  	  flagOneSMS=true;
						  flagMultipleSMS=false;
					  		flagNoSMS=false;
						  SMSFileName=SMSFileNames;
						  System.out.println("Single SMS detected");
						  
						  break;
				  }
				 
			  }
			  
			  //Thread.sleep(1000);
			  break;

			  
		}//while ends

		System.out.println("Lets check the content of sms file names : "+SMSFileNames);
		System.out.println("Lets see the flags : \nflagMultipleSMS : "+flagMultipleSMS+"\nflagOneSMS : "+flagOneSMS+"\nflagNoSMS : "+flagNoSMS);
		
		if(flagMultipleSMS || flagOneSMS)
		{
			//if there is only one message, compare text and publish results
			if(flagOneSMS)
			{
				String msgText="";
				String commandReadMsg="adb shell cat "+SMSFileName;
				msgText=Control.RunProcessWithOutput(commandReadMsg, 15, true);
				boolean resultCompare=compareTextSMSBackend(mytext,msgText);
				Generic.printSimilarity(mytext, msgText);
				if(resultCompare)
				{
					Generic.WriteTestData("Checking text message", "% match :"+Constant.lastSmsPercentMatch, "% match :"+Constant.lastSmsPercentMatch, "Expected Text: "+mytext, "Expected text "+mytext+"\nis similar to \nActual : "+msgText, "Pass");
					
					//String command="adb shell mv "+SMSFileName+" /storage/emulated/0/smsbackup*/";
					//Control.RunProcessWithOutput(command, 15, false);
					
				}
				else
				{
					Generic.WriteTestData("Checking text message", "% match :"+Constant.lastSmsPercentMatch, "% match :"+Constant.lastSmsPercentMatch, "Expected Text: "+mytext, "Expected text "+mytext+"\nis NOT similar to \nActual : "+msgText, "Fail");
					
				}
				//SMS_8080_1546878233147_20190108_002353.txt
				String[] info=SMSFileName.split("_");
				String incomingNumber=info[1];
				String timestamp=info[3]+"_"+info[4];
				
				Control.openMessageAndTakeScreenshot("SMS from : "+incomingNumber+"\nTimestamp : "+timestamp+"\n\nContent : "+msgText);
				return SMSFileName;
			}
			
			
			//if there are multiple messages, read one msg, compare, if matches, then delete, else move to next message
			String matchedFileName="";
			float percentageMatch=0.0f;
			if(flagMultipleSMS)
			{
				//complex - get the percentage match, the one with max percentage is the one
				
				//count the number of messages
				int smsCount=SMSFileNames.split(" ").length;
				int ctr=-1;
				
				//iterate over each message and compare text
				String msgText="";
				boolean flagMatchSMS=false;
				while(++ctr<smsCount)
				{
					SMSFileName=SMSFileNames.split(" ")[ctr];
					
					String commandReadMsg="adb shell cat "+SMSFileName;
					msgText=Control.RunProcessWithOutput(commandReadMsg, 15, true);
					
					
					boolean resultCompare=compareTextSMSBackend(mytext,msgText);
					Generic.printSimilarity(mytext, msgText);
					
					if(resultCompare)
					{
						//if matches here, no other verification needed, simply move the sms, write to excel,return
						Generic.WriteTestData("Checking text message", "% match :"+percentageMatch, "% match :"+percentageMatch, "Expected Text: "+mytext, "Expected text "+mytext+"\nis  same as \nActual : "+msgText, "Pass");
						String[] info=SMSFileName.split("_");
						String incomingNumber=info[1];
						String timestamp=info[3]+"_"+info[4];
						Control.openMessageAndTakeScreenshot("SMS from : "+incomingNumber+"\nTimestamp : "+timestamp+"\n\nContent : "+msgText);
						
						//String command="adb shell mv "+SMSFileName+" /storage/emulated/0/smsbackup*/";
						//Control.RunProcessWithOutput(command, 15, false);
						return SMSFileName;
					}
					
					if(Float.compare(Constant.lastSmsPercentMatch,percentageMatch)>=0)
					{
						percentageMatch=Constant.lastSmsPercentMatch;
						matchedFileName=SMSFileName;
					}

				}
				
				
				String[] info=matchedFileName.split("_");
				String incomingNumber=info[1];
				String timestamp=info[3]+"_"+info[4];
				
				if(percentageMatch>Constant.smsPercentageMatch)
				{
					Generic.WriteTestData("Checking text message", "% match :"+percentageMatch, "% match :"+percentageMatch, "Expected Text: "+mytext, "Expected text "+mytext+"\nis similar to \nActual : "+msgText, "Pass");
					
					Control.openMessageAndTakeScreenshot("SMS from : "+incomingNumber+"\nTimestamp : "+timestamp+"\n\nContent : "+msgText);
					
					//String command="adb shell mv "+matchedFileName+" /storage/emulated/0/smsbackup*/";
					//Control.RunProcessWithOutput(command, 15, false);
					return matchedFileName;
				}
				else
				{

					Generic.WriteTestData("Checking text message", "% match :"+percentageMatch, "% match :"+percentageMatch, "Expected Text: "+mytext, "Expected text "+mytext+"\nis NOT similar to \nActual : "+msgText, "Fail");
					Control.openMessageAndTakeScreenshot("SMS from : "+incomingNumber+"\nTimestamp : "+timestamp+"\n\nContent : "+msgText);
					
					
				}
				
			}
			
			
		}
		
		
		else
		{
			//No msg was received
			Date dateForSMS=new Date();
			Generic.WriteTestData("Checking for new message from Allowed Numbers Only", "Allowed Numbers : "+Constant.senderList, "Timeout seconds: "+Constant.smsWaitingTime, "Msg should be received "+dateForSMS, "No msg was received until "+dateForSMS, "Fail");
			return null;
		}
		
		return null;
	}
	
	
	
	public static void ReadMessageFromOneSpecificSender(String mytext,String BNumber) throws Exception
	{

		
		//2 scenarios here
		//1 checkpoint vs 1 msg
		//1 checkpoint vs multiple msgs
		//passing percentage : 0.70
		//in case of multiple messages, the one with max percentage wins
		//if passing percentage is not achieved, screenshot will be taken for the one with max percentage
		
		//need to consider only messages after the timestamp - Constant.SmsOrUssdTime
		//to achieve that, lets move the other files	
		System.out.println("Moving old SMS files");
		moveOldSMSFiles();
		//System.out.println("Moved old files");
		
		//wait for all messages to arrive
		//use this only incase of multiple messages
		//AI condition to check if the checkpoint contains multiple messages or single
		
		//if multiple messages expected, sleep else wont sleep
		
	
		
		String[] validSenders;
		String SMSFileName="",SMSFileNames="",commandForLastSMSFile="";

		
		boolean flagNoSMS=false;
		boolean flagOneSMS=false;
		boolean flagMultipleSMS=false;
		boolean flagSenderMatched=false;
		
		int timer=-1;
		int numberOfIncomingMsgs=0;
		
		String[] reqNumber=new String[1];
		reqNumber[0]=BNumber;
		
		
/*
 * This block is for getting all expected messages until a configurable timeout
 * 
 */
		//if the number of expected msgs have not been received,wait..
		while(++timer<Constant.smsWaitingTime)
		{
			SMSFileNames="";
			for(String temp:reqNumber)
			{
				String commandForLastSMSFile1="adb shell ls -rt /storage/emulated/0/sms/*\""+"_"+temp+"\"*";
				commandForLastSMSFile=Control.RunProcessWithOutput(commandForLastSMSFile1, 15, true);
				
				//System.out.println("commandForLastSMSFile : "+commandForLastSMSFile1);
				if(!commandForLastSMSFile.contains("No such"))
					SMSFileNames+=commandForLastSMSFile+" ";
			}

			
			if(SMSFileNames.trim().contains(" "))
			{
				//multiple msgs, lets parse
				numberOfIncomingMsgs=SMSFileNames.split(" ").length;
				System.out.println("Condition for multiple sms : ");
				System.out.println("Number of incoming msgs : "+numberOfIncomingMsgs);
				System.out.println("Number of expected SMS : "+Constant.numberOfExpectedSms);
				
				if(numberOfIncomingMsgs>=Constant.numberOfExpectedSms)
					break;
			}
			else
			{
				if(!SMSFileNames.trim().equals("") && Constant.numberOfExpectedSms==1)
				{
					break;
				}
				else if(!SMSFileNames.trim().equals(""))
				{
					numberOfIncomingMsgs=1;
					System.out.println("Number of incoming messages = 1");
				}
				else
				{
					System.out.println("No incoming messages yet");
				}
					
			}
			
			Thread.sleep(1000);
		}

		/*
		 * 
		 * Block ends - expectation is that by now the number of expected messages would have arrived
		 * 
		 */
		
		System.out.println("Outside while : By now expected messages should have arrived");
		
		timer=Constant.smsWaitingTime-2;
		
		while(++timer<Constant.smsWaitingTime)
		{
			System.out.println("Waiting for SMS - try#: "+timer+"/"+Constant.smsWaitingTime);
			//SMSFileNames=Control.RunProcessWithOutput(commandForLastSMSFile, 15, true);
			SMSFileNames="";
			for(String temp:reqNumber)
			{
				String commandForLastSMSFile1="adb shell ls -rt /storage/emulated/0/sms/*\""+"_"+temp+"\"*";
				commandForLastSMSFile=Control.RunProcessWithOutput(commandForLastSMSFile1, 15, true);
				
				//System.out.println("commandForLastSMSFile : "+commandForLastSMSFile1);
				if(!commandForLastSMSFile.contains("No such"))
					SMSFileNames+=commandForLastSMSFile+" ";
			}
			
			System.out.println("In second while : SMS File Names : "+SMSFileNames);
			
			if(SMSFileNames==null)
				SMSFileNames="";
			
			SMSFileNames=SMSFileNames.trim();
			
			if(SMSFileNames=="")
			{
				flagNoSMS=true;
				//Thread.sleep(1000);
				//continue;
			}
			
			
		/*
		 * 
		 * Here the condition of actual SMS verification starts
		 * 
		 * 	
		 * 
		 */
			
			

			  if(SMSFileNames.contains(" "))
			  {
				  	flagNoSMS=false;
				  	flagOneSMS=false;
				  	flagMultipleSMS=true;
				  	String[] eachMsg=SMSFileNames.split(" ");
				  	System.out.println("Multiple SMS Detected : "+SMSFileNames);

			  		break;
			  }
			  
			  else
			  {
				  //may be 1 file or no file
				  System.out.print("In else : May be single or no SMS ");
				  
				  if(SMSFileNames.equals(""))
				  {
					  //no files
					  flagNoSMS=true;
					  flagMultipleSMS=false;
			  			flagOneSMS=false;
					  System.out.println("No SMS detected");
				  }
				  

				  else if(!SMSFileNames.equals(""))
				  {
					  //one sms
					  	  flagOneSMS=true;
						  flagMultipleSMS=false;
					  		flagNoSMS=false;
						  SMSFileName=SMSFileNames;
						  System.out.println("Single SMS detected");
						  
						  break;
				  }
				 
			  }
			  
			  //Thread.sleep(1000);
			  break;

			  
		}//while ends

		System.out.println("All sms file names : "+SMSFileNames);
		System.out.println("Lets see the flags : \nflagMultipleSMS : "+flagMultipleSMS+"\nflagOneSMS : "+flagOneSMS+"\nflagNoSMS : "+flagNoSMS);
		
		if(flagMultipleSMS || flagOneSMS)
		{
			//if there is only one message, compare text and publish results
			if(flagOneSMS)
			{
				String msgText="";
				String commandReadMsg="adb shell cat "+SMSFileName;
				msgText=Control.RunProcessWithOutput(commandReadMsg, 15, true);
				boolean resultCompare=compareTextSMSBackend(mytext,msgText);
				Generic.printSimilarity(mytext, msgText);
				if(resultCompare)
				{
					Generic.WriteTestData("Checking text message", "% match :"+Constant.lastSmsPercentMatch, "% match :"+Constant.lastSmsPercentMatch, "Expected Text: "+mytext, "Expected text "+mytext+"\nis similar to \nActual : "+msgText, "Pass");
					
					String command="adb shell mv "+SMSFileName+" /storage/emulated/0/smsbackup*/";
					Control.RunProcessWithOutput(command, 15, false);
					--Constant.numberOfExpectedSms;
				}
				else
				{
					Generic.WriteTestData("Checking text message", "% match :"+Constant.lastSmsPercentMatch, "% match :"+Constant.lastSmsPercentMatch, "Expected Text: "+mytext, "Expected text "+mytext+"\nis NOT similar to \nActual : "+msgText, "Fail");
					
				}
				//SMS_8080_1546878233147_20190108_002353.txt
				String[] info=SMSFileName.split("_");
				String incomingNumber=info[1];
				String timestamp=info[3]+"_"+info[4];
				
				Control.openMessageAndTakeScreenshot("SMS from : "+incomingNumber+"\nTimestamp : "+timestamp+"\n\nContent : "+msgText);
				return;
			}
			
			
			//if there are multiple messages, read one msg, compare, if matches, then delete, else move to next message
			String matchedFileName="";
			float percentageMatch=0.0f;
			if(flagMultipleSMS)
			{
				//complex - get the percentage match, the one with max percentage is the one
				
				//count the number of messages
				int smsCount=SMSFileNames.split(" ").length;
				int ctr=-1;
				
				//iterate over each message and compare text
				String msgText="";
				boolean flagMatchSMS=false;
				while(++ctr<smsCount)
				{
					SMSFileName=SMSFileNames.split(" ")[ctr];
					
					String commandReadMsg="adb shell cat "+SMSFileName;
					msgText=Control.RunProcessWithOutput(commandReadMsg, 15, true);
					
					
					boolean resultCompare=compareTextSMSBackend(mytext,msgText);
					Generic.printSimilarity(mytext, msgText);
					
					if(resultCompare)
					{
						//if matches here, no other verification needed, simply move the sms, write to excel,return
						Generic.WriteTestData("Checking text message", "% match :"+percentageMatch, "% match :"+percentageMatch, "Expected Text: "+mytext, "Expected text "+mytext+"\nis  same as \nActual : "+msgText, "Pass");
						String[] info=SMSFileName.split("_");
						String incomingNumber=info[1];
						String timestamp=info[3]+"_"+info[4];
						Control.openMessageAndTakeScreenshot("SMS from : "+incomingNumber+"\nTimestamp : "+timestamp+"\n\nContent : "+msgText);
						
						String command="adb shell mv "+SMSFileName+" /storage/emulated/0/smsbackup*/";
						Control.RunProcessWithOutput(command, 15, false);
						--Constant.numberOfExpectedSms;
						return;
					}
					
					if(Float.compare(Constant.lastSmsPercentMatch,percentageMatch)>=0)
					{
						percentageMatch=Constant.lastSmsPercentMatch;
						matchedFileName=SMSFileName;
					}

				}
				
				
				String[] info=matchedFileName.split("_");
				String incomingNumber=info[1];
				String timestamp=info[3]+"_"+info[4];
				
				if(percentageMatch>Constant.smsPercentageMatch)
				{
					Generic.WriteTestData("Checking text message", "% match :"+percentageMatch, "% match :"+percentageMatch, "Expected Text: "+mytext, "Expected text "+mytext+"\nis similar to \nActual : "+msgText, "Pass");
					
					Control.openMessageAndTakeScreenshot("SMS from : "+incomingNumber+"\nTimestamp : "+timestamp+"\n\nContent : "+msgText);
					
					String command="adb shell mv "+matchedFileName+" /storage/emulated/0/smsbackup*/";
					Control.RunProcessWithOutput(command, 15, false);
					--Constant.numberOfExpectedSms;
				}
				else
				{

					Generic.WriteTestData("Checking text message", "% match :"+percentageMatch, "% match :"+percentageMatch, "Expected Text: "+mytext, "Expected text "+mytext+"\nis NOT similar to \nActual : "+msgText, "Fail");
					Control.openMessageAndTakeScreenshot("SMS from : "+incomingNumber+"\nTimestamp : "+timestamp+"\n\nContent : "+msgText);
					
					
				}
				
			}
			
			
		}
		
		
		else
		{
			//No msg was received
			Date dateForSMS=new Date();
			Generic.WriteTestData("From Allowed Numbers, no message was received", "Allowed Numbers : "+Constant.senderList, "Timeout seconds: "+Constant.smsWaitingTime, "Msg should be received from Sender List : "+Constant.senderList, "No msg was received was allowed number list : "+Constant.senderList+" until "+dateForSMS, "Fail");
			return;
		}
		

	
				
	
	
	}
	
	
	
	public static void ReadFirstMessageForSpecificSenders(String mytext,String BNumber) throws Exception
	{
		
		//2 scenarios here
		//1 checkpoint vs 1 msg
		//1 checkpoint vs multiple msgs
		//passing percentage : 0.70
		//in case of multiple messages, the one with max percentage wins
		//if passing percentage is not achieved, screenshot will be taken for the one with max percentage
		
		//need to consider only messages after the timestamp - Constant.SmsOrUssdTime
		//to achieve that, lets move the other files	
		System.out.println("Moving old SMS files");
		moveOldSMSFiles();
		//System.out.println("Moved old files");
		
		//wait for all messages to arrive
		//use this only incase of multiple messages
		//AI condition to check if the checkpoint contains multiple messages or single
		
		//if multiple messages expected, sleep else wont sleep
		
	
		
		String[] validSenders;
		String SMSFileName="",SMSFileNames="",commandForLastSMSFile="";

		
		boolean flagNoSMS=false;
		boolean flagOneSMS=false;
		boolean flagMultipleSMS=false;
		boolean flagSenderMatched=false;
		
		int timer=-1;
		int numberOfIncomingMsgs=0;
		
		
/*
 * This block is for getting all expected messages until a configurable timeout
 * 
 */
		//if the number of expected msgs have not been received,wait..
		while(++timer<Constant.smsWaitingTime)
		{
			
			if(Constant.alreadyWaited)
				break;
			else
				Constant.alreadyWaited=true;
			
			SMSFileNames="";
			for(String temp:Constant.specificSenderList)
			{
				String commandForLastSMSFile1="adb shell ls -rt /storage/emulated/0/sms/*\""+"_"+temp+"\"*";
				commandForLastSMSFile=Control.RunProcessWithOutput(commandForLastSMSFile1, 15, true);
				
				//System.out.println("commandForLastSMSFile : "+commandForLastSMSFile1);
				if(!commandForLastSMSFile.contains("No such"))
					SMSFileNames+=commandForLastSMSFile+" ";
			}

			
			if(SMSFileNames.trim().contains(" "))
			{
				//multiple msgs, lets parse
				numberOfIncomingMsgs=SMSFileNames.split(" ").length;
				System.out.println("Condition for multiple sms : ");
				System.out.println("Number of incoming msgs : "+numberOfIncomingMsgs);
				System.out.println("Number of expected SMS : "+Constant.numberOfExpectedSms);
				
				if(numberOfIncomingMsgs>=Constant.numberOfExpectedSms)
					break;
			}
			else
			{
				if(!SMSFileNames.trim().equals("") && Constant.numberOfExpectedSms==1)
				{
					break;
				}
				else if(!SMSFileNames.trim().equals(""))
				{
					numberOfIncomingMsgs=1;
					System.out.println("Number of incoming messages = 1");
				}
				else
				{
					System.out.println("No incoming messages yet");
				}
					
			}
			
			Thread.sleep(1000);
		}

		/*
		 * 
		 * Block ends - expectation is that by now the number of expected messages would have arrived
		 * 
		 */
		
		System.out.println("Outside while : By now expected messages should have arrived");
		
		timer=Constant.smsWaitingTime-2;
		
		while(++timer<Constant.smsWaitingTime)
		{
			System.out.println("Waiting for SMS - try#: "+timer+"/"+Constant.smsWaitingTime);
			//SMSFileNames=Control.RunProcessWithOutput(commandForLastSMSFile, 15, true);
			SMSFileNames="";
			for(String temp:Constant.specificSenderList)
			{
				String commandForLastSMSFile1="adb shell ls -rt /storage/emulated/0/sms/*\""+"_"+temp+"\"*";
				commandForLastSMSFile=Control.RunProcessWithOutput(commandForLastSMSFile1, 15, true);
				
				//System.out.println("commandForLastSMSFile : "+commandForLastSMSFile1);
				if(!commandForLastSMSFile.contains("No such"))
					SMSFileNames+=commandForLastSMSFile+" ";
			}
			
			System.out.println("In second while : SMS File Names : "+SMSFileNames);
			
			if(SMSFileNames==null)
				SMSFileNames="";
			
			SMSFileNames=SMSFileNames.trim();
			
			if(SMSFileNames=="")
			{
				flagNoSMS=true;
				//Thread.sleep(1000);
				//continue;
			}
			
			
		/*
		 * 
		 * Here the condition of actual SMS verification starts
		 * 
		 * 	
		 * 
		 */
			
			

			  if(SMSFileNames.contains(" "))
			  {
				  	flagNoSMS=false;
				  	flagOneSMS=false;
				  	flagMultipleSMS=true;
				  	String[] eachMsg=SMSFileNames.split(" ");
				  	System.out.println("Multiple SMS Detected : "+SMSFileNames);

			  		break;
			  }
			  
			  else
			  {
				  //may be 1 file or no file
				  System.out.print("In else : May be single or no SMS ");
				  
				  if(SMSFileNames.equals(""))
				  {
					  //no files
					  flagNoSMS=true;
					  flagMultipleSMS=false;
			  			flagOneSMS=false;
					  System.out.println("No SMS detected");
				  }
				  

				  else if(!SMSFileNames.equals(""))
				  {
					  //one sms
					  	  flagOneSMS=true;
						  flagMultipleSMS=false;
					  		flagNoSMS=false;
						  SMSFileName=SMSFileNames;
						  System.out.println("Single SMS detected");
						  
						  break;
				  }
				 
			  }
			  
			  //Thread.sleep(1000);
			  break;

			  
		}//while ends

		System.out.println("All sms file names : "+SMSFileNames);
		System.out.println("Lets see the flags : \nflagMultipleSMS : "+flagMultipleSMS+"\nflagOneSMS : "+flagOneSMS+"\nflagNoSMS : "+flagNoSMS);
		
		if(flagMultipleSMS || flagOneSMS)
		{
			//if there is only one message, compare text and publish results
			if(flagOneSMS)
			{
				String msgText="";
				String commandReadMsg="adb shell cat "+SMSFileName;
				msgText=Control.RunProcessWithOutput(commandReadMsg, 15, true);
				boolean resultCompare=compareTextSMSBackend(mytext,msgText);
				Generic.printSimilarity(mytext, msgText);
				if(resultCompare)
				{
					Generic.WriteTestData("Checking text message", "% match :"+Constant.lastSmsPercentMatch, "% match :"+Constant.lastSmsPercentMatch, "Expected Text: "+mytext, "Expected text "+mytext+"\nis similar to \nActual : "+msgText, "Pass");
					
					String command="adb shell mv "+SMSFileName+" /storage/emulated/0/smsbackup*/";
					Control.RunProcessWithOutput(command, 15, false);
					--Constant.numberOfExpectedSms;
				}
				else
				{
					Generic.WriteTestData("Checking text message", "% match :"+Constant.lastSmsPercentMatch, "% match :"+Constant.lastSmsPercentMatch, "Expected Text: "+mytext, "Expected text "+mytext+"\nis NOT similar to \nActual : "+msgText, "Fail");
					
				}
				//SMS_8080_1546878233147_20190108_002353.txt
				String[] info=SMSFileName.split("_");
				String incomingNumber=info[1];
				String timestamp=info[3]+"_"+info[4];
				
				Control.openMessageAndTakeScreenshot("SMS from : "+incomingNumber+"\nTimestamp : "+timestamp+"\n\nContent : "+msgText);
				return;
			}
			
			
			//if there are multiple messages, read one msg, compare, if matches, then delete, else move to next message
			String matchedFileName="";
			float percentageMatch=0.0f;
			if(flagMultipleSMS)
			{
				//complex - get the percentage match, the one with max percentage is the one
				
				//count the number of messages
				int smsCount=SMSFileNames.split(" ").length;
				int ctr=-1;
				
				//iterate over each message and compare text
				String msgText="";
				boolean flagMatchSMS=false;
				while(++ctr<smsCount)
				{
					SMSFileName=SMSFileNames.split(" ")[ctr];
					
					String commandReadMsg="adb shell cat "+SMSFileName;
					msgText=Control.RunProcessWithOutput(commandReadMsg, 15, true);
					
					
					boolean resultCompare=compareTextSMSBackend(mytext,msgText);
					Generic.printSimilarity(mytext, msgText);
					
					if(resultCompare)
					{
						//if matches here, no other verification needed, simply move the sms, write to excel,return
						Generic.WriteTestData("Checking text message", "% match :"+percentageMatch, "% match :"+percentageMatch, "Expected Text: "+mytext, "Expected text "+mytext+"\nis  same as \nActual : "+msgText, "Pass");
						String[] info=SMSFileName.split("_");
						String incomingNumber=info[1];
						String timestamp=info[3]+"_"+info[4];
						Control.openMessageAndTakeScreenshot("SMS from : "+incomingNumber+"\nTimestamp : "+timestamp+"\n\nContent : "+msgText);
						
						String command="adb shell mv "+SMSFileName+" /storage/emulated/0/smsbackup*/";
						Control.RunProcessWithOutput(command, 15, false);
						--Constant.numberOfExpectedSms;
						return;
					}
					
					if(Float.compare(Constant.lastSmsPercentMatch,percentageMatch)>=0)
					{
						percentageMatch=Constant.lastSmsPercentMatch;
						matchedFileName=SMSFileName;
					}

				}
				
				
				String[] info=matchedFileName.split("_");
				String incomingNumber=info[1];
				String timestamp=info[3]+"_"+info[4];
				
				if(percentageMatch>Constant.smsPercentageMatch)
				{
					Generic.WriteTestData("Checking text message", "% match :"+percentageMatch, "% match :"+percentageMatch, "Expected Text: "+mytext, "Expected text "+mytext+"\nis similar to \nActual : "+msgText, "Pass");
					
					Control.openMessageAndTakeScreenshot("SMS from : "+incomingNumber+"\nTimestamp : "+timestamp+"\n\nContent : "+msgText);
					
					String command="adb shell mv "+matchedFileName+" /storage/emulated/0/smsbackup*/";
					Control.RunProcessWithOutput(command, 15, false);
					--Constant.numberOfExpectedSms;
				}
				else
				{

					Generic.WriteTestData("Checking text message", "% match :"+percentageMatch, "% match :"+percentageMatch, "Expected Text: "+mytext, "Expected text "+mytext+"\nis NOT similar to \nActual : "+msgText, "Fail");
					Control.openMessageAndTakeScreenshot("SMS from : "+incomingNumber+"\nTimestamp : "+timestamp+"\n\nContent : "+msgText);
					
					
				}
				
			}
			
			
		}
		
		
		else
		{
			//No msg was received
			Date dateForSMS=new Date();
			Generic.WriteTestData("From Allowed Numbers, no message was received", "Allowed Numbers : "+Constant.senderList, "Timeout seconds: "+Constant.smsWaitingTime, "Msg should be received from Sender List : "+Constant.senderList, "No msg was received was allowed number list : "+Constant.senderList+" until "+dateForSMS, "Fail");
			return;
		}
		

	
				
	
	}
		
	
	public static void replyOnLastMessage(String keywordToSend,String mytext) throws Exception {

		//if mytext is null, the function simply replies on top of last message with keyword to send
		//if mytext is not null, then the function matches the incoming message, then replies on that
	
		
		boolean compareAndReplyFlag=false;
		
		if(mytext!=null)
			compareAndReplyFlag=true;
		
		System.out.println("In function : readFirstMessage(), will sleep for 15 seconds by default");
		Thread.sleep(15000);
		
		//first step is to make sure we have a msg to play with
		//timeout will be fetched from constant, check until then if a msg is there

		
		String commandForLastSMSFile="adb shell ls -rt /storage/emulated/0/sms";
		  
		String SMSFileName="";
		System.out.println("Checking is SMS was received..");
		String SMSFileNames=Control.RunProcessWithOutput(commandForLastSMSFile, 15, true);
		boolean flagNoSMS=false;
		boolean flagOneSMS=false;
		boolean flagMultipleSMS=false;
		System.out.println("O/P of shell command : "+SMSFileNames);
		int timer=-1;
		
		while(++timer<Constant.smsWaitingTime)
		{
			System.out.println("Waiting for SMS - try#: "+timer+"/"+Constant.smsWaitingTime);
			SMSFileNames=Control.RunProcessWithOutput(commandForLastSMSFile, 15, true);
			
			if(SMSFileNames==null)
				SMSFileNames="";
			
			SMSFileNames=SMSFileNames.trim();
			
			
			
			  if(SMSFileNames.contains(" "))
			  {
				  	flagMultipleSMS=true;
				  	System.out.println("Multiple SMS detected : "+SMSFileNames);
				  	break;
			  }
			  
			  else
			  {
				  //may be 1 file or no file
				  
				  if(SMSFileNames.equals(""))
				  {
					  //no files
					  System.out.println("No SMS detected yet");
					  flagNoSMS=true;
				  }
				  

				  if(!SMSFileNames.equals(""))
				  {
					  	  flagNoSMS=false;
						  SMSFileName=SMSFileNames;
						  System.out.println("One SMS Detected : "+SMSFileName);
						  flagOneSMS=true;
						  break;
				  }
				 
			  }
			  
			Thread.sleep(1000);
			
			
			
			  
		}//while ends
		
		if(flagNoSMS)
		{
			//No msg was received
			Date dateForSMS=new Date();
			Generic.WriteTestData("Checking for new message", "Allowed numbers : "+Constant.senderList, "Timeout seconds: "+Constant.smsWaitingTime, "Msg should be received", "No msg was received until "+dateForSMS, "Fail");
			return;
		}
		  
		else if(!compareAndReplyFlag)
		{
			//need not compare the message text before replying, simple reply on top of last message
			String commandReadMsg="adb shell \"ls -rt /storage/emulated/0/sms | tail -1\"";
			String latestMsg=Control.RunProcessWithOutput(commandReadMsg, 15, true);
			
			//now we have the latest message, get the B Number
			try
			{
				String myBNumber=latestMsg.split("_")[1];
				Generic.sendSMS(keywordToSend, myBNumber);
				
				
				//pass
				return;
			}
			catch(Exception e)
			{
				//fail
				Generic.WriteTestData("Not able Reply on top of message with Keyword", ""+latestMsg, ""+latestMsg, "Should be able to reply on top of incoming msg", "Unable to reply on top of incoming msg : "+latestMsg, "Fail");
				
				return;
			}
			
		}
		else
		{
			//scenario where input text needs to be compared before replying
		
			
			if(flagMultipleSMS || flagOneSMS)
			{
				//if there is only one message, compare text and publish results
				
				System.out.println("One or more messages have been detected");
				if(flagOneSMS)
				{
					String commandReadMsg="adb shell cat "+SMSFileName;
					String msgText=Control.RunProcessWithOutput(commandReadMsg, 15, true);
					boolean resultCompare=compareTextSMSBackend(mytext,msgText);
					
					//extract the number from which the text was received
	
	
					
					if(resultCompare)
					{
						
						Generic.WriteTestData("Checking text message", "", "", "Expected Text: "+mytext, "Expected text "+mytext+"\nis  same as \nActual : "+msgText, "Pass");
						
						String command="adb shell mv "+SMSFileName+" /storage/emulated/0/smsbackup*/";
						Control.RunProcessWithOutput(command, 15, false);
						
						
						try
						{
							String BNumber=SMSFileName.split("_")[1];
							Generic.sendSMS(keywordToSend, BNumber);
							return;
						}
						catch(Exception e)
						{
							//failure while parsing the number to send message to
							Generic.WriteTestData("Reply on top of message with Keyword", ""+SMSFileName, ""+SMSFileName, "Should be able to parse incoming number", "Unable to parse incoming number : "+SMSFileName, "Fail");
							
							return;
						}
						
						
					}
					else
					{
						Generic.WriteTestData("Checking text message", "", "", "Expected Text: "+mytext, "Expected text "+mytext+"\nis not same as \nActual : "+msgText, "Fail");
						
					}
				}
				
				
				//if there are multiple messages, read one msg, compare, if matches, then delete, else move to next message
				
				if(flagMultipleSMS)
				{
					//count the number of messages
					int smsCount=SMSFileNames.split(" ").length;
					int ctr=-1;
					
					//iterate over each message and compare text
					while(++ctr<smsCount)
					{
						SMSFileName=SMSFileNames.split(" ")[ctr];
						String commandReadMsg="adb shell cat "+SMSFileName;
						String msgText=Control.RunProcessWithOutput(commandReadMsg, 15, true);
						boolean resultCompare=compareTextSMSBackend(mytext,msgText);
						if(resultCompare)
						{
							
							
							
							Generic.WriteTestData("Checking text message", "", "", "Expected Text: "+mytext, "Expected text "+mytext+"\nis  same as \nActual : "+msgText, "Pass");
						
							String command="adb shell mv "+SMSFileName+" /storage/emulated/0/smsbackup*/";
							Control.RunProcessWithOutput(command, 15, false);
							
							
							try
							{
								String BNumber=SMSFileName.split("_")[1];
								Generic.sendSMS(keywordToSend, BNumber);
								
							}
							catch(Exception e)
							{
								//failure while parsing the number to send message to
								Generic.WriteTestData("Reply on top of message with Keyword", ""+SMSFileName, ""+SMSFileName, "Should be able to parse incoming number", "Unable to parse incoming number : "+SMSFileName, "Fail");
								
								return;
							}
							
							break;
						}
						else
						{
							Generic.WriteTestData("Checking text message", "", "", "Expected Text: "+mytext, "Expected text "+mytext+"\nis not same as \nActual : "+msgText, "Fail");
							
						}
					}
					
				}
				
	
				
				
				
			}
			
		}

	
				
	}
		

	public static void GeneratePDFReport() throws Exception
	{
		String excel_path=Constant.ResultFilePath.substring(0,Constant.ResultFilePath.lastIndexOf("\\"))+"\\";
		Constant.path_to_python_scripts = System.getProperty("user.dir")+File.separator+Generic.ReadFromExcel("Python27","AI_TestData",1);		
		System.out.println("Path to Result : "+excel_path);
        JavaReport jr=new JavaReport();
        jr.GenerateReport(Constant.path_to_python_scripts, Constant.ResultFilePath, excel_path, Constant.ResultFilePath, "Report_Test_Summary"+".pdf");
        //jr.GenerateReport(Constant.path_to_python_scripts, Constant.ResultFilePath, excel_path, Constant.ResultFilePath, "Report_Test_Summary"+".pdf");
		JavaReport.GenerateJunitReport(Constant.ResultFilePath);
	}
	
	
	
	public static boolean closeFlashMsg() throws Exception
	{
		//System.out.println("Closing Flash Message explicitly...");
		try
		{
			if(Control.findElement("SamsungMessage","Cancel")!=null)
				{
					Control.click("SamsungMessage","Cancel");
					return true;
				}
	
			else if(Control.findElement("SamsungMessage","ok")!=null)
				{
					Control.click("SamsungMessage","ok");
					return true;
				}
			
			return false;
			
		}
		catch(Exception e)
		{
			System.out.println("Exception encountered while closing Flash : "+e.getMessage());
			return false;
		}
	
	}
	
  public static String GetSMSFileTimestampOld() throws Exception
	    {
	  
	  /**
	   * 
	   * ***********************************************************************
	   * Returns the timestamp of the last msg received
	   * ***********************************************************************
	   * 
	   */
	  				System.out.println("Function : GetSMSFileTimestamp()");
	  				//Thread.sleep(2000);
	  				String line="",line1="";
	  				String command="stat /storage/emulated/0/ReceivedSMS.txt | grep Change";
	                ProcessBuilder builder = new ProcessBuilder(
	                "cmd.exe", "/c", "adb shell \""+command+"\"");
	                Process p = builder.start();
	                p.waitFor(10,TimeUnit.SECONDS);
	                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
	                //System.out.println("Buffered Reader : "+r.readLine());

	                while ((line1=r.readLine())!=null) 
	                {
	                
	                	try
	                	{
	                		line+=line1;
	                	}
	                    catch(Exception e)
	                    {
	                        System.out.println("Exception while reading SMS from Backend "+e.getMessage());
	                        e.printStackTrace();
	                        break;
	                    }
	                    
	                }
	                
	                //line=line.replace("<ENDMSG>", "");
	                System.out.println("Line is : "+line);
	                return line;
	            }
  
  
  public static String GetLastSMSFile() throws Exception
  {

/**
 * 
 * ***********************************************************************
 * 			Returns the timestamp of the last msg received
 * ***********************************************************************
 * 
 */
				System.out.println("Function : GetSMSFileTimestamp()");
				//Thread.sleep(2000);
				String line="",line1="";
				
				//returns the file names delimited by a space
				//should return null/blank if no file
				String command="adb shell \'ls -rt /storage/emulated/0/sms | cut -d\' \' -f1 | tail -1\'";
				System.out.println("Command for fetching last file : "+command);
				String lastFileName=Control.RunProcessWithOutput(command, 15, true);
				
				if(lastFileName==null)
					lastFileName="";
				
				
				try
				{
					lastFileName=lastFileName.trim();
				}
				catch(Exception e)
				{
					System.out.println("Exception in GetLastSMSFile() while getting last file name : "+e.getMessage());
					e.printStackTrace();
				}
				
				if(lastFileName.equals(""))
				{
					return Constant.lastSMSTime;
				}
				
			command="stat /storage/emulated/0/"+lastFileName+"| grep Change";
				
			line=Control.RunProcessWithOutput(command, 15, true);
            
              return line;
          }

  
  
  
  
  public static void deletePreviousTextMessages()
  {
	  
	  
	  String commandForDeletingAllMessages="adb shell mv /storage/emulated/0/sms/* /storage/emulated/0/smsbackup*/";
	  System.out.println("Deleting prev messages using command : \n"+commandForDeletingAllMessages);
	  Control.RunProcessWithOutput(commandForDeletingAllMessages, 15, false);
	  
	  
	  String commandListFiles="adb shell ls /storage/emulated/0/sms/* /storage/emulated/0/sms";
	  System.out.println("Checking if all messages have been deleted ");
	  String files=Control.RunProcessWithOutput(commandListFiles, 15, true);
	  System.out.println("Files : "+files);
	  try
	  {
		  files=files.trim();
		  if(files.contains("No such file or directory") || files.equals(""))
		  {
			  System.out.println("All SMS have been deleted!");
		  }
		  else
		  {
			  System.out.println("SMS were not deleted!");
		  }
	  }
	  catch(Exception e)
	  {
		  System.out.println("Exception : All SMS have been deleted!");
		  return;
	  }
	  
	  
  }
  
  public static String readLastSMSFromBackend() throws Exception
  {
	  System.out.println("Triggered function : readLastSMSFromBackend()" );
	  Thread.sleep(3000);
	  String line="";
	  String line1="";
	  String SMSFileName="";
	  //will have to get last sms file name first
	  String commandForLastSMSFile="adb shell ls -rt /storage/emulated/0/";
	  
	  //above command may return multiple files, need to extract the first one
	  
	  String SMSFileNames=Control.RunProcessWithOutput(commandForLastSMSFile, 15, true);
	  
	  if(SMSFileNames.contains(" "))
	  {
		  //more than one file
		  SMSFileName=SMSFileNames.split(" ")[0];
	  }
	  else
	  {
		  //may be 1 file or no file
		  boolean flagNoSMS=false;
		  try
		  {
			  if(SMSFileNames==null || SMSFileNames.trim().equals(""))
			  {
				  //no files
				  flagNoSMS=true;
			  }
		  }
		  catch(Exception e)
		  {
			  //no files
			  flagNoSMS=true;
		  }
		  
		  if(SMSFileNames==null)
			  SMSFileNames="";
		  
		  if(!SMSFileNames.trim().equals(""))
		  {
			  //this means one SMS
			  flagNoSMS=false;
			  SMSFileName=SMSFileNames.trim();
			  
		  }
		 
	  }
	  
	  //holding this value may be to delete later if needed
	  Constant.lastReadSMSFileName=SMSFileName;
	  
      ProcessBuilder builder = new ProcessBuilder(
      "cmd.exe", "/c", "adb shell cat /storage/emulated/0/"+SMSFileName);
      Process p = builder.start();
      p.waitFor(10,TimeUnit.SECONDS);
      BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
      

      if (true) 
      {
          try
          {

        	  while((line1=r.readLine())!=null)
        	  {
        		  line+=line1;
        	  }
          }

          catch(Exception e)
          {
              System.out.println("Exception while reading SMS from Backend "+e.getMessage());
              e.printStackTrace();
              //break;
          }
          
      }
      
      //line=line.replace("<ENDMSG>\n", "");
      System.out.println("Incoming Message (read using backend code) : "+line);
      return line;
  }
	
  
  public static int determineNumberOfSms(int RowNo,int Col_input)
  {
		String InputValue="",tempCheckPoint="";
	
		
		InputValue=Constant.Map2.get("TestCase"+ RowNo).get("Input_"+Col_input);
		tempCheckPoint=Constant.Map2.get("TestCase"+ RowNo).get("CheckPoint_"+Col_input);
		
		//need to iterate over all checkpoint until input is blank
		
		if(InputValue==null)
			InputValue="";
		
		InputValue=InputValue.trim();
		
		int columnCounter=Col_input;
		int numberOfSMS=1;
		while(++columnCounter<Constant.maxNumberOfColumns)
		{
			InputValue=Constant.Map2.get("TestCase"+ RowNo).get("Input_"+columnCounter);
			tempCheckPoint=Constant.Map2.get("TestCase"+ RowNo).get("CheckPoint_"+columnCounter);
			
			if(InputValue==null)
				InputValue="";
			if(tempCheckPoint==null)
				tempCheckPoint="";
			
			
			System.out.println("Input val :"+InputValue.trim());
			System.out.println("tempCheckPoint :"+tempCheckPoint.trim());
			
			if(InputValue.trim().equals("") && tempCheckPoint.trim().contains("RECEIVE_TEXTMSG_"))
			{
				++numberOfSMS;
				continue;
			}
			break;
			
			
		}
		return numberOfSMS;


  }
  
  public static void DoForEveryTestCase() throws Exception
  {
	  	Constant.alreadyWaited=false;
		Constant.lastSmsOrUssdTime=System.currentTimeMillis();
		deletePreviousTextMessages();
		closeFlashMsg();
  }
	
  public static void DriverScriptForNFExecution(int FromTestCaseNo,int ToTestCaseNo) throws Exception {    	
		String InputValue=null;
		String CheckValue=null; 
		String InputValue2 = null;
		String keywor= null;
		int flagCheckType=0;
		float balance1=0.0f;
		float balance2=0.0f;
		float callStatus=0;
		float freeText1=0,freeText2=0;
		

		for(int RowNo=FromTestCaseNo; RowNo<=ToTestCaseNo;RowNo++) {
			try
			{
				DoForEveryTestCase();
				System.out.println("************************** [ TestCase #:"+ RowNo+"] **************************");
				Generic.WriteTestCase(Constant.Map2.get("TestCase"+ RowNo).get("TestCaseNo"), Constant.Map2.get("TestCase"+ RowNo).get("TestCaseName"), Constant.Map2.get("TestCase"+ RowNo).get("Expected_result"), Constant.Map2.get("TestCase"+ RowNo).get("Actual Result"));
				
				for(int Col_input= 1; Col_input<=20; Col_input++) 
				{
					Constant.comparisonType=2;	
	/*
	 * *****************************************************************************************************
						[ Input & Pre-CheckPoint Analysis  code for each Test Step]
	 * ******************************************************************************************************
	 */
					
					System.out.println("TestCase #:"+ RowNo+"\nTestStep #:"+Col_input);
					
						InputValue=Constant.Map2.get("TestCase"+ RowNo).get("Input_"+Col_input);
						keywor=InputValue;
						System.out.println("Input_"+Col_input+":"+InputValue);
						String tempCheckPoint=Constant.Map2.get("TestCase"+ RowNo).get("CheckPoint_"+Col_input);
						System.out.println("CheckPoint_"+Col_input+":"+tempCheckPoint);
	
						
				//break if input & checkpoint both are null
						if((InputValue == null || InputValue.equals("")) && (tempCheckPoint==null || tempCheckPoint.equals(""))) 
						{
							System.out.println("Breaking as Input_"+Col_input+" and Checkpoint_"+Col_input+" both are null");
							break;
						}
						
						
						if((InputValue == null))
						{
									InputValue="";
						}
						
						if((tempCheckPoint==null))
						{
							tempCheckPoint="";
						}
						
						if(!InputValue.trim().equals(""))
						{
							Constant.alreadyWaited=false;
						}
						
				//pre-check of Balance Verification CheckPoint to store balance
						if(tempCheckPoint.toLowerCase().contains("verify_balance_"))
						{
							System.out.println("Handling condition for : STORE_BALANCE");
									//store balance
									System.out.println("Storing Balance..");
									balance1=Float.parseFloat(checkbal("*143*2*2*1#"));
									freeText1=Constant.numberOfFreetexts;
									System.out.println("Stored Balance : "+balance1);
									
									closeFlashMsg();
						}
						
						
		/*
		 * *************************************************************************************
	 									YOUTUBE USAGE
		 * *************************************************************************************
		 */	
						
				//Trigger for Youtube Usage for certain duration
						if(InputValue.trim().toLowerCase().contains(("youtube_")))
						{
							closeFlashMsg();
							System.out.println("Handling condition for : YOUTUBE");
							flagCheckType=-1;
							System.out.println("Input value is : "+InputValue);
							
							//first parse the duration
							int surfduration=Integer.parseInt((InputValue.trim().split("_"))[1]);
							
							Generic.turnOnWifi(false);
							Generic.turnOnMobileData(true);
							
							if(Generic.isInternetConnected())
							{
								System.out.println("Internet is connected, will run youtube");
								long resYoutube=Generic.videoYoutube(surfduration);
								if(resYoutube==1)
								{
									Generic.WriteTestData("Youtube : Ran Successfully", "Duration", ""+surfduration, "Video shall run for "+surfduration+" seconds", "Video ran for "+surfduration+" seconds", "Pass");
									
								}
								else if(resYoutube==0.5)
								{
									Generic.WriteTestData("Youtube : Ran Successfully for partial duration", "Duration", ""+surfduration, "Video shall run for "+surfduration+" seconds", "Video ran for "+surfduration+" seconds", "Pass");
									
								}
								else
								{
									Generic.WriteTestData("Youtube : Was not triggered as Internet is not connected", "", "InternetConnectivity", "Internet should be accessible", "Internet is not accessible", "Fail");
									
								}
							}
							else
							{
								System.out.println("Can't run youtube as internet is NOT Connected");
								Generic.WriteTestData("Youtube : Was not triggered as Internet is not connected", "", "InternetConnectivity", "Internet should be accessible", "Internet is not accessible", "Fail");
							
							}
							
							System.out.println("Turning OFF Mobile Data");
							Generic.turnOnMobileData(false);
							
							
							
						}
						
						
						
						
						
						
		
		/*
		 * *************************************************************************************
	 							VOICE CALL TO A NUMBER
		 * *************************************************************************************
		 */	
						
												
				//Trigger for CALL Usage for certain duration
				//minimum duration will always be 30 seconds
						else if(InputValue.trim().contains(("CALL_")) && (InputValue.trim().split("_")).length==3)
						{
							closeFlashMsg();
							System.out.println("Handling condition for : CALL");
							//CALL_<NUMBER>_<MM:SS>
							flagCheckType=-1;
							String calledNumber=keywor.trim().split("_")[1];
							String durationMMSS=keywor.trim().split("_")[2];
							
							System.out.println("Making a Voice Call");
							
							
							int min=Integer.parseInt(durationMMSS.split(":")[0]);
							int sec=Integer.parseInt(durationMMSS.split(":")[1]);
							
							int durationOfCall=min*60+sec;
							
							if(durationOfCall<30)
							{
								durationOfCall=30;
							}
							
							callStatus=Generic.CallToBNumber(calledNumber, durationOfCall);
							
							System.out.println("call status is : "+callStatus);
							
							//if 0 is returned the entire function failed
							//if 0.5 is returned, a call was attempted from source device, but not picked up by target device
							//if 0.75 is returned, the call got connected, but was dropped before specified duration
							//if 1 is returned the function passed, a call was attempted and answered by target device
							
							
							if(callStatus==0)
							{
								
								Generic.WriteTestData("Call was not attempted", "CallToBNumber", "callStatus=0", "Call should be attempted", "Call was not attempted", "Fail");
							}
							
							else if(callStatus==0.5)
							{
								
								Generic.WriteTestData("Call was attempted, but not answered by target device", "CallToBNumber", "callStatus=0.5", "Call should be answered by target device", "Call was not answered by target device", "Fail");
							}
							
							else if(callStatus==0.75)
							{
								
								Generic.WriteTestData("Call Unsuccessful", "CallToBNumber", "callStatus=0.75", "Call should continue for the duration specified", "Call continued less than the target duration", "Fail");
							}
							else
							{
								
								Generic.WriteTestData("Call was successful", "CallToBNumber", "callStatus=1", "Call Successful", "Call Successful", "Pass");
							}
							
							
						}
						
			/*
			 * *************************************************************************************
		 							SEND SMS TO A NUMBER
			 * *************************************************************************************
			 */		
						else if(InputValue.trim().toUpperCase().contains("SMS") && (keywor.trim().split("_")).length==3)
						{
							//SMS_GOSURF15_8080
							
							closeFlashMsg();
							System.out.println("Handling condition for : SMS");
							flagCheckType=1;

							String msgContent=keywor.trim().split("_")[1];
							String BNumber=keywor.trim().split("_")[2];
							
							
							System.out.println("Msg Content : "+msgContent);
							System.out.println("B Number : "+BNumber);
							Generic.sendSMS(msgContent, BNumber);
							
							
						}
						
		
		/*
		 * *************************************************************************************
	 							REPLY ON TOP OF LAST MESSAGE
		 * *************************************************************************************
		 */						

						else if(InputValue.contains("REPLY_"))
						{
							//REPLY_<Keyword>_<Msg to verify>
							//REPLY_<Keyword>_<Msg to verify>_<SENDER>
							
							closeFlashMsg();
							int numberOfArgs=InputValue.split("_").length;
							
							if(numberOfArgs==3)
							{
								String keywordToSend=InputValue.split("_")[1];
								String msgToVerify=InputValue.split("_")[2];
								String SmsToReplyOn=verifyMessageContents(msgToVerify, "dummy");
								//replyOnLastMessage(keywordToSend, msgToVerify);
								Constant.lastSmsOrUssdTime=System.currentTimeMillis();
								if(SmsToReplyOn==null)
								{
									SmsToReplyOn="";
								}
								
								if(SmsToReplyOn.trim().equals(""))
								{
									//cant reply
								}
								else
								{
									String BNumber=SmsToReplyOn.split("_")[1];
									Generic.sendSMS(keywordToSend, BNumber);
								}
							}
							
							else if(numberOfArgs==4)
							{
								//
								String keywordToSend=InputValue.split("_")[1];
								String msgToVerify=InputValue.split("_")[2];
								String msgFrom=InputValue.split("_")[4];
								String SmsToReplyOn=verifyMessageContentsFromOneSpecificSender(msgToVerify, msgFrom);
								//replyOnLastMessage(keywordToSend, msgToVerify);
								Constant.lastSmsOrUssdTime=System.currentTimeMillis();
								if(SmsToReplyOn==null)
								{
									SmsToReplyOn="";
								}
								
								if(SmsToReplyOn.trim().equals(""))
								{
									//cant reply
								}
								else
								{
									String BNumber=SmsToReplyOn.split("_")[1];
									Generic.sendSMS(keywordToSend, BNumber);
								}
							}
							else
							{
								//write to excel that input format is incorrect
								Generic.WriteTestData("Incorrect format of Test Data", "Actual:"+InputValue, "Expected:REPLY_<Keyword>_<Msg to verify>" , "Expected format : REPLY_<Keyword>_<Msg to verify>\nFound in Excel : "+InputValue, "Expected format : REPLY_<Keyword>_<Msg to verify>\nFound in Excel : "+InputValue, "Fail");
							}
							
							
						}

			//Added condition to prevent null USSD values
						else if(InputValue==null || InputValue.equals(""))
						{
							System.out.println("Input Cell is NULL");
						}
						
						
						
		/*
		 * *************************************************************************************
	 									WEB-Browser	
		 * *************************************************************************************
		 */
												
						
						else if(InputValue.startsWith("WEB_"))
						{
							
							//Control.OpenDesktopBrowserWithUrl("Chrome", "https://www.google.com");
							Control.LaunchMobileApp("", Generic.ReadFromExcel("DeviceDetails","AI_TestData",1),Generic.ReadFromExcel("DeviceDetails","AI_TestData",2), "Android", Constant.textViewerPackageName, Constant.textViewerActivityName, Constant.ipToListen, "4723","");
							
						}
						
						
						
						
						
						
						
						
						
		/*
		 * *************************************************************************************
	 							Default : USSD	
		 * *************************************************************************************
		 */
						
		   //By default the input is USSD
						else
						{
							//USSD Part - if nothing else matches
							Constant.lastSmsOrUssdTime=System.currentTimeMillis();
							System.out.println("Handling condition for : USSD");
							
							flagCheckType=0;
							
							if(!(Generic.FindWithTimeOut("DialerApp","InputOption", 2000)))
							{
								closeFlashMsg();
								
								String USSDCode=InputValue.trim().replace("#", "%23");
								
								String dialUssdCommand="adb shell am start -a android.intent.action.DIAL -d tel:\""+USSDCode+"\"";
								Control.RunProcessWithOutput(dialUssdCommand, 10, false);
								
								Thread.sleep(1000);
								
								Generic.WriteTestData("Entering B Number", "", "", "Should be able to enter", "Should be able to enter", "Pass");
								Control.takeScreenshot();

								
								String makeVoiceCall="adb shell am start -a android.intent.action.CALL -d tel:\""+USSDCode+"\"";
						  		 
								Control.RunProcessWithOutput(makeVoiceCall, 10, false);
								
							}
							else
							{
									enterText("DialerApp","InputOption",InputValue.trim());
									takeScreenshot();
									//might need to check the text here, depends on test results
									click("DialerApp","Send");
									takeScreenshot();
								   
							}
							
						}
						
						
						
	/*
	 * **********************************************************************************************
	 * 							[ Actual Checkpoint code ]
	 * **********************************************************************************************
	 */
						
			//Fetch CheckPoint Scenario
						
						System.out.println("Mapping Checkpoint..");
						
						CheckValue=Constant.Map2.get("TestCase"+ RowNo).get("CheckPoint_"+Col_input);

						System.out.println("CheckPoint is : "+CheckValue);
						
						if(CheckValue=="" || CheckValue==null)
						{
							//Set to not null to avoid exception during trim()
							CheckValue="";
							
						}
						
		/*
		 * ************************************************************************************************
	 							Handle for Incoming SMS	
		 * ************************************************************************************************
		 */
						
			//
			//we only need to consider SMS after timestamp - Constant.lastSmsOrUssdTime
						else if(CheckValue.trim().toLowerCase().contains(("receive_text")))
						{
							
							closeFlashMsg();
							
							//Sample input 1: RECEIVE_TEXTMSG_<ACTUAL MSG>
							//Sample input 1: RECEIVE_TEXTMSG_<ACTUAL MSG>_8080
							
							Constant.comparisonType=2;
							
							System.out.println("Handling condition for : RECEIVE TEXTMSG");
							String TextMsg=CheckValue.split("_")[2].trim();

							
							Constant.numberOfExpectedSms=determineNumberOfSms(RowNo,Col_input);
							
							if(CheckValue.split("_").length==4)
								ReadMessageFromOneSpecificSender(TextMsg,CheckValue.split("_")[3]);

							ReadFirstMessageForSpecificSenders(TextMsg, "dummy_arg_IncomingNumber");
															
						}
						
		/*
		 * ***********************************************************************************************
		 * 					Handle  Verification for Balance Deduction	
		 * ***********************************************************************************************
		 */
						
						else if(CheckValue.trim().toUpperCase().contains("VERIFY_BALANCE_"))
						{
							//VERIFY_FREETEXT_DECUCTION_5
							//VERIFY_FREETEXT_DECUCTION_0
							//VERIFY_FREETEXT_DECUCTION_ANY
							
							closeFlashMsg();
							System.out.println("Handling condition for : Balance Verification");
							
							balance2=Float.parseFloat(checkbal("*143*2*2*1#"));
							freeText2=Constant.numberOfFreetexts;
							
							
							if(CheckValue.trim().split("_")[3].trim().equals("ANY"))
							{
								float deductionAmountActual=Float.compare(balance1, balance2);
								if(deductionAmountActual>0 || deductionAmountActual<0)
								{
									Generic.WriteTestData("Checking Balance Deduction : ", "Initial : "+balance1, "Final:"+balance2, "There should be some deduction in balance","There was some deduction in balance","Pass");
									
								}
								else
								{
									Generic.WriteTestData("Checking Balance Deduction", "Initial : "+balance1, "Final:"+balance2, "There should be some deduction in balance","There was NO deduction in balance","Fail");
									
								}
							}
							else
							{
								float deductionAmountExpected=Float.parseFloat(CheckValue.trim().split("_")[3]);
								float deductionAmountActual=balance1-balance2;
								
								
								System.out.println("deductionAmountActual:"+deductionAmountActual);
								System.out.println("deductionAmountExpected:"+deductionAmountExpected);
								
								if(Float.compare(deductionAmountExpected, deductionAmountActual)==0)
								{
									Generic.WriteTestData("Checking Balance Deduction", "", "", "Actual Deducation amount "+deductionAmountActual+" should be same as Expected Deduction Amount "+deductionAmountExpected, "Actual Deducation amount "+deductionAmountActual+" is same as Expected Deduction Amount "+deductionAmountExpected, "Pass");
								
								}
								else
								{
									Generic.WriteTestData("Checking Balance Deduction", "", "", "Actual Deducation amount "+deductionAmountActual+" should be same as Expected Deduction Amount "+deductionAmountExpected, "Actual Deducation amount "+deductionAmountActual+" is NOT same as Expected Deduction Amount "+deductionAmountExpected, "Fail");
									
								}
								
								
								if(Float.compare(freeText2, freeText1)==0)
								{
									Generic.WriteTestData("No Free Text deduction", "Inital Free texts : "+freeText1, "Final Free texts"+freeText2, "Initial Freetext is same as Final FreeText : "+freeText1, "Initial Freetext is same as Final FreeText : "+freeText1, "Pass");
								}
								else
								{
									Generic.WriteTestData("Free Text deduction", "Initial Free texts : "+freeText1, "Final Free texts : "+freeText2, "Initial Freetext :"+freeText1+" is not same as Final FreeText : "+freeText2, "Initial Freetext :"+freeText1+" is not same as Final FreeText : "+freeText2, "Pass");
								}

							}
														
							closeFlashMsg();
						}
						
		/*
		 * ***********************************************************************************************
		 * 					Handle  Verification for FreeText Deduction	
		 * ***********************************************************************************************
		 */
						else if(CheckValue.trim().toUpperCase().contains("VERIFY_FREETEXT_"))
						{
							//VERIFY_FREETEXT_DECUCTION_5
							//VERIFY_FREETEXT_DECUCTION_0
							//VERIFY_FREETEXT_DECUCTION_ANY
							
							closeFlashMsg();
							System.out.println("Handling condition for : FREETEXT DEDUCTION");
							
							balance2=Float.parseFloat(checkbal("*143*2*2*1#"));
							freeText2=Constant.numberOfFreetexts;
							
							
							if(CheckValue.trim().split("_")[3].trim().equals("ANY"))
							{
								float deductionAmountActual=Float.compare(freeText1, freeText2);
								if(deductionAmountActual>0 || deductionAmountActual<0)
								{
									Generic.WriteTestData("Checking FreeText Deduction : ", "Initial : "+balance1, "Final:"+balance2, "There should be some deduction in balance","There was some deduction in balance","Pass");
									
								}
								else
								{
									Generic.WriteTestData("Checking FreeText Deduction", "Initial : "+balance1, "Final:"+balance2, "There should be some deduction in balance","There was NO deduction in balance","Fail");
									
								}
							}
							else
							{
								float deductionAmountExpected=Float.parseFloat(CheckValue.trim().split("_")[3]);
								float deductionAmountActual=freeText1-freeText2;
								
								
								System.out.println("deductionAmountActual:"+deductionAmountActual);
								System.out.println("deductionAmountExpected:"+deductionAmountExpected);
								
								if(Float.compare(deductionAmountExpected, deductionAmountActual)==0)
								{
									Generic.WriteTestData("Checking FreeText Deduction", "", "", "Actual Deducation amount "+deductionAmountActual+" should be same as Expected Deduction Amount "+deductionAmountExpected, "Actual Deducation amount "+deductionAmountActual+" is same as Expected Deduction Amount "+deductionAmountExpected, "Pass");
								
								}
								else
								{
									Generic.WriteTestData("Checking FreeText Deduction", "", "", "Actual Deducation amount "+deductionAmountActual+" should be same as Expected Deduction Amount "+deductionAmountExpected, "Actual Deducation amount "+deductionAmountActual+" is NOT same as Expected Deduction Amount "+deductionAmountExpected, "Fail");
									
								}
								
								
								if(Float.compare(freeText1, freeText2)==0)
								{
									Generic.WriteTestData("No FreeText deduction", "Inital Free texts : "+freeText1, "Final Free texts"+freeText2, "Initial Freetext is same as Final FreeText : "+freeText1, "Initial Freetext is same as Final FreeText : "+freeText1, "Pass");
								}
								else
								{
									Generic.WriteTestData("FreeText deduction", "Initial Free texts : "+freeText1, "Final Free texts : "+freeText2, "Initial Freetext :"+freeText1+" is not same as Final FreeText : "+freeText2, "Initial Freetext :"+freeText1+" is not same as Final FreeText : "+freeText2, "Pass");
								}

							}
														
							closeFlashMsg();
						}

							
						else if(flagCheckType==0)
						{
						//verify USSD
							System.out.println("Handling condition for : USSD Default");
						
							Thread.sleep(1500);
							WebElement element2 = findElement("SamsungMessage","ok");
							
							//means if Ok button is not present
							if(element2 != null && !(element2.getText().toLowerCase().equals("ok")))
							{
								compareText("SamsungMessage","txtOutputMessage",CheckValue);
							}
							else 
							{
								compareText("SamsungMessage","BeforeOk",CheckValue);
							}
									

						}	
						
						else
						{
							System.out.println("No Condition matched! CheckPoint : "+CheckValue.trim());
							
						}
				

									
								
					}//end of test step

					
					
				}
				catch(Exception e)
				{
					System.out.println("Exception encountered in Test Case : "+RowNo+"\nException is : "+e.getMessage() );
					e.printStackTrace();
				}
				
				finally
				{
					Generic.TestScriptEnds();
					
				}
				
		}//End of Test Case
		//After every Test Case if Flash Msg is displayed on the screen, lets close it
		Constant.lastSmsOrUssdTime=System.currentTimeMillis();
		closeFlashMsg();
			

			}
	
	
	public static void SwipeDown(int x1, int y1, int x2, int y2) throws Exception
    {
		String commandSwipe="adb shell input swipe "+x1+" "+y1+" "+x2+" "+y2;
		Control.RunProcessWithOutput(commandSwipe, 15, false);
		
    }


	public static void SwipeUp(int x1, int y1, int x2, int y2) throws Exception
    {
		String commandSwipe="adb shell input swipe "+x1+" "+y1+" "+x2+" "+y2;
		Control.RunProcessWithOutput(commandSwipe, 15, false);
		
    }

	
	public static int verifyIfMenuOpened()
	{
	
		boolean verificationAchieved=false;
		String tapCommand="adb shell input tap 25 75";
		String backCommand="adb shell input keyevent 4";
		
		long t1=System.currentTimeMillis();
		
		while(System.currentTimeMillis()-t1<15000)
		{
			try
			{
				
				if(Generic.FindWithTimeOut("GlobeAtHomeApp", "DisplayName", 3000))
				{
					System.out.println("Menu is Open,will return");
					Generic.WriteTestData("Opening Menu Options", "MenuButton", "", "Should be able to view Menu Options", "Able to view Menu Options", "Pass");
					
					Control.takeScreenshot();
					Thread.sleep(1000);
					return 1;
				}
				else
				{
					System.out.println("Menu not  Open,will re-tap");
					Control.RunProcessWithOutput(tapCommand, 15, false);
				}
			}
			catch(Exception e)
			{
				
			}
	
		}
		
		return -99;
	
		
	}
	
	public static void verifyHomeScreen()
	{
		boolean verificationAchieved=false;
		String tapCommand="adb shell input tap 25 75";
		String backCommand="adb shell input keyevent 4";
		
		long t1=System.currentTimeMillis();
		
		
		while(System.currentTimeMillis()-t1<15000)
		{
			try
			{
				Control.RunProcessWithOutput(tapCommand, 15, false);
				if(Generic.FindWithTimeOut("GlobeAtHomeApp", "DisplayName", 3000))
				{
					Control.RunProcessWithOutput(backCommand, 15, false);
					
					break;	
				}
			}
			catch(Exception e)
			{
				
			}
	
		}
		
		
	}
	public static void tap(String PageName, String locator) throws Exception {
		Constant.PageName=PageName;
		Constant.locator=locator;
		
		try {
			WebElement element = findElement(PageName,locator);
			if (element != null) {
				
				
				int x=element.getLocation().getX();
				int y=element.getLocation().getY();
				String clickCommand="adb shell input tap "+x+" "+y;
				Control.RunProcessWithOutput(clickCommand, 30, false);
				
				//element.click();	
				Generic.WriteTestData("Click on '"+locator+"'",locator,"","Able to click on '"+locator+"' button.","Clicked on '"+locator+"' button.","Pass");
			
			}
			else {
				Generic.WriteTestData("Click on '"+locator+"'",locator,"","Element should be available '"+locator+"' button.","Element was not found  '"+locator+"'","Fail");
			}
			element = null;		
			} catch (Exception e) {
			Generic.WriteTestData("Click on '"+locator+"'",locator,"","Able to click on '"+locator+"' button.","Clicking on '"+locator+"' button is unsuccessful.","Fail");
			System.out.println(" Error occured whlie click on the element "
					+ locator + " *** " + e.getMessage());
			
		}
		
	}
	public static void Simpletap(String PageName, String locator) throws Exception {
		Constant.PageName=PageName;
		Constant.locator=locator;
		
		try {
			WebElement element = findElement(PageName,locator);
			if (element != null) {
				
				int x=element.getLocation().getX();
				int y=element.getLocation().getY();
				Map<String, Object> args = new HashMap<>();
				args.put("x", x);
				args.put("y", y);
				Constant.driver.executeScript("mobile: tap", args);	
				System.out.println("Simple tap on X:"+x+" Y:"+y);
			
			}	
			} catch (Exception e) {	
				System.out.println("Tap failed with coorninates");
		}
		
	}
	public static String[] GetRelativeLocation(String PageName, String locator, float AdditionalPixelsToMove) throws Exception {
		Constant.PageName=PageName;
		Constant.locator=locator;	
		windowSize = Constant.driver.manage().window().getSize();
		try {
			WebElement element = customWait(PageName, locator, 30);		
			if (element != null) {			
				float x=element.getLocation().getX();
				float y=element.getLocation().getY();
				final DecimalFormat DF = new DecimalFormat("0.00");
				float Height = windowSize.height;
				float Width = windowSize.width;
				float x1= x+AdditionalPixelsToMove;
				float y1 =y+AdditionalPixelsToMove;
				float xpercentage = x1/Width*100;
				float ypercnt = y1/Height*100;
				String xper= DF.format(xpercentage)+"%";
				String yper= DF.format(ypercnt)+"%";
				return new String[] {xper, yper};
			}else {
				return new String[] {null, null};
			}
		}catch (Exception e) {
			return new String[] {null, null};
		}
	}
	public static void ClickByRelativeLocation(String PageName, String locator) throws Exception {
		Constant.PageName=PageName;
		Constant.locator=locator;
		windowSize = Constant.driver.manage().window().getSize();
		try {
			WebElement element = customWait(PageName, locator, 240);		
			if (element != null) {			
				float x=element.getLocation().getX();
				float y=element.getLocation().getY();
				final DecimalFormat DF = new DecimalFormat("0.00");
				float Height = windowSize.height;
				float Width = windowSize.width;
				float x1= x+10;
				float y1 =y+10;
				float xpercentage = x1/Width*100;
				float ypercnt = y1/Height*100;
				String xper= DF.format(xpercentage)+"%";
				String yper= DF.format(ypercnt)+"%";			
				Map<String, Object> params = new HashMap<>();
				params.put("location", xper+","+yper);
				Constant.driver.executeScript("mobile:touch:tap", params);
				Generic.WriteTestData("Click on '"+locator+"'",locator,"","Able to click on '"+locator+"' button.","Clicked on '"+locator+"' button.","Pass");	
			
			}else {
				Generic.WriteTestData("Click on '"+locator+"'",locator,"","Element should be available '"+locator+"' button.","Element was not found  '"+locator+"'","Fail");
				throw new Exception();
			}
		} catch (Exception e) {		
			Generic.WriteTestData("Click on '"+locator+"'",locator,"","Element should be available '"+locator+"' button.","Element was not found  '"+locator+"'","Fail");
			e.printStackTrace();
			System.out.println("Tap failed with coorninates");
			throw new Exception("Action to tap bt coordinates faiiels for "+PageName+ "and "+locator);
		}
	}

	public static boolean launchAppUsingCustomAndroidApp(String appToLaunch)
	{
	    int ctr=-1;
	    while(++ctr<3)
	    {
	        try
	        {
			  Control.LaunchMobileAppEasy("com.example.aahmad.launchappbypackage", "com.example.aahmad.launchappbypackage.MainActivity");
			  WebElement launchButton=Generic.GetElementWithTimeOut("LaunchAppByPackage", "LaunchButton", 30000);
			  if(launchButton==null)
			                  continue;
			  Control.enterText("LaunchAppByPackage", "appPackageName", appToLaunch);
			  Control.click("LaunchAppByPackage", "LaunchButton");
			  return true;
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
				System.out.println("Exeption in launchAppUsingCustomAndroidApp while launching : "+appToLaunch);
				continue;
	        }
	    }
	    return false;
	}

	public static void DriverScriptForGlobeAtHomeApp(int FromTestCaseNo,int ToTestCaseNo) throws Exception 
	{    	
		String InputValue=null;
		String CheckValue=null; 
		String InputValue2 = null;
		String keywor= null;
		int flagCheckType=0;
		float balance1=0.0f;
		float balance2=0.0f;
		float callStatus=0;
		float freeText1=0,freeText2=0;
		
	
		for(int RowNo=FromTestCaseNo; RowNo<=ToTestCaseNo;RowNo++) 
		{
						try
						{
				
							System.out.println("************************** [ TestCase #:"+ RowNo+"] **************************");
	Generic.WriteTestCase(Constant.Map2.get("TestCase"+ RowNo).get("TestCaseNo"), Constant.Map2.get("TestCase"+ RowNo).get("TestCaseName"), Constant.Map2.get("TestCase"+ RowNo).get("Expected_result"), Constant.Map2.get("TestCase"+ RowNo).get("Actual Result"));
	
	for(int Col_input= 1; Col_input<=20; Col_input++) 
	{
		
		/*
	 * *****************************************************************************************************
						[ Input & Pre-CheckPoint Analysis  code for each Test Step]
	 * ******************************************************************************************************
	 */
	
	System.out.println("TestCase #:"+ RowNo+"\nTestStep #:"+Col_input);
	
	InputValue=Constant.Map2.get("TestCase"+ RowNo).get("Input_"+Col_input);
	keywor=InputValue;
	System.out.println("Input_"+Col_input+":"+InputValue);
	String tempCheckPoint=Constant.Map2.get("TestCase"+ RowNo).get("CheckPoint_"+Col_input);
	System.out.println("CheckPoint_"+Col_input+":"+tempCheckPoint);
	
	
	//break if input & checkpoint both are null
	if((InputValue == null || InputValue.equals("")) && (tempCheckPoint==null || tempCheckPoint.equals(""))) 
	{
		System.out.println("Breaking as Input_"+Col_input+" and Checkpoint_"+Col_input+" both are null");
		break;
	}
	
	
	if((InputValue == null))
	{
				InputValue="";
	}
	
	if((tempCheckPoint==null))
	{
		tempCheckPoint="";
	}
	
	if(!InputValue.trim().equals(""))
	{
		Constant.alreadyWaited=false;
	}
	
	//Added condition to prevent null USSD values
	else if(InputValue==null || InputValue.equals(""))
	{
		System.out.println("Input Cell is NULL");
	}
	
	
	
	/*
	 * *************************************************************************************
									Actual Keyword Handling Begins here	
	 * *************************************************************************************
	 */
							
	
	if(InputValue.contains("LAUNCHAPP"))
	{
		System.out.println("LaunchApp condition");
	
	if(InputValue.contains("GLOBEATHOME"))
	{
		//cant launch this app just like that - not allowed
		
		boolean flagPinEntered=false;
		
		/*String basicAppPackageName="ph.com.globe.globeathome";
		String basicAppActivityName="ph.com.globe.globeathome.landing.LandingActivity";
		
		Control.LaunchMobileAppEasy(basicAppPackageName, basicAppActivityName);*/
		
		 // String appPackageName=Generic.ReadFromExcel("GlobeAtHomeAppPackageName","ObjectRepository",1);
		 // String appActivityName=Generic.ReadFromExcel("GlobeAtHomeAppActivityName","ObjectRepository",1);
		
		 // Control.launchAppUsingCustomAndroidApp("globeathome");
		  
		  /*String commandToCloseApp="adb shell pm clear com.example.aahmad.launchappbypackage";
		  if(!(Control.RunProcessWithOutput(commandToCloseApp,15,true).contains("success")))
		  {
			  Control.RunProcessWithOutput(commandToCloseApp,15,false);
		  }*/
		
		 // System.out.println("Launching Mobile App : GlobeAtHome");
		
	
		/*  String commandToLaunchGlobeAtHome="adb shell am start -n com.example.aahmad.launchappbypackage/.MainActivity -e packageName "+appPackageName;
		  Control.RunProcessWithOutput(commandToLaunchGlobeAtHome,15,false);
		  */
		  /*WebElement elemButton=null;
		  if((elemButton=Generic.GetElementWithTimeOut("GlobeAtHomeApp", "Button_5", 40000))!=null)
		  {
			  //app launched lock screen found, go on with the flow
			  
			  flagPinEntered=true;
			  
			  Generic.WriteTestData("Launched GlobeAtHome App", "GlobeAtHomeApp", "", "App should be launched","App was launched", "Pass");
			  Control.takeScreenshot();
			  
			  Generic.FindAndClick("GlobeAtHomeApp", "Button_5", 1000);
			  Generic.FindAndClick("GlobeAtHomeApp", "Button_4", 2000);
			  Generic.FindAndClick("GlobeAtHomeApp", "Button_2", 2000);
			  Generic.FindAndClick("GlobeAtHomeApp", "Button_1", 2000);
			  
		  }
		  else
		  {
			  //if already logged in, continue with the flow
			  //Control.takeScreenshot();
			
			  
		  }*/
		  
		  /*System.out.println("Trying to locate the menu button");
		  
		  WebElement myMenuButton=Generic.GetElementWithTimeOut("GlobeAtHomeApp", "MenuButton", 2000);
		  int locationX=25,locationY=75;
		  if(myMenuButton!=null)
		  {
			  try
			  {
				  locationX=myMenuButton.getLocation().getX();
				  locationY=myMenuButton.getLocation().getY();
			  }
			  catch(Exception e)
			  {
				  locationX=25;
				  locationY=75;
			  }
	
		  }
		  else
		  {
			  System.out.println("Recovery Mechanism activated");
			  verifyHomeScreen();
		  }*/
		  //click on home button
		  //Control.click("GlobeAtHomeApp", "HomeButton");
		  WebElement dashboardButton=Generic.GetElementWithTimeOut("GlobeAtHomeApp", "GoToDashboard", 15000);
		  
		  
		  if(dashboardButton!=null)
		  {
			  Generic.WriteTestData("Verifying if Home Page is launched", "HomePage", "", "On Home Page : Should be able to locate Go To Dashboard Button", "Located Go To Dashboard Button on the Home Page", "Pass");
			  Control.takeScreenshot();
		  }
		  else 
		  {
	
			  Generic.WriteTestData("Verifying if Home Page is launched", "HomePage", "", "On Home Page : Should be able to locate Go To Dashboard Button", "Unable to locate Go To Dashboard Button on the Home Page", "Fail");
			  String commandGotoHome="adb shell input keyevent 3";
			  Control.RunProcessWithOutput(commandGotoHome, 15, false);
			  System.exit(-1);
		  }
		  
	
		  
		  
		  
		//  System.out.println("Menu button is : "+myMenuButton);
		  
		  
		 /* if(dashboardButton!=null && flagPinEntered==false)
		  {
			  Generic.WriteTestData("Launched GlobeAtHome App", "GlobeAtHomeApp", "", "App should be launched","App was launched", "Pass");
			  Control.takeScreenshot();
		  }*/
	
		  //
		  Generic.FindAndClick("GlobeAtHomeApp", "GoToDashboard", 3000);
		  Thread.sleep(2000);
		  Control.takeScreenshot();
		  
		  
		  WebElement dataConsumption=Generic.GetElementWithTimeOut("GlobeAtHomeApp", "DataConsumed", 15000);
		  WebElement dataTotal=Generic.GetElementWithTimeOut("GlobeAtHomeApp", "TotalDataBalance", 5000);
		  
		 
		  
		  if(dataConsumption!=null)
		  {
			  String strDataConsumption=dataConsumption.getText();
			  System.out.println("Data Consumption is  : "+dataConsumption.getText());
			  Generic.WriteTestData("Capturing Data Consumption", "DataConsumed", ""+strDataConsumption, "Should be able to capture Data Consumption", "Captured Data Consumption:"+strDataConsumption, "Pass");
		  
		  }
		  else
		  {
			  System.out.println("Data consumption is null");
			  Generic.WriteTestData("Capturing Data Consumption", "DataConsumed", "null", "Should be able to capture Data Consumption", "Unable to capture Data Consumption", "Fail");
			  
		  }
		  
		  //Control.takeScreenshot();
		  
		  if(dataTotal!=null)
		  {
			  String strTotalData="";
			  try
			  {
				  strTotalData=dataTotal.getText();
				  try
				  {
					  String tempStr=strTotalData.split(" ")[2];
					  strTotalData=tempStr;
				  }
				  catch(Exception e)
				  {
					  
				  }
				  System.out.println("Total Data is  : "+strTotalData);
				  
			  }
			  catch(Exception e)
			  {
				  strTotalData="Unable to Parse/Capture";
			  }
			  
			  Generic.WriteTestData("Capturing Total Data", "TotalData", ""+strTotalData, "Should be able to capture Total Data", "Captured Total Data : "+strTotalData, "Pass");
			  
		  }
		  else
		  {
			  System.out.println("Data total is null");
			  Generic.WriteTestData("Capturing Total Data", "TotalData", "null", "Should be able to capture Total Data", "Unable to capture Total Data", "Fail");
											  
										  }
	  
										  //input swipe 500 500 500 1000
		  /*SwipeDown(500,500,500,1000);
		  
		  WebElement planDescription=Generic.GetElementWithTimeOut("GlobeAtHomeApp", "PlanDesc", 15000);
		  
		  if(planDescription!=null)
		  {
			  String myPlanDesc=planDescription.getText();
			  Generic.WriteTestData("Capturing Plan Description", "PlanDescription", ""+myPlanDesc, "Should be able to capture Plan Desc", "Captured Plan Desc: "+myPlanDesc, "Pass");
			  Control.takeScreenshot();
		  }
		  else
		  {
			  Generic.WriteTestData("Capturing Plan Description", "PlanDescription", "null", "Should be able to capture Plan Desc", "Unable to capture Plan Description", "Fail");
			  
		  }
		  
		  SwipeUp(500,1000,500,500);*/
		  
		 WebElement backButton=Generic.GetElementWithTimeOut("GlobeAtHomeApp", "BackButton", 5000);
		 Generic.FindAndClick("GlobeAtHomeApp", "BackButton", 5000);
		 
		  Thread.sleep(2000);
		  Control.takeScreenshot();
		  
		  //String commandMenuButton="adb shell input tap "+locationX+" "+locationY; 
		// Control.RunProcessWithOutput(commandMenuButton, 15, false);
		  
		  int retVal=verifyIfMenuOpened();
		  
		  if(retVal==-99)
		  {
			  Generic.WriteTestData("Opening Menu Options", "MenuButton", "", "Should be able to view Menu Options", "Unable to view Menu Options", "Fail");
				
		  }
		  
	
		  WebElement consumerName=Generic.GetElementWithTimeOut("GlobeAtHomeApp", "DisplayName", 4000);
		  
		  if(consumerName==null)
		  {
			  System.out.println("Consumer Name is null");
			  Generic.FindAndClick("GlobeAtHomeApp", "MenuButton", 10000);
		  }
		  
		  WebElement consumerAccount=Generic.GetElementWithTimeOut("GlobeAtHomeApp", "DisplayAccount", 5000);
		  
		  if(consumerName!=null)
		  {
			  String strConsumerName=consumerName.getText();
			  System.out.println("Consumer Name is  : "+strConsumerName);
			  Generic.WriteTestData("Capturing Consumer Name", "ConsumerName", ""+strConsumerName, "Should be able to capture Consumer Name", "Captured Consumer Name : "+strConsumerName	, "Pass");
			  
		  }
		  else
		  {
			  System.out.println("Consumer Name is Null");
			  Generic.WriteTestData("Capturing Consumer Name", "ConsumerName", "null", "Should be able to capture Consumer Name", "Unable to capture Consumer Name", "Fail");
			  
		  }
		  
		  if(consumerAccount!=null)
		  {
			  String strConsumerAccount=consumerAccount.getText();
			  System.out.println("Consumer Account is  : "+strConsumerAccount);
			  Generic.WriteTestData("Capturing Consumer Account#", "ConsumerName", ""+strConsumerAccount, "Should be able to capture Consumer Account#", "Captured Consumer Account#  : "+strConsumerAccount, "Pass");
			  
		  }
		  else
		  {
			  System.out.println("Consumer Account is Null");
			  Generic.WriteTestData("Capturing Consumer Account#", "ConsumerName", "null", "Should be able to capture Consumer Account#", "Unable to capture Consumer Account#", "Fail");
			  
		  }
		  
		 
		  
		  Generic.FindAndClick("GlobeAtHomeApp", "LogoutOption", 3000);
		  Thread.sleep(1000);
		  Control.takeScreenshot();
		  
		  String backCommand="adb shell input keyevent 3";
			  Control.RunProcessWithOutput(backCommand, 15, false);
			  
			
		}
		
	}
								

				/*
				 * *************************************************************************************
			 							Default : USSD	
				 * *************************************************************************************
				 */
								
				   //By default the input is USSD
								else
								{
									
									System.out.println("Default Condition Handling");
								}
								
								
								
			/*
			 * **********************************************************************************************
			 * 							[ Actual Checkpoint code ]
			 * **********************************************************************************************
			 */
								
					//Fetch CheckPoint Scenario
								
								System.out.println("Mapping Checkpoint..");
								
								CheckValue=Constant.Map2.get("TestCase"+ RowNo).get("CheckPoint_"+Col_input);
			
								System.out.println("CheckPoint is : "+CheckValue);
								
								if(CheckValue=="" || CheckValue==null)
								{
									//Set to not null to avoid exception during trim()
									CheckValue="";
									
								}
								
				/*
				 * ************************************************************************************************
			 							Handle for Incoming SMS	
				 * ************************************************************************************************
				 */
								
					//
					//we only need to consider SMS after timestamp - Constant.lastSmsOrUssdTime
								else if(CheckValue.trim().toLowerCase().contains(("receive_text")))
								{
									
									
								}
								
				/*
				 * ***********************************************************************************************
				 * 					Handle  Verification for Balance Deduction	
				 * ***********************************************************************************************
				 */
								
								else if(CheckValue.trim().toUpperCase().contains("VERIFY_BALANCE_"))
								{
									
									
								}
								
				/*
				 * ***********************************************************************************************
				 * 					Handle  Verification for FreeText Deduction	
				 * ***********************************************************************************************
				 */
								else if(CheckValue.trim().toUpperCase().contains("VERIFY_FREETEXT_"))
								{
									
									
								}
			
									
								else if(flagCheckType==0)
								{
									
									
								}	
								
								else
								{
									System.out.println("No Condition matched! CheckPoint : "+CheckValue.trim());
									
								}
						
			
											
										
							}//end of test step
			
							
							
						}
						catch(Exception e)
						{
							System.out.println("Exception encountered in Test Case : "+RowNo+"\nException is : "+e.getMessage() );
							e.printStackTrace();
						}
						
						finally
						{
							Generic.TestScriptEnds();
							Control.GeneratePDFReport();
							System.out.println("PDF Report Generated");
						}
						
				}//End of Test Case
	
		}

	public static void tapOnHome()
	{
		boolean verificationAchieved=false;
		String tapCommand="adb shell input tap 25 75";
		String backCommand="adb shell input keyevent 4";
		//diff device resolution, Add code
		
				Control.RunProcessWithOutput(tapCommand, 15, false);
				Control.RunProcessWithOutput(backCommand, 15, false);
	
	}
	
	public static void close_without_clear()
	{
		  String closeApp="adb shell am force-stop ph.com.globe.globeathome";
		  Control.RunProcessWithOutput(closeApp,15,false);
	}
	
	public static void tapOnHome1()
	{
		String tapCommand2="adb shell input tap 50 300";
		Control.RunProcessWithOutput(tapCommand2, 15, false);
	}
		
	public static void FluentWait_function(String PageName, String locatorName)
	{
		return;
	}
	
	public static void grantPermission()
	{
		try
		{
			WebElement allowButton=Control.findElement("AllowButton", "Allow");
			if(allowButton!=null)
			{
				allowButton.click();
			}
			else
			{
				System.out.println("Did not click on Allow button as it is not displayed");
			}
		}
		catch(Exception e)
		{
			
		}
		
	}
	
	public static void IOS_SwitchApp()
	{
		Constant.driver.executeScript("seetest:client.deviceAction(\"Home\")");
		Constant.driver.findElement(By.xpath("//*[@text='Globe At Home']")).click();
	}
	
	public static void pressHomeOnIOS()
	{
		Constant.driver.executeScript("seetest:client.deviceAction(\"Home\")");
	}
	
	public static void ChooseAccount(String AccountNumber) throws Exception
	{
	   Constant.driver.findElement(By.xpath("//*[@text='"+AccountNumber+"']")) .click();
	   Thread.sleep(1000);
	}
	
	public static boolean IOS_EqualsText(String ActualText, String ExpectedText) throws Exception
	{
		try
		{
		 if(ActualText.equals(ExpectedText))
		  {
			  Generic.WriteTestData("Actual text should be same as expected text","Expected text is: '"+ExpectedText+"'"," ","Actual text should be the same as expected text","Actual text: '"+ActualText+"' is same as Expected text: '"+ExpectedText+"'","Pass");
			  return true;
		  }
		  else
		  {
			  Generic.WriteTestData("Actual text should be same as expected text","Expected text is: '"+ExpectedText+"'"," ","Actual text should be the same as expected text","Actual text: '"+ActualText+"' is not same as Expected text: '"+ExpectedText+"'","Fail");
			  return false;
		  }
		}
		catch (Exception e) {
	
			return false;
		}
	}
	
	public static WebElement customWait(String PageName, String locator, int timeout_exp) throws InterruptedException {
		Thread.sleep(1000);
		double x = Math.ceil(timeout_exp / Constant.defaultBrowserTimeOut);
		System.out.println("x is: " + x);
		WebElement a = null;
		for (double i = 0; i < x; i++) {
			a = Control.findElementWithTimeOut(PageName, locator,30);
			if (!(a==null)) {
				break;
			}
		}
		return a;
	}
	
	public static void LaunchAppInCloud() throws Exception
	{
		String Host = "techm-globe";
		DesiredCapabilities capabilities = new DesiredCapabilities("", "", Platform.ANY);
		capabilities.setCapability("securityToken", PerfectoLabUtils.fetchSecurityToken(Generic.ReadFromExcel("SecurityToken", "AI_TestData", 1)));
		capabilities.setCapability("deviceName", "00008030-00160C803640802E");
		capabilities.setCapability("platformName", "iOS");
		capabilities.setCapability("model", "iPhone-11");
		capabilities.setCapability("openDeviceTimeout", 2);
	    capabilities.setCapability("deviceSessionId", Generic.ReadFromExcel("SessionID", "AI_TestData", 1));
	   // Constant.driver = new RemoteWebDriver(new URL("https://"+Host+".perfectomobile.com/nexperience/perfectomobile/wd/hub"), capabilities);
	    Map<String, Object> params = new HashMap<>();
		params.put("identifier", "ph.gomo.gomox.dev");
		Constant.driver.executeScript("mobile:application:open", params);
	}
	
	public static void LaunchWebInCloud() throws Exception
	{
		String Host = "techm-globe-public";
		DesiredCapabilities capabilities = new DesiredCapabilities("", "", Platform.ANY);
		capabilities.setCapability("securityToken", PerfectoLabUtils.fetchSecurityToken(Generic.ReadFromExcel("SecurityToken", "AI_TestData", 1)));
		capabilities.setCapability("platformName", "Mac");
		capabilities.setCapability("platformVersion", "macOS Catalina");
		capabilities.setCapability("browserName", "Safari");
		capabilities.setCapability("browserVersion", "13");
		capabilities.setCapability("location", "NA-US-BOS");
		capabilities.setCapability("resolution", "800x600");
	    //Constant.driver = new RemoteWebDriver(new URL("https://"+Host+".perfectomobile.com/nexperience/perfectomobile/wd/hub"), capabilities);
	    Constant.driver.get("http://www.yahoo.com/");	
	}
	
	public static void RestartApp() throws Exception
	{
		Map<String, Object> params = new HashMap<>();
		//ph.gomo.gomoph.x.enterprise ph.gomo.gomox.dev	
		System.out.println("TESTING "+Constant.AppID);
		params.put("identifier", Constant.AppID);
		if(Constant.driver==null) {
			Constant.driver.executeScript("mobile:application:close", params);
		}
		else {
			Constant.driver.executeScript("mobile:application:close", params);
		}
		
		if(Constant.driver==null) {
			Constant.driver.executeScript("mobile:application:open", params);
		}
		else {
			Constant.driver.executeScript("mobile:application:open", params);
		}	
	}	
	
	public static void setDriverTimeOut(int timeInSeconds)
	{
		try	
		{
			System.out.println("\n[INFO] Setting Driver Timeout to : "+timeInSeconds);
	//		if(Constant.driver==null) {
	//			Constant.driver.manage().timeouts().implicitlyWait(timeInSeconds, TimeUnit.SECONDS); 
	//		}
	//		else {
			Constant.driver.manage().timeouts().implicitlyWait(timeInSeconds, TimeUnit.SECONDS); 
			//}
		}catch(Exception e)
		{
			System.out.println("[ERROR] Unable to set Driver TimeOut");
			e.printStackTrace();
		}
	}
	
	/*
	 PERFECTO SPECIFIC FUNCTIONS
	*/
	
	public static void TapCoordinates(String X, String Y) throws Exception
	{
		Map<String, Object> args = new HashMap<>();
		args.put("x", X);
		args.put("y", Y);
		Constant.driver.executeScript("mobile: tap", args);
	}
	
	public static void TapCoordinates2(String X, String Y) throws Exception
	{
		//USE FOR PERCENTAGE VALUE
		Map<String, Object> params = new HashMap<>();
		params.put("location", X+","+Y);
		params.put("operation", "single");
		Constant.driver.executeScript("mobile:touch:tap", params);
	}

	public static void SwipeToCoordinates (String X1, String Y1, String X2, String Y2) throws Exception
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", X1+","+Y1);
		params.put("end", X2+","+Y2);
		params.put("duration", "3");
		Object res = Constant.driver.executeScript("mobile:touch:swipe", params);
	}
	
	public static void EnterKeys(String Key) throws Exception
	{
		String Account = Key; 
		int lenght = Account.length();
		String number = String.valueOf(Account);
		String[] digits = number.split("(?<=.)");
	
		for(int i = 0; i < lenght; i++ )// i++
		{
			String element = digits[i];
			Thread.sleep(500);
			//Constant.driver.findElement(By.xpath("//*[@label='"+element+"']")).click();
			Constant.driver.findElement(By.xpath("//XCUIElementTypeKey[@label='"+ element +"']")).click();
			System.out.println(element);
			
		}
		Generic.WriteTestData("Enter Key", "", "", "Should Entey Key successfully", "Entered Key: "+Key, "Pass");
	}
	
	public static void EnterKeys_withoutTC(String Key) throws Exception
	{
		String Account = Key; 
		int lenght = Account.length();
		String number = String.valueOf(Account);
		String[] digits = number.split("(?<=.)");
	
		for(int i = 0; i < lenght; i++ )// i++
		{
			String element = digits[i];
			Thread.sleep(500);
			//Constant.driver.findElement(By.xpath("//*[@label='"+element+"']")).click();
			Constant.driver.findElement(By.xpath("//XCUIElementTypeKey[@label='"+ element +"']")).click();
			System.out.println(element);
		}
	}
	
	public static void HideKeyboard() throws Exception
	{
		Map<String, Object> params = new HashMap<>();
		params.put("mode", "off");
		Constant.driver.executeScript("mobile:keyboard:display", params);
	}
	
	public static void isTextVisible(String Text) throws Exception {
		if(Constant.driver.getPageSource().contains(Text))
		{
			Generic.WriteTestData("Check visibility of text '"+Text+"'", "", "", "Should be visible on the application", "'"+Text+"' text as found on the appliation", "Pass");
			System.out.println(Text + " is visible");
		}
		else
		{
			Generic.WriteTestData("Check visibility of text '"+Text+"'", "", "", "Should be visible on the application", "'"+Text+"' text as was not found on the appliation", "Fail");
			System.out.println(Text + " is not visible");
		}
	}
	
	public static String getOtp(String otp) throws Exception {
	
		//Dev: https://brskc46xre.execute-api.ap-southeast-1.amazonaws.com/dev/webtool/v1/get-otp-bd
		//STG: https://c0fs27xwue.execute-api.ap-southeast-1.amazonaws.com/stg/webtool/v1/get-otp-bd
		URL url = new URL("https://c0fs27xwue.execute-api.ap-southeast-1.amazonaws.com/stg/webtool/v1/get-otp-bd");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json; utf-8");
		con.setRequestProperty("Accept", "application/json");
		con.setDoOutput(true);
	
		String jsonInputString = "{\"serviceNumber\": \"" + otp + "\"}";
	
		try (OutputStream os = con.getOutputStream()) {
			byte[] input = jsonInputString.getBytes("utf-8");
			os.write(input, 0, input.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			// System.out.println(response.toString());
			String getResponse = response.toString();
			otp = getResponse.split("\"otp\":", 2)[1].replace("\"", "").replace("}", "");
			;
		}
		System.out.println("Otp: " + otp);
		return otp;
	}
	
	 public static void sendKeys(String text) {
		 Actions action = new Actions(Constant.driver);
		 action.sendKeys(text);
		 action.perform();
	
	 }
	 
	 public static void clickUsingActions(String PageName, String locator) throws Exception {
		Constant.PageName=PageName;
		Constant.locator=locator;
		try {
			WebElement element = findElement(PageName,locator);
			if (element != null) 
			{
				Actions action =new Actions(Constant.driver);
				action.moveToElement(element).build().perform();	
			}
			else {
	         	Constant.Failures += "Failed to click on "+locator+ "  in page "+PageName+ "\n";
				softAssertions.fail("Failed to click on "+locator+ "  in page "+PageName);
			}
			
			element = null;
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	 }
	 
	 public static void sendKeys2(String PageName, String label, String text) throws Exception {
			String getLabel = null;
			if (label != null) {
				if (PageName != "")
					getLabel = Constant.labelMap.get(PageName).get(label);
				else
					getLabel = label;
			}
			try {
				Map<String, Object> params = new HashMap<>();
				params.put("report", "Screenshot");
				params.put("report.resolution", "Medium");
				params.put("label", getLabel);
				params.put("text", text);
				String res = (String) Constant.driver.executeScript("mobile:edit-text:set", params);
				Thread.sleep(1000);
				//Control.takeScreenshot();
	
	//			Generic.WriteTestData("Sending text in to the text box", label, text,
	//					"Should be able to send text: " + text + " to the: " + label, "Same as expected", "Pass");
	
			} catch (Exception e) {
				Generic.WriteTestData("Sending text in to the text box", label, text,
						"Should be able to send text: " + text + " to the: " + label, "Not the same as  expected", "Fail");
			}
			Thread.sleep(1000);
			Constant.androiddriver.hideKeyboard();
		}
	 
	public static void enterPin(String pin) throws Exception {
		String[] enteredPin = pin.split("");
		String value = null;
		String keyCode = null;
		try {
			System.out.println("Start entering: " + pin);
			for (int i = 0; i < enteredPin.length; i++) {
				value = enteredPin[i];
				keyCode = getKeyCode(value);
				sendKeyEvent(keyCode, 0);
			}
			Thread.sleep(4500);
		} catch (Exception e) {
			
		}

	}
	
	public static String getKeyCode(String keyCode) throws Exception {
		String value = null;
		try {
			switch (keyCode) {
			case "0":
				value = "7";
				break;
			case "1":
				value = "8";
				break;
			case "2":
				value = "9";
				break;
			case "3":
				value = "10";
				break;
			case "4":
				value = "11";
				break;
			case "5":
				value = "12";
				break;
			case "6":
				value = "13";
				break;
			case "7":
				value = "14";
				break;
			case "8":
				value = "15";
				break;
			case "9":
				value = "16";
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return value;
	}
	
	public static void sendKeys(String PageName, String Locator, String textToSend) throws Exception {
		WebElement element;
		try {
			element = Control.findElement(PageName, Locator);
			if (element != null) {
				Assert.assertEquals(element.isDisplayed(), true,element+" is not displayed");
				Thread.sleep(2000);
				Map<String, Object> params = new HashMap<>();
				params.put("report", "Screenshot");
				params.put("report.resolution", "Medium");
				params.put("label", Locator);
				params.put("text", textToSend);
				String res = (String) Constant.driver.executeScript("mobile:edit-text:set", params);
				Thread.sleep(1000);
				
				Control.takeScreenshot();

				Generic.WriteTestData("Entering text '" + textToSend + "' in '" + Locator + "' text field", Locator,
						textToSend, "Should able to enter data'" + textToSend + "' into  '" + Locator + "' text field",
						"Entered data'" + textToSend + "' into  '" + Locator + "' text field successfully", "Pass");
			} else {
				Generic.WriteTestData("Entering text '" + textToSend + "' in '" + Locator + "' text field", Locator,
						textToSend, "Unable to identify the '" + Locator + "' text field",
						"Not able to identify the  '" + Locator + "' text field", "Fail");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Constant.androiddriver.hideKeyboard();
	}
	 
	public static void ClickByLabel(String PageName, String label) throws Exception {
		String getLabel = null;
		if (label != null) {
			if (PageName != "") {
				getLabel = Constant.labelMap.get(PageName).get(label);
				System.out.println(getLabel);
			} else {
				getLabel = label;
				System.out.println(getLabel);
			}
		}
		try {

			Map<String, Object> params = new HashMap<>();
			params.put("interval", 1);
			params.put("report", "Screenshot");
			params.put("report.resolution", "Medium");
			params.put("label", getLabel);
			Constant.driver.executeScript("mobile:button-text:click", params);
			Thread.sleep(1000);
			//Control.takeScreenshot();
//			Generic.WriteTestData("Should be able to click the button", PageName, label,
//					"Able to click button: " + label, "Same as expected", "Pass");
		} catch (Exception e) {
//			e.printStackTrace();
//			Generic.WriteTestData("Should be able to click the button", PageName, label,
//					"Able to click button: " + label, "Not the same as expected", "Fail");
		}
	}
	
	public static void scrollToWebElement(String PageName,String locator)
	    {
	        try
	        {
	        	WebElement element = findElement(PageName,locator);
	            JavascriptExecutor js = (JavascriptExecutor) Constant.driver;    
	            js.executeScript("arguments[0].scrollIntoView();", element);
	        }
	        catch (Exception e)
	        {
	            System.err.println("Unable to scroll to webelement.  WebElement is not visible.");
	        }
	    }
	 
	public static void fillUpAddAddress() throws Exception {
			String fName = Generic.ReadRandomCellData("Names");
			String lName = Generic.ReadRandomCellData("Names");

			String email = Generic.ReadFromExcel("Email", "LoginDetails", 1);
			String mobileNum = Generic.ReadFromExcel("GCashNum", "LoginDetails", 1);
			try {

				sendKeys("CreateAccount", "FirstName", fName);

				clickByLocation("42,783,1038,954");
				enterTextByKeyCodeEvent(lName);

//				sendKeys("CreateAccount", "UnitNo", Generic.ReadFromExcel("UnitNo", "LoginDetails", 1));
//				sendKeys("CreateAccount", "StreetNo", Generic.ReadFromExcel("StreetName", "LoginDetails", 1));
				AddAddress(1);
				Thread.sleep(2000);
				sendKeys("CreateAccount", "Email", email);
				Thread.sleep(2000);
				sendKeys("CreateAccount", "MobileNumber", mobileNum);
				Control.takeScreenshot();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	
	public static void clickByLocation(String location) throws Exception {
		try {
		Map<String, Object> params = new HashMap<>();
		params.put("location", location);
		System.out.println("Click by location: " + location);
		Object res = Constant.driver.executeScript("mobile:touch:tap", params);
		Thread.sleep(2000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 
	public static void enterTextByKeyCodeEvent(String textToSend) throws Exception {
		String[] texts = textToSend.split("");
		String textValue = null;
		try {
			for (int i = 0; i < texts.length; i++) {
				textValue = texts[i];
				String convertTextToKeyCode = GetKeyCodeEventForLetters(textValue);
				if (textValue == textValue.toUpperCase()) {
					sendKeyEvent(convertTextToKeyCode, 1);
				} else {
					sendKeyEvent(convertTextToKeyCode, 0);
				}
			}

			Thread.sleep(1000);
			Control.takeScreenshot();

			Generic.WriteTestData("Sending text in to the text box", "", textToSend,
					"Should be able to send text: " + textToSend, "Same as expected", "Pass");
		} catch (Exception e) {
			e.printStackTrace();
			Control.takeScreenshot();
			Generic.WriteTestData("Sending text in to the text box", "", textToSend,
					"Should be able to send text: " + textToSend, "Not the same as expected", "Fail");
		}
//			Constant.driver.hideKeyboard();
	}
	
	public static String GetKeyCodeEventForLetters(String letter) throws Exception {
		String value = null;
		try {
			switch (letter.toUpperCase()) {
			case "A":
				value = "29";
				break;
			case "B":
				value = "30";
				break;
			case "C":
				value = "31";
				break;
			case "D":
				value = "32";
				break;
			case "E":
				value = "33";
				break;
			case "F":
				value = "34";
				break;
			case "G":
				value = "35";
				break;
			case "H":
				value = "36";
				break;
			case "I":
				value = "37";
				break;
			case "J":
				value = "38";
				break;
			case "K":
				value = "39";
				break;
			case "L":
				value = "40";
				break;
			case "M":
				value = "41";
				break;
			case "N":
				value = "42";
				break;
			case "O":
				value = "43";
				break;
			case "P":
				value = "44";
				break;
			case "Q":
				value = "45";
				break;
			case "R":
				value = "46";
				break;
			case "S":
				value = "47";
				break;
			case "T":
				value = "48";
				break;
			case "U":
				value = "49";
				break;
			case "V":
				value = "50";
				break;
			case "W":
				value = "51";
				break;
			case "X":
				value = "52";
				break;
			case "Y":
				value = "53";
				break;
			case "Z":
				value = "54";
				break;
			case "@":
				value = "77";
				break;
			case "DEL":
				value = "112";
				break;
			case ".":
				value = "56";
				break;
			case " ":
				value = "62";
				break;
			case "0":
				value = "7";
				break;
			case "1":
				value = "8";
				break;
			case "2":
				value = "9";
				break;
			case "3":
				value = "10";
				break;
			case "4":
				value = "11";
				break;
			case "5":
				value = "12";
				break;
			case "6":
				value = "13";
				break;
			case "7":
				value = "14";
				break;
			case "8":
				value = "15";
				break;
			case "9":
				value = "16";
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public static void AddAddress(int selectAddress) throws Exception {
		WebElement element = null;
		Constant.UnitNo = Generic.ReadFromExcel("UnitNo", "LoginDetails", selectAddress);
		Constant.StreetName = Generic.ReadFromExcel("StreetName", "LoginDetails", selectAddress);
		Constant.Province = Generic.ReadFromExcel("Province", "LoginDetails", selectAddress);
		Constant.City = Generic.ReadFromExcel("City", "LoginDetails", selectAddress);
		Constant.Barangay = Generic.ReadFromExcel("Barangay", "LoginDetails", selectAddress);
		Constant.ZipCode = Generic.ReadFromExcel("ZipCode", "LoginDetails", selectAddress);
		try {
			Thread.sleep(5000);
			sendKeys("CreateAccount", "UnitNo", Constant.UnitNo);
			sendKeys("CreateAccount", "StreetNo", Constant.StreetName);
			click("CreateAccount", "Province");
			Thread.sleep(1000);
			sendKeys("CreateAccount", "EnterProvince", Constant.Province);
			Thread.sleep(1000);
			element = Constant.driver
					.findElement(By.xpath("//*[@content-desc=\"" + Constant.Province + ", List Choice Label\"]"));
			element.click();

			Thread.sleep(2000);

			sendKeys("CreateAccount", "EnterCity", Constant.City);
			Thread.sleep(1000);
			element = Constant.driver
					.findElement(By.xpath("//*[@content-desc=\"" + Constant.City + ", List Choice Label\"]"));
			element.click();

			Thread.sleep(2000);

			sendKeys("CreateAccount", "EnterBarangay", Constant.Barangay);
			Thread.sleep(1000);
			element = Constant.driver
					.findElement(By.xpath("//*[@content-desc=\"" + Constant.Barangay + ", List Choice Label\"]"));
			element.click();
			Thread.sleep(1000);
			Control.takeScreenshot();
			//scrollToText("This Address"); // Add This Address || Save This Address
			Control.scrollToWebElement("AddAddress", "UseThisAddressButton");
			Thread.sleep(2000);
			click("CreateAccount", "ZipCode");
			sendKeys("CreateAccount", "ZipCode", Constant.ZipCode);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void sendKeyEvent(String keyCode, int metaStates) throws Exception {
		try {
			Map<String, Object> pars = new HashMap<>();
			pars.put("key", keyCode);
			pars.put("metastate", metaStates); // 0 lower case || 1 upper case
			Constant.driver.executeScript("mobile:key:event", pars);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	public static void clearText(String PageName, String locator) throws Exception {
		Constant.PageName=PageName;
		Constant.locator=locator;
		try {
			WebElement element = findElement(PageName,locator);
			if (element != null) 
			{
				try {
					element.clear();
				} catch (Exception e) {}
				Thread.sleep(2000);
				Actions action =new Actions(Constant.driver);
			}
			else {
            	Constant.Failures += "Failed to enter text in "+locator+ "  in page "+PageName+ "\n";
				softAssertions.fail("Failed to enter text in "+locator+ "  in page "+PageName);

			}
			element = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public static String EnterValueManually(){  
		JFrame f;
	    f=new JFrame();   
	    String value=JOptionPane.showInputDialog(f,"Enter Value");     
		return value;
	}  
	
	public static void switchContext(String toContext) throws InterruptedException {	
		try {
			// get current context
			String currentContext = Constant.driver.getContext();
	    	System.out.printf(">> Current Context: %s \n", currentContext);
	    	
	    	// get context handles
			Set<String> allContext = Constant.driver.getContextHandles();
			System.out.println(">> Available Contexts:");
			allContext.forEach(System.out::println);
			
			// get match from passed parameter
			List<String> contexts = allContext.stream()
					.filter(x -> x.equalsIgnoreCase(toContext))
					.collect(Collectors.toList());
			
			if(contexts.size () > 0) {
				// set new context
				Constant.driver.context(contexts.get(0));
				
				Thread.sleep(1000);
				// get new context
				currentContext = Constant.driver.getContext();
	    		System.out.printf(">> new context: %s \n", currentContext);	
			}
			else {
				System.out.printf(">> Desired context %s not found\n", toContext);	
			}

		} catch (Exception e) {
			e.printStackTrace();	
		}	    
	}
		
	public static void editText(String PageName, String locator, String value) throws Exception {
		String getLabel = null;
		String attribute = null;

		if (Constant.Platform.equalsIgnoreCase("ios")) {
			attribute = "value";
			getLabel = getAttribute(PageName,locator,attribute);
		} else {
			attribute = "content-desc";
			getLabel = getAttribute(PageName,locator,attribute);
			String[] splitText = getLabel.split(",");
			getLabel = splitText[0];
		}		
		
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("report", "Screenshot");
			params.put("report.resolution", "Medium");
			params.put("label", getLabel);
			params.put("text", value);
			String res = (String)Constant.driver.executeScript("mobile:edit-text:set", params);
			Generic.WriteTestData("Sending text in to the text box", locator, value, "Should be able to send text: " + value + " to the: " + locator, "Same as expected", "Pass");

		}catch (ClassCastException e) {
			Generic.WriteTestData("Sending text in to the text box", locator, value,
					"Should be able to send text: " + value + " to the: " + locator, "Same as expected", "Pass");
			
		}catch (Exception e) {
			e.printStackTrace();
			Generic.WriteTestData("Sending text in to the text box", locator, value,
					"Should be able to send text: " + value + " to the: " + locator, "Not the same as  expected", "Fail");
		}
	}
		
	public static WebElement findXPath(String xpath) throws Exception {
		WebElement element = null;
		try {	
			element = Constant.driver.findElement(By.xpath(xpath));
			return element;
		}
		catch (Exception e) {
			return element;
		}	
	}
	
	public static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        double d = Double.parseDouble(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
		
	public static boolean isElementLocationVisible(String pageName, String locator) throws Exception {
		Constant.PageName=pageName;
		Constant.locator=locator;

		try {
			WebElement element = customWait(pageName, locator, 240);		
			if (element != null) {			
				float x = element.getLocation().getX();
				float y = element.getLocation().getY();

				if (x == 0 && y == 0) {
					System.out.println("Coordinates are set to 0");
					return false;
				}
				else {
					System.out.println("Element location is visible");
					return true;
				}
			
			}
			else {
				throw new Exception();
			}
		} 
		catch (Exception e) {		
				
				e.printStackTrace();
				System.out.println("Slide failed with coorninates");
				throw new Exception("Coordinates for "+ pageName + " and "+ locator + " not found");
		}	
	}
	
	public static void scrollToElement(String pageName, String locator, String yStart, String yEnd) throws Exception {	
		try {
			int i=6;
			while(Control.findElementWithoutPrintingException(pageName, locator) == null)
			{
				SwipeToCoordinates("50%", yStart,"50%", yEnd);
				i--;
				if(i==0)
				{	
					break;
				}
			}	
			
			if(Constant.Platform.equalsIgnoreCase("ios")) {
				boolean isVisible = Control.isElementLocationVisible(pageName, locator);
				if(isVisible == false) {
					SwipeToCoordinates("50%", yStart,"50%", yEnd);
				}
			}	
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public static void clickText(String Text) throws Exception
	{
		if(Constant.driver.getPageSource().contains(Text))
		{
			Map<String, Object> params = new HashMap<>();
			params.put("label", Text);
			params.put("threshold", 80);
			params.put("ignorecase", "nocase");
			Constant.driver.executeScript("mobile:button-text:click", params);
			Generic.WriteTestData("Click on text'"+"'",Text,"","Able to click on '"+Text+"'","Clicked on '"+Text+"' text.","Pass");
			System.out.println(("Clicked "+Text+" text"));
		}
		else
		{
			Generic.WriteTestData("Click on text'"+"'",Text,"","Text should be available '"+Text+"' .","Text was not found  '"+Text+"'","Fail");
			System.out.println(("Unable to find "+Text+" text"));
		}
	}

	public static void clickByImageText(String ElementName, String ImageLocation) throws Exception
	{
		 try {
			Map<String, Object> params = new HashMap<>();
			 params.put("label", ImageLocation);
			 params.put("threshold", 80);
			 params.put("imageBounds.needleBound", 30);
			 Constant.driver.executeScript("mobile:button-image:click", params);
			 Generic.WriteTestData("Click on the element'"+"'",ElementName,"","Click on the element should be success'"+ElementName+"' .","Falied to click on the element '"+ElementName+"'","Pass");
		} catch (Exception e) {
			Generic.WriteTestData("Click on the element'"+"'",ElementName,"","Click on the element should be success'"+ElementName+"' .","Falied to click on the element '"+ElementName+"'","Fail");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	////// Android Functions Only //////
	
	public static void keypadBackButton_android() throws Exception {
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("keySequence", "BACK");
			Constant.driver.executeScript("mobile:presskey", params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void keypadNextButton_android() throws Exception {
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("keySequence", "NEXT");
			Constant.driver.executeScript("mobile:presskey", params); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void enterKeyCodeEvent(String Text) throws Exception {
		String[] texts = Text.split("");
		String textValue = null;
		try {
			for (int i = 0; i < texts.length; i++) {
				textValue = texts[i];
				String convertTextToKeyCode = GetKeyCodeEventForLetters(textValue);
				if(isNumeric(textValue) == true || textValue.equals(".")) {
					sendKeyEvent(convertTextToKeyCode, 0); 
				}
				else {
					if (textValue == textValue.toUpperCase()) {
						sendKeyEvent(convertTextToKeyCode, 1);
					} else {
						sendKeyEvent(convertTextToKeyCode, 0);
					}
				}		
			}

			Thread.sleep(1000);
			Control.takeScreenshot();

			Generic.WriteTestData("Sending text in to the text box", "", Text,
					"Should be able to send text: " + Text, "Same as expected", "Pass");
		} catch (Exception e) {
			e.printStackTrace();
			Control.takeScreenshot();
			Generic.WriteTestData("Sending text in to the text box", "", Text,
					"Should be able to send text: " + Text, "Not the same as expected", "Fail");
		}
	}
	
	////// iOS Functions Only ////// 
	
	public static void iOSKeyboard_AlphabetOnly(String text) throws Exception {
		//manually press key from iOS Keyboard - slow
		//workaround for issue encountered using sendkey on GCash App
		WebElement key;
		WebElement change;
		String[] texts = text.split("");
		for (int i = 0; i < texts.length; i++) {
			key = findXPath("//*[@label='" + texts[i] +"']");
			if(key == null) {
				Control.click("Keyboard", "ShiftButton");
				key = findXPath("//XCUIElementTypeKey[@label='" + texts[i] + "']");
				key.click();
			}
			else {
				key.click();	
			}		
		}
	}
	
	public static void iOSKeyboard(String text) throws Exception {
		//manually press key from iOS Keyboard - slow
		//workaround for issue encountered using sendkey on GCash App
		WebElement key;
		WebElement change;
		String[] texts = text.split("");
		for (int i = 0; i < texts.length; i++) {
			if(isNumeric(texts[i]) == true || texts[i].equals("@") || texts[i].equals(".")) {
				change = findXPath("//*[@label='numbers']");
				if(change != null) {
					change.click();
				}
				key = findXPath("//XCUIElementTypeKey[@label='" + texts[i] + "']");
				key.click();
			}
			else {
				change = findXPath("//*[@label='letters']");
				if(change != null) {
					change.click();
				}
				
				key = findXPath("//*[@label='" + texts[i].toLowerCase() +"'] | //*[@label='" + texts[i].toUpperCase() + "']");
				key.click();
				/*
				if(key == null) {
					Control.click("Keyboard", "ShiftButton");
					key = findXPath("//XCUIElementTypeKey[@label='" + texts[i] + "']");
					key.click();
				}
				else {
					key.click();	
				}	
				*/
			}	
		}
	}
	
	public static void iOSKeyboard_NumericOnly(String text) throws Exception {
		//manually press key from iOS Keyboard - slow
		//workaround for issue encountered using sendkey on GCash App
		WebElement key;
		String[] texts = text.split("");
		Control.click("Keyboard", "NumberButton");
		for (int i = 0; i < texts.length; i++) {
			key = findXPath("//XCUIElementTypeKey[@label='" + texts[i] + "']");
			key.click();		
		}	
	}
	
	////// Swiping Functions ////// 
	
	public static void scrollDowntoXPath(String PageName, String Locator) 
	{
	    boolean flag=true;
	    int count=1;
	    while(flag){
	        try {
	        	Control.findElement(PageName, Locator);
	            flag=false;
	            break;
	        }
	        catch(Exception NoSuchElementException) {
	            count=count+1;
	            Map<String, Object> params = new HashMap<>();
	            params.put("start","40%,90%");
	            params.put("end","40%,20%");
	            params.put("duration","2");
	            Object res= Constant.driver.executeScript("mobile:touch:swipe",params);
	        if(count==5)
	        {
	            break;
	        }
	        }
	    }
	}
	
	public static void scrollforiOS(String PageName, String Locator, String direction) {
		try {
			if(!(Control.findElement(PageName, Locator)==null)) {
			    boolean flag=true;
			    int count=1;
			    while(!Control.findElement(PageName, Locator).isDisplayed()){
			    	 Map<String, Object> params = new HashMap<>();
			    	 params.put("start","40%,90%");
			    	 params.put("end","40%,20%");
			    	 params.put("duration","2");
			    	 Constant.driver.executeScript("mobile:touch:swipe",params);			  
			    }			
			}else {
				throw new Exception("No such  element is found in the dom");
			}		
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
		
	public static void scrollToElement(String PageName, String Locator) {
		HashMap<String,Object> scrollObject = new HashMap<>();
		scrollObject.put("direction", "down");
		scrollObject.put("name", "Web View");
		Constant.driver.executeScript("mobile:scroll",scrollObject);
		Constant.driver.findElementByXPath(Locator).click();	
	}
	
	public static void scrollToEmement_android(){
		JavascriptExecutor js = (JavascriptExecutor)Constant.driver;
		HashMap<String, String> scrollObject = new HashMap<>();
		scrollObject.put("direction", "down");
		js.executeScript("mobile: swipe", scrollObject);
	}
	
	public static void scrollToElement(String PageName, String Locator, String direction) throws Exception
	{
		Map<String, Object> params = new HashMap<>();
		int i=6;
		switch(direction)
		{
		case "down":
	        params.put("start","40%,90%");
	        params.put("end","40%,20%");
			break;
		case "up":
	        params.put("start","40%,20%");
	        params.put("end","40%,90%");
			break; 		
		}

		while(Control.findElementWithoutPrintingException(PageName, Locator)==null)
		{
	        params.put("duration","3");
	        Object res= Constant.driver.executeScript("mobile:touch:swipe",params);
	        System.out.println("Scrolling to "+Locator+" element");
			i--;
			if(i==0)
			{	
				break;
			}
		}
	}

	public static void scrollToText(String Text, String direction) throws Exception {
		try
		{
			Map<String, Object> params = new HashMap<>();
			int i=5;
			switch(direction)
			{
			case "down":
		        params.put("start","40%,90%");
		        params.put("end","40%,20%");
				break;
			case "up":
		        params.put("start","40%,20%");
		        params.put("end","40%,90%");
				break; 		
			}
		
			while(!Constant.driver.getPageSource().contains(Text))
			{
		        params.put("duration","2");
		        Object res= Constant.driver.executeScript("mobile:touch:swipe",params);
		        System.out.println("Scrolling to: " + Text);
				i--;
				if(i==0)
				{	
					break;
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void scrollToText2(String Text, String direction) throws Exception
	{
		try
		{
			Map<String, Object> params = new HashMap<>();
			int i=5;
			switch(direction)
			{
			case "down":
		        params.put("start","40%,50%");
		        params.put("end","40%,20%");
				break;
			case "up":
		        params.put("start","40%,20%");
		        params.put("end","40%,90%");
				break; 		
			}
		
			while(!Constant.driver.getPageSource().contains(Text))
			{
		        params.put("duration","3");
		        Object res= Constant.driver.executeScript("mobile:touch:swipe",params);
		        System.out.println("Scrolling to: " + Text);
				i--;

				if(i==0)
				{	
					break;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	
}

