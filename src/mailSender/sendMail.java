package mailSender;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.main.DatabaseConnections;

import tableDAO.data_read_table;

public class sendMail {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DatabaseConnections.startSession();
		send("D:\\PEM - Documents\\Bank Statements\\Unlocked Bank Statements","D:\\PEM - Documents\\Graphs");
		DatabaseConnections.closeSession();
	}

	public static void send(String unlockedPdfFilePath, String graphFilePath) {
		// TODO Auto-generated method stub
		
		String from = "";
		String to = "";
		String pwd = "";
		List<data_read_table> allObj = getMailPendingRecords();
		
		if(allObj == null || allObj.isEmpty())
		{
			System.out.println("No Emails to Send");
		}
		Properties eProp = new Properties();
		try {
			eProp.load(new FileInputStream("tools\\info.properties"));
			from = eProp.getProperty("from");
			to = eProp.getProperty("to");
			pwd = eProp.getProperty("pwd");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		String month = "";
		String year = "";
		String parser = "";
		String host = "smtp.gmail.com";
		
		Properties prop = System.getProperties();
		
		prop.put("mail.transport.protocol", "smtp");
	    prop.put("mail.smtp.starttls.enable", "true");
	    prop.put("mail.smtp.host", "smtp.gmail.com");

	    prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");

	    prop.put("mail.smtp.auth", "true");
	    prop.put("mail.smtp.port", "465");
	    prop.put("mail.smtp.ssl.enable", "true");
	    prop.put("mail.user", from);
	    prop.put("mail.password", pwd);
	  //  prop.put("mail.debug", "true");

	    Session session = Session.getInstance(prop);

	    //session.setDebug(true);

	
		
		for(int i = 0; i< allObj.size(); ++i)
		{
			data_read_table mailObj = allObj.get(i);
			System.out.println(mailObj.getMail_send());
			month = mailObj.getMonth();
			year = mailObj.getYear();
			parser = year + "MTH" + month;
			
			String attachments = getPDF(parser,unlockedPdfFilePath) + "," + getGraphs(parser, graphFilePath);
			
			System.out.println(attachments);
			try
			{
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(from));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
				message.setSubject("E-Statement Details :" + month + "-" + year);
				message.setText("TESTING");
				

				Multipart multipart = attachBodyParts(attachments);
				
			/*	BodyPart messageBodyPart = new MimeBodyPart();
		         Multipart multipart = new MimeMultipart();

				String filename = "D:\\PEM - Documents\\Graphs\\Deposits_2021MTH07.png";
				DataSource source = new FileDataSource(filename);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(filename);
				multipart.addBodyPart(messageBodyPart);
				*/

				message.setContent(multipart);
			
				Transport.send(message, from, pwd);
				System.out.println("Sent!");
				mailObj.setMail_send_attachments(attachments);
				mailObj.setMail_send("Sent");
				mailObj.setMail_send_time(Timestamp.from(Instant.now()));
				DatabaseConnections.saveObj(mailObj);

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			
			
		}
		
		
	}

	private static Multipart attachBodyParts(String attachments) {
		// TODO Auto-generated method stub
		Multipart multipart = new MimeMultipart();
		
		for(String file: attachments.split(","))
		{
			BodyPart messageBodyPart = new MimeBodyPart();

			DataSource source = new FileDataSource(file);
			try {
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(file.substring(file.lastIndexOf("\\")+1,file.length()));
				multipart.addBodyPart(messageBodyPart);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
		}
		
		
		
		return multipart;
	}

	private static String getGraphs(String parser, String graphFilePath) {
		// TODO Auto-generated method stub
		String temp = "";
		File f = new File(graphFilePath);
		for(File file: f.listFiles())
		{
			if(file.getName().contains(parser))
			{
				if(temp.isEmpty())
				{
					temp = file.getAbsolutePath();
				}
				else
				{
					temp = temp + "," + file.getAbsolutePath();
				}
				
			}
			
			
		}
		return temp;
	}

	private static String getPDF(String parser, String unlockedPdfFilePath) {
		// TODO Auto-generated method stub
		
		File f = new File(unlockedPdfFilePath);
		for(File file: f.listFiles())
		{
			if(file.getName().contains(parser))
			{
				return file.getAbsolutePath();
			}
			
			
		}
		System.out.println("No PDFs Found!");
		return null;
	}

	private static List<data_read_table> getMailPendingRecords() {
		// TODO Auto-generated method stub
		
		List<data_read_table> results = DatabaseConnections.session.createQuery("from data_read_table where mail_send = 'Pending' ").list();
		return results;
	}

}
