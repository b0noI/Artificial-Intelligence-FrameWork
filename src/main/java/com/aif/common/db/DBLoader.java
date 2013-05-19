package com.aif.common.db;

import com.aif.model.memory.long_time.LongTimeMemoryWord;
import com.aif.model.memory.long_time.LongTimeMemoryWordsConnection;

import java.util.Set;


/**
 * Created with IntelliJ IDEA.
 * User: o.kozlovskiy
 * Date: 1/24/13
 * Time: 1:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBLoader{

    private final DBHelper DBHelper = com.aif.common.db.DBHelper.getInstance();
    private LongTimeMemoryWord LTMW;
    private String inputWord;

    private DBLoader(){

    }

    public static DBLoader getInstance(){
        return InstanceHolder.getInstance();
    }

    public void setWord(String word){
        inputWord = word;
    }

    public String findWordInWOrdforms(String word){
        return DBHelper.findWordInWordForms(word);
    }

    public Set<String> getWordSet(){
        return DBHelper.getWordsSet();
    }

    public String getOriginalWord(){

        LTMW = DBHelper.getWord(inputWord);
        return LTMW.originalWord;

    }

    public double getAbstractionLevel(){

        LTMW = DBHelper.getWord(inputWord);
        return LTMW.abstractionLevel;

    }

    public double getWorldWeight(){

        LTMW = DBHelper.getWord(inputWord);
        return LTMW.wordWeight;

    }

    public Set<String> getWordForms(){

        LTMW = DBHelper.getWord(inputWord);
        return LTMW.wordForms;

    }

    public double getDistanseToWord(String word){

        LongTimeMemoryWordsConnection LTMC = DBHelper.getConnection(LTMW.originalWord, word);

        if(LTMC != null){
            return LTMC.connectionDistance;
        }
        return 0.0;
    }

    public double getConnectionProbabilityWithWord(String word){

        LongTimeMemoryWordsConnection LTMC = DBHelper.getConnection(LTMW.originalWord, word);

        if(LTMC != null){
            return LTMC.connectionWeight;
        }
        return 0.0;

    }

    private static class InstanceHolder{

        private static final DBLoader INSTANCE = new DBLoader();

        static DBLoader getInstance(){
            return InstanceHolder.INSTANCE;
        }

    }

}
