package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class excel {
	static int start_row;
	static int end_row;
	static int columns = 5;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String excelPath = "D:\\PEM - Documents\\Statement_2021MTH12_940555767-unlocked.xlsx";
		System.out.println(excelPath);
		try {
			FileInputStream inp = new FileInputStream(new File(excelPath));
			XSSFWorkbook workbook = new XSSFWorkbook(inp);
			XSSFSheet sheet = workbook.getSheetAt(0);
		
			getNoOfRows(sheet);
						
			System.out.println("Start Row: " + start_row );
			System.out.println("End Row: " + end_row);
		
		
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Workbook wb = WorkbookFactory.create(inp);
 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				System.out.println(formattedCellStr);
			}
			
			
		}


		
	}

}
