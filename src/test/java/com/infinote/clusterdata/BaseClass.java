package com.infinote.clusterdata;

import com.infinote.commonapi.JavaProperties;
import com.infinote.commonapi.MailingSystem;
import com.infinote.custom.BrowserLaunch;
import com.infinote.custom.CustomFunctions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

/**
 * @author Prudhvi
 */
public class BaseClass {
    protected static WebDriver driver;
    BrowserLaunch browserLaunch = new BrowserLaunch();
    CustomFunctions cf = new CustomFunctions();
    protected JavaProperties seleniumProperties = new JavaProperties();
    String propertiesLocation = cf.getTestResources().toString().concat("/properties/");
    String seleniumPropertiesLocation = propertiesLocation.concat("/selenium.properties");
    String excelDataLocation = propertiesLocation.concat("/ExcelData/");
//    String rulePropertiesLoc = cf.getTestResources().toString().concat("/properties/cluster/rule.properties");
    Long waitingTime;
    WebElement element;
    MailingSystem mail = new MailingSystem();

    public WebDriver launchBrowser(String browser, String browserDriverLoc) {
        if(browser.equalsIgnoreCase("CHROME")){
            System.setProperty("webdriver.chrome.driver", browserDriverLoc.concat("/Windows/chromedriver.exe"));
            ChromeOptions co = new ChromeOptions();
            co.addArguments("--start-maximized");
            driver = new ChromeDriver(co);
        }else if(browser.equalsIgnoreCase("FIREFOX")) {
            System.setProperty("webdriver.gecko.driver", browserDriverLoc.concat("/Windows/geckodriver.exe"));
            FirefoxOptions fo = new FirefoxOptions();
            fo.addArguments("--start-maximized");
            driver = new FirefoxDriver(fo);
        }else if(browser.equalsIgnoreCase("IE")||browser.equalsIgnoreCase("Internet Explorer")) {
            System.setProperty("webdriver.ie.driver", browserDriverLoc.concat("Windows/IEDriverServer.exe"));
            driver = new InternetExplorerDriver();
        }
        /*seleniumProperties.propertiesFileOpen(seleniumPropertiesLocation);
        driver.navigate().to(seleniumProperties.getPropertyValue("URL"));*/
        return driver;
    }

    @BeforeSuite
    public void setUp(){
        try{
            System.out.println(seleniumPropertiesLocation);
//            int waitTime = int.v
//            long waitingTime = new Long(seleniumProperties.getPropertyValue("WAITING_TIME")).longValue();
            seleniumProperties.propertiesFileOpen(seleniumPropertiesLocation);
            System.out.println(seleniumProperties.getPropertyValue("WAITING_TIME"));
            waitingTime = Long.parseLong(seleniumProperties.getPropertyValue("WAITING_TIME"));
            System.out.println(cf.getTestResources().toString().concat("//browserDrivers//windows//chromedriver.exe"));
            launchBrowser(seleniumProperties.getPropertyValue("BROWSER"), cf.getTestResources().toString().concat("/browserDrivers/"));
            driver.navigate().to(seleniumProperties.getPropertyValue("URL"));
            cf.waitForPagetoLoad(element, driver, waitingTime, By.xpath("//div/input[contains(@name,'username')]"));
            cf.enterValueInTextBox(driver,By.xpath("//div/input[contains(@name,'username')]"), seleniumProperties.getPropertyValue("USERNAME"));
            cf.enterValueInTextBox(driver,By.xpath("//div/input[contains(@name,'password')]"), seleniumProperties.getPropertyValue("PASSWORD"));
            driver.findElement(By.xpath("//div/input[contains(@value,'Sign In')]")).click();
            String url = driver.getCurrentUrl();
            if(url.contains("folder")){
                try {
                    cf.waitforVisibilityOfElementLocated(driver, By.xpath("//div[text()='Files']"), 60);
                } catch (Exception e) {
                    driver.quit();
                    mail.sendMail(seleniumProperties.getPropertyValue("EMAIL_SUBJECT_LOGIN_FAIL"), seleniumProperties.getPropertyValue("EMAIL_BODY_TEXT_LOGIN_FAIL"), seleniumProperties.getPropertyValue("TO_EMAIL"), seleniumProperties.getPropertyValue("FROM_EMAIL"), seleniumProperties.getPropertyValue("FROM_EMAIL_PSWD"));
                    Assert.fail("Login Failed");
                }
            }else if(driver.getPageSource().contains("The username or password you entered is incorrect.")){
                driver.quit();
                mail.sendMail(seleniumProperties.getPropertyValue("EMAIL_SUBJECT_LOGIN_FAIL"), seleniumProperties.getPropertyValue("EMAIL_BODY_TEXT_LOGIN_FAIL"), seleniumProperties.getPropertyValue("TO_EMAIL"), seleniumProperties.getPropertyValue("FROM_EMAIL"), seleniumProperties.getPropertyValue("FROM_EMAIL_PSWD"));
                Assert.fail("Login Failed");
            }
            seleniumProperties.propertiesFileClose();
//            driver.quit();
        }catch (Exception e){
            System.out.println("Problem");
            e.printStackTrace();
            driver.quit();
        }

    }

    @AfterSuite(alwaysRun = true)
    public void tearDown(){
        if(driver!=null){
            driver.quit();
        }else{
            System.out.println("driver is not present");
        }
    }
}
