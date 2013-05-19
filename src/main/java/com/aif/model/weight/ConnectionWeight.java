package com.aif.model.weight;

import com.aif.model.memory.short_time.Word;
import com.aif.model.memory.short_time.WordsStorage;

/**
 * User: b0noI
 * Date: 24.11.12
 * Time: 21:34
 */
public class ConnectionWeight extends AbstractWeightItem {

    private static final    String  TAG                 = ConnectionWeight.class.getSimpleName();

    private static final    int     VALUE_NOT_SET       = -1;

    private static final    double  INFLUENT_LEVEL      = 1.0;

    private                 long    maxConnectionSize   = VALUE_NOT_SET;

    private WordsStorage wordsStorage;

    public ConnectionWeight(WordsStorage wordsStorage){
        if (wordsStorage == null)
            throw new NullPointerException(TAG + ": ConnectionWeight(WordsStorage wordsStorage, Text text): wordsStorage is null");

        this.wordsStorage = wordsStorage;
    }

    @Override
    public double calculateWeight(Word word) {
        return (1.1 - ((double)word.getConnectedWords().size() / (double) wordsStorage.getMaxConnectionCount())) *
                (1.1 - ((double)getWordMaximumConnection(word)/(double)getMaxConnectionSize()));
    }

    @Override
    public double getInfluent() {
        return INFLUENT_LEVEL;
    }

    private long getMaxConnectionSize(){

        if (maxConnectionSize != VALUE_NOT_SET)
            return maxConnectionSize;

        long maxConnectionLevel = 0;
        for (Word word : wordsStorage.getAllWord()){

            long connectionLevel = getWordMaximumConnection(word);
            if (connectionLevel > maxConnectionLevel)
                maxConnectionLevel = connectionLevel;

        }
        maxConnectionSize = maxConnectionLevel;
        return maxConnectionLevel;
    }

    private long getWordMaximumConnection(Word word){

        long maxConnectionLevel = 0;
        for (Word connectedWord : word.getConnectedWords()){
            maxConnectionLevel += word.getConnectionCount(connectedWord);
        }

        return maxConnectionLevel;
    }

}
