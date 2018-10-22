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

    /**
     * Uses the {@link SentencesRepository} to query all the sentences and builds a
     * {@link ResponseEntity} object with them.
     * @return {@link ResponseEntity} list of retrieved sentences from DB
     */
    @Override
    public ResponseEntity getAllSentences() {
        List<Sentences> sentencesList = sentencesRepository.findAll();
        return ResponseEntity.ok(AppUtils.buildSentencesResponse(sentencesList));
    }

    /**
     * Generates a sentence and then persists it in DB.
     * @return {@link ResponseEntity} with the result of the operation.
     */
    @Override
    public ResponseEntity generateSentence() {
        HashMap<String, Optional<Words>> words = retrieveWords();
        if (checkIfEmpty(words)) {
            return ResponseEntity.ok(new GenericResponse(AppConstants.MISSING_WORDS));
        } else {
            Sentences repeatedSentence = sentencesRepository.findByNounEqualsAndVerbEqualsAndAdjectiveEquals(
                    words.get(AppConstants.NOUN).get().getWord(),
                    words.get(AppConstants.VERB).get().getWord(),
                    words.get(AppConstants.ADJECTIVE).get().getWord());
            if (repeatedSentence != null) {
                repeatedSentence.setGenerationCount(repeatedSentence.getGenerationCount() + 1);
                sentencesRepository.save(repeatedSentence);
                return ResponseEntity.ok(new GenericResponse(AppConstants.EXISTING_SENTENCE));
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

    /**
     * Retrieves a map with one word of each word category.
     * @return {@link HashMap} with a random word of each word category.
     */
    private HashMap<String, Optional<Words>> retrieveWords() {
        HashMap<String, Optional<Words>> wordsMap = new HashMap<>();
        wordsMap.put(AppConstants.NOUN, wordsRepository.getSingleRandomWord(AppConstants.NOUN));
        wordsMap.put(AppConstants.VERB, wordsRepository.getSingleRandomWord(AppConstants.VERB));
        wordsMap.put(AppConstants.ADJECTIVE, wordsRepository.getSingleRandomWord(AppConstants.ADJECTIVE));
        return wordsMap;
    }

    /**
     * Checks if any of the words retrieved from DB is empty,
     * that means that no single word for that category has been inserted yet.
     * @param wordsMap {@link HashMap} with the retrieved words from DB.
     * @return {@link Boolean} <i>true</i> if one of the words is empty.
     */
    private boolean checkIfEmpty(HashMap<String, Optional<Words>> wordsMap) {
        for (String key : wordsMap.keySet()) {
            if (!wordsMap.get(key).isPresent()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Uses the {@link SentencesRepository} to query one sentence and builds a
     * {@link ResponseEntity} object with it.
     * @param sentenceID the ID of the sentence.
     * @return {@link ResponseEntity} with the result of the operation.
     */
    @Override
    public ResponseEntity getSentenceByID(String sentenceID) {
        if (isValidSentenceID(sentenceID)) {
            Sentences result = sentencesRepository.findBy_id(new ObjectId(sentenceID));
            if (result == null) {
                return ResponseEntity.ok(new GenericResponse(AppConstants.SENTENCE_NOT_FOUND));
            } else {
                result.setDisplayCount(result.getDisplayCount() + 1);
                sentencesRepository.save(result);
                return ResponseEntity.ok(AppUtils.buildSentenceResponse(result));
            }
        } else {
            return ResponseEntity.ok(new GenericResponse(AppConstants.INVALID_SENTENCE_ID));
        }
    }

    /**
     * Uses the {@link SentencesRepository} to query one sentence and builds a
     * {@link ResponseEntity} object with it in YodaTalk.
     * @param sentenceID the ID of the sentence.
     * @return {@link ResponseEntity} with the result of the operation.
     */
    @Override
    public ResponseEntity getYodaTalk(String sentenceID) {
        if (isValidSentenceID(sentenceID)) {
            Sentences result = sentencesRepository.findBy_id(new ObjectId(sentenceID));
            if (result == null) {
                return ResponseEntity.ok(
                        new GenericResponse(AppConstants.SENTENCE_NOT_FOUND));
            } else {
                return ResponseEntity.ok(AppUtils.buildYodaSentenceResponse(result));
            }
        } else {
            return ResponseEntity.ok(new GenericResponse(AppConstants.INVALID_SENTENCE_ID));
        }
    }

    /**
     * Uses the {@link SentencesRepository} to query one sentence and builds a
     * {@link ResponseEntity} object with the amount of time this sentence has been generated.
     * @param sentenceID the ID of the sentence.
     * @return {@link ResponseEntity} with the result of the operation.
     */
    @Override
    public ResponseEntity getRepetitionsByID(String sentenceID) {
        if (isValidSentenceID(sentenceID)) {
            Sentences result = sentencesRepository.findBy_id(new ObjectId(sentenceID));
            if (result == null) {
                return ResponseEntity.ok(
                        new GenericResponse(AppConstants.SENTENCE_NOT_FOUND));
            } else {
                return ResponseEntity.ok(
                        new GenericResponse(AppConstants.TIMES_GENERATED + result.getGenerationCount()));
            }
        } else {
            return ResponseEntity.ok(new GenericResponse(AppConstants.INVALID_SENTENCE_ID));
        }
    }

    /**
     * Check if an {@link ObjectId} object can be constructed out of the ID provided.
     * @param sentenceID the ID to be verified
     * @return {@link Boolean} <i>true</i> if the object can be built.
     */
    private boolean isValidSentenceID(String sentenceID) {
        ObjectId objectId;
        try {
            objectId = new ObjectId(sentenceID);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
