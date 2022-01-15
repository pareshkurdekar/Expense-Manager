package com.main;

import java.io.File;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DatabaseConnections {
	
	
	public static Session session = null;
	public static SessionFactory sessionFactory = null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//startSession();
	//	closeSession();
		

	}

	public static void closeSession() {
		// TODO Auto-generated method stub
		
		session.close();
		sessionFactory.close();
		System.out.println("Session Closed!");
		
		
	}

	public static void startSession() {
		// TODO Auto-generated method stub
		sessionFactory = new Configuration()
				.configure(new File("tools\\hibernate.cfg.xml")).buildSessionFactory();
		session = 	sessionFactory.openSession();
		System.out.println("Session Opened!");
		
	}
	public static void saveObj(Object obj)
	{
		try
		{
			session.beginTransaction();
			session.saveOrUpdate(obj);
			session.getTransaction().commit();
			System.out.println("Object Pushed to DB!");
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Unable to Push to DB!");
		}
	}

}
