package com.danielmoreno.sentences.controller;

import com.danielmoreno.sentences.service.SentencesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sentences")
public class SentencesController {

    @Autowired
    private SentencesService sentencesService;


    /**
     * Retrieves the list of all sentences present in the DB collection
     * @return {@link ResponseEntity} the list of sentences.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity getAllSentences() {
        return sentencesService.getAllSentences();
    }

    /**
     * Generates a new sentence and persists it in the DB collection.
     * @return {@link ResponseEntity} a notification message with the ID of the
     * generated sentence.
     */
    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public ResponseEntity generateSentences() {
        return sentencesService.generateSentence();
    }

    /**
     * Retrieves a sentence with the specified ID.
     * @param sentenceID the ID of the sentence
     * @return {@link ResponseEntity} the sentence or a notification message
     * in case of error.
     */
    @RequestMapping(value = "/{sentenceID}", method = RequestMethod.GET)
    public ResponseEntity getSentenceByID(@PathVariable String sentenceID) {
        return sentencesService.getSentenceByID(sentenceID);
    }

    /**
     * Retrieves a sentence with the specified ID in <i>Yoda talk</i> format.
     * @param sentenceID the ID of the sentence
     * @return {@link ResponseEntity} the sentence in Yoda talk format or a notification
     * message in case of error.
     */
    @RequestMapping(value = "/{sentenceID}/yodaTalk", method = RequestMethod.GET)
    public ResponseEntity getYodaTalk(@PathVariable String sentenceID) {
        return sentencesService.getYodaTalk(sentenceID);
    }

    /**
     * Retrieves the times that a sentences has been randomly generated.
     * @param sentenceID the ID of the sentence
     * @return {@link ResponseEntity} A message with the amount of times the
     * sentence has been generated or an error message.
     */
    @RequestMapping(value = "/{sentenceID}/generation", method = RequestMethod.GET)
    public ResponseEntity getRepetitionsByID(@PathVariable String sentenceID) {
        return sentencesService.getRepetitionsByID(sentenceID);
    }
}
