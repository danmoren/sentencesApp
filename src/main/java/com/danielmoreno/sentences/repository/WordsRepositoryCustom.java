package com.danielmoreno.sentences.repository;

import com.danielmoreno.sentences.entity.Words;

import java.util.Optional;

/**
 * Custom interface created to add a query with aggregations to retrieve a single
 * random document of the words collection
 */
public interface WordsRepositoryCustom {

    Optional<Words> getSingleRandomWord(String wordCategory);

}
