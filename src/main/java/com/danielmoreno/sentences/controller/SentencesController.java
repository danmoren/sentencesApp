package com.danielmoreno.sentences.controller;

import com.danielmoreno.sentences.entity.Sentences;
import com.danielmoreno.sentences.entity.Words;
import com.danielmoreno.sentences.repository.SentencesRepository;
import com.danielmoreno.sentences.repository.WordsRepository;
import com.danielmoreno.sentences.service.SentencesService;
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
import java.util.Optional;

@RestController
@RequestMapping("/sentences")
public class SentencesController {

    /*
    @Autowired
    private SentencesRepository sentencesRepository;

    @Autowired
    private WordsRepository wordsRepository;
     */
    @Autowired
    private SentencesService sentencesService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity getAllSentences() {
        //return sentencesRepository.findAll();
        return sentencesService.getAllSentences();
    }

    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public ResponseEntity generateSentences() {
        //TODO - This can be more dynamical
        //TODO - this method is too big!!
        /*
        Optional<Words> noun = wordsRepository.getSingleRandomWord("NOUN");
        Optional<Words> verb = wordsRepository.getSingleRandomWord("VERB");
        Optional<Words> adjective = wordsRepository.getSingleRandomWord("ADJECTIVE");

        if (!noun.isPresent() || !verb.isPresent() || !adjective.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing words to build the sentence");
        } else {
            //Check if the sentence already exists
            Sentences repeatedSentence = sentencesRepository.findByNounEqualsAndVerbEqualsAndAdjectiveEquals(
                    noun.get().getWord(), verb.get().getWord(), adjective.get().getWord());
            if (repeatedSentence != null) {
                //int genTimes = repeatedSentence.getGenerationCount() + 1;
                repeatedSentence.setGenerationCount(repeatedSentence.getGenerationCount() + 1);
                sentencesRepository.save(repeatedSentence);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Sentence already generated");
            } else {
                Sentences newSentence = AppUtils.buildSentence(noun.get(), verb.get(), adjective.get());
                sentencesRepository.save(newSentence);
                return ResponseEntity.ok("Sentence successfully generated with id: " + newSentence.get_id().toString());
            }
        }
        */
        return sentencesService.generateSentence();
    }

    @RequestMapping(value = "/{sentenceID}", method = RequestMethod.GET)
    public ResponseEntity getSentenceByID(@PathVariable String sentenceID) {
        /*
        Sentences result = sentencesRepository.findBy_id(new ObjectId(sentenceID));
        if (result == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sentence not found");
        } else {
            result.setDisplayCount(result.getDisplayCount() + 1);
            sentencesRepository.save(result);
            return ResponseEntity.ok(AppUtils.buildSentenceResponse(result, false));
        }
        */
        return sentencesService.getSentenceByID(sentenceID);
    }

    @RequestMapping(value = "/{sentenceID}/yodaTalk", method = RequestMethod.GET)
    public ResponseEntity getYodaTalk(@PathVariable String sentenceID) {
        /*
        Sentences result = sentencesRepository.findBy_id(new ObjectId(sentenceID));
        if (result == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sentence not found");
        } else {
            return ResponseEntity.ok(AppUtils.buildSentenceResponse(result, true));
        }
        */
        return sentencesService.getYodaTalk(sentenceID);
    }

    @RequestMapping(value = "/{sentenceID}/generation", method = RequestMethod.GET)
    public ResponseEntity getRepetitionsByID(@PathVariable String sentenceID) {
        /*
        Sentences result = sentencesRepository.findBy_id(new ObjectId(sentenceID));
        if (result == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sentence not found");
        } else {
            return ResponseEntity.ok("This sentence has been generated " + result.getGenerationCount() + " times");
        }
        */
        return sentencesService.getRepetitionsByID(sentenceID);
    }
}
