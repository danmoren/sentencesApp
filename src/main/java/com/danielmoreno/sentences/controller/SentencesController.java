package com.danielmoreno.sentences.controller;

import com.danielmoreno.sentences.entity.Sentences;
import com.danielmoreno.sentences.entity.Words;
import com.danielmoreno.sentences.repository.SentencesRepository;
import com.danielmoreno.sentences.repository.WordsRepository;
import com.danielmoreno.sentences.service.SentencesService;
import com.danielmoreno.sentences.util.AppUtils;
import org.bson.types.ObjectId;
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

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity getAllSentences() {
        return sentencesService.getAllSentences();
    }

    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public ResponseEntity generateSentences() {
        return sentencesService.generateSentence();
    }

    @RequestMapping(value = "/{sentenceID}", method = RequestMethod.GET)
    public ResponseEntity getSentenceByID(@PathVariable String sentenceID) {
        return sentencesService.getSentenceByID(sentenceID);
    }

    @RequestMapping(value = "/{sentenceID}/yodaTalk", method = RequestMethod.GET)
    public ResponseEntity getYodaTalk(@PathVariable String sentenceID) {
        return sentencesService.getYodaTalk(sentenceID);
    }

    @RequestMapping(value = "/{sentenceID}/generation", method = RequestMethod.GET)
    public ResponseEntity getRepetitionsByID(@PathVariable String sentenceID) {
        return sentencesService.getRepetitionsByID(sentenceID);
    }
}
