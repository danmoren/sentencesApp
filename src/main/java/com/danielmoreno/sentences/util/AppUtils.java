package com.danielmoreno.sentences.util;

import com.danielmoreno.sentences.entity.Words;
import com.danielmoreno.sentences.enums.WordCategoryEnum;
import com.danielmoreno.sentences.model.Word;
import com.danielmoreno.sentences.model.WordPayload;
import org.apache.commons.lang3.EnumUtils;

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

}
