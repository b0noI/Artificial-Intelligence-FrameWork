package com.aif.model.weight;

import com.aif.model.memory.short_time.Word;

public abstract class AbstractWeightItem {

    static final double DEFAULT_INFLUENT_LEVEL = 1.0;

    public abstract double calculateWeight(Word word);

    public double getInfluent(){
        return AbstractWeightItem.DEFAULT_INFLUENT_LEVEL;
    }

}
