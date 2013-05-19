package com.aif.common;

import com.aif.model.Settings;
import com.aif.model.exceptions.NoContentException;
import com.aif.model.memory.short_time.Text;
import com.aif.model.memory.short_time.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ThemeSearcher {

    private static final String TAG = ThemeSearcher.class.getSimpleName();

    private final Word startingPoint;

    private final Word endPoint;

    private final Text text;

    private final List<Word> wordsThatWasVisited = new CopyOnWriteArrayList<>();

    private final ForkJoinPool forkJoinPool = new ForkJoinPool();

    private final List<List<Word>> themes = new CopyOnWriteArrayList<>();

    private static final int NO_MAX_LEVEL = -1;

    public ThemeSearcher(Word startingPoint, Word endPoint, Text text){
        this.startingPoint = startingPoint;
        this.endPoint = endPoint;
        this.text = text;
    }

    public ThemeSearcher(Text text) throws NoContentException {
        List<Word> words = text.getWords();

        if (words.size() < 2)
            throw new NoContentException(TAG + " ThemeSearcher(Text text): words.size() < 2");

        startingPoint = words.get(0);
        endPoint = getSecondWord(words);
        this.text = text;
    }

    public List<Word> getThem(){
        List<Word> startingPath = new ArrayList<>();
        startingPath.add(startingPoint);
        TextWalker walker = new TextWalker(startingPath);
        forkJoinPool.invoke(walker);

        return getBestTheme();
    }

    private Word getSecondWord(List<Word> words){
        Word firstWord = words.get(0);
        for (Word word : words){
            if (word == firstWord)
                continue;

            if (!word.getConnectedWords().contains(firstWord))
                return word;
        }
        return words.get(1);
    }

    private List<Word> getBestTheme(){

        if (themes.size() == 0)
            return new ArrayList<>();


        List<Word> bestThem = themes.get(0);
        double bestThemWeight = Double.MIN_VALUE;
        for (List<Word> them : themes){
            double themWeight = calculateThemWeight(them);
            if (themWeight> bestThemWeight){
                bestThemWeight = themWeight;
                bestThem = them;
            }
        }
        return bestThem;
    }

    private double calculateThemWeight(List<Word> them){

        double themWeight = 0;

        for (int i = 0; i< them.size() - 1;i++){
            Word currentWord = them.get(i);
            Word nextWord = them.get(i+1);
            double  connection = text.getConnectionLevelBetweenWords(currentWord, nextWord);
            connection *= text.getWordWeight(nextWord);
            connection /= 2;
            themWeight += connection;

        }

        return themWeight / (them.size() - 1);

    }

    private class TextWalker extends RecursiveAction{

        private final List<Word> wordsPath;

        private final Word lastWordInPath;

        private final List<Word> nextWords;

        TextWalker(List<Word> wordsPath){
            this.wordsPath = wordsPath;
            lastWordInPath = wordsPath.get(wordsPath.size() - 1);
            nextWords = nextWords();
            if (!wordsThatWasVisited.contains(lastWordInPath))
                wordsThatWasVisited.add(lastWordInPath);
        }

        private double getMaximumConnectionLevel(){
            double maxLevel = ThemeSearcher.NO_MAX_LEVEL;

            Set<Word> nextWords = lastWordInPath.getConnectedWords();
            for (Word word : nextWords){
                if (!wordsThatWasVisited.contains(word)){
                    double conectionLevel = text.getConnectionLevelBetweenWords(lastWordInPath, word);
                    if (conectionLevel > maxLevel)
                        maxLevel = conectionLevel;
                }
            }

            return maxLevel;

        }

        private List<Word> nextWords(){

            double maxConnectionLevel = getMaximumConnectionLevel();
            if (maxConnectionLevel == ThemeSearcher.NO_MAX_LEVEL)
                return new ArrayList<>();

            maxConnectionLevel *= 1 - Settings.DEEP_LEVEL_THEME_SEARCH;

            List<Word> nextWords = new ArrayList<>();
            Set<Word> connectedWords = lastWordInPath.getConnectedWords();
            for (Word word : connectedWords){
                 if (!wordsThatWasVisited.contains(word)
                         && !wordsPath.contains(word)) {
                     double connectionLevel = text.getConnectionLevelBetweenWords(word, lastWordInPath);
                     if (connectionLevel > maxConnectionLevel)
                         nextWords.add(word);
                 }
            }

            return nextWords;
        }

        private boolean isTarget(){

            for (Word word : nextWords){
                if (word.equals(endPoint)){
                    wordsPath.add(word);
                    return true;
                }
            }

            return false;

        }

        @Override
        protected void compute() {
            if (isTarget()){
                themes.add(wordsPath);
            } else {

                if (wordsPath.size() > Settings.MAX_THEME_LENGTH)
                    return;

                List<TextWalker> walkers = new ArrayList<>(nextWords.size());
                for (Word word : nextWords){
                    List<Word> nextWords = new ArrayList<>(wordsPath);
                    nextWords.add(word);
                    TextWalker textWalker = new TextWalker(nextWords);
                    walkers.add(textWalker);
                }
                invokeAll(walkers);
            }
        }
    }

}
