package com.danielmoreno.sentences.repository;

import com.danielmoreno.sentences.entity.Words;

import java.util.Optional;

public interface WordsRepositoryCustom {

    Optional<Words> getSingleRandomWord(String wordCategory);

}
