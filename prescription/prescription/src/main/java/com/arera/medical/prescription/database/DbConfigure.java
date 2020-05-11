package com.arera.medical.prescription.database;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

@Service
public class DbConfigure {
	
	public static MongoClient mongoClient;
	
	public static MongoDatabase mongoDatabase;
	public static String start="";
	
	static String IP = "127.0.0.1";
	static int port = 27017;
	
	public static String DBName = "prescription_database";
	
	
	
	public static void configureDatabase() {
		//this is just local configuration
		mongoClient = new MongoClient(IP, port);
		mongoDatabase = mongoClient.getDatabase(DBName);
	}
	
	

	public static Boolean CheckDatabase(String DBname) {
		//this is just local configuration
		mongoClient = new MongoClient();
		@SuppressWarnings("deprecation")
		List<String> database = mongoClient.getDatabaseNames();
		Boolean dbStatus = database.contains(DBname);
		return dbStatus;
	}
	

}
