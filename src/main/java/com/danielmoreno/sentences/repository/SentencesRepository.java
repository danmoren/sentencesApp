package com.danielmoreno.sentences.repository;

import com.danielmoreno.sentences.entity.Sentences;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SentencesRepository extends MongoRepository<Sentences, String> {

    Sentences findBy_id(ObjectId _id);
    Sentences findByNounEqualsAndVerbEqualsAndAdjectiveEquals(String noun, String verb, String adjective);
}
