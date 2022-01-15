package unlockPDF;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.hibernate.internal.build.AllowSysOut;

public class decryptPDF {
	
	public static void main(String[] args) {
		
		unlockOperations("D:\\PEM - Documents\\Bank Statements", "D:\\PEM - Documents\\Bank Statements\\Unlocked Bank Statements", "007201521489");
		
	/*	System.out.println("Testing");
		
		String inputPDF = "D:\\PEM - Documents\\PEM - PDFs\\Statement_2021MTH12_940555767.pdf";
		String outputPdf = "D:\\PEM - Documents\\PEM - PDFs\\unlocked.pdf";
		File out = new File(outputPdf);
		File inp = new File(inputPDF);
		
		try {
			PDDocument pdd = PDDocument.load(inp, "007201521489");
			
			pdd.setAllSecurityToBeRemoved(true);
			pdd.save(out);
			pdd.close();
			System.out.println("Decrypting Done!");
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		

	}

	public static void unlockOperations(String lockedPdfFilePath, String unlockedPdfFilePath, String password) {
		// TODO Auto-generated method stub
		
	//	System.out.println(lockedPdfFilePath);
	//	System.out.println(unlockedPdfFilePath);
		
		File f = new File(lockedPdfFilePath);
		for(File file: f.listFiles())
		{
			
			if(file.isFile())
			{
				
				if(isUnlocked(file.getName(),unlockedPdfFilePath))
				{
					
					System.out.println(file.getName());
					System.out.println("File already Unlocked");
				}
				else
				{
					System.out.println(file.getName());
					System.out.println("File Locked");
					unlockPDF(file,unlockedPdfFilePath, password);
					
				}
			}
		}
		
		
	}

	private static void unlockPDF(File file, String unlockedPdfFilePath, String password) {
		// TODO Auto-generated method stub
		 
		
		try {
			System.out.println("Unlocking PDF!!");
			PDDocument pdd = PDDocument.load(file, password);
			
			pdd.setAllSecurityToBeRemoved(true);
			
			File out = getOutputFile(file,unlockedPdfFilePath);
			
			pdd.save(out);
			pdd.close();
			System.out.println("Decrypting Done!");
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static File getOutputFile(File file, String unlockedPdfFilePath) {
		// TODO Auto-generated method stub
		String input = file.getName();
		String output = input.substring(0, input.indexOf(".pdf"));
		output = output+"_unlocked.pdf";
		File out = new File(unlockedPdfFilePath+"\\"+output);
		
		return out;
	}

	private static boolean isUnlocked(String fileName, String unlockedPdfFilePath) {
		// TODO Auto-generated method stub
		boolean flag = false;
		File f = new File(unlockedPdfFilePath);
		for(File file: f.listFiles())
		{
			if(file.isFile())
			{
			//	System.out.println(file.getName());
				
				if(file.getName().contains(fileName.substring(0,fileName.indexOf(".pdf"))))
				{
				//	System.out.println("Already Unlocked!");
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

}
