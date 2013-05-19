package com.aif.model.memory.short_time;

import java.util.HashSet;
import java.util.Set;

/**
 * User: b0noI
 * Date: 09.12.12
 * Time: 20:48
 */
public class WordsFormStorage {

    private final Set<WordForm> wordsForms = new HashSet<>();

    private WordsFormStorage(){}

    public static WordsFormStorage getInstance(){
        return InstanceHabdler.getInstance();
    }

    public WordForm getWordForm(String baseWord, String wordFormStr){
        WordForm wordForm = WordForm.generateWordForm(baseWord, wordFormStr);
        if (wordForm == null)
            return null;
        if (wordsForms.contains(wordForm)){
            for (WordForm form : wordsForms)
                if (form.equals(wordForm))
                    return form;
        }
        wordsForms.add(wordForm);
        return wordForm;
    }

    private static class InstanceHabdler{

        private final static WordsFormStorage INSTANCE = new WordsFormStorage();

        static WordsFormStorage getInstance(){
            return InstanceHabdler.INSTANCE;
        }

    }

}
