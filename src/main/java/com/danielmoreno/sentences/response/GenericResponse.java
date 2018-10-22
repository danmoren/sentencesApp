package com.danielmoreno.sentences.response;

/**
 * Payload POJO for generic response in the app
 */
public class GenericResponse {

    private String info;

    public GenericResponse(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
