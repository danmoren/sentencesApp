package com.danielmoreno.sentences.controller;

import com.danielmoreno.sentences.entity.Sentences;
import com.danielmoreno.sentences.repository.SentencesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sentences")
public class SentencesController {

    @Autowired
    private SentencesRepository sentencesRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Sentences> getAllSentences() {
        return sentencesRepository.findAll();
    }

    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public ResponseEntity generateSentences() {
        return ResponseEntity.ok("Sentence successfully generated");
    }
}
