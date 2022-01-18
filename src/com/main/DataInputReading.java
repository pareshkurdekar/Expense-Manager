package com.main;

import java.io.File;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;

import tableDAO.data_input_table;
import tableDAO.data_read_table;

public class DataInputReading {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			DatabaseConnections.startSession();
		//	checkForExcel();
		//	DatabaseConnections.closeSession();
		
		//	pushExcelToDB("D:\\PEM - Documents\\Excel Sheets");
		 HashMap<String, Double> chartValues = new HashMap<String, Double>();
		

		getValuesForCharts("deposit", chartValues, "09", "2021");
		 DatabaseConnections.closeSession();
		
	}

	private static String setCategoryValues(String mode, String description, List<String> category) {
		
		String temp = "";
		if(mode == null || mode.isEmpty())
		{
			temp = description;
		}
		else
		{
			 temp = mode + " " + description;
		}
		
	
		
		for( String s: category)
		{
			if(temp.toUpperCase().contains(s.toUpperCase()))
			{
			
				System.out.println(s);
				if(s.toUpperCase().equalsIgnoreCase("Earned for usin"))
				{
					return "GOOGLE REWARDS";
				}
				if(s.toUpperCase().equalsIgnoreCase("BKID0008176"))
				{
					return "LOAN EMI";
				}
				if(s.toUpperCase().equalsIgnoreCase("kurdekar.pdmny"))
				{
					return "SISTER";
				}
				if(s.toUpperCase().equalsIgnoreCase("chandrakurdekar"))
				{
					return "MOM";
				}
				if(s.toUpperCase().equalsIgnoreCase("ICSP"))
				{
					return "ICICI SALARY";
				}
				
				return s;
				
			}
				
		}
		
		
		System.out.println(temp);
		return temp;
		// TODO Auto-generated method stub
		
	}

	public static void pushToDB(List<Date> dates, List<String> modes, List<String> description, List<String> deposit,
			List<String> withdrawals, List<String> balance, List<String> year, List<String> month, List<String> category) {
		// TODO Auto-generated method stub
		
		int size = dates.size();
	//	Timestamp ts = null;
		for(int i = 0; i < size; ++i)
		{
			data_input_table obj = new data_input_table();
		//	System.out.println(dates.get(i).getTime());
			//ts = new Timestamp(dates.get(i).getTime());
			obj.setDate(new Timestamp(dates.get(i).getTime()));  // Set Date
		//	obj.setDate(ts);
			
			
			System.out.println(String.valueOf(dates.get(i)));
			obj.setMode(modes.get(i));                           // Set Modes
			obj.setDescription(description.get(i));              // Set Description
			obj.setDeposit(deposit.get(i));                      // Set Deposit
			obj.setWithdrawal(withdrawals.get(i));               // Set Withdrawals
			obj.setBalance(balance.get(i));                      // Set Balance
			
			obj.setTx_month(month.get(i));
					
			obj.setTx_year(year.get(i));
			
			String cat = DataInputReading.setCategoryValues(modes.get(i), description.get(i), category);
			
			obj.setCategory(cat);
			
			DatabaseConnections.saveObj(obj);
		}
		
	}

	public static void checkForExcel() {
		// TODO Auto-generated method stub
		
		String excelFilePath = "D:\\PEM - Documents";
		File f = new File(excelFilePath);
		for( File file: f.listFiles())
		{
			if(file.isFile())
			{
				System.out.println(file.getName());
				
				// Check if it is in DB

				if(isNotPresentInDB(file.getName()))
				{
					System.out.println(file.getName() + " File Not Present");
					// Push to DB
					data_read_table obj = new data_read_table();
					obj.setFile_name(file.getName());
					obj.setFile_path(file.getAbsolutePath());
					obj.setStatus("In Process");
					obj.setRead_time(Timestamp.from(Instant.now()));
					String[] x = file.getName().split("_")[1].split("MTH");
					obj.setMonth(x[1]);
					obj.setYear(x[0]);
					
					DatabaseConnections.saveObj(obj);
					
				}
				else
				{
					System.out.println(file.getName() + " File Present");
					// Do Nothing
				}
				
				
				
				
				
			}
		}
		
	}

	private static boolean isNotPresentInDB(String name) {
		// TODO Auto-generated method stub
		
		Query q = DatabaseConnections.session.createQuery("Select count(file_name) from data_read_table where file_name = ?1");
		q.setParameter(1, name.trim());
		long count = (long) q.uniqueResult();
		System.out.println("Count: " + count);
		if(count == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static void pushExcelToDB(String excelFilePath) {
		// TODO Auto-generated method stub
		File f = new File(excelFilePath);
		for(File file: f.listFiles())
		{
			if(file.isFile())
			{
				System.out.println(file.getName());
				if(isExcelInDB(file.getName())) 
				{
					System.out.println("Already Present in DB");
				}
				else
				{
					System.out.println("Not in DB");
					
					data_read_table obj = new data_read_table();
					obj.setFile_name(file.getName());
					obj.setFile_path(file.getAbsolutePath());
					obj.setStatus("In Process");
					obj.setRead_time(Timestamp.from(Instant.now()));
					String[] x = file.getName().split("_")[1].split("MTH");
					obj.setMonth(x[1]);
					obj.setYear(x[0]);
					obj.setMail_send("Pending");
					DatabaseConnections.saveObj(obj);
					
				}
				
			}
		}
		
	}

	private static boolean isExcelInDB(String name) {
		// TODO Auto-generated method stub
	
		data_read_table obj = new data_read_table();
		try 
		{
			 obj = DatabaseConnections.session.get(data_read_table.class, name);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
		if(obj != null)
		{
			return true;
		}
		
		return false;
	}

	public static void getValuesForCharts(String title, HashMap<String, Double> chartValues, String month, String year) {
		// TODO Auto-generated method stub
		chartValues.clear();
		Query q =  DatabaseConnections.session.createQuery("from data_input_table where tx_month = ?1 and tx_year = ?2 and coalesce(" +title+", '') != ''" );
	//	Query q =  DatabaseConnections.session.createQuery("from data_input_table where tx_month = ?1 and tx_year = ?2 and coalesce( ?0 , '') != ''" );
	//	q.setParameter(0, title.trim());
		q.setParameter(1, month);
		q.setParameter(2, year);
		
		 List<data_input_table> obj = q.list();
		System.out.println(obj.size());
		System.out.println(q.getQueryString());
		Double value = 0.0;
		for( data_input_table row: obj)
		{
			
		if(title.equalsIgnoreCase("withdrawal"))
		{
			value = Double.parseDouble( row.getWithdrawal());
			
		}
		else if (title.equalsIgnoreCase("deposit"))
		{
			value = Double.parseDouble( row.getDeposit());

		}
		
			
			if(chartValues.containsKey(row.getCategory()))
			{
				chartValues.put(row.getCategory(), chartValues.get(row.getCategory()) + value );
				//chartValues.put(row.getCategory(), 2.3);
			}
			else
			{
				chartValues.put(row.getCategory(), value);
			}
			
		}
		
	
		System.out.println(title);
		
		
		
	}
		


}
