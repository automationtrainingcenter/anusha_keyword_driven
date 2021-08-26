package utilities;

import actions.BrowserName;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.Select;
import sun.java2d.pipe.OutlineTextRenderer;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class BrowserHelper {

    static WebDriver driver;

    public static WebDriver launchBrowser(BrowserName browserName, String url) {
        // start extent reports
        ExtentTestHelper.createReport();

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


    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void closeBrowser(){
        if(driver != null) {
            ExtentTestHelper.saveReport();
            driver.quit();
        } else {
            throw new RuntimeException("driver is null.");
        }
    }

    // click on an element
    public static void click(By locator, String eleName) {
        try {
            driver.findElement(locator).click();
            ExtentTestHelper.info(String.format("clicked on element %s", eleName));
        } catch (Exception e) {
           ExtentTestHelper.fail(String.format("clicking on element %s failed due to exception "+e, eleName));
            System.out.println();
        }
    }

    // locate an element
    public static WebElement getElement(By locator, String eleName){
        try {
            WebElement element = driver.findElement(locator);
            ExtentTestHelper.info(String.format("element %s located successfully", eleName));
            return element;
        } catch (Exception e) {
            ExtentTestHelper.fail("locating element "+eleName+"  is failed due to exception "+e);
        }
        return null;
    }

    // enter text
    public static void enterText(By locator, String text, String elementName){
        try {
            getElement(locator, elementName).sendKeys(text);
        } catch (Exception e) {
            ExtentTestHelper.fail(String.format("Failed enter text '%s' in element '%s' due to exception "+e, text, elementName));
        }
    }

    // select an option of drop down
    public static void selectOption(By locator, String option, String eleName){
        try {
            Select select = new Select(getElement(locator, eleName));
            List<WebElement> options = select.getOptions();
            String first = options.stream().map(WebElement::getText).filter(optionText -> optionText.equalsIgnoreCase(option)).findFirst().toString();
            select.selectByVisibleText(first);
            ExtentTestHelper.info(String.format("Option '%s' selected successfully from drop down '%s'", option, eleName));
        } catch (Exception e){
            ExtentTestHelper.fail(String.format("Failed to select option '%s' from drop down '%s' due to exception "+e, option, eleName ));
        }
    }

    // accept alert
    public static String acceptAlert(){
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
    public static void mouseHoverToElement(By locator, String element) {
        try {
            WebElement ele = getElement(locator, element);
            Actions actions = new Actions(driver);
            actions.moveToElement(ele).build().perform();
            ExtentTestHelper.info("Mouse hover to "+element);
        } catch (Exception e) {
            ExtentTestHelper.fail(String.format("mouse hover on to the element %s failed due to exception "+e, element));
        }
    }

    // take screenshot
    public static void attachScreenshotToReport(String titleImage){
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            String screenshotAs = ts.getScreenshotAs(OutputType.BASE64);
            ExtentTestHelper.getTest().addScreenCaptureFromBase64String(screenshotAs, titleImage);
        } catch (WebDriverException e) {
           ExtentTestHelper.fail("Failed to capture the screenshot due to exception "+e);
        }
    }

    private static String getFilePath(String folderName, String fileName){
        return new File(DriverConfig.getFolderPath(folderName)+fileName).getAbsolutePath();
    }

    public static void getScreenShotAsFile(String fileName){
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File screenshotAs = ts.getScreenshotAs(OutputType.FILE);
            FileHandler.copy(screenshotAs, new File(DriverConfig.getFolderPath("screenshots")+fileName));
            ExtentTestHelper.getTest().addScreenCaptureFromPath(getFilePath("screenshot", fileName));
        } catch (Exception e) {
            ExtentTestHelper.fail("Failed to capture the screenshot due to exception "+e);
        }
    }
}
