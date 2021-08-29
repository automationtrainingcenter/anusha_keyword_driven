package utilities;

import org.openqa.selenium.By;

public class LocatorHelper {

    public static By locate(String locType, String locValue) {
        By loc = null;
        if(locType.equalsIgnoreCase("id")) {
            loc = By.id(locValue);
        } else if(locType.equalsIgnoreCase("name")) {
            loc = By.name(locValue);
        } else if (locType.equalsIgnoreCase("cssselector")) {
            loc = By.cssSelector(locValue);
        } else if (locType.equalsIgnoreCase("xpath")) {
            loc = By.xpath(locValue);
        } else if (locType.equalsIgnoreCase("linktext")) {
            loc = By.linkText(locValue);
        } else if (locType.equalsIgnoreCase("partiallinktext")) {
            loc = By.partialLinkText(locValue);
        } else if (locType.equalsIgnoreCase("tagname")) {
            loc = By.tagName(locValue);
        } else if (locType.equalsIgnoreCase("classname")) {
            loc = By.className(locValue);
        } else {
            System.out.println("Invalid locator type");
        }
        return loc;
    }

}
