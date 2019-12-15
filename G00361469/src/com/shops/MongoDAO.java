package com.shops;

import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDAO {
	
	String mongoDB = "storeHeadOfficeDB";
	String mongoCollection = "storeHeadOffice";
	
	MongoClient mongoClient;
	MongoDatabase database;
	MongoCollection<Document> collection;
	
	
	/* ======================================================================================================
	 * Constructor
	 * ====================================================================================================== */
	public MongoDAO() throws Exception {
		mongoClient = new MongoClient();
		database = mongoClient.getDatabase(mongoDB);
		collection = database.getCollection(mongoCollection);
	}
	
	// Load head offices into array list
	public ArrayList<HeadOffice> loadHeadOffices() {
		ArrayList<HeadOffice> hOffices = new ArrayList<HeadOffice>();
		
		FindIterable<Document> headOffices = collection.find();
	    
		for (Document d : headOffices) {
			HeadOffice ho = new HeadOffice();
		    ho.set_id(d.getInteger("_id"));
		    ho.setLocation(d.getString("location"));
		    hOffices.add(ho);
	    }		
	    
	    return hOffices;
	}
	
	// Add a new Head Office to the database
	public void addHeadOffice(HeadOffice headOffice) throws Exception {
		Document newOffice = new Document();
		newOffice.put("_id", headOffice.get_id());
		newOffice.put("location", headOffice.getLocation());
		
		
		collection.insertOne(newOffice);
		System.out.println("MONGO ADDED OFFICE");
	}
	
	// Check if the store Head Office already has a location
	public boolean hasLocation(int storeId) {
		FindIterable<Document> headOffices = collection.find();
	    
		for (Document d : headOffices) {
		    if(d.getInteger("_id") == storeId) {
		    	if(d.getString("location") != null && !d.getString("location").isEmpty())
		    		return true;	
		    }
	    }		
		
		return false;
	}
}
