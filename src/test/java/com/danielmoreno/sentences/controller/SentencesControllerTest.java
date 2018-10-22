package com.danielmoreno.sentences.controller;

import com.danielmoreno.sentences.entity.Sentences;
import com.danielmoreno.sentences.model.SentencePayload;
import com.danielmoreno.sentences.response.GenericResponse;
import com.danielmoreno.sentences.service.SentencesService;
import com.danielmoreno.sentences.util.AppConstants;
import com.danielmoreno.sentences.util.AppUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SentencesController.class)
public class SentencesControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SentencesService service;

    @Test
    public void getAllSentences() throws Exception {
        Gson gson = new Gson();
        Type sentencesListType = new TypeToken<ArrayList<Sentences>>(){}.getType();
        List<Sentences> sentencesList = gson.fromJson(getFile("sentences/sentences_list_db.json"), sentencesListType);
        given(service.getAllSentences()).willReturn(ResponseEntity.ok(AppUtils.buildSentencesResponse(sentencesList)));
        mvc.perform(get("/sentences/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sentence.text", is(
                        sentencesList.get(0).getNoun() + " " +
                                sentencesList.get(0).getVerb() + " " +
                                sentencesList.get(0).getAdjective()
                )))
                .andExpect(jsonPath("$[1].sentence.text", is(
                        sentencesList.get(1).getNoun() + " " +
                        sentencesList.get(1).getVerb() + " " +
                        sentencesList.get(1).getAdjective())));
    }

    @Test
    public void generateSentences() throws Exception {
        GenericResponse sentenceGenerated = new GenericResponse(AppConstants.SENTENCE_GENERATED);
        given(service.generateSentence()).willReturn(ResponseEntity.ok(sentenceGenerated));
        mvc.perform(post("/sentences/generate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info", is(sentenceGenerated.getInfo())));
    }

    @Test
    public void getSentenceByID() throws Exception {
        SentencePayload response = new Gson().fromJson(getFile("sentences/single_sentence.json"),
                SentencePayload.class);
        given(service.getSentenceByID(any())).willReturn(ResponseEntity.ok(response));
        mvc.perform(get("/sentences/5bc8c94e9f89d14df075cf22"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sentence.text", is(response.getSentence().getText())));
    }

    @Test
    public void getYodaTalk() throws Exception {
        SentencePayload response = new Gson().fromJson(getFile("sentences/single_sentence_yoda.json"),
                SentencePayload.class);
        given(service.getYodaTalk(any())).willReturn(ResponseEntity.ok(response));
        mvc.perform(get("/sentences/5bc8c94e9f89d14df075cf22/yodaTalk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sentence.text", is(response.getSentence().getText())));
    }

    @Test
    public void getRepetitionsByID() throws Exception {
        GenericResponse repetitionsInfo = new GenericResponse(AppConstants.SENTENCE_GENERATED);
        given(service.getRepetitionsByID(any())).willReturn(ResponseEntity.ok(repetitionsInfo));
        mvc.perform(get("/sentences/5bc8c94e9f89d14df075cf22/generation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info", is(repetitionsInfo.getInfo())));
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
