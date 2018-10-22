package com.danielmoreno.sentences.controller;

import com.danielmoreno.sentences.entity.Words;
import com.danielmoreno.sentences.model.Word;
import com.danielmoreno.sentences.model.WordPayload;
import com.danielmoreno.sentences.response.GenericResponse;
import com.danielmoreno.sentences.service.WordsService;
import com.danielmoreno.sentences.util.AppConstants;
import com.danielmoreno.sentences.util.AppUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(WordsController.class)
public class WordsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private WordsService service;

    @Test
    public void givenWord_whenGetWordByName_OK() throws Exception {
        Words word = new Words(new ObjectId(), "test", "NOUN");
        List<Words> list = new ArrayList<>();
        list.add(word);
        given(service.getWordByName("test")).willReturn(ResponseEntity.ok(AppUtils.buildWordResponse(list, "test")));
        mvc.perform(get("/words/test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.word.word", is(word.getWord())));
    }

    @Test
    public void givenWord_whenGetWordByName_notFound() throws Exception {
        GenericResponse noWord = new GenericResponse(AppConstants.WORD_NOT_FOUND);
        given(service.getWordByName("test")).willReturn(ResponseEntity.ok(noWord));
        mvc.perform(get("/words/test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info", is(noWord.getInfo())));
    }

    @Test
    public void geAllWords() throws Exception {
        Words word = new Words(new ObjectId(), "test", "NOUN");
        List<Words> list = new ArrayList<>();
        list.add(word);
        word = new Words(new ObjectId(), "test2", "VERB");
        list.add(word);
        given(service.getAllWords()).willReturn(ResponseEntity.ok(AppUtils.buildWordsResponse(list)));
        mvc.perform(get("/words/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].word.word", is(list.get(0).getWord())))
                .andExpect(jsonPath("$[1].word.word", is(list.get(1).getWord())));
    }

    @Test
    public void givenWord_createWord_noPayload() throws Exception {
        GenericResponse wordCreated = new GenericResponse(AppConstants.WORD_SAVED);
        given(service.createWord(any(), any())).willReturn(ResponseEntity.ok(wordCreated));
        mvc.perform(put("/words/test"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenWord_createWord() throws Exception {

        Gson gson = new Gson();
        WordPayload wordPayload = gson.fromJson(getFile("words/payload_verb.json"), WordPayload.class);

        Gson gsonBuilder = new GsonBuilder().create();
        String strJson = gsonBuilder.toJson(wordPayload);

        GenericResponse wordCreated = new GenericResponse(AppConstants.WORD_SAVED);
        given(service.createWord("test", wordPayload)).willReturn(ResponseEntity.ok(wordCreated));
        mvc.perform(put("/words/test").contentType(MediaType.APPLICATION_JSON).content(gsonBuilder.toJson(wordPayload)))
                .andExpect(status().isOk());
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
