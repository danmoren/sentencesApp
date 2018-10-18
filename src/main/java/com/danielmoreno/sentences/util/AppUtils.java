package com.danielmoreno.sentences.util;

import com.danielmoreno.sentences.entity.Sentences;
import com.danielmoreno.sentences.entity.Words;
import com.danielmoreno.sentences.enums.WordCategoryEnum;
import com.danielmoreno.sentences.model.Sentence;
import com.danielmoreno.sentences.model.SentencePayload;
import com.danielmoreno.sentences.model.Word;
import com.danielmoreno.sentences.model.WordPayload;
import org.apache.commons.lang3.EnumUtils;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.StringJoiner;

public class AppUtils {

    public static boolean isValidCategory(String category) {
        return EnumUtils.isValidEnum(WordCategoryEnum.class, category);
    }

    public static WordPayload buildWordResponse(Words info, String word) {
        Word responseWord = new Word();
        responseWord.setWord(word);
        responseWord.setWordCategory(info.getWordCategory());
        WordPayload responsePayload = new WordPayload();
        responsePayload.setWord(responseWord);
        return responsePayload;
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
        if (isYodaTalk) {
            responseSentence.setText(buildJoinedSentence(sentence.getNoun(),
                    sentence.getVerb(), sentence.getAdjective()));
        } else {
            responseSentence.setText(buildJoinedSentence(sentence.getNoun(),
                    sentence.getAdjective(), sentence.getVerb()));
        }
        responseSentence.setDisplayCount(sentence.getDisplayCount());
        SentencePayload payload = new SentencePayload();
        payload.setSentence(responseSentence);
        return payload;
    }

    //TODO - make it more dynamic (StringJoiner maybe?)
    private static String buildJoinedSentence(String word1, String word2, String word3) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(word1).add(word2).add(word3);
        return joiner.toString();
    }

}