package com.aif.common;

import com.aif.common.db.DBHelper;
import com.aif.model.memory.long_time.LongTimeMemoryConverter;
import com.aif.model.memory.long_time.LongTimeMemoryWord;
import com.aif.model.memory.long_time.LongTimeMemoryWordsConnection;
import com.aif.model.memory.short_time.Text;
import com.aif.model.memory.short_time.Word;

import java.util.List;
import java.util.Set;

/**
 * User: b0noI
 * Date: 08.12.12
 * Time: 22:02
 */
public class TextSaver implements Runnable{

    private final static String TAG = TextSaver.class.getSimpleName();

    private final DBHelper dbalHelper = DBHelper.getInstance();

    private final Text text;

    private IOnSaveFinished handler;

    public TextSaver(Text text){

        if (text == null)
            throw new NullPointerException(TAG + ": TextSaver(Text text): text == null");

        this.text = text;

    }

    public TextSaver(Text text, IOnSaveFinished handler){

        this(text);
        this.handler = handler;

    }

    public void saveTextDataToDB(){
        run();
    }

    @Override
    public void run() {
        List<Word> words = text.getWords();
        for (Word word : words){
            saveWord(word);
        }
        if (handler != null)
            handler.onSaveFinished();
    }

    private void saveWord(Word word){
        LongTimeMemoryWord longTimeMemoryWord =
                LongTimeMemoryConverter.convertShortTimeMemoryWord(text, word);
        dbalHelper.updateWord(longTimeMemoryWord);
        Set<Word> connectionWords = word.getConnectedWords();
        for (Word connectedWord : connectionWords){
            LongTimeMemoryWordsConnection longTimeMemoryWordsConnection =
                    LongTimeMemoryConverter.convertShortTimeMemoryConnection(text, word, connectedWord);
            dbalHelper.updateWordConnection(longTimeMemoryWordsConnection);
        }
    }

    public interface IOnSaveFinished{

        public void onSaveFinished();

    }

}
