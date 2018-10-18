package com.danielmoreno.sentences.controller;

import com.danielmoreno.sentences.entity.Words;
import com.danielmoreno.sentences.model.WordPayload;
import com.danielmoreno.sentences.repository.WordsRepository;
import com.danielmoreno.sentences.util.AppUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/words")
public class WordsController {

    @Autowired
    private WordsRepository wordsRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Words> getAllWords() {
        return wordsRepository.findAll();
    }

    //Insert a word
    @RequestMapping(value = "/{word}", method = RequestMethod.PUT)
    public ResponseEntity createWord(@PathVariable String word, @Valid @RequestBody WordPayload payload) {
        Words result = wordsRepository.findByWord(word);
        if (result == null) {
            if (AppUtils.isValidCategory(payload.getWord().getWordCategory())) {
                Words w = new Words(new ObjectId(), word, payload.getWord().getWordCategory());
                wordsRepository.save(w);
                return ResponseEntity.ok("Word " + word + " successfully saved");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid word category");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Word already exists");
        }
    }

    //Retrieve a single word (if exists)
    @RequestMapping(value = "/{word}", method = RequestMethod.GET)
    public ResponseEntity getWordsByName(@PathVariable("word") String word) {
        Words result = wordsRepository.findByWord(word);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Word not found");
        } else {
            return ResponseEntity.ok(AppUtils.buildWordResponse(result, word));
        }
    }

    //Non required, yet implemented
    @RequestMapping(value = "/{word}", method = RequestMethod.DELETE)
    public void deletePet(@PathVariable String word) {
        wordsRepository.delete(wordsRepository.findByWord(word));
    }
}
