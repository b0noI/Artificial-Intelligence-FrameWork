package com.aif.common.db;

import com.aif.model.memory.long_time.LongTimeMemoryConverter;
import com.aif.model.memory.long_time.LongTimeMemoryWord;
import com.aif.model.memory.long_time.LongTimeMemoryWordsConnection;
import com.aif.model.memory.short_time.Text;
import com.aif.model.memory.short_time.Word;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: o.kozlovskiy
 * Date: 1/29/13
 * Time: 11:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class DBSaver{

    //TODO Copy this class from TExtSaver

    private final DBHelper DBHleper = com.aif.common.db.DBHelper.getInstance();
    private final Text text;
    private final DBHelper dbHelper = DBHleper.getInstance();


    public DBSaver(Text text){

        if(text == null){
            throw new NullPointerException("TextSaver(Text text): text == null");
        }
        this.text = text;
    }

    private boolean saveWord(Word word){

        LongTimeMemoryWord longTimeMemoryWord =
                LongTimeMemoryConverter.convertShortTimeMemoryWord(text, word);
        if(!dbHelper.updateWord(longTimeMemoryWord)){
            return false;
        }

        Set<Word> connectionWords = word.getConnectedWords();

        for (Word connectedWord : connectionWords){
            LongTimeMemoryWordsConnection longTimeMemoryWordsConnection =
                    LongTimeMemoryConverter.convertShortTimeMemoryConnection(text, word, connectedWord);
            if(!dbHelper.updateWordConnection(longTimeMemoryWordsConnection)){
                return false;
            }
        }

        return true;
    }

    public void save(){
        //run();
        List<Word> words = text.getWords();
        for (Word word : words){
            saveWord(word);
        }
    }

/*
    @Override
    public void run() {
        //To change body of implemented methods use File | Settings | File Templates.
        List<Word> words = text.getWords();
        for (Word word : words){
            saveWord(word);
        }
    }
*/
}
