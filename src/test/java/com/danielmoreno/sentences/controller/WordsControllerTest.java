package com.danielmoreno.sentences.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@WebMvcTest(WordsController.class)
public class WordsControllerTest {

    //Pending
    public static final String CORRECT_JSON = "{\n"+
          "  \"key\":\"value\"\n"+
           "}";

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testGetWord() {

        //mockMvc.perform()

    }

}