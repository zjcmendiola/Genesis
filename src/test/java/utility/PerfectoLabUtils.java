package utility;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.CustomField;
import com.perfecto.reportium.model.Job;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;

import okhttp3.internal.platform.Platform;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PerfectoLabUtils {
	private static final String HTTPS = "https://";
	private static final String MEDIA_REPOSITORY ="/services/repositories/media/";
	private static final String UPLOAD_OPERATION = "operation=upload&overwrite=true";
	private static final String UTF_8 = "UTF-8";
	private static final String cloud1 = "&cloud=techm-globe-public-perfectomobile-com";
   
	private static final String VCS_URL_KEY = "vcs-url";
    private static final String VCS_COMMIT_KEY = "vcs-commit";
    private static final String VCS_FIELD_PREFIX = "perfecto.vcs.";
    private static final String VCS_URL_FIELD = VCS_FIELD_PREFIX + "repositoryUrl";
    private static final String VCS_COMMIT_FIELD = VCS_FIELD_PREFIX + "commit";
    private static final String VCS_FILE_PATH_FIELD = VCS_FIELD_PREFIX + "filePath";
    private static final String SOURCE_FILE_EXTENSION = ".java";

    
    /**
	 * fetches cloud name
	 * @param cloudName
	 * @return
	 * @throws Exception
	 */
	public static String fetchCloudName(String cloudName) throws Exception {
		//Verifies if cloudName is hardcoded, else loads from Maven properties 
		String finalCloudName = cloudName.equalsIgnoreCase("<<cloud name>>") ? System.getProperty("cloudName") : cloudName;
		//throw exceptions if cloudName isnt passed:
		if(finalCloudName == null || finalCloudName.equalsIgnoreCase("<<cloud name>>"))
			throw new Exception("Please replace <<cloud name>> with your perfecto cloud name (e.g. demo) or pass it as maven properties: -DcloudName=<<cloud name>>");
		else
			return finalCloudName;
	}

	/**
	 * Fetches security token
	 * @param securityToken
	 * @return
	 * @throws Exception
	 */
	public static String fetchSecurityToken(String securityToken) throws Exception {
		//Verifies if securityToken is hardcoded, else loads from Maven properties
		String finalSecurityToken = securityToken.equalsIgnoreCase("<<security token>>") ? System.getProperty("securityToken") : securityToken;
		//throw exceptions if securityToken isnt passed:
		if(finalSecurityToken == null || finalSecurityToken.equalsIgnoreCase("<<security token>>"))
			throw new Exception("Please replace <<security token>> with your perfecto security token or pass it as maven properties: -DsecurityToken=<<SECURITY TOKEN>>");
		else
			return finalSecurityToken;
	}

	/**
	 * Creates reportium client
	 * @param driver
	 * @param reportiumClient
	 * @return
	 * @throws Exception 
	 */
	public static ReportiumClient setReportiumClient(RemoteWebDriver driver, ReportiumClient reportiumClient) throws Exception {
		PerfectoExecutionContext perfectoExecutionContext;
		// Reporting client. For more details, see https://developers.perfectomobile.com/display/PD/Java
		if(System.getProperty("reportium-job-name") != null) {
			perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
					.withProject(new Project("My Project", "1.0"))
					.withJob(new Job(System.getProperty("reportium-job-name") , Integer.parseInt(System.getProperty("reportium-job-number"))))
					.withContextTags("tag1")
					.withWebDriver(driver)
					.build();
		} else {
			perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
					.withProject(new Project("My Project", "1.0"))
					.withContextTags("tag1")
					.withWebDriver(driver)
					.build();
		}
		reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);
		if (reportiumClient == null) {
			throw new Exception("Reportium client not created!");
		}
		return reportiumClient;
	}

	/**
	 * Asserts text
	 * @param WebElement
	 * @param reportiumClient
	 * @param text
	 */
	public static void assertText(WebElement element, ReportiumClient reportiumClient, String text) {
		String elementText = element.getText();
		if(reportiumClient != null)
			reportiumClient.reportiumAssert("Verify Field: " + elementText , elementText.equals(text));
		assert elementText.equals(text) : "Actual text : " + elementText + ". It did not match with expected text: " +  text;
	}
	
	/**
	 * Asserts contains text
	 * @param WebElement
	 * @param reportiumClient
	 * @param text
	 */
	public static void assertContainsText(WebElement element, ReportiumClient reportiumClient, String text) {
		String elementText = element.getText();
		if(reportiumClient != null)
			reportiumClient.reportiumAssert("Verify Field: " + elementText , elementText.contains(text));
		assert elementText.contains(text) : "Actual text : " + elementText + " does not contain the expected text: " +  text;
	}

	/**
	 * Assert title
	 * @param title
	 * @param reportiumClient
	 */
	public static void assertTitle(String title, ReportiumClient reportiumClient) {
		if (!title.equals("Web & Mobile App Testing | Continuous Testing | Perfecto")) {
			reportiumClient.reportiumAssert("Title is mismatched", false);
			throw new RuntimeException("Title is mismatched");
		}else {
			reportiumClient.reportiumAssert("Title is matching", true);
		}
	}

	public static String getDevicePhoneNumber(RemoteWebDriver driver){
		Map<String, Object> params1 = new HashMap<>();
		params1.put("property", "phoneNumber");
		return (String) driver.executeScript("mobile:handset:info", params1);
	}
	
	/**
	 * Download the report. 
	 * type - pdf, html, csv, xml
	 * Example: downloadReport(driver, "pdf", "C:\\test\\report");
     *
	 * Note that this method is relevant only for local hosted device lab (AKA "On Premise") and not for DigitalZoom (AKA ReportiumClient) users
	 */
	public static void downloadReport(RemoteWebDriver driver, String type, String fileName) throws IOException {
		try { 
			String command = "mobile:report:download"; 
			Map<String, Object> params = new HashMap<>(); 
			params.put("type", type); 
			String report = (String)driver.executeScript(command, params); 
			File reportFile = new File(fileName + "." + type); 
			BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(reportFile)); 
			byte[] reportBytes = OutputType.BYTES.convertFromBase64Png(report); 
			output.write(reportBytes);
            output.close();
		} catch (Exception ex) { 
			System.out.println("Got exception " + ex); }
	}

	/**
	 * Download all the report attachments with a certain type.
	 * type - video, image, vital, network
	 * Examples:
	 * downloadAttachment(driver, "video", "C:\\test\\report\\video", "flv");
	 * downloadAttachment(driver, "image", "C:\\test\\report\\images", "jpg");
     *
     * Note that this method is relevant only for local hosted device lab (AKA "On Premise") and not for DigitalZoom (AKA ReportiumClient) users
	 */
	public static void downloadAttachment(RemoteWebDriver driver, String type, String fileName, String suffix) throws IOException {
		try {
			String command = "mobile:report:attachment";
			boolean done = false;
			int index = 0;

			while (!done) {
				Map<String, Object> params = new HashMap<>();	

				params.put("type", type);
				params.put("index", Integer.toString(index));

				String attachment = (String)driver.executeScript(command, params);

				if (attachment == null) { 
					done = true; 
				}
				else { 
					File file = new File(fileName + index + "." + suffix); 
					BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file)); 
					byte[] bytes = OutputType.BYTES.convertFromBase64Png(attachment);	
					output.write(bytes); 
					output.close(); 
					index++; }
			}
		} catch (Exception ex) { 
			System.out.println("Got exception " + ex); 
		}
	}

	/**
	 * Uploads a file to the media repository.
	 * Example:
	 * URL url = new URL("http://file.appsapk.com/wp-content/uploads/downloads/Sudoku%20Free.apk");
	 * uploadMedia("demo", "securityToken", url, "PRIVATE:apps/ApiDemos.apk");
	 */
	
	public boolean isApplicationInstalled(String appName) {
        boolean retValue = false;
        String appList = null;
       
                Map<String, Object> param = new HashMap<>();
                param.put("format", "name");
                appList = (String)Constant.driver.executeScript("mobile:application:find", param);
              
        if (appList.indexOf(appName) > -1) {
            retValue = true;
        }
 
        return retValue;
    }
	public static void uploadMedia(String cloudName, String securityToken, URL mediaURL, String repositoryKey) throws IOException {
		byte[] content = readURL(mediaURL);
		uploadMedia(cloudName, securityToken, content, repositoryKey);
	}

	/**
	 * Uploads content to the media repository.
	 * Example:
	 * uploadMedia("demo", "securityToken", content, "PRIVATE:apps/ApiDemos.apk");
	 */
	public static void uploadMedia(String cloudName, String securityToken, byte[] content, String repositoryKey) throws UnsupportedEncodingException, MalformedURLException, IOException {
		if (content != null) {
			String encodedSecurityToken = URLEncoder.encode(securityToken, "UTF-8");
			String urlStr = HTTPS + cloudName + ".perfectomobile.com" + MEDIA_REPOSITORY + repositoryKey + "?" + UPLOAD_OPERATION + "&securityToken=" + encodedSecurityToken+cloud1;
			URL url = new URL(urlStr);
			
			System.out.println(url);
			
			sendRequest(content, url);
		}
	}
	
	public static void uploadMediaPrivate(String cloudName, String securityToken, byte[] content, String repositoryKey) throws UnsupportedEncodingException, MalformedURLException, IOException {
		if (content != null) {
			String encodedSecurityToken = URLEncoder.encode(securityToken, "UTF-8");
			String urlStr = HTTPS + cloudName + ".perfectomobile.com" + MEDIA_REPOSITORY + repositoryKey + "?" + UPLOAD_OPERATION + "&securityToken="+encodedSecurityToken;
			URL url = new URL(urlStr);
			
			System.out.println(url);
			
			sendRequest(content, url);
		}
	}

	private static void sendRequest(byte[] content, URL url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "application/octet-stream");
		connection.connect();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		outStream.write(content);
		outStream.writeTo(connection.getOutputStream());
		outStream.close();
		int code = connection.getResponseCode();
		if (code > HttpURLConnection.HTTP_OK) {
			handleError(connection);
		}
	}

	private static void handleError(HttpURLConnection connection) throws IOException {
		String msg = "Failed to upload media.";
		InputStream errorStream = connection.getErrorStream();
		if (errorStream != null) {
			InputStreamReader inputStreamReader = new InputStreamReader(errorStream, UTF_8);
			BufferedReader bufferReader = new BufferedReader(inputStreamReader);
			try {
				StringBuilder builder = new StringBuilder();
				String outputString;
				while ((outputString = bufferReader.readLine()) != null) {
					if (builder.length() != 0) {
						builder.append("\n");
					}
					builder.append(outputString);
				}
				String response = builder.toString();
				msg += "Response: " + response;
			}
			finally {
				bufferReader.close();
			}
		}
		throw new RuntimeException(msg);
	}

	private static byte[] readFile(File path) throws FileNotFoundException, IOException {
		int length = (int)path.length();
		byte[] content = new byte[length];
		InputStream inStream = new FileInputStream(path);
		try {
			inStream.read(content);
		}
		finally {
			inStream.close();
		}
		return content;
	}

	private static byte[] readURL(URL url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setDoOutput(true);
		int code = connection.getResponseCode();
		if (code > HttpURLConnection.HTTP_OK) {
			handleError(connection);
		}
		InputStream stream = connection.getInputStream();

		if (stream == null) {
			throw new RuntimeException("Failed to get content from url " + url + " - no response stream");
		}
		byte[] content = read(stream);
		return content;
	}

	private static byte[] read(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			byte[] buffer = new byte[1024];
			int nBytes = 0;
			while ((nBytes = input.read(buffer)) > 0) {
				output.write(buffer, 0, nBytes);
			}
			byte[] result = output.toByteArray();
			return result;
		} finally {
			try{
				input.close();
			} catch (IOException e){

			}
		}
	}
	
	/**
	 * Uploads a file to the media repository.
	 * Example:
	 * uploadMedia("demo", "securityToken", "C:\\test\\ApiDemos.apk", "PRIVATE:apps/ApiDemos.apk");
	 * @throws URISyntaxException 
	 */
	public static void uploadMedia_NewAPI(String cloudName, String securityToken, String path, String artifactLocator) throws URISyntaxException, ClientProtocolException, IOException {
			
		StopWatch stopwatch = new StopWatch();
		stopwatch.start();

		System.out.println("Upload Started");		  
		URIBuilder taskUriBuilder = new URIBuilder("https://" + cloudName + ".app.perfectomobile.com/repository");
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httppost = new HttpPost(taskUriBuilder.build());
		httppost.setHeader("Perfecto-Authorization", securityToken);
		
		MultipartEntityBuilder mpEntity = MultipartEntityBuilder.create();
		File packagedFile = new File(path);
		ContentBody inputStream = new FileBody(packagedFile, ContentType.APPLICATION_OCTET_STREAM);

		JSONObject req = new JSONObject();
		req.put("artifactLocator", artifactLocator);
		req.put("override", true);
		String rp = req.toString();

		ContentBody requestPart = new StringBody(rp, ContentType.APPLICATION_JSON);
		mpEntity.addPart("inputStream", inputStream);
		mpEntity.addPart("requestPart", requestPart);
		httppost.setEntity(mpEntity.build());
		HttpResponse response = httpClient.execute(httppost);
		int statusCode = response.getStatusLine().getStatusCode();

		stopwatch.stop();
		long x = stopwatch.getTime();
		System.out.println("Status Code = " + statusCode);
		System.out.println("Upload Time = " + Long.toString(x));
	}
	
	public static ReportiumClient createReportingClient(WebDriver driver) {
        CustomField teamCustomField = new CustomField("team", "devOps");
        CustomField departmentCustomField = new CustomField("department", "engineering");
        CustomField[] customFields = PerfectoLabUtils.addVcsFields("", teamCustomField, departmentCustomField);
        PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
                .withJob(new Job("qTest Integration Demo", 123).withBranch("GOMO_Automation"))
                .withProject(new Project("GOMO Selenium project", "1.0"))
                .withContextTags("SeleniumTests")
                .withCustomFields(customFields)
                .withWebDriver(driver)
                .build();
        return new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);
    }
	
   public static CustomField[] addVcsFields(String sourceFileRootPath, CustomField... userCustomFields) {
        CustomField[] vcsFields = createVcsFields(sourceFileRootPath);
        CustomField[] customFields = mergeCustomFields(vcsFields, userCustomFields);
        return customFields;
    }

    public static CustomField[] createVcsFields(String sourceFileRootPath) {
        String testClassName = getCallingClassName();
        CustomField[] vcsFields = createVcsFields(sourceFileRootPath, testClassName);
        return vcsFields;
    }

    public static CustomField[] createVcsFields(String sourceFileRootPath, String testClassName) {
        // Repository URL
        String repositoryUrl = System.getProperty(VCS_URL_KEY);
        if (repositoryUrl == null || repositoryUrl.isEmpty()) {
            // No source control integration without repository URL
            return new CustomField[0];
        }
        List<CustomField> customFields = new LinkedList<>();
        customFields.add(new CustomField(VCS_URL_FIELD, repositoryUrl));

        // Commit
        String commit = System.getProperty(VCS_COMMIT_KEY);
        if (commit != null && !commit.isEmpty()) {
            customFields.add(new CustomField(VCS_COMMIT_FIELD, commit));
        }

        // File path
        String relativeFilePath = testClassName.replace('.', '/');
        String fullFilePath = sourceFileRootPath + "/" + relativeFilePath + SOURCE_FILE_EXTENSION;
        customFields.add(new CustomField(VCS_FILE_PATH_FIELD, fullFilePath));

        CustomField[] vcsFields = customFields.toArray(new CustomField[0]);
        return vcsFields;
    }

    private static CustomField[] mergeCustomFields(CustomField[] vcsFields, CustomField... userCustomFields) {
        if (vcsFields == null || vcsFields.length == 0) {
            return userCustomFields;
        }
        if (userCustomFields.length == 0) {
            return vcsFields;
        }
        CustomField[] allCustomFields = Arrays.copyOf(vcsFields, vcsFields.length + userCustomFields.length);
        System.arraycopy(userCustomFields, 0, allCustomFields, vcsFields.length, userCustomFields.length);
        return allCustomFields;
    }

    private static String getCallingClassName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        int index = 1;
        String thisClassName = PerfectoLabUtils.class.getName();
        StackTraceElement stackTraceElement;
        do {
            index++;
            stackTraceElement = stackTrace[index];
        } while (stackTraceElement.getClassName().equals(thisClassName));
        String callingClassName = stackTraceElement.getClassName();
        return callingClassName;
    }
}
