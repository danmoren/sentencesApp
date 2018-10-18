package com.danielmoreno.sentences.model;

import javax.validation.constraints.NotNull;

public class Word {

    private String word;

    @NotNull(message = "You must enter a word category")
    private String wordCategory;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWordCategory() {
        return wordCategory;
    }

    public void setWordCategory(String wordCategory) {
        this.wordCategory = wordCategory;
    }

}
