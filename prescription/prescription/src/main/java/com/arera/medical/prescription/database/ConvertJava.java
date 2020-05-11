package com.arera.medical.prescription.database;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.arera.medical.prescription.model.User;



public class ConvertJava {
	
	static Document getUserDbObject(User user) {
		Document userDBobject = new Document();
		userDBobject.append("firstName", user.getFirstName());
		userDBobject.append("lastName", user.getLastName());
		userDBobject.append("email", user.getEmail());
		userDBobject.append("password", user.getPassword());
		
		
		//userDBobject.append("status", "approved");
		if(DbConfigure.start.equals("start")) {
			userDBobject.append("status", "approved");
		}else {
			userDBobject.append("status", "unapproved");
		}
		
		Document role = new Document();
		
		if(DbConfigure.start.equals("start")) {
			role.append("name", "ADMIN");
			DbConfigure.start="";
		}else {
			role.append("name", "USER");
		}
		
		List<Document> roles = new ArrayList<Document>();;
		roles.add(role);
		
		userDBobject.append("roles", roles);
		
		
			
		return userDBobject;
	}

}
