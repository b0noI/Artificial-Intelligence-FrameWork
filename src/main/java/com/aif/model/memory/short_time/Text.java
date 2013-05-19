package com.aif.model.memory.short_time;

import com.aif.common.TextSaver;
import com.aif.common.ThemeSearcher;
import com.aif.model.exceptions.InputOpenException;
import com.aif.model.exceptions.NoContentException;
import com.aif.model.weight.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class Text {

    private static final String TAG = Text.class.getSimpleName();

    private static final String REGEXP_EPLITTER = "[\\\"\\.!?\\[\\];\\(\\)][ ]";

    private final List<Sentence> textSentences = new CopyOnWriteArrayList<>();

    private final ForkJoinPool forkJoinPool = new ForkJoinPool();

    private final WordsStorage wordsStorage = new WordsStorage();

    private final List<AbstractWeightItem> wordWeights = new ArrayList<>();

    private List<Word> wordsInText = null;

    private List<Word> allObjects = null;

    public Text(Reader inputText){
        parsInputReader(inputText);
    }

    public Text(String textFilePath) throws InputOpenException {
        try{
            parsInputReader(new FileReader(textFilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new InputOpenException(TAG+" Text(String textFilePath)", e);
        }
    }

    @Deprecated
    // please use getSentences()
    public int getSentencesCount(){
        return textSentences.size();
    }

    public List<Sentence> getSentences(){
        return textSentences;
    }

    public double getSentencesWeight(Sentence sentence){
        double weight = 0;
        for (Word word : sentence.getSentenceWords()){
            weight += getWordWeight(word);
        }
        return weight / (double) sentence.getSentenceWords().size();
    }

    public double getConnectionLevelBetweenWords(String wordStr1, String wordStr2) throws NoContentException {
        Word word1 = wordsStorage.getWordFromString(wordStr1);
        Word word2 = wordsStorage.getWordFromString(wordStr2);
        return getConnectionLevelBetweenWords(word1, word2);
    }

    public double getConnectionLevelBetweenWords(Word word1, Word word2){
        Word.WordsConnection wordsConnection = word1.getConnection(word2);
        if (wordsConnection == null)
            return 0;
        long usageCount = wordsConnection.getCount();
        double exp = (double)usageCount / (double)(word1.getCount() + word2.getCount() - usageCount);
        exp = Math.exp(exp);
        double a = (getWordWeight(word1) + getWordWeight(word2))
                / (wordsConnection.getDistance()*2);
        return - ((a * exp) * (Math.log(a * exp) / Math.log(2)));
    }

    public double getDistanceBetweenWords(Word word1, Word word2){
        Word.WordsConnection wordsConnection = word1.getConnection(word2);
        return wordsConnection.getDistance()*2;
    }

    public double getWordWeight(String wordStr) throws NoContentException {
        Word word = wordsStorage.getWordFromString(wordStr);
        return getWordWeight(word);
    }

    public double getWordWeight(Word word){
        double wordWeight = 1;
        for (AbstractWeightItem weightItem : wordWeights){
            wordWeight *= (weightItem.calculateWeight(word) * weightItem.getInfluent());
        }
        return wordWeight;
    }

    public List<Word> getWords(){

        if (wordsInText != null)
            return wordsInText;

        List<Word> words = new ArrayList<>(wordsStorage.getAllWord());
        Collections.sort(words, new WordCompratorByWeight());
        wordsInText = words;
        return words;
    }

    public List<Word> getObjects(){

        if (allObjects != null)
            return allObjects;

        List<Word> allWordsInText = getWords();
        List<Word> allObjectsInText = new ArrayList<>();

        for (Word word : allWordsInText){
            if (word.getWordType() == Word.Type.OBJECT)
                allObjectsInText.add(word);
        }

        allObjects = allObjectsInText;
        return allObjectsInText;
    }

    public List<Word> getThem(){
        ThemeSearcher themeSearcher;
        try {
            themeSearcher = new ThemeSearcher(this);
        } catch (NoContentException e) {
            e.printStackTrace();
            return getWords();
        }
        return themeSearcher.getThem();
    }

    public void save(){
        new TextSaver(this).saveTextDataToDB();
    }

    private void parsInputReader(Reader inputReader){
        parsInputText(new BufferedReader(inputReader));

        prepareWordsWeight();
    }

    private class WordCompratorByWeight implements Comparator<Word>{

        @Override
        public int compare(Word word1, Word word2) {
            double word1weight = getWordWeight(word1);
            double word2weight = getWordWeight(word2);
            double deltaWeight = word2weight - word1weight;

            if (deltaWeight == 0)
                return 0;

            while (deltaWeight < 1
                    && deltaWeight > -1)
                deltaWeight *= 10;
            return (int)(deltaWeight);
        }

    }

    private class StringParsingTask extends RecursiveAction{

        private final String[] sentencesStrings;

        private final int from;

        private final int to;

        private StringParsingTask(String[] sentsStrings, int from, int to){
            sentencesStrings = sentsStrings;
            this.from = from;
            this.to = to;
        }

        private void computeDirectly(int n){
            try {
                textSentences.set(n, Sentence.generateSentence(sentencesStrings[n], wordsStorage));
            } catch (NoContentException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void compute() {
            if (to == from + 1){
                computeDirectly(from);
                return;
            }

            int split = (from + to) / 2;
            StringParsingTask left = new StringParsingTask(sentencesStrings, from, split);
            StringParsingTask right = new StringParsingTask(sentencesStrings, split, to);
            invokeAll(left);
            right.compute();
        }
    }

    private void parsInputText(BufferedReader inputText) {

        List<String> sentanses = new ArrayList<>();
        try (Scanner inputScanner = new Scanner(inputText)){
            inputScanner.useDelimiter(Text.REGEXP_EPLITTER);

            while (inputScanner.hasNext()){
                sentanses.add(inputScanner.next());
            }
        }
        StringParsingTask stringParsingTask =
                new StringParsingTask(sentanses.toArray(new String[0]), 0, sentanses.size());
        for (int i=0; i<sentanses.size(); i++)
            textSentences.add(null);
        forkJoinPool.invoke(stringParsingTask);
    }

    private void prepareWordsWeight(){
        wordWeights.add(new ShenonWeight(wordsStorage));
        wordWeights.add(new ConnectionWeight(wordsStorage));
        wordWeights.add(new AbstractionWeight(wordsStorage));
        wordWeights.add(new ObjectWeight());
    }

}
