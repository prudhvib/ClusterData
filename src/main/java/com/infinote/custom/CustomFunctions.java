package com.infinote.custom;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Prudhvi
 */
public class CustomFunctions {

    public Path getResources(){
        Path resourceLocation = Paths.get("src/java/resources/");
        return resourceLocation;
    }

    public Path getTestResources(){
        Path resourceLocation = Paths.get("src/test/resources/");
        return resourceLocation;
    }

    public WebElement waitForPagetoLoad(WebElement element, WebDriver driver, long time, By by) {
        element = (new WebDriverWait(driver, time)).until(new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver d) {
                d.getCurrentUrl();
//				System.out.println("Driver is "+d.getCurrentUrl());
                return d.findElement(by);
            }
        });
//        System.out.println("Element is ::::: "+element);
        if(element==null) System.out.println("Element '"+element+"' is null");
        return element;
    }

    public void waitforVisibilityOfElementLocated(WebDriver driver, By by, long time){
        try {
            WebDriverWait wait = new WebDriverWait(driver, time);
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Element not Present");
            e.printStackTrace();
        }
    }

    public void enterValueInTextBox(WebDriver driver, By by, String value) {
        driver.findElement(by).click();
        System.out.println("Placed Cursor on the selected Element");
//		driver.findElement(by).clear();
        driver.findElement(by).sendKeys(value);
    }

    public void clearDataAndEnterText(WebDriver driver, By by, String value) {
        driver.findElement(by).click();
        driver.findElement(by).clear();
        driver.findElement(by).sendKeys(value);
    }

    public void waitforElementtobeClickable(WebDriver driver,By by, long time) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, time);
            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(by)));
        }catch (Exception e) {
            System.out.println("Element is not available for WebDriver to click");
//			e.printStackTrace();
        }

    }

    public boolean isElementClickable(WebDriver driver,By by, long time) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, time);
            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(by)));
            return true;
        }catch (Exception e) {
            System.out.println("Element is not available for WebDriver to click");
//			e.printStackTrace();
            return false;
        }
    }

    public void waitforElementtobeClickableandClick(WebDriver driver,By by, long time) {
        waitforElementtobeClickable(driver, by, time);
        driver.findElement(by).click();
    }

    public void waitforElementtobeClickableandEnterText(WebDriver driver,By by, long time, String value) {
        waitforElementtobeClickable(driver, by, time);
        enterValueInTextBox(driver, by, value);
    }

    public void waitforElementtobeClickableandEnterTextandSubmit(WebDriver driver,By by, long time, String value){
        waitforElementtobeClickableandEnterText(driver,by,time,value);
        driver.findElement(by).submit();
    }

    public void waitforElementtobeVisible(WebDriver driver, By by, long time) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, time);
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(by)));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isElementVisible(WebDriver driver, By by, long time) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, time);
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(by)));
            return true;
        }catch (Exception e) {
//			e.printStackTrace();
            System.out.println("Element is not visible");
            return false;
        }
    }

    public void waitforElementtobeVisibleandEnterText(WebDriver driver,By by, long time, String value) {
        waitforElementtobeVisible(driver, by, time);
        enterValueInTextBox(driver, by, value);
    }

    public void waitforElementtobeVisibleandClick(WebDriver driver,By by, long time){
        waitforElementtobeVisible(driver, by, time);
        System.out.println("Elements SIze: "+driver.findElements(by).size());
        driver.findElement(by).click();

    }

    public boolean isElementPresent(WebDriver driver, By by, long time){
       try{
           driver.findElement(by);
           return true;
       } catch (Exception e) {
           System.out.println("Element Not Present");
           return false;
        }
    }

    public String getTextByXPath(WebDriver driver,String xpathExp){
        try{
            return driver.findElement(By.xpath(xpathExp)).getText();
        }catch (NoSuchElementException nse){
            System.out.println("Element Not found");
            return "";
        }
    }

}
