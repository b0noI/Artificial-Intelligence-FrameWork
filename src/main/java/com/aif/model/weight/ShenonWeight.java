package com.aif.model.weight;

import com.aif.model.memory.short_time.Word;
import com.aif.model.memory.short_time.WordsStorage;

public class ShenonWeight extends AbstractWeightItem {

    private static final    String              TAG             = ShenonWeight.class.getSimpleName();

    private static final    double              INFLUENT_LEVEL  = 0.2;

    private                 WordsStorage        wordsStorage    = null;

    public ShenonWeight(WordsStorage wordsStorage){
        if (wordsStorage == null)
            throw new NullPointerException(TAG + ": ShenonWeight(WordsStorage wordsStorage): wordsStorage is null");
        this.wordsStorage = wordsStorage;
    }

    @Override
    public double calculateWeight(Word word) {
        long mostPopularWordCount = wordsStorage.getMostPopularWord().getCount();
        long wordCount = word.getCount();
        double wordCountToMostPopularCount = (double)wordCount/(double)mostPopularWordCount;

        double wordWeight = -(wordCountToMostPopularCount *
                (Math.log(wordCountToMostPopularCount)/Math.log(2)));
        return wordWeight;
    }

    @Override
    public double getInfluent() {
        return INFLUENT_LEVEL;
    }

}
