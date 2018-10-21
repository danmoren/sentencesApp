package com.danielmoreno.sentences.service;

import com.danielmoreno.sentences.model.WordPayload;
import org.springframework.http.ResponseEntity;

public interface WordsService {
    ResponseEntity getAllWords();
    ResponseEntity getWordByName(String word);
    ResponseEntity createWord(String word, WordPayload payload);
}
