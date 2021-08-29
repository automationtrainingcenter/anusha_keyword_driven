package utilities;

import actions.BrowserName;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.util.List;

public class BrowserHelper {

    static WebDriver driver;

    public static WebDriver launchBrowser(String locType, String locValue, String elementName, String testData) {
        BrowserName browserName = BrowserName.valueOf(elementName.toUpperCase());
        String url = testData;

        DriverConfig.setDriverPath(browserName);
        switch (browserName){
            case CHROME:
                driver = new ChromeDriver();
                break;
            case FIREFOX:
                driver = new FirefoxDriver();
                break;
            default:
                throw new RuntimeException("Browser name is invalid");
        }
        driver.manage().window().maximize();
        driver.get(url);
        ExtentTestHelper.info(String.format("%s browser launched and navigated to %s\n", browserName.toString(), url));
        return driver;
    }


    public static void sleep(String locType, String locValue, String elementName, String testData) {
        try {
            Thread.sleep(Long.parseLong(testData));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void closeBrowser(String locType, String locValue, String elementName, String testData){
        if(driver != null) {
            driver.quit();
        } else {
            throw new RuntimeException("driver is null.");
        }
    }

    // click on an element
    public static void click(String locType, String locValue, String elementName, String testData) {
        try {
            By locator = LocatorHelper.locate(locType, locValue);
            driver.findElement(locator).click();
            ExtentTestHelper.info(String.format("clicked on element %s", elementName));
        } catch (Exception e) {
           ExtentTestHelper.fail(String.format("clicking on element %s failed due to exception "+e, elementName));
            System.out.println();
        }
    }

    // locate an element
    public static WebElement getElement(String locType, String locValue, String elementName, String testData){
        try {
            By locator = LocatorHelper.locate(locType, locValue);
            WebElement element = driver.findElement(locator);
            return element;
        } catch (Exception e) {
            ExtentTestHelper.fail("locating element "+elementName+"  is failed due to exception "+e);
        }
        return null;
    }

    // enter text
    public static void enterText(String locType, String locValue, String elementName, String testData){
        try {
            getElement(locType, locValue, elementName, testData).sendKeys(testData);
            ExtentTestHelper.info(String.format("Entered text '%s' in element %s  successfully", testData, elementName));
        } catch (Exception e) {
            ExtentTestHelper.fail(String.format("Failed enter text '%s' in element '%s' due to exception "+e, testData, elementName));
        }
    }

    // select an option of drop down
    public static void selectOption(String locType, String locValue, String elementName, String testData){
        try {
            Select select = new Select(getElement(locType, locValue, elementName, testData));
            List<WebElement> options = select.getOptions();
            String first = options.stream().map(WebElement::getText).filter(optionText -> optionText.equalsIgnoreCase(testData)).findFirst().toString();
            select.selectByVisibleText(first);
            ExtentTestHelper.info(String.format("Option '%s' selected successfully from drop down '%s'", testData, elementName));
        } catch (Exception e){
            ExtentTestHelper.fail(String.format("Failed to select option '%s' from drop down '%s' due to exception "+e, testData, elementName ));
        }
    }

    // accept alert
    public static String acceptAlert(String locType, String locValue, String elementName, String testData){
        try {
            Alert alert = driver.switchTo().alert();
            String text = alert.getText();
            alert.accept();
            ExtentTestHelper.info(String.format("Clicked on OK button of the alert with text: '%s'",text));
            return text;
        } catch (Exception e) {
            ExtentTestHelper.fail(String.format("Failed to click on OK button of the alert due to exception "+e));
        }
        return null;
    }

    // mouse hover event
    public static void mouseHoverToElement(String locType, String locValue, String elementName, String testData) {
        try {
            WebElement ele = getElement(locType, locValue, elementName, testData);
            Actions actions = new Actions(driver);
            actions.moveToElement(ele).build().perform();
            ExtentTestHelper.info("Mouse hover to "+elementName);
        } catch (Exception e) {
            ExtentTestHelper.fail(String.format("mouse hover on to the element %s failed due to exception "+e, elementName));
        }
    }

    // take screenshot
    public static void attachScreenshotToReport(String locType, String locValue, String elementName, String testData){
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            String screenshotAs = ts.getScreenshotAs(OutputType.BASE64);
            ExtentTestHelper.getTest().addScreenCaptureFromBase64String(screenshotAs, testData);
        } catch (WebDriverException e) {
           ExtentTestHelper.fail("Failed to capture the screenshot due to exception "+e);
        }
    }


    public static void getScreenShotAsFile(String locType, String locValue, String elementName, String testData){
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File screenshotAs = ts.getScreenshotAs(OutputType.FILE);
            FileHandler.copy(screenshotAs, new File(DriverConfig.getFolderPath("screenshots")+testData));
            ExtentTestHelper.getTest().addScreenCaptureFromPath(DriverConfig.getFilePath("screenshot", testData));
        } catch (Exception e) {
            ExtentTestHelper.fail("Failed to capture the screenshot due to exception "+e);
        }
    }
}
