package com.aif.model.memory.long_time;

import com.aif.model.memory.short_time.Text;
import com.aif.model.memory.short_time.Word;

public class LongTimeMemoryConverter {

    public static LongTimeMemoryWord convertShortTimeMemoryWord(Text text, Word word){
        LongTimeMemoryWord longTimeMemoryConverter = new LongTimeMemoryWord();
        longTimeMemoryConverter.originalWord = word.getOriginalWord();
        longTimeMemoryConverter.abstractionLevel = word.getAbstractoinLevel();
        longTimeMemoryConverter.wordForms = word.getWordFormsStrings();
        longTimeMemoryConverter.wordWeight = text.getWordWeight(word);
        return longTimeMemoryConverter;
    }

    public static LongTimeMemoryWordsConnection convertShortTimeMemoryConnection(Text text, Word word1, Word word2){
        LongTimeMemoryWordsConnection longTimeMemoryWordsConnection =
                new LongTimeMemoryWordsConnection();
        longTimeMemoryWordsConnection.wordFirst =
                LongTimeMemoryConverter.convertShortTimeMemoryWord(text, word1);
        longTimeMemoryWordsConnection.wordSecond =
                LongTimeMemoryConverter.convertShortTimeMemoryWord(text, word2);
        longTimeMemoryWordsConnection.connectionWeight =
                text.getConnectionLevelBetweenWords(word1, word2);
        longTimeMemoryWordsConnection.connectionDistance =
                text.getDistanceBetweenWords(word1, word2);
        return longTimeMemoryWordsConnection;
    }

}
