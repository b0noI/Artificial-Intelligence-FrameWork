package com.aif.model.memory.short_time;

import com.aif.model.exceptions.NoContentException;

import java.util.ArrayList;
import java.util.List;

public class Sentence {

    private static final String TAG = Sentence.class.getSimpleName();

    private List<Word> sentenceWords = null;

    private static final String SPLITTER = " ";

    public static Sentence generateSentence(String sentence, WordsStorage wordsStorage) throws NoContentException {
        String[] strWords = sentence.split(Sentence.SPLITTER);

        List<Word> words = new ArrayList<>();

        for (int i = 0; i<strWords.length;i++){
            String strWord = strWords[i];

            if (i == 0)
                strWord = strWord.toLowerCase();

            if (strWord.length() == 1)
                continue;

            try {
                Word word = wordsStorage.generateWordFromString(strWord);
                if (word != null){
                    words.add(word);
                    word.increaseCount();
                }
            } catch (NoContentException e){
                e.printStackTrace();
            }
        }

        if (words.size() == 0)
            throw new NoContentException(TAG+" generateSentence(String sentance) words.size() == 0");

        return new Sentence(words);
    }

    @Override
    public int hashCode(){
        long hashSum = 0;
        for (Word word : sentenceWords)
            hashSum += word.hashCode();

        return (int) (hashSum / sentenceWords.size());
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null)
            return false;

        if (obj instanceof Sentence){
            Sentence sentence = (Sentence) obj;
            if (sentence.sentenceWords.size() != sentenceWords.size())
                return false;

            for (int i=0;i< sentenceWords.size();i++)
                if (!sentenceWords.get(i).equals(sentence.sentenceWords.get(i)))
                    return false;

            return true;
        }

        return false;

    }

    @Override
    public String toString(){
        String outStr = "";
        for (Word word : sentenceWords){
             outStr += (word + " ");
        }

        return outStr;
    }

    public List<Word> getSentenceWords(){
        return sentenceWords;
    }

    private Sentence(List<Word> words) throws NoContentException{

        if (words == null || words.size() == 0)
            throw new NoContentException(TAG + " Sentence(List<Word> words): words == null || words.size() == 0");

       sentenceWords = words;
       for (int i = 0; i< sentenceWords.size()-1;i++){
           Word firstWord = sentenceWords.get(i);
           for (int j=i+1;j< sentenceWords.size();j++){
               Word secondWord = sentenceWords.get(j);
               Word.WordsConnection wordConnection = firstWord.addConnection(secondWord,j-i-1);
               secondWord.refreshConnection(firstWord, wordConnection);
           }
       }
    }

}
