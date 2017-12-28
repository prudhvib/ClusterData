package com.infinote.clusterdata;

import com.infinote.commonapi.ExcelProcessing;
import com.infinote.commonapi.JavaProperties;
import com.infinote.custom.CustomFunctions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;
import org.openqa.selenium.NoSuchElementException;

import java.util.*;

/**
 * @author Prudhvi
 */
public class ClusterData extends BaseClass{
    private CustomFunctions cf = new CustomFunctions();
    private JavaProperties jp = new JavaProperties();
    private JavaProperties ruleProperties = new JavaProperties();
    ExcelProcessing exp = new ExcelProcessing();
//    String clusterGroupProperties = propertiesLocation.concat("/cluster/ClusterGroups.properties");
    String rulePropertiesLoc = propertiesLocation.concat("/cluster/rule.properties");
    String excelFileExtractedValues = excelDataLocation.concat("/extracteddata/ContractsDatabase.xlsx");
    String excelFileExtractedData = excelDataLocation.concat("/extracteddata/ExtractedData.xlsx");


    @Test(priority = 1)
    public void clusterPage(){
        seleniumProperties.propertiesFileOpen(seleniumPropertiesLocation);
        waitingTime = Long.parseLong(seleniumProperties.getPropertyValue("WAITING_TIME"));
        String clusterPath = "//span[text()='"+seleniumProperties.getPropertyValue("CLUSTER_NAME") +"']";
        System.out.println("Cluster Path is "+clusterPath);
        System.out.println("Name of the Cluster is "+clusterPath);
        driver.navigate().to(seleniumProperties.getPropertyValue("URL").concat("/#/document-cluster"));
        cf.waitForPagetoLoad(element,driver,waitingTime, By.xpath(clusterPath));
        cf.waitforElementtobeClickableandClick(driver,By.xpath(clusterPath),waitingTime);
        cf.waitForPagetoLoad(element,driver,waitingTime,By.xpath("//div[text()='"+seleniumProperties.getPropertyValue("CLUSTER_NAME")+"']"));
        seleniumProperties.writeProperty(seleniumPropertiesLocation,"CLUSTER_URL",driver.getCurrentUrl());
        driver.navigate().to(seleniumProperties.getPropertyValue("CLUSTER_URL"));
        cf.waitForPagetoLoad(element,driver,waitingTime,By.xpath("//div[text()='"+seleniumProperties.getPropertyValue("CLUSTER_NAME")+"']"));
        seleniumProperties.propertiesFileClose();
    }

//    @Test(priority = 2)
    public void retriveClusterDocsExtractedValues(){
        seleniumProperties.propertiesFileOpen(seleniumPropertiesLocation);
        ruleProperties.propertiesFileOpen(rulePropertiesLoc);
//        jp.propertiesFileOpen(clusterGroupProperties);
//        jp.removeAllProperties(clusterGroupProperties);
        waitingTime = Long.parseLong(seleniumProperties.getPropertyValue("WAITING_TIME"));
        cf.waitForPagetoLoad(element,driver,waitingTime,By.xpath("//li[contains(@class,'childsRows')]/span/span[2]/span[2]"));
        List<WebElement> clusteredGroups = driver.findElements(By.xpath("//li[contains(@class,'childsRows')]/span/span[2]/span[2]"));
        int clusteredGroupCount = 0;
        int rowNo=1;
        for(WebElement clusteredGroup:clusteredGroups){
            String groupClusterName = clusteredGroup.getText();
            System.out.println(clusteredGroupCount+"  ::::::::  "+groupClusterName);
            driver.findElement(By.xpath("//li[contains(@class,'childsRows')]/span/span[2]/span[text()='"+groupClusterName+"']")).click();
            cf.waitForPagetoLoad(element,driver,waitingTime,By.xpath("//div[text()='"+groupClusterName+"']"));
            driver.findElement(By.id("simple-search-action-btn")).click();
            cf.waitForPagetoLoad(element,driver,waitingTime,By.xpath("//a[text()='View documents ']"));
            driver.findElement(By.xpath("//a[text()='View documents ']")).click();
            cf.waitForPagetoLoad(element,driver,waitingTime,By.xpath("//div[text()='"+groupClusterName+"']"));
            
            List<WebElement> clusteredGroupDocs = driver.findElements(By.xpath("//infinote-table-document-name-column/span"));
            int clusteredGroupDocCount=0;
            
            System.out.println("Number of Docs ::: "+clusteredGroupDocs.size());
            for(WebElement clusteredGroupDoc: clusteredGroupDocs){
               /* if(clusteredGroupCount>0){
                    rowNo = clusteredGroupDocs.size();
                }*/
                String clusteredGroupDocName = clusteredGroupDoc.getText();
//                System.out.println("Row No is ::: "+rowNo);
                WebElement doc = driver.findElement(By.xpath("//span[text()=' "+clusteredGroupDocName+"']"));
//                System.out.println(groupClusteredDocsCount+"  :::::   "+clusteredGroupDoc.getText());
                Actions action = new Actions(driver);
                action.moveToElement(doc);
                action.contextClick(doc).build().perform();
                driver.findElement(By.xpath("//a[text()='Extracted Data']")).click();
                ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
                driver.switchTo().window(tabs.get(1));
                cf.waitForPagetoLoad(element,driver,waitingTime,By.xpath("//h3[contains(text(),'Document Intelligence Report')]"));
                List<WebElement> extractedRules = driver.findElements(By.xpath("//tr[contains(@ng-repeat,'rule')]/td[1]/b"));
                int columnNo=0;
                for(WebElement extractedRule: extractedRules){
                    String rule = extractedRule.getText();
                    columnNo = Integer.parseInt(ruleProperties.getPropertyValue(rule));
                    try{
                        String ruleValue = driver.findElement(By.xpath("//td/b[text()='"+rule+"']/../../td/div/div")).getText();
                        exp.writeDataToCell(excelFileExtractedValues,"Sheet1",rowNo,columnNo,ruleValue);

                    }catch (Exception nse){
                        if(rule.equalsIgnoreCase("Customer")&&cf.isElementPresent(driver,By.xpath("//td/b[text()='Customer']/../../td[2]/div/div"),waitingTime)){
                            exp.writeDataToCell(excelFileExtractedValues,"Sheet1",rowNo,columnNo,driver.findElement(By.xpath("//table/tbody/tr/td/b[text()='File']/../../td[2]")).getText());
                        }else {
//                            System.out.println("Value for this Rule, under the selected doc is not present");
                            exp.writeDataToCell(excelFileExtractedValues, "Sheet1", rowNo, columnNo, "");
                        }
                    }
                }
                driver.close();
                driver.switchTo().window(tabs.get(0));
                driver.switchTo().defaultContent();
                rowNo++;
                clusteredGroupDocCount++;
            }
            clusteredGroupCount++;
        }
    }

