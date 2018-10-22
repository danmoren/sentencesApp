package com.danielmoreno.sentences.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

/**
 * The Words entity as mapped in MongoDB collection
 */
public class Words {

    @Id
    private ObjectId _id;
    private String word;
    private String wordCategory;

    public Words() {}

    public Words(ObjectId _id, String word, String wordCategory) {
        this._id = _id;
        this.word = word;
        this.wordCategory = wordCategory;
    }

    public String get_id() {
        return _id.toHexString();
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

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
