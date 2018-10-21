package com.danielmoreno.sentences.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class WordPayload {

    @NotNull(message = "Wrong payload structure")
    @Valid
    private Word word;

    public WordPayload() {}

    public WordPayload(Word word) {
        this.word = word;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

}
