package com.aif.common;

import com.aif.model.memory.short_time.Word;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that implement different Word comparing method
 * You can work with this class only via singelton access:
 * WordComparator compare = WordComparator.getInstance();
 *
 * @see model.memory.short_time.Word
 */
public class WordComparator implements Comparator<Word>{

    /**
     *
     * For getting WordComparator object just call this method
     * EXAMPLE:
     * WordComparator compare = WordComparator.getInstance();
     *
     * @return WordComparator instance
     */
    public static WordComparator getInstance(){
        return SingeltonHolder.SINGELTON;
    }

    /**
     * Calculating distance between word1 and word2 using recursive and symbol distance
     *
     * distanceBetweenWords(word1, word2)
     * ==
     * (recursiveDistanceBetweenWords(word1, word2) +
     *      symbolDistanceBetweenWords(word1, word2)) / 2;
     *
     * @see distanceBetweenWords(String word1, String word2)
     *
     * @return distance between words
     */
    public double distanceBetweenWords(Word word1, Word word2){
        return distanceBetweenWords(word1.toString(), word2.toString());
    }

    /**
     * Calculating distance between word1 and word2 using recursive and symbol distance
     *
     * distanceBetweenWords(word1, word2)
     * ==
     * (recursiveDistanceBetweenWords(word1, word2) +
     *      symbolDistanceBetweenWords(word1, word2)) / 2;
     *
     * @see distanceBetweenWords(Word word1, Word word2)
     *
     * @return distance between words
     */
    public double distanceBetweenWords(String word1, String word2){

        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();

        return
                (recursiveDistanceBetweenWords(word1, word2) +
                        symbolDistanceBetweenWords(word1, word2)) / 2;
    }

    /**
     * method calculating symbol distance between words word1 and word2
     * returning value [0, 1]:
     * from 0 to 1 when
     * 0 - word1 not equal word2 at all
     * 1 - word1 same as word2
     *
     * if
     * recursiveDistanceBetweenWords(word1, word2) == 1
     * then
     * word1.equal(word2) == true
     *
     * SEE:
     * @see symbolDistanceBetweenWords(String word1, String word2)
     *
     * @return distance between words word1 and word2
     */
    public double symbolDistanceBetweenWords(Word word1, Word word2){
         return symbolDistanceBetweenWords(word1.toString(), word2.toString());
    }

    /**
     * method calculating symbol distance between words word1 and word2
     * returning value [0, 1]:
     * from 0 to 1 when
     * 0 - word1 not equal word2 at all
     * 1 - word1 same as word2
     *
     * if
     * recursiveDistanceBetweenWords(word1, word2) == 1
     * then
     * word1.equal(word2) == true
     *
     * SEE:
     * @see symbolDistanceBetweenWords(Word word1, Word word2)
     *
     * @return distance between words word1 and word2
     */
    public double symbolDistanceBetweenWords(String word1, String word2){

        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();

        Map<Character, Integer> symbolsWord1 = new HashMap<>();
        Map<Character, Integer> symbolsWord2 = new HashMap<>();

        for (Character ch : word1.toLowerCase().toCharArray()){
            if (symbolsWord1.containsKey(ch))
                symbolsWord1.put(ch, symbolsWord1.get(ch) + 1);
            else
                symbolsWord1.put(ch, 1);
        }

        for (Character ch : word2.toLowerCase().toCharArray()){
            if (symbolsWord2.containsKey(ch))
                symbolsWord2.put(ch, symbolsWord2.get(ch) + 1);
            else
                symbolsWord2.put(ch, 1);
        }

        int sameSymbolsCount = 0;

        for (Character key : symbolsWord1.keySet()){
            if (symbolsWord2.containsKey(key)){
                sameSymbolsCount += symbolsWord1.get(key) > symbolsWord2.get(key) ?
                        symbolsWord2.get(key) : symbolsWord1.get(key);
            }
        }

        return ((double)sameSymbolsCount * 2d) / (word1.length() + word2.length());

    }

    /**
     * method calculating recursive distance between words word1 and word2
     * returning value [0, 1]:
     * from 0 to 1 when
     * 0 - word1 not equal word2 at all
     * 1 - word1 same as word2
     *
     * if
     * recursiveDistanceBetweenWords(word1, word2) == 1
     * then
     * word1.equal(word2) == true
     *
     * SEE:
     * @see recursiveDistanceBetweenWords(Word word1, Word word2)
     *
     * @return distance between words word1 and word2
     */
	public double recursiveDistanceBetweenWords(Word word1, Word word2){
         return recursiveDistanceBetweenWords(word1.toString(), word2.toString());
    }

