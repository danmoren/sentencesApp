package com.danielmoreno.sentences.service;

import org.springframework.http.ResponseEntity;

public interface SentencesService {

    ResponseEntity getAllSentences();
    ResponseEntity generateSentence();
    ResponseEntity getSentenceByID(String sentenceID);
    ResponseEntity getYodaTalk(String sentenceID);
    ResponseEntity getRepetitionsByID(String sentenceID);
}
