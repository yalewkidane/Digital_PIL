package com.arera.medical.prescription.database;



import org.bson.Document;
import org.bson.conversions.Bson;

import com.arera.medical.prescription.model.User;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import  com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import static com.mongodb.client.model.Updates.*;

import java.util.ArrayList;
import java.util.Collection;

public class test {

	//public static void main(String[] args) {
	//	String email="zz";
		//findOne("Account");
		//insertUser(email);
		//findByEmail(email);
		//resetCollection("Account");
		//updateStatus(email);
		//updateRole(email, "ADMIN");
		//deleteByEmail(email);
		//System.out.println(serchByRole("USER"));
		//update_prescription();
		//serchBycode("4545");
		
	//	System.out.println(searchPrescriptionByLanguage("english"));
		
		

	//}
	
	static public String searchPrescriptionByLanguage(String language) {
		DbConfigure.configureDatabase();
		Collection<String> codeList=new ArrayList<>();
		MongoCollection<Document> collection  = DbConfigure.mongoDatabase.getCollection("Prescription");
		Bson filter = Filters.eq("language", language);
		FindIterable<Document> newDods = collection.find(filter).projection(Projections.include("code"));
		if(newDods !=null) {
			for(Document newDod: newDods) {
				String resutl = newDod.getString("code");
				codeList.add(resutl);
			}
			return codeList.toString();
		}else {
			return "";
		}
	}
	
	
	static public String serchBycode(String code) {
		DbConfigure.configureDatabase();
		
		
		MongoCollection<Document> collection  = DbConfigure.mongoDatabase.getCollection("Prescription");
		
		Bson filter = Filters.eq("code", code);
		Document newDod = collection.find(filter).first();
		if(newDod !=null) {
			System.out.println(newDod.toJson());
			return "";
		}else {
			return "{errror}";
		}
		
	}
	
	static public void update_prescription() {
		
		Document doc =new Document();
		doc.append("code", "4545");
		doc.append("language", "english");
		doc.append("description", "text description");
		
		DbConfigure.configureDatabase();
		String  collection_name="Prescription";
		MongoCollection<Document> collection  = DbConfigure.mongoDatabase.getCollection(collection_name);
		Bson filter = Filters.and(Filters.eq("code", "4545"),Filters.eq("language","english"));
		
		Bson updateOperation = set("description", doc.getString("description"));
		UpdateResult updateResult = collection.updateOne(filter, updateOperation);
	}
	
	
	static void resetCollection(String collection_name) {
		DbConfigure.configureDatabase();
		MongoCollection<Document> collection  = DbConfigure.mongoDatabase.getCollection(collection_name);
		collection.drop();
		
	}
	
	static void findOne(String collection_name) {
		DbConfigure.configureDatabase();
		MongoCollection<Document> collection  = DbConfigure.mongoDatabase.getCollection(collection_name);
		Document newDod = collection.find().first();
		System.out.println(newDod.toJson());
	}
	
	static void insertUser(String test) {
		
		DbConfigure.configureDatabase();
		MongoCollection<Document> collection  = DbConfigure.mongoDatabase.getCollection("Account");
		
		
		User us=new User();
		
		us.setEmail(test);
		us.setFirstName(test);
		us.setLastName(test);
		us.setPassword(test);
		
		collection.insertOne(ConvertJava.getUserDbObject(us));
		
	}
	
	static public void findByEmail(String email) {
		DbConfigure.configureDatabase();
		
		User user=new User();
		
		MongoCollection<Document> collection  = DbConfigure.mongoDatabase.getCollection("Account");
		
		Bson filter = Filters.and(Filters.eq("email", email),Filters.eq("status", "approved"));
		Document newDod = collection.find(filter).first();
		System.out.println(newDod.toJson());
		
		user = UserDBService.docToUser(newDod);
		
		System.out.println(user.getRoles().iterator().next().getName());
		
		
	}
	
	static public void updateStatus(String email, String status) {
		DbConfigure.configureDatabase();
		
		
		MongoCollection<Document> collection  = DbConfigure.mongoDatabase.getCollection("Account");
		
		Bson filter = Filters.eq("email", email);
		Document newDod = collection.find(filter).first();
		if(newDod!=null) {
			Bson updateOperation = set("status", status);
			UpdateResult updateResult = collection.updateOne(filter, updateOperation);
			System.out.println(updateResult);
		}	
		
	}
	
	static public void updateRole(String email, String role) {
		DbConfigure.configureDatabase();
		
		
		MongoCollection<Document> collection  = DbConfigure.mongoDatabase.getCollection("Account");
		
		Bson filter = Filters.eq("email", email);
		Document newDod = collection.find(filter).first();
		if(newDod!=null) {
			Bson update = new Document()
	                .append("roles.0.name", role);
			Bson set_bson = new Document().append("$set", update);
			UpdateResult updateResult = collection.updateOne(filter, set_bson, new UpdateOptions());
			System.out.println(updateResult);
		}
	}
	
	static public void deleteByEmail(String email) {
		DbConfigure.configureDatabase();
		
		
		MongoCollection<Document> collection  = DbConfigure.mongoDatabase.getCollection("Account");
		
		Bson filter = Filters.eq("email", email);
		Document newDod = collection.find(filter).first();
		if(newDod!=null) {
			DeleteResult deleteResult = collection.deleteMany(filter);
			System.out.println(deleteResult);
		}
	}
	
	static public String serchBySatus(String status) {
		DbConfigure.configureDatabase();
		
		
		MongoCollection<Document> collection  = DbConfigure.mongoDatabase.getCollection("Account");
		
		Bson filter = Filters.eq("status", status);
		Document newDod = collection.find(filter).first();
		if(newDod !=null) {
			return newDod.toJson();
		}else {
			return "{}";
		}
		
	}
	
	static public String serchByRole(String role) {
		DbConfigure.configureDatabase();
		
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
	
	static void save() {
		// TODO Auto-generated method stub
				DbConfigure.configureDatabase();
				
				User us=new User();
				
				us.setEmail("email_1");
				us.setFirstName("didi");
				us.setLastName("last");
				us.setPassword("d1");

				
				MongoCollection<Document> collection  = DbConfigure.mongoDatabase.getCollection("Account");
				
				//Bson filter = Filters.and(Filters.eq("field1", "value"), Filters.gt("field2", "value2"));
				
				Bson filter = Filters.eq("email", us.getEmail());
				Document newDod = collection.find(filter).first();
				if(newDod!=null) {
					System.out.println("email already existes");
					
				}else {
					try {
						collection.insertOne(ConvertJava.getUserDbObject(us));
						System.out.println("Account registerd");
					}catch(Exception E) {
						System.out.println("unable to save");
					}
					
				}
				
				
				//collection.insertOne(ConvertJava.getUserDbObject(us));
			
				//Document newDod = collection.find().first(); 
				//System.out.println(newDod.getString("name"));
				
				
				//JSONObject jsonobject= newDod.toJson(). ;
				//System.out.println(newDod.toJson());
	}

}
