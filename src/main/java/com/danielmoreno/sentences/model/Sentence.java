package com.danielmoreno.sentences.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Sentence extends SentenceBase {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int displayCount;

    public int getDisplayCount() {
        return displayCount;
    }

    public void setDisplayCount(int displayCount) {
        this.displayCount = displayCount;
    }
}