    @Test(priority = 2)
    public void retrieveClusteredDocsExtractedData(){
        seleniumProperties.propertiesFileOpen(seleniumPropertiesLocation);
        waitingTime = Long.parseLong(seleniumProperties.getPropertyValue("WAITING_TIME"));
//        exp.deleteAllSheets(excelFileExtractedData);
        cf.waitForPagetoLoad(element,driver,waitingTime,By.xpath("//li[contains(@class,'childsRows')]/span/span[2]/span[2]"));
        List<WebElement> clusteredGroups = driver.findElements(By.xpath("//li[contains(@class,'childsRows')]/span/span[2]/span[2]"));
        int clusteredGroupCount = 0;
        for(WebElement clusteredGroup:clusteredGroups){
            String groupClusterName = clusteredGroup.getText();
            System.out.println(clusteredGroupCount+"  ::::::::  "+groupClusterName);
            driver.findElement(By.xpath("//li[contains(@class,'childsRows')]/span/span[2]/span[text()='"+groupClusterName+"']")).click();
            cf.waitForPagetoLoad(element,driver,waitingTime,By.xpath("//div[text()='"+groupClusterName+"']"));
            driver.findElement(By.id("simple-search-action-btn")).click();
            cf.waitForPagetoLoad(element,driver,waitingTime,By.xpath("//a[text()='View documents ']"));
            driver.findElement(By.xpath("//a[text()='View documents ']")).click();
            cf.waitForPagetoLoad(element,driver,waitingTime,By.xpath("//div[text()='"+groupClusterName+"']"));
            List<WebElement> clusteredGroupDocs = driver.findElements(By.xpath("//infinote-table-document-name-column/span"));
            for(WebElement clusteredGroupDoc: clusteredGroupDocs){
                int rowNo=0;
                System.out.println("Row no is ::::   "+rowNo);
                String clusteredGroupDocName = clusteredGroupDoc.getText();
                WebElement doc = driver.findElement(By.xpath("//span[text()=' "+clusteredGroupDocName+"']"));
                Actions action = new Actions(driver);
                action.moveToElement(doc);
                action.contextClick(doc).build().perform();
                driver.findElement(By.xpath("//a[text()='Extracted Data']")).click();
                ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
                driver.switchTo().window(tabs.get(1));
                cf.waitForPagetoLoad(element,driver,waitingTime,By.xpath("//h3[contains(text(),'Document Intelligence Report')]"));
                try{
                    cf.waitForPagetoLoad(element,driver,5,By.xpath("//span[text()='No Tables found in document']"));
                }catch (Exception e){
                    try{
                        cf.waitForPagetoLoad(element,driver,5,By.xpath("//document-extracted-table/table/tbody/tr"));
                        System.out.println("Tables are identified");
                    }catch (Exception e1){
                        System.out.println("Table value extracted");
                    }
                }
                try{
                    cf.waitForPagetoLoad(element,driver,5,By.xpath("//span[text()='No Relations extracted']"));
                }catch (Exception e){
                    System.out.println("Some Relations got extracted");
                }
                try{
                    cf.waitForPagetoLoad(element,driver,5,By.xpath("//span[text()='No Clauses extracted']"));
                }catch (Exception e){
                    System.out.println("Some Clauses got extracted");
                }
                
//                System.out.println("Customer Name is :::: "+customerElement.getText());
                String sheetName=null;
                if(cf.isElementPresent(driver,By.xpath("//td/b[text()='Customer']/../../td/div/div"),waitingTime)){
                    WebElement customerElement = driver.findElement(By.xpath("//td/b[text()='Customer']/../../td/div/div"));
                    sheetName = customerElement.getText();
                    System.out.println("Customer Value is present. Name is :::::: "+sheetName);
                }else if(cf.isElementPresent(driver,By.xpath("//table/tbody/tr/td/b[text()='File']/../../td[2]"),waitingTime)){
                    WebElement customerFileName = driver.findElement(By.xpath("//table/tbody/tr/td/b[text()='File']/../../td[2]"));
                    sheetName = customerFileName.getText();
                    System.out.println("Writing File name to Sheet :::::: " +sheetName);
                }
                if(sheetName.length()>32){
                    sheetName = sheetName.substring(0,31);
                }
                /*if(driver.findElement(By.xpath("//td/b[text()='Customer']/../../td/div/div")).isDisplayed()){
                }else if(driver.findElement(By.xpath("//table/tbody/tr/td/b[text()='File']/../../td[2]")).isDisplayed()){
                }*/
                exp.createSheetinExcel(excelFileExtractedData,sheetName);
                exp.writeDataToCell(excelFileExtractedData,sheetName,rowNo,0,cf.getTextByXPath(driver,"//table/tbody/tr/td/b[text()='File']"));
                exp.writeDataToCell(excelFileExtractedData,sheetName,rowNo++,1,cf.getTextByXPath(driver,"//table/tbody/tr/td/b[text()='File']/../../td[2]"));
                exp.writeDataToCell(excelFileExtractedData,sheetName,rowNo,0,cf.getTextByXPath(driver,"//table/tbody/tr/td/b[text()='Location']"));
                exp.writeDataToCell(excelFileExtractedData,sheetName,rowNo++,1,cf.getTextByXPath(driver,"//table/tbody/tr/td/b[text()='Location']/../../td[2]"));
                rowNo++;
                exp.writeDataToCell(excelFileExtractedData,sheetName,rowNo++,0,cf.getTextByXPath(driver,"//h4[text()='Extracted Data']"));
                if(driver.getPageSource().contains("No Rules for data extraction")){
                    System.out.println("No Rules for data extraction");
                    exp.writeDataToCell(excelFileExtractedData,sheetName,rowNo++,1,"No Rules for data extraction");
                    rowNo++;
                }else {
                    List<WebElement> extractedRules = driver.findElements(By.xpath("//tr[contains(@ng-repeat,'rule')]/td[1]/b"));
                    for(WebElement extractedRule: extractedRules){
                        String rule = extractedRule.getText();
//                        System.out.println("Rule name is :::: "+ rule);
                        try{
                            String ruleValue = driver.findElement(By.xpath("//td/b[text()='"+rule+"']/../../td/div/div")).getText();
//                            System.out.println("Data for the rule "+rule+" is "+ruleValue);
                            exp.writeDataToCell(excelFileExtractedData,sheetName,rowNo,0,rule);
                            exp.writeDataToCell(excelFileExtractedData,sheetName,rowNo,1,ruleValue);

                        }catch (NoSuchElementException nse){
                            System.out.println("Value for this Rule, under the selected doc is not present");
                            exp.writeDataToCell(excelFileExtractedData,sheetName,rowNo,0,rule);
                            exp.writeDataToCell(excelFileExtractedData,sheetName,rowNo,1,"");
                        } catch (Exception e){
                        
                        }
                        rowNo++;
                    }
                }
                rowNo++;
                exp.writeDataToCell(excelFileExtractedData,sheetName,rowNo++,0,cf.getTextByXPath(driver,"//h4[text()='Extracted Relations']"));
                /* writing Extracted Relations to Excel*/
                /* Need to write code in else block*/
                if(driver.getPageSource().contains("No Relations extracted")){
                    exp.writeDataToCell(excelFileExtractedData,sheetName,rowNo++,1,"No Relations extracted");
                    rowNo++;
                }else {
                   /* List<WebElement> extractedRelations = driver.findElements(By.xpath("//tbody[contains(@ng-repeat,'relationRule')]/tr/td/b"));
                    for(WebElement extractedRelation: extractedRelations){
                        String extractedRelationName=extractedRelation.getText();
                    }*/
                }

                /*Extracted Clauses writing to excel*/
                exp.writeDataToCell(excelFileExtractedData,sheetName,rowNo++,0,cf.getTextByXPath(driver,"//h4[text()='Extracted Clauses']"));
                if(driver.getPageSource().contains("No Clauses extracted")){
                    System.out.println("No Clauses Extracted");
                    exp.writeDataToCell(excelFileExtractedData,sheetName,rowNo++,1,"No Clauses extracted");
                    rowNo++;
                }else {
                    List<WebElement> extractedClauses = driver.findElements(By.xpath("//table/tbody[contains(@ng-repeat,'clauseRule')]/tr/td[1]/b"));
                    for (WebElement extractedClause : extractedClauses) {
                        String clause = extractedClause.getText();
                        try {
                            String ruleValue = driver.findElement(By.xpath("//table/tbody[contains(@ng-repeat,'clauseRule')]/tr/td/b[text()='" + clause + "']/../../td[2]")).getText();
                            exp.writeDataToCell(excelFileExtractedData, sheetName, rowNo, 0, clause);
                            exp.writeDataToCell(excelFileExtractedData, sheetName, rowNo, 1, ruleValue);
    
                        } catch (NoSuchElementException nse) {
                            System.out.println("Value for this Rule, under the selected doc is not present");
                            exp.writeDataToCell(excelFileExtractedData, sheetName, rowNo, 0, clause);
                            exp.writeDataToCell(excelFileExtractedData, sheetName, rowNo, 1, " ");
                        }
                        rowNo++;
                    }
                }

                    /*Writing Extracted tables to Excel*/
                exp.writeDataToCell(excelFileExtractedData,sheetName,rowNo++,0,cf.getTextByXPath(driver,"//h4[text()='Extracted Tables']"));
                if(driver.getPageSource().contains("No Tables found in document")){
                    System.out.println("No Tables found");
                    exp.writeDataToCell(excelFileExtractedData,sheetName,rowNo++,1,"No Tables found in document");
                    rowNo++;
                }else{
                    rowNo++;
                    List<WebElement> tableRows = driver.findElements(By.xpath("//table[contains(@class,'table')]/tbody/tr[contains(@ng-repeat,'row')]"));
//                    System.out.println("No of table rows :::: "+tableRows.size());
                    for(int tableRowCount = 1;tableRowCount<=tableRows.size();tableRowCount++){
                        List<WebElement> tableColumns = driver.findElements(By.xpath("//table[contains(@class,'table')]/tbody/tr[contains(@ng-repeat,'row')]["+tableRowCount+"]/td[contains(@ng-repeat,'col')]"));
                        for(int tableColumnCount = 1; tableColumnCount<=tableColumns.size();tableColumnCount++){
                            exp.writeDataToCell(excelFileExtractedData,sheetName,rowNo,tableColumnCount-1,cf.getTextByXPath(driver,"//table[contains(@class,'table')]/tbody/tr[contains(@ng-repeat,'row')]["+tableRowCount+"]/td[contains(@ng-repeat,'col')]["+tableColumnCount+"]/div"));
                        }
                        rowNo++;
                    }

                }
                
                driver.close();
                driver.switchTo().window(tabs.get(0));
                driver.switchTo().defaultContent();
            }
            clusteredGroupCount++;
        }
    }
}
