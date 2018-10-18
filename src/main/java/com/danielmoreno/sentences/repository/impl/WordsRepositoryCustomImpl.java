package com.danielmoreno.sentences.repository.impl;

import com.danielmoreno.sentences.entity.Words;
import com.danielmoreno.sentences.repository.WordsRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SampleOperation;
import org.springframework.data.mongodb.core.query.Criteria;

public class WordsRepositoryCustomImpl implements WordsRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public WordsRepositoryCustomImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Words getSingleRandomWord(String wordCategory) {

        SampleOperation sampleOperation = getSampleOperation();

        //TODO - fix this, looks not that nice - could cause an NPE?
        return mongoTemplate.aggregate(Aggregation.newAggregation(
                Aggregation.match(Criteria.where("wordCategory").in(wordCategory)), sampleOperation),
                Words.class, Words.class).getMappedResults().get(0);

    }

    //TODO - Fix this, put the number in a variable
    private SampleOperation getSampleOperation() {
        return new SampleOperation(1);
    }

}
