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

    //@Autowired
    //private WordsRepository wordsRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity getAllWords() {
        return wordsService.getAllWords();
    }

    //Insert a word
    @RequestMapping(value = "/{word}", method = RequestMethod.PUT)
    public ResponseEntity createWord(@PathVariable String word, @Valid @RequestBody WordPayload payload) {
        return wordsService.createWord(word.toLowerCase(), payload);
    }

    //Retrieve a single word (if exists)
    //Could retrieve multiple words since same word could fit multiple categories
    @RequestMapping(value = "/{word}", method = RequestMethod.GET)
    public ResponseEntity getWordsByName(@PathVariable("word") String word) {
        return wordsService.getWordByName(word.toLowerCase());
    }

    /*
    //Non required, yet implemented
    @RequestMapping(value = "/{word}", method = RequestMethod.DELETE)
    public void deleteWord(@PathVariable String word) {
        for (Words words : wordsRepository.findByWord(word)) {
            wordsRepository.delete(words);
        }
    }
    */
}
