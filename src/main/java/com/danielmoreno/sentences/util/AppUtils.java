package com.danielmoreno.sentences.util;

import com.danielmoreno.sentences.entity.Sentences;
import com.danielmoreno.sentences.entity.Words;
import com.danielmoreno.sentences.enums.WordCategoryEnum;
import com.danielmoreno.sentences.model.*;
import org.apache.commons.lang3.EnumUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

public class AppUtils {

    /**
     * Validates if a category is supported by the app, see {@link WordCategoryEnum}
     * @param category
     * @return {@link Boolean} <i>true</i> if the category is supported, false <i>otherwise</i>
     */
    public static boolean isValidCategory(String category) {
        return EnumUtils.isValidEnum(WordCategoryEnum.class, category);
    }

    /**
     * Builds the payload object of a single word
     * @param info information about the word
     * @param word the word queried
     * @return {@link WordPayload} with the information about the word.
     */
    public static WordPayload buildWordResponse(List<Words> info, String word) {
        Word responseWord = new Word();
        responseWord.setWord(word);
        if (info.size() > 1) {
            StringJoiner joiner = new StringJoiner(AppConstants.DELIM_COMMA);
            for (Words words : info) {
                joiner.add(words.getWordCategory());
            }
            responseWord.setWordCategory(joiner.toString());
        } else {
            responseWord.setWordCategory(info.get(0).getWordCategory());
        }
        WordPayload responsePayload = new WordPayload();
        responsePayload.setWord(responseWord);
        return responsePayload;
    }

    /**
     * Builds the payload list of multiple words
     * @param wordsList list with the words queried.
     * @return {@link List} list of {@link WordPayload} with the information
     * about each word.
     */
    public static List<WordPayload> buildWordsResponse(List<Words> wordsList) {
        List<WordPayload> responseList = new ArrayList<>();
        for (Words word : wordsList) {
            Word responseWord = new Word();
            responseWord.setWord(word.getWord());
            responseWord.setWordCategory(word.getWordCategory());
            WordPayload responsePayload = new WordPayload();
            responsePayload.setWord(responseWord);
            responseList.add(responsePayload);
        }
        return responseList;
    }

    /**
     * Builds a new sentence
     * @param noun the noun to be used
     * @param verb the verb to be used
     * @param adjective the adjective to be used
     * @return a new {@link Sentences} object.
     */
    public static Sentences buildSentence(Words noun, Words verb, Words adjective) {
        Sentences newSentence = new Sentences();
        newSentence.set_id(new ObjectId());
        newSentence.setCreationDate(new Date());
        newSentence.setDisplayCount(0);
        newSentence.setGenerationCount(1);
        newSentence.setNoun(noun.getWord());
        newSentence.setVerb(verb.getWord());
        newSentence.setAdjective(adjective.getWord());
        return newSentence;
    }

    /**
     * Builds the payload object of a single sentence.
     * @param sentence information about the sentence
     * @return {@link SentencePayload} with the information about the sentence.
     */
    public static SentencePayload buildSentenceResponse(Sentences sentence) {
        Sentence responseSentence = new Sentence();
        SentencePayload payload = new SentencePayload();
        //Normal order: NOUN - VERB - ADJECTIVE
        responseSentence.setText(buildJoinedSentence(sentence.getNoun(),
                sentence.getVerb(), sentence.getAdjective(), false));
        responseSentence.setDisplayCount(sentence.getDisplayCount());
        payload.setSentence(responseSentence);
        return payload;
    }

    /**
     * Builds the payload object of a single sentence in YodaTalk format.
     * @param sentence information about the sentence
     * @return {@link SentencePayload} with the information about the sentence.
     */
    public static YodaSentencePayload buildYodaSentenceResponse(Sentences sentence) {
        YodaSentence responseSentenceBase = new YodaSentence();
        YodaSentencePayload payload = new YodaSentencePayload();
        //YodaTalk order: ADJECTIVE - NOUN - VERB
        responseSentenceBase.setText(buildJoinedSentence(sentence.getAdjective(), sentence.getNoun(),
                sentence.getVerb(), true));
        payload.setSentence(responseSentenceBase);
        return payload;
    }

    /**
     * Builds the payload list of multiple sentences.
     * @param sentencesList the list of queried sentences
     * @return {@link List} List of sentences.
     */
    public static List<SentencePayload> buildSentencesResponse(List<Sentences> sentencesList) {
        List<SentencePayload> responseList = new ArrayList<>();
        for (Sentences sentence : sentencesList) {
            responseList.add(buildSentenceResponse(sentence));
        }
        return responseList;
    }

    /**
     * Builds a sentences with the three categories available.
     * @param word1 word1
     * @param word2 word2
     * @param word3 word3
     * @param isYodaTalk defines if the sentence must be in YodaTalk format or not.
     * @return {@link String} the build string with the three words.
     */
    private static String buildJoinedSentence(String word1, String word2, String word3, boolean isYodaTalk) {
        StringJoiner joiner = new StringJoiner(AppConstants.DELIM_EMPTY_SPACE);
        if (isYodaTalk) {
            word1 = word1 + AppConstants.DELIM_COMMA;
        }
        joiner.add(word1).add(word2).add(word3);
        return joiner.toString();
    }

    /**
     * Checks if a word is already created with an specific category
     * @param wordsList Word or words already stored.
     * @param category category to be validated
     * @return {@link Boolean} <i>true</i> if the word was already created.
     */
    public static boolean checkExistingCategory(List<Words> wordsList, String category) {
        boolean result = false;
        for (Words word : wordsList) {
            if (word.getWordCategory().equals(category)) {
                result = true;
                break;
            }
        }
        return result;
    }
}
