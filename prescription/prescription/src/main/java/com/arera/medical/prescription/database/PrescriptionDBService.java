package com.arera.medical.prescription.database;

import static com.mongodb.client.model.Updates.set;

import java.util.ArrayList;
import java.util.Collection;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.result.DeleteResult;

public class PrescriptionDBService {
	
	static String  collection_name="Prescription";

	static public void save_prescription(Document doc) {
		MongoCollection<Document> collection  = DbConfigure.mongoDatabase.getCollection(collection_name);
		collection.insertOne(doc);
	}
	
	static public String searchPrescription(String code, String language) {
		MongoCollection<Document> collection  = DbConfigure.mongoDatabase.getCollection(collection_name);
		Bson filter = Filters.and(Filters.eq("code", code),Filters.eq("language",language));
		Document newDod = collection.find(filter).first();
		if(newDod !=null) {
			return newDod.toJson();
		}else {
			return "";
		}
	}
	
	static public String deletePrescription(String code, String language) {
		MongoCollection<Document> collection  = DbConfigure.mongoDatabase.getCollection(collection_name);
		Bson filter = Filters.and(Filters.eq("code", code),Filters.eq("language",language));
		DeleteResult deleteResult = collection.deleteMany(filter);
		if(deleteResult !=null) {
			return deleteResult.toString();
		}else {
			return "";
		}
	}
	
	static public String searchPrescriptionByLanguage(String language) {
		Collection<String> codeList=new ArrayList<>();
		MongoCollection<Document> collection  = DbConfigure.mongoDatabase.getCollection(collection_name);
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
	
	static public void update_prescription(String code, String language, Document doc) {
		MongoCollection<Document> collection  = DbConfigure.mongoDatabase.getCollection(collection_name);
		Bson filter = Filters.and(Filters.eq("code", code),Filters.eq("language",language));
		
		Bson updateOperation = set("description", doc.getString("description"));
		collection.updateOne(filter, updateOperation);
	}
	
	static public long count_prescription(String code) {
		MongoCollection<Document> collection  = DbConfigure.mongoDatabase.getCollection(collection_name);
		Bson filter = Filters.eq("code", code);
		@SuppressWarnings("deprecation")
		long count = collection.count(filter);
		return count;
		
	}
	
	static public long count_prescription(String code, String language) {
		MongoCollection<Document> collection  = DbConfigure.mongoDatabase.getCollection(collection_name);
		Bson filter = Filters.and(Filters.eq("code", code),Filters.eq("language",language));
		@SuppressWarnings("deprecation")
		long count = collection.count(filter);
		return count;
		
	}
	
	
}
