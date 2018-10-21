package com.danielmoreno.sentences.service;

import com.danielmoreno.sentences.model.WordPayload;
import org.springframework.http.ResponseEntity;

public interface WordsService {

    //Service layer to apply business logic
    ResponseEntity getAllWords();
    ResponseEntity getWordByName(String word);
    ResponseEntity createWord(String word, WordPayload payload);

}
