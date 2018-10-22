package com.danielmoreno.sentences.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * The Sentences entity as mapped in MongoDB collection
 */
public class Sentences {

    @Id
    private ObjectId _id;
    private String noun;
    private String verb;
    private String adjective;
    private Date creationDate;
    private int displayCount;
    private int generationCount;

    public Sentences() {}

    public Sentences(ObjectId _id, String noun, String verb, String adjective,
                     Date creationDate, int displayCount, int generationCount) {
        this._id = _id;
        this.noun = noun;
        this.verb = verb;
        this.adjective = adjective;
        this.creationDate = creationDate;
        this.displayCount = displayCount;
        this.generationCount = generationCount;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getNoun() {
        return noun;
    }

    public void setNoun(String noun) {
        this.noun = noun;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getAdjective() {
        return adjective;
    }

    public void setAdjective(String adjective) {
        this.adjective = adjective;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getDisplayCount() {
        return displayCount;
    }

    public void setDisplayCount(int displayCount) {
        this.displayCount = displayCount;
    }

    public int getGenerationCount() {
        return generationCount;
    }

    public void setGenerationCount(int generationCount) {
        this.generationCount = generationCount;
    }
}
