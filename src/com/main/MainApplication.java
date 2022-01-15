package com.main;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Query;

import excelConversion.pdfTableAPI;
import tableDAO.data_read_table;
import unlockPDF.decryptPDF;

public class MainApplication {
	
	static int start_row;
	static int end_row;
	
	String password = "";
	String lockedPdfFilePath = "";
	String unlockedPdfFilePath = "";
	String excelFilePath = "";
	String apiKey = "";
	String url = "";
	
	static List<Date> dates = new ArrayList<Date>();
	static List<String> modes = new ArrayList<String>();
	static List<String> description = new ArrayList<String>();
	static List<String> deposit = new ArrayList<String>();
	static List<String> withdrawals = new ArrayList<String>();
	static List<String> balance = new ArrayList<String>();
	static HashMap<Integer, String> row_cell_values = new HashMap<Integer, String>();
	static List<String> month = new ArrayList<String>();
	static List<String> year = new ArrayList<String>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainApplication obj = new MainApplication();
		
	//	readPropertiesFile();
		obj.performOperations();
		//DataInputReading.check ForExcel();
	
				
		
		
	}
	
	private void performOperations() {
		// TODO Auto-generated method stub
		
		DatabaseConnections.startSession();
		
		System.out.println("Started Session");
		readPropertiesFile();
		
		decryptPDF.unlockOperations(lockedPdfFilePath, unlockedPdfFilePath, password);
		
		pdfTableAPI.convertPdfToExcel(unlockedPdfFilePath, url, apiKey, excelFilePath);
		
		DataInputReading.pushExcelToDB(excelFilePath);
		
		
	
	//	DataInputReading.checkForExcel();
		
		List<data_read_table> allReadObj = getPendingRecords();
		if(allReadObj == null || allReadObj.isEmpty())
		{
			System.out.println("No Records to Process");
		}
		System.out.println("Size: " + allReadObj.size());
			
		for(int i =0; i < allReadObj.size(); ++i)
		{
			dates.clear();
			modes.clear();
			description.clear();
			deposit.clear();
			balance.clear();
			withdrawals.clear();
			balance.clear();
		
			month.clear();
			year.clear();
			row_cell_values.clear();
			
			data_read_table readData = new data_read_table();
			readData = allReadObj.get(i);
		
			//String excelPath = "D:\\PEM - Documents\\Statement_2021MTH09_940555767-unlocked.xlsx";
			String excelPath = readData.getFile_path();
			System.out.println(excelPath);
			File excelFile = new File(excelPath);
			if(excelFile.isFile())
			{
				
				try {
					FileInputStream inp = new FileInputStream(excelFile);
					XSSFWorkbook workbook = new XSSFWorkbook(inp);
					XSSFSheet sheet = workbook.getSheetAt(0);
				
					getNoOfRows(sheet);
					
					System.out.println("Getting Data");
					getTransactionnValues(sheet);
					
					System.out.println("Start Row: " + start_row );
					System.out.println("End Row: " + end_row);
					
					System.out.println("Printing Tranactions");
				//	printTransactions();
					
					DataInputReading.pushToDB(dates,modes,description,deposit,withdrawals,balance,year,month);
				
					readData.setStatus("Success");
				
				} 
				catch (FileNotFoundException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				readData.setStatus("Error");
				System.out.println("Not a Proper File");
			}
			
			
			DatabaseConnections.saveObj(readData);
		}
		System.out.println("Closing Sesssion");
		DatabaseConnections.closeSession();
		
		
	}

	private void readPropertiesFile() {
		// TODO Auto-generated method stub
		
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("tools\\info.properties"));
			password = prop.getProperty("pdfPassword");
			lockedPdfFilePath = prop.getProperty("lockedPdfFilePath");
			unlockedPdfFilePath = prop.getProperty("unlockedPdfFilePath");
			excelFilePath = prop.getProperty("excelFilePath");
			apiKey = prop.getProperty("apiKey");
			url = prop.getProperty("url");
					
			System.out.println(password);
			System.out.println(lockedPdfFilePath);
			System.out.println(unlockedPdfFilePath);
			System.out.println(excelFilePath);
			System.out.println(apiKey);
			System.out.println(url);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	
	}

	private List<data_read_table> getPendingRecords() {
		// TODO Auto-generated method stub
		
		List<data_read_table> allData = DatabaseConnections.session.createQuery("from data_read_table where status = 'In Process'").list();
		List<data_read_table> results = new ArrayList<data_read_table>();
		for(int i = 0; i<allData.size(); ++i)
		{
			data_read_table temp = new data_read_table();
			temp = allData.get(i);
			
			Query q = DatabaseConnections.session.createQuery("select count(sr_num) from data_input_table where tx_month = ?1 and tx_year = ?2");
			q.setParameter(1,temp.getMonth());
			q.setParameter(2, temp.getYear());
			
			long count = (long) q.uniqueResult();
			System.out.println("Count: " +  count);
			if(count == 0 )
			{
				results.add(temp);
			}
			else
			{
				System.out.println("Already Processed");
				temp.setStatus("Processed");
				//results.remove(i);
				DatabaseConnections.saveObj(temp);
			}
		
		}
		
		return results;
	}

	private static void getTransactionnValues(XSSFSheet sheet) {
		// TODO Auto-generated method stub
		
		
		Row r = sheet.getRow(start_row);
		boolean flag = true;
		
		for(int i =0; flag == true; ++i)
		{
			Cell c = r.getCell(i);
			DataFormatter dataFormatter = new DataFormatter();
			String formattedCellStr = dataFormatter.formatCellValue(c);
			if(!formattedCellStr.isEmpty())
			{
				System.out.println("Index: " + i +" Value: " + formattedCellStr);
				row_cell_values.put(i, formattedCellStr);

			}
			if(formattedCellStr.equalsIgnoreCase("Balance"))
			{
				flag = false;
			}
			
		}
		for( int x: row_cell_values.keySet())
		{
			System.out.println(x);
			System.out.println(row_cell_values.get(x));
		}
	//	System.out.println(c1.getStringCellValue());
				
	
		
 
	for(int i: row_cell_values.keySet())
	{
		
		for(int j = start_row + 1; j< end_row; ++j)
		{
			Cell cell  = sheet.getRow(j).getCell(i);
			
		
			DataFormatter dataFormatter = new DataFormatter();
			String formattedCellStr = dataFormatter.formatCellValue(cell);
			
			switch(row_cell_values.get(i).toUpperCase())
			{
			case "DATE":
				getDateValues(formattedCellStr);                     //0
				break;
			case "MODE":
				getModeValues(formattedCellStr, j, sheet, i);        //1
				break;
			case "PARTICULARS":
				getDescValues(formattedCellStr, j, sheet, i);                     //2
				break;
			case "DEPOSITS":
				getDepositValues(formattedCellStr, j, sheet, i);     //4
				break;
			case "WITHDRAWALS":
				getWithdrawalValues(formattedCellStr, j, sheet, i);  //5
				break;
			case "BALANCE":
				getBalanceValues(formattedCellStr);                  //6
				break;
			}
//			System.out.println(formattedCellStr);
			
	//		getDateValues(formattedCellStr);                     //0
	//		getModeValues(formattedCellStr, j, sheet, i);        //1
	//		getDescValues(formattedCellStr);                     //2
	//		getDepositValues(formattedCellStr, j, sheet, i);     //4
	//		getWithdrawalValues(formattedCellStr, j, sheet, i);  //5
	//		getBalanceValues(formattedCellStr);                  //6
		
		
			
		}
	}

		
	}

	private static void printTransactions() {
		// TODO Auto-generated method stub
		
		for(int i = 0; i < dates.size(); ++i)
		{
			System.out.println("Date: " + dates.get(i));
			
			System.out.println("Modes: " + modes.get(i));
			System.out.print(" ");
			System.out.println("Particulars: " + description.get(i));
			System.out.print(" ");
			System.out.println("Deposits: " + deposit.get(i));
			System.out.print(" ");
			System.out.println("Withdrawals: " + withdrawals.get(i));
			System.out.print(" ");
			System.out.println("Balance: " + balance.get(i));
			System.out.println("-------------------------------------------------");
		}
		
	}

	private static void getBalanceValues(String str) {
		// TODO Auto-generated method stub
		
		if(str != null && !str.isEmpty())
		{
			System.out.println("True: " + str);
			balance.add(str);
			System.out.println("Balance Size: " + balance.size());
		}
		
	}

	private static void getWithdrawalValues(String str, int row_num, XSSFSheet sheet, int col_num) {
		// TODO Auto-generated method stub
		Row r = sheet.getRow(row_num);
		Cell c = r.getCell(col_num);
		Object[] temp_arr = row_cell_values.keySet().toArray();
		int diff = (int) temp_arr[4] - (int) temp_arr[0];
		System.out.println("Wihtdrawal to Deposit: " + diff);
		Cell c1 = r.getCell(col_num - diff);
		DataFormatter dataFormatter = new DataFormatter();
		String date = dataFormatter.formatCellValue(c1);
		String withdrawl = dataFormatter.formatCellValue(c);
	//	System.out.println("Date" + date);
		if(isDate(date))
		{
			System.out.println("True: " + withdrawl);
			//deposit.add(dep);
			withdrawals.add(withdrawl);
		}
		
		System.out.println("Withdrawals Size: " + withdrawals.size());

		
	}

	private static void getDepositValues(String str, int row_num, XSSFSheet sheet, int col_num) {
		// TODO Auto-generated method stub
		Row r = sheet.getRow(row_num);
		Cell c = r.getCell(col_num);
		Object[] temp_arr = row_cell_values.keySet().toArray();
		int diff = (int) temp_arr[3] - (int) temp_arr[0];
		System.out.println("Deposit to Date: " + diff);
		Cell c1 = r.getCell(col_num - diff);
		DataFormatter dataFormatter = new DataFormatter();
		String date = dataFormatter.formatCellValue(c1);
		String dep = dataFormatter.formatCellValue(c);
		System.out.println("Date" + date);
		if(isDate(date))
		{
			System.out.println("True: " + dep);
			deposit.add(dep);
		}
		System.out.println("Deposit Size: " + deposit.size());
	}

	private static void getDescValues(String str, int row_num, XSSFSheet sheet, int col_num) {
		// TODO Auto-generated method stub
	/*	Row r = sheet.getRow(row_num);
		
		Cell c1 = r.getCell(col_num - 2);
		DataFormatter dataFormatter = new DataFormatter();
		String date = dataFormatter.formatCellValue(c1);
		
		System.out.println("Date" + date);
		if(isDate(date))
		{
			System.out.println("True: " + str);
			description.add(str);
			System.out.println("Description Size: " + description.size());
		}*/
		if(!str.isEmpty() || str != null)
		{
			System.out.println("True: " + str);
			description.add(str);
			System.out.println("Description Size: " + description.size());
		}
		
	}

	private static void getModeValues(String str, int row_num, XSSFSheet sheet, int col_num) {
		// TODO Auto-generated method stub
		Row row = sheet.getRow(row_num);
		Cell cell = row.getCell(col_num);
		Object[] temp_arr = row_cell_values.keySet().toArray();
		int diff = (int) temp_arr[1] - (int) temp_arr[0];
		
		System.out.println("Mode to Date: " + diff);
		Cell c1 = row.getCell(col_num-diff);
		Cell c2 = row.getCell(col_num+diff);
		
		DataFormatter dataFormatter = new DataFormatter();
		String left_cell = dataFormatter.formatCellValue(c1);
		String right_cell = dataFormatter.formatCellValue(c2);
		String formattedCellStr = dataFormatter.formatCellValue(cell);

		if(checkAdjacentCells(left_cell,right_cell))
		{
			//System.out.println("True");
			System.out.println("True: " + formattedCellStr);
			modes.add(formattedCellStr);
			
			
			
		}
		else
		{
			System.out.println("False");
		}
	
		System.out.println("Modes Size: " + modes.size());	
		
		
	}



	private static boolean checkAdjacentCells(String left, String right) {
		// TODO Auto-generated method stub
		
	
		if(isDate(left)) 
		{
			return true;
		}
		else 
		{
			return false;
		}
		
	}

	private static void getDateValues(String str) {
		// TODO Auto-generated method stub
		
		if(isDate(str))
		{
			//System.out.println("True");
			System.out.println(str);
			Date d = null;
			try {
				d = new SimpleDateFormat("dd-MM-yyyy").parse(str);
				System.out.println(d);
			} catch (ParseException e) {
				System.out.println("Not able to Convert to Date");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dates.add(d);
			year.add(str.split("-")[2]);
			month.add(str.split("-")[1]);
			
		}
		else
		{
			//System.out.println("False");
		}
		System.out.println("Dates Size: " + dates.size());
		
	}

	private static boolean isDate(String str) {
		// TODO Auto-generated method stub
		if(str.equalsIgnoreCase("") || str.isEmpty() || str == null)
		{
			return false;
		}
		try {
			Date d = new SimpleDateFormat("dd-MM-yyyy").parse(str);
			//System.out.println(d.toString());
			return true;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return false;
		}
		
	}

	private static boolean isNumeric(String str)
	{
		try
		{
			Double.parseDouble(str);
			return true;
		}
		catch(Exception e)
		{
			return false;

		}
		
	}

	private static void getNoOfRows(XSSFSheet sheet) {
		// TODO Auto-generated method stub
	
		Iterator<Row> row_it = 	sheet.iterator();		
		
		boolean flag = false;
		while(row_it.hasNext())
		{
		
			Row row = row_it.next();
			Iterator<Cell> cell_it = row.cellIterator();
			
			while(cell_it.hasNext())
			{
				Cell cell = cell_it.next();
				DataFormatter dataFormatter = new DataFormatter();
				String formattedCellStr = dataFormatter.formatCellValue(cell);
				
				if(formattedCellStr.equalsIgnoreCase("mode"))
				{
				
					start_row = cell.getRowIndex();
					flag = true;
					
				}
				
				if(formattedCellStr.equalsIgnoreCase("Total:") && flag)
				{
					end_row = cell.getRowIndex();
				}
			//	System.out.println(formattedCellStr);
			}
			
			
		}


		
	}

}
