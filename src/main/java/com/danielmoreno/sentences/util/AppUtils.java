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

    public static boolean isValidCategory(String category) {
        return EnumUtils.isValidEnum(WordCategoryEnum.class, category);
    }

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

    public static SentencePayload buildSentenceResponse(Sentences sentence, boolean isYodaTalk) {
        Sentence responseSentence = new Sentence();
        SentenceBase responseSentenceBase = new SentenceBase();
        SentencePayload payload = new SentencePayload();
        if (isYodaTalk) {
            //YodaTalk order: ADJECTIVE - NOUN - VERB
            responseSentenceBase.setText(buildJoinedSentence(sentence.getAdjective(), sentence.getNoun(),
                    sentence.getVerb(), true));
            payload.setSentence(responseSentenceBase);
        } else {
            //Normal order: NOUN - VERB - ADJECTIVE
            responseSentence.setText(buildJoinedSentence(sentence.getNoun(),
                    sentence.getVerb(), sentence.getAdjective(), false));
            responseSentence.setDisplayCount(sentence.getDisplayCount());
            payload.setSentence(responseSentence);
        }
        return payload;
    }

    public static List<SentencePayload> buildSentencesResponse(List<Sentences> sentencesList) {
        List<SentencePayload> responseList = new ArrayList<>();
        for (Sentences sentence : sentencesList) {
            responseList.add(buildSentenceResponse(sentence, false));
        }
        return responseList;
    }

    private static String buildJoinedSentence(String word1, String word2, String word3, boolean isYodaTalk) {
        StringJoiner joiner = new StringJoiner(AppConstants.DELIM_EMPTY_SPACE);
        if (isYodaTalk) {
            word1 = word1 + AppConstants.DELIM_COMMA;
        }
        joiner.add(word1).add(word2).add(word3);
        return joiner.toString();
    }

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
