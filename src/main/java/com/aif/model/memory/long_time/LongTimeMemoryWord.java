package com.aif.model.memory.long_time;

import com.aif.model.memory.short_time.Word;

import java.util.Set;

public class LongTimeMemoryWord {

    public String originalWord;

    public double wordWeight;

    public double abstractionLevel;

    public Set<String> wordForms;

    public LongTimeMemoryWord(){
    }

    public LongTimeMemoryWord(String originalWord, Set<String> wordForms){
        this.originalWord = originalWord;
        this.wordForms = wordForms;

    }

    public void merge(LongTimeMemoryWord newWord){
        //originalWord = newWord.originalWord;
        wordWeight = (wordWeight + newWord.wordWeight) / 2;
        for (String wordForm : newWord.wordForms){
            if (!wordForms.contains(wordForm))
                wordForms.add(wordForm);
        }
        abstractionLevel = (abstractionLevel + newWord.abstractionLevel) / 2;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Word){
            Word word = (Word) obj;
            if (word.equals(originalWord))
                return true;
        } else if (obj instanceof LongTimeMemoryWord){
            LongTimeMemoryWord word = (LongTimeMemoryWord) obj;
            if (word.originalWord.equals(originalWord))
                return true;
        } else if (obj instanceof String){
            String word = (String) obj;
            if (word.equals(originalWord))
                return true;
        }

        return false;
    }
}
