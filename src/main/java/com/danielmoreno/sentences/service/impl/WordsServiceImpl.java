package com.danielmoreno.sentences.service.impl;

import com.danielmoreno.sentences.entity.Words;
import com.danielmoreno.sentences.model.WordPayload;
import com.danielmoreno.sentences.repository.WordsRepository;
import com.danielmoreno.sentences.response.GenericResponse;
import com.danielmoreno.sentences.service.WordsService;
import com.danielmoreno.sentences.util.AppConstants;
import com.danielmoreno.sentences.util.AppUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WordsServiceImpl implements WordsService {

    @Autowired
    private WordsRepository wordsRepository;

    @Override
    public ResponseEntity getAllWords() {
        List<Words> wordsList = wordsRepository.findAll();
        return ResponseEntity.ok(AppUtils.buildWordsResponse(wordsList));
    }

    @Override
    public ResponseEntity getWordByName(String word) {
        List<Words> result = wordsRepository.findByWord(word);
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new GenericResponse(AppConstants.WORD_NOT_FOUND)
            );
        } else {
            return ResponseEntity.ok(AppUtils.buildWordResponse(result, word));
        }
    }

    @Override
    public ResponseEntity createWord(String word, WordPayload payload) {
        List<Words> wordsList = wordsRepository.findByWord(word);
        if (!wordsList.isEmpty()) {
            if (AppUtils.checkExistingCategory(wordsList, payload.getWord().getWordCategory())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new GenericResponse(AppConstants.EXISTING_WORD)
                );
            } else {
                return saveWord(word, payload.getWord().getWordCategory());
            }
        } else {
            return saveWord(word, payload.getWord().getWordCategory());
        }
    }

    private ResponseEntity saveWord(String word, String wordCategory) {
        if (AppUtils.isValidCategory(wordCategory)) {
            Words w = new Words(new ObjectId(), word, wordCategory);
            wordsRepository.save(w);
            return ResponseEntity.ok(new GenericResponse(AppConstants.WORD_SAVED + word));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new GenericResponse(AppConstants.INVALID_CATEGORY)
            );
        }
    }
}
