package com.itwasneo.activejapp.provider;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.activej.inject.annotation.Provides;
import org.bson.Document;

public record MongoRepositoryConfiguration(String databaseName, String collectionName, MongoClient c) {

	@Provides
	MongoDatabase mongoDatabase() { return c.getDatabase(databaseName); }

	@Provides
	MongoCollection<Document> mongoCollection(MongoDatabase db) { return db.getCollection(collectionName); }

}
