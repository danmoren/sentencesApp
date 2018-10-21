package com.danielmoreno.sentences.repository;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@DataMongoTest
public class WordsRepositoryTest {
    //Embedded Mongo?
    //Fongo?
    @Test
    public void testAggregate(@Autowired MongoTemplate mongoTemplate) {
        DBObject objectToSave = BasicDBObjectBuilder.start()
                .add("key","value")
                .get();
        mongoTemplate.save(objectToSave, "collection");
        Assert.assertNotNull(mongoTemplate.findAll(DBObject.class, "collection").get(0).toString());
    }
}