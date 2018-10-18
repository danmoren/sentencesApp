package com.danielmoreno.sentences.repository;

import com.danielmoreno.sentences.entity.Words;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WordsRepository extends MongoRepository<Words, String>, WordsRepositoryCustom {

        Words findBy_id(ObjectId _id);
        Words findByWord(String word);
        //List<Words> findByWordCategory (String wordCategory);
        //List<Words> findRandomWord (String wordCategory);
        //Words findRandomWord (String wordCategory);
}
