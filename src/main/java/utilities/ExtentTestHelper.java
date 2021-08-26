package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentTestHelper {

    private static ExtentReports reports;
    private static ExtentSparkReporter reporter;
    private static ExtentTest extenttest;

    // create html report by initializing required classes
    public static void createReport(){
        reports = new ExtentReports();
        reporter = new ExtentSparkReporter("");
        reports.attachReporter(reporter);
    }

    // start the extent test for every test case
    public static void startTest(String testCaseName){
        extenttest = reports.createTest(testCaseName);
    }

    // get the extent test object to write the logs
    public static ExtentTest getTest(){
        return extenttest;
    }

    // info log
    public static void info(String message){
        getTest().info(message);
    }

    // pass log
    public static void pass(String message){
        getTest().pass(message);
    }

    // fail log
    public static void fail(String message){
        getTest().fail(message);
    }

    // save report
    public static void saveReport(){
        reports.flush();
    }


}
