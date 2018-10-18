package com.danielmoreno.sentences.repository;

import com.danielmoreno.sentences.entity.Words;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WordsRepository extends MongoRepository<Words, String>, WordsRepositoryCustom {
        Words findBy_id(ObjectId _id);
        Words findByWord(String word);
}
