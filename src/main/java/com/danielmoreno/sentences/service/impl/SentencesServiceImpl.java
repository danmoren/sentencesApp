package com.danielmoreno.sentences.service.impl;

import com.danielmoreno.sentences.entity.Sentences;
import com.danielmoreno.sentences.entity.Words;
import com.danielmoreno.sentences.repository.SentencesRepository;
import com.danielmoreno.sentences.repository.WordsRepository;
import com.danielmoreno.sentences.response.GenericResponse;
import com.danielmoreno.sentences.service.SentencesService;
import com.danielmoreno.sentences.util.AppConstants;
import com.danielmoreno.sentences.util.AppUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class SentencesServiceImpl implements SentencesService {

    @Autowired
    SentencesRepository sentencesRepository;

    @Autowired
    WordsRepository wordsRepository;

    @Override
    public ResponseEntity getAllSentences() {
        List<Sentences> sentencesList = sentencesRepository.findAll();
        return ResponseEntity.ok(AppUtils.buildSentencesResponse(sentencesList));
    }

    @Override
    public ResponseEntity generateSentence() {
        HashMap<String, Optional<Words>> words = retrieveWords();
        if (checkIfEmpty(words)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse(AppConstants.MISSING_WORDS));
        } else {
            Sentences repeatedSentence = sentencesRepository.findByNounEqualsAndVerbEqualsAndAdjectiveEquals(
                    words.get(AppConstants.NOUN).get().getWord(),
                    words.get(AppConstants.VERB).get().getWord(),
                    words.get(AppConstants.ADJECTIVE).get().getWord());
            if (repeatedSentence != null) {
                repeatedSentence.setGenerationCount(repeatedSentence.getGenerationCount() + 1);
                sentencesRepository.save(repeatedSentence);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new GenericResponse(AppConstants.EXISTING_SENTENCE));
            } else {
                Sentences newSentence = AppUtils.buildSentence(
                        words.get(AppConstants.NOUN).get(),
                        words.get(AppConstants.VERB).get(),
                        words.get(AppConstants.ADJECTIVE).get());
                sentencesRepository.save(newSentence);
                return ResponseEntity.ok(new GenericResponse(
                        AppConstants.SENTENCE_GENERATED + newSentence.get_id().toString()));
            }
        }
    }

    private HashMap<String, Optional<Words>> retrieveWords() {
        HashMap<String, Optional<Words>> wordsMap = new HashMap<>();
        wordsMap.put(AppConstants.NOUN, wordsRepository.getSingleRandomWord(AppConstants.NOUN));
        wordsMap.put(AppConstants.VERB, wordsRepository.getSingleRandomWord(AppConstants.VERB));
        wordsMap.put(AppConstants.ADJECTIVE, wordsRepository.getSingleRandomWord(AppConstants.ADJECTIVE));
        return wordsMap;
    }

    private boolean checkIfEmpty(HashMap<String, Optional<Words>> wordsMap) {
        for (String key : wordsMap.keySet()) {
            if (!wordsMap.get(key).isPresent()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ResponseEntity getSentenceByID(String sentenceID) {
        if (isValidSentenceID(sentenceID)) {
            Sentences result = sentencesRepository.findBy_id(new ObjectId(sentenceID));
            if (result == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new GenericResponse(AppConstants.SENTENCE_NOT_FOUND));
            } else {
                result.setDisplayCount(result.getDisplayCount() + 1);
                sentencesRepository.save(result);
                return ResponseEntity.ok(AppUtils.buildSentenceResponse(result, false));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new GenericResponse(AppConstants.INVALID_SENTENCE_ID));
        }
    }

    @Override
    public ResponseEntity getYodaTalk(String sentenceID) {
        if (isValidSentenceID(sentenceID)) {
            Sentences result = sentencesRepository.findBy_id(new ObjectId(sentenceID));
            if (result == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new GenericResponse(AppConstants.SENTENCE_NOT_FOUND));
            } else {
                return ResponseEntity.ok(AppUtils.buildSentenceResponse(result, true));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new GenericResponse(AppConstants.INVALID_SENTENCE_ID));
        }
    }

    @Override
    public ResponseEntity getRepetitionsByID(String sentenceID) {
        if (isValidSentenceID(sentenceID)) {
            Sentences result = sentencesRepository.findBy_id(new ObjectId(sentenceID));
            if (result == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new GenericResponse(AppConstants.SENTENCE_NOT_FOUND));
            } else {
                return ResponseEntity.ok(
                        new GenericResponse(AppConstants.TIMES_GENERATED + result.getGenerationCount()));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new GenericResponse(AppConstants.INVALID_SENTENCE_ID));
        }
    }

    public boolean isValidSentenceID(String sentenceID) {
        ObjectId objectId;
        try {
            objectId = new ObjectId(sentenceID);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
