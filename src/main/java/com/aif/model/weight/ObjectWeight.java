package com.aif.model.weight;

import com.aif.model.memory.short_time.Word;

import java.util.HashMap;
import java.util.Map;

/**
 * User: b0noI
 * Date: 08.01.13
 * Time: 17:58
 */
public class ObjectWeight extends AbstractWeightItem {

    private static final Map<Word.Type, Double> TYPE_WEIGHT = new HashMap<>();

    static {
        TYPE_WEIGHT.put(Word.Type.OBJECT, 1.0);
        TYPE_WEIGHT.put(Word.Type.SIMPL, 0.9);
    }

    @Override
    public double calculateWeight(Word word) {

        if (TYPE_WEIGHT.containsKey(word.getWordType())){
            return TYPE_WEIGHT.get(word.getWordType());
        }

        return DEFAULT_INFLUENT_LEVEL;
    }
}
