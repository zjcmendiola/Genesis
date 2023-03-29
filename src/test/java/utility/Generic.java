//Description: Control Functions library 

package utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumDriver;

import org.apache.poi.ss.usermodel.*;

public class Generic {	
	private static XSSFWorkbook workbook;
	private static XSSFSheet Worksheet;
	private static XSSFCell cell;
	
	private static String getcelldata(int rownum, int colnum) throws Exception{
		String celldata=null;
		DataFormatter formatter = new DataFormatter();
		try{
			//cell= Worksheet.getRow(rownum).getCell(colnum);
		
		 //celldata=cell.getStringCellValue();
			celldata=formatter.formatCellValue(Worksheet.getRow(rownum).getCell(colnum));
		
		}catch (Exception e) {			
			System.out.println("Exception while getCellData : Row,Col"+rownum+","+colnum+e.getMessage());
			e.printStackTrace();
			
			//System.exit(-1);
		}
		return celldata;
	}
	
	public static String ReadFromExcel(String strVariable, String strSheetname,int iColumnNo) throws Exception
	{
		//System.out.println("In Read from Excel");
		String strText = null;
		String strData;
		try {
			FileInputStream ExcelFile= new FileInputStream(Constant.TestDataFilePath);
			workbook = new XSSFWorkbook(ExcelFile);
			Worksheet =  workbook.getSheet(strSheetname);		
			int totalrows=Worksheet.getLastRowNum();		
			for(int i=0; i<totalrows+1;i++)
			{
				strData=getcelldata(i,0);
				//System.out.println("StrData is "+strData);
				//System.out.println("StrVar is "+strVariable);
				if(strVariable.equals(strData.toString())){					
					strText=getcelldata(i,iColumnNo);
					//System.out.println("*************************** [ FINALLY EQUAL ] *************************** ");
					break;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Exception while reading from Excel : "+e.getMessage());
			e.printStackTrace();
		}
		return strText;		
	}
	
	public static void WriteToExcel(String strVariable, String strText, String strSheetname,int iColumnNo) throws Exception
	{
		String strData;
		try {
			FileInputStream ExcelFile= new FileInputStream(Constant.TestDataFilePath);
			workbook = new XSSFWorkbook(ExcelFile);
			Worksheet =  workbook.getSheet(strSheetname);		
			int totalrows=Worksheet.getLastRowNum();	
			for(int i=0; i<totalrows+1;i++)
			{
				strData=getcelldata(i,0);
				if (strData.toString().equals(strVariable))
				{
					cell = Worksheet.getRow(i).getCell(iColumnNo);
					cell.setCellValue(strText);
					break;
				}
			}
			FileOutputStream fos = new FileOutputStream(Constant.TestDataFilePath);
			workbook.write(fos);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				System.out.println("Error writingto the existing excel, making a copy");
				FileOutputStream fos = new FileOutputStream("TestDataBkUp.xlsx");
				workbook.write(fos);
				System.out.println("Excel Saved to the in another name");
			}catch (FileNotFoundException ex){
				e.printStackTrace();
			}		
		}			
	}
	
	public static void TestScriptStart(String UserStoryName,String platform) throws Exception{		
		System.out.println("Handling Repo Creation");
		RepositoryCreation(platform);
		//RepositoryLabelCreation();
		System.out.println("Done Repo Creation");
		Constant.UserStoryName = UserStoryName;
		Constant.Env = System.getProperty("env");
		if(Constant.Env.equalsIgnoreCase("dev")) {
			Constant.AppID = Generic.ReadFromExcel("BundleID_Dev", "AI_TestData", 1).trim();
		}else if(Constant.Env.equalsIgnoreCase("stg")) {
			if(Constant.Platform.equals("iOS")) {
				Constant.AppID = Generic.ReadFromExcel("BundleID_Stg_iOS", "AI_TestData", 1); 
			} 
			else {
				Constant.AppID = Generic.ReadFromExcel("BundleID_Stg_Android", "AI_TestData", 1); 
			}	  
		}
		Constant.Platform=platform;
		String Main_Folder;
		Main_Folder= System.getProperty("user.dir")+File.separator+ReadFromExcel("ResultsFolderPath","AI_TestData",1);		
		System.out.println(System.getProperty("user.dir"));
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
		DateFormat TimeFormat = new SimpleDateFormat("HHMMSS");
		Date date2= new Date();
		
		String Date3 = TimeFormat.format(date2); 
		Date date= new Date();
		String Date1 = dateFormat.format(date);
		String strFolderName  = UserStoryName +Date1+"_"+Date3;
		File file = new File(Main_Folder);
		if(!file.exists()) {
			file.mkdir();
			System.out.println("Created : "+Main_Folder);
		}
		String strFolderPath = Main_Folder  + strFolderName;
		file = new File(strFolderPath);
		if(!file.exists()) {
			file.mkdir();
			System.out.println("Created : "+strFolderPath);
		}
		strFolderName = strFolderPath + File.separator + UserStoryName;
		file = new File(strFolderName);
		if (!file.exists()) {
			file.mkdir();
			System.out.println("Created : "+strFolderName);
		}
		String strXlsFileName = strFolderName + File.separator + UserStoryName +".xlsx";
		String strHtmlFileName = strFolderName + File.separator + UserStoryName +".html";
		if (file.exists()) 
		{	
			File FileName= new File(strXlsFileName);
			FileOutputStream fos =new FileOutputStream (FileName);
			XSSFWorkbook Wb = new  XSSFWorkbook();
			XSSFSheet sh = Wb.createSheet("Results");
			Row row = sh.createRow(0);
			String[] columNames = {"Test Scenario Sr No","Test Step No","Test Case/Step Desc","Test Object Name","Test Data","Expected Result","Actual Result","Test Case/Step Status"	,"Screenshot", "StartTime", "EndTime"};
			
			for(int i=0;i<=10;i++) {
				Cell cell =row.createCell(i);
				cell.setCellValue(columNames[i]);				
			}		
			Wb.write(fos);
			fos.close();	
			Wb.close();
			strFolderName = strFolderName + File.separator + "Screenshots";
			
			Constant.ScreenshotFolderName = strFolderName;
			file = new File(strFolderName);
			if(!file.exists()) 
			{
				file.mkdir();
			}
			Date objDate = new Date();  
		}
		Constant.ResultFilePath=strXlsFileName;
	}

	public static void TestScriptStart1(String UserStoryName,String platform) throws Exception {
		System.out.println("Handling Repo Creation");
		RepositoryCreation(platform);
		RepositoryLabelCreation();
		System.out.println("Done Repo Creation");
		Constant.UserStoryName = UserStoryName;
		Constant.Platform=platform;
	
		String Main_Folder;
	
		if(platform.equals("iOS")) {
			Main_Folder= System.getProperty("user.dir")+File.separator+ReadFromExcel("ResultsFolderPath_iOSIPA","AI_TestData",1);
		}else {
			Main_Folder= System.getProperty("user.dir")+File.separator+ReadFromExcel("ResultsFolderPath_AndroidApk","AI_TestData",1);
		}
		
		System.out.println(System.getProperty("user.dir"));
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
		DateFormat TimeFormat = new SimpleDateFormat("HHMMSS");
		Date date2= new Date();
		
		String Date3 = TimeFormat.format(date2); 
		Date date= new Date();
		String Date1 = dateFormat.format(date);
		String strFolderName  = UserStoryName +Date1+"_"+Date3;
		File file = new File(Main_Folder);
		if(!file.exists()) {
			file.mkdir();
			System.out.println("Created : "+Main_Folder);
		}
		String strFolderPath = Main_Folder  + strFolderName;
		file = new File(strFolderPath);
		if(!file.exists()) {
			file.mkdir();
			System.out.println("Created : "+strFolderPath);
		}
		strFolderName = strFolderPath + File.separator + UserStoryName;
		file = new File(strFolderName);
		if (!file.exists()) {
			file.mkdir();
			System.out.println("Created : "+strFolderName);
		}
		String strXlsFileName = strFolderName + File.separator + UserStoryName +".xlsx";
		String strHtmlFileName = strFolderName + File.separator + UserStoryName +".html";
		if (file.exists()) 
		{	
			File FileName= new File(strXlsFileName);
			FileOutputStream fos =new FileOutputStream (FileName);
			XSSFWorkbook Wb = new  XSSFWorkbook();
			XSSFSheet sh = Wb.createSheet("Results");
			Row row = sh.createRow(0);
			String[] columNames = {"Test Scenario Sr No","Test Step No","Test Case/Step Desc","Test Object Name","Test Data","Expected Result","Actual Result","Test Case/Step Status"	,"Screenshot", "StartTime", "EndTime"};
			
			for(int i=0;i<=10;i++) {
				Cell cell =row.createCell(i);
				cell.setCellValue(columNames[i]);				
			}		
			Wb.write(fos);
			fos.close();	
			Wb.close();
			strFolderName = strFolderName + File.separator + "Screenshots";
			
			Constant.ScreenshotFolderName = strFolderName;
			file = new File(strFolderName);
			if(!file.exists()) {
				file.mkdir();
			}
			Date objDate = new Date();

		}
		Constant.ResultFilePath=strXlsFileName;
	}

	public static void RC1() throws Exception	{
		String strText,PageName, strValue;
		int totalrows;
		try {
			FileInputStream ExcelFile= new FileInputStream(Constant.TestDataFilePath);
			workbook = new XSSFWorkbook(ExcelFile);
			
			for(int j=0; j<workbook.getNumberOfSheets(); j++){
				PageName = workbook.getSheetName(j);			
				Constant.Map.put(PageName, new HashMap<String,String>());
				Worksheet =  workbook.getSheet(PageName);
				totalrows = Worksheet.getLastRowNum();
				for(int i=0; i<totalrows+1;i++)
				{							
					strText=getcelldata(i,0);
					strValue = getcelldata(i,1);
					Constant.Map.get(PageName).put(strText, strValue);
					strText=null;	
				}	
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void WriteTestData(String strStepDesc, String strObjectname, String strTestData, String strExpectedResult,String strActualResult, String strStepStatus ) {
		try
		{
			String strXlsFileName = Constant.ResultFilePath;
			System.out.printf(">> StepDesc: %s, Objectname:%s, TestData:%s,%n   Expected: %s, Actual: %s, StepStatus: %s%n", strStepDesc, strObjectname, strTestData, strExpectedResult, strActualResult, strStepStatus);
			int strData,strData1, strData11, strData111;
			strData = Constant.StepIndex;
			strData1 = strData+1;
			Constant.StepIndex = strData1;
			strData = Constant.StepIndex;
			strData11 = Constant.TestStepIndex;
			strData111 = strData11+1;
			Constant.TestStepIndex = strData111;
			strData111 = Constant.TestStepIndex;	
			FileInputStream ExcelFile=null;
			try {
			ExcelFile= new FileInputStream(strXlsFileName);
			workbook = new XSSFWorkbook(ExcelFile);
			Worksheet =  workbook.getSheet("Results");
			Row row = Worksheet.createRow(strData);
			String[] columNames = {""+strData111+"",strStepDesc,strObjectname,strTestData,strExpectedResult,strActualResult,strStepStatus};
			for(int i=1;i<8;i++) {
				Cell cell =row.createCell(i);
				cell.setCellValue(columNames[i-1]);				
			}	
			
			if(strStepStatus.equalsIgnoreCase("Fail")) {   

					strData11 = Constant.StepStatus;
					strData11 = strData11+1;
					Constant.StepStatus = strData11;
				

	        	Control.takeScreenshot(false);        	
		        Cell cell = row.createCell(8);
		    	cell.setCellValue(Constant.ScreenshotFolderName+File.separator+Constant.RecentScreenshot);
		        final Hyperlink href = workbook.getCreationHelper().createHyperlink(HyperlinkType.FILE);
		        String FolderPath= "File:///"+ Constant.ScreenshotFolderName+File.separator+Constant.RecentScreenshot;	        
		        FolderPath= FolderPath.replace("\\", "/");	       
		        href.setAddress(FolderPath);
		        cell.setHyperlink(href);
	        }
		
			}catch (Exception e) {
				System.out.println("Exception in WriteTestData(): "+e.getMessage());
				e.printStackTrace();
				
			}
			
			finally
			{
				ExcelFile.close();		
				FileOutputStream fos = new FileOutputStream(strXlsFileName);		
				workbook.write(fos);		
				workbook.close();
				fos.close();	
			}
		}
		catch(Exception e)
		{
			System.out.println("Exception while Writing test data : "+e.getMessage());
			e.printStackTrace();
		}

	}
	
	public static void RepositoryCreation(String platform) throws Exception
	{
		String strText,PageName, strValue;
		int totalrows;
		try {
			FileInputStream ExcelFile= new FileInputStream(Constant.TestDataFilePath);
			workbook = new XSSFWorkbook(ExcelFile);
			Worksheet =  workbook.getSheet("ObjectRepository");
			totalrows = Worksheet.getLastRowNum();
			PageName = null; 
			for(int j=0; j <= totalrows; j++){
				strValue= getcelldata(j, 0);	
				if (strValue.equalsIgnoreCase("")){
					
				}
				else
				{
					PageName = strValue;
					Constant.Map.put(PageName, new HashMap<String,String>());
				}						
				strText=getcelldata(j,1);

				if(platform.equalsIgnoreCase("iOS")) {
					strValue = getcelldata(j,3);
				}
				else {
					strValue = getcelldata(j,2);
				}
				
				Constant.Map.get(PageName).put(strText, strValue);
				strText=null;					
			}					
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void WriteTestCase(String sTestCaseNumber,String strScenarioDesc,String strExpectedResult,String strActualResult) throws Exception{
		String strXlsFileName = Constant.ResultFilePath;
		Constant.StepStatus=0;
		if(Constant.MultipleTCInOneMethod)
		{
			
		}
		else
		{
		Constant.Failures = " ";
		}
		try
		{
			File f=new File(Constant.ResultFilePath);
			f.getParentFile().mkdirs();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		int strData1,strData2, stepStatus, strDiff;
		strData1 = Constant.StepIndex; 
        strData2 = Constant.TestStepIndex;
        stepStatus = Constant.StepStatus;
        strDiff= strData1-strData2;   
        FileInputStream ExcelFile= new FileInputStream(strXlsFileName);
		workbook =  new XSSFWorkbook(ExcelFile);
		Worksheet =  workbook.getSheet("Results");
//		Row row = Worksheet.createRow(strDiff+1);
//		CellStyle style = workbook.createCellStyle();
//		for(int i=0;i<8;i++) {
//			Cell cell =row.createCell(i);
//			if(stepStatus==0) {				
//				style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
//				cell.setCellStyle(style);
//			}
//			else
//			{
//				style.setFillForegroundColor(IndexedColors.RED.getIndex());
//				cell.setCellStyle(style);				
//			}
//		}
		
		Constant.StepStatus=0;
		
	int strData = Constant.StepIndex;
	if(Constant.TestCaseNumber==0) {
	    strData1=strData+1;
	    Constant.StepIndex=strData1 ;
	}
	    strData =Constant.StepIndex;
	    
	    strData2 = Constant.TestCaseIndex;
	    int strData3=strData2+1;
	    Constant.TestCaseIndex=strData3;
	    int strData4 = Constant.TestCaseIndex;
	            	            
	    Constant.TestStepIndex=0;
	    //Row row1 = Worksheet.createRow(strDiff+1);
	    Row row1 = Worksheet.createRow(strData);	
	    Cell cell=row1.createCell(0);
	    cell.setCellValue(sTestCaseNumber);
	    cell=row1.createCell(5);	     
	    cell.setCellValue(strExpectedResult);
	    cell=row1.createCell(6);
	    cell.setCellValue(strActualResult); 		
	    cell=row1.createCell(2);
	   // Worksheet.addMergedRegion(new CellRangeAddress(strData,strData,1,4));
	    cell.setCellValue(strScenarioDesc); 
	    cell=row1.createCell(1);
	    cell.setCellValue(""); 
	    cell=row1.createCell(3);
	    cell.setCellValue(""); 
	    cell=row1.createCell(4);
	    cell.setCellValue(""); 
	    cell=row1.createCell(7);
	    cell.setCellValue(""); 
	    DateTimeFormatter dtf= DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	    cell=row1.createCell(9);
	    String tim = LocalDateTime.now().format(dtf);
	    cell.setCellValue(tim); 
	    
//	    cell=row1.createCell(11);
//	    cell.setCellValue(Control.checkbal("*143*2*2*1#")); 
	    
	    
		ExcelFile.close();		
		FileOutputStream fos = new FileOutputStream(strXlsFileName);	
		workbook.write(fos);		
		workbook.close();
		fos.close();
		Constant.strScenarioDesc =strScenarioDesc;
		Constant.strExpectedResult =strExpectedResult;
		Constant.strActualResult =strActualResult;
		Constant.TestCaseNumber=Constant.TestCaseNumber+1;
				
	}
	
	@Test
	public static void TestScriptEnds() throws Exception{
		String strXlsFileName = Constant.ResultFilePath;

        int strData1,strData2, stepStatus, strDiff;

        strData1 = Constant.StepIndex;

			strData2 = Constant.TestStepIndex;
			
			stepStatus = Constant.StepStatus;
			
			strDiff= strData1-strData2;
			
			try {
			
			FileInputStream ExcelFile= new FileInputStream(strXlsFileName);
			
			        workbook = new XSSFWorkbook(ExcelFile);
			
			        Worksheet =  workbook.getSheet("Results");
			
			        Row row = Worksheet.getRow(strDiff); 
			
			        CellStyle style = workbook.createCellStyle();
			
			        for(int i=0;i<8;i++) {
			                Cell cell =row.getCell(i);
			                if(stepStatus==0) {                                                          
			                    style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
			                    style.setAlignment(HorizontalAlignment.CENTER);
			                    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			                    cell.setCellStyle(style);
			                }
			                else{                                                             
			                    style.setFillForegroundColor(IndexedColors.RED.getIndex());
			                    style.setAlignment(HorizontalAlignment.CENTER);
			                    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);          
			                    cell.setCellStyle(style);
			                }
			        }
			
			        
			        Cell cell =row.getCell(7);
		        	if(stepStatus==0) {
		                cell.setCellValue("Pass");
				        cell=row.createCell(10);
					    DateTimeFormatter dtf= DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
					    cell.setCellValue(LocalDateTime.now().format(dtf).toString()); 
//					    cell=row.createCell(12);
//					    cell.setCellValue(Control.checkbal("*143*2*2*1#")); 
		                }
		                else
		                {
		                    cell.setCellValue("Fail");
					        cell=row.createCell(10);
						    DateTimeFormatter dtf= DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
						    cell.setCellValue(LocalDateTime.now().format(dtf).toString()); 
//						    cell=row.createCell(12);
//						    cell.setCellValue(Control.checkbal("*143*2*2*1#")); 
		                }
		        
			        	
				    cell = row.createCell(8);
			        if(stepStatus==0) {
			        	
			        }
			        else
			        {
			        	
			        	cell.setCellValue(Constant.ScreenshotFolderName);
			            final Hyperlink href = workbook.getCreationHelper().createHyperlink(HyperlinkType.FILE);
			            String FolderPath= "File:///"+ Constant.ScreenshotFolderName;
			            FolderPath= FolderPath.replace("\\", "/");
			            System.out.println(FolderPath);
			            href.setAddress(FolderPath);
			            cell.setHyperlink(href);
			        }
			        

			        
			        //Constant.StepStatus=0;
					int strData = Constant.StepIndex;
					strData1=strData+1;
					Constant.StepIndex=strData1 ;
					strData =Constant.StepIndex;
					strData2 = Constant.TestCaseIndex;
					int strData3=strData2+1;
					Constant.TestCaseIndex=strData3;     
					ExcelFile.close();                             
					FileOutputStream fos = new FileOutputStream(strXlsFileName);                
					workbook.write(fos);                    
					workbook.close();
					fos.close();
					
					if(!(Constant.StepStatus==0))
					{
						//Assert.fail("\n" + Constant.Failures);	
	        			//softAssertions.fail("\n" + Constant.Failures);
						Constant.softAssertions.fail("Failure in the test case");
					}
					
			}catch (Exception e) {
			
			        System.out.println(e.getMessage());
			}
}
	
	@Test
	public static void MultipleTestScriptEnds() throws Exception{
		String strXlsFileName = Constant.ResultFilePath;

        int strData1,strData2, stepStatus, strDiff;

        strData1 = Constant.StepIndex;

			strData2 = Constant.TestStepIndex;
			
			stepStatus = Constant.StepStatus;
			
			strDiff= strData1-strData2;
			
			try {
			
			FileInputStream ExcelFile= new FileInputStream(strXlsFileName);
			
			        workbook = new XSSFWorkbook(ExcelFile);
			
			        Worksheet =  workbook.getSheet("Results");
			
			        Row row = Worksheet.getRow(strDiff); 
			
			        CellStyle style = workbook.createCellStyle();
			
			        for(int i=0;i<8;i++) {
			                Cell cell =row.getCell(i);
			                if(stepStatus==0) {                                                          
			                    style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
			                    style.setAlignment(HorizontalAlignment.CENTER);
			                    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			                    cell.setCellStyle(style);
			                }
			                else{                                                             
			                    style.setFillForegroundColor(IndexedColors.RED.getIndex());
			                    style.setAlignment(HorizontalAlignment.CENTER);
			                    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);          
			                    cell.setCellStyle(style);
			                }
			        }
			
			        
			        Cell cell =row.getCell(7);
		        	if(stepStatus==0) {
		                cell.setCellValue("Pass");
				        cell=row.createCell(10);
					    DateTimeFormatter dtf= DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
					    cell.setCellValue(LocalDateTime.now().format(dtf).toString()); 
//					    cell=row.createCell(12);
//					    cell.setCellValue(Control.checkbal("*143*2*2*1#")); 
		                }
		                else
		                {
		                    cell.setCellValue("Fail");
					        cell=row.createCell(10);
						    DateTimeFormatter dtf= DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
						    cell.setCellValue(LocalDateTime.now().format(dtf).toString()); 
//						    cell=row.createCell(12);
//						    cell.setCellValue(Control.checkbal("*143*2*2*1#")); 
		                }
		        
			        	
				    cell = row.createCell(8);
			        if(stepStatus==0) {
			        	
			        }
			        else
			        {
			        	
			        	cell.setCellValue(Constant.ScreenshotFolderName);
			            final Hyperlink href = workbook.getCreationHelper().createHyperlink(HyperlinkType.FILE);
			            String FolderPath= "File:///"+ Constant.ScreenshotFolderName;
			            FolderPath= FolderPath.replace("\\", "/");
			            System.out.println(FolderPath);
			            href.setAddress(FolderPath);
			            cell.setHyperlink(href);
			        }
			        

			        
			        //Constant.StepStatus=0;
					int strData = Constant.StepIndex;
					strData1=strData+1;
					Constant.StepIndex=strData1 ;
					strData =Constant.StepIndex;
					strData2 = Constant.TestCaseIndex;
					int strData3=strData2+1;
					Constant.TestCaseIndex=strData3;     
					ExcelFile.close();                             
					FileOutputStream fos = new FileOutputStream(strXlsFileName);                
					workbook.write(fos);                    
					workbook.close();
					fos.close();
					
					if(!(Constant.Failures==""))
					{
						//Assert.fail("\n" + Constant.Failures);	
						Assert.fail("\n"+Constant.Failures);
						Constant.Failures = "";

					}
					
			}catch (Exception e) {
			
			        System.out.println(e.getMessage());
			}
}

	
	
	
	public static String RandomString(int intvalue) throws Exception{
		char c[]= {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
		int randomPosition;
		String RandomString= "";
		for(int i=0;i<intvalue;i++) {
			randomPosition=generateRandomIntIntRange(0,51);
			RandomString=RandomString+c[randomPosition];
		}
		System.out.println(RandomString);
		return RandomString;		
	}
	
	public static int generateRandomIntIntRange(int min, int max) {
	    Random r = new Random();
	    return r.nextInt((max - min) + 1) + min;
	}
	public static void GenerateHTMLReport() {
		String strData,strTestCaseNo,strTestDesc,strExpectedResult,strActualResult,strTestCaseStatus;
		int strTotalCases;
		try {
			FileInputStream ExcelFile= new FileInputStream(Constant.ResultFilePath);
			String HtmlFile = Constant.ResultFilePath.replace("xlsx","htm");
			workbook = new XSSFWorkbook(ExcelFile);
			Worksheet =  workbook.getSheet("Results");		
			int totalrows=Worksheet.getLastRowNum();
			//File file1 = new File(HtmlFileName);
			//FileWriter fw = new FileWriter(file1);
			//StringBuilder htmlStringBuilder=new StringBuilder();
			//File HtmlFileName = new File(HtmlFile);
			
			StringBuilder htmlStringBuilder=new StringBuilder();
	        htmlStringBuilder.append("<HTML><BODY><TABLE BORDER=0 CELLPADDING=3 CELLSPACING=1 WIDTH=100%>");
	        htmlStringBuilder.append("<TR COLS=2><TD BGCOLOR=WHITE WIDTH=6%><IMG SRC='https://myaccountpreprod.globe.com.ph/etc/designs/multi-solutions/assets/img/icon/loglobe.png'></TD><TD WIDTH=94% BGCOLOR=WHITE><FONT FACE=VERDANA COLOR=NAVY SIZE=2><B>&nbsp;OMNI-My Account Automation - " + "objDate.toString()" + " -  & Time &  on Machine  & Environment.Value(LocalHostName) & </B></FONT></TD></TR></TABLE>");
	        htmlStringBuilder.append("<TABLE BORDER=1 BGCOLOR=BLACK CELLPADDING=3 CELLSPACING=1 WIDTH=100%>");
	        htmlStringBuilder.append("<TR><TD BGCOLOR=#66699 WIDTH=25%><FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>Business Process Name:  </B></FONT></TD><TD COLSPAN=6 BGCOLOR=#66699><FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>"+Constant.UserStoryName +"</B></FONT></TD></TR>");
	        htmlStringBuilder.append("<TR COLS=6><TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Test Case No</B></FONT></TD><TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Test Case Description</B></FONT></TD><TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Expected Result</B></FONT></TD><TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Actual Result</B></FONT></TD><TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Result</B></FONT></TD></TR>");
	        File HtmlFileName = new File(HtmlFile);
	        
			for(int i=1;i<totalrows;i++) {
				strData = getcelldata(i,0);
				if(!strData.equals("")){
					strTestCaseNo = getcelldata(i,0);
					strTestDesc = getcelldata(i,2);
					strExpectedResult = getcelldata(i,5);
					strActualResult = getcelldata(i,6);
					strTestCaseStatus = getcelldata(i,7);
					if(strTestCaseStatus.equals("Fail")) {
						Constant.FailedCases =Constant.FailedCases+1;
						htmlStringBuilder.append("<TR COLS=6><TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>"+strTestCaseNo+"</B></FONT></TD><TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>"+strTestDesc+"</B></FONT></TD><TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>"+strExpectedResult+"</B></FONT></TD><TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>"+strActualResult+"</B></FONT></TD><TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><a href=ScreenShots><B>"+strTestCaseStatus+"</B></FONT></a> </TD></TR>");
						
					}
					if(strTestCaseStatus.equals("Pass")) {
						Constant.PassedCases =Constant.PassedCases+1;
				
						htmlStringBuilder.append("<TR COLS=6><TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>"+strTestCaseNo+"</B></FONT></TD><TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>"+strTestDesc+"</B></FONT></TD><TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>"+strExpectedResult+"</B></FONT></TD><TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>"+strActualResult+"</B></FONT></TD><TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><a href=ScreenShots><B>"+strTestCaseStatus+"</B></FONT></a> </TD></TR>");
					}					
			        
			        /*FileOutputStream outputStream = new FileOutputStream(HtmlFile);
			        Writer writer=new OutputStreamWriter(outputStream);
			        writer.append(htmlStringBuilder.toString());
			        writer.close();*/
			   }
			}
			strTotalCases= Constant.PassedCases + Constant.FailedCases;
			 htmlStringBuilder.append("<TR COLS=6><TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Total Test Cases: "+strTotalCases+"</B></FONT></TD><TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>No. Of Test Cases Passed: "+Constant.PassedCases+"</B></FONT></TD><TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>No. Of Test Cases Failed: "+Constant.FailedCases+"</B></FONT></TD><TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B></B></FONT></TD><TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B></B></FONT></TD></TR>");
			FileOutputStream outputStream = new FileOutputStream(HtmlFileName);
	        Writer writer=new OutputStreamWriter(outputStream);
	        writer.write(htmlStringBuilder.toString());
	        writer.close();
	        /*strTotalCases= Constant.PassedCases + Constant.FailedCases;
			strTotalCases= Constant.PassedCases + Constant.FailedCases;
 	        htmlStringBuilder.append("<TR COLS=6><TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Total Test Cases: "+strTotalCases+"</B></FONT></TD><TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>No. Of Test Cases Passed: "+Constant.PassedCases+"</B></FONT></TD><TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>No. Of Test Cases Failed: "+Constant.FailedCases+"</B></FONT></TD><TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B></B></FONT></TD><TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B></B></FONT></TD></TR>");
 	       FileOutputStream outputStream = new FileOutputStream(HtmlFile);
	        Writer writer=new OutputStreamWriter(outputStream);
	        writer.append(htmlStringBuilder.toString());
	        writer.close();*/
			
		}
		catch (Exception e){
			e.printStackTrace();
			
		}
	}
	
	
	public static void setScreenshothyperlink(String screenshotFilePath) throws Exception{			
			int strData=Constant.StepIndex;   			
				try {				
						FileInputStream ExcelFile= new FileInputStream(Constant.ResultFilePath);
				        workbook = new XSSFWorkbook(ExcelFile);
				        Worksheet =  workbook.getSheet("Results");
						Row row = Worksheet.getRow(strData); 		
						Cell cell = row.createCell(8);
						cell.setCellValue(screenshotFilePath);
						final Hyperlink href = workbook.getCreationHelper().createHyperlink(HyperlinkType.FILE);
						String FolderPath= "File:///"+ screenshotFilePath;
						FolderPath= FolderPath.replace("\\", "/");			        
						href.setAddress(FolderPath);
						cell.setHyperlink(href);				
						ExcelFile.close();		
						FileOutputStream fos = new FileOutputStream(Constant.ResultFilePath);		
						workbook.write(fos);		
						workbook.close();
						fos.close();
			}
			catch(Exception e) {	
			    System.out.println(e.getMessage());
						}
				}
	public static void TestDataForUSSD(String SheetName) throws Exception
	{
		String  strValue,ColumnName,ColumnValue;
		int totalrows,totalColumns;
		try {
			FileInputStream ExcelFile= new FileInputStream(Constant.TestDataFilePath);
			workbook = new XSSFWorkbook(ExcelFile);
			Worksheet =  workbook.getSheet(SheetName);
			totalrows = Worksheet.getLastRowNum();
			totalColumns= Worksheet.getRow(0).getLastCellNum();
			for(int i=0; i<=totalrows;i++) {	
				Constant.Map2.put("TestCase"+ i , new HashMap<String,String>());
				for(int j=0; j <= totalColumns; j++){
					ColumnName= getcelldata(0,j);
						
						ColumnValue = getcelldata(i,j);		
						//System.out.println("Sheet : "+SheetName+"(Row,Col) : ("+i+","+j+"):"+ColumnValue);
						Constant.Map2.get("TestCase"+ i).put(ColumnName, ColumnValue);
						
				}
				
			}				
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	  public void GenerateReport(String path_to_python_scripts,String path_to_input_excel,String path_to_output_pdf,String input_excel_name,String output_pdf_name)
	    {
	    	String python_command=path_to_python_scripts+"python.exe RP3.py "+path_to_python_scripts+" "+input_excel_name+" "+path_to_input_excel+" "+path_to_output_pdf+" "+output_pdf_name;
	                   try
	                    {
	                	   
	                	   
	                        path_to_python_scripts=URLDecoder.decode(path_to_python_scripts, "UTF-8");
	                        System.out.println("Path to python script : "+path_to_python_scripts);
	                        python_command=URLDecoder.decode(python_command, "UTF-8");
	                         System.out.println("Python command: "+python_command);
	                    }
	                catch(Exception e)
	                    {
	                            System.out.println("Exception while setting Python Path : "+e.getMessage());
	                            System.exit(-1);
	                    }
	        
	        
	                ProcessBuilder builder = new ProcessBuilder(
	                "cmd.exe", "/c", "cd "+path_to_python_scripts+" && "+python_command);
	                builder.redirectErrorStream(true);
	                try
	                {
	                							String line=null;
	                                            Process p = builder.start();
	                                            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
	                                            System.out.println("Buffered Reader : "+r.readLine());

	                                            while (true) 
	                                            {
	                                                try
	                                                {
	                                                    line =r.readLine();
	                                                    if(line==null)
	                                                        break;

	                                                    System.out.println(""+line);
	                                                }

	                                                catch(Exception e)
	                                                {
	                                                    System.out.println("Execption while receiving output from Python Script : "+e.getMessage());
	                                                    break;
	                                                }
	                                            }

	                    

	                }
	                catch(Exception e)
	                {
	                    System.out.println("Exception while triggering process to initialize PDF Generation Script  : "+e.getMessage());
	                }    
	    }
	  
	  
	  

	  public static void startTimer()
	  {
		  Constant.updatedStartTime=System.currentTimeMillis();
		  
		  
	  }
	  public static void stopTimer()
	  {
		  //the moment a timer is stopped, final time becomes initial time, and elapsedDuration increases
		  
		  //calculate the elapsed duration
		  Constant.youtubeElapsedTime+=System.currentTimeMillis()-Constant.updatedStartTime;
		  
	  }
	  
	  public static void resetTimer()
	  {
		  Constant.youtubeElapsedTime=0;
	  }
	  
	  
	  public static float LetsSurfYoutube(int durationForYoutubeVideo)
	  {
		  //handle duration in milliseconds
		  //7 hrs video
		  int timer=0;
		  String videoLink="https://www.youtube.com/watch?v=Mh3M5P4VjYg";
		  durationForYoutubeVideo*=1000;
		 
		  String commandYoutube="adb shell am start -a android.intent.action.VIEW \""+videoLink+"\"";
		  String clearPrevLogs="logcat -c";
		  String videoStatus="adb shell \"logcat -d -e onPlaybackStateChanged | tail -1\"";
		  String currentVideoStatus="";
		  int videoTimeout=30;
		  
		  System.out.println("videoStatusCommand : "+videoStatus);
		  
		  //Control.RunProcessWithOutput(clearPrevLogs, 30, false);
		  
		  
		  //start youtube
		  Control.RunProcessWithOutput(commandYoutube, 20, false);
		  
		  //wait for 45 seconds for the video to start
		  while(true)
		  {
			  //Control.RunProcessWithOutput(clearPrevLogs, 30, false);
			  currentVideoStatus=Control.RunProcessWithOutput(videoStatus, 20, true);
			  
			  //if video is playing,start timer and break from this loop
			  if(currentVideoStatus.contains("com.google.android.youtube STATE_PLAYING"))
			  {
				  startTimer();
				  System.out.println("Video started");
				  break;
			  }
				  
			  
			  else if(timer>videoTimeout)
			  {
				  //video could not start for a certain duration, exit
				  System.out.println("Video could not start, exiting with Result Code -1.");
				  return -1;
			  }
			  
			  else
			  {
				  timer+=1;
				  try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  continue;
			  }
		  }
		  
		  //video has already started, need to play it for a certain duration
		  timer=0;
		  System.out.println("Video is Running...");
		  while(true)
		  {
			  //continuously monitor video status, the moment state is not playing then pause
			  //if it remains in non-playing mode for more than videoTimeout, exit
			  
			  //Control.RunProcessWithOutput(clearPrevLogs, 30, false);
			  currentVideoStatus=Control.RunProcessWithOutput(videoStatus, 20, true);
			  
			  while(!(currentVideoStatus.contains("vol.MediaSessions: onPlaybackStateChanged com.google.android.youtube STATE_PLAYING PlaybackState")))
			  {
				  System.out.println("Video has stopped..");
				  timer+=1;
				  try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				  
				  if(timer>videoTimeout) //video started but stopped
					  return 0.25f;
				  
				  if(Constant.timerRunning)
					  stopTimer();
				  Constant.timerRunning=false;
				  
				  
				  currentVideoStatus=Control.RunProcessWithOutput(videoStatus, 20, true);
			  }
			  
			  System.out.println("Video is running..");
			  if(!Constant.timerRunning)
				  startTimer();
			  
			  if(Constant.youtubeElapsedTime>=durationForYoutubeVideo)
			  {
				  System.out.println("Video ran successfully..");
				  return 1;//video completed successfully
			  }
			  
			  
			  
			  
			  
		  }
		  

		  
	  }
	  
	  
	  public static void tapOnYoutubeVideo()
	  {
		  Control.RunProcessWithOutput("adb shell input tap "+Constant.youtubeX+" "+Constant.youtubeY, 20, false);
	  }
	  
	  public static long getYoutubeElapsedDuration() throws Exception
	  {
		  
		  WebElement durationElapsed=null;
		  String rawPassedDuration="";
		  WebElement screen=null;
		  WebElement tempWebElem=null;
		 
		  tapOnYoutubeVideo();
		  if(Constant.youtubeScreen!=null)
		  {
			 
			  //now parse the duration elapsed

			  try
			  {
				  
				  int ctr=-1;
				  while(true)
				  {
					  ++ctr;
					  System.out.println("In function : getYoutubeElapsedDuration : Iteration : "+ctr);
					  
					  try
					  {
						  System.out.println("In try block");
						  //screen=Generic.GetElementWithTimeOut("YoutubeVideo", "videoScreen",5000);
						  
						  if((tempWebElem=Control.findElement("YoutubeVideo", "videoElapsedDuration"))!=null)
						  {
							  rawPassedDuration=tempWebElem.getText();
						  }
						  
						  if(rawPassedDuration.contains(":") || ctr>=3)
							  break;
						  
						  
						tapOnYoutubeVideo();
						    
						if(Generic.FindWithTimeOut("YoutubeVideo", "videoElapsedDuration",2000))
							  rawPassedDuration=Generic.GetElementWithTimeOut("YoutubeVideo", "videoElapsedDuration",2000).getText();
						  
						  System.out.println("Try block : rawPassedDuration : "+rawPassedDuration);
					  }
					  catch(Exception e)
					  {
						  System.out.println("In catch block");
						  if(Control.findElement("YoutubeVideo", "videoElapsedDuration")==null)
							  Control.RunProcessWithOutput("adb shell input tap "+Constant.youtubeX+" "+Constant.youtubeY, 20, false);
						  
						  rawPassedDuration=Generic.GetElementWithTimeOut("YoutubeVideo", "videoElapsedDuration",2000).getText();
						  System.out.println("Catch block : rawPassedDuration : "+rawPassedDuration);
					  }
				  }
				 
				  
				  if(rawPassedDuration.contains(":"))
				  {
					  String[] temp=rawPassedDuration.split(":");
					  int len=temp.length;
					  if(len==2)
					  {
				  			int minutes=Integer.parseInt(temp[0]);
				  			int seconds=Integer.parseInt(temp[1]);
				  			
				  			long durationElapsedInSeconds=(minutes*60)+seconds;
				  			return durationElapsedInSeconds;
				  			
				  			
					  }
			  else if(len==3)
					  {
				  			int hours=Integer.parseInt(temp[0]);
				  			int minutes=Integer.parseInt(temp[1]);
				  			int seconds=Integer.parseInt(temp[2]);
				  			
				  			long durationElapsedInSeconds=(hours*60*60)+(minutes*60)+seconds;
				  			return durationElapsedInSeconds;
					  }
				  }
			  else
	  			{
				  		return -1;
		  		}  
				  

				  
			  }
			  catch(Exception e)
			  {
				  System.out.println("Error while parsing youtube elasped duration : "+e.getMessage());
				  e.printStackTrace();
			  }
			  
			  
			  
		  }
		  else
		  {
			  tapOnYoutubeVideo();
			  System.out.println("Flag 2 : Could not locate the Youtube video Screen");
		  }
		  
		 
		  
		  return -1;
		  
	  }
	  
	  
	  public static long getYoutubeInitialDuration() throws Exception
	  {
		  System.out.println("In function : getYoutubeInitialDuration");
		  WebElement durationElapsed=null;
		  
		  
		  
		  if(Constant.youtubeScreen!=null)
		  {
			 //System.out.println("Located the screen on which the video is running..");
			  //now parse the duration elapsed
			  String rawPassedDuration="";
			  WebElement screen=null;
			  try
			  {
				  
				  int ctr=-1;
				  while(!rawPassedDuration.contains(":") && ++ctr<2)
				  {
					  
					  try
					  {
						  
						  //screen=Generic.GetElementWithTimeOut("YoutubeVideo", "videoScreen",5000);
						  if(Control.findElement("YoutubeVideo", "videoElapsedDuration")==null)
						  {
							  tapOnYoutubeVideo();
						  }
							  	
						  
						  rawPassedDuration=Generic.GetElementWithTimeOut("YoutubeVideo", "videoElapsedDuration",2000).getText().toString();
						  
						  System.out.println("Try block : rawPassedDuration : "+rawPassedDuration);
					  }
					  catch(Exception e)
					  {
						  
						  if(Control.findElement("YoutubeVideo", "videoElapsedDuration")==null)
						  { 
							  Control.RunProcessWithOutput("adb shell input tap "+Constant.youtubeX+" "+Constant.youtubeY, 20, false);
							  //Control.RunProcessWithOutput("adb shell input tap "+Constant.youtubeX+" "+Constant.youtubeY, 20, false);
							  
							 
							  
						  }
						  
						  rawPassedDuration=Generic.GetElementWithTimeOut("YoutubeVideo", "videoElapsedDuration",2000).getText().toString();
						  System.out.println("Catch block : rawPassedDuration : "+rawPassedDuration);
					  }
				  }
				 
				  
				  if(rawPassedDuration.contains(":"))
				  {
					  String[] temp=rawPassedDuration.split(":");
					  if(temp.length==2)
					  {
						  	
				  			int minutes=Integer.parseInt(temp[0]);
				  			int seconds=Integer.parseInt(rawPassedDuration.split(":")[1]);
				  			
				  			long durationElapsedInSeconds=(minutes*60)+seconds;
				  			return durationElapsedInSeconds;
				  			
				  			
					  }
			  else if(temp.length==3)
					  {
				  			int hours=Integer.parseInt(rawPassedDuration.split(":")[0]);
				  			int minutes=Integer.parseInt(rawPassedDuration.split(":")[1]);
				  			int seconds=Integer.parseInt(rawPassedDuration.split(":")[2]);
				  			
				  			long durationElapsedInSeconds=(hours*60*60)+(minutes*60)+seconds;
				  			return durationElapsedInSeconds;
					  }
				  }
			  else
	  			{
				  		return -1;
		  		}  
				  

				  
			  }
			  catch(Exception e)
			  {
				  System.out.println("Error while parsing youtube elasped duration : "+e.getMessage());
				  e.printStackTrace();
			  }
			  
			  
			  
		  }
		  else
		  {
			  
			  System.out.println("Flag 1: Could not locate the Youtube video Screen");
		  }
		  
		 
		  
		  return -1;
		  
	  }
	  
	  
	  
	  public static void closeYoutube()
	  {
		  String command="adb shell am force-stop com.google.android.youtube";
		  
		  int count=0;
		  while(true)
		  {
			  Control.RunProcessWithOutput(command, 20, false);

			  
			  if(++count>5)
			  {
				  break;
			  }
			  
			  if(!Generic.checkProcessResultCode())
			  {
				  continue;
			  }
			  
		  }
		  
		  
	  }
	  

	  public static boolean youtubePlayButtonClick(WebElement dummy) throws Exception
	  {
		  int xPlayButton=-1,yplayButton=-1;
		  WebElement playButton=null;
		  int ctr=-1;
		  while(++ctr<5)
		  {
			  Control.RunProcessWithOutput("adb shell input tap "+Constant.youtubeX+" "+Constant.youtubeY, 20, false);
			  if((playButton=Generic.GetElementWithTimeOut("YoutubeVideo", "play",2500))!=null)
				{
					  xPlayButton=playButton.getLocation().getX();
					  yplayButton=playButton.getLocation().getY();
					  break;
				}
			  
		  }

		  if(xPlayButton!=-1 && yplayButton!=-1)
		  {
			  Control.RunProcessWithOutput("adb shell input tap "+xPlayButton+" "+yplayButton, 20, false);
			  if((playButton=Generic.GetElementWithTimeOut("YoutubeVideo", "play",2500))!=null)
			  {
				 try
				 {
					 playButton.click();
				 }
				 catch(Exception e)
				 {
					 Control.RunProcessWithOutput("adb shell input tap "+xPlayButton+" "+yplayButton, 20, false);
					 (Generic.GetElementWithTimeOut("YoutubeVideo", "play",2500)).click();
				 }
				  
				  return Generic.checkProcessResultCode();
				  //Control.RunProcessWithOutput("adb shell input tap "+xPlayButton+" "+yplayButton, 20, false);
				  
			  }
			  else
			  {
				  Control.RunProcessWithOutput("adb shell input tap "+xPlayButton+" "+yplayButton, 20, false);
				  if(Generic.FindWithTimeOut("YoutubeVideo", "videoElapsedDuration",2500))
				  {
						 try
						 {
							 playButton.click();
						 }
						 catch(Exception e)
						 {
							 Control.RunProcessWithOutput("adb shell input tap "+xPlayButton+" "+yplayButton, 20, false);
							 (Generic.GetElementWithTimeOut("YoutubeVideo", "play",2500)).click();
						 }
					  //playButton.click();
					  //Control.RunProcessWithOutput("adb shell input tap "+xPlayButton+" "+yplayButton, 20, false);
					  //return Generic.checkProcessResultCode();
				  }
				  
			  }
		  }

		  return false;

	  }
	  
	  public static long videoYoutube(int durationForYoutubeVideo) throws Exception
	  {
		  //handle duration in seconds
		  //exits if video does not move for videoTimeout seconds
		  
		  //return values
		  // 1 : All good : Pass
		  //0.5 : Not for specified duration but for sometime
		  //-1 : Not at all
		  
		  int videoTimeout=30;
		  int timer=0;
		  //WebElement videoScreen=null;
		  
		//7 hrs video
		  String videoLink="https://www.youtube.com/watch?v=Mh3M5P4VjYg";
		  String commandYoutube="adb shell am start -a android.intent.action.VIEW \""+videoLink+"\"";
		  long videoStartTime=0;
		  
		  
		  try
		  {
			  System.out.println("Flag 0 : Closing youtube");
			  closeYoutube();
		  }
		  catch(Exception e)
		  {
			  System.out.println("Ignorable Exception : Making sure youtube is closed before starting Youtube : "+e.getMessage());
		  }

	  /*
	   * **************************************************************
	   * 		Launch and play the video
	   * **************************************************************
	   */
		  
		  Control.RunProcessWithOutput(commandYoutube, 7, false);
		
		  System.out.println("Youtube has been launched");
	  
	  /*
	   * **************************************************************
	   * 		first fetch the screen co-ordinates
	   * **************************************************************
	   */
		  
		  int ctr=-1;
		  WebElement screenElem=null;
		  while(++ctr<1)
		  {
			  try
			  {
				 if((screenElem=Generic.GetElementWithTimeOut("YoutubeVideo", "videoScreen", 15000))!=null)
				 {
					 //videoScreen=
					 //Constant.youtubeScreen=Control.findElement("YoutubeVideo", "videoScreen");
					 System.out.println("Video screen found");
					 Constant.youtubeScreen=screenElem;
					 Constant.youtubeX=Constant.youtubeScreen.getLocation().getX();
					 Constant.youtubeY=Constant.youtubeScreen.getLocation().getY();
					 
					  Generic.WriteTestData("Youtube Screen located at :"+Constant.youtubeX+","+Constant.youtubeY, "", "", "Youtube Screen located", "Yotube Screen located", "Pass");
					  Control.takeScreenshot();
					 break;
					
					 
				 }
				 else
				 {
					 
					 System.out.println("Unable to locate youtube screen hence closing");
					 Generic.WriteTestData("Unable to locate youtube screen", "", "", "Locate Youtube Screen", "Yotube Screen NOT located", "Fail");
					 closeYoutube();
					 return 0;
				 }

			  }
			  catch(Exception e)
			  {
				  	 e.printStackTrace();
					 Generic.WriteTestData("Unable to locate youtube screen", "", "", "Locate Youtube Screen", "Yotube Screen NOT located", "Fail");
					 closeYoutube();
					 return 0;
			  }
			  

			  
		  }
		  
		  

		  

		  /*
		   * *************************************************************************************
		   * 							check for Ads/Video Start for 10 seconds
		   * *************************************************************************************
		   */

		  
		  System.out.println("Lets check for Ads");
		  
		  //max 30 seconds here
		  ctr=-1;
		  int ctrMain=-1;
		  
		  while(++ctrMain<30)
		  {
			//will break only if Skip Ad appears or the actual video starts
			  
			  WebElement adDuration=null;
			  /*if((adDuration=Control.findElement("YoutubeVideo", "adDuration"))!=null)
			  {
				  System.out.println("Ad duration : "+adDuration.getText());
			  }*/
			  
			  //condition for skipAd
			  if(Control.findElement("YoutubeVideo", "adSkip")!=null)
			  {
				  try
				  {
					  Control.click("YoutubeVideo", "adSkip");
					  break;
				  }
				  catch(Exception e)
				  {
					  System.out.println("Exception while skipping ads");
					  e.printStackTrace();
				  }
			  }
				  
			  
			  //condition for videoStart
			  if(Control.findElement("YoutubeVideo", "videoElapsedDuration")!=null)
			  {
				  
				  System.out.println("Fetching initial start time");
				  videoStartTime=0;
				  break;	
			  }
			  
			  tapOnYoutubeVideo();
			  
			  if(Generic.FindWithTimeOut("YoutubeVideo", "videoElapsedDuration",2000))
			  {
				  
				  System.out.println("Fetching initial start time");
				  videoStartTime=0;
				  break;	
			  }
	  
			  
			  Thread.sleep(1000);
				  
			  
			  
		  }
		  
		  
		  System.out.println("Done checking for Ads");
		  
  /*
   * *************************************************************************************
   * 				fetched the initial video duration, ads have been skipped 
   * *************************************************************************************
   */

		  

		  boolean initialVideoTimeRecorded=false;
		  int flag=0;
		  int ctr1=-1;
		  
		  System.out.println("Video start time fetched : "+videoStartTime);
		  
		 /* if(videoStartTime==-1)
		  {
			  	//should never be the case
			 System.out.println("FATAL : Still not able to capture video duration, will make one last try");
			 videoStartTime=Generic.getYoutubeElapsedDuration();
			 if(videoStartTime==-1)
			 {
				 Generic.WriteTestData("Unable to capture video duration", "", "", "Should capture video duration", "Unable to capture video duration", "Fail");
				 closeYoutube();
				 return 0;
			 }
				 
		  }
		  else
		  {
			  initialVideoTimeRecorded=true;
			  flag=1;
		  }
		  

		  
		  /*
		   * *************************************************************************************
		   * 		making double sure that the video screen is in place and running 
		   * *************************************************************************************
		   */
		  
		/*  WebElement playButton=null;
		  
		  ctr1=-1;
		  if(flag!=0)
		  {
			  while(++ctr1<3)
			  {
				  System.out.println("Running "+ctr1+"/3 iterations to find video Elapsed Duration" );
				  
				  if(Control.findElement("YoutubeVideo", "videoElapsedDuration")!=null)
				  {
					  
					  flag=1;
					  
					  //record only once the initial video start time
					  if(!initialVideoTimeRecorded)
					  {
						  videoStartTime=getYoutubeElapsedDuration();
						  initialVideoTimeRecorded=true;
						  
						  Generic.WriteTestData("Capturing Start time", "", "", "Capture the start time", "Captured the start time", "Pass");
						  Control.takeScreenshot();
					  }
						  
					  String playButtonValue="";
					  //playPausedVideo();

					  
					  break;
				  }

				  else
				  {
					  Control.RunProcessWithOutput("adb shell input tap "+Constant.youtubeX+" "+Constant.youtubeY, 20, false);
					  
					  if(Generic.FindWithTimeOut("YoutubeVideo", "videoElapsedDuration",2500))
					  {
						  flag=1;
						  if(!initialVideoTimeRecorded)
						  {
							  videoStartTime=getYoutubeElapsedDuration();
							  initialVideoTimeRecorded=true;
							  Generic.WriteTestData("Capturing Start time", "", "", "Capture the start time", "Captured the start time", "Pass");
							  Control.takeScreenshot();
						  }
						
						  break;
					  }
				  }
				  //Thread.sleep(500);
				  
			  }
		  }
		  

		  
		 System.out.println("Video Elapsed duration found after : "+ctr1+" iterations\nVideo start time : "+videoStartTime); 
		  
		 */
		 
		  /*
		   * *************************************************************************************
		   * 		If still the video player was not located, simply exit
		   * *************************************************************************************
		   */		 
		  	

		/*  if(flag==0)
		  {
			  return -1;
		  } */
		  
		  
		  /*
		   * *************************************************************************************
		   * 		Last lag : Actual tracking starts here
		   * *************************************************************************************
		   */		 
		  	
		  
		  System.out.println("Last lag : Actual tracking happens here");
		  //Generic.WriteTestData("Youtube Video Playing", "", "", "Video shall play", "Video is playing", "Pass");
		  //Control.takeScreenshot();
		  long myVideoDuration=0;
		  long currentVideoDuration=0;
		  long tempVideoDuration=0;
		  //check elapsed duration
		  //getYoutubeElapsedDuration
		  while(true)
		  {
			  System.out.println(" *********** Tracking usage ***********");
			  currentVideoDuration=getYoutubeElapsedDuration();
			  
			  if(currentVideoDuration==-1)
			  {
				  System.out.println("Unable to get duration");
				  return -1;
			  }
			  
			  
			  System.out.println("Video duration is : "+currentVideoDuration);
			  if(currentVideoDuration-videoStartTime>=durationForYoutubeVideo)
			  {
				  
				Generic.WriteTestData("Exiting Youtube as required duration has been met", "Duration", ""+durationForYoutubeVideo, "Youtube required duration met", "Youtube required duration met", "Pass");
				Control.takeScreenshot();
				System.out.println("Exiting youtube as required browsing time : "+durationForYoutubeVideo+" seconds has exceeded.");
				closeYoutube();

				System.out.println("Video ran from : "+videoStartTime+" until "+currentVideoDuration);
				System.out.print("Closing application...");
				Thread.sleep(2000);
				if(!Generic.FindWithTimeOut("YoutubeVideo", "videoScreen", 500))
						{
						  Generic.WriteTestData("Youtube closed", "", "", "Youtube should be closed", "Youtube was closed", "Pass");
						  
						  Control.takeScreenshot();
						}
				else
				{
					 Generic.WriteTestData("Unable to close Youtube", "", "", "Youtube should be closed", "Unable to close Youtube", "Fail");
					  
				}

				  int x=1/0;
				  return 1;
			  }
			  
			  else
			  {
				  //if the video has not moved for the past videoTimeout seconds, exit
				  timer=0;
				  
				  while(currentVideoDuration==(myVideoDuration=getYoutubeElapsedDuration()) && myVideoDuration!=-1)
				  {
					  
					  //playPausedVideo();
					  ++timer;
					  Thread.sleep(1000);
					  if(timer>videoTimeout)
					  {
						  System.out.println("Exiting as video is not moving since last "+videoTimeout+" seconds");
							System.out.println("Video ran from : "+videoStartTime+" until "+currentVideoDuration);
						  System.out.print("Closing application...");
						  
						  Generic.WriteTestData("Closing Youtube as the video is not playing", "", "", "Video shall play", "Video is not playing", "Fail");
						  
						  
						  
							closeYoutube();
						  return (long) 0.5; //video could not progress
					  }
					  System.out.println("Video is not moving");
						  
				  }
				  
			  }
			  
			  if(myVideoDuration>currentVideoDuration)
				 currentVideoDuration=myVideoDuration;
			  
			  if(currentVideoDuration-videoStartTime<durationForYoutubeVideo)
			  {
				  System.out.println("Video is moving : "+currentVideoDuration);
			  }
			  
		  }
		  


	  }
	  
	  public static boolean playPausedVideo()
	  {
		  
		  //if the video is paused, play it
		  int ctr=-1;
		  String playButtonValue="";
		  WebElement playButton=null;
		  while(++ctr<10)
		  {
			  try {
				if((playButton=Generic.GetElementWithTimeOut("YoutubeVideo", "videoElapsedDuration",2000))!=null)
				  {

						  try
						  {
							  playButtonValue=(Generic.GetElementWithTimeOut("YoutubeVideo", "play",2000)).getAttribute("name");
							  if(playButtonValue.contains("Play"))
							  {
								  //implies video is paused and needs to be played
								  youtubePlayButtonClick(playButton);

								  return true;
								  
							  }
							  
							  
						  }
						  catch(Exception e)
						  {
							  //tap again and check the content
							  if(Control.findElement("YoutubeVideo", "play")==null)
							  {
								  Control.RunProcessWithOutput("adb shell input tap "+Constant.youtubeX+" "+Constant.youtubeY, 20, false);
								  try {
									playButtonValue=(Generic.GetElementWithTimeOut("YoutubeVideo", "play",3000)).getAttribute("name");
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								  if(playButtonValue.contains("Play"))
								  {
									  //implies video is paused and needs to be played
									  try {
										youtubePlayButtonClick(playButton);
									} catch (Exception e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									  /*if(!initialVideoTimeRecorded)
									  {
										  videoStartTime=getYoutubeElapsedDuration();
										  initialVideoTimeRecorded=true;
									  }*/
									  return true;
									  
								  }
							  }
							  else
							  {
								  try {
									playButtonValue=(Generic.GetElementWithTimeOut("YoutubeVideo", "play",3000)).getAttribute("name");
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								  if(playButtonValue.contains("Play"))
								  {
									  //implies video is paused and needs to be played
									  try {
										youtubePlayButtonClick(playButton);
									} catch (Exception e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									  /*if(!initialVideoTimeRecorded)
									  {
										  videoStartTime=getYoutubeElapsedDuration();
										  initialVideoTimeRecorded=true;
									  }*/
									  return true;
									  
								  }
								  
							  }
							
						  }

					  

				  }
				  else
					  Control.RunProcessWithOutput("adb shell input tap "+Constant.youtubeX+" "+Constant.youtubeY, 20, false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  
		  }
		  /*if((playButton=Generic.GetElementWithTimeOut("YoutubeVideo", "play",5000))!=null)
		  {
			  try
			  {
				  playButtonValue=(Generic.GetElementWithTimeOut("YoutubeVideo", "play",2000)).getAttribute("name");
				  if(playButtonValue.contains("Play"))
				  {
					  //implies video is paused and needs to be played
					  youtubePlayButtonClick(playButton);
					  
				  }
				  
				  
			  }
			  catch(Exception e)
			  {
				  //tap again and check the content
				  Control.RunProcessWithOutput("adb shell input tap "+Constant.youtubeX+" "+Constant.youtubeY, 20, false);
				  playButtonValue=(Generic.GetElementWithTimeOut("YoutubeVideo", "play",3000)).getAttribute("name");
				  if(playButtonValue.contains("Play"))
				  {
					  //implies video is paused and needs to be played
					  youtubePlayButtonClick(playButton);
					  
				  }
			  }

		  }*/
		return false;

	  }
	  
	  public static boolean SurfYoutube(String videoTitle,int duration) throws Exception
		{
		  Control.closeFlashMsg();
			//duration needs be in Seconds only
			duration*=1000;
			int counter=0;
			//Control.LaunchMobileApp("",Generic.ReadFromExcel("DeviceDetails","AI_TestData",1),Generic.ReadFromExcel("DeviceDetails","AI_TestData",2), "Android",Generic.ReadFromExcel("DeviceDetails","AI_TestData",3), Generic.ReadFromExcel("DeviceDetails","AI_TestData",4),Generic.ReadFromExcel("DeviceDetails","AI_TestData",5) , "4723","");
			//Control.LaunchMobileApp("",Generic.ReadFromExcel("DeviceDetails","AI_TestData",1),Generic.ReadFromExcel("DeviceDetails","AI_TestData",2), "Android",Generic.ReadFromExcel("YoutubeAppPackageName","ObjectRepository",1), Generic.ReadFromExcel("YoutubeAppActivityName","ObjectRepository",1),Generic.ReadFromExcel("DeviceDetails","AI_TestData",5) , "4723","");
			
			
			System.out.println("Launching youtube..");
			Control.LaunchMobileApp("",Generic.ReadFromExcel("DeviceDetails","AI_TestData",1),Generic.ReadFromExcel("DeviceDetails","AI_TestData",2), "Android",Generic.ReadFromExcel("YoutubeAppActivityName","ObjectRepository",1), Generic.ReadFromExcel("YoutubeAppPackageName","ObjectRepository",1),Generic.ReadFromExcel("DeviceDetails","AI_TestData",5) , "4723","");
			
			System.out.println("Clicking on Youtube Home Icon..");
			if(Control.objExists("Youtube", "HomeIcon", true))
			{
				Control.click("Youtube", "HomeIcon");
				WebElement x=Control.findElement("Youtube", "HomeIcon");
				

			}
					
			else
			{
				
				System.out.println("Youtube application - Could not find Home Button");
				return false;
			}

			System.out.println("Clicking on Youtube Search Icon..");
			if(Control.objExists("Youtube", "SearchIcon", true))
				Control.click("Youtube", "SearchIcon");
			else
			{
				System.out.println("Youtube application - Could not find Search Icon");
				return false;
			}

			System.out.println("Entering text in Search Box..");
			Control.enterText("Youtube", "SearchBox", "Ind vs Aus 30 mins\n");
			
			counter=0;
			while(counter<30000)
			{
				System.out.println("Trying to find element : Filter Row");
				if(Control.findElement("Youtube","FilterRow")!=null)
				{
					try
					{
						Control.click("Youtube","FilterRow");
					}
					catch(Exception e)
					{
						Control.click("Youtube","FilterRow");
					}
					
					break;
				}
				Thread.sleep(1000);
				counter+=1000;
			}

			counter=0;
			while(counter<15000)
			{
				if(Control.findElement("Youtube","FilterDurationAny")!=null)
				{
					try
					{
						Control.click("Youtube","FilterDurationAny");
						break;
					}
						
					catch(Exception e)
					{
						Control.click("Youtube","FilterDurationAny");
						break;
					}
						
				}
				Thread.sleep(1000);
				counter+=1000;
			}
			
			counter=0;
			while(counter<15000)
			{
				if(Control.findElement("Youtube","FilterDurationLongVideo")!=null)
				{
					try
					{
						Control.click("Youtube","FilterDurationLongVideo");
						break;
					}
						
					catch(Exception e)
					{
						Control.click("Youtube","FilterDurationLongVideo");
						break;
					}
						
				}
				Thread.sleep(1000);
				counter+=1000;
			}
			
		
			Thread.sleep(1000);
			
			Control.click("Youtube","FilterApply");
			
			counter=0;
			
			while(counter<30000)
			{
				if(Control.findElement("Youtube", "FirstVideoAfterSearch")!=null)
				{
					break;
				}
				
				Thread.sleep(1000);
				counter+=1000;
			}

			
			try
			{
				Control.click("Youtube", "FirstVideoAfterSearch");
			}
			catch(Exception e)
			{
				Control.click("Youtube", "FirstVideoAfterSearch");
			}

			

			Thread.sleep(duration);
			
			
			System.out.print("Closing application...");
			((AppiumDriver)Constant.driver).closeApp();
			return true;
		}
	  
	  
	  	public static boolean FindAndClick(String elem1,String elem2,int timeOut) throws Exception
	  	{
	  		
			int counter=0;
			while(counter<=timeOut)
			{
				if(Control.findElement(elem1,elem2)!=null)
				{
					try
					{
						Control.click(elem1,elem2);
					}
					catch(Exception e)
					{
						Control.click(elem1,elem2);
					}
					
					return true;
				}
				Thread.sleep(1000);
				counter+=1000;
			}
			return false;
	  	}
		
	  	
	  	public static boolean FindWithTimeOut(String elem1,String elem2,int timeOut) throws Exception
	  	{
	  		if(timeOut<100)
	  		{
	  			timeOut*=1000;
	  		}
	  		
			int counter=0;
			while(counter<=timeOut)
			{
				if(Control.findElement(elem1,elem2)!=null)
				{
					
					return true;
				}
				Thread.sleep(1000);
				counter+=1000;
			}
			return false;
	  	}
	  	
	  	
	  	
	  	
	  	public static WebElement GetElementWithTimeOut(String elem1,String elem2,int timeOut) throws Exception
	  	{
	  		
	  		return Control.findElement(elem1, elem2);
	  	}
	  	
	  	
	  	public static boolean checkProcessResultCode()
	  	{
	  		String ResultCode="-1";
	  		try
	  		{
	  			 ResultCode=Control.RunProcessWithOutput("echo %errorlevel%", 10, true).trim();
	  			 //System.out.println("Result code from Shell : "+ResultCode);
	  		}
	  		catch(Exception e)
	  		{
	  			System.out.println("Exception while checking ErrorLevel");
	  			e.printStackTrace();
	  			return false;
	  		}
	  		
	  		if(ResultCode.equals("0"))
	  				{
	  					return true;
	  				}
	  		return false;
	  	}
	  	
	  	public static boolean sendSMS(String msgContent,String BNumber) throws Exception
	  	{	
	  		Constant.alreadyWaited=false;
	  		Constant.lastSmsOrUssdTime=System.currentTimeMillis();
	  		String ResultCode="";
	  		
	  		System.out.println("Message to send : "+BNumber);
	  		
	  		 String enterRecipient="adb shell am start -a android.intent.action.SENDTO -d sms:"+BNumber;
	  		 String enterMessageBody="adb shell input text \""+"'"+msgContent+"'"+"\"";
	  		 String pressTAB="adb shell input keyevent KEYCODE_TAB";
	  		 String pressEnter="adb shell input keyevent 66";
	  		 
	  		 //System.out.println("Command for entering msg is : "+enterMessageBody);


	  		 //System.out.println("Entering Recepient : "+BNumber);
	  		 
	  		 ProcessBuilder builderEnterNumber = new ProcessBuilder(
	  	      "cmd.exe", "/c", enterRecipient);
	  	    
	  	      builderEnterNumber.start().waitFor(10,TimeUnit.SECONDS);
	  	      
	  	    if(checkProcessResultCode())
	  	    {
	  	    	System.out.println("Writing to Excel");
	  	    	Generic.WriteTestData("Enter Recepient", "", BNumber, "Should enter Recepient", "Able to enter Recepient", "Pass");
	 	  	   	Thread.sleep(1000);
	  	    	Control.takeScreenshot();
	  	    }
	  	    	
	  	    else
	  	    {
	  	    	System.out.println("Writing to Excel");
	  	    	Generic.WriteTestData("Enter Recepient", "", BNumber, "Should enter Recepient", "Unable to enter Recepient", "Fail");
			  	   
	  	    }
	  	    	
	  	    	
	  	    
	  	    System.out.println("Entering Msg Content : "+msgContent);
	  	    
	  		 ProcessBuilder builderEnterMsg = new ProcessBuilder(
	  		  	      "cmd.exe", "/c", enterMessageBody);
	  		  	    
	  		 builderEnterMsg.start().waitFor(10,TimeUnit.SECONDS);
	  		 
		  	    if(checkProcessResultCode())
		  	    {
		  	    	
		  	    	Generic.WriteTestData("Enter Msg Body", "", BNumber, "Should enter Message", "Able to enter Message", "Pass");
		  	    	Thread.sleep(1000);
		  	    	Control.takeScreenshot();
		  	    }
		  	    	
		  	    else
		  	    {
		  	    	System.out.println("Writing to Excel");
		  	    	Generic.WriteTestData("Enter Msg Body", "", BNumber, "Should enter Message", "Unable to enter Message", "Fail");
				  	   
		  	    }
		  	    	
		  	  
		  	    
		  	 if(Generic.FindAndClick("SamsungMessage", "messageSendButton", 15000)==true)
		  	 {
		  		Generic.WriteTestData("Click Send Button", "", BNumber, "Should be able to click Send", "Able to enter Send", "Pass");
		  		Control.takeScreenshot();
		  	 }
		  	 else
		  	 {
		  		 //not able to Send Message
		  		Generic.WriteTestData("Click Send Button", "", BNumber, "Should be able to click Send", "Unable to click Send", "Fail");
			  	return false;
		  	 }

	  		
	  		return true;
			
	  	}
	  	
	  	public static boolean checkIfBalanceDeducted(String lastBalance) throws Exception
	  	{
	  		Float curBalance=Float.parseFloat(Control.checkbal("*143*2*2*1#"));
	  		if(Float.parseFloat(lastBalance)>curBalance)
	  			return true;
	  		else
	  			return false;
	  	}
	  	
	  	public static float CallToBNumber(String BNumber, int durationOfCall) throws Exception
		{
	  		  
	  		  String line="";
	  		  long time1=0, time2=0;
	  		  
	  		  String makeVoiceCall="adb shell am start -a android.intent.action.CALL -d tel:\""+BNumber+"\"";
	  		  String checkCallStatus="(for /F `delims=` %G in ('adb shell dumpsys telecom ^| findstr SET_') do @set `lastoccur=%G`)&set lastoccur".replace("`", "\"");
	  		  String disconnectCall="adb shell input keyevent KEYCODE_ENDCALL";
	  		  Boolean callConnected=true;
	  		  float status=0;
	  		  int ctr=0;
	  		  
	  		  Float callDuration=0f;
	  		  
	  	      ProcessBuilder builderCall = new ProcessBuilder(
	  	      "cmd.exe", "/c", makeVoiceCall);
	  	      builderCall.redirectErrorStream(true);
	  	      Process p = builderCall.start();
	  	      p.waitFor(10,TimeUnit.SECONDS);
	  	      
	  	      
	  	      
	  	      
	  	      
	  	      ProcessBuilder builder = new ProcessBuilder(
	  	      "cmd.exe", "/c", checkCallStatus);
	  	      builder.redirectErrorStream(true);
	  	      Process p1 = null;
	  	      
	  	      ProcessBuilder builderEndCall = new ProcessBuilder(
	  	      "cmd.exe", "/c", disconnectCall);
	  	      builderEndCall.redirectErrorStream(true);
	  	      //Process pEndCall = null;
	  	      

	  	     
	  	      String tempStr="";
	  	      //System.out.println("Buffered Reader : "+r.readLine());

	  	      while (true) 
	  	      {
	  	    	  
	  	          try
	  	          {
	  	        	  line="";
	  	        	  p1=builder.start();
	  	        	  p1.waitFor(10,TimeUnit.SECONDS);
	  	        	  BufferedReader r = new BufferedReader(new InputStreamReader(p1.getInputStream()));
	  	        	  while((tempStr=r.readLine())!=null)
	  	        	  {
	  	        		  line+=tempStr;
	  	        	  }
	  	        	  
	  	        	  if(line.contains("SET_DIALING"))
	  	        	  {
	  	        		Generic.WriteTestData("Dialing Number", "", BNumber, "Should be able to dial", "Able to dial", "Pass");
	  	        		Control.takeScreenshot();
					  	
	  	        		  System.out.println("Dialing..");
	  	        		  status=0.5f;
	  	        	  }

	  	        	  
	  	        	  else if(line.contains("SET_ACTIVE"))
	  	        	  {
	  	        		if(callConnected)
	  	        		{
		  	        		Generic.WriteTestData("Call Connected", "", BNumber, "Should be able to connect", "Able to connect", "Pass");
		  	        		Control.takeScreenshot();
	  	        			time1 = new Date().getTime(); //milliseconds
	  	        			callConnected=false;
	  	        			status=0.75f;
	  	        		}
	  	        			
	  	        		System.out.println("Ongoing Call..");
	  	        		//can handle here for Ongoing Call
	  	        		//start the timer
	  	        		
	  	        		 
	  	        		 callDuration=(float) ((new Date().getTime()-time1)/1000);
	  	        		 System.out.println("callDuration is "+callDuration);
	  	        		 if(callDuration>=durationOfCall)
	  	        		 {
	  	        			status=1;
	 	  	        		Generic.WriteTestData("Call Successful", "Duration", ""+callDuration, "Call continued for the expected duration", "Call should continue for expected duration", "Pass");
	 	  	        		Control.takeScreenshot();
	  	        			builderEndCall.start().waitFor(10,TimeUnit.SECONDS);
	  	        			System.out.println("Disconnecting as Call Duration : "+callDuration+" is >= Input Duration : "+durationOfCall+" seconds");
	  	        			break;
	  	        		 }
	  	        		 
	  	        		//System.out.print(Seconds.secondsBetween(dt1, dt2).getSeconds() % 60 + " seconds.");
	  	        		  
	  	        		  
	  	        	  }
	  	        	  
	  	        	  else
	  	        	  {
	  	        		//call was disconnected
	  	        		  //before declaring the call as dead, lets try for 10 seconds to connect the call
	  	        		  
	  	        		  if(ctr<11 && status==0)
	  	        		  {
	  	        			  Thread.sleep(1000);
	  	        			  ctr+=1;
	  	        			  continue;
	  	        		  }
	  	        		  
	  	        		  System.out.println("Pre-exit with status : "+status);
	  	        		  return status;
	  	        		
	  	        		 
	  	        	  }

		  	        	 

	  	          }

	  	          catch(Exception e)
	  	          {
	  	              System.out.println("Exception while reading SMS from Backend "+e.getMessage());
	  	              e.printStackTrace();
	  	              //break;
	  	          }
	  	          
	  	          

	  	      }
	  	    System.out.println("Returning status : "+status);
	  	    if(status!=1)
	  	    	Generic.WriteTestData("Call Unsucessful", "status", ""+status, "Call should be sucessful", "Call was unsucessful", "Fail");
     		
	  	    return status;
	  	      
	  	  

	  		
	  		
		}
	  	
	  	
	  	public static void replyOnLastMessage()
	  	{
	  		
	  	}
	  	
		public static float CallToBNumberFrontEnd(String BNumber, String DurationMISS) throws Exception
		{
			//if 0 is returned the entire function failed
			//if 0.5 is returned, a call was attempted from source device, but not picked up by target device
			//if 0.75 is returned, the call got connected, but was dropped before specified duration
			//if 1 is returned the function passed, a call was attempted and answered by target device
			
			//Expected format of duration is MI:SS for e.g. "015:15" for 15 minutes and 15 seconds of call
			
			//Limitation - for now, the max duration of call allowed is 59:59 (59 mins, 59 seconds), can be handled with additional coding
			
			//lets parse the duration
			//int call_hours=Integer.parseInt(DurationMISS.split(":")[0]);
			
			System.out.println("Making a call to "+BNumber+"for "+DurationMISS);
			int call_minutes=Integer.parseInt(DurationMISS.split(":")[0]);
			int call_seconds=Integer.parseInt(DurationMISS.split(":")[1]);
			String storeCallDuration="0";
			String storeCallDuration1="0";
			
			
			Control.LaunchMobileApp("",Generic.ReadFromExcel("DeviceDetails","AI_TestData",1),Generic.ReadFromExcel("DeviceDetails","AI_TestData",2), "Android",Generic.ReadFromExcel("DialerAppPackageName","ObjectRepository",1), Generic.ReadFromExcel("DialerAppActivityName","ObjectRepository",1),Generic.ReadFromExcel("DeviceDetails","AI_TestData",5) , "4723","");
			
			
			
			//Control.click("DialerApp", "DialButton");
			
			Generic.FindAndClick("DialerApp", "DialButton", 20000);
			Generic.FindAndClick("DialerApp", "Dialer",20000);
			Control.enterText("DialerApp", "Dialer", BNumber);
			Generic.FindAndClick("DialerApp", "btnDialler",20000);
			
			int counter=0;
			float status_ctr=0;
			
			
			/*if(Generic.FindWithTimeOut("DialerApp", "SamsungDialingLabel", 5000))
			{
				status_ctr+=0.5;
				System.out.println("Identified Samsung Dialing Label, status ctr is "+status_ctr);

			}*/
			
			try
			{
				WebElement timeElapsed=Generic.GetElementWithTimeOut("DialerApp", "SamsungElapsedTime",10000);
				//while((Integer.parseInt(((timeElapsed).getText().split(":")[1]))<=call_seconds) && (Integer.parseInt(((timeElapsed).getText().split(":")[0]))<=call_minutes))
				if(timeElapsed==null)
				{
					return 0;
				}
				while(true)
				{
					storeCallDuration=timeElapsed.getText();
					int callMinutes=Integer.parseInt(storeCallDuration.split(":")[0]);
					int callSeconds=Integer.parseInt(storeCallDuration.split(":")[1]);
					storeCallDuration1=storeCallDuration;
					if(callMinutes>=call_minutes && callSeconds>=call_seconds)
					{
						break;
					}
						
					System.out.println("Call Duration is : "+storeCallDuration);
					//Thread.sleep(900);
					
				}
			}
			catch(Exception e)
			{
				if(storeCallDuration1.contains(":"))
				{
					if (Integer.parseInt(storeCallDuration1.split(":")[1])>=call_seconds && Integer.parseInt(storeCallDuration1.split(":")[1])>=call_minutes)
						status_ctr=1;
					else 
						status_ctr=0.75f;
				}

				
				
				System.out.println("Encountered exception while calculating elasped time, status ctr is "+status_ctr);
				System.out.println("Call Duration is : "+storeCallDuration1);
				e.printStackTrace();
				return status_ctr;
			}

			
			if (Integer.parseInt(storeCallDuration1.split(":")[1])>=call_seconds && Integer.parseInt(storeCallDuration1.split(":")[1])>=call_minutes)
				status_ctr=1;
			else
				status_ctr=0.75f;
			
			System.out.println("Checking if the call was successful, status ctr is "+status_ctr);
			//lets disconnect call
			//SamsungEndButton
			Control.click("DialerApp", "SamsungEndButton");

			System.out.println("Returning status ctr is "+status_ctr);
			return status_ctr;
			
		
			}

		
		
		 
		 public static boolean turnOnMobileData(boolean flag)
		 {
			 int ctr=0;
			 
			 while(ctr<Constant.retryOnFailureCount)
			 {
				 
				 try
				 {
					 ProcessBuilder builderCall=null;
					 if(flag)
					 {
					      builderCall = new ProcessBuilder(
					    	      "cmd.exe", "/c", "adb shell svc data enable");
					 }
					 else
					 {
					      builderCall = new ProcessBuilder(
					    	      "cmd.exe", "/c", "adb shell svc data disable");
					 }

				    	      builderCall.redirectErrorStream(true);
				    	      builderCall.start().waitFor(10,TimeUnit.SECONDS);
				    	      
				    
				       if(Generic.checkProcessResultCode())
				       {
				    	   if(flag)
				    	   {
				    		   Generic.WriteTestData("Turned on Mobile Data","Mobile Data","Mobile Data","Should be able to turn ON mobile data","Was able to turn on mobile data","Pass");
					    	   
				    	   }
				    		   
				    	   else
				    		   Generic.WriteTestData("Turned off Mobile Data","Mobile Data","Mobile Data","Should be able to turn Off mobile data","Was able to turn off mobile data","Pass");
				    	   
				    	   Control.takeScreenshot();
				    	   
				    	   return true;
				       }
				    	   
				 
				 }
				 
				 catch(Exception e)
				 {
					 if(flag)
						 Generic.WriteTestData("Exception while turning on Mobile Data","Mobile Data","Mobile Data","Should be able to turn ON mobile data","Was able to turn on mobile data","Fail");   
					 else
						 Generic.WriteTestData("Exception while turning off Mobile Data","Mobile Data","Mobile Data","Should be able to turn Off mobile data","Was NOT able to turn off mobile data","Fail");
			    	   

					 System.out.println("Exception while turning Off Mobile Data : "+e.getMessage());
					 e.printStackTrace();
				 }
				 
				 finally
				 {
					ctr+=1; 
				 }
				 
			 }
			 return false;

		 }
		 
		 
		 public static boolean isInternetConnected()
		 {
			 System.out.println("Checking if internet is connected");
			 int ctr=-1;
			 while(++ctr<2)
			 {
				 
				 String strCheckInternetConnection=Control.RunProcessWithOutputInternet("adb shell ping www.google.com", 20, true);
				 System.out.println("Result of Internet connectivity : "+strCheckInternetConnection);
				 if(strCheckInternetConnection.contains("bytes from"))
				 {
					 System.out.println("Yes,internet is connected, returning true");
					 return true;
				 }
				 //restart mobile data
				 System.out.println("Restarting mobile data and retrying!");
				 Generic.turnOnMobileData(false);
				 Generic.turnOnMobileData(true);
				 try
				 {
					 Thread.sleep(2000);
				 }
				 catch(Exception e)
				 {
					 
				 }
				 
					 
			 }
			 
			 System.out.println("No, internet is not connected, returning False");
			 return false;
		 }
		 
		 
		 public static boolean turnOnWifi(boolean flag)
		 {
			 int ctr=0;
			 
			 while(ctr<Constant.retryOnFailureCount)
			 {
				 ProcessBuilder builderCall =null;
				 try
				 {
					 if(flag)
					 {
					      builderCall = new ProcessBuilder(
					    	      "cmd.exe", "/c", "adb shell svc wifi enable");
					 }
					 else
					 {
					      builderCall = new ProcessBuilder(
					    	      "cmd.exe", "/c", "adb shell svc wifi disable");
					 }

				    	      builderCall.redirectErrorStream(true);
				    	      builderCall.start().waitFor(10,TimeUnit.SECONDS);
				    	      
				    
				       if(Generic.checkProcessResultCode())
				    	   return true;
				 
				 }
				 
				 catch(Exception e)
				 {
					 System.out.println("Exception while turning Off Wifi: "+e.getMessage());
					 e.printStackTrace();
				 }
				 
				 finally
				 {
					ctr+=1; 
				 }
				 
			 }
			 return false;

		 }
		 
		 
		 public static boolean turnOnMobileData()
		 {
			 int ctr=0;
			 
			 while(ctr<Constant.retryOnFailureCount)
			 {
				 
				 try
				 {
				      ProcessBuilder builderCall = new ProcessBuilder(
				    	      "cmd.exe", "/c", "adb shell svc data enable");
				    	      builderCall.redirectErrorStream(true);
				    	      builderCall.start().waitFor(10,TimeUnit.SECONDS);
				    	      
				    
				       if(Generic.checkProcessResultCode())
				    	   return true;
				 
				 }
				 
				 catch(Exception e)
				 {
					 System.out.println("Exception while turning Off Mobile Data : "+e.getMessage());
					 e.printStackTrace();
				 }
				 
				 finally
				 {
					ctr+=1; 
				 }
				 
			 }
			 return false;

		 }
		 
		 
		 

	  	  /**
	  	   * Calculates the similarity (a number within 0 and 1) between two strings.
	  	   */
	  	  public static double similarity(String s1, String s2) {
	  	    String longer = s1, shorter = s2;
	  	    if (s1.length() < s2.length()) { // longer should always have greater length
	  	      longer = s2; shorter = s1;
	  	    }
	  	    int longerLength = longer.length();
	  	    if (longerLength == 0) { return 1.0; /* both strings are zero length */ }
	  	    /* // If you have Apache Commons Text, you can use it to calculate the edit distance:
	  	    LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
	  	    return (longerLength - levenshteinDistance.apply(longer, shorter)) / (double) longerLength; */
	  	    return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

	  	  }


	  	  public static int editDistance(String s1, String s2) {
	  	    s1 = s1.toLowerCase();
	  	    s2 = s2.toLowerCase();

	  	    int[] costs = new int[s2.length() + 1];
	  	    for (int i = 0; i <= s1.length(); i++) {
	  	      int lastValue = i;
	  	      for (int j = 0; j <= s2.length(); j++) {
	  	        if (i == 0)
	  	          costs[j] = j;
	  	        else {
	  	          if (j > 0) {
	  	            int newValue = costs[j - 1];
	  	            if (s1.charAt(i - 1) != s2.charAt(j - 1))
	  	              newValue = Math.min(Math.min(newValue, lastValue),
	  	                  costs[j]) + 1;
	  	            costs[j - 1] = lastValue;
	  	            lastValue = newValue;
	  	          }
	  	        }
	  	      }
	  	      if (i > 0)
	  	        costs[s2.length()] = lastValue;
	  	    }
	  	    return costs[s2.length()];
	  	  }

	  	  public static float printSimilarity(String s, String t) {
	  		  try
	  		  {
	  			String similarity1="";
	  			  try
	  			  {
	  				 similarity1=String.format(
			  		  	      "%.3f is the similarity between \"%s\" and \"%s\"", similarity(s, t), s, t);
	  				 
	  				 System.out.println("Similarity in string : "+similarity1);
	  				 
	  			  }
	  			  catch(Exception e)
	  			  {
	  				System.out.println("Exception in string calc : "+similarity1);  
	  			  }
	  			
	  			
	  			Constant.lastSmsPercentMatch=(float)Double.parseDouble(similarity1.split(" ")[0]);
	  			
	  			System.out.print("Calculating similarity : "+Constant.lastSmsPercentMatch);
	  			return Constant.lastSmsPercentMatch;
	  		  }
	  		  catch(Exception e)
	  		  {
	  			  System.out.print("Exception : "+e.getMessage());
	  			  e.printStackTrace();
	  			  return 0.0f;
	  		  }

	  	  }

	  	  /*public static void main(String[] args) {
	  	    printSimilarity("", "");
	  	    printSimilarity("1234567890", "1");
	  	    printSimilarity("1234567890", "123");
	  	    printSimilarity("1234567890", "1234567");
	  	    printSimilarity("1234567890", "1234567890");
	  	    printSimilarity("1234567890", "1234567980");
	  	    printSimilarity("47/2010", "472010");
	  	    printSimilarity("47/2010", "472011");
	  	    printSimilarity("47/2010", "AB.CDEF");
	  	    printSimilarity("47/2010", "4B.CDEFG");
	  	    printSimilarity("47/2010", "AB.CDEFG");
	  	    printSimilarity("The quick fox jumped", "The fox jumped");
	  	    printSimilarity("The quick fox jumped", "The fox");
	  	    printSimilarity("kitten", "sitting");
	  	  }*/

		 
		 public static String checkbalNew(String dummy) throws Exception {
				/*Control.LaunchMobileApp("", Generic.ReadFromExcel("DeviceDetails","AI_TestData",1),Generic.ReadFromExcel("DeviceDetails","AI_TestData",2), "Android", "com.simpler.dialer", "com.simpler.ui.activities.ContactsAppActivity", Generic.ReadFromExcel("DeviceDetails","AI_TestData",5), "4723","");
		   	 	Control.click("DialerApp", "DialPad");
		   	 	Control.enterText("DialerApp","Dialer","*143*2*2*1#");
				Control.click("DialerApp","btnDialler");
				Control.takeScreenshot();
				WebElement element1=null;*/
				//Thread.sleep(2000);
				
			
				int runCount=0;
				
				
		while(true)
		{
				
			runCount+=1;
			if(runCount>Constant.retryOnFailureCount)
				return null;
			
			
			
			String InputValue="*143*2*2*1#";
			String USSDCode=InputValue.replace("#", "%23");
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
					
				}
				
				
				while(element1.getText().contains("USSD code running"))
				{
					continue;
				}
				
				String text1 = element1.getText();
				
				
				String bala="";
				String[] freeText;
				try
				{
					 bala= (text1.split("is P")[1]).split(" valid")[0];
					 
					 try
					 {
						 freeText=(text1.split("with")[1]).split(" Free texts")[0].split(" ");
						 System.out.println("Free texts : "+freeText[freeText.length-1]);
						 Constant.numberOfFreeMessages=(int) Float.parseFloat((freeText[freeText.length-1]).trim());
					 }
					 catch(Exception e)
					 {
						 Constant.numberOfFreeMessages=-999;
						 if(runCount>=Constant.retryOnFailureCount)
								Generic.WriteTestData("Failed while parsing No. of free texts",""+Constant.numberOfFreeMessages,""+Constant.numberOfFreeMessages,"Should be able to parse number of free texts","Not able to parse number of free texts","Fail");
							
						 
						 System.out.println("Exception while parsing number of free messages : "+e.getMessage());
						 e.printStackTrace();
					 }
					 
					 
				}
				catch(Exception e)
				{
					System.out.println("Exception while Parsing User Balance : "+e.getMessage());
					
					if(runCount>=Constant.retryOnFailureCount)
						Generic.WriteTestData("Failed while parsing User Account Balance",text1,text1,"Should be able to parse User Balance","Not able to parse User Balance","Fail");
					
					
				}
				//String bala = bal[7].replaceAll("P", "");
				Generic.WriteTestData("Available balance", "", bala, "Available balance is "+bala, "Available balance is "+bala, "Pass");
				Control.takeScreenshot();
				Generic.WriteTestData("Free texts available", "", ""+Constant.numberOfFreeMessages, "No. of free texts : "+Constant.numberOfFreeMessages, "No. of free texts : "+Constant.numberOfFreeMessages, "Pass");
				
				//Thread.sleep(4000);
				Generic.FindAndClick("SamsungMessage", "ok", 10000);
				return bala;
			}
		 
		 }
		 
		 public static String ReadRandomCellData(String sheetName) {
				String celldata = null;
				try {
					FileInputStream ExcelFile = new FileInputStream(Constant.TestDataFilePath);
					workbook = new XSSFWorkbook(ExcelFile);
					Worksheet = workbook.getSheet(sheetName);
					int totalrows = Worksheet.getLastRowNum();
					Random r = new Random();
					int rownum = r.nextInt(totalrows);
					try {
						cell = Worksheet.getRow(rownum).getCell(0);

						celldata = cell.getStringCellValue();

					} catch (Exception e) {
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return celldata;
			}
		 
			public static void RepositoryLabelCreation() throws Exception {
				String strText, PageName, strValue;
				int totalrows;
				try {
					FileInputStream ExcelFile = new FileInputStream(Constant.TestDataFilePath);
					workbook = new XSSFWorkbook(ExcelFile);
					Worksheet = workbook.getSheet("Label");
					totalrows = Worksheet.getLastRowNum();
					PageName = null;
					for (int j = 0; j <= totalrows; j++) {
						strValue = getcelldata(j, 0);
						//System.out.println("strValue is " + strValue);
						if (strValue.equalsIgnoreCase("")) {
						} else {
							PageName = strValue;
							Constant.labelMap.put(PageName, new HashMap<String, String>());
						}
						strText = getcelldata(j, 1);
						strValue = getcelldata(j, 2);
						//System.out.println(strText + " : " + strValue);
						Constant.labelMap.get(PageName).put(strText, strValue);
						strText = null;

					}

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		 
		
		
}

