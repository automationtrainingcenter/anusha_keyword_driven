package utilities;

import actions.BrowserName;

import java.io.File;

public class DriverConfig {

    public static String getFolderPath(String folderName){
        String baseDir = System.getProperty("user.dir");
        StringBuilder builder = new StringBuilder(baseDir);
        return builder.append(File.separator).append(folderName).append(File.separator).toString();
    }

    public static String getFilePath(String folderName, String fileName){
        return new File(DriverConfig.getFolderPath(folderName)+fileName).getAbsolutePath();
    }


    private static String getDriverName(String driverName){
        String osName = System.getProperty("os.name");
        if(osName.toLowerCase().contains("win")){
            driverName = driverName+".exe";
        }
        return driverName;
    }

    public static void setDriverPath(BrowserName browserName) {
        String driversPath = getFolderPath("drivers");
        switch(browserName){
            case CHROME:
                System.setProperty("webdriver.chrome.driver", driversPath.concat(getDriverName("chromedriver")));
                break;
            case FIREFOX:
                System.setProperty("webdriver.gecko.driver", driversPath.concat(getDriverName("geckodriver")));
                break;
        }
    }
}
