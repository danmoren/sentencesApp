package com.danielmoreno.sentences.service.impl;

import com.danielmoreno.sentences.entity.Sentences;
import com.danielmoreno.sentences.entity.Words;
import com.danielmoreno.sentences.model.SentencePayload;
import com.danielmoreno.sentences.model.YodaSentencePayload;
import com.danielmoreno.sentences.repository.SentencesRepository;
import com.danielmoreno.sentences.repository.WordsRepository;
import com.danielmoreno.sentences.response.GenericResponse;
import com.danielmoreno.sentences.util.AppConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SentencesServiceImplTest {

    @Mock
    SentencesRepository sentencesRepositoryMock;
    @Mock
    WordsRepository wordsRepositoryMock;
    @InjectMocks
    SentencesServiceImpl sentencesService;


    @Test
    public void getAllSentences() {
        Gson gson = new Gson();
        Type sentencesListType = new TypeToken<ArrayList<Sentences>>(){}.getType();
        List<Sentences> sentencesList = gson.fromJson(getFile("sentences/sentences_list_db.json"), sentencesListType);
        when(sentencesRepositoryMock.findAll()).thenReturn(sentencesList);
        ResponseEntity result = sentencesService.getAllSentences();
        Type sentenceListType = new TypeToken<ArrayList<SentencePayload>>(){}.getType();
        List<SentencePayload> sentencePayloadList = gson.fromJson(getFile("sentences/sentences_list.json"), sentenceListType);
        String jsonInString = gson.toJson(sentencePayloadList);
        Gson gsonBuilder = new GsonBuilder().create();
        String strJson = gsonBuilder.toJson(result.getBody());
        assertJsonEquals(strJson, jsonInString);
    }

    @Test
    public void generateSentence() {
        Gson gson = new Gson();
        Sentences repeatedSentence = new Sentences();
        when(sentencesRepositoryMock.findByNounEqualsAndVerbEqualsAndAdjectiveEquals(
                "testnoun1","testverb1","testadjective1")).thenReturn(repeatedSentence);
        Type wordsListType = new TypeToken<ArrayList<Words>>(){}.getType();
        List<Words> singleWord = gson.fromJson(getFile("words/singleWordFromDB.json"), wordsListType);
        when(wordsRepositoryMock.findByWord("test1")).thenReturn(singleWord);
        Optional<Words> randomWord = Optional.of(singleWord.get(0));
        when(wordsRepositoryMock.getSingleRandomWord(anyString())).thenReturn(randomWord);
        ResponseEntity result = sentencesService.generateSentence();
        Gson gsonBuilder = new GsonBuilder().create();
        String strJson = gsonBuilder.toJson(result.getBody());
        assertTrue(strJson.contains(AppConstants.SENTENCE_GENERATED));
    }

    @Test
    public void generateSentenceNoWords() {
        Gson gson = new Gson();
        Optional<Words> emptyWord = Optional.empty();
        when(wordsRepositoryMock.getSingleRandomWord(any())).thenReturn(emptyWord);
        Type wordsListType = new TypeToken<ArrayList<Words>>(){}.getType();
        List<Words> singleWord = gson.fromJson(getFile("words/singleWordFromDB.json"), wordsListType);
        when(wordsRepositoryMock.findByWord(any())).thenReturn(singleWord);
        ResponseEntity result = sentencesService.generateSentence();
        GenericResponse response = new Gson().fromJson(getFile("sentences/info_missing_word.json"),
                GenericResponse.class);
        String jsonInString = gson.toJson(response);
        Gson gsonBuilder = new GsonBuilder().create();
        String strJson = gsonBuilder.toJson(result.getBody());
        assertJsonEquals(strJson, jsonInString);
    }

    @Test
    public void generateSentenceRepeated() {
        Gson gson = new Gson();
        Sentences repeatedSentence = gson.fromJson(getFile("sentences/single_sentence_db.json"),
                Sentences.class);
        when(sentencesRepositoryMock.findByNounEqualsAndVerbEqualsAndAdjectiveEquals(
                any(),any(),any())).thenReturn(repeatedSentence);
        Type wordsListType = new TypeToken<ArrayList<Words>>(){}.getType();
        List<Words> singleWord = gson.fromJson(getFile("words/singleWordFromDB.json"), wordsListType);
        when(wordsRepositoryMock.findByWord("test1")).thenReturn(singleWord);
        Optional<Words> randomWord = Optional.of(singleWord.get(0));
        when(wordsRepositoryMock.getSingleRandomWord(anyString())).thenReturn(randomWord);
        ResponseEntity result = sentencesService.generateSentence();
        GenericResponse response = new Gson().fromJson(getFile("sentences/info_existing_sentence.json"),
                GenericResponse.class);
        String jsonInString = gson.toJson(response);
        Gson gsonBuilder = new GsonBuilder().create();
        String strJson = gsonBuilder.toJson(result.getBody());
        assertJsonEquals(strJson, jsonInString);
    }


    @Test
    public void getSentenceByID() {
        Gson gson = new Gson();
        Sentences singleSentence = gson.fromJson(getFile("sentences/single_sentence_db.json"), Sentences.class);
        when(sentencesRepositoryMock.findBy_id(any())).thenReturn(singleSentence);
        ResponseEntity result = sentencesService.getSentenceByID("5bc8c94e9f89d14df075cf22");
        SentencePayload response = new Gson().fromJson(getFile("sentences/single_sentence.json"), SentencePayload.class);
        String jsonInString = gson.toJson(response);
        Gson gsonBuilder = new GsonBuilder().create();
        String strJson = gsonBuilder.toJson(result.getBody());
        assertJsonEquals(strJson, jsonInString);
    }

    @Test
    public void getSentenceByIDNoSentence() {
        Gson gson = new Gson();
        when(sentencesRepositoryMock.findBy_id(any())).thenReturn(null);
        ResponseEntity result = sentencesService.getSentenceByID("5bc8c94e9f89d14df075cf22");
        GenericResponse response = new Gson().fromJson(getFile("sentences/info_no_sentence.json"),
                GenericResponse.class);
        String jsonInString = gson.toJson(response);
        Gson gsonBuilder = new GsonBuilder().create();
        String strJson = gsonBuilder.toJson(result.getBody());
        assertJsonEquals(strJson, jsonInString);
    }

    @Test
    public void getSentenceByIDInvalidID() {
        Gson gson = new Gson();
        Sentences singleSentence = gson.fromJson(getFile("sentences/single_sentence_db.json"), Sentences.class);
        when(sentencesRepositoryMock.findBy_id(any())).thenReturn(singleSentence);
        ResponseEntity result = sentencesService.getSentenceByID("INVALID");
        GenericResponse response = new Gson().fromJson(getFile("sentences/info_invalid_id.json"),
                GenericResponse.class);
        String jsonInString = gson.toJson(response);
        Gson gsonBuilder = new GsonBuilder().create();
        String strJson = gsonBuilder.toJson(result.getBody());
        assertJsonEquals(strJson, jsonInString);
    }

    @Test
    public void getYodaTalk() {
        Gson gson = new Gson();
        Sentences singleSentence = gson.fromJson(getFile("sentences/single_sentence_db.json"), Sentences.class);
        when(sentencesRepositoryMock.findBy_id(any())).thenReturn(singleSentence);
        ResponseEntity result = sentencesService.getYodaTalk("5bc8c94e9f89d14df075cf22");
        YodaSentencePayload response = new Gson().fromJson(getFile("sentences/single_sentence_yoda.json"),
                YodaSentencePayload.class);
        String jsonInString = gson.toJson(response);
        Gson gsonBuilder = new GsonBuilder().create();
        String strJson = gsonBuilder.toJson(result.getBody());
        assertJsonEquals(strJson, jsonInString);
    }

    @Test
    public void getYodaTalkInvalidID() {
        Gson gson = new Gson();
        ResponseEntity result = sentencesService.getYodaTalk("INVALID");
        GenericResponse response = new Gson().fromJson(getFile("sentences/info_invalid_id.json"),
                GenericResponse.class);
        String jsonInString = gson.toJson(response);
        Gson gsonBuilder = new GsonBuilder().create();
        String strJson = gsonBuilder.toJson(result.getBody());
        assertJsonEquals(strJson, jsonInString);
    }

    @Test
    public void getYodaTalkNoSentence() {
        Gson gson = new Gson();
        when(sentencesRepositoryMock.findBy_id(any())).thenReturn(null);
        ResponseEntity result = sentencesService.getYodaTalk("5bc8c94e9f89d14df075cf22");
        GenericResponse response = new Gson().fromJson(getFile("sentences/info_no_sentence.json"),
                GenericResponse.class);
        String jsonInString = gson.toJson(response);
        Gson gsonBuilder = new GsonBuilder().create();
        String strJson = gsonBuilder.toJson(result.getBody());
        assertJsonEquals(strJson, jsonInString);
    }

    @Test
    public void getRepetitionsByID() {
        Gson gson = new Gson();
        Sentences singleSentence = gson.fromJson(getFile("sentences/single_sentence_db.json"), Sentences.class);
        when(sentencesRepositoryMock.findBy_id(any())).thenReturn(singleSentence);
        ResponseEntity result = sentencesService.getRepetitionsByID("5bc8c94e9f89d14df075cf22");
        GenericResponse response = new Gson().fromJson(getFile("sentences/info_sentence_generated.json"),
                GenericResponse.class);
        String jsonInString = gson.toJson(response);
        Gson gsonBuilder = new GsonBuilder().create();
        String strJson = gsonBuilder.toJson(result.getBody());
        assertJsonEquals(strJson, jsonInString);
    }

    @Test
    public void getRepetitionsByIDInvalidID() {
        Gson gson = new Gson();
        ResponseEntity result = sentencesService.getRepetitionsByID("INVALID");
        GenericResponse response = new Gson().fromJson(getFile("sentences/info_invalid_id.json"),
                GenericResponse.class);
        String jsonInString = gson.toJson(response);
        Gson gsonBuilder = new GsonBuilder().create();
        String strJson = gsonBuilder.toJson(result.getBody());
        assertJsonEquals(strJson, jsonInString);
    }

    @Test
    public void getRepetitionsByIDNoSentence() {
        Gson gson = new Gson();
        when(sentencesRepositoryMock.findBy_id(any())).thenReturn(null);
        ResponseEntity result = sentencesService.getRepetitionsByID("5bc8c94e9f89d14df075cf22");
        GenericResponse response = new Gson().fromJson(getFile("sentences/info_no_sentence.json"),
                GenericResponse.class);
        String jsonInString = gson.toJson(response);
        Gson gsonBuilder = new GsonBuilder().create();
        String strJson = gsonBuilder.toJson(result.getBody());
        assertJsonEquals(strJson, jsonInString);
    }

    private String getFile(String fileName){
        String result = "";
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            result = IOUtils.toString(classLoader.getResourceAsStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
