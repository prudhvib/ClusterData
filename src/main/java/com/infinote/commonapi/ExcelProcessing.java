package com.infinote.commonapi;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;

/**
 * @author Prudhvi
 */
public class ExcelProcessing {

    private XSSFSheet writeSheet, checkSheet;
    FileOutputStream outFile;
    FileInputStream fis;
    private XSSFWorkbook writeWorkbook, checkWorkbook;
    XSSFRow writeRow;
    XSSFCell writeCell;
    private File writeFile;

    public void writeDataToCell(String fileName, String sheetName, int row, int column, String value){
        try {
            fis = new FileInputStream(new File(fileName));
            writeWorkbook = new XSSFWorkbook(fis);
            writeSheet = writeWorkbook.getSheet(sheetName);
            writeRow = writeSheet.getRow(row);
            if(writeRow==null){
                writeRow = writeSheet.createRow(row);
            }
            writeCell = writeRow.createCell(column);
            XSSFCellStyle wrapText=writeWorkbook.createCellStyle();
            wrapText.setWrapText(true);
            writeCell.setCellStyle(wrapText);
            writeCell.setCellValue(value);
            writeSheet.autoSizeColumn(column);
            fis.close();
            outFile = new FileOutputStream(new File(fileName));
            writeWorkbook.write(outFile);
            outFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EncryptedDocumentException e){
            e.printStackTrace();
        }

    }

    public void createSheetinExcel(String fileName, String sheetName){
        try {
            fis = new FileInputStream(new File(fileName));
            writeWorkbook = new XSSFWorkbook(fis);
            writeSheet = writeWorkbook.createSheet(sheetName);
            fis.close();
            outFile = new FileOutputStream(new File(fileName));
            writeWorkbook.write(outFile);
            outFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EncryptedDocumentException e){
            e.printStackTrace();
        }
    }
    
    public boolean checkSheetisPresent(String fileName, String sheetName){
        try{
            fis = new FileInputStream(new File(fileName));
            checkWorkbook = new XSSFWorkbook(fis);
            checkSheet = checkWorkbook.getSheet(sheetName);
            fis.close();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void deleteAllSheets(String fileName){
        try {
            fis = new FileInputStream(new File(fileName));
            writeWorkbook = new XSSFWorkbook(fis);
            for(int sheetIndex=0;sheetIndex<writeWorkbook.getNumberOfSheets();sheetIndex++){
                if(writeSheet!=null){
                    writeSheet = writeWorkbook.getSheetAt(sheetIndex);
                    writeWorkbook.removeSheetAt(sheetIndex);
                }
            }
            fis.close();
            outFile = new FileOutputStream(new File(fileName));
            writeWorkbook.write(outFile);
            outFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EncryptedDocumentException e){
            e.printStackTrace();
        }
    }



    public static void main(String[] args){
        ExcelProcessing exp = new ExcelProcessing();
        String fileName = "C:\\Prudhvi\\Automation\\ClusterData\\src\\test\\resources\\properties\\ExcelData\\test.xlsx";
        exp.createSheetinExcel(fileName,"Test, Sheet.,");
        exp.writeDataToCell("C:\\Prudhvi\\Automation\\ClusterData\\src\\test\\resources\\properties\\ExcelData\\test.xlsx", "Test, Sheet.,",0,0,"Test");
    }

}
