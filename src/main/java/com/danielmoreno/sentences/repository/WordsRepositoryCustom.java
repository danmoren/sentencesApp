package com.danielmoreno.sentences.repository;

import com.danielmoreno.sentences.entity.Words;

public interface WordsRepositoryCustom {

    //Receives the desired type of word
    Words getSingleRandomWord(String wordCategory);

}
