package com.danielmoreno.sentences.repository.impl;

import com.danielmoreno.sentences.entity.Words;
import com.danielmoreno.sentences.repository.WordsRepositoryCustom;
import com.danielmoreno.sentences.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SampleOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link WordsRepositoryCustom} interface
 */
public class WordsRepositoryCustomImpl implements WordsRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public WordsRepositoryCustomImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Optional<Words> getSingleRandomWord(String wordCategory) {
        SampleOperation sampleOperation = getSampleOperation();
        return Optional.ofNullable(mongoTemplate.aggregate(Aggregation.newAggregation(
                Aggregation.match(Criteria.where(AppConstants.WORD_CATEGORY_QUERY_CRITERIA).in(wordCategory)),
                sampleOperation), Words.class, Words.class).getUniqueMappedResult());
    }

    /**
     * Builds a {@link SampleOperation} ready to be added to the aggregation, this operation is meant
     * to pick randomly a determined about of documents from a collection in mongo.
     * @return {@link SampleOperation} the defined operation.
     */
    private SampleOperation getSampleOperation() {
        return new SampleOperation(AppConstants.SAMPLE_SIZE);
    }

}
