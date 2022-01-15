package excelConversion;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class pdfTableAPI {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		convertPdfToExcel("D:\\PEM - Documents\\Bank Statements\\Unlocked Bank Statements",
"https://pdftables.com/api?format=xlsx-single&key=","4b6xsc8of5ob","D:\\PEM - Documents\\Excel Sheets");
	/*	String apiKey = "4b6xsc8of5ob";
		String format = "xlsx-single";
		String url = "https://pdftables.com/api?format=" + format + "&key=" + apiKey;
		System.out.println("URL: " + url);

		String pdfPath = "D:\\PEM - Documents\\PEM - PDFs\\Statement_2021MTH09_940555767-unlocked.pdf";
		File pdfFile = new File(pdfPath);
		

		if(pdfFile.canRead())
		{
			System.out.println("File can be read");
		}
		
		RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();

		try 
		{
			CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
			HttpPost httppost = new HttpPost(url);
			
			FileBody fileBody = new FileBody(pdfFile);
			
			HttpEntity requestBody = MultipartEntityBuilder.create().addPart("f", fileBody).build();
			httppost.setEntity(requestBody);
			
			System.out.println("Sending Request!");
			
			CloseableHttpResponse response = httpclient.execute(httppost);
			
			if (response.getStatusLine().getStatusCode() != 200) {
				System.out.println(response.getStatusLine());
				System.exit(1);
			}
			HttpEntity resEntity = response.getEntity();
			if(resEntity != null)
			{
				String excelFileName = "D:\\PEM - Documents\\excel.xlsx";
				File f = new File(excelFileName);
				
				
				
				FileUtils.copyToFile(resEntity.getContent(), f);
			///	Files.copy(inp, f.toPath(), StandardCopyOption.REPLACE_EXISTING);
				
			}
			else
			{
				System.out.println("Didn't Work");
			}
		}
		catch(Exception e)
		{
			
		}
		*/
		

		
		
	}

	public static void convertPdfToExcel(String unlockedPdfFilePath, String url, String apiKey, String excelFilePath) {
		// TODO Auto-generated method stub
		
		File f = new File(unlockedPdfFilePath);
		
		for( File file: f.listFiles())
		{
			if(file.isFile())
			{
				System.out.println(file.getName());
				if(isConvertedToExcel(excelFilePath, file.getName()))
				{
					System.out.println("Already Converted");
				}
				else
				{
					System.out.println("Converting to Excel");
					convertToExcel(url, apiKey, file, excelFilePath);
				}
				
			}
		}
		
	}

	private static void convertToExcel(String url, String apiKey, File pdfFile, String excelFilePath) {
		// TODO Auto-generated method stub
		
		System.out.println(url + apiKey);
		


		if(pdfFile.canRead())
		{
			System.out.println("File can be read");
		}
		
		RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();

		try 
		{
			CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
			HttpPost httppost = new HttpPost(url+apiKey);
			
			FileBody fileBody = new FileBody(pdfFile);
			
			HttpEntity requestBody = MultipartEntityBuilder.create().addPart("f", fileBody).build();
			httppost.setEntity(requestBody);
			
			System.out.println("Sending Request!");
			
			CloseableHttpResponse response = httpclient.execute(httppost);
			
			if (response.getStatusLine().getStatusCode() != 200) {
				System.out.println(response.getStatusLine());
				System.exit(1);
			}
			HttpEntity resEntity = response.getEntity();
			if(resEntity != null)
			{
			
				System.out.println("Copying to Excel Folder!");
				
				File out = getOutputExcelFile(excelFilePath, pdfFile.getName());
								
				FileUtils.copyToFile(resEntity.getContent(), out);
								
			}
			else
			{
				System.out.println("Didn't Work");
			}
		}
		catch(Exception e)
		{
		
			e.printStackTrace();
			
		}
		
		
	}

	private static File getOutputExcelFile(String excelFilePath, String name) {
		// TODO Auto-generated method stub
		
		String output = name.substring(0 , name.indexOf(".pdf"));
		File out = new File(excelFilePath + "\\" + output + ".xlsx");
		
		return out;
	}

	private static boolean isConvertedToExcel(String excelFilePath, String name) {
		// TODO Auto-generated method stub
		File f = new File(excelFilePath);
		boolean flag = false;
		for( File file: f.listFiles())
		{
			if(file.isFile())
			{
				
				if(file.getName().contains(name.substring(0, name.indexOf(".pdf"))))
				{
				//	System.out.println("Converted");
					flag = true;
					break;
				}
				
			}
		}
		
		return flag;
	}

}
