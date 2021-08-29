package keyword;

import utilities.BrowserHelper;
import utilities.ExcelHelper;
import utilities.ExtentTestHelper;

import java.lang.reflect.Method;
import java.util.Arrays;


public class Runner {
    public static void main( String[] args ) {
        // represents excel sheets
        ExcelHelper testcases = new ExcelHelper();
        testcases.openExcel("resources", "TestCase.xlsx", "testcases");
        ExcelHelper teststeps = new ExcelHelper();
        teststeps.openExcel("resources", "TestCase.xlsx", "teststeps");

        // create an object of BrowserHelper class
        BrowserHelper keywords = new BrowserHelper();

        // create an object of Method class
        Method[] methods = keywords.getClass().getDeclaredMethods();
        // start extent reports
        ExtentTestHelper.createReport();
        try {
            for(int i = 1; i <= testcases.getRows(); i++) {
                // retrieve test case sheet data
                String tcid = testcases.readData(i, 0);
                String tcName = testcases.readData(i, 1);
                String runMode = testcases.readData(i, 2);
                if(runMode.equalsIgnoreCase("yes")) {
                    // retrieve test steps sheet data
                    boolean tcFoundInTSteps = false;
                    ExtentTestHelper.startTest(tcName);
                    for(int j = 1; j <= teststeps.getRows(); j++) {
                        String tsTCID = teststeps.readData(j, 0);
                        if(tcid.equalsIgnoreCase(tsTCID)) {
                            tcFoundInTSteps = true;

                            String stepName = teststeps.readData(j, 1);
                            String locType = teststeps.readData(j, 2);
                            String locValue = teststeps.readData(j, 3);
                            String elementName = teststeps.readData(j, 4);
                            String action = teststeps.readData(j, 5);
                            String testData = teststeps.readData(j, 6);
                            for(Method method : methods){
                                String methodName = method.getName();
                                if(methodName.contains("$")){
                                    methodName = methodName.replace("$", " ").split(" ")[1];
                                }
                                if(methodName.equalsIgnoreCase(action)){
                                    method.invoke(keywords, locType, locValue, elementName, testData);
                                    break;
                                }
                            }

                        }
                    }
                    if(!tcFoundInTSteps) {
                       ExtentTestHelper.info("Test case "+tcid+" not found in test steps sheet");
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BrowserHelper.closeBrowser("", "", "", "");
        }
        ExtentTestHelper.saveReport();
    }
}
