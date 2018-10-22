package com.danielmoreno.sentences.model;

/**
 * Payload POJO for sentences in Yoda format
 */
public class YodaSentencePayload {

    private YodaSentence sentence;

    public YodaSentence getSentence() {
        return sentence;
    }

    public void setSentence(YodaSentence sentence) {
        this.sentence = sentence;
    }
}
