package com.danielmoreno.sentences.repository;

import com.danielmoreno.sentences.entity.Words;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WordsRepository extends MongoRepository<Words, String>, WordsRepositoryCustom {
        Words findBy_id(ObjectId _id);
        List<Words> findByWord(String word);
}
