package com.danielmoreno.sentences.controller;

import com.danielmoreno.sentences.model.WordPayload;
import com.danielmoreno.sentences.repository.WordsRepository;
import com.danielmoreno.sentences.service.WordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/words")
public class WordsController {

    @Autowired
    private WordsService wordsService;

    /**
     * Retrieves the list of all words present in the DB collection
     * @return {@link ResponseEntity} the list of words.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity getAllWords() {
        return wordsService.getAllWords();
    }

    /**
     * Create a word and persists it in the DB collection.
     * @param word the word to be stored
     * @param payload a JSON object containing the category associated to the word
     * @return {@link ResponseEntity} an information message in case or success or failure.
     */
    @RequestMapping(value = "/{word}", method = RequestMethod.PUT)
    public ResponseEntity createWord(@PathVariable String word, @Valid @RequestBody WordPayload payload) {
        return wordsService.createWord(word.toLowerCase(), payload);
    }

    /**
     * Retrieve the information related to a specific word
     * @param word the word to be queried
     * @return {@link ResponseEntity} the word or a notification message
     * in case of error.
     */
    @RequestMapping(value = "/{word}", method = RequestMethod.GET)
    public ResponseEntity getWordsByName(@PathVariable("word") String word) {
        return wordsService.getWordByName(word.toLowerCase());
    }
}
