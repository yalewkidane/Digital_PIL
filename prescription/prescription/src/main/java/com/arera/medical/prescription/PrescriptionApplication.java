package com.arera.medical.prescription;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import com.arera.medical.prescription.database.DbConfigure;

@SpringBootApplication
@ComponentScan({"com.arera.medical.prescription"})
public class PrescriptionApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(PrescriptionApplication.class, args);
		
		DbConfigure.configureDatabase();
		
		try {
			if (!DbConfigure.CheckDatabase(DbConfigure.DBName)) {
				System.out.println("System admin initiating");
				DbConfigure.start="start";
			}
		}catch (Exception e){
	        
	    }
		
	}

}
