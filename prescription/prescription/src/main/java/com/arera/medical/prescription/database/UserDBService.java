package com.arera.medical.prescription.database;

import static com.mongodb.client.model.Updates.set;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.arera.medical.prescription.model.Role;
import com.arera.medical.prescription.model.User;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class UserDBService {
	
	static public  User findByEmail(String email) {
		
		User user=new User();
		
		MongoCollection<Document> collection  = DbConfigure.mongoDatabase.getCollection("Account");
		
		Bson filter = Filters.and(Filters.eq("email", email),Filters.eq("status", "approved"));
		Document newDod = collection.find(filter).first();
		if(newDod!=null) {
			System.out.println(email + " is found");
			user = docToUser(newDod);
		}else {
			System.out.println(email + " is not foud found");
			return null;
		}
		
		return user;
	}
	
	static public User docToUser(Document newDod) {
		User user=new User();
		Collection<Role> roles= new ArrayList<Role>();
		
		user.setFirstName(newDod.getString("firstName"));      
		user.setLastName(newDod.getString("lastName"));
		user.setEmail(newDod.getString("email"));
		user.setPassword(newDod.getString("password"));
		user.setStatus(newDod.getString("status"));
		
		@SuppressWarnings("unchecked")
		List<Document> roledoc = newDod.get("roles", List.class);
		//Collection<Document> roledoc = newDod.get("roles", CollectionDocument);
	    for (Document d : roledoc) {
	    	Role role = new Role();
	    	role.setName(d.getString("name"));
	    	roles.add(role);
	    	
	    }
		
	    user.setRoles(roles);
		return user;
		
	}
	
	static public  User save(User user) {
		MongoCollection<Document> collection  = DbConfigure.mongoDatabase.getCollection("Account");
		collection.insertOne(ConvertJava.getUserDbObject(user));
		return user;
		
	}
	
	
	
	//static public void deletByEmail() {
		
	//}
	
	static public String serchBySatus(String status) {
		
		Collection<String> emailList=new ArrayList<>();
		MongoCollection<Document> collection  = DbConfigure.mongoDatabase.getCollection("Account");
		
		Bson filter = Filters.eq("status", status);
		FindIterable<Document> newDods = collection.find(filter).projection(Projections.include("email"));
		if(newDods !=null) {
			for(Document newDod: newDods) {
				String resutl = newDod.getString("email");
				emailList.add(resutl);
			}
			return emailList.toString();
		}else {
			return "";
		}
		
		
	}
	
	static public String serchByRole(String role) {
		
		Collection<String> emailList=new ArrayList<>();
		MongoCollection<Document> collection  = DbConfigure.mongoDatabase.getCollection("Account");
		
		Bson filter = Filters.eq("roles.name", role);
		FindIterable<Document> newDods = collection.find(filter).projection(Projections.include("email"));
		if(newDods !=null) {
			for(Document newDod: newDods) {
				String resutl = newDod.getString("email");
				emailList.add(resutl);
			}
			return emailList.toString();
		}else {
			return "";
		}
		
	}
	
	static public String updateStatus(String email, String status) {
		
		MongoCollection<Document> collection  = DbConfigure.mongoDatabase.getCollection("Account");
		
		Bson filter = Filters.eq("email", email);
		Document newDod = collection.find(filter).first();
		if(newDod!=null) {
			Bson updateOperation = set("status", status);
			UpdateResult updateResult = collection.updateOne(filter, updateOperation);
			System.out.println(updateResult);
			return updateResult.toString();
		}	
		return null;
		
	}
	
	static public String searchByEmail(String email) {

		MongoCollection<Document> collection  = DbConfigure.mongoDatabase.getCollection("Account");
		
		Bson filter = Filters.eq("email", email);
		Document newDod = collection.find(filter).projection(Projections.excludeId()).first();
		if(newDod != null) {
			newDod.remove("password");
			return newDod.toJson();
		}else {
			return "";
		}
		
	}
	
	static public String deleteByEmail(String email) {
		
		
		MongoCollection<Document> collection  = DbConfigure.mongoDatabase.getCollection("Account");
		
		Bson filter = Filters.eq("email", email);
		Document newDod = collection.find(filter).first();
		if(newDod!=null) {
			DeleteResult deleteResult = collection.deleteMany(filter);
			return deleteResult.toString();
		}else {
			return null;
		}
	}
	
	static public String updateRole(String email, String role) {
		DbConfigure.configureDatabase();
		
		
		MongoCollection<Document> collection  = DbConfigure.mongoDatabase.getCollection("Account");
		
		Bson filter = Filters.eq("email", email);
		Document newDod = collection.find(filter).first();
		if(newDod!=null) {
			Bson update = new Document()
	                .append("roles.0.name", role);
			Bson set_bson = new Document().append("$set", update);
			UpdateResult updateResult = collection.updateOne(filter, set_bson, new UpdateOptions());
			return updateResult.toString();
		}else {
			return null;
		}
	}

}
