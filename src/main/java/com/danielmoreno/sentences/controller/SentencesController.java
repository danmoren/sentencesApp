package com.danielmoreno.sentences.controller;

import com.danielmoreno.sentences.entity.Sentences;
import com.danielmoreno.sentences.entity.Words;
import com.danielmoreno.sentences.repository.SentencesRepository;
import com.danielmoreno.sentences.repository.WordsRepository;
import com.danielmoreno.sentences.util.AppUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sentences")
public class SentencesController {

    @Autowired
    private SentencesRepository sentencesRepository;

    @Autowired
    private WordsRepository wordsRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Sentences> getAllSentences() {
        return sentencesRepository.findAll();
    }

    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public ResponseEntity generateSentences() {
        //TODO - This can be more dynamical
        //TODO - this method is too big!!
        Words noun = wordsRepository.getSingleRandomWord("NOUN");
        Words verb = wordsRepository.getSingleRandomWord("VERB");
        Words adjective = wordsRepository.getSingleRandomWord("ADJECTIVE");

        if (noun == null || verb == null || adjective == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing words to build the sentence");
        } else {
            //Check if the sentence already exists
            Sentences repeatedSentence = sentencesRepository.findByNounEqualsAndVerbEqualsAndAdjectiveEquals(
                    noun.getWord(), verb.getWord(), adjective.getWord());
            if (repeatedSentence != null) {
                //Increase the generationCounter
                repeatedSentence.setGenerationCount(repeatedSentence.getGenerationCount() + 1);
                sentencesRepository.save(repeatedSentence);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sentence already generated");
            } else {
                sentencesRepository.save(AppUtils.buildSentence(noun, verb, adjective));
                return ResponseEntity.ok("Sentence successfully generated");
            }
        }
    }

    @RequestMapping(value = "/{sentenceID}", method = RequestMethod.GET)
    public ResponseEntity getSentenceByID(@PathVariable String sentenceID) {
        //TODO - fix this! PROBABLY WILL NOT WORK
        Sentences result = sentencesRepository.findBy_id(new ObjectId(sentenceID));
        if (result == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sentence not found");
        } else {
            return ResponseEntity.ok(AppUtils.buildSentenceResponse(result, false));
        }
    }

    @RequestMapping(value = "/{sentenceID}/yodaTalk", method = RequestMethod.GET)
    public ResponseEntity getYodaTalk(@PathVariable String sentenceID) {
        //TODO - fix this! PROBABLY WILL NOT WORK
        Sentences result = sentencesRepository.findBy_id(new ObjectId(sentenceID));
        if (result == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sentence not found");
        } else {
            return ResponseEntity.ok(AppUtils.buildSentenceResponse(result, true));
        }
    }
}
