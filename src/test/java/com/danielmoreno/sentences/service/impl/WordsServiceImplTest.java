package com.danielmoreno.sentences.service.impl;

import com.danielmoreno.sentences.entity.Words;
import com.danielmoreno.sentences.model.WordPayload;
import com.danielmoreno.sentences.repository.WordsRepository;
import com.danielmoreno.sentences.response.GenericResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WordsServiceImplTest {

    @Mock
    WordsRepository wordsRepositoryMock;
    @InjectMocks
    WordsServiceImpl wordsService;

    @Before
    public void setUp() {
    }

    @Test
    public void testGetAllWords() {
        Gson gson = new Gson();
        Type wordsListType = new TypeToken<ArrayList<Words>>(){}.getType();
        List<Words> wordsList = gson.fromJson(getFile("words/wordsListFromDB.json"), wordsListType);
        when(wordsRepositoryMock.findAll()).thenReturn(wordsList);
        ResponseEntity result = wordsService.getAllWords();

        Type wordsPayloadListType = new TypeToken<ArrayList<WordPayload>>(){}.getType();
        List<WordPayload> wordsPayloadList = gson.fromJson(getFile("words/wordsList.json"), wordsPayloadListType);
        String jsonInString = gson.toJson(wordsPayloadList);

        Gson gsonBuilder = new GsonBuilder().create();
        String strJson = gsonBuilder.toJson(result.getBody());

        assertJsonEquals(strJson, jsonInString);
    }

    @Test
    public void testGetWordByName() {
        Gson gson = new Gson();
        Type wordsListType = new TypeToken<ArrayList<Words>>(){}.getType();
        List<Words> singleWord = gson.fromJson(getFile("words/singleWordFromDB.json"), wordsListType);
        when(wordsRepositoryMock.findByWord("test1")).thenReturn(singleWord);
        ResponseEntity result = wordsService.getWordByName("test1");

        WordPayload wordPayload = gson.fromJson(getFile("words/singleWord.json"), WordPayload.class);
        String jsonInString = gson.toJson(wordPayload);

        Gson gsonBuilder = new GsonBuilder().create();
        String strJson = gsonBuilder.toJson(result.getBody());

        assertJsonEquals(strJson, jsonInString);
    }

    @Test
    public void testGetWordByNameNotFound() {
        Gson gson = new Gson();
        List<Words> emptyList = new ArrayList<>();
        when(wordsRepositoryMock.findByWord("test1")).thenReturn(emptyList);
        ResponseEntity result = wordsService.getWordByName("test1");

        GenericResponse response = gson.fromJson(getFile("words/error_word_not_found.json"), GenericResponse.class);
        String jsonInString = gson.toJson(response);

        Gson gsonBuilder = new GsonBuilder().create();
        String strJson = gsonBuilder.toJson(result.getBody());

        assertJsonEquals(strJson, jsonInString);
    }

    @Test
    public void testCreateWord() {
        Gson gson = new Gson();
        List<Words> emptyList = new ArrayList<>();
        when(wordsRepositoryMock.findByWord("TEST1")).thenReturn(emptyList);
        WordPayload wordPayload = gson.fromJson(getFile("words/payload_adjective.json"), WordPayload.class);
        ResponseEntity result = wordsService.createWord("TEST1", wordPayload);

        GenericResponse response = gson.fromJson(getFile("words/successful_response_save.json"), GenericResponse.class);
        String jsonInString = gson.toJson(response);

        Gson gsonBuilder = new GsonBuilder().create();
        String strJson = gsonBuilder.toJson(result.getBody());

        assertJsonEquals(strJson, jsonInString);
    }


    @Test
    public void testCreateExistingWordSuccess() {
        Gson gson = new Gson();
        Type wordsListType = new TypeToken<ArrayList<Words>>(){}.getType();
        List<Words> wordsList = gson.fromJson(getFile("words/singleWordFromDB_existing.json"), wordsListType);
        when(wordsRepositoryMock.findByWord("TEST1")).thenReturn(wordsList);

        WordPayload wordPayload = gson.fromJson(getFile("words/payload_adjective.json"), WordPayload.class);
        ResponseEntity result = wordsService.createWord("TEST1", wordPayload);

        GenericResponse response = gson.fromJson(getFile("words/successful_response_save.json"), GenericResponse.class);
        String jsonInString = gson.toJson(response);

        Gson gsonBuilder = new GsonBuilder().create();
        String strJson = gsonBuilder.toJson(result.getBody());

        assertJsonEquals(strJson, jsonInString);
    }

    @Test
    public void testCreateExistingWordError() {
        Gson gson = new Gson();
        Type wordsListType = new TypeToken<ArrayList<Words>>(){}.getType();
        List<Words> wordsList = gson.fromJson(getFile("words/wordsListFromDB_same_word.json"), wordsListType);
        when(wordsRepositoryMock.findByWord("TEST1")).thenReturn(wordsList);

        WordPayload wordPayload = gson.fromJson(getFile("words/payload_adjective.json"), WordPayload.class);
        ResponseEntity result = wordsService.createWord("TEST1", wordPayload);

        GenericResponse response = gson.fromJson(getFile("words/error_existing_word.json"), GenericResponse.class);
        String jsonInString = gson.toJson(response);

        Gson gsonBuilder = new GsonBuilder().create();
        String strJson = gsonBuilder.toJson(result.getBody());

        assertJsonEquals(strJson, jsonInString);
    }

    @Test
    public void testCreateExistingInvalidCategory() {
        Gson gson = new Gson();
        Type wordsListType = new TypeToken<ArrayList<Words>>(){}.getType();
        List<Words> wordsList = gson.fromJson(getFile("words/singleWordFromDB_existing.json"), wordsListType);
        when(wordsRepositoryMock.findByWord("TEST1")).thenReturn(wordsList);

        WordPayload wordPayload = gson.fromJson(getFile("words/payload_wrong_category.json"), WordPayload.class);
        ResponseEntity result = wordsService.createWord("TEST1", wordPayload);

        GenericResponse response = gson.fromJson(getFile("words/error_wrong_category.json"), GenericResponse.class);
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