package com.aif.model.weight;

import com.aif.model.memory.short_time.Word;
import com.aif.model.memory.short_time.WordsStorage;

/**
 * User: b0noI
 * Date: 24.11.12
 * Time: 21:22
 */
public class AbstractionWeight extends AbstractWeightItem{

    private static final    String              TAG             = AbstractionWeight.class.getSimpleName();

    private static final    double              INFLUENT_LEVEL  = 0.7;

        private                 WordsStorage    wordsStorage    = null;

    public AbstractionWeight(WordsStorage wordsStorage){
        if (wordsStorage == null)
            throw new NullPointerException(TAG + ": AbstractionWeight(WordsStorage wordsStorage): wordsStorage is null");
        this.wordsStorage = wordsStorage;
    }

    @Override
    public double getInfluent() {
        return INFLUENT_LEVEL;
    }

    @Override
    public double calculateWeight(Word word) {
        double abstractionLevel = (word.getAbstractoinLevel() + 0.1);
        abstractionLevel *= (double)word.getWordFormsStrings().size() / (double) wordsStorage.getMaximumWordFormsCount();
        return abstractionLevel;
    }
}