    /**
     * method calculating recursive distance between words word1 and word2
     * returning value [0, 1]:
     * from 0 to 1 when
     * 0 - word1 not equal word2 at all
     * 1 - word1 same as word2
     *
     * if
     * recursiveDistanceBetweenWords(word1, word2) == 1
     * then
     * word1.equal(word2) == true
     *
     * SEE:
     * @see recursiveDistanceBetweenWords(Word word1, Word word2)
     *
     * @return distance between words word1 and word2
     */
    public double recursiveDistanceBetweenWords(String word1, String word2){

        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();

        int sameSimbols = simbolsCountInSameStrings(word1.toLowerCase(), word2.toLowerCase());
        sameSimbols *= 2;

        return (double)sameSimbols / (word1.length() + word2.length());
    }

    /**
     * this method return longest common substring in word1 and word2
     * it is case sensative
     *
     * EXAMPLE:
     * Word word1 = new Word("aabbcc");
     * Word word2 = new Word("eebbdd");
     * new WordComparator.longestSubstring(word1, word2); // will return "bb";
     *
     * NOTE:
     * if word1 or word2 is null will return empty String;
     * this method was copy/pasted from {@link http://karussell.wordpress.com/2011/04/14/longest-common-substring-algorithm-in-java/}
     * 
     * SEE:
     * @see longestSubstring(String str1, String str2)
     *
     * @return String (longest common substring in words)
     */
    public String longestSubstring(Word word1, Word word2) {

        String str1 = word1.toString();
        String str2 = word2.toString();

        return longestSubstring(str1, str2);
    }
    
    /**
     * this method return longest common substring in str1 and str2
     * it is case sensative
     *
     * EXAMPLE:
     * String str1 = new String("aabbcc");
     * String str2 = new String("eebbdd");
     * new WordComparator.longestSubstring(str1, str2); // will return "bb";
     *
     * NOTE:
     * if str1 or str2 is null will return empty String;
     * this method was copy/pasted from {@link http://karussell.wordpress.com/2011/04/14/longest-common-substring-algorithm-in-java/}
     * 
     * SEE:
     * @see longestSubstring(Word word1, Word word2)
     *
     * @return String (longest common substring in words)
     */
    public static String longestSubstring(String str1, String str2) {

        str1 = str1.toLowerCase();
        str2 = str2.toLowerCase();

    	int[][] num = new int[str1.length()][str2.length()];
        int maxlen = 0;
        int lastSubsBegin = 0;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < str1.length(); i++) {
            for (int j = 0; j < str2.length(); j++) {
                if (str1.charAt(i) == str2.charAt(j)) {
                    if ((i == 0) || (j == 0))
                        num[i][j] = 1;
                    else
                        num[i][j] = 1 + num[i - 1][j - 1];

                    if (num[i][j] > maxlen) {
                        maxlen = num[i][j];
                        // generate substring from str1 => i
                        int thisSubsBegin = i - num[i][j] + 1;
                        if (lastSubsBegin == thisSubsBegin) {
                            //if the current LCS is the same as the last time this block ran
                            sb.append(str1.charAt(i));
                        } else {
                            //this block resets the string builder if a different LCS is found
                            lastSubsBegin = thisSubsBegin;
                            sb = new StringBuilder();
                            sb.append(str1.substring(lastSubsBegin, i + 1));
                        }
                    }
                }
            }}
        return sb.toString();
    }

    @Override
    public int compare(Word w1, Word w2) {
        return w1.toString().compareTo(w2.toString());
    }

    private String getLeftString(String str1, String str2){
        String splitter = longestSubstring(str1, str2);
        splitter = splitter.replace(".","");
        splitter = splitter.replace("(","");
        splitter = splitter.replace(")","");
        splitter = splitter.replace("[","");
        splitter = splitter.replace("]","");
        splitter = splitter.replace("?","");
        String[] strings = str1.split(splitter);

        if (strings.length == 0)
            return "";
        else
            return strings[0].intern();
    }

    private String getRightString(String str1, String str2){

        String splitter = longestSubstring(str1, str2);

        splitter = splitter.replace(".","");
        splitter = splitter.replace("(","");
        splitter = splitter.replace(")","");
        splitter = splitter.replace("[","");
        splitter = splitter.replace("]","");
        splitter = splitter.replace("?","");

        String all[] = str1.split(splitter);
        if (all.length < 2)
            return "";

        return all[1].intern();
    }

    private int simbolsCountInSameStrings(String str1, String str2){
        if (str1.equals(str2))
            return str1.length();

        if (str1.contains(str2))
            return str2.length();

        if (str2.contains(str1))
            return str1.length();

        String substring = longestSubstring(str1, str2);
        if (substring.length() == 0)
            return 0;

        return substring.length() +
                simbolsCountInSameStrings(getLeftString(str1, str2), getLeftString(str2, str1)) +
                simbolsCountInSameStrings(getRightString(str1, str2), getRightString(str2, str1));

    }

    private WordComparator(){}

    private static class SingeltonHolder{

        static final WordComparator SINGELTON = new WordComparator();

    }

}