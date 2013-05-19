package com.aif.model.memory.short_time;

import com.aif.common.WordComparator;
import com.aif.model.Settings;
import com.aif.model.exceptions.NoContentException;

import java.util.*;

/**
 * Class Word is class that store each word from Text
 * Common Text Mining processor works only with this class
 */
public class Word {

    private static final String TAG = Word.class.getSimpleName();

    private final SortedSet<String> wordFormsStrings = new TreeSet<>(new SizeStringComparator());

    private final List<Double> compareLevelList = new ArrayList<>();

    private final WordComparator wordComparator = WordComparator.getInstance();

    private final Map<Word, WordsConnection> connections = new HashMap<>();

    private final Set<WordForm> wordForms = new HashSet<>();

    private long count = 1;

    private Type wordType = Type.SIMPL;

    public enum Type {
        OBJECT,
        SIMPL
    }
    
    public Set<String> getWordFormsStrings() {
    	return wordFormsStrings;
    }

    /**
     * Create and return copy of String that Word store
     * @return copy of baseWord
     */
    @Override
    public String toString(){
       return getOriginalWord();
    }

    /**
     *
     * using default String hashCode method for generating hash
     * @see String.hashCode()
     *
     * @return toString().hashCode()
     */
    @Override
    public int hashCode(){
        return toString().hashCode();
    }

    /**
     *
     * Calculating equals to String or Word in other cases retur false
     * using method from WordComparator class
     * @see com.aif.common.WordComparator
     * @see com.aif.common.WordComparator.distanceBetweenWords(Word, Word)
     * @see com.aif.common.WordComparator.distanceBetweenWords(String, String)
     * @see Word.COMPARE_MAX_LEVEL
     *
     * @return true if words is equals
     */
    @Override
    public boolean equals(Object word){

        if (word == this)
            return true;

        if (word instanceof Word){

            if (word == this)
                return true;

            return equals(word.toString());
        }

        if (word instanceof String){

            String wordString = (String) word;
            String originalString = wordString;
            if (originalString.equals(toString()))
                return true;
            wordString = Word.clearString(wordString);
            int lengthDifferens = Math.abs(toString().length() - wordString.length());
            int minSizeWord = toString().length() > wordString.length()
                    ? wordString.length()
                    : toString().length();
            double compareDelta = (double)lengthDifferens / (double)minSizeWord;
            if (compareDelta >= 1 - Settings.WORDS_COMPARE_MAX_LEVEL)
                return false;

            for (String wordStr : wordFormsStrings){
                if (wordStr.equals(wordString))
                    return true;
            }

            double distance = wordComparator.distanceBetweenWords(toString(), wordString);
            if (distance > Settings.WORDS_COMPARE_MAX_LEVEL){

                if (!compareLevelList.contains(distance))
                    compareLevelList.add(distance);

                wordType = getWordType(originalString);

                addNewWordForm(wordString);
                parsWordForm(wordString);

                return true;
            }
        }

        return false;
    }

    public static String clearString(String strForCleaning){
        strForCleaning = strForCleaning.trim();
        StringBuilder sb = new StringBuilder(strForCleaning.length());
        for (Character ch : strForCleaning.toCharArray()){
            if (Character.isAlphabetic(ch)
                    || Character.isDigit(ch))
                sb.append(ch);
        }
        return sb.toString();
    }

    public Type getWordType() {
        return wordType;
    }

    public long getCount(){
        return count;
    }

    public double getAbstractoinLevel(){

        if (compareLevelList.size() == 0)
            return 0;

        double abstractionLeve = 0;

        for (Double compareLevel : compareLevelList){
            abstractionLeve += (1 - compareLevel);
        }

        abstractionLeve /= compareLevelList.size();

        return abstractionLeve;
    }

    public Set<Word> getConnectedWords(){
        return connections.keySet();
    }

    public String getOriginalWord(){
        return getWordFormsStrings().iterator().next();
    }

    public long getConnectionCount(Word word){

        if (word == null)
            return 0l;

        WordsConnection wordsConnection = connections.get(word);
        if (wordsConnection != null)
            return wordsConnection.getCount();
        else
            return 0l;
    }

    public String applyWordForm(WordForm wordForm){
        if (!wordForms.contains(wordForm))
            return null;

        for (String wordFormString : wordFormsStrings){
            if (wordForm.isWordInWordForm(wordFormString))
                return wordFormString;
        }

        return null;
    }

    WordsConnection getConnection(Word word){
        return connections.get(word);
    }

    void refreshConnection(Word toWord, WordsConnection wordConnection){
        connections.put(toWord, wordConnection);
    }

    WordsConnection addConnection(Word toWord, double distance){
        if (!connections.containsKey(toWord)){
            connections.put(toWord, new WordsConnection(distance));
        } else {
            WordsConnection connection = connections.get(toWord);
            connection.addDistance(distance);
            connection.increaseCount();
        }
        return connections.get(toWord);
    }

    void increaseCount(){
        count++;
    }

    Word(String word) throws NoContentException {

        if (word == null || word.length() < Settings.MINIMUM_WORD_LENGTH)
            throw new NoContentException(TAG + " Word(String word): word == null || word.length() < MINIMUM_WORD_LENGTH");

        setWordFromString(word);
    }

    private void parsWordForm(String newWord){

        if (Settings.sCurrentWordFormMode
                == Settings.WordFormMode.IGNORE_WORD_FORM)
            return;

        WordsFormStorage wordsFormStorage = WordsFormStorage.getInstance();

        WordForm wordForm = wordsFormStorage.getWordForm(getOriginalWord(), newWord);
        if (wordForm != null)
            wordForms.add(wordForm);
    }

    private void setWordFromString(String word){

        wordType = getWordType(word);
        wordFormsStrings.add(Word.clearString(word));
    }

    private void addNewWordForm(Word newWordForm){
        addNewWordForm(newWordForm.toString());
    }

    private void addNewWordForm(String newWordForm){
        if (newWordForm == null || newWordForm.length() < 2)
            return;

        newWordForm = Word.clearString(newWordForm);

        synchronized (wordFormsStrings){
            if (wordFormsStrings.contains(newWordForm))
                return;

            wordFormsStrings.add(newWordForm);
        }
    }

    private Type getWordType(String str){

        str = str.trim();
        if (str.length() < 2)
            return Type.SIMPL;

        if (Character.isAlphabetic(str.charAt(0))
                && Character.isUpperCase(str.charAt(0))){
            return Type.OBJECT;
        }

        return Type.SIMPL;
    }

    class WordsConnection{

        private long count = 1;

        private double distance;

        public WordsConnection(double distance){
            this.distance = distance;
        }

        public long getCount(){
            return count;
        }

        public void increaseCount(){
            count++;
        }

        public double getDistance(){
            return distance + 1;
        }

        public void addDistance(double distance){
            this.distance = (this.distance + distance) / 2;
        }

    }

    private class SizeStringComparator implements Comparator<String>{

        @Override
        public int compare(String o1, String o2) {
            return o1.length() - o2.length();
        }

    }

}
