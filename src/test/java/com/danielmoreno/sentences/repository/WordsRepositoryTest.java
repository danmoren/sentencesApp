package com.danielmoreno.sentences.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import javafx.application.Application;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

/*
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WordsRepository.class})
@EnableMongoRepositories
@Import(EmbeddedMongoAutoConfiguration.class)
*/

//@ContextConfiguration(classes = Application.class)
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

        //mongoTemplate.findAll(DBObject.class, "collection").get(0).toString();
    }

    /*
    public void testAggregate() {

    }
    */


}