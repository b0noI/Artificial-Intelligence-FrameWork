package com.aif.model.memory.short_time;

import com.aif.model.exceptions.NoContentException;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class WordsStorage {

    private static final int COUNT_NOT_SET = -1;

    private final String TAG = WordsStorage.class.getSimpleName();

    private final Set<Word> words = new CopyOnWriteArraySet<>();

    private long maxConnectionCount = WordsStorage.COUNT_NOT_SET;

    private int maxWordFormCount = WordsStorage.COUNT_NOT_SET;

    private Word mostPopularWord = null;

    public Word getMostPopularWord(){
        if (mostPopularWord != null)
            return mostPopularWord;

        return refreshMostPopularWord();
    }

    public int getMaximumWordFormsCount(){

        if (maxWordFormCount != WordsStorage.COUNT_NOT_SET)
            return maxWordFormCount;

        int maxCount = 0;
        for (Word word : words){
            if (word.getWordFormsStrings().size() > maxCount)
                maxCount = word.getWordFormsStrings().size();
        }

        maxWordFormCount = maxCount;
        return maxCount;

    }

    /**
     * Creating Word object from String str
     * @param str
     *
     * EXAMPLE:
     * String str = "word";
     * Word newWord = wordsStorage.generateWordFromString(str);
     *
     * NOTE:
     * You can't call constructor of object Word, you need to call this method insted
     */
    public Word generateWordFromString(String str) throws NoContentException {

        if (str == null)
            throw new NoContentException(TAG + " Word generateWordFromString(String str): str == null || str.length() < Word.MAX_LENGTH_DIFFERENT_FOR_COMPARE");

        if (checkWordInStorage(str))
            return getWordFromString(str);
        Word newWord = new Word(str);
        addWord(newWord);

        return newWord;
    }

    public Word getWordFromString(String strWord) throws NoContentException{
        for (Word word : words){
             if (word.equals(strWord))
                 return word;
        }

        Word newWord = generateWordFromString(strWord);
        addWord(newWord);
        return newWord;
    }

    public long getWordsFormCount(){
        long count = 0;

        for (Word word : words)
            count += word.getCount();

        return count;
    }

    public void clearStorage(){
        words.clear();
    }

    public long getWordsCount(){
        return words.size();
    }

    public Set<Word> getAllWord(){
        return words;
    }

    public long getMaxConnectionCount(){

       if (maxConnectionCount != COUNT_NOT_SET)
           return maxConnectionCount;

       for (Word word : words){
           if (word.getConnectedWords().size() > maxConnectionCount)
               maxConnectionCount = word.getConnectedWords().size();
       }

       return maxConnectionCount;

    }

    Word refreshMostPopularWord(){
        long maxCount = 0;
        Word mostPopularWord = null;

        for (Word word : words){
            if (word.getCount() > maxCount){
                maxCount = word.getCount();
                mostPopularWord = word;
            }
        }

        this.mostPopularWord = mostPopularWord;

        return mostPopularWord;
    }

    boolean checkWordInStorage(String str){
        for (Word word : words){
            if (word.equals(str))
                return true;
        }

        return false;
    }

    void addWord(Word word){

        synchronized (words){
            words.add(word);
        }

    }

}
