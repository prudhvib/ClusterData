package com.infinote.commonapi;

import java.io.*;
import java.util.Properties;
import java.util.Set;

/**
 * @author Prudhvi
 */
public class JavaProperties {
	Properties prop = new Properties();
	FileInputStream fip;
	OutputStream os;
	FileOutputStream fos;
	String filePath, message;

	/*public JavaProperties(){
		InputStream is = null;
		try{
			this.prop = new Properties();
			is = this.getClass().getResourceAsStream(filePath);
			prop.load(is);
		}catch(FileNotFoundException fne){
			System.out.println("File not found");
		}catch(IOException io){
			System.out.println("Input Output exception");
		}
	}*/
	
	public void propertiesFileOpen(String filePath) {
		try {
			this.filePath = filePath;
			fip = new FileInputStream(filePath);
		}catch (IOException io) {
			// TODO: handle exception
//			Assert.fail(io.toString());
			io.printStackTrace();
		}
	}
	
	public String getPropertyValue(String property) {
		try {
			prop.load(fip);
			message = prop.getProperty(property);
			if(message == null) {
				message = new String("");
			}
		}catch (NullPointerException npe) {
			if(message == null) {
				message = new String("");
			}
		}catch (IOException io) {
//			Assert.fail(io.toString());
			io.printStackTrace();
		}
		return message;
	}

	/*public String getPropertyValue(String key){
		return this.prop.getProperty(key);
	}*/

	public void propertiesFileClose(){
		try {
			fip.close();
		} catch (IOException e) {
			System.out.println("File Stream not closed");
			e.printStackTrace();
		}
	}

	public void writeProperty(String file, String key, String value){
		try{
			os = new FileOutputStream(file);
			prop.setProperty(key,value);
			prop.store(os,"Value");
			os.close();
		}catch (FileNotFoundException fne) {
			System.out.println("File Not Found");
//			fne.printStackTrace();
		}catch (IOException io){
			System.out.println("IO Exception");
//			io.printStackTrace();
		}
	}

	public void removeProperty(String file, String key){
		try{
//			prop.load();
			fos = new FileOutputStream(file);
			prop.remove(key);
			fos.close();
		}catch (FileNotFoundException fne){

		}catch (IOException io){
			System.out.println("IO Exception");
//			io.printStackTrace();
		}
	}

	public Set<Object> getAllKeys(){
		Set<Object> keys = prop.keySet();
		return keys;
	}

	public void removeAllProperties(String filePath){
		try{
			propertiesFileOpen(filePath);
			prop.load(fip);
			Set<Object> keys = getAllKeys();
			for(Object k:keys){
				System.out.println();
				String key = (String)k;
				System.out.println(key+": "+getPropertyValue(key));
				removeProperty(filePath,key);
			}
		}catch (Exception e){

		}
	}

	public void numberofKeys(){
		try{
			propertiesFileOpen(filePath);
			prop.load(fip);
			Set<Object> keys = getAllKeys();
		}catch (Exception e){

		}

	}

	public static void main(String[] args){
		JavaProperties jp = new JavaProperties();
		FileInputStream fip;
		String file = "C:\\Prudhvi\\Automation\\ClusterData\\src\\test\\resources\\properties\\test.properties";
		/*jp.propertiesFileOpen(file);
		jp.removeProperty(file,"URL");
		jp.writeProperty(file,"URL","test");
		jp.writeProperty(file,"URL1","test1");
		jp.writeProperty(file,"URL2","test2");
//		jp.removeProperty(file,"URL1");
//		jp.removeAllProperties(file);*/
		Set<Object> keys = jp.getAllKeys();
		for(Object k:keys) {
			System.out.println();
			String key = (String) k;
			System.out.println(key + ": " + jp.getPropertyValue(key));
		}

	}

}
