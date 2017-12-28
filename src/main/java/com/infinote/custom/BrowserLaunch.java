package com.infinote.custom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * @author Prudhvi
 */
public class BrowserLaunch {
    private static WebDriver driver = null;

    public static WebDriver getBrowser(String browser, String browserDriverLoc){
        if (driver ==null){
            if(browser.equalsIgnoreCase("Firefox")){
                System.setProperty("webdriver.gecko.driver", browserDriverLoc.concat("//Windows//geckodriver.exe"));
                FirefoxOptions fo = new FirefoxOptions();
                fo.addArguments("--start-maximized");
                driver = new FirefoxDriver(fo);
            }else if(browser.equalsIgnoreCase("Chrome")){
                System.setProperty("webdriver.chrome.driver", browserDriverLoc.concat("//Windows//chromedriver.exe"));
                ChromeOptions co = new ChromeOptions();
                co.addArguments("--start-maximized");
                driver = new ChromeDriver(co);
            }
        }
        return driver;
    }
}
