"use strict"

const { execSync } = require('child_process');
const fs = require('fs');
const os = require('os');

process.env["NODE_TLS_REJECT_UNAUTHORIZED"] = 1;


/**
* Module to execute test framework based on parameters passed by qTest.
* @param testCaseACs String Comma delimited. List of Automation Contents passed by qTest.
* @param projectData JSON Project data passed by qTest.
* @param testrunList JSON Test Run information passed by qTest.
* @param workingDir String Working directory set in qTest Automation host.
* @returns none
* 		
**/
module.exports.executeTestSuite = (testCaseACs,projectData,testrunList,workingDir) => {
	console.log("++++++++++++++++++++++++++++++++++++++++++++++");

	try{

		// Get Project name 
		var projectName = getProjectName(workingDir);

		// Select Tests based on test identier supplied by qTest
		var testSelectionQuery = selectTests(testCaseACs);

		// Compile Test Suite (Optional based on technology)
		//compileTests(workingDir,projectName);

		// Execute Test Suite
		executeTestFramework(workingDir,projectName,testSelectionQuery);

	}catch(err){
		console.log(">>>>>>> Error: " + err);
	}
	console.log("++++++++++++++++++++++++++++++++++++++++++++++");
}

/**
* Utility function to get Current project name.
* @param String workingDir current working directory
* @returns String
* 		Project name if exists
*		"" otherwise
**/
var getProjectName = (workingDir)=>{

	try{
		var projectName = "";
		fs.readdirSync(workingDir).forEach(file => {
			if(file.endsWith(".csproj")){
				projectName = file.split(".")[0];
			}
		});

		return projectName;
	}catch(err){
		console.log(">>>>>>> Error while Searching for file with .csproj extension");
		throw err;
	}
	
}

/**
* Utility function to check whether OS is Windows or not.
* @param none
* @returns boolean
* 		true if Windows OS
*		false otherwise
**/
var isWindowsOS = ()=>{
	return os.platform().startsWith("win");
};

/**
*
* Function to Select Test from test suite based on Automation Contents passed by qTest.
* @param String testCaseACs	List of Automation Contents separated by ',' delimiter.
*
* @return it can return value to be passed to other functions (Optional)
**/
var selectTests = (testCaseACs) =>{

	var automation_content;
	var testQualifiedName;
	var testSelectionQuery = "";
	
	// Split string by comma to get identifiers supplied by qTest
	var automation_contents = testCaseACs.split(",");

	console.log("***************** Test selected from qTest *****************");
		for (var index = 0; index < automation_contents.length; index++) {
			automation_content = automation_contents[index];
			testQualifiedName = automation_content.split('$')[0];
			
			if(index==0){
				testSelectionQuery = "FullyQualifiedName=" + testQualifiedName;
			}else{
				testSelectionQuery = testSelectionQuery + "|FullyQualifiedName=" + testQualifiedName;
			}
		}
	console.log("****************************************************");

	return testSelectionQuery;
};

/**
*
* Function to compile test suite.
* @param based on framework and technology (Optional)
*
* @return it can return value to be passed to other functions (Optional)
**/
var compileTests = (workingDir,projectName) =>{
	console.log(`***************** Compiling Started *****************`);
		
		//var mavenExePath = 'C:\JARs\apache-maven-3.8.1';
		//var command = `"${mavenExePath}" clean test`; 
		//var command = `dotnet publish ${workingDir}/${projectName}.csproj`;
		executeCommand(command);

	console.log("***************** Compiling Ended *****************");
}

/**
*
* Function to execute test suite.
* @param based on framework and technology (Optional)
*
* @return it can return value to be passed to other functions (Optional)
**/
var executeTestFramework = (workingDir,projectName,testSelectionQuery)=>{
	console.log("***************** Execution Started *****************");

		var mavenExePath = 'C:\\Drivers\\apache-maven-3.8.6\\bin\\mvn';
		var command = `"${mavenExePath}" test`; 
		//var command = `dotnet vstest ${workingDir}/bin/Debug/netcoreapp2.2/${projectName}.dll --TestCaseFilter:"${testSelectionQuery}"`;
		executeCommand(command);

	console.log("***************** Execution Ended *****************");
}

var executeCommand = (command)=>{
	try{
		execSync(command,{ stdio: "inherit" });
	}catch(err){
		console.log(">>>>>>> Error while executing command - " + command);
		throw err;
	}
}

